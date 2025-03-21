// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.facilitytype;

import java.util.*;
import java.util.function.Consumer;
import org.dddml.support.criterion.Criterion;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;

public abstract class AbstractFacilityTypeApplicationService implements FacilityTypeApplicationService {
    private FacilityTypeStateRepository stateRepository;

    protected FacilityTypeStateRepository getStateRepository() {
        return stateRepository;
    }

    private FacilityTypeStateQueryRepository stateQueryRepository;

    protected FacilityTypeStateQueryRepository getStateQueryRepository() {
        return stateQueryRepository;
    }

    public AbstractFacilityTypeApplicationService(FacilityTypeStateRepository stateRepository, FacilityTypeStateQueryRepository stateQueryRepository) {
        this.stateRepository = stateRepository;
        this.stateQueryRepository = stateQueryRepository;
    }

    public void when(FacilityTypeCommand.CreateFacilityType c) {
        update(c, s -> {
        // //////////////////////////
        throwOnConcurrencyConflict(s, c);
        FacilityTypeState.SqlFacilityTypeState ss = ((FacilityTypeState.SqlFacilityTypeState)s);
        ss.setParentTypeId(c.getParentTypeId());
        ss.setHasTable(c.getHasTable());
        ss.setDescription(c.getDescription());
        ss.setCreatedBy(c.getRequesterId());
        ss.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        ss.setCommandId(c.getCommandId());
        // //////////////////////////
        });
    }

    public void when(FacilityTypeCommand.MergePatchFacilityType c) {
        update(c, s -> {
        // //////////////////////////////////
        throwOnConcurrencyConflict(s, c);
        FacilityTypeState.SqlFacilityTypeState ss = ((FacilityTypeState.SqlFacilityTypeState)s);
        if (c.getParentTypeId() == null) {
            if (c.getIsPropertyParentTypeIdRemoved() != null && c.getIsPropertyParentTypeIdRemoved()) {
                ss.setParentTypeId(null);
            }
        } else {
            ss.setParentTypeId(c.getParentTypeId());
        }
        if (c.getHasTable() == null) {
            if (c.getIsPropertyHasTableRemoved() != null && c.getIsPropertyHasTableRemoved()) {
                ss.setHasTable(null);
            }
        } else {
            ss.setHasTable(c.getHasTable());
        }
        if (c.getDescription() == null) {
            if (c.getIsPropertyDescriptionRemoved() != null && c.getIsPropertyDescriptionRemoved()) {
                ss.setDescription(null);
            }
        } else {
            ss.setDescription(c.getDescription());
        }
        ss.setUpdatedBy(c.getRequesterId());
        ss.setUpdatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        ss.setCommandId(c.getCommandId());
        // //////////////////////////////////
        });
    }

    public FacilityTypeState get(String id) {
        FacilityTypeState state = getStateRepository().get(id, true);
        return state;
    }

    public Iterable<FacilityTypeState> getAll(Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().getAll(firstResult, maxResults);
    }

    public Iterable<FacilityTypeState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().get(filter, orders, firstResult, maxResults);
    }

    public Iterable<FacilityTypeState> get(Criterion filter, List<String> orders, Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().get(filter, orders, firstResult, maxResults);
    }

    public Iterable<FacilityTypeState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().getByProperty(propertyName, propertyValue, orders, firstResult, maxResults);
    }

    public long getCount(Iterable<Map.Entry<String, Object>> filter) {
        return getStateQueryRepository().getCount(filter);
    }

    public long getCount(Criterion filter) {
        return getStateQueryRepository().getCount(filter);
    }

    public EventStoreAggregateId toEventStoreAggregateId(String aggregateId) {
        return new EventStoreAggregateId.SimpleEventStoreAggregateId(aggregateId);
    }

    protected void update(FacilityTypeCommand c, Consumer<FacilityTypeState> action) {
        String aggregateId = c.getFacilityTypeId();
        EventStoreAggregateId eventStoreAggregateId = toEventStoreAggregateId(aggregateId);
        FacilityTypeState state = getStateRepository().get(aggregateId, false);
        boolean duplicate = isDuplicateCommand(c, eventStoreAggregateId, state);
        if (duplicate) { return; }

        FacilityTypeCommand.throwOnInvalidStateTransition(state, c);
        action.accept(state);
        persist(eventStoreAggregateId, c.getVersion() == null ? FacilityTypeState.VERSION_NULL : c.getVersion(), state); // State version may be null!

    }

    private DomainEventPublisher domainEventPublisher;

    public void setDomainEventPublisher(DomainEventPublisher domainEventPublisher) {
        this.domainEventPublisher = domainEventPublisher;
    }

    public DomainEventPublisher getDomainEventPublisher() {
        if (domainEventPublisher != null) { return domainEventPublisher; }
        return ApplicationContext.current.get(DomainEventPublisher.class);
    }

    private void persist(EventStoreAggregateId eventStoreAggregateId, long version, FacilityTypeState state) {
        getStateRepository().save(state);
    }

    protected boolean isDuplicateCommand(FacilityTypeCommand command, EventStoreAggregateId eventStoreAggregateId, FacilityTypeState state) {
        boolean duplicate = false;
        if (command.getVersion() == null) { command.setVersion(FacilityTypeState.VERSION_NULL); }
        if (state.getVersion() != null && state.getVersion() == command.getVersion() + 1) {
            if (command.getCommandId() != null && command.getCommandId().equals(state.getCommandId())) {
                duplicate = true;
            }
        }
        return duplicate;
    }

    protected static void throwOnConcurrencyConflict(FacilityTypeState s, FacilityTypeCommand c) {
        Long stateVersion = s.getVersion();
        Long commandVersion = c.getVersion();
        if (commandVersion == null) { commandVersion = FacilityTypeState.VERSION_NULL; }
        if (!(stateVersion == null && commandVersion.equals(FacilityTypeState.VERSION_NULL)) && !commandVersion.equals(stateVersion)) {
            throw DomainError.named("concurrencyConflict", "Conflict between state version (%1$s) and command version (%2$s)", stateVersion, commandVersion);
        }
    }

    public static class SimpleFacilityTypeApplicationService extends AbstractFacilityTypeApplicationService {
        public SimpleFacilityTypeApplicationService(FacilityTypeStateRepository stateRepository, FacilityTypeStateQueryRepository stateQueryRepository)
        {
            super(stateRepository, stateQueryRepository);
        }
    }

}

