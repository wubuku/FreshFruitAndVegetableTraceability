// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.shipment;

import java.util.*;
import java.util.function.Consumer;
import org.dddml.support.criterion.Criterion;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;

public abstract class AbstractShipmentApplicationService implements ShipmentApplicationService {

    private EventStore eventStore;

    protected EventStore getEventStore()
    {
        return eventStore;
    }

    private ShipmentStateRepository stateRepository;

    protected ShipmentStateRepository getStateRepository() {
        return stateRepository;
    }

    private ShipmentStateQueryRepository stateQueryRepository;

    protected ShipmentStateQueryRepository getStateQueryRepository() {
        return stateQueryRepository;
    }

    private AggregateEventListener<ShipmentAggregate, ShipmentState> aggregateEventListener;

    public AggregateEventListener<ShipmentAggregate, ShipmentState> getAggregateEventListener() {
        return aggregateEventListener;
    }

    public void setAggregateEventListener(AggregateEventListener<ShipmentAggregate, ShipmentState> eventListener) {
        this.aggregateEventListener = eventListener;
    }

    public AbstractShipmentApplicationService(EventStore eventStore, ShipmentStateRepository stateRepository, ShipmentStateQueryRepository stateQueryRepository) {
        this.eventStore = eventStore;
        this.stateRepository = stateRepository;
        this.stateQueryRepository = stateQueryRepository;
    }

    public void when(ShipmentCommand.CreateShipment c) {
        update(c, ar -> ar.create(c));
    }

    public void when(ShipmentCommand.MergePatchShipment c) {
        update(c, ar -> ar.mergePatch(c));
    }

    public void when(ShipmentCommand.DeleteShipment c) {
        update(c, ar -> ar.delete(c));
    }

    public ShipmentState get(String id) {
        ShipmentState state = getStateRepository().get(id, true);
        return state;
    }

    public Iterable<ShipmentState> getAll(Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().getAll(firstResult, maxResults);
    }

