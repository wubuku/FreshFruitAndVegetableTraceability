// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.inventoryitem;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.AbstractEvent;

public abstract class AbstractInventoryItemEvent extends AbstractEvent implements InventoryItemEvent.SqlInventoryItemEvent {
    private InventoryItemEventId inventoryItemEventId = new InventoryItemEventId();

    public InventoryItemEventId getInventoryItemEventId() {
        return this.inventoryItemEventId;
    }

    public void setInventoryItemEventId(InventoryItemEventId eventId) {
        this.inventoryItemEventId = eventId;
    }
    
    public String getInventoryItemId() {
        return getInventoryItemEventId().getInventoryItemId();
    }

    public void setInventoryItemId(String inventoryItemId) {
        getInventoryItemEventId().setInventoryItemId(inventoryItemId);
    }

    private boolean eventReadOnly;

    public boolean getEventReadOnly() { return this.eventReadOnly; }

    public void setEventReadOnly(boolean readOnly) { this.eventReadOnly = readOnly; }

    public Long getVersion() {
        return getInventoryItemEventId().getVersion();
    }
    
    public void setVersion(Long version) {
        getInventoryItemEventId().setVersion(version);
    }

    private String tenantId;

    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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


    private String commandId;

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    private String commandType;

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    protected AbstractInventoryItemEvent() {
    }

    protected AbstractInventoryItemEvent(InventoryItemEventId eventId) {
        this.inventoryItemEventId = eventId;
    }

    protected InventoryItemDetailEventDao getInventoryItemDetailEventDao() {
        return (InventoryItemDetailEventDao)ApplicationContext.current.get("inventoryItemDetailEventDao");
    }

    protected InventoryItemDetailEventId newInventoryItemDetailEventId(String inventoryItemDetailSeqId)
    {
        InventoryItemDetailEventId eventId = new InventoryItemDetailEventId(this.getInventoryItemEventId().getInventoryItemId(), 
            inventoryItemDetailSeqId, 
            this.getInventoryItemEventId().getVersion());
        return eventId;
    }

    protected void throwOnInconsistentEventIds(InventoryItemDetailEvent.SqlInventoryItemDetailEvent e)
    {
        throwOnInconsistentEventIds(this, e);
    }

    public static void throwOnInconsistentEventIds(InventoryItemEvent.SqlInventoryItemEvent oe, InventoryItemDetailEvent.SqlInventoryItemDetailEvent e)
    {
        if (!oe.getInventoryItemEventId().getInventoryItemId().equals(e.getInventoryItemDetailEventId().getInventoryItemId()))
        { 
            throw DomainError.named("inconsistentEventIds", "Outer Id InventoryItemId %1$s but inner id InventoryItemId %2$s", 
                oe.getInventoryItemEventId().getInventoryItemId(), e.getInventoryItemDetailEventId().getInventoryItemId());
        }
    }


    public abstract String getEventType();

    public static class InventoryItemLobEvent extends AbstractInventoryItemEvent {

        public Map<String, Object> getDynamicProperties() {
            return dynamicProperties;
        }

        public void setDynamicProperties(Map<String, Object> dynamicProperties) {
            if (dynamicProperties == null) {
                throw new IllegalArgumentException("dynamicProperties is null.");
            }
            this.dynamicProperties = dynamicProperties;
        }

        private Map<String, Object> dynamicProperties = new HashMap<>();

        @Override
        public String getEventType() {
            return "InventoryItemLobEvent";
        }

    }

    public static class RecordInventoryEntryEvent extends InventoryItemLobEvent implements InventoryItemEvent.RecordInventoryEntryEvent {

        @Override
        public String getEventType() {
            return "RecordInventoryEntryEvent";
        }

        public InventoryItemAttributes getInventoryItemAttributes() {
            Object val = getDynamicProperties().get("inventoryItemAttributes");
            if (val instanceof InventoryItemAttributes) {
                return (InventoryItemAttributes) val;
            }
            return ApplicationContext.current.getTypeConverter().convertValue(val, InventoryItemAttributes.class);
        }

