// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.shipment;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.shipment.ShipmentPackageContentEvent.*;

public abstract class AbstractShipmentPackageContentState implements ShipmentPackageContentState.SqlShipmentPackageContentState {

    private ShipmentPackageContentId shipmentPackageContentId = new ShipmentPackageContentId();

    public ShipmentPackageContentId getShipmentPackageContentId() {
        return this.shipmentPackageContentId;
    }

    public void setShipmentPackageContentId(ShipmentPackageContentId shipmentPackageContentId) {
        this.shipmentPackageContentId = shipmentPackageContentId;
    }

    private transient ShipmentState shipmentState;

    public ShipmentState getShipmentState() {
        return shipmentState;
    }

    public void setShipmentState(ShipmentState s) {
        shipmentState = s;
    }
    
    private ShipmentPackageState protectedShipmentPackageState;

    protected ShipmentPackageState getProtectedShipmentPackageState() {
        return protectedShipmentPackageState;
    }

    protected void setProtectedShipmentPackageState(ShipmentPackageState protectedShipmentPackageState) {
        this.protectedShipmentPackageState = protectedShipmentPackageState;
    }

    public String getShipmentId() {
        return this.getShipmentPackageContentId().getShipmentId();
    }
        
    public void setShipmentId(String shipmentId) {
        this.getShipmentPackageContentId().setShipmentId(shipmentId);
    }

    public String getShipmentPackageSeqId() {
        return this.getShipmentPackageContentId().getShipmentPackageSeqId();
    }
        
    public void setShipmentPackageSeqId(String shipmentPackageSeqId) {
        this.getShipmentPackageContentId().setShipmentPackageSeqId(shipmentPackageSeqId);
    }

    public String getShipmentItemSeqId() {
        return this.getShipmentPackageContentId().getShipmentItemSeqId();
    }
        
    public void setShipmentItemSeqId(String shipmentItemSeqId) {
        this.getShipmentPackageContentId().setShipmentItemSeqId(shipmentItemSeqId);
    }

    private java.math.BigDecimal quantity;

    public java.math.BigDecimal getQuantity() {
        return this.quantity;
    }

    public void setQuantity(java.math.BigDecimal quantity) {
        this.quantity = quantity;
    }

    private String subProductId;

    public String getSubProductId() {
        return this.subProductId;
    }

    public void setSubProductId(String subProductId) {
        this.subProductId = subProductId;
    }

    private java.math.BigDecimal subProductQuantity;

    public java.math.BigDecimal getSubProductQuantity() {
        return this.subProductQuantity;
    }

    public void setSubProductQuantity(java.math.BigDecimal subProductQuantity) {
        this.subProductQuantity = subProductQuantity;
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

    private Boolean deleted;

    public Boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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


    public AbstractShipmentPackageContentState() {
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
        return getShipmentItemSeqId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj instanceof ShipmentPackageContentState) {
            return Objects.equals(this.getShipmentItemSeqId(), ((ShipmentPackageContentState)obj).getShipmentItemSeqId());
        }
        return false;
    }


    public void mutate(Event e) {
        setStateReadOnly(false);
        if (e instanceof ShipmentPackageContentStateCreated) {
            when((ShipmentPackageContentStateCreated) e);
        } else if (e instanceof ShipmentPackageContentStateMergePatched) {
            when((ShipmentPackageContentStateMergePatched) e);
        } else if (e instanceof ShipmentPackageContentStateRemoved) {
            when((ShipmentPackageContentStateRemoved) e);
        } else {
            throw new UnsupportedOperationException(String.format("Unsupported event type: %1$s", e.getClass().getName()));
        }
    }

    public void when(ShipmentPackageContentStateCreated e) {
        throwOnWrongEvent(e);

        this.setQuantity(e.getQuantity());
        this.setSubProductId(e.getSubProductId());
        this.setSubProductQuantity(e.getSubProductQuantity());

        this.setDeleted(false);

        this.setCreatedBy(e.getCreatedBy());
        this.setCreatedAt(e.getCreatedAt());

    }

    public void merge(ShipmentPackageContentState s) {
        if (s == this) {
            return;
        }
        this.setQuantity(s.getQuantity());
        this.setSubProductId(s.getSubProductId());
        this.setSubProductQuantity(s.getSubProductQuantity());
    }