    public Iterable<ShipmentState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().get(filter, orders, firstResult, maxResults);
    }

    public Iterable<ShipmentState> get(Criterion filter, List<String> orders, Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().get(filter, orders, firstResult, maxResults);
    }

    public Iterable<ShipmentState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().getByProperty(propertyName, propertyValue, orders, firstResult, maxResults);
    }

    public long getCount(Iterable<Map.Entry<String, Object>> filter) {
        return getStateQueryRepository().getCount(filter);
    }

    public long getCount(Criterion filter) {
        return getStateQueryRepository().getCount(filter);
    }

    public ShipmentEvent getEvent(String shipmentId, long version) {
        ShipmentEvent e = (ShipmentEvent)getEventStore().getEvent(toEventStoreAggregateId(shipmentId), version);
        if (e != null) {
            ((ShipmentEvent.SqlShipmentEvent)e).setEventReadOnly(true); 
        } else if (version == -1) {
            return getEvent(shipmentId, 0);
        }
        return e;
    }

    public ShipmentState getHistoryState(String shipmentId, long version) {
        EventStream eventStream = getEventStore().loadEventStream(AbstractShipmentEvent.class, toEventStoreAggregateId(shipmentId), version - 1);
        return new AbstractShipmentState.SimpleShipmentState(eventStream.getEvents());
    }

    public ShipmentItemState getShipmentItem(String shipmentId, String shipmentItemSeqId) {
        return getStateQueryRepository().getShipmentItem(shipmentId, shipmentItemSeqId);
    }

    public Iterable<ShipmentItemState> getShipmentItems(String shipmentId, Criterion filter, List<String> orders) {
        return getStateQueryRepository().getShipmentItems(shipmentId, filter, orders);
    }

    public ShipmentPackageState getShipmentPackage(String shipmentId, String shipmentPackageSeqId) {
        return getStateQueryRepository().getShipmentPackage(shipmentId, shipmentPackageSeqId);
    }

    public Iterable<ShipmentPackageState> getShipmentPackages(String shipmentId, Criterion filter, List<String> orders) {
        return getStateQueryRepository().getShipmentPackages(shipmentId, filter, orders);
    }

    public ShipmentPackageContentState getShipmentPackageContent(String shipmentId, String shipmentPackageSeqId, String shipmentItemSeqId) {
        return getStateQueryRepository().getShipmentPackageContent(shipmentId, shipmentPackageSeqId, shipmentItemSeqId);
    }

    public Iterable<ShipmentPackageContentState> getShipmentPackageContents(String shipmentId, String shipmentPackageSeqId, Criterion filter, List<String> orders) {
        return getStateQueryRepository().getShipmentPackageContents(shipmentId, shipmentPackageSeqId, filter, orders);
    }


    public ShipmentAggregate getShipmentAggregate(ShipmentState state) {
        return new AbstractShipmentAggregate.SimpleShipmentAggregate(state);
    }

    public EventStoreAggregateId toEventStoreAggregateId(String aggregateId) {
        return new EventStoreAggregateId.SimpleEventStoreAggregateId(aggregateId);
    }

    protected void update(ShipmentCommand c, Consumer<ShipmentAggregate> action) {
        String aggregateId = c.getShipmentId();
        EventStoreAggregateId eventStoreAggregateId = toEventStoreAggregateId(aggregateId);
        ShipmentState state = getStateRepository().get(aggregateId, false);
        boolean duplicate = isDuplicateCommand(c, eventStoreAggregateId, state);
        if (duplicate) { return; }

        ShipmentAggregate aggregate = getShipmentAggregate(state);
        aggregate.throwOnInvalidStateTransition(c);
        action.accept(aggregate);
        persist(eventStoreAggregateId, c.getVersion() == null ? ShipmentState.VERSION_NULL : c.getVersion(), aggregate, state); // State version may be null!

    }

    private void persist(EventStoreAggregateId eventStoreAggregateId, long version, ShipmentAggregate aggregate, ShipmentState state) {
        getEventStore().appendEvents(eventStoreAggregateId, version, 
            aggregate.getChanges(), (events) -> { 
                getStateRepository().save(state); 
            });
        if (aggregateEventListener != null) {
            aggregateEventListener.eventAppended(new AggregateEvent<>(aggregate, state, aggregate.getChanges()));
        }
    }

    public void initialize(ShipmentEvent.ShipmentStateCreated stateCreated) {
        String aggregateId = ((ShipmentEvent.SqlShipmentEvent)stateCreated).getShipmentEventId().getShipmentId();
        ShipmentState.SqlShipmentState state = new AbstractShipmentState.SimpleShipmentState();
        state.setShipmentId(aggregateId);

        ShipmentAggregate aggregate = getShipmentAggregate(state);
        ((AbstractShipmentAggregate) aggregate).apply(stateCreated);

        EventStoreAggregateId eventStoreAggregateId = toEventStoreAggregateId(aggregateId);
        persist(eventStoreAggregateId, ((ShipmentEvent.SqlShipmentEvent)stateCreated).getShipmentEventId().getVersion(), aggregate, state);
    }

    protected boolean isDuplicateCommand(ShipmentCommand command, EventStoreAggregateId eventStoreAggregateId, ShipmentState state) {
        boolean duplicate = false;
        if (command.getVersion() == null) { command.setVersion(ShipmentState.VERSION_NULL); }
        if (state.getVersion() != null && state.getVersion() > command.getVersion()) {
            Event lastEvent = getEventStore().getEvent(AbstractShipmentEvent.class, eventStoreAggregateId, command.getVersion());
            if (lastEvent != null && lastEvent instanceof AbstractEvent
               && command.getCommandId() != null && command.getCommandId().equals(((AbstractEvent) lastEvent).getCommandId())) {
                duplicate = true;
            }
        }
        return duplicate;
    }

    public static class SimpleShipmentApplicationService extends AbstractShipmentApplicationService {
        public SimpleShipmentApplicationService(EventStore eventStore, ShipmentStateRepository stateRepository, ShipmentStateQueryRepository stateQueryRepository)
        {
            super(eventStore, stateRepository, stateQueryRepository);
        }
    }

}
