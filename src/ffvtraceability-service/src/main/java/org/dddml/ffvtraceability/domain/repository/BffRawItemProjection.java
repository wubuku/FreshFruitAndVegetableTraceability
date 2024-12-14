package org.dddml.ffvtraceability.domain.repository;

import java.math.BigDecimal;

public interface BffRawItemProjection {
    String getProductId();

    String getProductName();

    String getDescription();

    String getSmallImageUrl();

    String getMediumImageUrl();

    String getLargeImageUrl();

    String getQuantityUomId();

    BigDecimal getQuantityIncluded();

    Long getPiecesIncluded();

    String getGtin();

    String getSupplierId();

    String getStatusId();
}
