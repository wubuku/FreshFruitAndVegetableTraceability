// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain;

import java.io.Serializable;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public class BffRawItemInventoryItemDto implements Serializable {
    private String productId;

    public String getProductId()
    {
        return this.productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
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

    private String receivingDocumentId;

    public String getReceivingDocumentId()
    {
        return this.receivingDocumentId;
    }

    public void setReceivingDocumentId(String receivingDocumentId)
    {
        this.receivingDocumentId = receivingDocumentId;
    }

    private OffsetDateTime receivedAt;

    public OffsetDateTime getReceivedAt()
    {
        return this.receivedAt;
    }

    public void setReceivedAt(OffsetDateTime receivedAt)
    {
        this.receivedAt = receivedAt;
    }

    private String orderId;

    public String getOrderId()
    {
        return this.orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    private String qaStatusId;

    public String getQaStatusId()
    {
        return this.qaStatusId;
    }

    public void setQaStatusId(String qaStatusId)
    {
        this.qaStatusId = qaStatusId;
    }

    private String locationCode;

    public String getLocationCode()
    {
        return this.locationCode;
    }

    public void setLocationCode(String locationCode)
    {
        this.locationCode = locationCode;
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

    private String caseUomId;

    public String getCaseUomId()
    {
        return this.caseUomId;
    }

    public void setCaseUomId(String caseUomId)
    {
        this.caseUomId = caseUomId;
    }

    public BffRawItemInventoryItemDto()
    {
    }

    public BffRawItemInventoryItemDto(String productId, String internalId, String lotId, String lotNo, String receivingDocumentId, OffsetDateTime receivedAt, String orderId, String qaStatusId, String locationCode, java.math.BigDecimal quantityOnHandTotal, String quantityUomId, java.math.BigDecimal quantityIncluded, String caseUomId)
    {
        this.productId = productId;
        this.internalId = internalId;
        this.lotId = lotId;
        this.lotNo = lotNo;
        this.receivingDocumentId = receivingDocumentId;
        this.receivedAt = receivedAt;
        this.orderId = orderId;
        this.qaStatusId = qaStatusId;
        this.locationCode = locationCode;
        this.quantityOnHandTotal = quantityOnHandTotal;
        this.quantityUomId = quantityUomId;
        this.quantityIncluded = quantityIncluded;
        this.caseUomId = caseUomId;
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

        BffRawItemInventoryItemDto other = (BffRawItemInventoryItemDto)obj;
        return true 
            && (productId == other.productId || (productId != null && productId.equals(other.productId)))
            && (internalId == other.internalId || (internalId != null && internalId.equals(other.internalId)))
            && (lotId == other.lotId || (lotId != null && lotId.equals(other.lotId)))
            && (lotNo == other.lotNo || (lotNo != null && lotNo.equals(other.lotNo)))
            && (receivingDocumentId == other.receivingDocumentId || (receivingDocumentId != null && receivingDocumentId.equals(other.receivingDocumentId)))
            && (receivedAt == other.receivedAt || (receivedAt != null && receivedAt.equals(other.receivedAt)))
            && (orderId == other.orderId || (orderId != null && orderId.equals(other.orderId)))
            && (qaStatusId == other.qaStatusId || (qaStatusId != null && qaStatusId.equals(other.qaStatusId)))
            && (locationCode == other.locationCode || (locationCode != null && locationCode.equals(other.locationCode)))
            && (quantityOnHandTotal == other.quantityOnHandTotal || (quantityOnHandTotal != null && quantityOnHandTotal.equals(other.quantityOnHandTotal)))
            && (quantityUomId == other.quantityUomId || (quantityUomId != null && quantityUomId.equals(other.quantityUomId)))
            && (quantityIncluded == other.quantityIncluded || (quantityIncluded != null && quantityIncluded.equals(other.quantityIncluded)))
            && (caseUomId == other.caseUomId || (caseUomId != null && caseUomId.equals(other.caseUomId)))
            ;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        if (this.productId != null) {
            hash += 13 * this.productId.hashCode();
        }
        if (this.internalId != null) {
            hash += 13 * this.internalId.hashCode();
        }
        if (this.lotId != null) {
            hash += 13 * this.lotId.hashCode();
        }
        if (this.lotNo != null) {
            hash += 13 * this.lotNo.hashCode();
        }
        if (this.receivingDocumentId != null) {
            hash += 13 * this.receivingDocumentId.hashCode();
        }
        if (this.receivedAt != null) {
            hash += 13 * this.receivedAt.hashCode();
        }
        if (this.orderId != null) {
            hash += 13 * this.orderId.hashCode();
        }
        if (this.qaStatusId != null) {
            hash += 13 * this.qaStatusId.hashCode();
        }
        if (this.locationCode != null) {
            hash += 13 * this.locationCode.hashCode();
        }
        if (this.quantityOnHandTotal != null) {
            hash += 13 * this.quantityOnHandTotal.hashCode();
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
        return hash;
    }

    @Override
    public String toString() {
        return "BffRawItemInventoryItemDto{" +
                "productId=" + '\'' + productId + '\'' +
                ", internalId=" + '\'' + internalId + '\'' +
                ", lotId=" + '\'' + lotId + '\'' +
                ", lotNo=" + '\'' + lotNo + '\'' +
                ", receivingDocumentId=" + '\'' + receivingDocumentId + '\'' +
                ", receivedAt=" + receivedAt +
                ", orderId=" + '\'' + orderId + '\'' +
                ", qaStatusId=" + '\'' + qaStatusId + '\'' +
                ", locationCode=" + '\'' + locationCode + '\'' +
                ", quantityOnHandTotal=" + quantityOnHandTotal +
                ", quantityUomId=" + '\'' + quantityUomId + '\'' +
                ", quantityIncluded=" + quantityIncluded +
                ", caseUomId=" + '\'' + caseUomId + '\'' +
                '}';
    }


}

