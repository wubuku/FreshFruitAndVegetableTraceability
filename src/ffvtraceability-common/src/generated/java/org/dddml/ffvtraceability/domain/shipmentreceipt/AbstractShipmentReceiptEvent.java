// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.shipmentreceipt;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.partyrole.*;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.AbstractEvent;

public abstract class AbstractShipmentReceiptEvent extends AbstractEvent implements ShipmentReceiptEvent.SqlShipmentReceiptEvent {
    private ShipmentReceiptEventId shipmentReceiptEventId = new ShipmentReceiptEventId();

    public ShipmentReceiptEventId getShipmentReceiptEventId() {
        return this.shipmentReceiptEventId;
    }

    public void setShipmentReceiptEventId(ShipmentReceiptEventId eventId) {
        this.shipmentReceiptEventId = eventId;
    }
    
    public String getReceiptId() {
        return getShipmentReceiptEventId().getReceiptId();
    }

    public void setReceiptId(String receiptId) {
        getShipmentReceiptEventId().setReceiptId(receiptId);
    }

    private boolean eventReadOnly;

    public boolean getEventReadOnly() { return this.eventReadOnly; }

    public void setEventReadOnly(boolean readOnly) { this.eventReadOnly = readOnly; }

    public Long getVersion() {
        return getShipmentReceiptEventId().getVersion();
    }
    
    public void setVersion(Long version) {
        getShipmentReceiptEventId().setVersion(version);
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

    protected AbstractShipmentReceiptEvent() {
    }

    protected AbstractShipmentReceiptEvent(ShipmentReceiptEventId eventId) {
        this.shipmentReceiptEventId = eventId;
    }

    protected ShipmentReceiptRoleEventDao getShipmentReceiptRoleEventDao() {
        return (ShipmentReceiptRoleEventDao)ApplicationContext.current.get("shipmentReceiptRoleEventDao");
    }

    protected ShipmentReceiptRoleEventId newShipmentReceiptRoleEventId(PartyRoleId partyRoleId)
    {
        ShipmentReceiptRoleEventId eventId = new ShipmentReceiptRoleEventId(this.getShipmentReceiptEventId().getReceiptId(), 
            partyRoleId, 
            this.getShipmentReceiptEventId().getVersion());
        return eventId;
    }

    protected void throwOnInconsistentEventIds(ShipmentReceiptRoleEvent.SqlShipmentReceiptRoleEvent e)
    {
        throwOnInconsistentEventIds(this, e);
    }

    public static void throwOnInconsistentEventIds(ShipmentReceiptEvent.SqlShipmentReceiptEvent oe, ShipmentReceiptRoleEvent.SqlShipmentReceiptRoleEvent e)
    {
        if (!oe.getShipmentReceiptEventId().getReceiptId().equals(e.getShipmentReceiptRoleEventId().getShipmentReceiptReceiptId()))
        { 
            throw DomainError.named("inconsistentEventIds", "Outer Id ReceiptId %1$s but inner id ShipmentReceiptReceiptId %2$s", 
                oe.getShipmentReceiptEventId().getReceiptId(), e.getShipmentReceiptRoleEventId().getShipmentReceiptReceiptId());
        }
    }

    public ShipmentReceiptRoleEvent.ShipmentReceiptRoleStateCreated newShipmentReceiptRoleStateCreated(PartyRoleId partyRoleId) {
        return new AbstractShipmentReceiptRoleEvent.SimpleShipmentReceiptRoleStateCreated(newShipmentReceiptRoleEventId(partyRoleId));
    }

    public ShipmentReceiptRoleEvent.ShipmentReceiptRoleStateMergePatched newShipmentReceiptRoleStateMergePatched(PartyRoleId partyRoleId) {
        return new AbstractShipmentReceiptRoleEvent.SimpleShipmentReceiptRoleStateMergePatched(newShipmentReceiptRoleEventId(partyRoleId));
    }


    public abstract String getEventType();

    public static class ShipmentReceiptLobEvent extends AbstractShipmentReceiptEvent {

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
            return "ShipmentReceiptLobEvent";
        }

    }


