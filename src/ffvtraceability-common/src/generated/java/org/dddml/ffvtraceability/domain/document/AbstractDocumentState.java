// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.document;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.document.DocumentEvent.*;

public abstract class AbstractDocumentState implements DocumentState.SqlDocumentState {

    private String documentId;

    public String getDocumentId() {
        return this.documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    private String documentTypeId;

    public String getDocumentTypeId() {
        return this.documentTypeId;
    }

    public void setDocumentTypeId(String documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    private String comments;

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    private String documentLocation;

    public String getDocumentLocation() {
        return this.documentLocation;
    }

    public void setDocumentLocation(String documentLocation) {
        this.documentLocation = documentLocation;
    }

    private String documentText;

    public String getDocumentText() {
        return this.documentText;
    }

    public void setDocumentText(String documentText) {
        this.documentText = documentText;
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

    public AbstractDocumentState(List<Event> events) {
        initializeForReapplying();
        if (events != null && events.size() > 0) {
            this.setDocumentId(((DocumentEvent.SqlDocumentEvent) events.get(0)).getDocumentEventId().getDocumentId());
            for (Event e : events) {
                mutate(e);
                this.setVersion((this.getVersion() == null ? DocumentState.VERSION_NULL : this.getVersion()) + 1);
            }
        }
    }


    public AbstractDocumentState() {
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
        return getDocumentId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj instanceof DocumentState) {
            return Objects.equals(this.getDocumentId(), ((DocumentState)obj).getDocumentId());
        }
        return false;
    }


    public void mutate(Event e) {
        setStateReadOnly(false);
        if (e instanceof DocumentStateCreated) {
            when((DocumentStateCreated) e);
        } else if (e instanceof DocumentStateMergePatched) {
            when((DocumentStateMergePatched) e);
        } else if (e instanceof DocumentStateDeleted) {
            when((DocumentStateDeleted) e);
        } else {
            throw new UnsupportedOperationException(String.format("Unsupported event type: %1$s", e.getClass().getName()));
        }
    }

    public void when(DocumentStateCreated e) {
        throwOnWrongEvent(e);

        this.setDocumentTypeId(e.getDocumentTypeId());
        this.setComments(e.getComments());
        this.setDocumentLocation(e.getDocumentLocation());
        this.setDocumentText(e.getDocumentText());

        this.setDeleted(false);

        this.setCreatedBy(e.getCreatedBy());
        this.setCreatedAt(e.getCreatedAt());

    }

    public void merge(DocumentState s) {
        if (s == this) {
            return;
        }
        this.setDocumentTypeId(s.getDocumentTypeId());
        this.setComments(s.getComments());
        this.setDocumentLocation(s.getDocumentLocation());
        this.setDocumentText(s.getDocumentText());
    }

    public void when(DocumentStateMergePatched e) {
        throwOnWrongEvent(e);

        if (e.getDocumentTypeId() == null) {
            if (e.getIsPropertyDocumentTypeIdRemoved() != null && e.getIsPropertyDocumentTypeIdRemoved()) {
                this.setDocumentTypeId(null);
            }
        } else {
            this.setDocumentTypeId(e.getDocumentTypeId());
        }
        if (e.getComments() == null) {
            if (e.getIsPropertyCommentsRemoved() != null && e.getIsPropertyCommentsRemoved()) {
                this.setComments(null);
            }
        } else {
            this.setComments(e.getComments());
        }
        if (e.getDocumentLocation() == null) {
            if (e.getIsPropertyDocumentLocationRemoved() != null && e.getIsPropertyDocumentLocationRemoved()) {
                this.setDocumentLocation(null);
            }
        } else {
            this.setDocumentLocation(e.getDocumentLocation());
        }
        if (e.getDocumentText() == null) {
            if (e.getIsPropertyDocumentTextRemoved() != null && e.getIsPropertyDocumentTextRemoved()) {
                this.setDocumentText(null);
            }
        } else {
            this.setDocumentText(e.getDocumentText());
        }

        this.setUpdatedBy(e.getCreatedBy());
        this.setUpdatedAt(e.getCreatedAt());

    }

    public void when(DocumentStateDeleted e) {
        throwOnWrongEvent(e);

        this.setDeleted(true);
        this.setUpdatedBy(e.getCreatedBy());
        this.setUpdatedAt(e.getCreatedAt());

    }

    public void save() {
    }

    protected void throwOnWrongEvent(DocumentEvent event) {
        String stateEntityId = this.getDocumentId(); // Aggregate Id
        String eventEntityId = ((DocumentEvent.SqlDocumentEvent)event).getDocumentEventId().getDocumentId(); // EntityBase.Aggregate.GetEventIdPropertyIdName();
        if (!stateEntityId.equals(eventEntityId)) {
            throw DomainError.named("mutateWrongEntity", "Entity Id %1$s in state but entity id %2$s in event", stateEntityId, eventEntityId);
        }


        Long stateVersion = this.getVersion();
        Long eventVersion = ((DocumentEvent.SqlDocumentEvent)event).getDocumentEventId().getVersion();// Event Version
        if (eventVersion == null) {
            throw new NullPointerException("event.getDocumentEventId().getVersion() == null");
        }
        if (!(stateVersion == null && eventVersion.equals(VERSION_NULL)) && !eventVersion.equals(stateVersion)) {
            throw DomainError.named("concurrencyConflict", "Conflict between state version (%1$s) and event version (%2$s)", stateVersion, eventVersion == VERSION_NULL ? "NULL" : eventVersion + "");
        }

    }


    public static class SimpleDocumentState extends AbstractDocumentState {

        public SimpleDocumentState() {
        }

        public SimpleDocumentState(List<Event> events) {
            super(events);
        }

        public static SimpleDocumentState newForReapplying() {
            SimpleDocumentState s = new SimpleDocumentState();
            s.initializeForReapplying();
            return s;
        }

    }



}
