// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.*;

public interface BffRawItemProjection {
    String getProductId();

    String getProductName();

    String getDescription();

    String getGtin();

    String getSmallImageUrl();

    String getMediumImageUrl();

    String getLargeImageUrl();

    String getQuantityUomId();

    java.math.BigDecimal getQuantityIncluded();

    Long getPiecesIncluded();

    String getStatusId();

    String getSupplierId();

}
