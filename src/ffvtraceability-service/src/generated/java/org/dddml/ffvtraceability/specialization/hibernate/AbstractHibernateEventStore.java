package org.dddml.ffvtraceability.specialization.hibernate;

import org.dddml.ffvtraceability.specialization.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * Created by Yang on 2024/10/27.
 */
public abstract class AbstractHibernateEventStore implements EventStore {
    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Transactional(readOnly = true)
    @Override
    public EventStream loadEventStream(EventStoreAggregateId aggregateId) {
        throw new UnsupportedOperationException();
    }

    @Transactional
    @Override
    public void appendEvents(EventStoreAggregateId aggregateId, long version, Collection<Event> events, Consumer<Collection<Event>> afterEventsAppended) {
        for (Event e : events) {
            getEntityManager().persist(e);
            if (e instanceof Saveable) {
                Saveable saveable = (Saveable) e;
                saveable.save();
            }
        }
        afterEventsAppended.accept(events);
    }

    @Transactional(readOnly = true)
    @Override
    public Event getEvent(Class eventType, EventStoreAggregateId eventStoreAggregateId, long version) {
        Class supportedEventType = getSupportedEventType();
        if (!eventType.isAssignableFrom(supportedEventType)) {
            throw new UnsupportedOperationException();
        }
        Serializable eventId = getEventId(eventStoreAggregateId, version);
        return (Event) getEntityManager().find(eventType, eventId);
    }

    @Transactional(readOnly = true)
    @Override
    public Event getEvent(EventStoreAggregateId eventStoreAggregateId, long version) {
        Serializable eventId = getEventId(eventStoreAggregateId, version);
        return (Event) getEntityManager().find(getSupportedEventType(), eventId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isEventWithCommandIdExisted(Class eventType, EventStoreAggregateId eventStoreAggregateId, String commandId) {
        throw new UnsupportedOperationException();
    }

    protected abstract Class getSupportedEventType();

    protected abstract Serializable getEventId(EventStoreAggregateId eventStoreAggregateId, long version);
}