    public static abstract class AbstractShipmentReceiptStateEvent extends AbstractShipmentReceiptEvent implements ShipmentReceiptEvent.ShipmentReceiptStateEvent {
        private String productId;

        public String getProductId()
        {
            return this.productId;
        }

        public void setProductId(String productId)
        {
            this.productId = productId;
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

        private String shipmentPackageSeqId;

        public String getShipmentPackageSeqId()
        {
            return this.shipmentPackageSeqId;
        }

        public void setShipmentPackageSeqId(String shipmentPackageSeqId)
        {
            this.shipmentPackageSeqId = shipmentPackageSeqId;
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

        private String rejectionId;

        public String getRejectionId()
        {
            return this.rejectionId;
        }

        public void setRejectionId(String rejectionId)
        {
            this.rejectionId = rejectionId;
        }

        private String receivedBy;

        public String getReceivedBy()
        {
            return this.receivedBy;
        }

        public void setReceivedBy(String receivedBy)
        {
            this.receivedBy = receivedBy;
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

        private String itemDescription;

        public String getItemDescription()
        {
            return this.itemDescription;
        }

        public void setItemDescription(String itemDescription)
        {
            this.itemDescription = itemDescription;
        }

        private java.math.BigDecimal quantityAccepted;

        public java.math.BigDecimal getQuantityAccepted()
        {
            return this.quantityAccepted;
        }

        public void setQuantityAccepted(java.math.BigDecimal quantityAccepted)
        {
            this.quantityAccepted = quantityAccepted;
        }

        private java.math.BigDecimal quantityRejected;

        public java.math.BigDecimal getQuantityRejected()
        {
            return this.quantityRejected;
        }

        public void setQuantityRejected(java.math.BigDecimal quantityRejected)
        {
            this.quantityRejected = quantityRejected;
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

        private String locationSeqId;

        public String getLocationSeqId()
        {
            return this.locationSeqId;
        }

        public void setLocationSeqId(String locationSeqId)
        {
            this.locationSeqId = locationSeqId;
        }

        private Long casesAccepted;

        public Long getCasesAccepted()
        {
            return this.casesAccepted;
        }

        public void setCasesAccepted(Long casesAccepted)
        {
            this.casesAccepted = casesAccepted;
        }

        private Long casesRejected;

        public Long getCasesRejected()
        {
            return this.casesRejected;
        }

        public void setCasesRejected(Long casesRejected)
        {
            this.casesRejected = casesRejected;
        }

        protected AbstractShipmentReceiptStateEvent(ShipmentReceiptEventId eventId) {
            super(eventId);
        }
    }

    public static abstract class AbstractShipmentReceiptStateCreated extends AbstractShipmentReceiptStateEvent implements ShipmentReceiptEvent.ShipmentReceiptStateCreated, Saveable
    {
        public AbstractShipmentReceiptStateCreated() {
            this(new ShipmentReceiptEventId());
        }

        public AbstractShipmentReceiptStateCreated(ShipmentReceiptEventId eventId) {
            super(eventId);
        }

        public String getEventType() {
            return StateEventType.CREATED;
        }

        private Map<ShipmentReceiptRoleEventId, ShipmentReceiptRoleEvent.ShipmentReceiptRoleStateCreated> shipmentReceiptRoleEvents = new HashMap<ShipmentReceiptRoleEventId, ShipmentReceiptRoleEvent.ShipmentReceiptRoleStateCreated>();
        
        private Iterable<ShipmentReceiptRoleEvent.ShipmentReceiptRoleStateCreated> readOnlyShipmentReceiptRoleEvents;

        public Iterable<ShipmentReceiptRoleEvent.ShipmentReceiptRoleStateCreated> getShipmentReceiptRoleEvents()
        {
            if (!getEventReadOnly())
            {
                return this.shipmentReceiptRoleEvents.values();
            }
            else
            {
                if (readOnlyShipmentReceiptRoleEvents != null) { return readOnlyShipmentReceiptRoleEvents; }
                ShipmentReceiptRoleEventDao eventDao = getShipmentReceiptRoleEventDao();
                List<ShipmentReceiptRoleEvent.ShipmentReceiptRoleStateCreated> eL = new ArrayList<ShipmentReceiptRoleEvent.ShipmentReceiptRoleStateCreated>();
                for (ShipmentReceiptRoleEvent e : eventDao.findByShipmentReceiptEventId(this.getShipmentReceiptEventId()))
                {
                    ((ShipmentReceiptRoleEvent.SqlShipmentReceiptRoleEvent)e).setEventReadOnly(true);
                    eL.add((ShipmentReceiptRoleEvent.ShipmentReceiptRoleStateCreated)e);
                }
                return (readOnlyShipmentReceiptRoleEvents = eL);
            }
        }

        public void setShipmentReceiptRoleEvents(Iterable<ShipmentReceiptRoleEvent.ShipmentReceiptRoleStateCreated> es)
        {
            if (es != null)
            {
                for (ShipmentReceiptRoleEvent.ShipmentReceiptRoleStateCreated e : es)
                {
                    addShipmentReceiptRoleEvent(e);
                }
            }
            else { this.shipmentReceiptRoleEvents.clear(); }
        }
        
        public void addShipmentReceiptRoleEvent(ShipmentReceiptRoleEvent.ShipmentReceiptRoleStateCreated e)
        {
            throwOnInconsistentEventIds((ShipmentReceiptRoleEvent.SqlShipmentReceiptRoleEvent)e);
            this.shipmentReceiptRoleEvents.put(((ShipmentReceiptRoleEvent.SqlShipmentReceiptRoleEvent)e).getShipmentReceiptRoleEventId(), e);
        }

        public void save()
        {
            for (ShipmentReceiptRoleEvent.ShipmentReceiptRoleStateCreated e : this.getShipmentReceiptRoleEvents()) {
                getShipmentReceiptRoleEventDao().save(e);
            }
        }
    }


    public static abstract class AbstractShipmentReceiptStateMergePatched extends AbstractShipmentReceiptStateEvent implements ShipmentReceiptEvent.ShipmentReceiptStateMergePatched, Saveable
    {
        public AbstractShipmentReceiptStateMergePatched() {
            this(new ShipmentReceiptEventId());
        }

        public AbstractShipmentReceiptStateMergePatched(ShipmentReceiptEventId eventId) {
            super(eventId);
        }

        public String getEventType() {
            return StateEventType.MERGE_PATCHED;
        }

        private Boolean isPropertyProductIdRemoved;

        public Boolean getIsPropertyProductIdRemoved() {
            return this.isPropertyProductIdRemoved;
        }

        public void setIsPropertyProductIdRemoved(Boolean removed) {
            this.isPropertyProductIdRemoved = removed;
        }

        private Boolean isPropertyShipmentIdRemoved;

        public Boolean getIsPropertyShipmentIdRemoved() {
            return this.isPropertyShipmentIdRemoved;
        }

        public void setIsPropertyShipmentIdRemoved(Boolean removed) {
            this.isPropertyShipmentIdRemoved = removed;
        }

        private Boolean isPropertyShipmentItemSeqIdRemoved;

        public Boolean getIsPropertyShipmentItemSeqIdRemoved() {
            return this.isPropertyShipmentItemSeqIdRemoved;
        }

        public void setIsPropertyShipmentItemSeqIdRemoved(Boolean removed) {
            this.isPropertyShipmentItemSeqIdRemoved = removed;
        }

        private Boolean isPropertyShipmentPackageSeqIdRemoved;

        public Boolean getIsPropertyShipmentPackageSeqIdRemoved() {
            return this.isPropertyShipmentPackageSeqIdRemoved;
        }

        public void setIsPropertyShipmentPackageSeqIdRemoved(Boolean removed) {
            this.isPropertyShipmentPackageSeqIdRemoved = removed;
        }

        private Boolean isPropertyOrderIdRemoved;

        public Boolean getIsPropertyOrderIdRemoved() {
            return this.isPropertyOrderIdRemoved;
        }

        public void setIsPropertyOrderIdRemoved(Boolean removed) {
            this.isPropertyOrderIdRemoved = removed;
        }

        private Boolean isPropertyOrderItemSeqIdRemoved;

        public Boolean getIsPropertyOrderItemSeqIdRemoved() {
            return this.isPropertyOrderItemSeqIdRemoved;
        }

        public void setIsPropertyOrderItemSeqIdRemoved(Boolean removed) {
            this.isPropertyOrderItemSeqIdRemoved = removed;
        }

        private Boolean isPropertyReturnIdRemoved;

        public Boolean getIsPropertyReturnIdRemoved() {
            return this.isPropertyReturnIdRemoved;
        }

        public void setIsPropertyReturnIdRemoved(Boolean removed) {
            this.isPropertyReturnIdRemoved = removed;
        }

        private Boolean isPropertyReturnItemSeqIdRemoved;

        public Boolean getIsPropertyReturnItemSeqIdRemoved() {
            return this.isPropertyReturnItemSeqIdRemoved;
        }

        public void setIsPropertyReturnItemSeqIdRemoved(Boolean removed) {
            this.isPropertyReturnItemSeqIdRemoved = removed;
        }

        private Boolean isPropertyRejectionIdRemoved;

        public Boolean getIsPropertyRejectionIdRemoved() {
            return this.isPropertyRejectionIdRemoved;
        }

        public void setIsPropertyRejectionIdRemoved(Boolean removed) {
            this.isPropertyRejectionIdRemoved = removed;
        }

        private Boolean isPropertyReceivedByRemoved;

        public Boolean getIsPropertyReceivedByRemoved() {
            return this.isPropertyReceivedByRemoved;
        }

        public void setIsPropertyReceivedByRemoved(Boolean removed) {
            this.isPropertyReceivedByRemoved = removed;
        }

        private Boolean isPropertyDatetimeReceivedRemoved;

        public Boolean getIsPropertyDatetimeReceivedRemoved() {
            return this.isPropertyDatetimeReceivedRemoved;
        }

        public void setIsPropertyDatetimeReceivedRemoved(Boolean removed) {
            this.isPropertyDatetimeReceivedRemoved = removed;
        }

        private Boolean isPropertyItemDescriptionRemoved;

        public Boolean getIsPropertyItemDescriptionRemoved() {
            return this.isPropertyItemDescriptionRemoved;
        }

        public void setIsPropertyItemDescriptionRemoved(Boolean removed) {
            this.isPropertyItemDescriptionRemoved = removed;
        }

        private Boolean isPropertyQuantityAcceptedRemoved;

        public Boolean getIsPropertyQuantityAcceptedRemoved() {
            return this.isPropertyQuantityAcceptedRemoved;
        }

        public void setIsPropertyQuantityAcceptedRemoved(Boolean removed) {
            this.isPropertyQuantityAcceptedRemoved = removed;
        }

        private Boolean isPropertyQuantityRejectedRemoved;

        public Boolean getIsPropertyQuantityRejectedRemoved() {
            return this.isPropertyQuantityRejectedRemoved;
        }

        public void setIsPropertyQuantityRejectedRemoved(Boolean removed) {
            this.isPropertyQuantityRejectedRemoved = removed;
        }

        private Boolean isPropertyLotIdRemoved;

        public Boolean getIsPropertyLotIdRemoved() {
            return this.isPropertyLotIdRemoved;
        }

        public void setIsPropertyLotIdRemoved(Boolean removed) {
            this.isPropertyLotIdRemoved = removed;
        }

        private Boolean isPropertyLocationSeqIdRemoved;

        public Boolean getIsPropertyLocationSeqIdRemoved() {
            return this.isPropertyLocationSeqIdRemoved;
        }

        public void setIsPropertyLocationSeqIdRemoved(Boolean removed) {
            this.isPropertyLocationSeqIdRemoved = removed;
        }

        private Boolean isPropertyCasesAcceptedRemoved;

        public Boolean getIsPropertyCasesAcceptedRemoved() {
            return this.isPropertyCasesAcceptedRemoved;
        }

        public void setIsPropertyCasesAcceptedRemoved(Boolean removed) {
            this.isPropertyCasesAcceptedRemoved = removed;
        }

        private Boolean isPropertyCasesRejectedRemoved;

        public Boolean getIsPropertyCasesRejectedRemoved() {
            return this.isPropertyCasesRejectedRemoved;
        }

        public void setIsPropertyCasesRejectedRemoved(Boolean removed) {
            this.isPropertyCasesRejectedRemoved = removed;
        }


        private Map<ShipmentReceiptRoleEventId, ShipmentReceiptRoleEvent> shipmentReceiptRoleEvents = new HashMap<ShipmentReceiptRoleEventId, ShipmentReceiptRoleEvent>();
        
        private Iterable<ShipmentReceiptRoleEvent> readOnlyShipmentReceiptRoleEvents;

        public Iterable<ShipmentReceiptRoleEvent> getShipmentReceiptRoleEvents()
        {
            if (!getEventReadOnly())
            {
                return this.shipmentReceiptRoleEvents.values();
            }
            else
            {
                if (readOnlyShipmentReceiptRoleEvents != null) { return readOnlyShipmentReceiptRoleEvents; }
                ShipmentReceiptRoleEventDao eventDao = getShipmentReceiptRoleEventDao();
                List<ShipmentReceiptRoleEvent> eL = new ArrayList<ShipmentReceiptRoleEvent>();
                for (ShipmentReceiptRoleEvent e : eventDao.findByShipmentReceiptEventId(this.getShipmentReceiptEventId()))
                {
                    ((ShipmentReceiptRoleEvent.SqlShipmentReceiptRoleEvent)e).setEventReadOnly(true);
                    eL.add((ShipmentReceiptRoleEvent)e);
                }
                return (readOnlyShipmentReceiptRoleEvents = eL);
            }
        }

        public void setShipmentReceiptRoleEvents(Iterable<ShipmentReceiptRoleEvent> es)
        {
            if (es != null)
            {
                for (ShipmentReceiptRoleEvent e : es)
                {
                    addShipmentReceiptRoleEvent(e);
                }
            }
            else { this.shipmentReceiptRoleEvents.clear(); }
        }
        
        public void addShipmentReceiptRoleEvent(ShipmentReceiptRoleEvent e)
        {
            throwOnInconsistentEventIds((ShipmentReceiptRoleEvent.SqlShipmentReceiptRoleEvent)e);
            this.shipmentReceiptRoleEvents.put(((ShipmentReceiptRoleEvent.SqlShipmentReceiptRoleEvent)e).getShipmentReceiptRoleEventId(), e);
        }

        public void save()
        {
            for (ShipmentReceiptRoleEvent e : this.getShipmentReceiptRoleEvents()) {
                getShipmentReceiptRoleEventDao().save(e);
            }
        }
    }


    public static abstract class AbstractShipmentReceiptStateDeleted extends AbstractShipmentReceiptStateEvent implements ShipmentReceiptEvent.ShipmentReceiptStateDeleted, Saveable
    {
        public AbstractShipmentReceiptStateDeleted() {
            this(new ShipmentReceiptEventId());
        }

        public AbstractShipmentReceiptStateDeleted(ShipmentReceiptEventId eventId) {
            super(eventId);
        }

        public String getEventType() {
            return StateEventType.DELETED;
        }

        public void save()
        {
        }
    }

    public static class SimpleShipmentReceiptStateCreated extends AbstractShipmentReceiptStateCreated
    {
        public SimpleShipmentReceiptStateCreated() {
        }

        public SimpleShipmentReceiptStateCreated(ShipmentReceiptEventId eventId) {
            super(eventId);
        }
    }

    public static class SimpleShipmentReceiptStateMergePatched extends AbstractShipmentReceiptStateMergePatched
    {
        public SimpleShipmentReceiptStateMergePatched() {
        }

        public SimpleShipmentReceiptStateMergePatched(ShipmentReceiptEventId eventId) {
            super(eventId);
        }
    }

    public static class SimpleShipmentReceiptStateDeleted extends AbstractShipmentReceiptStateDeleted
    {
        public SimpleShipmentReceiptStateDeleted() {
        }

        public SimpleShipmentReceiptStateDeleted(ShipmentReceiptEventId eventId) {
            super(eventId);
        }
    }

}

