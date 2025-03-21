// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.inventorytransfer;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.inventorytransfer.InventoryTransferEvent.*;

public abstract class AbstractInventoryTransferState implements InventoryTransferState.SqlInventoryTransferState {

    private String inventoryTransferId;

    public String getInventoryTransferId() {
        return this.inventoryTransferId;
    }

    public void setInventoryTransferId(String inventoryTransferId) {
        this.inventoryTransferId = inventoryTransferId;
    }

    private String statusId;

    public String getStatusId() {
        return this.statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    private String inventoryItemId;

    public String getInventoryItemId() {
        return this.inventoryItemId;
    }

    public void setInventoryItemId(String inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }

    private String facilityId;

    public String getFacilityId() {
        return this.facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    private String locationSeqId;

    public String getLocationSeqId() {
        return this.locationSeqId;
    }

    public void setLocationSeqId(String locationSeqId) {
        this.locationSeqId = locationSeqId;
    }

    private String containerId;

    public String getContainerId() {
        return this.containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    private String facilityIdTo;

    public String getFacilityIdTo() {
        return this.facilityIdTo;
    }

    public void setFacilityIdTo(String facilityIdTo) {
        this.facilityIdTo = facilityIdTo;
    }

    private String locationSeqIdTo;

    public String getLocationSeqIdTo() {
        return this.locationSeqIdTo;
    }

    public void setLocationSeqIdTo(String locationSeqIdTo) {
        this.locationSeqIdTo = locationSeqIdTo;
    }

    private String containerIdTo;

    public String getContainerIdTo() {
        return this.containerIdTo;
    }

    public void setContainerIdTo(String containerIdTo) {
        this.containerIdTo = containerIdTo;
    }

    private String itemIssuanceId;

    public String getItemIssuanceId() {
        return this.itemIssuanceId;
    }

    public void setItemIssuanceId(String itemIssuanceId) {
        this.itemIssuanceId = itemIssuanceId;
    }

    private OffsetDateTime sendDate;

    public OffsetDateTime getSendDate() {
        return this.sendDate;
    }

    public void setSendDate(OffsetDateTime sendDate) {
        this.sendDate = sendDate;
    }

    private OffsetDateTime receiveDate;

    public OffsetDateTime getReceiveDate() {
        return this.receiveDate;
    }

    public void setReceiveDate(OffsetDateTime receiveDate) {
        this.receiveDate = receiveDate;
    }

    private String comments;

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    private String tenantId;

    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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

    public AbstractInventoryTransferState(List<Event> events) {
        initializeForReapplying();
        if (events != null && events.size() > 0) {
            this.setInventoryTransferId(((InventoryTransferEvent.SqlInventoryTransferEvent) events.get(0)).getInventoryTransferEventId().getInventoryTransferId());
            for (Event e : events) {
                mutate(e);
                this.setVersion((this.getVersion() == null ? InventoryTransferState.VERSION_NULL : this.getVersion()) + 1);
            }
        }
    }


    public AbstractInventoryTransferState() {
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
        return getInventoryTransferId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj instanceof InventoryTransferState) {
            return Objects.equals(this.getInventoryTransferId(), ((InventoryTransferState)obj).getInventoryTransferId());
        }
        return false;
    }


    public void mutate(Event e) {
        setStateReadOnly(false);
        if (e instanceof InventoryTransferStateCreated) {
            when((InventoryTransferStateCreated) e);
        } else if (e instanceof InventoryTransferStateMergePatched) {
            when((InventoryTransferStateMergePatched) e);
        } else {
            throw new UnsupportedOperationException(String.format("Unsupported event type: %1$s", e.getClass().getName()));
        }
    }

    public void when(InventoryTransferStateCreated e) {
        throwOnWrongEvent(e);

        this.setStatusId(e.getStatusId());
        this.setInventoryItemId(e.getInventoryItemId());
        this.setFacilityId(e.getFacilityId());
        this.setLocationSeqId(e.getLocationSeqId());
        this.setContainerId(e.getContainerId());
        this.setFacilityIdTo(e.getFacilityIdTo());
        this.setLocationSeqIdTo(e.getLocationSeqIdTo());
        this.setContainerIdTo(e.getContainerIdTo());
        this.setItemIssuanceId(e.getItemIssuanceId());
        this.setSendDate(e.getSendDate());
        this.setReceiveDate(e.getReceiveDate());
        this.setComments(e.getComments());

        this.setCreatedBy(e.getCreatedBy());
        this.setCreatedAt(e.getCreatedAt());

    }

    public void merge(InventoryTransferState s) {
        if (s == this) {
            return;
        }
        this.setStatusId(s.getStatusId());
        this.setInventoryItemId(s.getInventoryItemId());
        this.setFacilityId(s.getFacilityId());
        this.setLocationSeqId(s.getLocationSeqId());
        this.setContainerId(s.getContainerId());
        this.setFacilityIdTo(s.getFacilityIdTo());
        this.setLocationSeqIdTo(s.getLocationSeqIdTo());
        this.setContainerIdTo(s.getContainerIdTo());
        this.setItemIssuanceId(s.getItemIssuanceId());
        this.setSendDate(s.getSendDate());
        this.setReceiveDate(s.getReceiveDate());
        this.setComments(s.getComments());
    }

    public void when(InventoryTransferStateMergePatched e) {
        throwOnWrongEvent(e);

        if (e.getStatusId() == null) {
            if (e.getIsPropertyStatusIdRemoved() != null && e.getIsPropertyStatusIdRemoved()) {
                this.setStatusId(null);
            }
        } else {
            this.setStatusId(e.getStatusId());
        }
        if (e.getInventoryItemId() == null) {
            if (e.getIsPropertyInventoryItemIdRemoved() != null && e.getIsPropertyInventoryItemIdRemoved()) {
                this.setInventoryItemId(null);
            }
        } else {
            this.setInventoryItemId(e.getInventoryItemId());
        }
        if (e.getFacilityId() == null) {
            if (e.getIsPropertyFacilityIdRemoved() != null && e.getIsPropertyFacilityIdRemoved()) {
                this.setFacilityId(null);
            }
        } else {
            this.setFacilityId(e.getFacilityId());
        }
        if (e.getLocationSeqId() == null) {
            if (e.getIsPropertyLocationSeqIdRemoved() != null && e.getIsPropertyLocationSeqIdRemoved()) {
                this.setLocationSeqId(null);
            }
        } else {
            this.setLocationSeqId(e.getLocationSeqId());
        }
        if (e.getContainerId() == null) {
            if (e.getIsPropertyContainerIdRemoved() != null && e.getIsPropertyContainerIdRemoved()) {
                this.setContainerId(null);
            }
        } else {
            this.setContainerId(e.getContainerId());
        }
        if (e.getFacilityIdTo() == null) {
            if (e.getIsPropertyFacilityIdToRemoved() != null && e.getIsPropertyFacilityIdToRemoved()) {
                this.setFacilityIdTo(null);
            }
        } else {
            this.setFacilityIdTo(e.getFacilityIdTo());
        }
        if (e.getLocationSeqIdTo() == null) {
            if (e.getIsPropertyLocationSeqIdToRemoved() != null && e.getIsPropertyLocationSeqIdToRemoved()) {
                this.setLocationSeqIdTo(null);
            }
        } else {
            this.setLocationSeqIdTo(e.getLocationSeqIdTo());
        }
        if (e.getContainerIdTo() == null) {
            if (e.getIsPropertyContainerIdToRemoved() != null && e.getIsPropertyContainerIdToRemoved()) {
                this.setContainerIdTo(null);
            }
        } else {
            this.setContainerIdTo(e.getContainerIdTo());
        }
        if (e.getItemIssuanceId() == null) {
            if (e.getIsPropertyItemIssuanceIdRemoved() != null && e.getIsPropertyItemIssuanceIdRemoved()) {
                this.setItemIssuanceId(null);
            }
        } else {
            this.setItemIssuanceId(e.getItemIssuanceId());
        }
        if (e.getSendDate() == null) {
            if (e.getIsPropertySendDateRemoved() != null && e.getIsPropertySendDateRemoved()) {
                this.setSendDate(null);
            }
        } else {
            this.setSendDate(e.getSendDate());
        }
        if (e.getReceiveDate() == null) {
            if (e.getIsPropertyReceiveDateRemoved() != null && e.getIsPropertyReceiveDateRemoved()) {
                this.setReceiveDate(null);
            }
        } else {
            this.setReceiveDate(e.getReceiveDate());
        }
        if (e.getComments() == null) {
            if (e.getIsPropertyCommentsRemoved() != null && e.getIsPropertyCommentsRemoved()) {
                this.setComments(null);
            }
        } else {
            this.setComments(e.getComments());
        }

        this.setUpdatedBy(e.getCreatedBy());
        this.setUpdatedAt(e.getCreatedAt());

    }

    public void save() {
    }

    protected void throwOnWrongEvent(InventoryTransferEvent event) {
        String stateEntityId = this.getInventoryTransferId(); // Aggregate Id
        String eventEntityId = ((InventoryTransferEvent.SqlInventoryTransferEvent)event).getInventoryTransferEventId().getInventoryTransferId(); // EntityBase.Aggregate.GetEventIdPropertyIdName();
        if (!stateEntityId.equals(eventEntityId)) {
            throw DomainError.named("mutateWrongEntity", "Entity Id %1$s in state but entity id %2$s in event", stateEntityId, eventEntityId);
        }


        Long stateVersion = this.getVersion();
        Long eventVersion = ((InventoryTransferEvent.SqlInventoryTransferEvent)event).getInventoryTransferEventId().getVersion();// Event Version
        if (eventVersion == null) {
            throw new NullPointerException("event.getInventoryTransferEventId().getVersion() == null");
        }
        if (!(stateVersion == null && eventVersion.equals(VERSION_NULL)) && !eventVersion.equals(stateVersion)) {
            throw DomainError.named("concurrencyConflict", "Conflict between state version (%1$s) and event version (%2$s)", stateVersion, eventVersion == VERSION_NULL ? "NULL" : eventVersion + "");
        }

    }


    public static class SimpleInventoryTransferState extends AbstractInventoryTransferState {

        public SimpleInventoryTransferState() {
        }

        public SimpleInventoryTransferState(List<Event> events) {
            super(events);
        }

        public static SimpleInventoryTransferState newForReapplying() {
            SimpleInventoryTransferState s = new SimpleInventoryTransferState();
            s.initializeForReapplying();
            return s;
        }

    }



}

