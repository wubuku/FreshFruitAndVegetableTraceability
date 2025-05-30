// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.physicalinventory;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.AbstractEvent;

public abstract class AbstractPhysicalInventoryEvent extends AbstractEvent implements PhysicalInventoryEvent.SqlPhysicalInventoryEvent {
    private PhysicalInventoryEventId physicalInventoryEventId = new PhysicalInventoryEventId();

    public PhysicalInventoryEventId getPhysicalInventoryEventId() {
        return this.physicalInventoryEventId;
    }

    public void setPhysicalInventoryEventId(PhysicalInventoryEventId eventId) {
        this.physicalInventoryEventId = eventId;
    }
    
    public String getPhysicalInventoryId() {
        return getPhysicalInventoryEventId().getPhysicalInventoryId();
    }

    public void setPhysicalInventoryId(String physicalInventoryId) {
        getPhysicalInventoryEventId().setPhysicalInventoryId(physicalInventoryId);
    }

    private boolean eventReadOnly;

    public boolean getEventReadOnly() { return this.eventReadOnly; }

    public void setEventReadOnly(boolean readOnly) { this.eventReadOnly = readOnly; }

    public Long getVersion() {
        return getPhysicalInventoryEventId().getVersion();
    }
    
    public void setVersion(Long version) {
        getPhysicalInventoryEventId().setVersion(version);
    }

    private String tenantId;

    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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

    protected AbstractPhysicalInventoryEvent() {
    }

    protected AbstractPhysicalInventoryEvent(PhysicalInventoryEventId eventId) {
        this.physicalInventoryEventId = eventId;
    }

    protected InventoryItemVarianceEventDao getInventoryItemVarianceEventDao() {
        return (InventoryItemVarianceEventDao)ApplicationContext.current.get("inventoryItemVarianceEventDao");
    }

    protected InventoryItemVarianceEventId newInventoryItemVarianceEventId(String inventoryItemId)
    {
        InventoryItemVarianceEventId eventId = new InventoryItemVarianceEventId(this.getPhysicalInventoryEventId().getPhysicalInventoryId(), 
            inventoryItemId, 
            this.getPhysicalInventoryEventId().getVersion());
        return eventId;
    }

    protected void throwOnInconsistentEventIds(InventoryItemVarianceEvent.SqlInventoryItemVarianceEvent e)
    {
        throwOnInconsistentEventIds(this, e);
    }

    public static void throwOnInconsistentEventIds(PhysicalInventoryEvent.SqlPhysicalInventoryEvent oe, InventoryItemVarianceEvent.SqlInventoryItemVarianceEvent e)
    {
        if (!oe.getPhysicalInventoryEventId().getPhysicalInventoryId().equals(e.getInventoryItemVarianceEventId().getPhysicalInventoryId()))
        { 
            throw DomainError.named("inconsistentEventIds", "Outer Id PhysicalInventoryId %1$s but inner id PhysicalInventoryId %2$s", 
                oe.getPhysicalInventoryEventId().getPhysicalInventoryId(), e.getInventoryItemVarianceEventId().getPhysicalInventoryId());
        }
    }

    public InventoryItemVarianceEvent.InventoryItemVarianceStateCreated newInventoryItemVarianceStateCreated(String inventoryItemId) {
        return new AbstractInventoryItemVarianceEvent.SimpleInventoryItemVarianceStateCreated(newInventoryItemVarianceEventId(inventoryItemId));
    }

    public InventoryItemVarianceEvent.InventoryItemVarianceStateMergePatched newInventoryItemVarianceStateMergePatched(String inventoryItemId) {
        return new AbstractInventoryItemVarianceEvent.SimpleInventoryItemVarianceStateMergePatched(newInventoryItemVarianceEventId(inventoryItemId));
    }


    public abstract String getEventType();

