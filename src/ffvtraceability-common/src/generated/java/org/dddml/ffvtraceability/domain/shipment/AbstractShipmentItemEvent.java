// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.shipment;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.AbstractEvent;

public abstract class AbstractShipmentItemEvent extends AbstractEvent implements ShipmentItemEvent.SqlShipmentItemEvent {
    private ShipmentItemEventId shipmentItemEventId = new ShipmentItemEventId();

    public ShipmentItemEventId getShipmentItemEventId() {
        return this.shipmentItemEventId;
    }

    public void setShipmentItemEventId(ShipmentItemEventId eventId) {
        this.shipmentItemEventId = eventId;
    }
    
    public String getShipmentItemSeqId() {
        return getShipmentItemEventId().getShipmentItemSeqId();
    }

    public void setShipmentItemSeqId(String shipmentItemSeqId) {
        getShipmentItemEventId().setShipmentItemSeqId(shipmentItemSeqId);
    }

    private boolean eventReadOnly;

    public boolean getEventReadOnly() { return this.eventReadOnly; }

    public void setEventReadOnly(boolean readOnly) { this.eventReadOnly = readOnly; }

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

    protected AbstractShipmentItemEvent() {
    }

    protected AbstractShipmentItemEvent(ShipmentItemEventId eventId) {
        this.shipmentItemEventId = eventId;
    }


    public abstract String getEventType();


    public static abstract class AbstractShipmentItemStateEvent extends AbstractShipmentItemEvent implements ShipmentItemEvent.ShipmentItemStateEvent {
        private Long version;

        public Long getVersion()
        {
            return this.version;
        }

        public void setVersion(Long version)
        {
            this.version = version;
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

        private java.math.BigDecimal quantity;

        public java.math.BigDecimal getQuantity()
        {
            return this.quantity;
        }

        public void setQuantity(java.math.BigDecimal quantity)
        {
            this.quantity = quantity;
        }

        private String shipmentContentDescription;

        public String getShipmentContentDescription()
        {
            return this.shipmentContentDescription;
        }

        public void setShipmentContentDescription(String shipmentContentDescription)
        {
            this.shipmentContentDescription = shipmentContentDescription;
        }

        protected AbstractShipmentItemStateEvent(ShipmentItemEventId eventId) {
            super(eventId);
        }
    }

    public static abstract class AbstractShipmentItemStateCreated extends AbstractShipmentItemStateEvent implements ShipmentItemEvent.ShipmentItemStateCreated
    {
        public AbstractShipmentItemStateCreated() {
            this(new ShipmentItemEventId());
        }

        public AbstractShipmentItemStateCreated(ShipmentItemEventId eventId) {
            super(eventId);
        }

        public String getEventType() {
            return StateEventType.CREATED;
        }

    }


    public static abstract class AbstractShipmentItemStateMergePatched extends AbstractShipmentItemStateEvent implements ShipmentItemEvent.ShipmentItemStateMergePatched
    {
        public AbstractShipmentItemStateMergePatched() {
            this(new ShipmentItemEventId());
        }

        public AbstractShipmentItemStateMergePatched(ShipmentItemEventId eventId) {
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

        private Boolean isPropertyQuantityRemoved;

        public Boolean getIsPropertyQuantityRemoved() {
            return this.isPropertyQuantityRemoved;
        }

        public void setIsPropertyQuantityRemoved(Boolean removed) {
            this.isPropertyQuantityRemoved = removed;
        }

        private Boolean isPropertyShipmentContentDescriptionRemoved;

        public Boolean getIsPropertyShipmentContentDescriptionRemoved() {
            return this.isPropertyShipmentContentDescriptionRemoved;
        }

        public void setIsPropertyShipmentContentDescriptionRemoved(Boolean removed) {
            this.isPropertyShipmentContentDescriptionRemoved = removed;
        }


    }


    public static abstract class AbstractShipmentItemStateRemoved extends AbstractShipmentItemStateEvent implements ShipmentItemEvent.ShipmentItemStateRemoved
    {
        public AbstractShipmentItemStateRemoved() {
            this(new ShipmentItemEventId());
        }

        public AbstractShipmentItemStateRemoved(ShipmentItemEventId eventId) {
            super(eventId);
        }

        public String getEventType() {
            return StateEventType.REMOVED;
        }

    }

    public static class SimpleShipmentItemStateCreated extends AbstractShipmentItemStateCreated
    {
        public SimpleShipmentItemStateCreated() {
        }

        public SimpleShipmentItemStateCreated(ShipmentItemEventId eventId) {
            super(eventId);
        }
    }

    public static class SimpleShipmentItemStateMergePatched extends AbstractShipmentItemStateMergePatched
    {
        public SimpleShipmentItemStateMergePatched() {
        }

        public SimpleShipmentItemStateMergePatched(ShipmentItemEventId eventId) {
            super(eventId);
        }
    }

    public static class SimpleShipmentItemStateRemoved extends AbstractShipmentItemStateRemoved
    {
        public SimpleShipmentItemStateRemoved() {
        }

        public SimpleShipmentItemStateRemoved(ShipmentItemEventId eventId) {
            super(eventId);
        }
    }

}
