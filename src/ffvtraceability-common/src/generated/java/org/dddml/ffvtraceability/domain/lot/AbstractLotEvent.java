// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.lot;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.AbstractEvent;

public abstract class AbstractLotEvent extends AbstractEvent implements LotEvent.SqlLotEvent {
    private LotEventId lotEventId = new LotEventId();

    public LotEventId getLotEventId() {
        return this.lotEventId;
    }

    public void setLotEventId(LotEventId eventId) {
        this.lotEventId = eventId;
    }
    
    public String getLotId() {
        return getLotEventId().getLotId();
    }

    public void setLotId(String lotId) {
        getLotEventId().setLotId(lotId);
    }

    private boolean eventReadOnly;

    public boolean getEventReadOnly() { return this.eventReadOnly; }

    public void setEventReadOnly(boolean readOnly) { this.eventReadOnly = readOnly; }

    public Long getVersion() {
        return getLotEventId().getVersion();
    }
    
    public void setVersion(Long version) {
        getLotEventId().setVersion(version);
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

    protected AbstractLotEvent() {
    }

    protected AbstractLotEvent(LotEventId eventId) {
        this.lotEventId = eventId;
    }

    protected LotIdentificationEventDao getLotIdentificationEventDao() {
        return (LotIdentificationEventDao)ApplicationContext.current.get("lotIdentificationEventDao");
    }

    protected LotIdentificationEventId newLotIdentificationEventId(String lotIdentificationTypeId)
    {
        LotIdentificationEventId eventId = new LotIdentificationEventId(this.getLotEventId().getLotId(), 
            lotIdentificationTypeId, 
            this.getLotEventId().getVersion());
        return eventId;
    }

    protected void throwOnInconsistentEventIds(LotIdentificationEvent.SqlLotIdentificationEvent e)
    {
        throwOnInconsistentEventIds(this, e);
    }

    public static void throwOnInconsistentEventIds(LotEvent.SqlLotEvent oe, LotIdentificationEvent.SqlLotIdentificationEvent e)
    {
        if (!oe.getLotEventId().getLotId().equals(e.getLotIdentificationEventId().getLotId()))
        { 
            throw DomainError.named("inconsistentEventIds", "Outer Id LotId %1$s but inner id LotId %2$s", 
                oe.getLotEventId().getLotId(), e.getLotIdentificationEventId().getLotId());
        }
    }

    public LotIdentificationEvent.LotIdentificationStateCreated newLotIdentificationStateCreated(String lotIdentificationTypeId) {
        return new AbstractLotIdentificationEvent.SimpleLotIdentificationStateCreated(newLotIdentificationEventId(lotIdentificationTypeId));
    }

    public LotIdentificationEvent.LotIdentificationStateMergePatched newLotIdentificationStateMergePatched(String lotIdentificationTypeId) {
        return new AbstractLotIdentificationEvent.SimpleLotIdentificationStateMergePatched(newLotIdentificationEventId(lotIdentificationTypeId));
    }

    public LotIdentificationEvent.LotIdentificationStateRemoved newLotIdentificationStateRemoved(String lotIdentificationTypeId) {
        return new AbstractLotIdentificationEvent.SimpleLotIdentificationStateRemoved(newLotIdentificationEventId(lotIdentificationTypeId));
    }


    public abstract String getEventType();

    public static class LotLobEvent extends AbstractLotEvent {

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
            return "LotLobEvent";
        }

    }


    public static abstract class AbstractLotStateEvent extends AbstractLotEvent implements LotEvent.LotStateEvent {
        private String gs1Batch;

        public String getGs1Batch()
        {
            return this.gs1Batch;
        }

        public void setGs1Batch(String gs1Batch)
        {
            this.gs1Batch = gs1Batch;
        }

        private java.math.BigDecimal quantity;

        public java.math.BigDecimal getQuantity()
        {
            return this.quantity;
        }

        public void setQuantity(java.math.BigDecimal quantity)
        {
            this.quantity = quantity;
        }

        private OffsetDateTime expirationDate;

        public OffsetDateTime getExpirationDate()
        {
            return this.expirationDate;
        }

        public void setExpirationDate(OffsetDateTime expirationDate)
        {
            this.expirationDate = expirationDate;
        }

        protected AbstractLotStateEvent(LotEventId eventId) {
            super(eventId);
        }
    }

    public static abstract class AbstractLotStateCreated extends AbstractLotStateEvent implements LotEvent.LotStateCreated, Saveable
    {
        public AbstractLotStateCreated() {
            this(new LotEventId());
        }

