// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.shipment;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.shipment.ShipmentPackageEvent.*;

public abstract class AbstractShipmentPackageState implements ShipmentPackageState.SqlShipmentPackageState, Saveable {

    private ShipmentPackageId shipmentPackageId = new ShipmentPackageId();

    public ShipmentPackageId getShipmentPackageId() {
        return this.shipmentPackageId;
    }

    public void setShipmentPackageId(ShipmentPackageId shipmentPackageId) {
        this.shipmentPackageId = shipmentPackageId;
    }

    private transient ShipmentState shipmentState;

    public ShipmentState getShipmentState() {
        return shipmentState;
    }

    public void setShipmentState(ShipmentState s) {
        shipmentState = s;
    }
    
    private ShipmentState protectedShipmentState;

    protected ShipmentState getProtectedShipmentState() {
        return protectedShipmentState;
    }

    protected void setProtectedShipmentState(ShipmentState protectedShipmentState) {
        this.protectedShipmentState = protectedShipmentState;
    }

    public String getShipmentId() {
        return this.getShipmentPackageId().getShipmentId();
    }
        
    public void setShipmentId(String shipmentId) {
        this.getShipmentPackageId().setShipmentId(shipmentId);
    }

    public String getShipmentPackageSeqId() {
        return this.getShipmentPackageId().getShipmentPackageSeqId();
    }
        
    public void setShipmentPackageSeqId(String shipmentPackageSeqId) {
        this.getShipmentPackageId().setShipmentPackageSeqId(shipmentPackageSeqId);
    }

    private String shipmentBoxTypeId;

    public String getShipmentBoxTypeId() {
        return this.shipmentBoxTypeId;
    }

    public void setShipmentBoxTypeId(String shipmentBoxTypeId) {
        this.shipmentBoxTypeId = shipmentBoxTypeId;
    }

    private OffsetDateTime dateCreated;

    public OffsetDateTime getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    private java.math.BigDecimal boxLength;

    public java.math.BigDecimal getBoxLength() {
        return this.boxLength;
    }

    public void setBoxLength(java.math.BigDecimal boxLength) {
        this.boxLength = boxLength;
    }

    private java.math.BigDecimal boxHeight;

    public java.math.BigDecimal getBoxHeight() {
        return this.boxHeight;
    }

    public void setBoxHeight(java.math.BigDecimal boxHeight) {
        this.boxHeight = boxHeight;
    }

    private java.math.BigDecimal boxWidth;

    public java.math.BigDecimal getBoxWidth() {
        return this.boxWidth;
    }

    public void setBoxWidth(java.math.BigDecimal boxWidth) {
        this.boxWidth = boxWidth;
    }

    private String dimensionUomId;

    public String getDimensionUomId() {
        return this.dimensionUomId;
    }

    public void setDimensionUomId(String dimensionUomId) {
        this.dimensionUomId = dimensionUomId;
    }

    private java.math.BigDecimal weight;

    public java.math.BigDecimal getWeight() {
        return this.weight;
    }

    public void setWeight(java.math.BigDecimal weight) {
        this.weight = weight;
    }

    private String weightUomId;

    public String getWeightUomId() {
        return this.weightUomId;
    }

    public void setWeightUomId(String weightUomId) {
        this.weightUomId = weightUomId;
    }

    private java.math.BigDecimal insuredValue;

    public java.math.BigDecimal getInsuredValue() {
        return this.insuredValue;
    }

