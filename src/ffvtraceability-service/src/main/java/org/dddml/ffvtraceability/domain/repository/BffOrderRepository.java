package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.order.AbstractOrderHeaderState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface BffOrderRepository extends JpaRepository<AbstractOrderHeaderState.SimpleOrderHeaderState, String> {

    String COMMON_SELECT = """
            SELECT 
                o.order_id as orderId,
                o.order_name as orderName,
                o.external_id as externalId,
                o.order_type_id as orderTypeId,
                o.status_id as statusId,
                o.order_date as orderDate,
                o.currency_uom_id as currencyUomId,
                o.origin_facility_id as originFacilityId,
                f.facility_name as originFacilityName,
                o.memo as memo,
                o.created_at as createdAt,
                oi.order_item_seq_id as orderItemSeqId,
                oi.product_id as productId,
                prod.product_name as productName,
                gi.id_value as gtin,
                oi.quantity as quantity,
                oi.unit_price as unitPrice,
                oi.item_description as itemDescription,
                oi.comments as comments,
                oi.supplier_product_id as supplierProductId,
                oi.estimated_ship_date as estimatedShipDate,
                oi.estimated_delivery_date as estimatedDeliveryDate,
                orole.party_id as supplierId,
                COALESCE(p.organization_name, p.last_name) as supplierName
            """;

    String COMMON_JOINS = """
            LEFT JOIN order_role orole ON o.order_id = orole.order_id AND orole.role_type_id = 'SUPPLIER'
            LEFT JOIN party p ON orole.party_id = p.party_id
            LEFT JOIN facility f ON o.origin_facility_id = f.facility_id
            LEFT JOIN product prod ON oi.product_id = prod.product_id
            LEFT JOIN good_identification gi ON prod.product_id = gi.product_id 
                AND gi.good_identification_type_id = 'GTIN'
            """;

    String ORDER_ITEM_JOIN = """
            LEFT JOIN order_item oi ON o.order_id = oi.order_id
            """;
    // AND (oi.deleted IS NULL OR oi.deleted = false)

    @Query(value = """
            WITH filtered_orders AS (
                SELECT DISTINCT o.order_id, o.order_date
                FROM order_header o
                """ + ORDER_ITEM_JOIN + """
                LEFT JOIN product prod ON oi.product_id = prod.product_id
                LEFT JOIN good_identification gi ON prod.product_id = gi.product_id 
                    AND gi.good_identification_type_id = 'GTIN'
                WHERE o.order_type_id = 'PURCHASE_ORDER'
                    AND (:orderIdOrItem IS NULL 
                    OR o.order_id LIKE CONCAT(:orderIdOrItem, '%')
                    OR oi.product_id LIKE CONCAT(:orderIdOrItem, '%')
                    OR gi.id_value LIKE CONCAT(:orderIdOrItem, '%'))
                    AND (:supplierId IS NULL OR o.order_id IN (
                        SELECT order_id FROM order_role 
                        WHERE party_id = :supplierId AND role_type_id = 'SUPPLIER'
                    ))
                    AND (CAST(:orderDateFrom AS timestamptz) IS NULL OR o.order_date >= :orderDateFrom)
                    AND (CAST(:orderDateTo AS timestamptz) IS NULL OR o.order_date <= :orderDateTo)
                ORDER BY o.order_date DESC
                LIMIT :pageSize OFFSET :offset
            )
            """ + COMMON_SELECT + """
            FROM filtered_orders fo
            JOIN order_header o ON fo.order_id = o.order_id
            """ + ORDER_ITEM_JOIN + COMMON_JOINS + """
            ORDER BY o.order_date DESC
            """, nativeQuery = true)
    List<BffPurchaseOrderAndItemProjection> findAllPurchaseOrdersWithItems(
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("orderIdOrItem") String orderIdOrItem,
            @Param("supplierId") String supplierId,
            @Param("orderDateFrom") OffsetDateTime orderDateFrom,
            @Param("orderDateTo") OffsetDateTime orderDateTo
    );

    @Query(value = """
            SELECT COUNT(DISTINCT o.order_id)
            FROM order_header o
            LEFT JOIN order_item oi ON o.order_id = oi.order_id
            LEFT JOIN product prod ON oi.product_id = prod.product_id
            LEFT JOIN good_identification gi ON prod.product_id = gi.product_id 
                AND gi.good_identification_type_id = 'GTIN'
            WHERE o.order_type_id = 'PURCHASE_ORDER'
                AND (:orderIdOrItem IS NULL OR
                    o.order_id LIKE CONCAT(:orderIdOrItem, '%') OR
                    oi.product_id LIKE CONCAT(:orderIdOrItem, '%') OR
                    gi.id_value LIKE CONCAT(:orderIdOrItem, '%'))
                AND (:supplierId IS NULL OR o.order_id IN (
                    SELECT order_id FROM order_role 
                    WHERE party_id = :supplierId AND role_type_id = 'SUPPLIER'
                ))
                AND (CAST(:orderDateFrom AS timestamptz) IS NULL OR o.order_date >= :orderDateFrom)
                AND (CAST(:orderDateTo AS timestamptz) IS NULL OR o.order_date <= :orderDateTo)
            """, nativeQuery = true)
    long countTotalShipments(@Param("orderIdOrItem") String orderIdOrItem,
                             @Param("supplierId") String supplierId,
                             @Param("orderDateFrom") OffsetDateTime orderDateFrom,
                             @Param("orderDateTo") OffsetDateTime orderDateTo
    );

    @Query(value = COMMON_SELECT + """
            FROM order_header o
            """ + ORDER_ITEM_JOIN + COMMON_JOINS + """
            WHERE o.order_id = :orderId
            """, nativeQuery = true)
    List<BffPurchaseOrderAndItemProjection> findPurchaseOrderWithItems(@Param("orderId") String orderId);

    @Query(value = """
            SELECT 
                o.order_id as orderId,
                oi.order_item_seq_id as orderItemSeqId,
                oi.product_id as productId,
                prod.product_name as productName,
                gi.id_value as gtin,
                oi.quantity as quantity,
                oi.unit_price as unitPrice,
                oi.item_description as itemDescription,
                oi.comments as comments,
                oi.supplier_product_id as supplierProductId,
                oi.estimated_ship_date as estimatedShipDate,
                oi.estimated_delivery_date as estimatedDeliveryDate
            FROM order_header o
            """ + ORDER_ITEM_JOIN + COMMON_JOINS + """
            WHERE o.order_id = :orderId 
                AND oi.order_item_seq_id = :orderItemSeqId
            """, nativeQuery = true)
    BffPurchaseOrderAndItemProjection findPurchaseOrderItem(
            @Param("orderId") String orderId,
            @Param("orderItemSeqId") String orderItemSeqId);
}
