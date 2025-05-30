// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.inventoryitem;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.inventoryitem.InventoryItemDetailEvent.*;

public abstract class AbstractInventoryItemDetailState implements InventoryItemDetailState.SqlInventoryItemDetailState {

    private InventoryItemDetailId inventoryItemDetailId = new InventoryItemDetailId();

    public InventoryItemDetailId getInventoryItemDetailId() {
        return this.inventoryItemDetailId;
    }

    public void setInventoryItemDetailId(InventoryItemDetailId inventoryItemDetailId) {
        this.inventoryItemDetailId = inventoryItemDetailId;
    }

    private transient InventoryItemState inventoryItemState;

    public InventoryItemState getInventoryItemState() {
        return inventoryItemState;
    }

    public void setInventoryItemState(InventoryItemState s) {
        inventoryItemState = s;
    }
    
    public String getInventoryItemId() {
        return this.getInventoryItemDetailId().getInventoryItemId();
    }
        
    public void setInventoryItemId(String inventoryItemId) {
        this.getInventoryItemDetailId().setInventoryItemId(inventoryItemId);
    }

    public String getInventoryItemDetailSeqId() {
        return this.getInventoryItemDetailId().getInventoryItemDetailSeqId();
    }
        
    public void setInventoryItemDetailSeqId(String inventoryItemDetailSeqId) {
        this.getInventoryItemDetailId().setInventoryItemDetailSeqId(inventoryItemDetailSeqId);
    }

    private OffsetDateTime effectiveDate;

    public OffsetDateTime getEffectiveDate() {
        return this.effectiveDate;
    }

