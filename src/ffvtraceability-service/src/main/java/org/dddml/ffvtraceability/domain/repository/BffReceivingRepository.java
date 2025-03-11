package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.shipmentreceipt.AbstractShipmentReceiptState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BffReceivingRepository extends JpaRepository<AbstractShipmentReceiptState.SimpleShipmentReceiptState, String> {

    String COMMON_SELECT = """
            SELECT 
                s.shipment_id as documentId,
                s.status_id as statusId,
                s.qa_status_id as qaStatusId,
                s.party_id_to as partyIdTo,
                s.party_id_from as partyIdFrom,
                COALESCE(p.short_description,p.organization_name, p.last_name) as partyNameFrom,
                s.origin_facility_id as originFacilityId,
                f.facility_name as originFacilityName,
                s.destination_facility_id as destinationFacilityId,
                d.facility_name as destinationFacilityName,
                s.primary_order_id as primaryOrderId,
                s.created_at as createdAt,
                s.created_by as createdBy,
                s.received_by as receivedBy,
                s.datetime_received as receivedAt,
                sr.receipt_id as receiptId,
                sr.product_id as productId,
                prod.product_name as productName,
                prod.small_image_url as smallImageUrl,
                prod.quantity_uom_id as quantityUomId,
                prod.case_uom_id as caseUomId,
                prod.quantity_included as quantityIncluded,
                prod.pieces_included as piecesIncluded,
                gi.id_value as gtin,
                ii.id_value as internalId,
                qi.inspected_by as inspectedBy,
                qi.comments as comments,
                sr.lot_id as lotId,
                sr.location_seq_id as locationSeqId,
                fl.location_name as locationName,
                sr.item_description as itemDescription,
                sr.quantity_accepted as quantityAccepted,
                sr.quantity_rejected as quantityRejected,
                sr.cases_accepted as casesAccepted,
                sr.cases_rejected as casesRejected,
                sr.datetime_received as datetimeReceived,
                sr.order_id as orderId,
                sr.order_item_seq_id as orderItemSeqId,
                sr.return_id as returnId,
                sr.return_item_seq_id as returnItemSeqId,
                sr.rejection_id as rejectionId,
                sr.shipment_id as shipmentId,
                sr.shipment_item_seq_id as shipmentItemSeqId,
                sr.shipment_package_seq_id as shipmentPackageSeqId
            """;

    String COMMON_JOINS = """
            LEFT JOIN party p ON s.party_id_from = p.party_id
            LEFT JOIN facility f ON s.origin_facility_id = f.facility_id
            left join facility d on s.destination_facility_id = d.facility_id
            left join facility_location fl on sr.location_seq_id = fl.location_seq_id 
                                           and s.destination_facility_id= fl.facility_id
            LEFT JOIN product prod ON sr.product_id = prod.product_id
            left join qa_inspection qi on qi.receipt_id = sr.receipt_id
            LEFT JOIN good_identification gi ON prod.product_id = gi.product_id 
                AND gi.good_identification_type_id = 'GTIN'
            LEFT JOIN good_identification ii ON prod.product_id = ii.product_id 
                 AND ii.good_identification_type_id = 'INTERNAL_ID'
            """;

    String RECEIPT_JOIN = """
            LEFT JOIN shipment_receipt sr ON s.shipment_id = sr.shipment_id 
                AND (sr.deleted IS NULL OR sr.deleted = false)
            """;

    /**
     * SQL 片段常量，用于计算 shipment 的 QA 检验状态。
     * 通过分析 shipment 关联的所有 shipment_receipt 的 qa_inspection 状态，得出整体的检验状态：
     * - 'INSPECTED': 所有收货行项都已完成检验 (APPROVED 或 REJECTED，甚至包括 ON_HOLD)
     * - 'PARTIALLY_INSPECTED': 部分收货行项已完成检验，部分未完成
     * - 'PENDING_INSPECTION': 没有收货行项，或所有收货行项都未完成检验
     */
    String QA_INSPECTION_STATUS_SELECT = """
            SELECT
                CASE
                    WHEN COUNT(sr.receipt_id) = 0 THEN 'PENDING_INSPECTION'
                    WHEN COUNT(sr.receipt_id) = COUNT(CASE WHEN qi.status_id IN ('APPROVED', 'REJECTED', 'ON_HOLD') THEN 1 END) 
                        THEN 'INSPECTED'
                    WHEN COUNT(CASE WHEN qi.status_id IN ('APPROVED', 'REJECTED', 'ON_HOLD') THEN 1 END) > 0 
                        THEN 'PARTIALLY_INSPECTED'
                    ELSE 'PENDING_INSPECTION'
                END
            FROM shipment s
            LEFT JOIN shipment_receipt sr ON s.shipment_id = sr.shipment_id 
                AND (sr.deleted IS NULL OR sr.deleted = false)
            LEFT JOIN qa_inspection qi ON sr.receipt_id = qi.receipt_id
            """;

    @Query(value = """
            WITH filtered_shipments AS (
                SELECT DISTINCT s.shipment_id, s.created_at
                FROM shipment s
            """ + RECEIPT_JOIN + """
                LEFT JOIN product prod ON sr.product_id = prod.product_id
                LEFT JOIN good_identification gi ON prod.product_id = gi.product_id 
                    AND gi.good_identification_type_id = 'GTIN'
                LEFT JOIN good_identification ii ON prod.product_id = ii.product_id 
                    AND ii.good_identification_type_id = 'INTERNAL_ID'
                WHERE (:documentIdOrItem IS NULL 
                    OR s.shipment_id LIKE CONCAT(:documentIdOrItem, '%')
                    OR sr.product_id LIKE CONCAT(:documentIdOrItem, '%')
                    OR gi.id_value LIKE CONCAT(:documentIdOrItem, '%'))
                    AND (:facilityId IS NULL OR s.destination_facility_id = :facilityId)
                    AND (:supplierId IS NULL OR s.party_id_from = :supplierId)
                    AND (CAST(:receivedAtFrom AS timestamptz) IS NULL OR s.created_at >= :receivedAtFrom)
                    AND (CAST(:receivedAtTo AS timestamptz) IS NULL OR s.created_at <= :receivedAtTo)
                ORDER BY s.created_at DESC
                LIMIT :pageSize OFFSET :offset
            )
            """ + COMMON_SELECT + """
            FROM filtered_shipments fs
            JOIN shipment s ON fs.shipment_id = s.shipment_id
            """ + RECEIPT_JOIN + COMMON_JOINS + """
            ORDER BY s.created_at DESC
            """, nativeQuery = true)
    List<BffReceivingDocumentItemProjection> findAllReceivingDocumentsWithItems(
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("documentIdOrItem") String documentIdOrItem,
            @Param("facilityId") String facilityId,
            @Param("supplierId") String supplierId,
            @Param("receivedAtFrom") OffsetDateTime receivedAtFrom,
            @Param("receivedAtTo") OffsetDateTime receivedAtTo
    );

    //
    // NOTE：
    // 关于使用 OffsetDateTime 类型的查询参数，参考：https://blog.mimacom.com/java-8-dates-with-postgresql/
    //

    @Query(value = """
            SELECT COUNT(DISTINCT s.shipment_id)
            FROM shipment s
            LEFT JOIN shipment_receipt sr ON s.shipment_id = sr.shipment_id
            LEFT JOIN product prod ON sr.product_id = prod.product_id
            LEFT JOIN good_identification gi ON prod.product_id = gi.product_id 
                AND gi.good_identification_type_id = 'GTIN'
            WHERE (:documentIdOrItem IS NULL OR
                  s.shipment_id LIKE CONCAT(:documentIdOrItem, '%') OR
                  sr.product_id LIKE CONCAT(:documentIdOrItem, '%') OR
                  gi.id_value LIKE CONCAT(:documentIdOrItem, '%'))
                  AND (:facilityId IS NULL OR s.destination_facility_id = :facilityId)
                  AND (:supplierId IS NULL OR s.party_id_from = :supplierId)
                  AND (CAST(:receivedAtFrom AS timestamptz) IS NULL OR s.created_at >= :receivedAtFrom)
                  AND (CAST(:receivedAtTo AS timestamptz) IS NULL OR s.created_at <= :receivedAtTo)
            """, nativeQuery = true)
    long countTotalShipments(@Param("documentIdOrItem") String documentIdOrItem,
                             @Param("facilityId") String facilityId,
                             @Param("supplierId") String supplierId,
                             @Param("receivedAtFrom") OffsetDateTime receivedAtFrom,
                             @Param("receivedAtTo") OffsetDateTime receivedAtTo
    );

    @Query(value = COMMON_SELECT + """
            FROM shipment s
            """ + RECEIPT_JOIN + COMMON_JOINS + """
            WHERE s.shipment_id = :documentId
            """, nativeQuery = true)
    List<BffReceivingDocumentItemProjection> findReceivingDocumentWithItems(@Param("documentId") String documentId);

    @Query(value = """
            SELECT 
                s.shipment_id as documentId,
                sr.receipt_id as receiptId,
                sr.product_id as productId,
                prod.product_name as productName,
                prod.small_image_url as smallImageUrl,
                gi.id_value as gtin,
                sr.lot_id as lotId,
                sr.location_seq_id as locationSeqId,
                sr.item_description as itemDescription,
                sr.quantity_accepted as quantityAccepted,
                sr.quantity_rejected as quantityRejected,
                sr.cases_accepted as casesAccepted,
                sr.cases_rejected as casesRejected,
                sr.datetime_received as datetimeReceived
            FROM shipment s
            """ + RECEIPT_JOIN + COMMON_JOINS + """
            WHERE sr.shipment_id = :documentId
                AND sr.receipt_id = :receiptId
            """, nativeQuery = true)
    BffReceivingDocumentItemProjection findReceivingItem(
            @Param("documentId") String documentId,
            @Param("receiptId") String receiptId);

    @Query(value = "SELECT " +
            "   sd.shipment_id as documentId, " +
            "   d.document_id as referenceDocumentId, " +
            "   d.comments as referenceComments, " +
            "   d.document_location as referenceDocumentLocation, " +
            "   d.document_text as referenceDocumentText " +
            "FROM shipping_document sd " +
            "JOIN document d ON sd.document_id = d.document_id " +
            "WHERE sd.shipment_id IN :shipmentIds " +
            "   AND (sd.deleted IS NULL OR sd.deleted = false)",
            nativeQuery = true)
    List<BffReceivingDocumentItemProjection> findReferenceDocumentsByShipmentIds(@Param("shipmentIds") List<String> shipmentIds);

    @Query(value = COMMON_SELECT + """
            FROM shipment s
            """ + RECEIPT_JOIN + COMMON_JOINS + """
            WHERE (sr.order_id = :orderId) OR 
                (sr.order_id IS NULL AND s.primary_order_id = :orderId)
            """,
            nativeQuery = true)
    List<BffReceivingDocumentItemProjection> findReceivingItemsByOrderId(@Param("orderId") String orderId);

    @Query(value = """
            SELECT
                oa.receipt_id as receiptId,
                oa.quantity_allocated as allocatedQuantity,
                sr1.datetime_received as receivedAt,
                s1.shipment_id as shipmentId,
                s1.qa_status_id as shipmentQaStatusId,
                s1.status_id as shipmentStatusId,
                (""" + QA_INSPECTION_STATUS_SELECT + """
                    WHERE s.shipment_id = s1.shipment_id
                    GROUP BY s.shipment_id
                ) as shipmentQaInspectionStatusId
            FROM shipment_receipt_order_allocation oa
            JOIN shipment_receipt sr1 ON oa.receipt_id = sr1.receipt_id
            JOIN shipment s1 ON sr1.shipment_id = s1.shipment_id
            WHERE oa.order_id = :orderId 
                AND oa.order_item_seq_id = :orderItemSeqId
            """,
            nativeQuery = true)
    List<BffPurchaseOrderFulfillmentProjection> findPurchaseOrderItemFulfillments(
            @Param("orderId") String orderId,
            @Param("orderItemSeqId") String orderItemSeqId
    );

    /**
     * 根据收货单 ID 查询其 QA 检验状态
     *
     * @param shipmentId 收货单 ID (shipment_id)
     * @return 返回 QA 检验状态：
     * - 'INSPECTED': 所有收货行项都已完成检验
     * - 'PARTIALLY_INSPECTED': 部分收货行项已完成检验
     * - 'PENDING_INSPECTION': 未开始检验或检验中
     */
    @Query(value = QA_INSPECTION_STATUS_SELECT + """
            WHERE s.shipment_id = :shipmentId
            GROUP BY s.shipment_id
            """, nativeQuery = true)
    Optional<String> findQaInspectionStatusByShipmentId(@Param("shipmentId") String shipmentId);

    /**
     * 根据多个收货单 ID 批量查询其 QA 检验状态
     */
    @Query(value = """
            SELECT 
                outer_s.shipment_id as documentId,
                (""" + QA_INSPECTION_STATUS_SELECT + """
                    WHERE s.shipment_id = outer_s.shipment_id
                    GROUP BY s.shipment_id
                ) as qaInspectionStatusId
            FROM shipment outer_s
            WHERE outer_s.shipment_id IN :shipmentIds
            """, nativeQuery = true)
    List<BffReceivingDocumentProjection> findQaInspectionStatusByShipmentIds(
            @Param("shipmentIds") List<String> shipmentIds
    );

    /**
     * 查询收货行项的 QA 检验状态
     *
     * @param receiptIds 收货行项ID列表
     * @return 返回收货行项的QA检验状态列表，每个状态可能为:
     * - INSPECTION_PASSED: 通过检验 (qa_inspection.status_id = 'APPROVED')
     * - INSPECTION_FAILED: 未通过检验 (qa_inspection.status_id = 'REJECTED')
     * - null: 其他情况
     */
    @Query(value = """
            SELECT 
                sr.receipt_id as receiptId,
                CASE 
                    WHEN qi.status_id = 'APPROVED' THEN 'INSPECTION_PASSED'
                    WHEN qi.status_id = 'REJECTED' THEN 'INSPECTION_FAILED'                    
                    WHEN qi.status_id = 'ON_HOLD' THEN 'INSPECTION_ON_HOLD'
                    ELSE NULL 
                END as qaInspectionStatusId
            FROM shipment_receipt sr
            LEFT JOIN qa_inspection qi ON sr.receipt_id = qi.receipt_id
            WHERE sr.receipt_id IN :receiptIds
            """, nativeQuery = true)
    List<BffReceivingItemProjection> findQaInspectionStatusByShipmentReceiptIds(
            @Param("receiptIds") List<String> receiptIds
    );
}
