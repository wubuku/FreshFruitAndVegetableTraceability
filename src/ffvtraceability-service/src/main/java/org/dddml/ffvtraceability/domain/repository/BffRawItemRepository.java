package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.product.AbstractProductState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
                gi.id_value as gtin,
                party.party_id as supplierId
            FROM product p
            LEFT JOIN (
                SELECT
                    gi.product_id,
                    gi.id_value
                FROM good_identification gi
                WHERE gi.good_identification_type_id = 'GTIN'
                    AND (gi.deleted IS NULL OR gi.deleted = false)
            ) gi ON gi.product_id = p.product_id
            LEFT JOIN (
                SELECT DISTINCT ON (sp.product_id)
                    sp.product_id,
                    sp.party_id,
                    o.organization_name
                FROM supplier_product sp
                JOIN party o ON o.party_id = sp.party_id
                WHERE o.party_type_id = 'ORGANIZATION'
                    AND (o.deleted IS NULL OR o.deleted = false)
                    AND sp.available_from_date <= CURRENT_TIMESTAMP
                    AND (sp.available_thru_date IS NULL OR sp.available_thru_date > CURRENT_TIMESTAMP)
                ORDER BY sp.product_id, sp.available_from_date DESC
            ) party ON party.product_id = p.product_id
            WHERE p.product_type_id = 'PRODUCT'
            ORDER BY p.created_at DESC
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM product p
                    WHERE p.product_type_id = 'PRODUCT'
                    """,
            nativeQuery = true)
    Page<BffRawItemProjection> findAllRawItems(Pageable pageable);
    //String tenantId
    //todo AND p.tenant_id = :tenantId
    //todo WHERE p.product_type_id = 'PRODUCT' ??? 这个地方应该过滤出“原材料”类型的产品？


    // 这个查询保证了每个产品只返回一个供应商：
    // `DISTINCT ON (sp.product_id)` - PostgreSQL 特有的语法，它会为每个 product_id 只保留一行。
    // `ORDER BY sp.product_id, sp.available_from_date DESC` - 配合 DISTINCT ON 使用，确保保留的是最新的供应商记录。
    // 所以即使一个产品有多个供应商，这个查询也只会返回 available_from_date 最新的那个供应商的信息。

}