// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.*;

public interface BffProductProjection {
    String getProductId();

    String getProductName();

    String getProductTypeId();

    String getInternalName();

    String getBrandName();

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

    String getSupplierName();

    String getWeightUomId();

    java.math.BigDecimal getShippingWeight();

    java.math.BigDecimal getProductWeight();

    String getHeightUomId();

    java.math.BigDecimal getProductHeight();

    java.math.BigDecimal getShippingHeight();

    String getWidthUomId();

    java.math.BigDecimal getProductWidth();

    java.math.BigDecimal getShippingWidth();

    String getDepthUomId();

    java.math.BigDecimal getProductDepth();

    java.math.BigDecimal getShippingDepth();

    String getDiameterUomId();

    java.math.BigDecimal getProductDiameter();

    String getActive();

    String getCaseUomId();

    String getInternalId();

    String getProduceVariety();

    String getHsCode();

    String getOrganicCertifications();

    String getMaterialCompositionDescription();

    String getCountryOfOrigin();

    String getShelfLifeDescription();

    String getHandlingInstructions();

    String getStorageConditions();

    String getCertificationCodes();

    Long getIndividualsPerPackage();

    String getDimensionsDescription();

}

