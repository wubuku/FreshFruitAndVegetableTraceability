// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain;

import java.io.Serializable;
import org.dddml.ffvtraceability.domain.*;

public class BffProductDto implements Serializable {
    private String productId;

    public String getProductId()
    {
        return this.productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    private String productName;

    public String getProductName()
    {
        return this.productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    private String productTypeId;

    public String getProductTypeId()
    {
        return this.productTypeId;
    }

    public void setProductTypeId(String productTypeId)
    {
        this.productTypeId = productTypeId;
    }

    private String internalName;

    public String getInternalName()
    {
        return this.internalName;
    }

    public void setInternalName(String internalName)
    {
        this.internalName = internalName;
    }

    private String brandName;

    public String getBrandName()
    {
        return this.brandName;
    }

    public void setBrandName(String brandName)
    {
        this.brandName = brandName;
    }

    private String description;

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * GTIN (Global Trade Item Number)
     */
    private String gtin;

    public String getGtin()
    {
        return this.gtin;
    }

    public void setGtin(String gtin)
    {
        this.gtin = gtin;
    }

    private String smallImageUrl;

    public String getSmallImageUrl()
    {
        return this.smallImageUrl;
    }

    public void setSmallImageUrl(String smallImageUrl)
    {
        this.smallImageUrl = smallImageUrl;
    }

    private String mediumImageUrl;

    public String getMediumImageUrl()
    {
        return this.mediumImageUrl;
    }

    public void setMediumImageUrl(String mediumImageUrl)
    {
        this.mediumImageUrl = mediumImageUrl;
    }

    private String largeImageUrl;

    public String getLargeImageUrl()
    {
        return this.largeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl)
    {
        this.largeImageUrl = largeImageUrl;
    }

    private String quantityUomId;

    public String getQuantityUomId()
    {
        return this.quantityUomId;
    }

    public void setQuantityUomId(String quantityUomId)
    {
        this.quantityUomId = quantityUomId;
    }

    /**
     * If you have a six-pack of 12oz soda cans you would have quantityIncluded=12, quantityUomId=oz, piecesIncluded=6.
     */
    private java.math.BigDecimal quantityIncluded;

    public java.math.BigDecimal getQuantityIncluded()
    {
        return this.quantityIncluded;
    }

    public void setQuantityIncluded(java.math.BigDecimal quantityIncluded)
    {
        this.quantityIncluded = quantityIncluded;
    }

    private Long piecesIncluded;

    public Long getPiecesIncluded()
    {
        return this.piecesIncluded;
    }

    public void setPiecesIncluded(Long piecesIncluded)
    {
        this.piecesIncluded = piecesIncluded;
    }

    private String statusId;

    public String getStatusId()
    {
        return this.statusId;
    }

    public void setStatusId(String statusId)
    {
        this.statusId = statusId;
    }

    private String supplierId;

    public String getSupplierId()
    {
        return this.supplierId;
    }

    public void setSupplierId(String supplierId)
    {
        this.supplierId = supplierId;
    }

    private String supplierName;

    public String getSupplierName()
    {
        return this.supplierName;
    }

    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName;
    }

    private String weightUomId;

    public String getWeightUomId()
    {
        return this.weightUomId;
    }

    public void setWeightUomId(String weightUomId)
    {
        this.weightUomId = weightUomId;
    }

    /**
     * The shipping weight of the product.
     */
    private java.math.BigDecimal shippingWeight;

    public java.math.BigDecimal getShippingWeight()
    {
        return this.shippingWeight;
    }

    public void setShippingWeight(java.math.BigDecimal shippingWeight)
    {
        this.shippingWeight = shippingWeight;
    }

    private java.math.BigDecimal productWeight;

    public java.math.BigDecimal getProductWeight()
    {
        return this.productWeight;
    }

    public void setProductWeight(java.math.BigDecimal productWeight)
    {
        this.productWeight = productWeight;
    }

    private String heightUomId;

    public String getHeightUomId()
    {
        return this.heightUomId;
    }

    public void setHeightUomId(String heightUomId)
    {
        this.heightUomId = heightUomId;
    }

    private java.math.BigDecimal productHeight;

    public java.math.BigDecimal getProductHeight()
    {
        return this.productHeight;
    }

    public void setProductHeight(java.math.BigDecimal productHeight)
    {
        this.productHeight = productHeight;
    }

    private java.math.BigDecimal shippingHeight;

    public java.math.BigDecimal getShippingHeight()
    {
        return this.shippingHeight;
    }

    public void setShippingHeight(java.math.BigDecimal shippingHeight)
    {
        this.shippingHeight = shippingHeight;
    }

    private String widthUomId;

    public String getWidthUomId()
    {
        return this.widthUomId;
    }

    public void setWidthUomId(String widthUomId)
    {
        this.widthUomId = widthUomId;
    }

    private java.math.BigDecimal productWidth;

    public java.math.BigDecimal getProductWidth()
    {
        return this.productWidth;
    }

    public void setProductWidth(java.math.BigDecimal productWidth)
    {
        this.productWidth = productWidth;
    }

    private java.math.BigDecimal shippingWidth;

    public java.math.BigDecimal getShippingWidth()
    {
        return this.shippingWidth;
    }

    public void setShippingWidth(java.math.BigDecimal shippingWidth)
    {
        this.shippingWidth = shippingWidth;
    }

    private String depthUomId;

    public String getDepthUomId()
    {
        return this.depthUomId;
    }

    public void setDepthUomId(String depthUomId)
    {
        this.depthUomId = depthUomId;
    }

    private java.math.BigDecimal productDepth;

    public java.math.BigDecimal getProductDepth()
    {
        return this.productDepth;
    }

    public void setProductDepth(java.math.BigDecimal productDepth)
    {
        this.productDepth = productDepth;
    }

    private java.math.BigDecimal shippingDepth;

    public java.math.BigDecimal getShippingDepth()
    {
        return this.shippingDepth;
    }

    public void setShippingDepth(java.math.BigDecimal shippingDepth)
    {
        this.shippingDepth = shippingDepth;
    }

    private String diameterUomId;

    public String getDiameterUomId()
    {
        return this.diameterUomId;
    }

    public void setDiameterUomId(String diameterUomId)
    {
        this.diameterUomId = diameterUomId;
    }

    private java.math.BigDecimal productDiameter;

    public java.math.BigDecimal getProductDiameter()
    {
        return this.productDiameter;
    }

    public void setProductDiameter(java.math.BigDecimal productDiameter)
    {
        this.productDiameter = productDiameter;
    }

    private String active;

    public String getActive()
    {
        return this.active;
    }

    public void setActive(String active)
    {
        this.active = active;
    }

    private String caseUomId;

    public String getCaseUomId()
    {
        return this.caseUomId;
    }

    public void setCaseUomId(String caseUomId)
    {
        this.caseUomId = caseUomId;
    }

    private String internalId;

    public String getInternalId()
    {
        return this.internalId;
    }

    public void setInternalId(String internalId)
    {
        this.internalId = internalId;
    }

    private String produceVariety;

    public String getProduceVariety()
    {
        return this.produceVariety;
    }

    public void setProduceVariety(String produceVariety)
    {
        this.produceVariety = produceVariety;
    }

    private String hsCode;

    public String getHsCode()
    {
        return this.hsCode;
    }

    public void setHsCode(String hsCode)
    {
        this.hsCode = hsCode;
    }

    private String organicCertifications;

    public String getOrganicCertifications()
    {
        return this.organicCertifications;
    }

    public void setOrganicCertifications(String organicCertifications)
    {
        this.organicCertifications = organicCertifications;
    }

    private String materialCompositionDescription;

    public String getMaterialCompositionDescription()
    {
        return this.materialCompositionDescription;
    }

    public void setMaterialCompositionDescription(String materialCompositionDescription)
    {
        this.materialCompositionDescription = materialCompositionDescription;
    }

    private String countryOfOrigin;

    public String getCountryOfOrigin()
    {
        return this.countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin)
    {
        this.countryOfOrigin = countryOfOrigin;
    }

    private String shelfLifeDescription;

    public String getShelfLifeDescription()
    {
        return this.shelfLifeDescription;
    }

    public void setShelfLifeDescription(String shelfLifeDescription)
    {
        this.shelfLifeDescription = shelfLifeDescription;
    }

    private String handlingInstructions;

    public String getHandlingInstructions()
    {
        return this.handlingInstructions;
    }

    public void setHandlingInstructions(String handlingInstructions)
    {
        this.handlingInstructions = handlingInstructions;
    }

    private String storageConditions;

    public String getStorageConditions()
    {
        return this.storageConditions;
    }

    public void setStorageConditions(String storageConditions)
    {
        this.storageConditions = storageConditions;
    }

    private String certificationCodes;

    public String getCertificationCodes()
    {
        return this.certificationCodes;
    }

    public void setCertificationCodes(String certificationCodes)
    {
        this.certificationCodes = certificationCodes;
    }

    /**
     * Number of individual units in one package (for products counted by individual pieces, e.g., fruits, eggs)
     */
    private Long individualsPerPackage;

    public Long getIndividualsPerPackage()
    {
        return this.individualsPerPackage;
    }

    public void setIndividualsPerPackage(Long individualsPerPackage)
    {
        this.individualsPerPackage = individualsPerPackage;
    }

    private String dimensionsDescription;

    public String getDimensionsDescription()
    {
        return this.dimensionsDescription;
    }

    public void setDimensionsDescription(String dimensionsDescription)
    {
        this.dimensionsDescription = dimensionsDescription;
    }

    public BffProductDto()
    {
    }

    public BffProductDto(String productId, String productName, String productTypeId, String internalName, String brandName, String description, String gtin, String smallImageUrl, String mediumImageUrl, String largeImageUrl, String quantityUomId, java.math.BigDecimal quantityIncluded, Long piecesIncluded, String statusId, String supplierId, String supplierName, String weightUomId, java.math.BigDecimal shippingWeight, java.math.BigDecimal productWeight, String heightUomId, java.math.BigDecimal productHeight, java.math.BigDecimal shippingHeight, String widthUomId, java.math.BigDecimal productWidth, java.math.BigDecimal shippingWidth, String depthUomId, java.math.BigDecimal productDepth, java.math.BigDecimal shippingDepth, String diameterUomId, java.math.BigDecimal productDiameter, String active, String caseUomId, String internalId, String produceVariety, String hsCode, String organicCertifications, String materialCompositionDescription, String countryOfOrigin, String shelfLifeDescription, String handlingInstructions, String storageConditions, String certificationCodes, Long individualsPerPackage, String dimensionsDescription)
    {
        this.productId = productId;
        this.productName = productName;
        this.productTypeId = productTypeId;
        this.internalName = internalName;
        this.brandName = brandName;
        this.description = description;
        this.gtin = gtin;
        this.smallImageUrl = smallImageUrl;
        this.mediumImageUrl = mediumImageUrl;
        this.largeImageUrl = largeImageUrl;
        this.quantityUomId = quantityUomId;
        this.quantityIncluded = quantityIncluded;
        this.piecesIncluded = piecesIncluded;
        this.statusId = statusId;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.weightUomId = weightUomId;
        this.shippingWeight = shippingWeight;
        this.productWeight = productWeight;
        this.heightUomId = heightUomId;
        this.productHeight = productHeight;
        this.shippingHeight = shippingHeight;
        this.widthUomId = widthUomId;
        this.productWidth = productWidth;
        this.shippingWidth = shippingWidth;
        this.depthUomId = depthUomId;
        this.productDepth = productDepth;
        this.shippingDepth = shippingDepth;
        this.diameterUomId = diameterUomId;
        this.productDiameter = productDiameter;
        this.active = active;
        this.caseUomId = caseUomId;
        this.internalId = internalId;
        this.produceVariety = produceVariety;
        this.hsCode = hsCode;
        this.organicCertifications = organicCertifications;
        this.materialCompositionDescription = materialCompositionDescription;
        this.countryOfOrigin = countryOfOrigin;
        this.shelfLifeDescription = shelfLifeDescription;
        this.handlingInstructions = handlingInstructions;
        this.storageConditions = storageConditions;
        this.certificationCodes = certificationCodes;
        this.individualsPerPackage = individualsPerPackage;
        this.dimensionsDescription = dimensionsDescription;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        BffProductDto other = (BffProductDto)obj;
        return true 
            && (productId == other.productId || (productId != null && productId.equals(other.productId)))
            && (productName == other.productName || (productName != null && productName.equals(other.productName)))
            && (productTypeId == other.productTypeId || (productTypeId != null && productTypeId.equals(other.productTypeId)))
            && (internalName == other.internalName || (internalName != null && internalName.equals(other.internalName)))
            && (brandName == other.brandName || (brandName != null && brandName.equals(other.brandName)))
            && (description == other.description || (description != null && description.equals(other.description)))
            && (gtin == other.gtin || (gtin != null && gtin.equals(other.gtin)))
            && (smallImageUrl == other.smallImageUrl || (smallImageUrl != null && smallImageUrl.equals(other.smallImageUrl)))
            && (mediumImageUrl == other.mediumImageUrl || (mediumImageUrl != null && mediumImageUrl.equals(other.mediumImageUrl)))
            && (largeImageUrl == other.largeImageUrl || (largeImageUrl != null && largeImageUrl.equals(other.largeImageUrl)))
            && (quantityUomId == other.quantityUomId || (quantityUomId != null && quantityUomId.equals(other.quantityUomId)))
            && (quantityIncluded == other.quantityIncluded || (quantityIncluded != null && quantityIncluded.equals(other.quantityIncluded)))
            && (piecesIncluded == other.piecesIncluded || (piecesIncluded != null && piecesIncluded.equals(other.piecesIncluded)))
            && (statusId == other.statusId || (statusId != null && statusId.equals(other.statusId)))
            && (supplierId == other.supplierId || (supplierId != null && supplierId.equals(other.supplierId)))
            && (supplierName == other.supplierName || (supplierName != null && supplierName.equals(other.supplierName)))
            && (weightUomId == other.weightUomId || (weightUomId != null && weightUomId.equals(other.weightUomId)))
            && (shippingWeight == other.shippingWeight || (shippingWeight != null && shippingWeight.equals(other.shippingWeight)))
            && (productWeight == other.productWeight || (productWeight != null && productWeight.equals(other.productWeight)))
            && (heightUomId == other.heightUomId || (heightUomId != null && heightUomId.equals(other.heightUomId)))
            && (productHeight == other.productHeight || (productHeight != null && productHeight.equals(other.productHeight)))
            && (shippingHeight == other.shippingHeight || (shippingHeight != null && shippingHeight.equals(other.shippingHeight)))
            && (widthUomId == other.widthUomId || (widthUomId != null && widthUomId.equals(other.widthUomId)))
            && (productWidth == other.productWidth || (productWidth != null && productWidth.equals(other.productWidth)))
            && (shippingWidth == other.shippingWidth || (shippingWidth != null && shippingWidth.equals(other.shippingWidth)))
            && (depthUomId == other.depthUomId || (depthUomId != null && depthUomId.equals(other.depthUomId)))
            && (productDepth == other.productDepth || (productDepth != null && productDepth.equals(other.productDepth)))
            && (shippingDepth == other.shippingDepth || (shippingDepth != null && shippingDepth.equals(other.shippingDepth)))
            && (diameterUomId == other.diameterUomId || (diameterUomId != null && diameterUomId.equals(other.diameterUomId)))
            && (productDiameter == other.productDiameter || (productDiameter != null && productDiameter.equals(other.productDiameter)))
            && (active == other.active || (active != null && active.equals(other.active)))
            && (caseUomId == other.caseUomId || (caseUomId != null && caseUomId.equals(other.caseUomId)))
            && (internalId == other.internalId || (internalId != null && internalId.equals(other.internalId)))
            && (produceVariety == other.produceVariety || (produceVariety != null && produceVariety.equals(other.produceVariety)))
            && (hsCode == other.hsCode || (hsCode != null && hsCode.equals(other.hsCode)))
            && (organicCertifications == other.organicCertifications || (organicCertifications != null && organicCertifications.equals(other.organicCertifications)))
            && (materialCompositionDescription == other.materialCompositionDescription || (materialCompositionDescription != null && materialCompositionDescription.equals(other.materialCompositionDescription)))
            && (countryOfOrigin == other.countryOfOrigin || (countryOfOrigin != null && countryOfOrigin.equals(other.countryOfOrigin)))
            && (shelfLifeDescription == other.shelfLifeDescription || (shelfLifeDescription != null && shelfLifeDescription.equals(other.shelfLifeDescription)))
            && (handlingInstructions == other.handlingInstructions || (handlingInstructions != null && handlingInstructions.equals(other.handlingInstructions)))
            && (storageConditions == other.storageConditions || (storageConditions != null && storageConditions.equals(other.storageConditions)))
            && (certificationCodes == other.certificationCodes || (certificationCodes != null && certificationCodes.equals(other.certificationCodes)))
            && (individualsPerPackage == other.individualsPerPackage || (individualsPerPackage != null && individualsPerPackage.equals(other.individualsPerPackage)))
            && (dimensionsDescription == other.dimensionsDescription || (dimensionsDescription != null && dimensionsDescription.equals(other.dimensionsDescription)))
            ;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        if (this.productId != null) {
            hash += 13 * this.productId.hashCode();
        }
        if (this.productName != null) {
            hash += 13 * this.productName.hashCode();
        }
        if (this.productTypeId != null) {
            hash += 13 * this.productTypeId.hashCode();
        }
        if (this.internalName != null) {
            hash += 13 * this.internalName.hashCode();
        }
        if (this.brandName != null) {
            hash += 13 * this.brandName.hashCode();
        }
        if (this.description != null) {
            hash += 13 * this.description.hashCode();
        }
        if (this.gtin != null) {
            hash += 13 * this.gtin.hashCode();
        }
        if (this.smallImageUrl != null) {
            hash += 13 * this.smallImageUrl.hashCode();
        }
        if (this.mediumImageUrl != null) {
            hash += 13 * this.mediumImageUrl.hashCode();
        }
        if (this.largeImageUrl != null) {
            hash += 13 * this.largeImageUrl.hashCode();
        }
        if (this.quantityUomId != null) {
            hash += 13 * this.quantityUomId.hashCode();
        }
        if (this.quantityIncluded != null) {
            hash += 13 * this.quantityIncluded.hashCode();
        }
        if (this.piecesIncluded != null) {
            hash += 13 * this.piecesIncluded.hashCode();
        }
        if (this.statusId != null) {
            hash += 13 * this.statusId.hashCode();
        }
        if (this.supplierId != null) {
            hash += 13 * this.supplierId.hashCode();
        }
        if (this.supplierName != null) {
            hash += 13 * this.supplierName.hashCode();
        }
        if (this.weightUomId != null) {
            hash += 13 * this.weightUomId.hashCode();
        }
        if (this.shippingWeight != null) {
            hash += 13 * this.shippingWeight.hashCode();
        }
        if (this.productWeight != null) {
            hash += 13 * this.productWeight.hashCode();
        }
        if (this.heightUomId != null) {
            hash += 13 * this.heightUomId.hashCode();
        }
        if (this.productHeight != null) {
            hash += 13 * this.productHeight.hashCode();
        }
        if (this.shippingHeight != null) {
            hash += 13 * this.shippingHeight.hashCode();
        }
        if (this.widthUomId != null) {
            hash += 13 * this.widthUomId.hashCode();
        }
        if (this.productWidth != null) {
            hash += 13 * this.productWidth.hashCode();
        }
        if (this.shippingWidth != null) {
            hash += 13 * this.shippingWidth.hashCode();
        }
        if (this.depthUomId != null) {
            hash += 13 * this.depthUomId.hashCode();
        }
        if (this.productDepth != null) {
            hash += 13 * this.productDepth.hashCode();
        }
        if (this.shippingDepth != null) {
            hash += 13 * this.shippingDepth.hashCode();
        }
        if (this.diameterUomId != null) {
            hash += 13 * this.diameterUomId.hashCode();
        }
        if (this.productDiameter != null) {
            hash += 13 * this.productDiameter.hashCode();
        }
        if (this.active != null) {
            hash += 13 * this.active.hashCode();
        }
        if (this.caseUomId != null) {
            hash += 13 * this.caseUomId.hashCode();
        }
        if (this.internalId != null) {
            hash += 13 * this.internalId.hashCode();
        }
        if (this.produceVariety != null) {
            hash += 13 * this.produceVariety.hashCode();
        }
        if (this.hsCode != null) {
            hash += 13 * this.hsCode.hashCode();
        }
        if (this.organicCertifications != null) {
            hash += 13 * this.organicCertifications.hashCode();
        }
        if (this.materialCompositionDescription != null) {
            hash += 13 * this.materialCompositionDescription.hashCode();
        }
        if (this.countryOfOrigin != null) {
            hash += 13 * this.countryOfOrigin.hashCode();
        }
        if (this.shelfLifeDescription != null) {
            hash += 13 * this.shelfLifeDescription.hashCode();
        }
        if (this.handlingInstructions != null) {
            hash += 13 * this.handlingInstructions.hashCode();
        }
        if (this.storageConditions != null) {
            hash += 13 * this.storageConditions.hashCode();
        }
        if (this.certificationCodes != null) {
            hash += 13 * this.certificationCodes.hashCode();
        }
        if (this.individualsPerPackage != null) {
            hash += 13 * this.individualsPerPackage.hashCode();
        }
        if (this.dimensionsDescription != null) {
            hash += 13 * this.dimensionsDescription.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "BffProductDto{" +
                "productId=" + '\'' + productId + '\'' +
                ", productName=" + '\'' + productName + '\'' +
                ", productTypeId=" + '\'' + productTypeId + '\'' +
                ", internalName=" + '\'' + internalName + '\'' +
                ", brandName=" + '\'' + brandName + '\'' +
                ", description=" + '\'' + description + '\'' +
                ", gtin=" + '\'' + gtin + '\'' +
                ", smallImageUrl=" + '\'' + smallImageUrl + '\'' +
                ", mediumImageUrl=" + '\'' + mediumImageUrl + '\'' +
                ", largeImageUrl=" + '\'' + largeImageUrl + '\'' +
                ", quantityUomId=" + '\'' + quantityUomId + '\'' +
                ", quantityIncluded=" + quantityIncluded +
                ", piecesIncluded=" + piecesIncluded +
                ", statusId=" + '\'' + statusId + '\'' +
                ", supplierId=" + '\'' + supplierId + '\'' +
                ", supplierName=" + '\'' + supplierName + '\'' +
                ", weightUomId=" + '\'' + weightUomId + '\'' +
                ", shippingWeight=" + shippingWeight +
                ", productWeight=" + productWeight +
                ", heightUomId=" + '\'' + heightUomId + '\'' +
                ", productHeight=" + productHeight +
                ", shippingHeight=" + shippingHeight +
                ", widthUomId=" + '\'' + widthUomId + '\'' +
                ", productWidth=" + productWidth +
                ", shippingWidth=" + shippingWidth +
                ", depthUomId=" + '\'' + depthUomId + '\'' +
                ", productDepth=" + productDepth +
                ", shippingDepth=" + shippingDepth +
                ", diameterUomId=" + '\'' + diameterUomId + '\'' +
                ", productDiameter=" + productDiameter +
                ", active=" + '\'' + active + '\'' +
                ", caseUomId=" + '\'' + caseUomId + '\'' +
                ", internalId=" + '\'' + internalId + '\'' +
                ", produceVariety=" + '\'' + produceVariety + '\'' +
                ", hsCode=" + '\'' + hsCode + '\'' +
                ", organicCertifications=" + '\'' + organicCertifications + '\'' +
                ", materialCompositionDescription=" + '\'' + materialCompositionDescription + '\'' +
                ", countryOfOrigin=" + '\'' + countryOfOrigin + '\'' +
                ", shelfLifeDescription=" + '\'' + shelfLifeDescription + '\'' +
                ", handlingInstructions=" + '\'' + handlingInstructions + '\'' +
                ", storageConditions=" + '\'' + storageConditions + '\'' +
                ", certificationCodes=" + '\'' + certificationCodes + '\'' +
                ", individualsPerPackage=" + individualsPerPackage +
                ", dimensionsDescription=" + '\'' + dimensionsDescription + '\'' +
                '}';
    }


}

