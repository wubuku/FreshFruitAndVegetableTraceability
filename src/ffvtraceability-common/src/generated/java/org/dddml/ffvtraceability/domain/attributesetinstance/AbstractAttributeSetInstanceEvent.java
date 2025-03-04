// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.attributesetinstance;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.AbstractEvent;

public abstract class AbstractAttributeSetInstanceEvent extends AbstractEvent implements AttributeSetInstanceEvent.SqlAttributeSetInstanceEvent {
    private AttributeSetInstanceState.MutableAttributeSetInstanceState state;

    public AttributeSetInstanceState.MutableAttributeSetInstanceState getAttributeSetInstanceState() {
        return state;
    }

    public AttributeSetInstanceEventId getAttributeSetInstanceEventId() {
        AttributeSetInstanceEventId eventId = new AttributeSetInstanceEventId(state.getAttributeSetInstanceId(), AttributeSetInstanceState.VERSION_NULL);
        return eventId;
    }

    public void setAttributeSetInstanceEventId(AttributeSetInstanceEventId eventId) {
        this.state.setAttributeSetInstanceId(eventId.getAttributeSetInstanceId());
    }

    public String getAttributeSetInstanceId() {
        return getAttributeSetInstanceEventId().getAttributeSetInstanceId();
    }

    public void setAttributeSetInstanceId(String attributeSetInstanceId) {
        getAttributeSetInstanceEventId().setAttributeSetInstanceId(attributeSetInstanceId);
    }

    private boolean eventReadOnly;

    public boolean getEventReadOnly() { return this.eventReadOnly; }

    public void setEventReadOnly(boolean readOnly) { this.eventReadOnly = readOnly; }

    public Long getVersion() {
        return getAttributeSetInstanceEventId().getVersion();
    }
    
    public void setVersion(Long version) {
        getAttributeSetInstanceEventId().setVersion(version);
    }

    private String tenantId;

    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCreatedBy() {
        return this.state.getCreatedBy();
    }

    public void setCreatedBy(String createdBy) {
        this.state.setCreatedBy(createdBy);
    }

    public OffsetDateTime getCreatedAt() {
        return this.state.getCreatedAt();
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.state.setCreatedAt(createdAt);
    }


    public String getCommandId() {
        return this.state.getCommandId();
    }

    public void setCommandId(String commandId) {
        this.state.setCommandId(commandId);
    }

    protected AbstractAttributeSetInstanceEvent() {
        this(new AbstractAttributeSetInstanceState.SimpleAttributeSetInstanceState());
    }

    protected AbstractAttributeSetInstanceEvent(AttributeSetInstanceEventId eventId) {
        this(new AbstractAttributeSetInstanceState.SimpleAttributeSetInstanceState());
        setAttributeSetInstanceEventId(eventId);
    }

    protected AbstractAttributeSetInstanceEvent(AttributeSetInstanceState s) {
        if (s == null) { throw new IllegalArgumentException(); }
        this.state = (AttributeSetInstanceState.MutableAttributeSetInstanceState)s;
    }


    public abstract String getEventType();

    public static class AttributeSetInstanceLobEvent extends AbstractAttributeSetInstanceEvent {

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
            return "AttributeSetInstanceLobEvent";
        }

    }


    public static abstract class AbstractAttributeSetInstanceStateEvent extends AbstractAttributeSetInstanceEvent implements AttributeSetInstanceEvent.AttributeSetInstanceStateEvent {
        public org.dddml.ffvtraceability.domain.ConsistentEqualityMap getAttributes()
        {
            return this.getAttributeSetInstanceState().getAttributes();
        }

        public void setAttributes(org.dddml.ffvtraceability.domain.ConsistentEqualityMap attributes)
        {
            this.getAttributeSetInstanceState().setAttributes(attributes);
        }

        protected AbstractAttributeSetInstanceStateEvent(AttributeSetInstanceEventId eventId) {
            super(eventId);
        }

        public AbstractAttributeSetInstanceStateEvent(AttributeSetInstanceState s) {
            super(s);
        }
    }

    public static abstract class AbstractAttributeSetInstanceStateCreated extends AbstractAttributeSetInstanceStateEvent implements AttributeSetInstanceEvent.AttributeSetInstanceStateCreated
    {
        public AbstractAttributeSetInstanceStateCreated() {
            this(new AttributeSetInstanceEventId());
        }

        public AbstractAttributeSetInstanceStateCreated(AttributeSetInstanceEventId eventId) {
            super(eventId);
        }

        public AbstractAttributeSetInstanceStateCreated(AttributeSetInstanceState s) {
            super(s);
        }

        public String getEventType() {
            return StateEventType.CREATED;
        }

    }



    public static class SimpleAttributeSetInstanceStateCreated extends AbstractAttributeSetInstanceStateCreated
    {
        public SimpleAttributeSetInstanceStateCreated() {
        }

        public SimpleAttributeSetInstanceStateCreated(AttributeSetInstanceEventId eventId) {
            super(eventId);
        }

        public SimpleAttributeSetInstanceStateCreated(AttributeSetInstanceState s) {
            super(s);
        }
    }

}