    public static class PhysicalInventoryLobEvent extends AbstractPhysicalInventoryEvent {

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
            return "PhysicalInventoryLobEvent";
        }

    }


    public static abstract class AbstractPhysicalInventoryStateEvent extends AbstractPhysicalInventoryEvent implements PhysicalInventoryEvent.PhysicalInventoryStateEvent {
        private OffsetDateTime physicalInventoryDate;

        public OffsetDateTime getPhysicalInventoryDate()
        {
            return this.physicalInventoryDate;
        }

        public void setPhysicalInventoryDate(OffsetDateTime physicalInventoryDate)
        {
            this.physicalInventoryDate = physicalInventoryDate;
        }

        private String partyId;

        public String getPartyId()
        {
            return this.partyId;
        }

        public void setPartyId(String partyId)
        {
            this.partyId = partyId;
        }

        private String generalComments;

        public String getGeneralComments()
        {
            return this.generalComments;
        }

        public void setGeneralComments(String generalComments)
        {
            this.generalComments = generalComments;
        }

        protected AbstractPhysicalInventoryStateEvent(PhysicalInventoryEventId eventId) {
            super(eventId);
        }
    }

    public static abstract class AbstractPhysicalInventoryStateCreated extends AbstractPhysicalInventoryStateEvent implements PhysicalInventoryEvent.PhysicalInventoryStateCreated, Saveable
    {
        public AbstractPhysicalInventoryStateCreated() {
            this(new PhysicalInventoryEventId());
        }

        public AbstractPhysicalInventoryStateCreated(PhysicalInventoryEventId eventId) {
            super(eventId);
        }

        public String getEventType() {
            return StateEventType.CREATED;
        }

        private Map<InventoryItemVarianceEventId, InventoryItemVarianceEvent.InventoryItemVarianceStateCreated> inventoryItemVarianceEvents = new HashMap<InventoryItemVarianceEventId, InventoryItemVarianceEvent.InventoryItemVarianceStateCreated>();
        
        private Iterable<InventoryItemVarianceEvent.InventoryItemVarianceStateCreated> readOnlyInventoryItemVarianceEvents;

        public Iterable<InventoryItemVarianceEvent.InventoryItemVarianceStateCreated> getInventoryItemVarianceEvents()
        {
            if (!getEventReadOnly())
            {
                return this.inventoryItemVarianceEvents.values();
            }
            else
            {
                if (readOnlyInventoryItemVarianceEvents != null) { return readOnlyInventoryItemVarianceEvents; }
                InventoryItemVarianceEventDao eventDao = getInventoryItemVarianceEventDao();
                List<InventoryItemVarianceEvent.InventoryItemVarianceStateCreated> eL = new ArrayList<InventoryItemVarianceEvent.InventoryItemVarianceStateCreated>();
                for (InventoryItemVarianceEvent e : eventDao.findByPhysicalInventoryEventId(this.getPhysicalInventoryEventId()))
                {
                    ((InventoryItemVarianceEvent.SqlInventoryItemVarianceEvent)e).setEventReadOnly(true);
                    eL.add((InventoryItemVarianceEvent.InventoryItemVarianceStateCreated)e);
                }
                return (readOnlyInventoryItemVarianceEvents = eL);
            }
        }

        public void setInventoryItemVarianceEvents(Iterable<InventoryItemVarianceEvent.InventoryItemVarianceStateCreated> es)
        {
            if (es != null)
            {
                for (InventoryItemVarianceEvent.InventoryItemVarianceStateCreated e : es)
                {
                    addInventoryItemVarianceEvent(e);
                }
            }
            else { this.inventoryItemVarianceEvents.clear(); }
        }
        
        public void addInventoryItemVarianceEvent(InventoryItemVarianceEvent.InventoryItemVarianceStateCreated e)
        {
            throwOnInconsistentEventIds((InventoryItemVarianceEvent.SqlInventoryItemVarianceEvent)e);
            this.inventoryItemVarianceEvents.put(((InventoryItemVarianceEvent.SqlInventoryItemVarianceEvent)e).getInventoryItemVarianceEventId(), e);
        }

        public void save()
        {
            for (InventoryItemVarianceEvent.InventoryItemVarianceStateCreated e : this.getInventoryItemVarianceEvents()) {
                getInventoryItemVarianceEventDao().save(e);
            }
        }
    }


    public static abstract class AbstractPhysicalInventoryStateMergePatched extends AbstractPhysicalInventoryStateEvent implements PhysicalInventoryEvent.PhysicalInventoryStateMergePatched, Saveable
    {
        public AbstractPhysicalInventoryStateMergePatched() {
            this(new PhysicalInventoryEventId());
        }

        public AbstractPhysicalInventoryStateMergePatched(PhysicalInventoryEventId eventId) {
            super(eventId);
        }

        public String getEventType() {
            return StateEventType.MERGE_PATCHED;
        }

        private Boolean isPropertyPhysicalInventoryDateRemoved;

        public Boolean getIsPropertyPhysicalInventoryDateRemoved() {
            return this.isPropertyPhysicalInventoryDateRemoved;
        }

        public void setIsPropertyPhysicalInventoryDateRemoved(Boolean removed) {
            this.isPropertyPhysicalInventoryDateRemoved = removed;
        }

        private Boolean isPropertyPartyIdRemoved;

        public Boolean getIsPropertyPartyIdRemoved() {
            return this.isPropertyPartyIdRemoved;
        }

        public void setIsPropertyPartyIdRemoved(Boolean removed) {
            this.isPropertyPartyIdRemoved = removed;
        }

        private Boolean isPropertyGeneralCommentsRemoved;

        public Boolean getIsPropertyGeneralCommentsRemoved() {
            return this.isPropertyGeneralCommentsRemoved;
        }

        public void setIsPropertyGeneralCommentsRemoved(Boolean removed) {
            this.isPropertyGeneralCommentsRemoved = removed;
        }


        private Map<InventoryItemVarianceEventId, InventoryItemVarianceEvent> inventoryItemVarianceEvents = new HashMap<InventoryItemVarianceEventId, InventoryItemVarianceEvent>();
        
        private Iterable<InventoryItemVarianceEvent> readOnlyInventoryItemVarianceEvents;

        public Iterable<InventoryItemVarianceEvent> getInventoryItemVarianceEvents()
        {
            if (!getEventReadOnly())
            {
                return this.inventoryItemVarianceEvents.values();
            }
            else
            {
                if (readOnlyInventoryItemVarianceEvents != null) { return readOnlyInventoryItemVarianceEvents; }
                InventoryItemVarianceEventDao eventDao = getInventoryItemVarianceEventDao();
                List<InventoryItemVarianceEvent> eL = new ArrayList<InventoryItemVarianceEvent>();
                for (InventoryItemVarianceEvent e : eventDao.findByPhysicalInventoryEventId(this.getPhysicalInventoryEventId()))
                {
                    ((InventoryItemVarianceEvent.SqlInventoryItemVarianceEvent)e).setEventReadOnly(true);
                    eL.add((InventoryItemVarianceEvent)e);
                }
                return (readOnlyInventoryItemVarianceEvents = eL);
            }
        }

        public void setInventoryItemVarianceEvents(Iterable<InventoryItemVarianceEvent> es)
        {
            if (es != null)
            {
                for (InventoryItemVarianceEvent e : es)
                {
                    addInventoryItemVarianceEvent(e);
                }
            }
            else { this.inventoryItemVarianceEvents.clear(); }
        }
        
        public void addInventoryItemVarianceEvent(InventoryItemVarianceEvent e)
        {
            throwOnInconsistentEventIds((InventoryItemVarianceEvent.SqlInventoryItemVarianceEvent)e);
            this.inventoryItemVarianceEvents.put(((InventoryItemVarianceEvent.SqlInventoryItemVarianceEvent)e).getInventoryItemVarianceEventId(), e);
        }

        public void save()
        {
            for (InventoryItemVarianceEvent e : this.getInventoryItemVarianceEvents()) {
                getInventoryItemVarianceEventDao().save(e);
            }
        }
    }



    public static class SimplePhysicalInventoryStateCreated extends AbstractPhysicalInventoryStateCreated
    {
        public SimplePhysicalInventoryStateCreated() {
        }

        public SimplePhysicalInventoryStateCreated(PhysicalInventoryEventId eventId) {
            super(eventId);
        }
    }

    public static class SimplePhysicalInventoryStateMergePatched extends AbstractPhysicalInventoryStateMergePatched
    {
        public SimplePhysicalInventoryStateMergePatched() {
        }

        public SimplePhysicalInventoryStateMergePatched(PhysicalInventoryEventId eventId) {
            super(eventId);
        }
    }

}

