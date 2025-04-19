package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.productassoc.AbstractProductAssocState;
import org.dddml.ffvtraceability.domain.productassoc.ProductAssocId;
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