    public void when(ShipmentPackageContentStateMergePatched e) {
        throwOnWrongEvent(e);

        if (e.getQuantity() == null) {
            if (e.getIsPropertyQuantityRemoved() != null && e.getIsPropertyQuantityRemoved()) {
                this.setQuantity(null);
            }
        } else {
            this.setQuantity(e.getQuantity());
        }
        if (e.getSubProductId() == null) {
            if (e.getIsPropertySubProductIdRemoved() != null && e.getIsPropertySubProductIdRemoved()) {
                this.setSubProductId(null);
            }
        } else {
            this.setSubProductId(e.getSubProductId());
        }
        if (e.getSubProductQuantity() == null) {
            if (e.getIsPropertySubProductQuantityRemoved() != null && e.getIsPropertySubProductQuantityRemoved()) {
                this.setSubProductQuantity(null);
            }
        } else {
            this.setSubProductQuantity(e.getSubProductQuantity());
        }

        this.setUpdatedBy(e.getCreatedBy());
        this.setUpdatedAt(e.getCreatedAt());

    }

    public void when(ShipmentPackageContentStateRemoved e) {
        throwOnWrongEvent(e);

        this.setDeleted(true);
        this.setUpdatedBy(e.getCreatedBy());
        this.setUpdatedAt(e.getCreatedAt());

    }

    public void save() {
    }

    protected void throwOnWrongEvent(ShipmentPackageContentEvent event) {
        String stateEntityIdShipmentId = this.getShipmentPackageContentId().getShipmentId();
        String eventEntityIdShipmentId = ((ShipmentPackageContentEvent.SqlShipmentPackageContentEvent)event).getShipmentPackageContentEventId().getShipmentId();
        if (!stateEntityIdShipmentId.equals(eventEntityIdShipmentId)) {
            throw DomainError.named("mutateWrongEntity", "Entity Id ShipmentId %1$s in state but entity id ShipmentId %2$s in event", stateEntityIdShipmentId, eventEntityIdShipmentId);
        }

        String stateEntityIdShipmentPackageSeqId = this.getShipmentPackageContentId().getShipmentPackageSeqId();
        String eventEntityIdShipmentPackageSeqId = ((ShipmentPackageContentEvent.SqlShipmentPackageContentEvent)event).getShipmentPackageContentEventId().getShipmentPackageSeqId();
        if (!stateEntityIdShipmentPackageSeqId.equals(eventEntityIdShipmentPackageSeqId)) {
            throw DomainError.named("mutateWrongEntity", "Entity Id ShipmentPackageSeqId %1$s in state but entity id ShipmentPackageSeqId %2$s in event", stateEntityIdShipmentPackageSeqId, eventEntityIdShipmentPackageSeqId);
        }

        String stateEntityIdShipmentItemSeqId = this.getShipmentPackageContentId().getShipmentItemSeqId();
        String eventEntityIdShipmentItemSeqId = ((ShipmentPackageContentEvent.SqlShipmentPackageContentEvent)event).getShipmentPackageContentEventId().getShipmentItemSeqId();
        if (!stateEntityIdShipmentItemSeqId.equals(eventEntityIdShipmentItemSeqId)) {
            throw DomainError.named("mutateWrongEntity", "Entity Id ShipmentItemSeqId %1$s in state but entity id ShipmentItemSeqId %2$s in event", stateEntityIdShipmentItemSeqId, eventEntityIdShipmentItemSeqId);
        }


        if (getForReapplying()) { return; }
        ShipmentPackageContentStateEvent stateEvent = event instanceof ShipmentPackageContentStateEvent ? (ShipmentPackageContentStateEvent)event : null;
        if (stateEvent == null) { return; }

        Long stateVersion = this.getVersion();
        Long stateEventStateVersion = stateEvent.getVersion();
        //if (stateEventStateVersion == null) {
        stateEventStateVersion = stateVersion == null ? ShipmentPackageContentState.VERSION_NULL : stateVersion;
        stateEvent.setVersion(stateEventStateVersion);
        //}
        //if (!(stateVersion == null && stateEventStateVersion.equals(ShipmentPackageContentState.VERSION_NULL)) && !stateEventStateVersion.equals(stateVersion))
        //{
        //    throw DomainError.named("concurrencyConflict", "Conflict between stateVersion (%1$s) and stateEventStateVersion (%2$s)", stateVersion, stateEventStateVersion);
        //}

    }


    public static class SimpleShipmentPackageContentState extends AbstractShipmentPackageContentState {

        public SimpleShipmentPackageContentState() {
        }

        public static SimpleShipmentPackageContentState newForReapplying() {
            SimpleShipmentPackageContentState s = new SimpleShipmentPackageContentState();
            s.initializeForReapplying();
            return s;
        }

    }



}
