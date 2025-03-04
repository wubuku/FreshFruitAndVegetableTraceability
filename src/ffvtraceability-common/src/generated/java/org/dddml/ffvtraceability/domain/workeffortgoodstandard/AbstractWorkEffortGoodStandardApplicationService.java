// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.workeffortgoodstandard;

import java.util.*;
import java.util.function.Consumer;
import org.dddml.support.criterion.Criterion;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;

public abstract class AbstractWorkEffortGoodStandardApplicationService implements WorkEffortGoodStandardApplicationService {

    private EventStore eventStore;

    protected EventStore getEventStore()
    {
        return eventStore;
    }

    private WorkEffortGoodStandardStateRepository stateRepository;

    protected WorkEffortGoodStandardStateRepository getStateRepository() {
        return stateRepository;
    }

    private WorkEffortGoodStandardStateQueryRepository stateQueryRepository;

    protected WorkEffortGoodStandardStateQueryRepository getStateQueryRepository() {
        return stateQueryRepository;
    }

    private AggregateEventListener<WorkEffortGoodStandardAggregate, WorkEffortGoodStandardState> aggregateEventListener;

    public AggregateEventListener<WorkEffortGoodStandardAggregate, WorkEffortGoodStandardState> getAggregateEventListener() {
        return aggregateEventListener;
    }

    public void setAggregateEventListener(AggregateEventListener<WorkEffortGoodStandardAggregate, WorkEffortGoodStandardState> eventListener) {
        this.aggregateEventListener = eventListener;
    }

    public AbstractWorkEffortGoodStandardApplicationService(EventStore eventStore, WorkEffortGoodStandardStateRepository stateRepository, WorkEffortGoodStandardStateQueryRepository stateQueryRepository) {
        this.eventStore = eventStore;
        this.stateRepository = stateRepository;
        this.stateQueryRepository = stateQueryRepository;
    }

    public void when(WorkEffortGoodStandardCommand.CreateWorkEffortGoodStandard c) {
        update(c, ar -> ar.create(c));
    }

    public void when(WorkEffortGoodStandardCommand.MergePatchWorkEffortGoodStandard c) {
        update(c, ar -> ar.mergePatch(c));
    }

    public WorkEffortGoodStandardState get(WorkEffortGoodStandardId id) {
        WorkEffortGoodStandardState state = getStateRepository().get(id, true);
        return state;
    }

    public Iterable<WorkEffortGoodStandardState> getAll(Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().getAll(firstResult, maxResults);
    }

