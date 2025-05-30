// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.enumerationtype;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;

public abstract class AbstractEnumerationTypeState implements EnumerationTypeState.SqlEnumerationTypeState {

    private String enumTypeId;

    public String getEnumTypeId() {
        return this.enumTypeId;
    }

    public void setEnumTypeId(String enumTypeId) {
        this.enumTypeId = enumTypeId;
    }

    private String parentTypeId;

    public String getParentTypeId() {
        return this.parentTypeId;
    }

    public void setParentTypeId(String parentTypeId) {
        this.parentTypeId = parentTypeId;
    }

    private String hasTable;

    public String getHasTable() {
        return this.hasTable;
    }

    public void setHasTable(String hasTable) {
        this.hasTable = hasTable;
    }

    private String description;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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


    public AbstractEnumerationTypeState() {
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
        return getEnumTypeId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj instanceof EnumerationTypeState) {
            return Objects.equals(this.getEnumTypeId(), ((EnumerationTypeState)obj).getEnumTypeId());
        }
        return false;
    }

    public void merge(EnumerationTypeState s) {
        if (s == this) {
            return;
        }
        this.setParentTypeId(s.getParentTypeId());
        this.setHasTable(s.getHasTable());
        this.setDescription(s.getDescription());
    }

    public void save() {
    }


    public static class SimpleEnumerationTypeState extends AbstractEnumerationTypeState {

        public SimpleEnumerationTypeState() {
        }

        public static SimpleEnumerationTypeState newForReapplying() {
            SimpleEnumerationTypeState s = new SimpleEnumerationTypeState();
            s.initializeForReapplying();
            return s;
        }

    }



}