    public void setInsuredValue(java.math.BigDecimal insuredValue) {
        this.insuredValue = insuredValue;
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

    private Set<ShipmentPackageContentState> protectedShipmentPackageContents = new HashSet<>();

    protected Set<ShipmentPackageContentState> getProtectedShipmentPackageContents() {
        return this.protectedShipmentPackageContents;
    }

    protected void setProtectedShipmentPackageContents(Set<ShipmentPackageContentState> protectedShipmentPackageContents) {
        this.protectedShipmentPackageContents = protectedShipmentPackageContents;
    }

    private EntityStateCollection<String, ShipmentPackageContentState> shipmentPackageContents;

    public EntityStateCollection<String, ShipmentPackageContentState> getShipmentPackageContents() {
        return this.shipmentPackageContents;
    }

    public void setShipmentPackageContents(EntityStateCollection<String, ShipmentPackageContentState> shipmentPackageContents) {
        this.shipmentPackageContents = shipmentPackageContents;
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


    public AbstractShipmentPackageState() {
        initializeProperties();
    }

    protected void initializeForReapplying() {
        this.forReapplying = true;

        initializeProperties();
    }
    
    protected void initializeProperties() {
        shipmentPackageContents = new SimpleShipmentPackageContentStateCollection();
    }

    @Override
    public int hashCode() {
        return getShipmentPackageSeqId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj instanceof ShipmentPackageState) {
            return Objects.equals(this.getShipmentPackageSeqId(), ((ShipmentPackageState)obj).getShipmentPackageSeqId());
        }
        return false;
    }


    public void mutate(Event e) {
        setStateReadOnly(false);
        if (e instanceof ShipmentPackageStateCreated) {
            when((ShipmentPackageStateCreated) e);
        } else if (e instanceof ShipmentPackageStateMergePatched) {
            when((ShipmentPackageStateMergePatched) e);
        } else if (e instanceof ShipmentPackageStateRemoved) {
            when((ShipmentPackageStateRemoved) e);
        } else {
            throw new UnsupportedOperationException(String.format("Unsupported event type: %1$s", e.getClass().getName()));
        }
    }

    public void when(ShipmentPackageStateCreated e) {
        throwOnWrongEvent(e);

        this.setShipmentBoxTypeId(e.getShipmentBoxTypeId());
        this.setDateCreated(e.getDateCreated());
        this.setBoxLength(e.getBoxLength());
        this.setBoxHeight(e.getBoxHeight());
        this.setBoxWidth(e.getBoxWidth());
        this.setDimensionUomId(e.getDimensionUomId());
        this.setWeight(e.getWeight());
        this.setWeightUomId(e.getWeightUomId());
        this.setInsuredValue(e.getInsuredValue());

        this.setDeleted(false);

        this.setCreatedBy(e.getCreatedBy());
        this.setCreatedAt(e.getCreatedAt());

        for (ShipmentPackageContentEvent.ShipmentPackageContentStateCreated innerEvent : e.getShipmentPackageContentEvents()) {
            ShipmentPackageContentState innerState = ((EntityStateCollection.ModifiableEntityStateCollection<String, ShipmentPackageContentState>)this.getShipmentPackageContents()).getOrAddDefault(((ShipmentPackageContentEvent.SqlShipmentPackageContentEvent)innerEvent).getShipmentPackageContentEventId().getShipmentItemSeqId());
            ((ShipmentPackageContentState.SqlShipmentPackageContentState)innerState).mutate(innerEvent);
        }
    }

    public void merge(ShipmentPackageState s) {
        if (s == this) {
            return;
        }
        this.setShipmentBoxTypeId(s.getShipmentBoxTypeId());
        this.setDateCreated(s.getDateCreated());
        this.setBoxLength(s.getBoxLength());
        this.setBoxHeight(s.getBoxHeight());
        this.setBoxWidth(s.getBoxWidth());
        this.setDimensionUomId(s.getDimensionUomId());
        this.setWeight(s.getWeight());
        this.setWeightUomId(s.getWeightUomId());
        this.setInsuredValue(s.getInsuredValue());

        if (s.getShipmentPackageContents() != null) {
            Iterable<ShipmentPackageContentState> iterable;
            if (s.getShipmentPackageContents().isLazy()) {
                iterable = s.getShipmentPackageContents().getLoadedStates();
            } else {
                iterable = s.getShipmentPackageContents();
            }
            if (iterable != null) {
                for (ShipmentPackageContentState ss : iterable) {
                    ShipmentPackageContentState thisInnerState = ((EntityStateCollection.ModifiableEntityStateCollection<String, ShipmentPackageContentState>)this.getShipmentPackageContents()).getOrAddDefault(ss.getShipmentItemSeqId());
                    ((AbstractShipmentPackageContentState) thisInnerState).merge(ss);
                }
            }
        }
        if (s.getShipmentPackageContents() != null) {
            if (s.getShipmentPackageContents() instanceof EntityStateCollection.RemovalLoggedEntityStateCollection) {
                if (((EntityStateCollection.RemovalLoggedEntityStateCollection)s.getShipmentPackageContents()).getRemovedStates() != null) {
                    for (ShipmentPackageContentState ss : ((EntityStateCollection.RemovalLoggedEntityStateCollection<String, ShipmentPackageContentState>)s.getShipmentPackageContents()).getRemovedStates()) {
                        ShipmentPackageContentState thisInnerState = ((EntityStateCollection.ModifiableEntityStateCollection<String, ShipmentPackageContentState>)this.getShipmentPackageContents()).getOrAddDefault(ss.getShipmentItemSeqId());
                        ((EntityStateCollection.ModifiableEntityStateCollection)this.getShipmentPackageContents()).removeState(thisInnerState);
                    }
                }
            } else {
                if (s.getShipmentPackageContents().isAllLoaded()) {
                    Set<String> removedStateIds = new HashSet<>(this.getShipmentPackageContents().stream().map(i -> i.getShipmentItemSeqId()).collect(java.util.stream.Collectors.toList()));
                    s.getShipmentPackageContents().forEach(i -> removedStateIds.remove(i.getShipmentItemSeqId()));
                    for (String i : removedStateIds) {
                        ShipmentPackageContentState thisInnerState = ((EntityStateCollection.ModifiableEntityStateCollection<String, ShipmentPackageContentState>)this.getShipmentPackageContents()).getOrAddDefault(i);
                        ((EntityStateCollection.ModifiableEntityStateCollection)this.getShipmentPackageContents()).removeState(thisInnerState);
                    }
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        }
    }

    public void when(ShipmentPackageStateMergePatched e) {
        throwOnWrongEvent(e);

        if (e.getShipmentBoxTypeId() == null) {
            if (e.getIsPropertyShipmentBoxTypeIdRemoved() != null && e.getIsPropertyShipmentBoxTypeIdRemoved()) {
                this.setShipmentBoxTypeId(null);
            }
        } else {
            this.setShipmentBoxTypeId(e.getShipmentBoxTypeId());
        }
        if (e.getDateCreated() == null) {
            if (e.getIsPropertyDateCreatedRemoved() != null && e.getIsPropertyDateCreatedRemoved()) {
                this.setDateCreated(null);
            }
        } else {
            this.setDateCreated(e.getDateCreated());
        }
        if (e.getBoxLength() == null) {
            if (e.getIsPropertyBoxLengthRemoved() != null && e.getIsPropertyBoxLengthRemoved()) {
                this.setBoxLength(null);
            }
        } else {
            this.setBoxLength(e.getBoxLength());
        }
        if (e.getBoxHeight() == null) {
            if (e.getIsPropertyBoxHeightRemoved() != null && e.getIsPropertyBoxHeightRemoved()) {
                this.setBoxHeight(null);
            }
        } else {
            this.setBoxHeight(e.getBoxHeight());
        }
        if (e.getBoxWidth() == null) {
            if (e.getIsPropertyBoxWidthRemoved() != null && e.getIsPropertyBoxWidthRemoved()) {
                this.setBoxWidth(null);
            }
        } else {
            this.setBoxWidth(e.getBoxWidth());
        }
        if (e.getDimensionUomId() == null) {
            if (e.getIsPropertyDimensionUomIdRemoved() != null && e.getIsPropertyDimensionUomIdRemoved()) {
                this.setDimensionUomId(null);
            }
        } else {
            this.setDimensionUomId(e.getDimensionUomId());
        }
        if (e.getWeight() == null) {
            if (e.getIsPropertyWeightRemoved() != null && e.getIsPropertyWeightRemoved()) {
                this.setWeight(null);
            }
        } else {
            this.setWeight(e.getWeight());
        }
        if (e.getWeightUomId() == null) {
            if (e.getIsPropertyWeightUomIdRemoved() != null && e.getIsPropertyWeightUomIdRemoved()) {
                this.setWeightUomId(null);
            }
        } else {
            this.setWeightUomId(e.getWeightUomId());
        }
        if (e.getInsuredValue() == null) {
            if (e.getIsPropertyInsuredValueRemoved() != null && e.getIsPropertyInsuredValueRemoved()) {
                this.setInsuredValue(null);
            }
        } else {
            this.setInsuredValue(e.getInsuredValue());
        }

        this.setUpdatedBy(e.getCreatedBy());
        this.setUpdatedAt(e.getCreatedAt());

        for (ShipmentPackageContentEvent innerEvent : e.getShipmentPackageContentEvents()) {
            ShipmentPackageContentState innerState = ((EntityStateCollection.ModifiableEntityStateCollection<String, ShipmentPackageContentState>)this.getShipmentPackageContents()).getOrAddDefault(((ShipmentPackageContentEvent.SqlShipmentPackageContentEvent)innerEvent).getShipmentPackageContentEventId().getShipmentItemSeqId());
            ((ShipmentPackageContentState.SqlShipmentPackageContentState)innerState).mutate(innerEvent);
            if (innerEvent instanceof ShipmentPackageContentEvent.ShipmentPackageContentStateRemoved) {
                //ShipmentPackageContentEvent.ShipmentPackageContentStateRemoved removed = (ShipmentPackageContentEvent.ShipmentPackageContentStateRemoved)innerEvent;
                ((EntityStateCollection.ModifiableEntityStateCollection)this.getShipmentPackageContents()).removeState(innerState);
            }
        }
    }

    public void when(ShipmentPackageStateRemoved e) {
        throwOnWrongEvent(e);

        this.setDeleted(true);
        this.setUpdatedBy(e.getCreatedBy());
        this.setUpdatedAt(e.getCreatedAt());

        for (ShipmentPackageContentState innerState : this.getShipmentPackageContents()) {
            ((EntityStateCollection.ModifiableEntityStateCollection)this.getShipmentPackageContents()).removeState(innerState);
        
            ShipmentPackageContentEvent.ShipmentPackageContentStateRemoved innerE = e.newShipmentPackageContentStateRemoved(innerState.getShipmentItemSeqId());
            innerE.setCreatedAt(e.getCreatedAt());
            innerE.setCreatedBy(e.getCreatedBy());
            ((ShipmentPackageContentState.MutableShipmentPackageContentState)innerState).mutate(innerE);
            //e.addShipmentPackageContentEvent(innerE);
        }
    }

    public void save() {
        if (shipmentPackageContents instanceof Saveable) {
            ((Saveable)shipmentPackageContents).save();
        }
    }

    protected void throwOnWrongEvent(ShipmentPackageEvent event) {
        String stateEntityIdShipmentId = this.getShipmentPackageId().getShipmentId();
        String eventEntityIdShipmentId = ((ShipmentPackageEvent.SqlShipmentPackageEvent)event).getShipmentPackageEventId().getShipmentId();
        if (!stateEntityIdShipmentId.equals(eventEntityIdShipmentId)) {
            throw DomainError.named("mutateWrongEntity", "Entity Id ShipmentId %1$s in state but entity id ShipmentId %2$s in event", stateEntityIdShipmentId, eventEntityIdShipmentId);
        }

        String stateEntityIdShipmentPackageSeqId = this.getShipmentPackageId().getShipmentPackageSeqId();
        String eventEntityIdShipmentPackageSeqId = ((ShipmentPackageEvent.SqlShipmentPackageEvent)event).getShipmentPackageEventId().getShipmentPackageSeqId();
        if (!stateEntityIdShipmentPackageSeqId.equals(eventEntityIdShipmentPackageSeqId)) {
            throw DomainError.named("mutateWrongEntity", "Entity Id ShipmentPackageSeqId %1$s in state but entity id ShipmentPackageSeqId %2$s in event", stateEntityIdShipmentPackageSeqId, eventEntityIdShipmentPackageSeqId);
        }


        if (getForReapplying()) { return; }
        ShipmentPackageStateEvent stateEvent = event instanceof ShipmentPackageStateEvent ? (ShipmentPackageStateEvent)event : null;
        if (stateEvent == null) { return; }

        Long stateVersion = this.getVersion();
        Long stateEventStateVersion = stateEvent.getVersion();
        //if (stateEventStateVersion == null) {
        stateEventStateVersion = stateVersion == null ? ShipmentPackageState.VERSION_NULL : stateVersion;
        stateEvent.setVersion(stateEventStateVersion);
        //}
        //if (!(stateVersion == null && stateEventStateVersion.equals(ShipmentPackageState.VERSION_NULL)) && !stateEventStateVersion.equals(stateVersion))
        //{
        //    throw DomainError.named("concurrencyConflict", "Conflict between stateVersion (%1$s) and stateEventStateVersion (%2$s)", stateVersion, stateEventStateVersion);
        //}

    }


    public static class SimpleShipmentPackageState extends AbstractShipmentPackageState {

        public SimpleShipmentPackageState() {
        }

        public static SimpleShipmentPackageState newForReapplying() {
            SimpleShipmentPackageState s = new SimpleShipmentPackageState();
            s.initializeForReapplying();
            return s;
        }

    }


    class SimpleShipmentPackageContentStateCollection implements EntityStateCollection.ModifiableEntityStateCollection<String, ShipmentPackageContentState>, Collection<ShipmentPackageContentState> {

        @Override
        public ShipmentPackageContentState get(String shipmentItemSeqId) {
            return protectedShipmentPackageContents.stream().filter(
                            e -> e.getShipmentItemSeqId().equals(shipmentItemSeqId))
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
        public Collection<ShipmentPackageContentState> getLoadedStates() {
            return protectedShipmentPackageContents;
        }

        @Override
        public ShipmentPackageContentState getOrAddDefault(String shipmentItemSeqId) {
            ShipmentPackageContentState s = get(shipmentItemSeqId);
            if (s == null) {
                ShipmentPackageContentId globalId = new ShipmentPackageContentId(getShipmentPackageId().getShipmentId(), getShipmentPackageId().getShipmentPackageSeqId(), shipmentItemSeqId);
                AbstractShipmentPackageContentState state = new AbstractShipmentPackageContentState.SimpleShipmentPackageContentState();
                state.setShipmentPackageContentId(globalId);
                add(state);
                s = state;
            }
            return s;
        }

        @Override
        public int size() {
            return protectedShipmentPackageContents.size();
        }

        @Override
        public boolean isEmpty() {
            return protectedShipmentPackageContents.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return protectedShipmentPackageContents.contains(o);
        }

        @Override
        public Iterator<ShipmentPackageContentState> iterator() {
            return protectedShipmentPackageContents.iterator();
        }

        @Override
        public java.util.stream.Stream<ShipmentPackageContentState> stream() {
            return protectedShipmentPackageContents.stream();
        }

        @Override
        public Object[] toArray() {
            return protectedShipmentPackageContents.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return protectedShipmentPackageContents.toArray(a);
        }

        @Override
        public boolean add(ShipmentPackageContentState s) {
            if (s instanceof AbstractShipmentPackageContentState) {
                AbstractShipmentPackageContentState state = (AbstractShipmentPackageContentState) s;
                state.setProtectedShipmentPackageState(AbstractShipmentPackageState.this);
            }
            return protectedShipmentPackageContents.add(s);
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof AbstractShipmentPackageContentState) {
                AbstractShipmentPackageContentState s = (AbstractShipmentPackageContentState) o;
                s.setProtectedShipmentPackageState(null);
            }
            return protectedShipmentPackageContents.remove(o);
        }

        @Override
        public boolean removeState(ShipmentPackageContentState s) {
            return remove(s);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return protectedShipmentPackageContents.contains(c);
        }

        @Override
        public boolean addAll(Collection<? extends ShipmentPackageContentState> c) {
            return protectedShipmentPackageContents.addAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return protectedShipmentPackageContents.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return protectedShipmentPackageContents.retainAll(c);
        }

        @Override
        public void clear() {
            protectedShipmentPackageContents.clear();
        }
    }


}
