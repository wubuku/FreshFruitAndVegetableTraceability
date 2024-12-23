// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.partyrole;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.AbstractEvent;

public abstract class AbstractPartyRoleEvent extends AbstractEvent implements PartyRoleEvent.SqlPartyRoleEvent {
    private PartyRoleEventId partyRoleEventId = new PartyRoleEventId();

    public PartyRoleEventId getPartyRoleEventId() {
        return this.partyRoleEventId;
    }

    public void setPartyRoleEventId(PartyRoleEventId eventId) {
        this.partyRoleEventId = eventId;
    }
    
    public PartyRoleId getPartyRoleId() {
        return getPartyRoleEventId().getPartyRoleId();
    }

    public void setPartyRoleId(PartyRoleId partyRoleId) {
        getPartyRoleEventId().setPartyRoleId(partyRoleId);
    }

    private boolean eventReadOnly;

    public boolean getEventReadOnly() { return this.eventReadOnly; }

    public void setEventReadOnly(boolean readOnly) { this.eventReadOnly = readOnly; }

    public Long getVersion() {
        return getPartyRoleEventId().getVersion();
    }
    
    public void setVersion(Long version) {
        getPartyRoleEventId().setVersion(version);
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

    protected AbstractPartyRoleEvent() {
    }

    protected AbstractPartyRoleEvent(PartyRoleEventId eventId) {
        this.partyRoleEventId = eventId;
    }


    public abstract String getEventType();

    public static class PartyRoleLobEvent extends AbstractPartyRoleEvent {

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
            return "PartyRoleLobEvent";
        }

    }


    public static abstract class AbstractPartyRoleStateEvent extends AbstractPartyRoleEvent implements PartyRoleEvent.PartyRoleStateEvent {
        protected AbstractPartyRoleStateEvent(PartyRoleEventId eventId) {
            super(eventId);
        }
    }

    public static abstract class AbstractPartyRoleStateCreated extends AbstractPartyRoleStateEvent implements PartyRoleEvent.PartyRoleStateCreated
    {
        public AbstractPartyRoleStateCreated() {
            this(new PartyRoleEventId());
        }

        public AbstractPartyRoleStateCreated(PartyRoleEventId eventId) {
            super(eventId);
        }

        public String getEventType() {
            return StateEventType.CREATED;
        }

    }


    public static abstract class AbstractPartyRoleStateMergePatched extends AbstractPartyRoleStateEvent implements PartyRoleEvent.PartyRoleStateMergePatched
    {
        public AbstractPartyRoleStateMergePatched() {
            this(new PartyRoleEventId());
        }

        public AbstractPartyRoleStateMergePatched(PartyRoleEventId eventId) {
            super(eventId);
        }

        public String getEventType() {
            return StateEventType.MERGE_PATCHED;
        }


    }



    public static class SimplePartyRoleStateCreated extends AbstractPartyRoleStateCreated
    {
        public SimplePartyRoleStateCreated() {
        }

        public SimplePartyRoleStateCreated(PartyRoleEventId eventId) {
            super(eventId);
        }
    }

    public static class SimplePartyRoleStateMergePatched extends AbstractPartyRoleStateMergePatched
    {
        public SimplePartyRoleStateMergePatched() {
        }

        public SimplePartyRoleStateMergePatched(PartyRoleEventId eventId) {
            super(eventId);
        }
    }

}