        public AbstractLotStateCreated(LotEventId eventId) {
            super(eventId);
        }

        public String getEventType() {
            return StateEventType.CREATED;
        }

        private Map<LotIdentificationEventId, LotIdentificationEvent.LotIdentificationStateCreated> lotIdentificationEvents = new HashMap<LotIdentificationEventId, LotIdentificationEvent.LotIdentificationStateCreated>();
        
        private Iterable<LotIdentificationEvent.LotIdentificationStateCreated> readOnlyLotIdentificationEvents;

        public Iterable<LotIdentificationEvent.LotIdentificationStateCreated> getLotIdentificationEvents()
        {
            if (!getEventReadOnly())
            {
                return this.lotIdentificationEvents.values();
            }
            else
            {
                if (readOnlyLotIdentificationEvents != null) { return readOnlyLotIdentificationEvents; }
                LotIdentificationEventDao eventDao = getLotIdentificationEventDao();
                List<LotIdentificationEvent.LotIdentificationStateCreated> eL = new ArrayList<LotIdentificationEvent.LotIdentificationStateCreated>();
                for (LotIdentificationEvent e : eventDao.findByLotEventId(this.getLotEventId()))
                {
                    ((LotIdentificationEvent.SqlLotIdentificationEvent)e).setEventReadOnly(true);
                    eL.add((LotIdentificationEvent.LotIdentificationStateCreated)e);
                }
                return (readOnlyLotIdentificationEvents = eL);
            }
        }

        public void setLotIdentificationEvents(Iterable<LotIdentificationEvent.LotIdentificationStateCreated> es)
        {
            if (es != null)
            {
                for (LotIdentificationEvent.LotIdentificationStateCreated e : es)
                {
                    addLotIdentificationEvent(e);
                }
            }
            else { this.lotIdentificationEvents.clear(); }
        }
        
        public void addLotIdentificationEvent(LotIdentificationEvent.LotIdentificationStateCreated e)
        {
            throwOnInconsistentEventIds((LotIdentificationEvent.SqlLotIdentificationEvent)e);
            this.lotIdentificationEvents.put(((LotIdentificationEvent.SqlLotIdentificationEvent)e).getLotIdentificationEventId(), e);
        }

        public void save()
        {
            for (LotIdentificationEvent.LotIdentificationStateCreated e : this.getLotIdentificationEvents()) {
                getLotIdentificationEventDao().save(e);
            }
        }
    }


    public static abstract class AbstractLotStateMergePatched extends AbstractLotStateEvent implements LotEvent.LotStateMergePatched, Saveable
    {
        public AbstractLotStateMergePatched() {
            this(new LotEventId());
        }

        public AbstractLotStateMergePatched(LotEventId eventId) {
            super(eventId);
        }

        public String getEventType() {
            return StateEventType.MERGE_PATCHED;
        }

        private Boolean isPropertyGs1BatchRemoved;

        public Boolean getIsPropertyGs1BatchRemoved() {
            return this.isPropertyGs1BatchRemoved;
        }

        public void setIsPropertyGs1BatchRemoved(Boolean removed) {
            this.isPropertyGs1BatchRemoved = removed;
        }

        private Boolean isPropertyQuantityRemoved;

        public Boolean getIsPropertyQuantityRemoved() {
            return this.isPropertyQuantityRemoved;
        }

        public void setIsPropertyQuantityRemoved(Boolean removed) {
            this.isPropertyQuantityRemoved = removed;
        }

        private Boolean isPropertyExpirationDateRemoved;

        public Boolean getIsPropertyExpirationDateRemoved() {
            return this.isPropertyExpirationDateRemoved;
        }

        public void setIsPropertyExpirationDateRemoved(Boolean removed) {
            this.isPropertyExpirationDateRemoved = removed;
        }


        private Map<LotIdentificationEventId, LotIdentificationEvent> lotIdentificationEvents = new HashMap<LotIdentificationEventId, LotIdentificationEvent>();
        
        private Iterable<LotIdentificationEvent> readOnlyLotIdentificationEvents;

        public Iterable<LotIdentificationEvent> getLotIdentificationEvents()
        {
            if (!getEventReadOnly())
            {
                return this.lotIdentificationEvents.values();
            }
            else
            {
                if (readOnlyLotIdentificationEvents != null) { return readOnlyLotIdentificationEvents; }
                LotIdentificationEventDao eventDao = getLotIdentificationEventDao();
                List<LotIdentificationEvent> eL = new ArrayList<LotIdentificationEvent>();
                for (LotIdentificationEvent e : eventDao.findByLotEventId(this.getLotEventId()))
                {
                    ((LotIdentificationEvent.SqlLotIdentificationEvent)e).setEventReadOnly(true);
                    eL.add((LotIdentificationEvent)e);
                }
                return (readOnlyLotIdentificationEvents = eL);
            }
        }