    public Iterable<WorkEffortGoodStandardState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().get(filter, orders, firstResult, maxResults);
    }

    public Iterable<WorkEffortGoodStandardState> get(Criterion filter, List<String> orders, Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().get(filter, orders, firstResult, maxResults);
    }

    public Iterable<WorkEffortGoodStandardState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().getByProperty(propertyName, propertyValue, orders, firstResult, maxResults);
    }

    public long getCount(Iterable<Map.Entry<String, Object>> filter) {
        return getStateQueryRepository().getCount(filter);
    }

    public long getCount(Criterion filter) {
        return getStateQueryRepository().getCount(filter);
    }

    public WorkEffortGoodStandardEvent getEvent(WorkEffortGoodStandardId workEffortGoodStandardId, long version) {
        WorkEffortGoodStandardEvent e = (WorkEffortGoodStandardEvent)getEventStore().getEvent(toEventStoreAggregateId(workEffortGoodStandardId), version);
        if (e != null) {
            ((WorkEffortGoodStandardEvent.SqlWorkEffortGoodStandardEvent)e).setEventReadOnly(true); 
        } else if (version == -1) {
            return getEvent(workEffortGoodStandardId, 0);
        }
        return e;
    }

    public WorkEffortGoodStandardState getHistoryState(WorkEffortGoodStandardId workEffortGoodStandardId, long version) {
        EventStream eventStream = getEventStore().loadEventStream(AbstractWorkEffortGoodStandardEvent.class, toEventStoreAggregateId(workEffortGoodStandardId), version - 1);
        return new AbstractWorkEffortGoodStandardState.SimpleWorkEffortGoodStandardState(eventStream.getEvents());
    }


    public WorkEffortGoodStandardAggregate getWorkEffortGoodStandardAggregate(WorkEffortGoodStandardState state) {
        return new AbstractWorkEffortGoodStandardAggregate.SimpleWorkEffortGoodStandardAggregate(state);
    }

    public EventStoreAggregateId toEventStoreAggregateId(WorkEffortGoodStandardId aggregateId) {
        return new EventStoreAggregateId.SimpleEventStoreAggregateId(aggregateId);
    }

    protected void update(WorkEffortGoodStandardCommand c, Consumer<WorkEffortGoodStandardAggregate> action) {
        WorkEffortGoodStandardId aggregateId = c.getWorkEffortGoodStandardId();
        EventStoreAggregateId eventStoreAggregateId = toEventStoreAggregateId(aggregateId);
        WorkEffortGoodStandardState state = getStateRepository().get(aggregateId, false);
        boolean duplicate = isDuplicateCommand(c, eventStoreAggregateId, state);
        if (duplicate) { return; }

        WorkEffortGoodStandardAggregate aggregate = getWorkEffortGoodStandardAggregate(state);
        aggregate.throwOnInvalidStateTransition(c);
        action.accept(aggregate);
        persist(eventStoreAggregateId, c.getVersion() == null ? WorkEffortGoodStandardState.VERSION_NULL : c.getVersion(), aggregate, state); // State version may be null!

    }

    private DomainEventPublisher domainEventPublisher;

    public void setDomainEventPublisher(DomainEventPublisher domainEventPublisher) {
        this.domainEventPublisher = domainEventPublisher;
    }

    public DomainEventPublisher getDomainEventPublisher() {
        if (domainEventPublisher != null) { return domainEventPublisher; }
        return ApplicationContext.current.get(DomainEventPublisher.class);
    }

    private void persist(EventStoreAggregateId eventStoreAggregateId, long version, WorkEffortGoodStandardAggregate aggregate, WorkEffortGoodStandardState state) {
        final DomainEventPublisher ep = getDomainEventPublisher();
        getEventStore().appendEvents(eventStoreAggregateId, version, 
            aggregate.getChanges(), (events) -> { 
                getStateRepository().save(state); 
                if (ep != null) {
                    ep.publish(org.dddml.ffvtraceability.domain.workeffortgoodstandard.WorkEffortGoodStandardAggregate.class,
                        eventStoreAggregateId.getId(),
                        (List<Event>)events);
                }
            });
        if (aggregateEventListener != null) {
            aggregateEventListener.eventAppended(new AggregateEvent<>(aggregate, state, aggregate.getChanges()));
        }
    }

    void initialize(WorkEffortGoodStandardEvent.WorkEffortGoodStandardStateCreated stateCreated) {
        WorkEffortGoodStandardId aggregateId = ((WorkEffortGoodStandardEvent.SqlWorkEffortGoodStandardEvent)stateCreated).getWorkEffortGoodStandardEventId().getWorkEffortGoodStandardId();
        WorkEffortGoodStandardState.SqlWorkEffortGoodStandardState state = new AbstractWorkEffortGoodStandardState.SimpleWorkEffortGoodStandardState();
        state.setWorkEffortGoodStandardId(aggregateId);

        WorkEffortGoodStandardAggregate aggregate = getWorkEffortGoodStandardAggregate(state);
        ((AbstractWorkEffortGoodStandardAggregate) aggregate).apply(stateCreated);

        EventStoreAggregateId eventStoreAggregateId = toEventStoreAggregateId(aggregateId);
        persist(eventStoreAggregateId, ((WorkEffortGoodStandardEvent.SqlWorkEffortGoodStandardEvent)stateCreated).getWorkEffortGoodStandardEventId().getVersion(), aggregate, state);
    }

    protected boolean isDuplicateCommand(WorkEffortGoodStandardCommand command, EventStoreAggregateId eventStoreAggregateId, WorkEffortGoodStandardState state) {
        boolean duplicate = false;
        if (command.getVersion() == null) { command.setVersion(WorkEffortGoodStandardState.VERSION_NULL); }
        if (state.getVersion() != null && state.getVersion() > command.getVersion()) {
            Event lastEvent = getEventStore().getEvent(AbstractWorkEffortGoodStandardEvent.class, eventStoreAggregateId, command.getVersion());
            if (lastEvent != null && lastEvent instanceof AbstractEvent
               && command.getCommandId() != null && command.getCommandId().equals(((AbstractEvent) lastEvent).getCommandId())) {
                duplicate = true;
            }
        }
        return duplicate;
    }

    public static class SimpleWorkEffortGoodStandardApplicationService extends AbstractWorkEffortGoodStandardApplicationService {
        public SimpleWorkEffortGoodStandardApplicationService(EventStore eventStore, WorkEffortGoodStandardStateRepository stateRepository, WorkEffortGoodStandardStateQueryRepository stateQueryRepository)
        {
            super(eventStore, stateRepository, stateQueryRepository);
        }
    }

}

