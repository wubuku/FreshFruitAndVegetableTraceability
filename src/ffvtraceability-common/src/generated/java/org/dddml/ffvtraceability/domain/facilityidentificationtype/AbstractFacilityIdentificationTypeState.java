// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.facilityidentificationtype;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.facilityidentificationtype.FacilityIdentificationTypeEvent.*;

public abstract class AbstractFacilityIdentificationTypeState implements FacilityIdentificationTypeState.SqlFacilityIdentificationTypeState {

    private String facilityIdentificationTypeId;

    public String getFacilityIdentificationTypeId() {
        return this.facilityIdentificationTypeId;
    }

    public void setFacilityIdentificationTypeId(String facilityIdentificationTypeId) {
        this.facilityIdentificationTypeId = facilityIdentificationTypeId;
    }

    private String description;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public AbstractFacilityIdentificationTypeState(List<Event> events) {
        initializeForReapplying();
        if (events != null && events.size() > 0) {
            this.setFacilityIdentificationTypeId(((FacilityIdentificationTypeEvent.SqlFacilityIdentificationTypeEvent) events.get(0)).getFacilityIdentificationTypeEventId().getFacilityIdentificationTypeId());
            for (Event e : events) {
                mutate(e);
                this.setVersion((this.getVersion() == null ? FacilityIdentificationTypeState.VERSION_NULL : this.getVersion()) + 1);
            }
        }
    }


    public AbstractFacilityIdentificationTypeState() {
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
        return getFacilityIdentificationTypeId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj instanceof FacilityIdentificationTypeState) {
            return Objects.equals(this.getFacilityIdentificationTypeId(), ((FacilityIdentificationTypeState)obj).getFacilityIdentificationTypeId());
        }
        return false;
    }


    public void mutate(Event e) {
        setStateReadOnly(false);
        if (e instanceof FacilityIdentificationTypeStateCreated) {
            when((FacilityIdentificationTypeStateCreated) e);
        } else if (e instanceof FacilityIdentificationTypeStateMergePatched) {
            when((FacilityIdentificationTypeStateMergePatched) e);
        } else {
            throw new UnsupportedOperationException(String.format("Unsupported event type: %1$s", e.getClass().getName()));
        }
    }

    public void when(FacilityIdentificationTypeStateCreated e) {
        throwOnWrongEvent(e);

        this.setDescription(e.getDescription());

        this.setCreatedBy(e.getCreatedBy());
        this.setCreatedAt(e.getCreatedAt());

    }

    public void merge(FacilityIdentificationTypeState s) {
        if (s == this) {
            return;
        }
        this.setDescription(s.getDescription());
    }

    public void when(FacilityIdentificationTypeStateMergePatched e) {
        throwOnWrongEvent(e);

        if (e.getDescription() == null) {
            if (e.getIsPropertyDescriptionRemoved() != null && e.getIsPropertyDescriptionRemoved()) {
                this.setDescription(null);
            }
        } else {
            this.setDescription(e.getDescription());
        }

        this.setUpdatedBy(e.getCreatedBy());
        this.setUpdatedAt(e.getCreatedAt());

    }

    public void save() {
    }

    protected void throwOnWrongEvent(FacilityIdentificationTypeEvent event) {
        String stateEntityId = this.getFacilityIdentificationTypeId(); // Aggregate Id
        String eventEntityId = ((FacilityIdentificationTypeEvent.SqlFacilityIdentificationTypeEvent)event).getFacilityIdentificationTypeEventId().getFacilityIdentificationTypeId(); // EntityBase.Aggregate.GetEventIdPropertyIdName();
        if (!stateEntityId.equals(eventEntityId)) {
            throw DomainError.named("mutateWrongEntity", "Entity Id %1$s in state but entity id %2$s in event", stateEntityId, eventEntityId);
        }


        Long stateVersion = this.getVersion();
        Long eventVersion = ((FacilityIdentificationTypeEvent.SqlFacilityIdentificationTypeEvent)event).getFacilityIdentificationTypeEventId().getVersion();// Event Version
        if (eventVersion == null) {
            throw new NullPointerException("event.getFacilityIdentificationTypeEventId().getVersion() == null");
        }
        if (!(stateVersion == null && eventVersion.equals(VERSION_NULL)) && !eventVersion.equals(stateVersion)) {
            throw DomainError.named("concurrencyConflict", "Conflict between state version (%1$s) and event version (%2$s)", stateVersion, eventVersion == VERSION_NULL ? "NULL" : eventVersion + "");
        }

    }


    public static class SimpleFacilityIdentificationTypeState extends AbstractFacilityIdentificationTypeState {

        public SimpleFacilityIdentificationTypeState() {
        }

        public SimpleFacilityIdentificationTypeState(List<Event> events) {
            super(events);
        }

        public static SimpleFacilityIdentificationTypeState newForReapplying() {
            SimpleFacilityIdentificationTypeState s = new SimpleFacilityIdentificationTypeState();
            s.initializeForReapplying();
            return s;
        }

    }



}