        public void setLotIdentificationEvents(Iterable<LotIdentificationEvent> es)
        {
            if (es != null)
            {
                for (LotIdentificationEvent e : es)
                {
                    addLotIdentificationEvent(e);
                }
            }
            else { this.lotIdentificationEvents.clear(); }
        }
        
        public void addLotIdentificationEvent(LotIdentificationEvent e)
        {
            throwOnInconsistentEventIds((LotIdentificationEvent.SqlLotIdentificationEvent)e);
            this.lotIdentificationEvents.put(((LotIdentificationEvent.SqlLotIdentificationEvent)e).getLotIdentificationEventId(), e);
        }

        public void save()
        {
            for (LotIdentificationEvent e : this.getLotIdentificationEvents()) {
                getLotIdentificationEventDao().save(e);
            }
        }
    }


    public static abstract class AbstractLotStateDeleted extends AbstractLotStateEvent implements LotEvent.LotStateDeleted, Saveable
    {
        public AbstractLotStateDeleted() {
            this(new LotEventId());
        }

        public AbstractLotStateDeleted(LotEventId eventId) {
            super(eventId);
        }

        public String getEventType() {
            return StateEventType.DELETED;
        }

        
        private Map<LotIdentificationEventId, LotIdentificationEvent.LotIdentificationStateRemoved> lotIdentificationEvents = new HashMap<LotIdentificationEventId, LotIdentificationEvent.LotIdentificationStateRemoved>();
        
        private Iterable<LotIdentificationEvent.LotIdentificationStateRemoved> readOnlyLotIdentificationEvents;

        public Iterable<LotIdentificationEvent.LotIdentificationStateRemoved> getLotIdentificationEvents()
        {
            if (!getEventReadOnly())
            {
                return this.lotIdentificationEvents.values();
            }
            else
            {
                if (readOnlyLotIdentificationEvents != null) { return readOnlyLotIdentificationEvents; }
                LotIdentificationEventDao eventDao = getLotIdentificationEventDao();
                List<LotIdentificationEvent.LotIdentificationStateRemoved> eL = new ArrayList<LotIdentificationEvent.LotIdentificationStateRemoved>();
                for (LotIdentificationEvent e : eventDao.findByLotEventId(this.getLotEventId()))
                {
                    ((LotIdentificationEvent.SqlLotIdentificationEvent)e).setEventReadOnly(true);
                    eL.add((LotIdentificationEvent.LotIdentificationStateRemoved)e);
                }
                return (readOnlyLotIdentificationEvents = eL);
            }
        }

        public void setLotIdentificationEvents(Iterable<LotIdentificationEvent.LotIdentificationStateRemoved> es)
        {
            if (es != null)
            {
                for (LotIdentificationEvent.LotIdentificationStateRemoved e : es)
                {
                    addLotIdentificationEvent(e);
                }
            }
            else { this.lotIdentificationEvents.clear(); }
        }
        
        public void addLotIdentificationEvent(LotIdentificationEvent.LotIdentificationStateRemoved e)
        {
            throwOnInconsistentEventIds((LotIdentificationEvent.SqlLotIdentificationEvent)e);
            this.lotIdentificationEvents.put(((LotIdentificationEvent.SqlLotIdentificationEvent)e).getLotIdentificationEventId(), e);
        }

        public void save()
        {
            for (LotIdentificationEvent.LotIdentificationStateRemoved e : this.getLotIdentificationEvents()) {
                getLotIdentificationEventDao().save(e);
            }
        }
    }

    public static class SimpleLotStateCreated extends AbstractLotStateCreated
    {
        public SimpleLotStateCreated() {
        }

        public SimpleLotStateCreated(LotEventId eventId) {
            super(eventId);
        }
    }

    public static class SimpleLotStateMergePatched extends AbstractLotStateMergePatched
    {
        public SimpleLotStateMergePatched() {
        }

        public SimpleLotStateMergePatched(LotEventId eventId) {
            super(eventId);
        }
    }

    public static class SimpleLotStateDeleted extends AbstractLotStateDeleted
    {
        public SimpleLotStateDeleted() {
        }

        public SimpleLotStateDeleted(LotEventId eventId) {
            super(eventId);
        }
    }

}
