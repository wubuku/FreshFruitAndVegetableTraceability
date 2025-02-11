// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.inventoryitem;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.AbstractCommand;

public abstract class AbstractInventoryItemDetailCommand extends AbstractCommand implements InventoryItemDetailCommand {

    private String inventoryItemDetailSeqId;

    public String getInventoryItemDetailSeqId()
    {
        return this.inventoryItemDetailSeqId;
    }

    public void setInventoryItemDetailSeqId(String inventoryItemDetailSeqId)
    {
        this.inventoryItemDetailSeqId = inventoryItemDetailSeqId;
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


    public static abstract class AbstractCreateOrMergePatchInventoryItemDetail extends AbstractInventoryItemDetailCommand implements CreateOrMergePatchInventoryItemDetail
    {
        private OffsetDateTime effectiveDate;

        public OffsetDateTime getEffectiveDate()
        {
            return this.effectiveDate;
        }

        public void setEffectiveDate(OffsetDateTime effectiveDate)
        {
            this.effectiveDate = effectiveDate;
        }

        private java.math.BigDecimal quantityOnHandDiff;

        public java.math.BigDecimal getQuantityOnHandDiff()
        {
            return this.quantityOnHandDiff;
        }

        public void setQuantityOnHandDiff(java.math.BigDecimal quantityOnHandDiff)
        {
            this.quantityOnHandDiff = quantityOnHandDiff;
        }

        private java.math.BigDecimal availableToPromiseDiff;

        public java.math.BigDecimal getAvailableToPromiseDiff()
        {
            return this.availableToPromiseDiff;
        }

        public void setAvailableToPromiseDiff(java.math.BigDecimal availableToPromiseDiff)
        {
            this.availableToPromiseDiff = availableToPromiseDiff;
        }

        private java.math.BigDecimal accountingQuantityDiff;

        public java.math.BigDecimal getAccountingQuantityDiff()
        {
            return this.accountingQuantityDiff;
        }

        public void setAccountingQuantityDiff(java.math.BigDecimal accountingQuantityDiff)
        {
            this.accountingQuantityDiff = accountingQuantityDiff;
        }

        private java.math.BigDecimal unitCost;

        public java.math.BigDecimal getUnitCost()
        {
            return this.unitCost;
        }

        public void setUnitCost(java.math.BigDecimal unitCost)
        {
            this.unitCost = unitCost;
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

        private String inventoryItemAttributeHash;

        public String getInventoryItemAttributeHash()
        {
            return this.inventoryItemAttributeHash;
        }

        public void setInventoryItemAttributeHash(String inventoryItemAttributeHash)
        {
            this.inventoryItemAttributeHash = inventoryItemAttributeHash;
        }

        private String inventoryItemEntrySourceHash;

        public String getInventoryItemEntrySourceHash()
        {
            return this.inventoryItemEntrySourceHash;
        }

        public void setInventoryItemEntrySourceHash(String inventoryItemEntrySourceHash)
        {
            this.inventoryItemEntrySourceHash = inventoryItemEntrySourceHash;
        }

    }

    public static abstract class AbstractCreateInventoryItemDetail extends AbstractCreateOrMergePatchInventoryItemDetail implements CreateInventoryItemDetail
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }

    }

    public static class SimpleCreateInventoryItemDetail extends AbstractCreateInventoryItemDetail
    {
    }

    

}