    public void setEffectiveDate(OffsetDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    private java.math.BigDecimal quantityOnHandDiff;

    public java.math.BigDecimal getQuantityOnHandDiff() {
        return this.quantityOnHandDiff;
    }

    public void setQuantityOnHandDiff(java.math.BigDecimal quantityOnHandDiff) {
        this.quantityOnHandDiff = quantityOnHandDiff;
    }

    private java.math.BigDecimal availableToPromiseDiff;

    public java.math.BigDecimal getAvailableToPromiseDiff() {
        return this.availableToPromiseDiff;
    }

    public void setAvailableToPromiseDiff(java.math.BigDecimal availableToPromiseDiff) {
        this.availableToPromiseDiff = availableToPromiseDiff;
    }

    private java.math.BigDecimal accountingQuantityDiff;

    public java.math.BigDecimal getAccountingQuantityDiff() {
        return this.accountingQuantityDiff;
    }

    public void setAccountingQuantityDiff(java.math.BigDecimal accountingQuantityDiff) {
        this.accountingQuantityDiff = accountingQuantityDiff;
    }

    private java.math.BigDecimal unitCost;

    public java.math.BigDecimal getUnitCost() {
        return this.unitCost;
    }

    public void setUnitCost(java.math.BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    private String orderId;

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    private String orderItemSeqId;

    public String getOrderItemSeqId() {
        return this.orderItemSeqId;
    }

    public void setOrderItemSeqId(String orderItemSeqId) {
        this.orderItemSeqId = orderItemSeqId;
    }

    private String shipGroupSeqId;

    public String getShipGroupSeqId() {
        return this.shipGroupSeqId;
    }

    public void setShipGroupSeqId(String shipGroupSeqId) {
        this.shipGroupSeqId = shipGroupSeqId;
    }

    private String shipmentId;

    public String getShipmentId() {
        return this.shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    private String shipmentItemSeqId;

    public String getShipmentItemSeqId() {
        return this.shipmentItemSeqId;
    }

    public void setShipmentItemSeqId(String shipmentItemSeqId) {
        this.shipmentItemSeqId = shipmentItemSeqId;
    }

    private String returnId;

    public String getReturnId() {
        return this.returnId;
    }

    public void setReturnId(String returnId) {
        this.returnId = returnId;
    }

    private String returnItemSeqId;

    public String getReturnItemSeqId() {
        return this.returnItemSeqId;
    }

    public void setReturnItemSeqId(String returnItemSeqId) {
        this.returnItemSeqId = returnItemSeqId;
    }

    private String workEffortId;

    public String getWorkEffortId() {
        return this.workEffortId;
    }

    public void setWorkEffortId(String workEffortId) {
        this.workEffortId = workEffortId;
    }

    private String fixedAssetId;

    public String getFixedAssetId() {
        return this.fixedAssetId;
    }

    public void setFixedAssetId(String fixedAssetId) {
        this.fixedAssetId = fixedAssetId;
    }

    private String maintHistSeqId;

    public String getMaintHistSeqId() {
        return this.maintHistSeqId;
    }

    public void setMaintHistSeqId(String maintHistSeqId) {
        this.maintHistSeqId = maintHistSeqId;
    }

    private String itemIssuanceId;

    public String getItemIssuanceId() {
        return this.itemIssuanceId;
    }

    public void setItemIssuanceId(String itemIssuanceId) {
        this.itemIssuanceId = itemIssuanceId;
    }

    private String receiptId;

    public String getReceiptId() {
        return this.receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    private String physicalInventoryId;

    public String getPhysicalInventoryId() {
        return this.physicalInventoryId;
    }

    public void setPhysicalInventoryId(String physicalInventoryId) {
        this.physicalInventoryId = physicalInventoryId;
    }

    private String reasonEnumId;

    public String getReasonEnumId() {
        return this.reasonEnumId;
    }

    public void setReasonEnumId(String reasonEnumId) {
        this.reasonEnumId = reasonEnumId;
    }

    private String description;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String inventoryTransferId;

    public String getInventoryTransferId() {
        return this.inventoryTransferId;
    }

    public void setInventoryTransferId(String inventoryTransferId) {
        this.inventoryTransferId = inventoryTransferId;
    }

    private String inventoryItemAttributeHash;

    public String getInventoryItemAttributeHash() {
        return this.inventoryItemAttributeHash;
    }

    public void setInventoryItemAttributeHash(String inventoryItemAttributeHash) {
        this.inventoryItemAttributeHash = inventoryItemAttributeHash;
    }

    private String inventoryItemDetailAttributeHash;

    public String getInventoryItemDetailAttributeHash() {
        return this.inventoryItemDetailAttributeHash;
    }

    public void setInventoryItemDetailAttributeHash(String inventoryItemDetailAttributeHash) {
        this.inventoryItemDetailAttributeHash = inventoryItemDetailAttributeHash;
    }

    private Long version;

    public Long getVersion() {
        return this.version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    private String createdBy;

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    private OffsetDateTime createdAt;

    public OffsetDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    private String updatedBy;

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    private OffsetDateTime updatedAt;

    public OffsetDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isStateUnsaved() {
        return this.getVersion() == null;
    }

    private Boolean stateReadOnly;

    public Boolean getStateReadOnly() { return this.stateReadOnly; }

    public void setStateReadOnly(Boolean readOnly) { this.stateReadOnly = readOnly; }

    private boolean forReapplying;

    public boolean getForReapplying() {
        return forReapplying;
    }

    public void setForReapplying(boolean forReapplying) {
        this.forReapplying = forReapplying;
    }

    private String commandId;

    public String getCommandId() {
        return this.commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }


    public AbstractInventoryItemDetailState() {
        initializeProperties();
    }

    protected void initializeForReapplying() {
        this.forReapplying = true;

        initializeProperties();
    }
    
    protected void initializeProperties() {
    }

    @Override
    public int hashCode() {
        return getInventoryItemDetailSeqId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj instanceof InventoryItemDetailState) {
            return Objects.equals(this.getInventoryItemDetailSeqId(), ((InventoryItemDetailState)obj).getInventoryItemDetailSeqId());
        }
        return false;
    }


    public void mutate(Event e) {
        setStateReadOnly(false);
        if (false) { 
            ;
        } else {
            throw new UnsupportedOperationException(String.format("Unsupported event type: %1$s", e.getClass().getName()));
        }
    }

    public void merge(InventoryItemDetailState s) {
        if (s == this) {
            return;
        }
        this.setEffectiveDate(s.getEffectiveDate());
        this.setQuantityOnHandDiff(s.getQuantityOnHandDiff());
        this.setAvailableToPromiseDiff(s.getAvailableToPromiseDiff());
        this.setAccountingQuantityDiff(s.getAccountingQuantityDiff());
        this.setUnitCost(s.getUnitCost());
        this.setOrderId(s.getOrderId());
        this.setOrderItemSeqId(s.getOrderItemSeqId());
        this.setShipGroupSeqId(s.getShipGroupSeqId());
        this.setShipmentId(s.getShipmentId());
        this.setShipmentItemSeqId(s.getShipmentItemSeqId());
        this.setReturnId(s.getReturnId());
        this.setReturnItemSeqId(s.getReturnItemSeqId());
        this.setWorkEffortId(s.getWorkEffortId());
        this.setFixedAssetId(s.getFixedAssetId());
        this.setMaintHistSeqId(s.getMaintHistSeqId());
        this.setItemIssuanceId(s.getItemIssuanceId());
        this.setReceiptId(s.getReceiptId());
        this.setPhysicalInventoryId(s.getPhysicalInventoryId());
        this.setReasonEnumId(s.getReasonEnumId());
        this.setDescription(s.getDescription());
        this.setInventoryTransferId(s.getInventoryTransferId());
        this.setInventoryItemAttributeHash(s.getInventoryItemAttributeHash());
        this.setInventoryItemDetailAttributeHash(s.getInventoryItemDetailAttributeHash());
    }

    public void save() {
    }

    protected void throwOnWrongEvent(InventoryItemDetailEvent event) {
        String stateEntityIdInventoryItemId = this.getInventoryItemDetailId().getInventoryItemId();
        String eventEntityIdInventoryItemId = ((InventoryItemDetailEvent.SqlInventoryItemDetailEvent)event).getInventoryItemDetailEventId().getInventoryItemId();
        if (!stateEntityIdInventoryItemId.equals(eventEntityIdInventoryItemId)) {
            throw DomainError.named("mutateWrongEntity", "Entity Id InventoryItemId %1$s in state but entity id InventoryItemId %2$s in event", stateEntityIdInventoryItemId, eventEntityIdInventoryItemId);
        }

        String stateEntityIdInventoryItemDetailSeqId = this.getInventoryItemDetailId().getInventoryItemDetailSeqId();
        String eventEntityIdInventoryItemDetailSeqId = ((InventoryItemDetailEvent.SqlInventoryItemDetailEvent)event).getInventoryItemDetailEventId().getInventoryItemDetailSeqId();
        if (!stateEntityIdInventoryItemDetailSeqId.equals(eventEntityIdInventoryItemDetailSeqId)) {
            throw DomainError.named("mutateWrongEntity", "Entity Id InventoryItemDetailSeqId %1$s in state but entity id InventoryItemDetailSeqId %2$s in event", stateEntityIdInventoryItemDetailSeqId, eventEntityIdInventoryItemDetailSeqId);
        }


        if (getForReapplying()) { return; }

    }


    public static class SimpleInventoryItemDetailState extends AbstractInventoryItemDetailState {

        public SimpleInventoryItemDetailState() {
        }

        public static SimpleInventoryItemDetailState newForReapplying() {
            SimpleInventoryItemDetailState s = new SimpleInventoryItemDetailState();
            s.initializeForReapplying();
            return s;
        }

    }



}

