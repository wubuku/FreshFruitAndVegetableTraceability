// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain;

import java.io.Serializable;
import org.dddml.ffvtraceability.domain.*;

public class BffInventoryByLotNoDto implements Serializable {
    private String productId;

    public String getProductId()
    {
        return this.productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    private String productInternalId;

    public String getProductInternalId()
    {
        return this.productInternalId;
    }

    public void setProductInternalId(String productInternalId)
    {
        this.productInternalId = productInternalId;
    }

    private String lotId;

    public String getLotId()
    {
        return this.lotId;
    }

    public void setLotId(String lotId)
    {
        this.lotId = lotId;
    }

    private String lotNo;

    public String getLotNo()
    {
        return this.lotNo;
    }

    public void setLotNo(String lotNo)
    {
        this.lotNo = lotNo;
    }

    private String facilityId;

    public String getFacilityId()
    {
        return this.facilityId;
    }

    public void setFacilityId(String facilityId)
    {
        this.facilityId = facilityId;
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

    private java.math.BigDecimal quantityIncluded;

    public java.math.BigDecimal getQuantityIncluded()
    {
        return this.quantityIncluded;
    }

    public void setQuantityIncluded(java.math.BigDecimal quantityIncluded)
    {
        this.quantityIncluded = quantityIncluded;
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

    private java.math.BigDecimal quantityOnHandTotal;

    public java.math.BigDecimal getQuantityOnHandTotal()
    {
        return this.quantityOnHandTotal;
    }

    public void setQuantityOnHandTotal(java.math.BigDecimal quantityOnHandTotal)
    {
        this.quantityOnHandTotal = quantityOnHandTotal;
    }

    public BffInventoryByLotNoDto()
    {
    }

    public BffInventoryByLotNoDto(String productId, String productInternalId, String lotId, String lotNo, String facilityId, String quantityUomId, java.math.BigDecimal quantityIncluded, String caseUomId, java.math.BigDecimal quantityOnHandTotal)
    {
        this.productId = productId;
        this.productInternalId = productInternalId;
        this.lotId = lotId;
        this.lotNo = lotNo;
        this.facilityId = facilityId;
        this.quantityUomId = quantityUomId;
        this.quantityIncluded = quantityIncluded;
        this.caseUomId = caseUomId;
        this.quantityOnHandTotal = quantityOnHandTotal;
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

        BffInventoryByLotNoDto other = (BffInventoryByLotNoDto)obj;
        return true 
            && (productId == other.productId || (productId != null && productId.equals(other.productId)))
            && (productInternalId == other.productInternalId || (productInternalId != null && productInternalId.equals(other.productInternalId)))
            && (lotId == other.lotId || (lotId != null && lotId.equals(other.lotId)))
            && (lotNo == other.lotNo || (lotNo != null && lotNo.equals(other.lotNo)))
            && (facilityId == other.facilityId || (facilityId != null && facilityId.equals(other.facilityId)))
            && (quantityUomId == other.quantityUomId || (quantityUomId != null && quantityUomId.equals(other.quantityUomId)))
            && (quantityIncluded == other.quantityIncluded || (quantityIncluded != null && quantityIncluded.equals(other.quantityIncluded)))
            && (caseUomId == other.caseUomId || (caseUomId != null && caseUomId.equals(other.caseUomId)))
            && (quantityOnHandTotal == other.quantityOnHandTotal || (quantityOnHandTotal != null && quantityOnHandTotal.equals(other.quantityOnHandTotal)))
            ;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        if (this.productId != null) {
            hash += 13 * this.productId.hashCode();
        }
        if (this.productInternalId != null) {
            hash += 13 * this.productInternalId.hashCode();
        }
        if (this.lotId != null) {
            hash += 13 * this.lotId.hashCode();
        }
        if (this.lotNo != null) {
            hash += 13 * this.lotNo.hashCode();
        }
        if (this.facilityId != null) {
            hash += 13 * this.facilityId.hashCode();
        }
        if (this.quantityUomId != null) {
            hash += 13 * this.quantityUomId.hashCode();
        }
        if (this.quantityIncluded != null) {
            hash += 13 * this.quantityIncluded.hashCode();
        }
        if (this.caseUomId != null) {
            hash += 13 * this.caseUomId.hashCode();
        }
        if (this.quantityOnHandTotal != null) {
            hash += 13 * this.quantityOnHandTotal.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "BffInventoryByLotNoDto{" +
                "productId=" + '\'' + productId + '\'' +
                ", productInternalId=" + '\'' + productInternalId + '\'' +
                ", lotId=" + '\'' + lotId + '\'' +
                ", lotNo=" + '\'' + lotNo + '\'' +
                ", facilityId=" + '\'' + facilityId + '\'' +
                ", quantityUomId=" + '\'' + quantityUomId + '\'' +
                ", quantityIncluded=" + quantityIncluded +
                ", caseUomId=" + '\'' + caseUomId + '\'' +
                ", quantityOnHandTotal=" + quantityOnHandTotal +
                '}';
    }


}

