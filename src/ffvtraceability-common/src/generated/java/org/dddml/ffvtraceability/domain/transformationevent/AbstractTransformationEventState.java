// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.transformationevent;

import java.util.*;
import java.math.*;
import org.dddml.ffvtraceability.domain.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.specialization.*;

public abstract class AbstractTransformationEventState implements TransformationEventState.SqlTransformationEventState {

    private String eventId;

    public String getEventId() {
        return this.eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    private KdeTraceabilityLotCode foodUsedTlc;

    public KdeTraceabilityLotCode getFoodUsedTlc() {
        return this.foodUsedTlc;
    }

    public void setFoodUsedTlc(KdeTraceabilityLotCode foodUsedTlc) {
        this.foodUsedTlc = foodUsedTlc;
    }

    private KdeProductDescription foodUsedProductDescription;

    public KdeProductDescription getFoodUsedProductDescription() {
        return this.foodUsedProductDescription;
    }

    public void setFoodUsedProductDescription(KdeProductDescription foodUsedProductDescription) {
        this.foodUsedProductDescription = foodUsedProductDescription;
    }

    private KdeQuantityAndUom foodUsedQuantityAndUom;

    public KdeQuantityAndUom getFoodUsedQuantityAndUom() {
        return this.foodUsedQuantityAndUom;
    }

    public void setFoodUsedQuantityAndUom(KdeQuantityAndUom foodUsedQuantityAndUom) {
        this.foodUsedQuantityAndUom = foodUsedQuantityAndUom;
    }

    private KdeTraceabilityLotCode foodProducedNewTlc;

    public KdeTraceabilityLotCode getFoodProducedNewTlc() {
        return this.foodProducedNewTlc;
    }

    public void setFoodProducedNewTlc(KdeTraceabilityLotCode foodProducedNewTlc) {
        this.foodProducedNewTlc = foodProducedNewTlc;
    }

    private KdeProductDescription foodProducedProductDescription;

    public KdeProductDescription getFoodProducedProductDescription() {
        return this.foodProducedProductDescription;
    }

    public void setFoodProducedProductDescription(KdeProductDescription foodProducedProductDescription) {
        this.foodProducedProductDescription = foodProducedProductDescription;
    }

    private KdeQuantityAndUom foodProducedQuantityAndUom;

    public KdeQuantityAndUom getFoodProducedQuantityAndUom() {
        return this.foodProducedQuantityAndUom;
    }

    public void setFoodProducedQuantityAndUom(KdeQuantityAndUom foodProducedQuantityAndUom) {
        this.foodProducedQuantityAndUom = foodProducedQuantityAndUom;
    }

    private KdeLocationDescription transformationLocation;

    public KdeLocationDescription getTransformationLocation() {
        return this.transformationLocation;
    }

    public void setTransformationLocation(KdeLocationDescription transformationLocation) {
        this.transformationLocation = transformationLocation;
    }

    private String dateTransformed;

    public String getDateTransformed() {
        return this.dateTransformed;
    }

    public void setDateTransformed(String dateTransformed) {
        this.dateTransformed = dateTransformed;
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

    private Set<KdeReferenceDocument> referenceDocuments;

    public Set<KdeReferenceDocument> getReferenceDocuments() {
        return this.referenceDocuments;
    }

    public void setReferenceDocuments(Set<KdeReferenceDocument> referenceDocuments) {
        this.referenceDocuments = referenceDocuments;
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

    private String commandId;

    public String getCommandId() {
        return this.commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }


    public AbstractTransformationEventState() {
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
        return getEventId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj instanceof TransformationEventState) {
            return Objects.equals(this.getEventId(), ((TransformationEventState)obj).getEventId());
        }
        return false;
    }

    public void merge(TransformationEventState s) {
        if (s == this) {
            return;
        }
        this.setFoodUsedTlc(s.getFoodUsedTlc());
        this.setFoodUsedProductDescription(s.getFoodUsedProductDescription());
        this.setFoodUsedQuantityAndUom(s.getFoodUsedQuantityAndUom());
        this.setFoodProducedNewTlc(s.getFoodProducedNewTlc());
        this.setFoodProducedProductDescription(s.getFoodProducedProductDescription());
        this.setFoodProducedQuantityAndUom(s.getFoodProducedQuantityAndUom());
        this.setTransformationLocation(s.getTransformationLocation());
        this.setDateTransformed(s.getDateTransformed());
        this.setReferenceDocuments(s.getReferenceDocuments());
    }

    public void save() {
    }


    public static class SimpleTransformationEventState extends AbstractTransformationEventState {

        public SimpleTransformationEventState() {
        }

        public static SimpleTransformationEventState newForReapplying() {
            SimpleTransformationEventState s = new SimpleTransformationEventState();
            s.initializeForReapplying();
            return s;
        }

    }



}

