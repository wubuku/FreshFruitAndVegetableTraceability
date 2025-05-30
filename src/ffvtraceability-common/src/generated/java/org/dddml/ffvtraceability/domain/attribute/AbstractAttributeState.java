// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.attribute;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.attribute.AttributeEvent.*;

public abstract class AbstractAttributeState implements AttributeState.SqlAttributeState, Saveable {

    private String attributeId;

    public String getAttributeId() {
        return this.attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    private String attributeType;

    public String getAttributeType() {
        return this.attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    private String attributeName;

    public String getAttributeName() {
        return this.attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    private String description;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String isMandatory;

    public String getIsMandatory() {
        return this.isMandatory;
    }

    public void setIsMandatory(String isMandatory) {
        this.isMandatory = isMandatory;
    }

    private Long attributeLength;

    public Long getAttributeLength() {
        return this.attributeLength;
    }

    public void setAttributeLength(Long attributeLength) {
        this.attributeLength = attributeLength;
    }

    private String isEnumeration;

    public String getIsEnumeration() {
        return this.isEnumeration;
    }

    public void setIsEnumeration(String isEnumeration) {
        this.isEnumeration = isEnumeration;
    }

    private Long scale;

    public Long getScale() {
        return this.scale;
    }

    public void setScale(Long scale) {
        this.scale = scale;
    }

    private String truncatedTo;

    public String getTruncatedTo() {
        return this.truncatedTo;
    }

    public void setTruncatedTo(String truncatedTo) {
        this.truncatedTo = truncatedTo;
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

    private Boolean active;

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    private Set<AttributeValueState> protectedAttributeValues = new HashSet<>();

    protected Set<AttributeValueState> getProtectedAttributeValues() {
        return this.protectedAttributeValues;
    }

    protected void setProtectedAttributeValues(Set<AttributeValueState> protectedAttributeValues) {
        this.protectedAttributeValues = protectedAttributeValues;
    }

    private EntityStateCollection.MutableEntityStateCollection<String, AttributeValueState> attributeValues;

    public EntityStateCollection.MutableEntityStateCollection<String, AttributeValueState> getAttributeValues() {
        return this.attributeValues;
    }

    public void setAttributeValues(EntityStateCollection.MutableEntityStateCollection<String, AttributeValueState> attributeValues) {
        this.attributeValues = attributeValues;
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

    public AbstractAttributeState(List<Event> events) {
        initializeForReapplying();
        if (events != null && events.size() > 0) {
            this.setAttributeId(((AttributeEvent.SqlAttributeEvent) events.get(0)).getAttributeEventId().getAttributeId());
            for (Event e : events) {
                mutate(e);
                this.setVersion((this.getVersion() == null ? AttributeState.VERSION_NULL : this.getVersion()) + 1);
            }
        }
    }


    public AbstractAttributeState() {
        initializeProperties();
    }

    protected void initializeForReapplying() {
        this.forReapplying = true;

        initializeProperties();
    }
    
    protected void initializeProperties() {
        attributeValues = new SimpleAttributeValueStateCollection();
    }

    @Override
    public int hashCode() {
        return getAttributeId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj instanceof AttributeState) {
            return Objects.equals(this.getAttributeId(), ((AttributeState)obj).getAttributeId());
        }
        return false;
    }


    public void mutate(Event e) {
        setStateReadOnly(false);
        if (e instanceof AttributeStateCreated) {
            when((AttributeStateCreated) e);
        } else if (e instanceof AttributeStateMergePatched) {
            when((AttributeStateMergePatched) e);
        } else {
            throw new UnsupportedOperationException(String.format("Unsupported event type: %1$s", e.getClass().getName()));
        }
    }

    public void when(AttributeStateCreated e) {
        throwOnWrongEvent(e);

        this.setAttributeType(e.getAttributeType());
        this.setAttributeName(e.getAttributeName());
        this.setDescription(e.getDescription());
        this.setIsMandatory(e.getIsMandatory());
        this.setAttributeLength(e.getAttributeLength());
        this.setIsEnumeration(e.getIsEnumeration());
        this.setScale(e.getScale());
        this.setTruncatedTo(e.getTruncatedTo());
        this.setActive(e.getActive());

        this.setCreatedBy(e.getCreatedBy());
        this.setCreatedAt(e.getCreatedAt());

        for (AttributeValueEvent.AttributeValueStateCreated innerEvent : e.getAttributeValueEvents()) {
            AttributeValueState innerState = ((EntityStateCollection.MutableEntityStateCollection<String, AttributeValueState>)this.getAttributeValues()).getOrAddDefault(((AttributeValueEvent.SqlAttributeValueEvent)innerEvent).getAttributeValueEventId().getValue());
            ((AttributeValueState.SqlAttributeValueState)innerState).mutate(innerEvent);
        }
    }

    public void merge(AttributeState s) {
        if (s == this) {
            return;
        }
        this.setAttributeType(s.getAttributeType());
        this.setAttributeName(s.getAttributeName());
        this.setDescription(s.getDescription());
        this.setIsMandatory(s.getIsMandatory());
        this.setAttributeLength(s.getAttributeLength());
        this.setIsEnumeration(s.getIsEnumeration());
        this.setScale(s.getScale());
        this.setTruncatedTo(s.getTruncatedTo());
        this.setActive(s.getActive());

        if (s.getAttributeValues() != null) {
            Iterable<AttributeValueState> iterable;
            if (s.getAttributeValues().isLazy()) {
                iterable = s.getAttributeValues().getLoadedStates();
            } else {
                iterable = s.getAttributeValues();
            }
            if (iterable != null) {
                for (AttributeValueState ss : iterable) {
                    AttributeValueState thisInnerState = ((EntityStateCollection.MutableEntityStateCollection<String, AttributeValueState>)this.getAttributeValues()).getOrAddDefault(ss.getValue());
                    ((AbstractAttributeValueState) thisInnerState).merge(ss);
                }
            }
        }
        if (s.getAttributeValues() != null) {
            if (s.getAttributeValues() instanceof EntityStateCollection.RemovalLoggedEntityStateCollection) {
                if (((EntityStateCollection.RemovalLoggedEntityStateCollection)s.getAttributeValues()).getRemovedStates() != null) {
                    for (AttributeValueState ss : ((EntityStateCollection.RemovalLoggedEntityStateCollection<String, AttributeValueState>)s.getAttributeValues()).getRemovedStates()) {
                        AttributeValueState thisInnerState = ((EntityStateCollection.MutableEntityStateCollection<String, AttributeValueState>)this.getAttributeValues()).getOrAddDefault(ss.getValue());
                        ((EntityStateCollection.MutableEntityStateCollection)this.getAttributeValues()).removeState(thisInnerState);
                    }
                }
            } else {
                if (s.getAttributeValues().isAllLoaded()) {
                    Set<String> removedStateIds = new HashSet<>(this.getAttributeValues().stream().map(i -> i.getValue()).collect(java.util.stream.Collectors.toList()));
                    s.getAttributeValues().forEach(i -> removedStateIds.remove(i.getValue()));
                    for (String i : removedStateIds) {
                        AttributeValueState thisInnerState = ((EntityStateCollection.MutableEntityStateCollection<String, AttributeValueState>)this.getAttributeValues()).getOrAddDefault(i);
                        ((EntityStateCollection.MutableEntityStateCollection)this.getAttributeValues()).removeState(thisInnerState);
                    }
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        }
    }

    public void when(AttributeStateMergePatched e) {
        throwOnWrongEvent(e);

        if (e.getAttributeType() == null) {
            if (e.getIsPropertyAttributeTypeRemoved() != null && e.getIsPropertyAttributeTypeRemoved()) {
                this.setAttributeType(null);
            }
        } else {
            this.setAttributeType(e.getAttributeType());
        }
        if (e.getAttributeName() == null) {
            if (e.getIsPropertyAttributeNameRemoved() != null && e.getIsPropertyAttributeNameRemoved()) {
                this.setAttributeName(null);
            }
        } else {
            this.setAttributeName(e.getAttributeName());
        }
        if (e.getDescription() == null) {
            if (e.getIsPropertyDescriptionRemoved() != null && e.getIsPropertyDescriptionRemoved()) {
                this.setDescription(null);
            }
        } else {
            this.setDescription(e.getDescription());
        }
        if (e.getIsMandatory() == null) {
            if (e.getIsPropertyIsMandatoryRemoved() != null && e.getIsPropertyIsMandatoryRemoved()) {
                this.setIsMandatory(null);
            }
        } else {
            this.setIsMandatory(e.getIsMandatory());
        }
        if (e.getAttributeLength() == null) {
            if (e.getIsPropertyAttributeLengthRemoved() != null && e.getIsPropertyAttributeLengthRemoved()) {
                this.setAttributeLength(null);
            }
        } else {
            this.setAttributeLength(e.getAttributeLength());
        }
        if (e.getIsEnumeration() == null) {
            if (e.getIsPropertyIsEnumerationRemoved() != null && e.getIsPropertyIsEnumerationRemoved()) {
                this.setIsEnumeration(null);
            }
        } else {
            this.setIsEnumeration(e.getIsEnumeration());
        }
        if (e.getScale() == null) {
            if (e.getIsPropertyScaleRemoved() != null && e.getIsPropertyScaleRemoved()) {
                this.setScale(null);
            }
        } else {
            this.setScale(e.getScale());
        }
        if (e.getTruncatedTo() == null) {
            if (e.getIsPropertyTruncatedToRemoved() != null && e.getIsPropertyTruncatedToRemoved()) {
                this.setTruncatedTo(null);
            }
        } else {
            this.setTruncatedTo(e.getTruncatedTo());
        }
        if (e.getActive() == null) {
            if (e.getIsPropertyActiveRemoved() != null && e.getIsPropertyActiveRemoved()) {
                this.setActive(null);
            }
        } else {
            this.setActive(e.getActive());
        }

        this.setUpdatedBy(e.getCreatedBy());
        this.setUpdatedAt(e.getCreatedAt());

        for (AttributeValueEvent innerEvent : e.getAttributeValueEvents()) {
            AttributeValueState innerState = ((EntityStateCollection.MutableEntityStateCollection<String, AttributeValueState>)this.getAttributeValues()).getOrAddDefault(((AttributeValueEvent.SqlAttributeValueEvent)innerEvent).getAttributeValueEventId().getValue());
            ((AttributeValueState.SqlAttributeValueState)innerState).mutate(innerEvent);
        }
    }

    public void save() {
        if (attributeValues instanceof Saveable) {
            ((Saveable)attributeValues).save();
        }
    }

    protected void throwOnWrongEvent(AttributeEvent event) {
        String stateEntityId = this.getAttributeId(); // Aggregate Id
        String eventEntityId = ((AttributeEvent.SqlAttributeEvent)event).getAttributeEventId().getAttributeId(); // EntityBase.Aggregate.GetEventIdPropertyIdName();
        if (!stateEntityId.equals(eventEntityId)) {
            throw DomainError.named("mutateWrongEntity", "Entity Id %1$s in state but entity id %2$s in event", stateEntityId, eventEntityId);
        }


        Long stateVersion = this.getVersion();
        Long eventVersion = ((AttributeEvent.SqlAttributeEvent)event).getAttributeEventId().getVersion();// Event Version
        if (eventVersion == null) {
            throw new NullPointerException("event.getAttributeEventId().getVersion() == null");
        }
        if (!(stateVersion == null && eventVersion.equals(VERSION_NULL)) && !eventVersion.equals(stateVersion)) {
            throw DomainError.named("concurrencyConflict", "Conflict between state version (%1$s) and event version (%2$s)", stateVersion, eventVersion == VERSION_NULL ? "NULL" : eventVersion + "");
        }

    }


    public static class SimpleAttributeState extends AbstractAttributeState {

        public SimpleAttributeState() {
        }

        public SimpleAttributeState(List<Event> events) {
            super(events);
        }

        public static SimpleAttributeState newForReapplying() {
            SimpleAttributeState s = new SimpleAttributeState();
            s.initializeForReapplying();
            return s;
        }

    }


    class SimpleAttributeValueStateCollection implements EntityStateCollection.MutableEntityStateCollection<String, AttributeValueState>, Collection<AttributeValueState> {

        @Override
        public AttributeValueState get(String value) {
            return protectedAttributeValues.stream().filter(
                            e -> e.getValue().equals(value))
                    .findFirst().orElse(null);
        }

        @Override
        public boolean isLazy() {
            return false;
        }

        @Override
        public boolean isAllLoaded() {
            return true;
        }

        @Override
        public Collection<AttributeValueState> getLoadedStates() {
            return protectedAttributeValues;
        }

        @Override
        public AttributeValueState getOrAddDefault(String value) {
            AttributeValueState s = get(value);
            if (s == null) {
                AttributeValueId globalId = new AttributeValueId(getAttributeId(), value);
                AbstractAttributeValueState state = new AbstractAttributeValueState.SimpleAttributeValueState();
                state.setAttributeValueId(globalId);
                state.setCreatedBy(ApplicationContext.current.getRequesterId());
                state.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
                add(state);
                s = state;
            } else {
                AbstractAttributeValueState state = (AbstractAttributeValueState) s;
                state.setUpdatedBy(ApplicationContext.current.getRequesterId());
                state.setUpdatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
            }
            return s;
        }

        @Override
        public int size() {
            return protectedAttributeValues.size();
        }

        @Override
        public boolean isEmpty() {
            return protectedAttributeValues.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return protectedAttributeValues.contains(o);
        }

        @Override
        public Iterator<AttributeValueState> iterator() {
            return protectedAttributeValues.iterator();
        }

        @Override
        public java.util.stream.Stream<AttributeValueState> stream() {
            return protectedAttributeValues.stream();
        }

        @Override
        public Object[] toArray() {
            return protectedAttributeValues.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return protectedAttributeValues.toArray(a);
        }

        @Override
        public boolean add(AttributeValueState s) {
            if (s instanceof AbstractAttributeValueState) {
                AbstractAttributeValueState state = (AbstractAttributeValueState) s;
                state.setProtectedAttributeState(AbstractAttributeState.this);
            }
            return protectedAttributeValues.add(s);
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof AbstractAttributeValueState) {
                AbstractAttributeValueState s = (AbstractAttributeValueState) o;
                s.setProtectedAttributeState(null);
            }
            return protectedAttributeValues.remove(o);
        }

        @Override
        public boolean removeState(AttributeValueState s) {
            return remove(s);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return protectedAttributeValues.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends AttributeValueState> c) {
            return protectedAttributeValues.addAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return protectedAttributeValues.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return protectedAttributeValues.retainAll(c);
        }

        @Override
        public void clear() {
            protectedAttributeValues.clear();
        }
    }


}

