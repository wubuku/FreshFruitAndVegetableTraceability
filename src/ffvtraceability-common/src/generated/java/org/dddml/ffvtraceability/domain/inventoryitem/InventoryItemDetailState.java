// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.inventoryitem;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface InventoryItemDetailState
{
    Long VERSION_ZERO = 0L;

    Long VERSION_NULL = VERSION_ZERO - 1;

    String getInventoryItemDetailSeqId();

    OffsetDateTime getEffectiveDate();

    java.math.BigDecimal getQuantityOnHandDiff();

    java.math.BigDecimal getAvailableToPromiseDiff();

    java.math.BigDecimal getAccountingQuantityDiff();

    java.math.BigDecimal getUnitCost();

    String getOrderId();

    String getOrderItemSeqId();

    String getShipGroupSeqId();

    String getShipmentId();

    String getShipmentItemSeqId();

    String getReturnId();

    String getReturnItemSeqId();

    String getWorkEffortId();

    String getFixedAssetId();

    String getMaintHistSeqId();

    String getItemIssuanceId();

    String getReceiptId();

    String getPhysicalInventoryId();

    String getReasonEnumId();

    String getDescription();

    String getInventoryTransferId();

    String getInventoryItemAttributeHash();

    String getInventoryItemDetailAttributeHash();

    Long getVersion();

    String getCreatedBy();

    OffsetDateTime getCreatedAt();

    String getUpdatedBy();

    OffsetDateTime getUpdatedAt();

    String getInventoryItemId();

    String getCommandId();

    interface MutableInventoryItemDetailState extends InventoryItemDetailState {
        void setInventoryItemDetailSeqId(String inventoryItemDetailSeqId);

        void setEffectiveDate(OffsetDateTime effectiveDate);

        void setQuantityOnHandDiff(java.math.BigDecimal quantityOnHandDiff);

        void setAvailableToPromiseDiff(java.math.BigDecimal availableToPromiseDiff);

        void setAccountingQuantityDiff(java.math.BigDecimal accountingQuantityDiff);

        void setUnitCost(java.math.BigDecimal unitCost);

        void setOrderId(String orderId);

        void setOrderItemSeqId(String orderItemSeqId);

        void setShipGroupSeqId(String shipGroupSeqId);

        void setShipmentId(String shipmentId);

        void setShipmentItemSeqId(String shipmentItemSeqId);

        void setReturnId(String returnId);

        void setReturnItemSeqId(String returnItemSeqId);

        void setWorkEffortId(String workEffortId);

        void setFixedAssetId(String fixedAssetId);

        void setMaintHistSeqId(String maintHistSeqId);

        void setItemIssuanceId(String itemIssuanceId);

        void setReceiptId(String receiptId);

        void setPhysicalInventoryId(String physicalInventoryId);

        void setReasonEnumId(String reasonEnumId);

        void setDescription(String description);

        void setInventoryTransferId(String inventoryTransferId);

        void setInventoryItemAttributeHash(String inventoryItemAttributeHash);

        void setInventoryItemDetailAttributeHash(String inventoryItemDetailAttributeHash);

        void setVersion(Long version);

        void setCreatedBy(String createdBy);

        void setCreatedAt(OffsetDateTime createdAt);

        void setUpdatedBy(String updatedBy);

        void setUpdatedAt(OffsetDateTime updatedAt);

        void setInventoryItemId(String inventoryItemId);

        void setCommandId(String commandId);


        void mutate(Event e);

        //void when(InventoryItemDetailEvent.InventoryItemDetailStateCreated e);

    }

    interface SqlInventoryItemDetailState extends MutableInventoryItemDetailState {
        InventoryItemDetailId getInventoryItemDetailId();

        void setInventoryItemDetailId(InventoryItemDetailId inventoryItemDetailId);


        boolean isStateUnsaved();

        boolean getForReapplying();
    }
}

