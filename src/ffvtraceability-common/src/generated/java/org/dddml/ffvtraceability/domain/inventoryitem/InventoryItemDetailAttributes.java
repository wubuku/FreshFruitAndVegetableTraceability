// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.inventoryitem;

import java.io.Serializable;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public class InventoryItemDetailAttributes implements Serializable {
    private OffsetDateTime effectiveDate;

    public OffsetDateTime getEffectiveDate()
    {
        return this.effectiveDate;
    }

    public void setEffectiveDate(OffsetDateTime effectiveDate)
    {
        this.effectiveDate = effectiveDate;
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

    private String orderItemSeqId;

    public String getOrderItemSeqId()
    {
        return this.orderItemSeqId;
    }

    public void setOrderItemSeqId(String orderItemSeqId)
    {
        this.orderItemSeqId = orderItemSeqId;
    }

    private String shipGroupSeqId;

    public String getShipGroupSeqId()
    {
        return this.shipGroupSeqId;
    }

    public void setShipGroupSeqId(String shipGroupSeqId)
    {
        this.shipGroupSeqId = shipGroupSeqId;
    }

    private String shipmentId;

    public String getShipmentId()
    {
        return this.shipmentId;
    }

    public void setShipmentId(String shipmentId)
    {
        this.shipmentId = shipmentId;
    }

    private String shipmentItemSeqId;

    public String getShipmentItemSeqId()
    {
        return this.shipmentItemSeqId;
    }

    public void setShipmentItemSeqId(String shipmentItemSeqId)
    {
        this.shipmentItemSeqId = shipmentItemSeqId;
    }

    private String returnId;

    public String getReturnId()
    {
        return this.returnId;
    }

    public void setReturnId(String returnId)
    {
        this.returnId = returnId;
    }

    private String returnItemSeqId;

    public String getReturnItemSeqId()
    {
        return this.returnItemSeqId;
    }

    public void setReturnItemSeqId(String returnItemSeqId)
    {
        this.returnItemSeqId = returnItemSeqId;
    }

    private String workEffortId;

    public String getWorkEffortId()
    {
        return this.workEffortId;
    }

    public void setWorkEffortId(String workEffortId)
    {
        this.workEffortId = workEffortId;
    }

    private String fixedAssetId;

    public String getFixedAssetId()
    {
        return this.fixedAssetId;
    }

    public void setFixedAssetId(String fixedAssetId)
    {
        this.fixedAssetId = fixedAssetId;
    }

    private String maintHistSeqId;

    public String getMaintHistSeqId()
    {
        return this.maintHistSeqId;
    }

    public void setMaintHistSeqId(String maintHistSeqId)
    {
        this.maintHistSeqId = maintHistSeqId;
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

    private String receiptId;

    public String getReceiptId()
    {
        return this.receiptId;
    }

    public void setReceiptId(String receiptId)
    {
        this.receiptId = receiptId;
    }

    private String physicalInventoryId;

    public String getPhysicalInventoryId()
    {
        return this.physicalInventoryId;
    }

    public void setPhysicalInventoryId(String physicalInventoryId)
    {
        this.physicalInventoryId = physicalInventoryId;
    }

    private String reasonEnumId;

    public String getReasonEnumId()
    {
        return this.reasonEnumId;
    }

    public void setReasonEnumId(String reasonEnumId)
    {
        this.reasonEnumId = reasonEnumId;
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

    private String inventoryTransferId;

    public String getInventoryTransferId()
    {
        return this.inventoryTransferId;
    }

    public void setInventoryTransferId(String inventoryTransferId)
    {
        this.inventoryTransferId = inventoryTransferId;
    }

    public InventoryItemDetailAttributes()
    {
    }

    public InventoryItemDetailAttributes(OffsetDateTime effectiveDate, String orderId, String orderItemSeqId, String shipGroupSeqId, String shipmentId, String shipmentItemSeqId, String returnId, String returnItemSeqId, String workEffortId, String fixedAssetId, String maintHistSeqId, String itemIssuanceId, String receiptId, String physicalInventoryId, String reasonEnumId, String description, String inventoryTransferId)
    {
        this.effectiveDate = effectiveDate;
        this.orderId = orderId;
        this.orderItemSeqId = orderItemSeqId;
        this.shipGroupSeqId = shipGroupSeqId;
        this.shipmentId = shipmentId;
        this.shipmentItemSeqId = shipmentItemSeqId;
        this.returnId = returnId;
        this.returnItemSeqId = returnItemSeqId;
        this.workEffortId = workEffortId;
        this.fixedAssetId = fixedAssetId;
        this.maintHistSeqId = maintHistSeqId;
        this.itemIssuanceId = itemIssuanceId;
        this.receiptId = receiptId;
        this.physicalInventoryId = physicalInventoryId;
        this.reasonEnumId = reasonEnumId;
        this.description = description;
        this.inventoryTransferId = inventoryTransferId;
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

        InventoryItemDetailAttributes other = (InventoryItemDetailAttributes)obj;
        return true 
            && (effectiveDate == other.effectiveDate || (effectiveDate != null && effectiveDate.equals(other.effectiveDate)))
            && (orderId == other.orderId || (orderId != null && orderId.equals(other.orderId)))
            && (orderItemSeqId == other.orderItemSeqId || (orderItemSeqId != null && orderItemSeqId.equals(other.orderItemSeqId)))
            && (shipGroupSeqId == other.shipGroupSeqId || (shipGroupSeqId != null && shipGroupSeqId.equals(other.shipGroupSeqId)))
            && (shipmentId == other.shipmentId || (shipmentId != null && shipmentId.equals(other.shipmentId)))
            && (shipmentItemSeqId == other.shipmentItemSeqId || (shipmentItemSeqId != null && shipmentItemSeqId.equals(other.shipmentItemSeqId)))
            && (returnId == other.returnId || (returnId != null && returnId.equals(other.returnId)))
            && (returnItemSeqId == other.returnItemSeqId || (returnItemSeqId != null && returnItemSeqId.equals(other.returnItemSeqId)))
            && (workEffortId == other.workEffortId || (workEffortId != null && workEffortId.equals(other.workEffortId)))
            && (fixedAssetId == other.fixedAssetId || (fixedAssetId != null && fixedAssetId.equals(other.fixedAssetId)))
            && (maintHistSeqId == other.maintHistSeqId || (maintHistSeqId != null && maintHistSeqId.equals(other.maintHistSeqId)))
            && (itemIssuanceId == other.itemIssuanceId || (itemIssuanceId != null && itemIssuanceId.equals(other.itemIssuanceId)))
            && (receiptId == other.receiptId || (receiptId != null && receiptId.equals(other.receiptId)))
            && (physicalInventoryId == other.physicalInventoryId || (physicalInventoryId != null && physicalInventoryId.equals(other.physicalInventoryId)))
            && (reasonEnumId == other.reasonEnumId || (reasonEnumId != null && reasonEnumId.equals(other.reasonEnumId)))
            && (description == other.description || (description != null && description.equals(other.description)))
            && (inventoryTransferId == other.inventoryTransferId || (inventoryTransferId != null && inventoryTransferId.equals(other.inventoryTransferId)))
            ;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        if (this.effectiveDate != null) {
            hash += 13 * this.effectiveDate.hashCode();
        }
        if (this.orderId != null) {
            hash += 13 * this.orderId.hashCode();
        }
        if (this.orderItemSeqId != null) {
            hash += 13 * this.orderItemSeqId.hashCode();
        }
        if (this.shipGroupSeqId != null) {
            hash += 13 * this.shipGroupSeqId.hashCode();
        }
        if (this.shipmentId != null) {
            hash += 13 * this.shipmentId.hashCode();
        }
        if (this.shipmentItemSeqId != null) {
            hash += 13 * this.shipmentItemSeqId.hashCode();
        }
        if (this.returnId != null) {
            hash += 13 * this.returnId.hashCode();
        }
        if (this.returnItemSeqId != null) {
            hash += 13 * this.returnItemSeqId.hashCode();
        }
        if (this.workEffortId != null) {
            hash += 13 * this.workEffortId.hashCode();
        }
        if (this.fixedAssetId != null) {
            hash += 13 * this.fixedAssetId.hashCode();
        }
        if (this.maintHistSeqId != null) {
            hash += 13 * this.maintHistSeqId.hashCode();
        }
        if (this.itemIssuanceId != null) {
            hash += 13 * this.itemIssuanceId.hashCode();
        }
        if (this.receiptId != null) {
            hash += 13 * this.receiptId.hashCode();
        }
        if (this.physicalInventoryId != null) {
            hash += 13 * this.physicalInventoryId.hashCode();
        }
        if (this.reasonEnumId != null) {
            hash += 13 * this.reasonEnumId.hashCode();
        }
        if (this.description != null) {
            hash += 13 * this.description.hashCode();
        }
        if (this.inventoryTransferId != null) {
            hash += 13 * this.inventoryTransferId.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "InventoryItemDetailAttributes{" +
                "effectiveDate=" + effectiveDate +
                ", orderId=" + '\'' + orderId + '\'' +
                ", orderItemSeqId=" + '\'' + orderItemSeqId + '\'' +
                ", shipGroupSeqId=" + '\'' + shipGroupSeqId + '\'' +
                ", shipmentId=" + '\'' + shipmentId + '\'' +
                ", shipmentItemSeqId=" + '\'' + shipmentItemSeqId + '\'' +
                ", returnId=" + '\'' + returnId + '\'' +
                ", returnItemSeqId=" + '\'' + returnItemSeqId + '\'' +
                ", workEffortId=" + '\'' + workEffortId + '\'' +
                ", fixedAssetId=" + '\'' + fixedAssetId + '\'' +
                ", maintHistSeqId=" + '\'' + maintHistSeqId + '\'' +
                ", itemIssuanceId=" + '\'' + itemIssuanceId + '\'' +
                ", receiptId=" + '\'' + receiptId + '\'' +
                ", physicalInventoryId=" + '\'' + physicalInventoryId + '\'' +
                ", reasonEnumId=" + '\'' + reasonEnumId + '\'' +
                ", description=" + '\'' + description + '\'' +
                ", inventoryTransferId=" + '\'' + inventoryTransferId + '\'' +
                '}';
    }


}

