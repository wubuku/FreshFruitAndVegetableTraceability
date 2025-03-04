// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.product;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.product.GoodIdentificationEvent.*;

public abstract class AbstractGoodIdentificationState implements GoodIdentificationState.SqlGoodIdentificationState {

    private GoodIdentificationId goodIdentificationId = new GoodIdentificationId();

    public GoodIdentificationId getGoodIdentificationId() {
        return this.goodIdentificationId;
    }

    public void setGoodIdentificationId(GoodIdentificationId goodIdentificationId) {
        this.goodIdentificationId = goodIdentificationId;
    }

    private transient ProductState productState;

    public ProductState getProductState() {
        return productState;
    }

    public void setProductState(ProductState s) {
        productState = s;
    }
    
    private ProductState protectedProductState;

    protected ProductState getProtectedProductState() {
        return protectedProductState;
    }

    protected void setProtectedProductState(ProductState protectedProductState) {
        this.protectedProductState = protectedProductState;
    }

    public String getProductId() {
        return this.getGoodIdentificationId().getProductId();
    }
        
    public void setProductId(String productId) {
        this.getGoodIdentificationId().setProductId(productId);
    }

    public String getGoodIdentificationTypeId() {
        return this.getGoodIdentificationId().getGoodIdentificationTypeId();
    }
        
    public void setGoodIdentificationTypeId(String goodIdentificationTypeId) {
        this.getGoodIdentificationId().setGoodIdentificationTypeId(goodIdentificationTypeId);
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

    private Boolean __Deleted__;

    public Boolean get__Deleted__() {
        return this.__Deleted__;
    }

    public void set__Deleted__(Boolean __Deleted__) {
        this.__Deleted__ = __Deleted__;
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


    public AbstractGoodIdentificationState() {
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
        return getGoodIdentificationTypeId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj instanceof GoodIdentificationState) {
            return Objects.equals(this.getGoodIdentificationTypeId(), ((GoodIdentificationState)obj).getGoodIdentificationTypeId());
        }
        return false;
    }


    public void mutate(Event e) {
        setStateReadOnly(false);
        if (e instanceof GoodIdentificationStateCreated) {
            when((GoodIdentificationStateCreated) e);
        } else if (e instanceof GoodIdentificationStateMergePatched) {
            when((GoodIdentificationStateMergePatched) e);
        } else if (e instanceof GoodIdentificationStateRemoved) {
            when((GoodIdentificationStateRemoved) e);
        } else {
            throw new UnsupportedOperationException(String.format("Unsupported event type: %1$s", e.getClass().getName()));
        }
    }

    public void when(GoodIdentificationStateCreated e) {
        throwOnWrongEvent(e);

        this.setIdValue(e.getIdValue());

        this.set__Deleted__(false);

        this.setCreatedBy(e.getCreatedBy());
        this.setCreatedAt(e.getCreatedAt());

    }

    public void merge(GoodIdentificationState s) {
        if (s == this) {
            return;
        }
        this.setIdValue(s.getIdValue());
    }

    public void when(GoodIdentificationStateMergePatched e) {
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

    public void when(GoodIdentificationStateRemoved e) {
        throwOnWrongEvent(e);

        this.set__Deleted__(true);
        this.setUpdatedBy(e.getCreatedBy());
        this.setUpdatedAt(e.getCreatedAt());

    }

    public void save() {
    }

    protected void throwOnWrongEvent(GoodIdentificationEvent event) {
        String stateEntityIdProductId = this.getGoodIdentificationId().getProductId();
        String eventEntityIdProductId = ((GoodIdentificationEvent.SqlGoodIdentificationEvent)event).getGoodIdentificationEventId().getProductId();
        if (!stateEntityIdProductId.equals(eventEntityIdProductId)) {
            throw DomainError.named("mutateWrongEntity", "Entity Id ProductId %1$s in state but entity id ProductId %2$s in event", stateEntityIdProductId, eventEntityIdProductId);
        }

        String stateEntityIdGoodIdentificationTypeId = this.getGoodIdentificationId().getGoodIdentificationTypeId();
        String eventEntityIdGoodIdentificationTypeId = ((GoodIdentificationEvent.SqlGoodIdentificationEvent)event).getGoodIdentificationEventId().getGoodIdentificationTypeId();
        if (!stateEntityIdGoodIdentificationTypeId.equals(eventEntityIdGoodIdentificationTypeId)) {
            throw DomainError.named("mutateWrongEntity", "Entity Id GoodIdentificationTypeId %1$s in state but entity id GoodIdentificationTypeId %2$s in event", stateEntityIdGoodIdentificationTypeId, eventEntityIdGoodIdentificationTypeId);
        }


        if (getForReapplying()) { return; }
        GoodIdentificationStateEvent stateEvent = event instanceof GoodIdentificationStateEvent ? (GoodIdentificationStateEvent)event : null;
        if (stateEvent == null) { return; }

        Long stateVersion = this.getVersion();
        Long stateEventStateVersion = stateEvent.getVersion();
        //if (stateEventStateVersion == null) {
        stateEventStateVersion = stateVersion == null ? GoodIdentificationState.VERSION_NULL : stateVersion;
        stateEvent.setVersion(stateEventStateVersion);
        //}
        //if (!(stateVersion == null && stateEventStateVersion.equals(GoodIdentificationState.VERSION_NULL)) && !stateEventStateVersion.equals(stateVersion))
        //{
        //    throw DomainError.named("concurrencyConflict", "Conflict between stateVersion (%1$s) and stateEventStateVersion (%2$s)", stateVersion, stateEventStateVersion);
        //}

    }


    public static class SimpleGoodIdentificationState extends AbstractGoodIdentificationState {

        public SimpleGoodIdentificationState() {
        }

        public static SimpleGoodIdentificationState newForReapplying() {
            SimpleGoodIdentificationState s = new SimpleGoodIdentificationState();
            s.initializeForReapplying();
            return s;
        }

    }



}

