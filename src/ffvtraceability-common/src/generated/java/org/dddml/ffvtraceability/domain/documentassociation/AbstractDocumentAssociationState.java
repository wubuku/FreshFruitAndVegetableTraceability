// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.documentassociation;

import java.util.*;
import java.math.*;
import org.dddml.ffvtraceability.domain.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.documentassociation.DocumentAssociationEvent.*;

public abstract class AbstractDocumentAssociationState implements DocumentAssociationState.SqlDocumentAssociationState {

    private DocumentAssociationId documentAssociationId;

    public DocumentAssociationId getDocumentAssociationId() {
        return this.documentAssociationId;
    }

    public void setDocumentAssociationId(DocumentAssociationId documentAssociationId) {
        this.documentAssociationId = documentAssociationId;
    }

    private String documentId;

    public String getDocumentId() {
        return this.documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    private String documentIdTo;

    public String getDocumentIdTo() {
        return this.documentIdTo;
    }

    public void setDocumentIdTo(String documentIdTo) {
        this.documentIdTo = documentIdTo;
    }

    private String documentAssocTypeId;

    public String getDocumentAssocTypeId() {
        return this.documentAssocTypeId;
    }

    public void setDocumentAssocTypeId(String documentAssocTypeId) {
        this.documentAssocTypeId = documentAssocTypeId;
    }

    private OffsetDateTime fromDate;

    public OffsetDateTime getFromDate() {
        return this.fromDate;
    }

    public void setFromDate(OffsetDateTime fromDate) {
        this.fromDate = fromDate;
    }

    private OffsetDateTime thruDate;

    public OffsetDateTime getThruDate() {
        return this.thruDate;
    }

    public void setThruDate(OffsetDateTime thruDate) {
        this.thruDate = thruDate;
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

    public AbstractDocumentAssociationState(List<Event> events) {
        initializeForReapplying();
        if (events != null && events.size() > 0) {
            this.setDocumentAssociationId(((DocumentAssociationEvent.SqlDocumentAssociationEvent) events.get(0)).getDocumentAssociationEventId().getDocumentAssociationId());
            for (Event e : events) {
                mutate(e);
                this.setVersion((this.getVersion() == null ? DocumentAssociationState.VERSION_NULL : this.getVersion()) + 1);
            }
        }
    }


    public AbstractDocumentAssociationState() {
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
        return getDocumentAssociationId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj instanceof DocumentAssociationState) {
            return Objects.equals(this.getDocumentAssociationId(), ((DocumentAssociationState)obj).getDocumentAssociationId());
        }
        return false;
    }


    public void mutate(Event e) {
        setStateReadOnly(false);
        if (e instanceof DocumentAssociationStateCreated) {
            when((DocumentAssociationStateCreated) e);
        } else if (e instanceof DocumentAssociationStateMergePatched) {
            when((DocumentAssociationStateMergePatched) e);
        } else if (e instanceof DocumentAssociationStateDeleted) {
            when((DocumentAssociationStateDeleted) e);
        } else {
            throw new UnsupportedOperationException(String.format("Unsupported event type: %1$s", e.getClass().getName()));
        }
    }

    public void when(DocumentAssociationStateCreated e) {
        throwOnWrongEvent(e);

        this.setDocumentId(e.getDocumentId());
        this.setDocumentIdTo(e.getDocumentIdTo());
        this.setDocumentAssocTypeId(e.getDocumentAssocTypeId());
        this.setFromDate(e.getFromDate());
        this.setThruDate(e.getThruDate());

        this.setDeleted(false);

        this.setCreatedBy(e.getCreatedBy());
        this.setCreatedAt(e.getCreatedAt());

    }

    public void merge(DocumentAssociationState s) {
        if (s == this) {
            return;
        }
        this.setDocumentId(s.getDocumentId());
        this.setDocumentIdTo(s.getDocumentIdTo());
        this.setDocumentAssocTypeId(s.getDocumentAssocTypeId());
        this.setFromDate(s.getFromDate());
        this.setThruDate(s.getThruDate());
    }

    public void when(DocumentAssociationStateMergePatched e) {
        throwOnWrongEvent(e);

        if (e.getDocumentId() == null) {
            if (e.getIsPropertyDocumentIdRemoved() != null && e.getIsPropertyDocumentIdRemoved()) {
                this.setDocumentId(null);
            }
        } else {
            this.setDocumentId(e.getDocumentId());
        }
        if (e.getDocumentIdTo() == null) {
            if (e.getIsPropertyDocumentIdToRemoved() != null && e.getIsPropertyDocumentIdToRemoved()) {
                this.setDocumentIdTo(null);
            }
        } else {
            this.setDocumentIdTo(e.getDocumentIdTo());
        }
        if (e.getDocumentAssocTypeId() == null) {
            if (e.getIsPropertyDocumentAssocTypeIdRemoved() != null && e.getIsPropertyDocumentAssocTypeIdRemoved()) {
                this.setDocumentAssocTypeId(null);
            }
        } else {
            this.setDocumentAssocTypeId(e.getDocumentAssocTypeId());
        }
        if (e.getFromDate() == null) {
            if (e.getIsPropertyFromDateRemoved() != null && e.getIsPropertyFromDateRemoved()) {
                this.setFromDate(null);
            }
        } else {
            this.setFromDate(e.getFromDate());
        }
        if (e.getThruDate() == null) {
            if (e.getIsPropertyThruDateRemoved() != null && e.getIsPropertyThruDateRemoved()) {
                this.setThruDate(null);
            }
        } else {
            this.setThruDate(e.getThruDate());
        }

        this.setUpdatedBy(e.getCreatedBy());
        this.setUpdatedAt(e.getCreatedAt());

    }

    public void when(DocumentAssociationStateDeleted e) {
        throwOnWrongEvent(e);

        this.setDeleted(true);
        this.setUpdatedBy(e.getCreatedBy());
        this.setUpdatedAt(e.getCreatedAt());

    }

    public void save() {
    }

    protected void throwOnWrongEvent(DocumentAssociationEvent event) {
        DocumentAssociationId stateEntityId = this.getDocumentAssociationId(); // Aggregate Id
        DocumentAssociationId eventEntityId = ((DocumentAssociationEvent.SqlDocumentAssociationEvent)event).getDocumentAssociationEventId().getDocumentAssociationId(); // EntityBase.Aggregate.GetEventIdPropertyIdName();
        if (!stateEntityId.equals(eventEntityId)) {
            throw DomainError.named("mutateWrongEntity", "Entity Id %1$s in state but entity id %2$s in event", stateEntityId, eventEntityId);
        }


        Long stateVersion = this.getVersion();
        Long eventVersion = ((DocumentAssociationEvent.SqlDocumentAssociationEvent)event).getDocumentAssociationEventId().getVersion();// Event Version
        if (eventVersion == null) {
            throw new NullPointerException("event.getDocumentAssociationEventId().getVersion() == null");
        }
        if (!(stateVersion == null && eventVersion.equals(VERSION_NULL)) && !eventVersion.equals(stateVersion)) {
            throw DomainError.named("concurrencyConflict", "Conflict between state version (%1$s) and event version (%2$s)", stateVersion, eventVersion == VERSION_NULL ? "NULL" : eventVersion + "");
        }

    }


    public static class SimpleDocumentAssociationState extends AbstractDocumentAssociationState {

        public SimpleDocumentAssociationState() {
        }

        public SimpleDocumentAssociationState(List<Event> events) {
            super(events);
        }

        public static SimpleDocumentAssociationState newForReapplying() {
            SimpleDocumentAssociationState s = new SimpleDocumentAssociationState();
            s.initializeForReapplying();
            return s;
        }

    }



}
