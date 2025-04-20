package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.productassoc.AbstractProductAssocState;
import org.dddml.ffvtraceability.domain.productassoc.ProductAssocId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface BffProductAssocRepository extends JpaRepository<AbstractProductAssocState.SimpleProductAssocState, ProductAssocId> {

    @Query(value = """
            select
            product_id as productId,
            product_id_to as productIdTo,
            product_assoc_type_id as productAssocTypeId,
            from_date as fromDate,
            version,
            thru_date as thruDate,
            sequence_num as sequenceNum,
            reason,
            quantity,
            scrap_factor as scrapFactor,
            instruction,
            routing_work_effort_id as routingWorkEffortId,
            estimate_calc_method as estimateCalcMethod,
            recurrence_info_id as recurrenceInfoId,
            created_by as createdBy,
            updated_by as updatedBy,
            created_at as createdAt,
            updated_at as updatedAt
            from product_assoc
            where product_id = :productId
            and (deleted is null or deleted = false)
            """, nativeQuery = true)
    List<ProductAssocProjection> findBomByProductId(@Param("productId") String productId);


    @Query(value = """
            select
            pa.product_id as productId,
            p.product_type_id as productTypeId,
            p.product_name as productName,
            p.small_image_url as smallImageUrl,
            p.medium_image_url as mediumImageUrl,
            p.large_image_url as largeImageUrl,
            p.quantity_uom_id as quantityUomId,
            pa.from_date as fromDate,
            ii.id_value as internalId
            from (
            SELECT DISTINCT product_id, min(from_date) AS from_date
                                   FROM product_assoc
                                   WHERE (:productId IS NULL OR product_id = :productId)
                                   and (deleted is null or deleted = false)
                                   GROUP BY product_id
                                   ORDER BY from_date DESC
            ) pa
            left join product p on p.product_id = pa.product_id
            LEFT JOIN (
                SELECT
                    gi.product_id,
                    gi.id_value
                FROM good_identification gi
                WHERE gi.good_identification_type_id = 'INTERNAL_ID'
            ) ii ON ii.product_id = p.product_id
            where (:productTypeId is null or p.product_type_id = :productTypeId)
            and (:internalId is null or ii.id_value = :internalId)
            """, countQuery = """
            select count(*)
            from (
            SELECT DISTINCT product_id, min(from_date) AS from_date
                                   FROM product_assoc
                                   WHERE (:productId IS NULL OR product_id = :productId)
                                   and (deleted is null or deleted = false)
                                   GROUP BY product_id
                                   ORDER BY from_date DESC
            ) pa
            left join product p on p.product_id = pa.product_id
            LEFT JOIN (
                SELECT
                    gi.product_id,
                    gi.id_value
                FROM good_identification gi
                WHERE gi.good_identification_type_id = 'INTERNAL_ID'
            ) ii ON ii.product_id = p.product_id
            where (:productTypeId is null or p.product_type_id = :productTypeId)
            and (:internalId is null or ii.id_value = :internalId)
            """, nativeQuery = true)
    Page<BffProductAssociationDtoProjection> findAllBoms(Pageable pageable,
                                                         @Param("productTypeId") String productTypeId,
                                                         @Param("productId") String productId,
                                                         @Param("internalId") String internalId);

    @Query(value = """
            select
            pa.product_id as productId,
            p.product_type_id as productTypeId,
            p.product_name as productName,
            p.small_image_url as smallImageUrl,
            p.medium_image_url as mediumImageUrl,
            p.large_image_url as largeImageUrl,
            p.quantity_uom_id as quantityUomId,
            pa.from_date as fromDate,
            ii.id_value as internalId
            from (
            SELECT DISTINCT product_id, min(from_date) AS from_date
                                   FROM product_assoc
                                   WHERE product_id = :productId
                                   and (deleted is null or deleted = false)
                                   GROUP BY product_id
                                   ORDER BY from_date DESC
            ) pa
            left join product p on p.product_id = pa.product_id
            LEFT JOIN (
                SELECT
                    gi.product_id,
                    gi.id_value
                FROM good_identification gi
                WHERE gi.good_identification_type_id = 'INTERNAL_ID'
            ) ii ON ii.product_id = p.product_id
            """, nativeQuery = true)
    Optional<BffProductAssociationDtoProjection> findProductAssocByProductId(@Param("productId") String productId);

    @Query(value = """
            select
            pa.product_id_to as productId, --注意这里是将product_id_to作为productId
            p.product_type_id as productTypeId,
            p.product_name as productName,
            p.small_image_url as smallImageUrl,
            p.medium_image_url as mediumImageUrl,
            p.large_image_url as largeImageUrl,
            p.quantity_uom_id as quantityUomId,
            pa.from_date as fromDate,
            ii.id_value as internalId,
            pa.sequence_num as sequenceNum,
            pa.quantity,
            pa.scrap_factor as scrapFactor --,
            --pa.created_by as createdBy,
            --pa.created_at as createdAt
            from product_assoc pa
            left join product p on p.product_id = pa.product_id_to --注意这里是将product_id_to作为productId
            LEFT JOIN (
                SELECT
                    gi.product_id,
                    gi.id_value
                FROM good_identification gi
                WHERE gi.good_identification_type_id = 'INTERNAL_ID'
            ) ii ON ii.product_id = pa.product_id_to
            where pa.product_id = :parentProductId
            and pa.product_id_to = :productId
            and (pa.deleted is null or pa.deleted = false)
            """, nativeQuery = true)
        //当一个产品只作为BOM的子节点时，获取其作为BOM子节点的BOM信息（同时包括其产品信息）
    Optional<BffProductAssociationDtoProjection> findProductAssocInfoByProductIdTo(@Param("parentProductId") String parentProductId,
                                                                                   @Param("productId") String productId);

    @Query(value = """
            select
            pa.sequence_num as sequenceNum,
            pa.quantity,
            pa.scrap_factor as scrapFactor --,
            --pa.created_by as createdBy,
            --pa.created_at as createdAt
            from product_assoc pa
            where pa.product_id = :parentProductId
            and pa.product_id_to = :productId
            and (pa.deleted is null or pa.deleted = false)
            """, nativeQuery = true)
        //当一个产品同时作为BOM的父节点和子节点时，需要补足作为子节点的BOM信息
    Optional<ProductAssocProjection> findBomInfoByWhenProductAsBomAndComponent(@Param("parentProductId") String parentProductId,
                                                                               @Param("productId") String productId);


    interface ProductAssocProjection {

        String getProductId();

        String getProductIdTo();

        String getProductAssocTypeId();

        Instant getFromDate();

        Instant getThruDate();

        Long getSequenceNum();

        String getReason();

        BigDecimal getQuantity();

        BigDecimal getScrapFactor();

        String getInstruction();

        String getRoutingWorkEffortId();

        String getEstimateCalcMethod();

        String getRecurrenceInfoId();

        Long getVersion();

        String getCreatedBy();

        Instant getCreatedAt();

        String getUpdatedBy();

        Instant getUpdatedAt();

        String getTenantId();
    }
}