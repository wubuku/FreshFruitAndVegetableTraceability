// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.physicalinventory;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.physicalinventory.PhysicalInventoryEvent.*;

public abstract class AbstractPhysicalInventoryState implements PhysicalInventoryState.SqlPhysicalInventoryState, Saveable {

    private String physicalInventoryId;

    public String getPhysicalInventoryId() {
        return this.physicalInventoryId;
    }

    public void setPhysicalInventoryId(String physicalInventoryId) {
        this.physicalInventoryId = physicalInventoryId;
    }

    private OffsetDateTime physicalInventoryDate;

    public OffsetDateTime getPhysicalInventoryDate() {
        return this.physicalInventoryDate;
    }

    public void setPhysicalInventoryDate(OffsetDateTime physicalInventoryDate) {
        this.physicalInventoryDate = physicalInventoryDate;
    }

    private String partyId;

    public String getPartyId() {
        return this.partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    private String generalComments;

    public String getGeneralComments() {
        return this.generalComments;
    }

    public void setGeneralComments(String generalComments) {
        this.generalComments = generalComments;
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

    private Set<InventoryItemVarianceState> protectedInventoryItemVariances = new HashSet<>();

    protected Set<InventoryItemVarianceState> getProtectedInventoryItemVariances() {
        return this.protectedInventoryItemVariances;
    }

    protected void setProtectedInventoryItemVariances(Set<InventoryItemVarianceState> protectedInventoryItemVariances) {
        this.protectedInventoryItemVariances = protectedInventoryItemVariances;
    }

    private EntityStateCollection.MutableEntityStateCollection<String, InventoryItemVarianceState> inventoryItemVariances;

    public EntityStateCollection.MutableEntityStateCollection<String, InventoryItemVarianceState> getInventoryItemVariances() {
        return this.inventoryItemVariances;
    }

    public void setInventoryItemVariances(EntityStateCollection.MutableEntityStateCollection<String, InventoryItemVarianceState> inventoryItemVariances) {
        this.inventoryItemVariances = inventoryItemVariances;
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

    public AbstractPhysicalInventoryState(List<Event> events) {
        initializeForReapplying();
        if (events != null && events.size() > 0) {
            this.setPhysicalInventoryId(((PhysicalInventoryEvent.SqlPhysicalInventoryEvent) events.get(0)).getPhysicalInventoryEventId().getPhysicalInventoryId());
            for (Event e : events) {
                mutate(e);
                this.setVersion((this.getVersion() == null ? PhysicalInventoryState.VERSION_NULL : this.getVersion()) + 1);
            }
        }
    }


    public AbstractPhysicalInventoryState() {
        initializeProperties();
    }

    protected void initializeForReapplying() {
        this.forReapplying = true;

        initializeProperties();
    }
    
    protected void initializeProperties() {
        inventoryItemVariances = new SimpleInventoryItemVarianceStateCollection();
    }

    @Override
    public int hashCode() {
        return getPhysicalInventoryId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj instanceof PhysicalInventoryState) {
            return Objects.equals(this.getPhysicalInventoryId(), ((PhysicalInventoryState)obj).getPhysicalInventoryId());
        }
        return false;
    }


    public void mutate(Event e) {
        setStateReadOnly(false);
        if (e instanceof PhysicalInventoryStateCreated) {
            when((PhysicalInventoryStateCreated) e);
        } else if (e instanceof PhysicalInventoryStateMergePatched) {
            when((PhysicalInventoryStateMergePatched) e);
        } else {
            throw new UnsupportedOperationException(String.format("Unsupported event type: %1$s", e.getClass().getName()));
        }
    }

    public void when(PhysicalInventoryStateCreated e) {
        throwOnWrongEvent(e);

        this.setPhysicalInventoryDate(e.getPhysicalInventoryDate());
        this.setPartyId(e.getPartyId());
        this.setGeneralComments(e.getGeneralComments());

        this.setCreatedBy(e.getCreatedBy());
        this.setCreatedAt(e.getCreatedAt());

        for (InventoryItemVarianceEvent.InventoryItemVarianceStateCreated innerEvent : e.getInventoryItemVarianceEvents()) {
            InventoryItemVarianceState innerState = ((EntityStateCollection.MutableEntityStateCollection<String, InventoryItemVarianceState>)this.getInventoryItemVariances()).getOrAddDefault(((InventoryItemVarianceEvent.SqlInventoryItemVarianceEvent)innerEvent).getInventoryItemVarianceEventId().getInventoryItemId());
            ((InventoryItemVarianceState.SqlInventoryItemVarianceState)innerState).mutate(innerEvent);
        }
    }

    public void merge(PhysicalInventoryState s) {
        if (s == this) {
            return;
        }
        this.setPhysicalInventoryDate(s.getPhysicalInventoryDate());
        this.setPartyId(s.getPartyId());
        this.setGeneralComments(s.getGeneralComments());

        if (s.getInventoryItemVariances() != null) {
            Iterable<InventoryItemVarianceState> iterable;
            if (s.getInventoryItemVariances().isLazy()) {
                iterable = s.getInventoryItemVariances().getLoadedStates();
            } else {
                iterable = s.getInventoryItemVariances();
            }
            if (iterable != null) {
                for (InventoryItemVarianceState ss : iterable) {
                    InventoryItemVarianceState thisInnerState = ((EntityStateCollection.MutableEntityStateCollection<String, InventoryItemVarianceState>)this.getInventoryItemVariances()).getOrAddDefault(ss.getInventoryItemId());
                    ((AbstractInventoryItemVarianceState) thisInnerState).merge(ss);
                }
            }
        }
        if (s.getInventoryItemVariances() != null) {
            if (s.getInventoryItemVariances() instanceof EntityStateCollection.RemovalLoggedEntityStateCollection) {
                if (((EntityStateCollection.RemovalLoggedEntityStateCollection)s.getInventoryItemVariances()).getRemovedStates() != null) {
                    for (InventoryItemVarianceState ss : ((EntityStateCollection.RemovalLoggedEntityStateCollection<String, InventoryItemVarianceState>)s.getInventoryItemVariances()).getRemovedStates()) {
                        InventoryItemVarianceState thisInnerState = ((EntityStateCollection.MutableEntityStateCollection<String, InventoryItemVarianceState>)this.getInventoryItemVariances()).getOrAddDefault(ss.getInventoryItemId());
                        ((EntityStateCollection.MutableEntityStateCollection)this.getInventoryItemVariances()).removeState(thisInnerState);
                    }
                }
            } else {
                if (s.getInventoryItemVariances().isAllLoaded()) {
                    Set<String> removedStateIds = new HashSet<>(this.getInventoryItemVariances().stream().map(i -> i.getInventoryItemId()).collect(java.util.stream.Collectors.toList()));
                    s.getInventoryItemVariances().forEach(i -> removedStateIds.remove(i.getInventoryItemId()));
                    for (String i : removedStateIds) {
                        InventoryItemVarianceState thisInnerState = ((EntityStateCollection.MutableEntityStateCollection<String, InventoryItemVarianceState>)this.getInventoryItemVariances()).getOrAddDefault(i);
                        ((EntityStateCollection.MutableEntityStateCollection)this.getInventoryItemVariances()).removeState(thisInnerState);
                    }
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        }
    }

    public void when(PhysicalInventoryStateMergePatched e) {
        throwOnWrongEvent(e);

        if (e.getPhysicalInventoryDate() == null) {
            if (e.getIsPropertyPhysicalInventoryDateRemoved() != null && e.getIsPropertyPhysicalInventoryDateRemoved()) {
                this.setPhysicalInventoryDate(null);
            }
        } else {
            this.setPhysicalInventoryDate(e.getPhysicalInventoryDate());
        }
        if (e.getPartyId() == null) {
            if (e.getIsPropertyPartyIdRemoved() != null && e.getIsPropertyPartyIdRemoved()) {
                this.setPartyId(null);
            }
        } else {
            this.setPartyId(e.getPartyId());
        }
        if (e.getGeneralComments() == null) {
            if (e.getIsPropertyGeneralCommentsRemoved() != null && e.getIsPropertyGeneralCommentsRemoved()) {
                this.setGeneralComments(null);
            }
        } else {
            this.setGeneralComments(e.getGeneralComments());
        }

        this.setUpdatedBy(e.getCreatedBy());
        this.setUpdatedAt(e.getCreatedAt());

        for (InventoryItemVarianceEvent innerEvent : e.getInventoryItemVarianceEvents()) {
            InventoryItemVarianceState innerState = ((EntityStateCollection.MutableEntityStateCollection<String, InventoryItemVarianceState>)this.getInventoryItemVariances()).getOrAddDefault(((InventoryItemVarianceEvent.SqlInventoryItemVarianceEvent)innerEvent).getInventoryItemVarianceEventId().getInventoryItemId());
            ((InventoryItemVarianceState.SqlInventoryItemVarianceState)innerState).mutate(innerEvent);
        }
    }

    public void save() {
        if (inventoryItemVariances instanceof Saveable) {
            ((Saveable)inventoryItemVariances).save();
        }
    }

    protected void throwOnWrongEvent(PhysicalInventoryEvent event) {
        String stateEntityId = this.getPhysicalInventoryId(); // Aggregate Id
        String eventEntityId = ((PhysicalInventoryEvent.SqlPhysicalInventoryEvent)event).getPhysicalInventoryEventId().getPhysicalInventoryId(); // EntityBase.Aggregate.GetEventIdPropertyIdName();
        if (!stateEntityId.equals(eventEntityId)) {
            throw DomainError.named("mutateWrongEntity", "Entity Id %1$s in state but entity id %2$s in event", stateEntityId, eventEntityId);
        }


        Long stateVersion = this.getVersion();
        Long eventVersion = ((PhysicalInventoryEvent.SqlPhysicalInventoryEvent)event).getPhysicalInventoryEventId().getVersion();// Event Version
        if (eventVersion == null) {
            throw new NullPointerException("event.getPhysicalInventoryEventId().getVersion() == null");
        }
        if (!(stateVersion == null && eventVersion.equals(VERSION_NULL)) && !eventVersion.equals(stateVersion)) {
            throw DomainError.named("concurrencyConflict", "Conflict between state version (%1$s) and event version (%2$s)", stateVersion, eventVersion == VERSION_NULL ? "NULL" : eventVersion + "");
        }

    }


    public static class SimplePhysicalInventoryState extends AbstractPhysicalInventoryState {

        public SimplePhysicalInventoryState() {
        }

        public SimplePhysicalInventoryState(List<Event> events) {
            super(events);
        }

        public static SimplePhysicalInventoryState newForReapplying() {
            SimplePhysicalInventoryState s = new SimplePhysicalInventoryState();
            s.initializeForReapplying();
            return s;
        }

    }


    class SimpleInventoryItemVarianceStateCollection implements EntityStateCollection.MutableEntityStateCollection<String, InventoryItemVarianceState>, Collection<InventoryItemVarianceState> {

        @Override
        public InventoryItemVarianceState get(String inventoryItemId) {
            return protectedInventoryItemVariances.stream().filter(
                            e -> e.getInventoryItemId().equals(inventoryItemId))
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
        public Collection<InventoryItemVarianceState> getLoadedStates() {
            return protectedInventoryItemVariances;
        }

        @Override
        public InventoryItemVarianceState getOrAddDefault(String inventoryItemId) {
            InventoryItemVarianceState s = get(inventoryItemId);
            if (s == null) {
                PhysicalInventoryInventoryItemVarianceId globalId = new PhysicalInventoryInventoryItemVarianceId(getPhysicalInventoryId(), inventoryItemId);
                AbstractInventoryItemVarianceState state = new AbstractInventoryItemVarianceState.SimpleInventoryItemVarianceState();
                state.setPhysicalInventoryInventoryItemVarianceId(globalId);
                state.setCreatedBy(ApplicationContext.current.getRequesterId());
                state.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
                add(state);
                s = state;
            } else {
                AbstractInventoryItemVarianceState state = (AbstractInventoryItemVarianceState) s;
                state.setUpdatedBy(ApplicationContext.current.getRequesterId());
                state.setUpdatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
            }
            return s;
        }

        @Override
        public int size() {
            return protectedInventoryItemVariances.size();
        }

        @Override
        public boolean isEmpty() {
            return protectedInventoryItemVariances.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return protectedInventoryItemVariances.contains(o);
        }

        @Override
        public Iterator<InventoryItemVarianceState> iterator() {
            return protectedInventoryItemVariances.iterator();
        }

        @Override
        public java.util.stream.Stream<InventoryItemVarianceState> stream() {
            return protectedInventoryItemVariances.stream();
        }

        @Override
        public Object[] toArray() {
            return protectedInventoryItemVariances.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return protectedInventoryItemVariances.toArray(a);
        }

        @Override
        public boolean add(InventoryItemVarianceState s) {
            if (s instanceof AbstractInventoryItemVarianceState) {
                AbstractInventoryItemVarianceState state = (AbstractInventoryItemVarianceState) s;
                state.setProtectedPhysicalInventoryState(AbstractPhysicalInventoryState.this);
            }
            return protectedInventoryItemVariances.add(s);
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof AbstractInventoryItemVarianceState) {
                AbstractInventoryItemVarianceState s = (AbstractInventoryItemVarianceState) o;
                s.setProtectedPhysicalInventoryState(null);
            }
            return protectedInventoryItemVariances.remove(o);
        }

        @Override
        public boolean removeState(InventoryItemVarianceState s) {
            return remove(s);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return protectedInventoryItemVariances.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends InventoryItemVarianceState> c) {
            return protectedInventoryItemVariances.addAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return protectedInventoryItemVariances.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return protectedInventoryItemVariances.retainAll(c);
        }

        @Override
        public void clear() {
            protectedInventoryItemVariances.clear();
        }
    }


}

