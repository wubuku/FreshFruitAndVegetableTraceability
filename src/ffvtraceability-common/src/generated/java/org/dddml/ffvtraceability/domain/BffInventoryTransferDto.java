// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain;

import java.io.Serializable;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public class BffInventoryTransferDto implements Serializable {
    private String inventoryTransferId;

    public String getInventoryTransferId()
    {
        return this.inventoryTransferId;
    }

    public void setInventoryTransferId(String inventoryTransferId)
    {
        this.inventoryTransferId = inventoryTransferId;
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

    private String inventoryItemId;

    public String getInventoryItemId()
    {
        return this.inventoryItemId;
    }

    public void setInventoryItemId(String inventoryItemId)
    {
        this.inventoryItemId = inventoryItemId;
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

    private String locationSeqId;

    public String getLocationSeqId()
    {
        return this.locationSeqId;
    }

    public void setLocationSeqId(String locationSeqId)
    {
        this.locationSeqId = locationSeqId;
    }

    private String containerId;

    public String getContainerId()
    {
        return this.containerId;
    }

    public void setContainerId(String containerId)
    {
        this.containerId = containerId;
    }

    private String facilityIdTo;

    public String getFacilityIdTo()
    {
        return this.facilityIdTo;
    }

    public void setFacilityIdTo(String facilityIdTo)
    {
        this.facilityIdTo = facilityIdTo;
    }

    private String locationSeqIdTo;

    public String getLocationSeqIdTo()
    {
        return this.locationSeqIdTo;
    }

    public void setLocationSeqIdTo(String locationSeqIdTo)
    {
        this.locationSeqIdTo = locationSeqIdTo;
    }

    private String containerIdTo;

    public String getContainerIdTo()
    {
        return this.containerIdTo;
    }

    public void setContainerIdTo(String containerIdTo)
    {
        this.containerIdTo = containerIdTo;
    }

    private String itemIssuanceId;

    public String getItemIssuanceId()
    {
        return this.itemIssuanceId;
    }

    public void setItemIssuanceId(String itemIssuanceId)
    {
        this.itemIssuanceId = itemIssuanceId;
    }

    private OffsetDateTime sendDate;

    public OffsetDateTime getSendDate()
    {
        return this.sendDate;
    }

    public void setSendDate(OffsetDateTime sendDate)
    {
        this.sendDate = sendDate;
    }

    private OffsetDateTime receiveDate;

    public OffsetDateTime getReceiveDate()
    {
        return this.receiveDate;
    }

    public void setReceiveDate(OffsetDateTime receiveDate)
    {
        this.receiveDate = receiveDate;
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

    public BffInventoryTransferDto()
    {
    }

    public BffInventoryTransferDto(String inventoryTransferId, String statusId, String inventoryItemId, String facilityId, String locationSeqId, String containerId, String facilityIdTo, String locationSeqIdTo, String containerIdTo, String itemIssuanceId, OffsetDateTime sendDate, OffsetDateTime receiveDate, String comments)
    {
        this.inventoryTransferId = inventoryTransferId;
        this.statusId = statusId;
        this.inventoryItemId = inventoryItemId;
        this.facilityId = facilityId;
        this.locationSeqId = locationSeqId;
        this.containerId = containerId;
        this.facilityIdTo = facilityIdTo;
        this.locationSeqIdTo = locationSeqIdTo;
        this.containerIdTo = containerIdTo;
        this.itemIssuanceId = itemIssuanceId;
        this.sendDate = sendDate;
        this.receiveDate = receiveDate;
        this.comments = comments;
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

        BffInventoryTransferDto other = (BffInventoryTransferDto)obj;
        return true 
            && (inventoryTransferId == other.inventoryTransferId || (inventoryTransferId != null && inventoryTransferId.equals(other.inventoryTransferId)))
            && (statusId == other.statusId || (statusId != null && statusId.equals(other.statusId)))
            && (inventoryItemId == other.inventoryItemId || (inventoryItemId != null && inventoryItemId.equals(other.inventoryItemId)))
            && (facilityId == other.facilityId || (facilityId != null && facilityId.equals(other.facilityId)))
            && (locationSeqId == other.locationSeqId || (locationSeqId != null && locationSeqId.equals(other.locationSeqId)))
            && (containerId == other.containerId || (containerId != null && containerId.equals(other.containerId)))
            && (facilityIdTo == other.facilityIdTo || (facilityIdTo != null && facilityIdTo.equals(other.facilityIdTo)))
            && (locationSeqIdTo == other.locationSeqIdTo || (locationSeqIdTo != null && locationSeqIdTo.equals(other.locationSeqIdTo)))
            && (containerIdTo == other.containerIdTo || (containerIdTo != null && containerIdTo.equals(other.containerIdTo)))
            && (itemIssuanceId == other.itemIssuanceId || (itemIssuanceId != null && itemIssuanceId.equals(other.itemIssuanceId)))
            && (sendDate == other.sendDate || (sendDate != null && sendDate.equals(other.sendDate)))
            && (receiveDate == other.receiveDate || (receiveDate != null && receiveDate.equals(other.receiveDate)))
            && (comments == other.comments || (comments != null && comments.equals(other.comments)))
            ;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        if (this.inventoryTransferId != null) {
            hash += 13 * this.inventoryTransferId.hashCode();
        }
        if (this.statusId != null) {
            hash += 13 * this.statusId.hashCode();
        }
        if (this.inventoryItemId != null) {
            hash += 13 * this.inventoryItemId.hashCode();
        }
        if (this.facilityId != null) {
            hash += 13 * this.facilityId.hashCode();
        }
        if (this.locationSeqId != null) {
            hash += 13 * this.locationSeqId.hashCode();
        }
        if (this.containerId != null) {
            hash += 13 * this.containerId.hashCode();
        }
        if (this.facilityIdTo != null) {
            hash += 13 * this.facilityIdTo.hashCode();
        }
        if (this.locationSeqIdTo != null) {
            hash += 13 * this.locationSeqIdTo.hashCode();
        }
        if (this.containerIdTo != null) {
            hash += 13 * this.containerIdTo.hashCode();
        }
        if (this.itemIssuanceId != null) {
            hash += 13 * this.itemIssuanceId.hashCode();
        }
        if (this.sendDate != null) {
            hash += 13 * this.sendDate.hashCode();
        }
        if (this.receiveDate != null) {
            hash += 13 * this.receiveDate.hashCode();
        }
        if (this.comments != null) {
            hash += 13 * this.comments.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "BffInventoryTransferDto{" +
                "inventoryTransferId=" + '\'' + inventoryTransferId + '\'' +
                ", statusId=" + '\'' + statusId + '\'' +
                ", inventoryItemId=" + '\'' + inventoryItemId + '\'' +
                ", facilityId=" + '\'' + facilityId + '\'' +
                ", locationSeqId=" + '\'' + locationSeqId + '\'' +
                ", containerId=" + '\'' + containerId + '\'' +
                ", facilityIdTo=" + '\'' + facilityIdTo + '\'' +
                ", locationSeqIdTo=" + '\'' + locationSeqIdTo + '\'' +
                ", containerIdTo=" + '\'' + containerIdTo + '\'' +
                ", itemIssuanceId=" + '\'' + itemIssuanceId + '\'' +
                ", sendDate=" + sendDate +
                ", receiveDate=" + receiveDate +
                ", comments=" + '\'' + comments + '\'' +
                '}';
    }


}

