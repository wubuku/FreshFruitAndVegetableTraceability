// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.facility;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.facility.FacilityIdentificationEvent.*;

public abstract class AbstractFacilityIdentificationState implements FacilityIdentificationState.SqlFacilityIdentificationState {

    private FacilityIdentificationId facilityIdentificationId = new FacilityIdentificationId();

    public FacilityIdentificationId getFacilityIdentificationId() {
        return this.facilityIdentificationId;
    }

    public void setFacilityIdentificationId(FacilityIdentificationId facilityIdentificationId) {
        this.facilityIdentificationId = facilityIdentificationId;
    }

    private transient FacilityState facilityState;

    public FacilityState getFacilityState() {
        return facilityState;
    }

    public void setFacilityState(FacilityState s) {
        facilityState = s;
    }
    
    private FacilityState protectedFacilityState;

    protected FacilityState getProtectedFacilityState() {
        return protectedFacilityState;
    }

    protected void setProtectedFacilityState(FacilityState protectedFacilityState) {
        this.protectedFacilityState = protectedFacilityState;
    }

    public String getFacilityId() {
        return this.getFacilityIdentificationId().getFacilityId();
    }
        
    public void setFacilityId(String facilityId) {
        this.getFacilityIdentificationId().setFacilityId(facilityId);
    }

    public String getFacilityIdentificationTypeId() {
        return this.getFacilityIdentificationId().getFacilityIdentificationTypeId();
    }
        
    public void setFacilityIdentificationTypeId(String facilityIdentificationTypeId) {
        this.getFacilityIdentificationId().setFacilityIdentificationTypeId(facilityIdentificationTypeId);
    }

    private String idValue;

    public String getIdValue() {
        return this.idValue;
    }

    public void setIdValue(String idValue) {
        this.idValue = idValue;
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


    public AbstractFacilityIdentificationState() {
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
        if (obj instanceof FacilityIdentificationState) {
            return Objects.equals(this.getFacilityIdentificationTypeId(), ((FacilityIdentificationState)obj).getFacilityIdentificationTypeId());
        }
        return false;
    }


    public void mutate(Event e) {
        setStateReadOnly(false);
        if (e instanceof FacilityIdentificationStateCreated) {
            when((FacilityIdentificationStateCreated) e);
        } else if (e instanceof FacilityIdentificationStateMergePatched) {
            when((FacilityIdentificationStateMergePatched) e);
        } else if (e instanceof FacilityIdentificationStateRemoved) {
            when((FacilityIdentificationStateRemoved) e);
        } else {
            throw new UnsupportedOperationException(String.format("Unsupported event type: %1$s", e.getClass().getName()));
        }
    }

    public void when(FacilityIdentificationStateCreated e) {
        throwOnWrongEvent(e);

        this.setIdValue(e.getIdValue());

        this.setDeleted(false);

        this.setCreatedBy(e.getCreatedBy());
        this.setCreatedAt(e.getCreatedAt());

    }

    public void merge(FacilityIdentificationState s) {
        if (s == this) {
            return;
        }
        this.setIdValue(s.getIdValue());
    }

    public void when(FacilityIdentificationStateMergePatched e) {
        throwOnWrongEvent(e);

        if (e.getIdValue() == null) {
            if (e.getIsPropertyIdValueRemoved() != null && e.getIsPropertyIdValueRemoved()) {
                this.setIdValue(null);
            }
        } else {
            this.setIdValue(e.getIdValue());
        }

        this.setUpdatedBy(e.getCreatedBy());
        this.setUpdatedAt(e.getCreatedAt());

    }

    public void when(FacilityIdentificationStateRemoved e) {
        throwOnWrongEvent(e);

        this.setDeleted(true);
        this.setUpdatedBy(e.getCreatedBy());
        this.setUpdatedAt(e.getCreatedAt());

    }

    public void save() {
    }

    protected void throwOnWrongEvent(FacilityIdentificationEvent event) {
        String stateEntityIdFacilityId = this.getFacilityIdentificationId().getFacilityId();
        String eventEntityIdFacilityId = ((FacilityIdentificationEvent.SqlFacilityIdentificationEvent)event).getFacilityIdentificationEventId().getFacilityId();
        if (!stateEntityIdFacilityId.equals(eventEntityIdFacilityId)) {
            throw DomainError.named("mutateWrongEntity", "Entity Id FacilityId %1$s in state but entity id FacilityId %2$s in event", stateEntityIdFacilityId, eventEntityIdFacilityId);
        }

        String stateEntityIdFacilityIdentificationTypeId = this.getFacilityIdentificationId().getFacilityIdentificationTypeId();
        String eventEntityIdFacilityIdentificationTypeId = ((FacilityIdentificationEvent.SqlFacilityIdentificationEvent)event).getFacilityIdentificationEventId().getFacilityIdentificationTypeId();
        if (!stateEntityIdFacilityIdentificationTypeId.equals(eventEntityIdFacilityIdentificationTypeId)) {
            throw DomainError.named("mutateWrongEntity", "Entity Id FacilityIdentificationTypeId %1$s in state but entity id FacilityIdentificationTypeId %2$s in event", stateEntityIdFacilityIdentificationTypeId, eventEntityIdFacilityIdentificationTypeId);
        }


        if (getForReapplying()) { return; }
        FacilityIdentificationStateEvent stateEvent = event instanceof FacilityIdentificationStateEvent ? (FacilityIdentificationStateEvent)event : null;
        if (stateEvent == null) { return; }

        Long stateVersion = this.getVersion();
        Long stateEventStateVersion = stateEvent.getVersion();
        //if (stateEventStateVersion == null) {
        stateEventStateVersion = stateVersion == null ? FacilityIdentificationState.VERSION_NULL : stateVersion;
        stateEvent.setVersion(stateEventStateVersion);
        //}
        //if (!(stateVersion == null && stateEventStateVersion.equals(FacilityIdentificationState.VERSION_NULL)) && !stateEventStateVersion.equals(stateVersion))
        //{
        //    throw DomainError.named("concurrencyConflict", "Conflict between stateVersion (%1$s) and stateEventStateVersion (%2$s)", stateVersion, stateEventStateVersion);
        //}

    }


    public static class SimpleFacilityIdentificationState extends AbstractFacilityIdentificationState {

        public SimpleFacilityIdentificationState() {
        }

        public static SimpleFacilityIdentificationState newForReapplying() {
            SimpleFacilityIdentificationState s = new SimpleFacilityIdentificationState();
            s.initializeForReapplying();
            return s;
        }

    }



}
