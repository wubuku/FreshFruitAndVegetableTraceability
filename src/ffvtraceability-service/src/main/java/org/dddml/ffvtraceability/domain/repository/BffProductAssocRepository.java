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
import java.time.OffsetDateTime;
import java.util.List;

public interface BffProductAssocRepository extends JpaRepository<AbstractProductAssocState.SimpleProductAssocState, ProductAssocId> {

    @Query(value = """
            select * from product_assoc where product_id = :productId
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
            """,countQuery = """
            select count(*)
            from (
            SELECT DISTINCT product_id, min(from_date) AS from_date
                                   FROM product_assoc
                                   WHERE (:productId IS NULL OR product_id = :productId)
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


    public interface ProductAssocProjection {

        String getProductId();

        String getProductIdTo();

        String getProductAssocTypeId();

        Instant getFromDate();

        OffsetDateTime getThruDate();

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

        OffsetDateTime getCreatedAt();

        String getUpdatedBy();

        OffsetDateTime getUpdatedAt();

        String getTenantId();
    }
}