// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.lot;

import java.util.*;
import java.util.function.Consumer;
import org.dddml.support.criterion.Criterion;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;

public abstract class AbstractLotApplicationService implements LotApplicationService {

    private EventStore eventStore;

    protected EventStore getEventStore()
    {
        return eventStore;
    }

    private LotStateRepository stateRepository;

    protected LotStateRepository getStateRepository() {
        return stateRepository;
    }

    private LotStateQueryRepository stateQueryRepository;

    protected LotStateQueryRepository getStateQueryRepository() {
        return stateQueryRepository;
    }

    private AggregateEventListener<LotAggregate, LotState> aggregateEventListener;

    public AggregateEventListener<LotAggregate, LotState> getAggregateEventListener() {
        return aggregateEventListener;
    }

    public void setAggregateEventListener(AggregateEventListener<LotAggregate, LotState> eventListener) {
        this.aggregateEventListener = eventListener;
    }

    public AbstractLotApplicationService(EventStore eventStore, LotStateRepository stateRepository, LotStateQueryRepository stateQueryRepository) {
        this.eventStore = eventStore;
        this.stateRepository = stateRepository;
        this.stateQueryRepository = stateQueryRepository;
    }

    public void when(LotCommand.CreateLot c) {
        update(c, ar -> ar.create(c));
    }

    public void when(LotCommand.MergePatchLot c) {
        update(c, ar -> ar.mergePatch(c));
    }

    public LotState get(String id) {
        LotState state = getStateRepository().get(id, true);
        return state;
    }

    public Iterable<LotState> getAll(Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().getAll(firstResult, maxResults);
    }

    public Iterable<LotState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().get(filter, orders, firstResult, maxResults);
    }

    public Iterable<LotState> get(Criterion filter, List<String> orders, Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().get(filter, orders, firstResult, maxResults);
    }

    public Iterable<LotState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().getByProperty(propertyName, propertyValue, orders, firstResult, maxResults);
    }

    public long getCount(Iterable<Map.Entry<String, Object>> filter) {
        return getStateQueryRepository().getCount(filter);
    }

    public long getCount(Criterion filter) {
        return getStateQueryRepository().getCount(filter);
    }

    public LotEvent getEvent(String lotId, long version) {
        LotEvent e = (LotEvent)getEventStore().getEvent(toEventStoreAggregateId(lotId), version);
        if (e != null) {
            ((LotEvent.SqlLotEvent)e).setEventReadOnly(true); 
        } else if (version == -1) {
            return getEvent(lotId, 0);
        }
        return e;
    }

    public LotState getHistoryState(String lotId, long version) {
        EventStream eventStream = getEventStore().loadEventStream(AbstractLotEvent.class, toEventStoreAggregateId(lotId), version - 1);
        return new AbstractLotState.SimpleLotState(eventStream.getEvents());
    }

    public LotIdentificationState getLotIdentification(String lotId, String lotIdentificationTypeId) {
        return getStateQueryRepository().getLotIdentification(lotId, lotIdentificationTypeId);
    }

    public Iterable<LotIdentificationState> getLotIdentifications(String lotId, Criterion filter, List<String> orders) {
        return getStateQueryRepository().getLotIdentifications(lotId, filter, orders);
    }


    public LotAggregate getLotAggregate(LotState state) {
        return new AbstractLotAggregate.SimpleLotAggregate(state);
    }

    public EventStoreAggregateId toEventStoreAggregateId(String aggregateId) {
        return new EventStoreAggregateId.SimpleEventStoreAggregateId(aggregateId);
    }

    protected void update(LotCommand c, Consumer<LotAggregate> action) {
        String aggregateId = c.getLotId();
        EventStoreAggregateId eventStoreAggregateId = toEventStoreAggregateId(aggregateId);
        LotState state = getStateRepository().get(aggregateId, false);
        boolean duplicate = isDuplicateCommand(c, eventStoreAggregateId, state);
        if (duplicate) { return; }

        LotAggregate aggregate = getLotAggregate(state);
        aggregate.throwOnInvalidStateTransition(c);
        action.accept(aggregate);
        persist(eventStoreAggregateId, c.getVersion() == null ? LotState.VERSION_NULL : c.getVersion(), aggregate, state); // State version may be null!

    }

    private DomainEventPublisher domainEventPublisher;

    public void setDomainEventPublisher(DomainEventPublisher domainEventPublisher) {
        this.domainEventPublisher = domainEventPublisher;
    }

    public DomainEventPublisher getDomainEventPublisher() {
        if (domainEventPublisher != null) { return domainEventPublisher; }
        return ApplicationContext.current.get(DomainEventPublisher.class);
    }

    private void persist(EventStoreAggregateId eventStoreAggregateId, long version, LotAggregate aggregate, LotState state) {
        final DomainEventPublisher ep = getDomainEventPublisher();
        getEventStore().appendEvents(eventStoreAggregateId, version, 
            aggregate.getChanges(), (events) -> { 
                getStateRepository().save(state); 
                if (ep != null) {
                    ep.publish(org.dddml.ffvtraceability.domain.lot.LotAggregate.class,
                        eventStoreAggregateId.getId(),
                        (List<Event>)events);
                }
            });
        if (aggregateEventListener != null) {
            aggregateEventListener.eventAppended(new AggregateEvent<>(aggregate, state, aggregate.getChanges()));
        }
    }

    void initialize(LotEvent.LotStateCreated stateCreated) {
        String aggregateId = ((LotEvent.SqlLotEvent)stateCreated).getLotEventId().getLotId();
        LotState.SqlLotState state = new AbstractLotState.SimpleLotState();
        state.setLotId(aggregateId);

        LotAggregate aggregate = getLotAggregate(state);
        ((AbstractLotAggregate) aggregate).apply(stateCreated);

        EventStoreAggregateId eventStoreAggregateId = toEventStoreAggregateId(aggregateId);
        persist(eventStoreAggregateId, ((LotEvent.SqlLotEvent)stateCreated).getLotEventId().getVersion(), aggregate, state);
    }

    protected boolean isDuplicateCommand(LotCommand command, EventStoreAggregateId eventStoreAggregateId, LotState state) {
        boolean duplicate = false;
        if (command.getVersion() == null) { command.setVersion(LotState.VERSION_NULL); }
        if (state.getVersion() != null && state.getVersion() > command.getVersion()) {
            Event lastEvent = getEventStore().getEvent(AbstractLotEvent.class, eventStoreAggregateId, command.getVersion());
            if (lastEvent != null && lastEvent instanceof AbstractEvent
               && command.getCommandId() != null && command.getCommandId().equals(((AbstractEvent) lastEvent).getCommandId())) {
                duplicate = true;
            }
        }
        return duplicate;
    }

    public static class SimpleLotApplicationService extends AbstractLotApplicationService {
        public SimpleLotApplicationService(EventStore eventStore, LotStateRepository stateRepository, LotStateQueryRepository stateQueryRepository)
        {
            super(eventStore, stateRepository, stateQueryRepository);
        }
    }

}