        public void setInventoryItemAttributes(InventoryItemAttributes value) {
            getDynamicProperties().put("inventoryItemAttributes", value);
        }

        public InventoryItemDetailAttributes getInventoryItemDetailAttributes() {
            Object val = getDynamicProperties().get("inventoryItemDetailAttributes");
            if (val instanceof InventoryItemDetailAttributes) {
                return (InventoryItemDetailAttributes) val;
            }
            return ApplicationContext.current.getTypeConverter().convertValue(val, InventoryItemDetailAttributes.class);
        }

        public void setInventoryItemDetailAttributes(InventoryItemDetailAttributes value) {
            getDynamicProperties().put("inventoryItemDetailAttributes", value);
        }

        public java.math.BigDecimal getQuantityOnHandDiff() {
            Object val = getDynamicProperties().get("quantityOnHandDiff");
            if (val instanceof java.math.BigDecimal) {
                return (java.math.BigDecimal) val;
            }
            return ApplicationContext.current.getTypeConverter().convertValue(val, java.math.BigDecimal.class);
        }

        public void setQuantityOnHandDiff(java.math.BigDecimal value) {
            getDynamicProperties().put("quantityOnHandDiff", value);
        }

        public java.math.BigDecimal getAvailableToPromiseDiff() {
            Object val = getDynamicProperties().get("availableToPromiseDiff");
            if (val instanceof java.math.BigDecimal) {
                return (java.math.BigDecimal) val;
            }
            return ApplicationContext.current.getTypeConverter().convertValue(val, java.math.BigDecimal.class);
        }

        public void setAvailableToPromiseDiff(java.math.BigDecimal value) {
            getDynamicProperties().put("availableToPromiseDiff", value);
        }

        public java.math.BigDecimal getAccountingQuantityDiff() {
            Object val = getDynamicProperties().get("accountingQuantityDiff");
            if (val instanceof java.math.BigDecimal) {
                return (java.math.BigDecimal) val;
            }
            return ApplicationContext.current.getTypeConverter().convertValue(val, java.math.BigDecimal.class);
        }

        public void setAccountingQuantityDiff(java.math.BigDecimal value) {
            getDynamicProperties().put("accountingQuantityDiff", value);
        }

        public java.math.BigDecimal getUnitCost() {
            Object val = getDynamicProperties().get("unitCost");
            if (val instanceof java.math.BigDecimal) {
                return (java.math.BigDecimal) val;
            }
            return ApplicationContext.current.getTypeConverter().convertValue(val, java.math.BigDecimal.class);
        }

        public void setUnitCost(java.math.BigDecimal value) {
            getDynamicProperties().put("unitCost", value);
        }

    }


    public static abstract class AbstractInventoryItemStateEvent extends AbstractInventoryItemEvent implements InventoryItemEvent.InventoryItemStateEvent {
        private String inventoryItemTypeId;

        public String getInventoryItemTypeId()
        {
            return this.inventoryItemTypeId;
        }

