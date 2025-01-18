package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.product.AbstractProductState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public interface BffRawItemRepository extends JpaRepository<AbstractProductState, String> {

    @Query(value = """
            SELECT
                p.product_id as productId,
                p.product_name as productName,
                p.description as description,
                p.small_image_url as smallImageUrl,
                p.medium_image_url as mediumImageUrl,
                p.large_image_url as largeImageUrl,
                p.quantity_uom_id as quantityUomId,
                p.quantity_included as quantityIncluded,
                p.pieces_included as piecesIncluded,
                p.weight_uom_id as weightUomId,
                p.shipping_weight as shippingWeight,
                p.product_weight as productWeight,
                p.height_uom_id as heightUomId,
                p.product_height as productHeight,
                p.shipping_height as shippingHeight,
                p.width_uom_id as widthUomId,
                p.product_width as productWidth,
                p.shipping_width as shippingWidth,
                p.depth_uom_id as depthUomId,
                p.product_depth as productDepth,
                p.shipping_depth as shippingDepth,
                p.diameter_uom_id as diameterUomId,
                p.product_diameter as productDiameter,
                p.default_shipment_box_type_id as defaultShipmentBoxTypeId,
                p.active as active,
                gi.id_value as gtin,
                ii.id_value as internalId,
                party.party_id as supplierId,
                party.supplier_name as supplierName
                
            FROM product p
            LEFT JOIN (
                SELECT
                    gi.product_id,
                    gi.id_value
                FROM good_identification gi
                WHERE gi.good_identification_type_id = 'GTIN'
            ) gi ON gi.product_id = p.product_id
            
            LEFT JOIN (
                SELECT
                    gi.product_id,
                    gi.id_value
                FROM good_identification gi
                WHERE gi.good_identification_type_id = 'INTERNAL_ID'
            ) ii ON ii.product_id = p.product_id
            
            LEFT JOIN (
                SELECT DISTINCT ON (sp.product_id)
                    sp.product_id,
                    sp.party_id,
                    COALESCE(o.organization_name, o.last_name) as supplier_name
                FROM supplier_product sp
                JOIN party o ON o.party_id = sp.party_id
                WHERE sp.available_from_date <= CURRENT_TIMESTAMP
                    AND (sp.available_thru_date IS NULL OR sp.available_thru_date > CURRENT_TIMESTAMP)
                ORDER BY sp.product_id, sp.available_from_date DESC
            ) party ON party.product_id = p.product_id
            WHERE p.product_type_id = 'PRODUCT'
                AND (:active IS NULL OR p.active = :active)
            ORDER BY p.created_at DESC
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM product p
                    WHERE p.product_type_id = 'PRODUCT'
                        AND (:active IS NULL OR p.active = :active)
                    """,
            nativeQuery = true)
    Page<BffRawItemProjection> findAllRawItems(Pageable pageable, @Param("active") String active);
    //String tenantId
    //todo AND p.tenant_id = :tenantId
    //todo WHERE p.product_type_id = 'PRODUCT' ??? 这个地方应该过滤出“原材料”类型的产品？


    // NOTE: 这个查询保证了每个产品只返回一个供应商：
    // `DISTINCT ON (sp.product_id)` - PostgreSQL 特有的语法，它会为每个 product_id 只保留一行。
    // `ORDER BY sp.product_id, sp.available_from_date DESC` - 配合 DISTINCT ON 使用，确保保留的是最新的供应商记录。
    // 所以即使一个产品有多个供应商，这个查询也只会返回 available_from_date 最新的那个供应商的信息。

    @Query(value = """
            SELECT 
                sp.product_id as productId,
                sp.party_id as partyId,
                sp.currency_uom_id as currencyUomId,
                sp.minimum_order_quantity as minimumOrderQuantity,
                sp.available_from_date as availableFromDateInstant,
                sp.version as version
            FROM supplier_product sp
            WHERE sp.product_id = :productId
                AND sp.party_id = :partyId
                AND sp.currency_uom_id = :currencyUomId
                AND sp.minimum_order_quantity = :minimumOrderQuantity
                AND sp.available_from_date <= :currentTime
            ORDER BY sp.available_from_date DESC
            LIMIT 1
            """, nativeQuery = true)
    BffSupplierProductAssocProjection findSupplierProductAssociation(
            @Param("productId") String productId,
            @Param("partyId") String partyId,
            @Param("currencyUomId") String currencyUomId,
            @Param("minimumOrderQuantity") BigDecimal minimumOrderQuantity,
            @Param("currentTime") OffsetDateTime currentTime);

}