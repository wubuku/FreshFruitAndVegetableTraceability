// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain;

import java.io.Serializable;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public class BffQaInspectionDto implements Serializable {
    private String qaInspectionId;

    public String getQaInspectionId()
    {
        return this.qaInspectionId;
    }

    public void setQaInspectionId(String qaInspectionId)
    {
        this.qaInspectionId = qaInspectionId;
    }

    private String receiptId;

    public String getReceiptId()
    {
        return this.receiptId;
    }

    public void setReceiptId(String receiptId)
    {
        this.receiptId = receiptId;
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

    private String inspectionTypeId;

    public String getInspectionTypeId()
    {
        return this.inspectionTypeId;
    }

    public void setInspectionTypeId(String inspectionTypeId)
    {
        this.inspectionTypeId = inspectionTypeId;
    }

    private String productId;

    public String getProductId()
    {
        return this.productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
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

    private String supplierId;

    public String getSupplierId()
    {
        return this.supplierId;
    }

    public void setSupplierId(String supplierId)
    {
        this.supplierId = supplierId;
    }

    private String comments;

    public String getComments()
    {
        return this.comments;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
    }

    private String inspectionFacilityId;

    public String getInspectionFacilityId()
    {
        return this.inspectionFacilityId;
    }

    public void setInspectionFacilityId(String inspectionFacilityId)
    {
        this.inspectionFacilityId = inspectionFacilityId;
    }

    private String inspectedBy;

    public String getInspectedBy()
    {
        return this.inspectedBy;
    }

    public void setInspectedBy(String inspectedBy)
    {
        this.inspectedBy = inspectedBy;
    }

    private String createdBy;

    public String getCreatedBy()
    {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    private OffsetDateTime inspectedAt;

    public OffsetDateTime getInspectedAt()
    {
        return this.inspectedAt;
    }

    public void setInspectedAt(OffsetDateTime inspectedAt)
    {
        this.inspectedAt = inspectedAt;
    }

    private OffsetDateTime createdAt;

    public OffsetDateTime getCreatedAt()
    {
        return this.createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    private OffsetDateTime updatedAt;

    public OffsetDateTime getUpdatedAt()
    {
        return this.updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public BffQaInspectionDto()
    {
    }

    public BffQaInspectionDto(String qaInspectionId, String receiptId, String statusId, String inspectionTypeId, String productId, String lotId, String supplierId, String comments, String inspectionFacilityId, String inspectedBy, String createdBy, OffsetDateTime inspectedAt, OffsetDateTime createdAt, OffsetDateTime updatedAt)
    {
        this.qaInspectionId = qaInspectionId;
        this.receiptId = receiptId;
        this.statusId = statusId;
        this.inspectionTypeId = inspectionTypeId;
        this.productId = productId;
        this.lotId = lotId;
        this.supplierId = supplierId;
        this.comments = comments;
        this.inspectionFacilityId = inspectionFacilityId;
        this.inspectedBy = inspectedBy;
        this.createdBy = createdBy;
        this.inspectedAt = inspectedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

        BffQaInspectionDto other = (BffQaInspectionDto)obj;
        return true 
            && (qaInspectionId == other.qaInspectionId || (qaInspectionId != null && qaInspectionId.equals(other.qaInspectionId)))
            && (receiptId == other.receiptId || (receiptId != null && receiptId.equals(other.receiptId)))
            && (statusId == other.statusId || (statusId != null && statusId.equals(other.statusId)))
            && (inspectionTypeId == other.inspectionTypeId || (inspectionTypeId != null && inspectionTypeId.equals(other.inspectionTypeId)))
            && (productId == other.productId || (productId != null && productId.equals(other.productId)))
            && (lotId == other.lotId || (lotId != null && lotId.equals(other.lotId)))
            && (supplierId == other.supplierId || (supplierId != null && supplierId.equals(other.supplierId)))
            && (comments == other.comments || (comments != null && comments.equals(other.comments)))
            && (inspectionFacilityId == other.inspectionFacilityId || (inspectionFacilityId != null && inspectionFacilityId.equals(other.inspectionFacilityId)))
            && (inspectedBy == other.inspectedBy || (inspectedBy != null && inspectedBy.equals(other.inspectedBy)))
            && (createdBy == other.createdBy || (createdBy != null && createdBy.equals(other.createdBy)))
            && (inspectedAt == other.inspectedAt || (inspectedAt != null && inspectedAt.equals(other.inspectedAt)))
            && (createdAt == other.createdAt || (createdAt != null && createdAt.equals(other.createdAt)))
            && (updatedAt == other.updatedAt || (updatedAt != null && updatedAt.equals(other.updatedAt)))
            ;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        if (this.qaInspectionId != null) {
            hash += 13 * this.qaInspectionId.hashCode();
        }
        if (this.receiptId != null) {
            hash += 13 * this.receiptId.hashCode();
        }
        if (this.statusId != null) {
            hash += 13 * this.statusId.hashCode();
        }
        if (this.inspectionTypeId != null) {
            hash += 13 * this.inspectionTypeId.hashCode();
        }
        if (this.productId != null) {
            hash += 13 * this.productId.hashCode();
        }
        if (this.lotId != null) {
            hash += 13 * this.lotId.hashCode();
        }
        if (this.supplierId != null) {
            hash += 13 * this.supplierId.hashCode();
        }
        if (this.comments != null) {
            hash += 13 * this.comments.hashCode();
        }
        if (this.inspectionFacilityId != null) {
            hash += 13 * this.inspectionFacilityId.hashCode();
        }
        if (this.inspectedBy != null) {
            hash += 13 * this.inspectedBy.hashCode();
        }
        if (this.createdBy != null) {
            hash += 13 * this.createdBy.hashCode();
        }
        if (this.inspectedAt != null) {
            hash += 13 * this.inspectedAt.hashCode();
        }
        if (this.createdAt != null) {
            hash += 13 * this.createdAt.hashCode();
        }
        if (this.updatedAt != null) {
            hash += 13 * this.updatedAt.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "BffQaInspectionDto{" +
                "qaInspectionId=" + '\'' + qaInspectionId + '\'' +
                ", receiptId=" + '\'' + receiptId + '\'' +
                ", statusId=" + '\'' + statusId + '\'' +
                ", inspectionTypeId=" + '\'' + inspectionTypeId + '\'' +
                ", productId=" + '\'' + productId + '\'' +
                ", lotId=" + '\'' + lotId + '\'' +
                ", supplierId=" + '\'' + supplierId + '\'' +
                ", comments=" + '\'' + comments + '\'' +
                ", inspectionFacilityId=" + '\'' + inspectionFacilityId + '\'' +
                ", inspectedBy=" + '\'' + inspectedBy + '\'' +
                ", createdBy=" + '\'' + createdBy + '\'' +
                ", inspectedAt=" + inspectedAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }


}