        public void setInventoryItemTypeId(String inventoryItemTypeId)
        {
            this.inventoryItemTypeId = inventoryItemTypeId;
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

        private String partyId;

        public String getPartyId()
        {
            return this.partyId;
        }

        public void setPartyId(String partyId)
        {
            this.partyId = partyId;
        }

        private String ownerPartyId;

        public String getOwnerPartyId()
        {
            return this.ownerPartyId;
        }

        public void setOwnerPartyId(String ownerPartyId)
        {
            this.ownerPartyId = ownerPartyId;
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

        private OffsetDateTime datetimeReceived;

        public OffsetDateTime getDatetimeReceived()
        {
            return this.datetimeReceived;
        }

        public void setDatetimeReceived(OffsetDateTime datetimeReceived)
        {
            this.datetimeReceived = datetimeReceived;
        }

        private OffsetDateTime datetimeManufactured;

        public OffsetDateTime getDatetimeManufactured()
        {
            return this.datetimeManufactured;
        }

        public void setDatetimeManufactured(OffsetDateTime datetimeManufactured)
        {
            this.datetimeManufactured = datetimeManufactured;
        }

        private OffsetDateTime expireDate;

        public OffsetDateTime getExpireDate()
        {
            return this.expireDate;
        }

        public void setExpireDate(OffsetDateTime expireDate)
        {
            this.expireDate = expireDate;
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

        private String containerId;

        public String getContainerId()
        {
            return this.containerId;
        }

        public void setContainerId(String containerId)
        {
            this.containerId = containerId;
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

        private String uomId;

        public String getUomId()
        {
            return this.uomId;
        }

        public void setUomId(String uomId)
        {
            this.uomId = uomId;
        }

        private String binNumber;

        public String getBinNumber()
        {
            return this.binNumber;
        }

        public void setBinNumber(String binNumber)
        {
            this.binNumber = binNumber;
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

        private String comments;

        public String getComments()
        {
            return this.comments;
        }

        public void setComments(String comments)
        {
            this.comments = comments;
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

        private java.math.BigDecimal availableToPromiseTotal;

        public java.math.BigDecimal getAvailableToPromiseTotal()
        {
            return this.availableToPromiseTotal;
        }

        public void setAvailableToPromiseTotal(java.math.BigDecimal availableToPromiseTotal)
        {
            this.availableToPromiseTotal = availableToPromiseTotal;
        }

        private java.math.BigDecimal accountingQuantityTotal;

        public java.math.BigDecimal getAccountingQuantityTotal()
        {
            return this.accountingQuantityTotal;
        }

        public void setAccountingQuantityTotal(java.math.BigDecimal accountingQuantityTotal)
        {
            this.accountingQuantityTotal = accountingQuantityTotal;
        }

        private String serialNumber;

        public String getSerialNumber()
        {
            return this.serialNumber;
        }

        public void setSerialNumber(String serialNumber)
        {
            this.serialNumber = serialNumber;
        }

        private String softIdentifier;

        public String getSoftIdentifier()
        {
            return this.softIdentifier;
        }

        public void setSoftIdentifier(String softIdentifier)
        {
            this.softIdentifier = softIdentifier;
        }

        private String activationNumber;

        public String getActivationNumber()
        {
            return this.activationNumber;
        }

        public void setActivationNumber(String activationNumber)
        {
            this.activationNumber = activationNumber;
        }

        private OffsetDateTime activationValidThru;

        public OffsetDateTime getActivationValidThru()
        {
            return this.activationValidThru;
        }

        public void setActivationValidThru(OffsetDateTime activationValidThru)
        {
            this.activationValidThru = activationValidThru;
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

        private String currencyUomId;

        public String getCurrencyUomId()
        {
            return this.currencyUomId;
        }

        public void setCurrencyUomId(String currencyUomId)
        {
            this.currencyUomId = currencyUomId;
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

        private String inventoryItemAttributeHash;

        public String getInventoryItemAttributeHash()
        {
            return this.inventoryItemAttributeHash;
        }

        public void setInventoryItemAttributeHash(String inventoryItemAttributeHash)
        {
            this.inventoryItemAttributeHash = inventoryItemAttributeHash;
        }

        protected AbstractInventoryItemStateEvent(InventoryItemEventId eventId) {
            super(eventId);
        }
    }

    public static abstract class AbstractInventoryItemStateCreated extends AbstractInventoryItemStateEvent implements InventoryItemEvent.InventoryItemStateCreated
    {
        public AbstractInventoryItemStateCreated() {
            this(new InventoryItemEventId());
        }

        public AbstractInventoryItemStateCreated(InventoryItemEventId eventId) {
            super(eventId);
        }

        public String getEventType() {
            return StateEventType.CREATED;
        }

    }


    public static abstract class AbstractInventoryItemStateMergePatched extends AbstractInventoryItemStateEvent implements InventoryItemEvent.InventoryItemStateMergePatched
    {
        public AbstractInventoryItemStateMergePatched() {
            this(new InventoryItemEventId());
        }

        public AbstractInventoryItemStateMergePatched(InventoryItemEventId eventId) {
            super(eventId);
        }

        public String getEventType() {
            return StateEventType.MERGE_PATCHED;
        }

        private Boolean isPropertyInventoryItemTypeIdRemoved;

        public Boolean getIsPropertyInventoryItemTypeIdRemoved() {
            return this.isPropertyInventoryItemTypeIdRemoved;
        }

        public void setIsPropertyInventoryItemTypeIdRemoved(Boolean removed) {
            this.isPropertyInventoryItemTypeIdRemoved = removed;
        }

        private Boolean isPropertyProductIdRemoved;

        public Boolean getIsPropertyProductIdRemoved() {
            return this.isPropertyProductIdRemoved;
        }

        public void setIsPropertyProductIdRemoved(Boolean removed) {
            this.isPropertyProductIdRemoved = removed;
        }

        private Boolean isPropertyPartyIdRemoved;

        public Boolean getIsPropertyPartyIdRemoved() {
            return this.isPropertyPartyIdRemoved;
        }

        public void setIsPropertyPartyIdRemoved(Boolean removed) {
            this.isPropertyPartyIdRemoved = removed;
        }

        private Boolean isPropertyOwnerPartyIdRemoved;

        public Boolean getIsPropertyOwnerPartyIdRemoved() {
            return this.isPropertyOwnerPartyIdRemoved;
        }

        public void setIsPropertyOwnerPartyIdRemoved(Boolean removed) {
            this.isPropertyOwnerPartyIdRemoved = removed;
        }

        private Boolean isPropertyStatusIdRemoved;

        public Boolean getIsPropertyStatusIdRemoved() {
            return this.isPropertyStatusIdRemoved;
        }

        public void setIsPropertyStatusIdRemoved(Boolean removed) {
            this.isPropertyStatusIdRemoved = removed;
        }

        private Boolean isPropertyDatetimeReceivedRemoved;

        public Boolean getIsPropertyDatetimeReceivedRemoved() {
            return this.isPropertyDatetimeReceivedRemoved;
        }

        public void setIsPropertyDatetimeReceivedRemoved(Boolean removed) {
            this.isPropertyDatetimeReceivedRemoved = removed;
        }

        private Boolean isPropertyDatetimeManufacturedRemoved;

        public Boolean getIsPropertyDatetimeManufacturedRemoved() {
            return this.isPropertyDatetimeManufacturedRemoved;
        }

        public void setIsPropertyDatetimeManufacturedRemoved(Boolean removed) {
            this.isPropertyDatetimeManufacturedRemoved = removed;
        }

        private Boolean isPropertyExpireDateRemoved;

        public Boolean getIsPropertyExpireDateRemoved() {
            return this.isPropertyExpireDateRemoved;
        }

        public void setIsPropertyExpireDateRemoved(Boolean removed) {
            this.isPropertyExpireDateRemoved = removed;
        }

        private Boolean isPropertyFacilityIdRemoved;

        public Boolean getIsPropertyFacilityIdRemoved() {
            return this.isPropertyFacilityIdRemoved;
        }

        public void setIsPropertyFacilityIdRemoved(Boolean removed) {
            this.isPropertyFacilityIdRemoved = removed;
        }

        private Boolean isPropertyContainerIdRemoved;

        public Boolean getIsPropertyContainerIdRemoved() {
            return this.isPropertyContainerIdRemoved;
        }

        public void setIsPropertyContainerIdRemoved(Boolean removed) {
            this.isPropertyContainerIdRemoved = removed;
        }

        private Boolean isPropertyLotIdRemoved;

        public Boolean getIsPropertyLotIdRemoved() {
            return this.isPropertyLotIdRemoved;
        }

        public void setIsPropertyLotIdRemoved(Boolean removed) {
            this.isPropertyLotIdRemoved = removed;
        }

        private Boolean isPropertyUomIdRemoved;

        public Boolean getIsPropertyUomIdRemoved() {
            return this.isPropertyUomIdRemoved;
        }

        public void setIsPropertyUomIdRemoved(Boolean removed) {
            this.isPropertyUomIdRemoved = removed;
        }

        private Boolean isPropertyBinNumberRemoved;

        public Boolean getIsPropertyBinNumberRemoved() {
            return this.isPropertyBinNumberRemoved;
        }

        public void setIsPropertyBinNumberRemoved(Boolean removed) {
            this.isPropertyBinNumberRemoved = removed;
        }

        private Boolean isPropertyLocationSeqIdRemoved;

        public Boolean getIsPropertyLocationSeqIdRemoved() {
            return this.isPropertyLocationSeqIdRemoved;
        }

        public void setIsPropertyLocationSeqIdRemoved(Boolean removed) {
            this.isPropertyLocationSeqIdRemoved = removed;
        }

        private Boolean isPropertyCommentsRemoved;

        public Boolean getIsPropertyCommentsRemoved() {
            return this.isPropertyCommentsRemoved;
        }

        public void setIsPropertyCommentsRemoved(Boolean removed) {
            this.isPropertyCommentsRemoved = removed;
        }

        private Boolean isPropertyQuantityOnHandTotalRemoved;

        public Boolean getIsPropertyQuantityOnHandTotalRemoved() {
            return this.isPropertyQuantityOnHandTotalRemoved;
        }

        public void setIsPropertyQuantityOnHandTotalRemoved(Boolean removed) {
            this.isPropertyQuantityOnHandTotalRemoved = removed;
        }

        private Boolean isPropertyAvailableToPromiseTotalRemoved;

        public Boolean getIsPropertyAvailableToPromiseTotalRemoved() {
            return this.isPropertyAvailableToPromiseTotalRemoved;
        }

        public void setIsPropertyAvailableToPromiseTotalRemoved(Boolean removed) {
            this.isPropertyAvailableToPromiseTotalRemoved = removed;
        }

        private Boolean isPropertyAccountingQuantityTotalRemoved;

        public Boolean getIsPropertyAccountingQuantityTotalRemoved() {
            return this.isPropertyAccountingQuantityTotalRemoved;
        }

        public void setIsPropertyAccountingQuantityTotalRemoved(Boolean removed) {
            this.isPropertyAccountingQuantityTotalRemoved = removed;
        }

        private Boolean isPropertySerialNumberRemoved;

        public Boolean getIsPropertySerialNumberRemoved() {
            return this.isPropertySerialNumberRemoved;
        }

        public void setIsPropertySerialNumberRemoved(Boolean removed) {
            this.isPropertySerialNumberRemoved = removed;
        }

        private Boolean isPropertySoftIdentifierRemoved;

        public Boolean getIsPropertySoftIdentifierRemoved() {
            return this.isPropertySoftIdentifierRemoved;
        }

        public void setIsPropertySoftIdentifierRemoved(Boolean removed) {
            this.isPropertySoftIdentifierRemoved = removed;
        }

        private Boolean isPropertyActivationNumberRemoved;

        public Boolean getIsPropertyActivationNumberRemoved() {
            return this.isPropertyActivationNumberRemoved;
        }

        public void setIsPropertyActivationNumberRemoved(Boolean removed) {
            this.isPropertyActivationNumberRemoved = removed;
        }

        private Boolean isPropertyActivationValidThruRemoved;

        public Boolean getIsPropertyActivationValidThruRemoved() {
            return this.isPropertyActivationValidThruRemoved;
        }

        public void setIsPropertyActivationValidThruRemoved(Boolean removed) {
            this.isPropertyActivationValidThruRemoved = removed;
        }

        private Boolean isPropertyUnitCostRemoved;

        public Boolean getIsPropertyUnitCostRemoved() {
            return this.isPropertyUnitCostRemoved;
        }

        public void setIsPropertyUnitCostRemoved(Boolean removed) {
            this.isPropertyUnitCostRemoved = removed;
        }

        private Boolean isPropertyCurrencyUomIdRemoved;

        public Boolean getIsPropertyCurrencyUomIdRemoved() {
            return this.isPropertyCurrencyUomIdRemoved;
        }

        public void setIsPropertyCurrencyUomIdRemoved(Boolean removed) {
            this.isPropertyCurrencyUomIdRemoved = removed;
        }

        private Boolean isPropertyFixedAssetIdRemoved;

        public Boolean getIsPropertyFixedAssetIdRemoved() {
            return this.isPropertyFixedAssetIdRemoved;
        }

        public void setIsPropertyFixedAssetIdRemoved(Boolean removed) {
            this.isPropertyFixedAssetIdRemoved = removed;
        }

        private Boolean isPropertyInventoryItemAttributeHashRemoved;

        public Boolean getIsPropertyInventoryItemAttributeHashRemoved() {
            return this.isPropertyInventoryItemAttributeHashRemoved;
        }

        public void setIsPropertyInventoryItemAttributeHashRemoved(Boolean removed) {
            this.isPropertyInventoryItemAttributeHashRemoved = removed;
        }


        private Map<InventoryItemDetailEventId, InventoryItemDetailEvent> inventoryItemDetailEvents = new HashMap<InventoryItemDetailEventId, InventoryItemDetailEvent>();
        
        private Iterable<InventoryItemDetailEvent> readOnlyInventoryItemDetailEvents;

        public Iterable<InventoryItemDetailEvent> getInventoryItemDetailEvents()
        {
            if (!getEventReadOnly())
            {
                return this.inventoryItemDetailEvents.values();
            }
            else
            {
                if (readOnlyInventoryItemDetailEvents != null) { return readOnlyInventoryItemDetailEvents; }
                InventoryItemDetailEventDao eventDao = getInventoryItemDetailEventDao();
                List<InventoryItemDetailEvent> eL = new ArrayList<InventoryItemDetailEvent>();
                for (InventoryItemDetailEvent e : eventDao.findByInventoryItemEventId(this.getInventoryItemEventId()))
                {
                    ((InventoryItemDetailEvent.SqlInventoryItemDetailEvent)e).setEventReadOnly(true);
                    eL.add((InventoryItemDetailEvent)e);
                }
                return (readOnlyInventoryItemDetailEvents = eL);
            }
        }

        public void setInventoryItemDetailEvents(Iterable<InventoryItemDetailEvent> es)
        {
            if (es != null)
            {
                for (InventoryItemDetailEvent e : es)
                {
                    addInventoryItemDetailEvent(e);
                }
            }
            else { this.inventoryItemDetailEvents.clear(); }
        }
        
        public void addInventoryItemDetailEvent(InventoryItemDetailEvent e)
        {
            throwOnInconsistentEventIds((InventoryItemDetailEvent.SqlInventoryItemDetailEvent)e);
            this.inventoryItemDetailEvents.put(((InventoryItemDetailEvent.SqlInventoryItemDetailEvent)e).getInventoryItemDetailEventId(), e);
        }

    }



    public static class SimpleInventoryItemStateCreated extends AbstractInventoryItemStateCreated
    {
        public SimpleInventoryItemStateCreated() {
        }

        public SimpleInventoryItemStateCreated(InventoryItemEventId eventId) {
            super(eventId);
        }
    }

    public static class SimpleInventoryItemStateMergePatched extends AbstractInventoryItemStateMergePatched
    {
        public SimpleInventoryItemStateMergePatched() {
        }

        public SimpleInventoryItemStateMergePatched(InventoryItemEventId eventId) {
            super(eventId);
        }
    }

}

