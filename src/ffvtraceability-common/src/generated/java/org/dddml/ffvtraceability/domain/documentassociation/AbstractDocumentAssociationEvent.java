// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.documentassociation;

import java.util.*;
import org.dddml.ffvtraceability.domain.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.AbstractEvent;

public abstract class AbstractDocumentAssociationEvent extends AbstractEvent implements DocumentAssociationEvent.SqlDocumentAssociationEvent {
    private DocumentAssociationEventId documentAssociationEventId = new DocumentAssociationEventId();

    public DocumentAssociationEventId getDocumentAssociationEventId() {
        return this.documentAssociationEventId;
    }

    public void setDocumentAssociationEventId(DocumentAssociationEventId eventId) {
        this.documentAssociationEventId = eventId;
    }
    
    public DocumentAssociationId getDocumentAssociationId() {
        return getDocumentAssociationEventId().getDocumentAssociationId();
    }

    public void setDocumentAssociationId(DocumentAssociationId documentAssociationId) {
        getDocumentAssociationEventId().setDocumentAssociationId(documentAssociationId);
    }

    private boolean eventReadOnly;

    public boolean getEventReadOnly() { return this.eventReadOnly; }

    public void setEventReadOnly(boolean readOnly) { this.eventReadOnly = readOnly; }

    public Long getVersion() {
        return getDocumentAssociationEventId().getVersion();
    }
    
    public void setVersion(Long version) {
        getDocumentAssociationEventId().setVersion(version);
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

    protected AbstractDocumentAssociationEvent() {
    }

    protected AbstractDocumentAssociationEvent(DocumentAssociationEventId eventId) {
        this.documentAssociationEventId = eventId;
    }


    public abstract String getEventType();

    public static class DocumentAssociationLobEvent extends AbstractDocumentAssociationEvent {

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
            return "DocumentAssociationLobEvent";
        }

    }


    public static abstract class AbstractDocumentAssociationStateEvent extends AbstractDocumentAssociationEvent implements DocumentAssociationEvent.DocumentAssociationStateEvent {
        private String documentId;

        public String getDocumentId()
        {
            return this.documentId;
        }

        public void setDocumentId(String documentId)
        {
            this.documentId = documentId;
        }

        private String documentIdTo;

        public String getDocumentIdTo()
        {
            return this.documentIdTo;
        }

        public void setDocumentIdTo(String documentIdTo)
        {
            this.documentIdTo = documentIdTo;
        }

        private String documentAssocTypeId;

        public String getDocumentAssocTypeId()
        {
            return this.documentAssocTypeId;
        }

        public void setDocumentAssocTypeId(String documentAssocTypeId)
        {
            this.documentAssocTypeId = documentAssocTypeId;
        }

        private OffsetDateTime fromDate;

        public OffsetDateTime getFromDate()
        {
            return this.fromDate;
        }

        public void setFromDate(OffsetDateTime fromDate)
        {
            this.fromDate = fromDate;
        }

        private OffsetDateTime thruDate;

        public OffsetDateTime getThruDate()
        {
            return this.thruDate;
        }

        public void setThruDate(OffsetDateTime thruDate)
        {
            this.thruDate = thruDate;
        }

        protected AbstractDocumentAssociationStateEvent(DocumentAssociationEventId eventId) {
            super(eventId);
        }
    }

    public static abstract class AbstractDocumentAssociationStateCreated extends AbstractDocumentAssociationStateEvent implements DocumentAssociationEvent.DocumentAssociationStateCreated
    {
        public AbstractDocumentAssociationStateCreated() {
            this(new DocumentAssociationEventId());
        }

        public AbstractDocumentAssociationStateCreated(DocumentAssociationEventId eventId) {
            super(eventId);
        }

        public String getEventType() {
            return StateEventType.CREATED;
        }

    }


    public static abstract class AbstractDocumentAssociationStateMergePatched extends AbstractDocumentAssociationStateEvent implements DocumentAssociationEvent.DocumentAssociationStateMergePatched
    {
        public AbstractDocumentAssociationStateMergePatched() {
            this(new DocumentAssociationEventId());
        }

        public AbstractDocumentAssociationStateMergePatched(DocumentAssociationEventId eventId) {
            super(eventId);
        }

        public String getEventType() {
            return StateEventType.MERGE_PATCHED;
        }

        private Boolean isPropertyDocumentIdRemoved;

        public Boolean getIsPropertyDocumentIdRemoved() {
            return this.isPropertyDocumentIdRemoved;
        }

        public void setIsPropertyDocumentIdRemoved(Boolean removed) {
            this.isPropertyDocumentIdRemoved = removed;
        }

        private Boolean isPropertyDocumentIdToRemoved;

        public Boolean getIsPropertyDocumentIdToRemoved() {
            return this.isPropertyDocumentIdToRemoved;
        }

        public void setIsPropertyDocumentIdToRemoved(Boolean removed) {
            this.isPropertyDocumentIdToRemoved = removed;
        }

        private Boolean isPropertyDocumentAssocTypeIdRemoved;

        public Boolean getIsPropertyDocumentAssocTypeIdRemoved() {
            return this.isPropertyDocumentAssocTypeIdRemoved;
        }

        public void setIsPropertyDocumentAssocTypeIdRemoved(Boolean removed) {
            this.isPropertyDocumentAssocTypeIdRemoved = removed;
        }

        private Boolean isPropertyFromDateRemoved;

        public Boolean getIsPropertyFromDateRemoved() {
            return this.isPropertyFromDateRemoved;
        }

        public void setIsPropertyFromDateRemoved(Boolean removed) {
            this.isPropertyFromDateRemoved = removed;
        }

        private Boolean isPropertyThruDateRemoved;

        public Boolean getIsPropertyThruDateRemoved() {
            return this.isPropertyThruDateRemoved;
        }

        public void setIsPropertyThruDateRemoved(Boolean removed) {
            this.isPropertyThruDateRemoved = removed;
        }


    }


    public static abstract class AbstractDocumentAssociationStateDeleted extends AbstractDocumentAssociationStateEvent implements DocumentAssociationEvent.DocumentAssociationStateDeleted
    {
        public AbstractDocumentAssociationStateDeleted() {
            this(new DocumentAssociationEventId());
        }

        public AbstractDocumentAssociationStateDeleted(DocumentAssociationEventId eventId) {
            super(eventId);
        }

        public String getEventType() {
            return StateEventType.DELETED;
        }

    }

    public static class SimpleDocumentAssociationStateCreated extends AbstractDocumentAssociationStateCreated
    {
        public SimpleDocumentAssociationStateCreated() {
        }

        public SimpleDocumentAssociationStateCreated(DocumentAssociationEventId eventId) {
            super(eventId);
        }
    }

    public static class SimpleDocumentAssociationStateMergePatched extends AbstractDocumentAssociationStateMergePatched
    {
        public SimpleDocumentAssociationStateMergePatched() {
        }

        public SimpleDocumentAssociationStateMergePatched(DocumentAssociationEventId eventId) {
            super(eventId);
        }
    }

    public static class SimpleDocumentAssociationStateDeleted extends AbstractDocumentAssociationStateDeleted
    {
        public SimpleDocumentAssociationStateDeleted() {
        }

        public SimpleDocumentAssociationStateDeleted(DocumentAssociationEventId eventId) {
            super(eventId);
        }
    }

}
