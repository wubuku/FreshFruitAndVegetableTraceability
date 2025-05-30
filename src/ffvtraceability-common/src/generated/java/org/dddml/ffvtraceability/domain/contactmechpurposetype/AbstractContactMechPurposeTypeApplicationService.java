// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.contactmechpurposetype;

import java.util.*;
import java.util.function.Consumer;
import org.dddml.support.criterion.Criterion;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;

public abstract class AbstractContactMechPurposeTypeApplicationService implements ContactMechPurposeTypeApplicationService {
    private ContactMechPurposeTypeStateRepository stateRepository;

    protected ContactMechPurposeTypeStateRepository getStateRepository() {
        return stateRepository;
    }

    private ContactMechPurposeTypeStateQueryRepository stateQueryRepository;

    protected ContactMechPurposeTypeStateQueryRepository getStateQueryRepository() {
        return stateQueryRepository;
    }

    public AbstractContactMechPurposeTypeApplicationService(ContactMechPurposeTypeStateRepository stateRepository, ContactMechPurposeTypeStateQueryRepository stateQueryRepository) {
        this.stateRepository = stateRepository;
        this.stateQueryRepository = stateQueryRepository;
    }

    public void when(ContactMechPurposeTypeCommand.CreateContactMechPurposeType c) {
        update(c, s -> {
        // //////////////////////////
        throwOnConcurrencyConflict(s, c);
        ContactMechPurposeTypeState.SqlContactMechPurposeTypeState ss = ((ContactMechPurposeTypeState.SqlContactMechPurposeTypeState)s);
        ss.setParentTypeId(c.getParentTypeId());
        ss.setHasTable(c.getHasTable());
        ss.setDescription(c.getDescription());
        ss.setCreatedBy(c.getRequesterId());
        ss.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        ss.setCommandId(c.getCommandId());
        // //////////////////////////
        });
    }

    public void when(ContactMechPurposeTypeCommand.MergePatchContactMechPurposeType c) {
        update(c, s -> {
        // //////////////////////////////////
        throwOnConcurrencyConflict(s, c);
        ContactMechPurposeTypeState.SqlContactMechPurposeTypeState ss = ((ContactMechPurposeTypeState.SqlContactMechPurposeTypeState)s);
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

    public ContactMechPurposeTypeState get(String id) {
        ContactMechPurposeTypeState state = getStateRepository().get(id, true);
        return state;
    }

    public Iterable<ContactMechPurposeTypeState> getAll(Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().getAll(firstResult, maxResults);
    }

    public Iterable<ContactMechPurposeTypeState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().get(filter, orders, firstResult, maxResults);
    }

    public Iterable<ContactMechPurposeTypeState> get(Criterion filter, List<String> orders, Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().get(filter, orders, firstResult, maxResults);
    }

    public Iterable<ContactMechPurposeTypeState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults) {
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

    protected void update(ContactMechPurposeTypeCommand c, Consumer<ContactMechPurposeTypeState> action) {
        String aggregateId = c.getContactMechPurposeTypeId();
        EventStoreAggregateId eventStoreAggregateId = toEventStoreAggregateId(aggregateId);
        ContactMechPurposeTypeState state = getStateRepository().get(aggregateId, false);
        boolean duplicate = isDuplicateCommand(c, eventStoreAggregateId, state);
        if (duplicate) { return; }

        ContactMechPurposeTypeCommand.throwOnInvalidStateTransition(state, c);
        action.accept(state);
        persist(eventStoreAggregateId, c.getVersion() == null ? ContactMechPurposeTypeState.VERSION_NULL : c.getVersion(), state); // State version may be null!

    }

    private DomainEventPublisher domainEventPublisher;

    public void setDomainEventPublisher(DomainEventPublisher domainEventPublisher) {
        this.domainEventPublisher = domainEventPublisher;
    }

    public DomainEventPublisher getDomainEventPublisher() {
        if (domainEventPublisher != null) { return domainEventPublisher; }
        return ApplicationContext.current.get(DomainEventPublisher.class);
    }

    private void persist(EventStoreAggregateId eventStoreAggregateId, long version, ContactMechPurposeTypeState state) {
        getStateRepository().save(state);
    }

    protected boolean isDuplicateCommand(ContactMechPurposeTypeCommand command, EventStoreAggregateId eventStoreAggregateId, ContactMechPurposeTypeState state) {
        boolean duplicate = false;
        if (command.getVersion() == null) { command.setVersion(ContactMechPurposeTypeState.VERSION_NULL); }
        if (state.getVersion() != null && state.getVersion() == command.getVersion() + 1) {
            if (command.getCommandId() != null && command.getCommandId().equals(state.getCommandId())) {
                duplicate = true;
            }
        }
        return duplicate;
    }

    protected static void throwOnConcurrencyConflict(ContactMechPurposeTypeState s, ContactMechPurposeTypeCommand c) {
        Long stateVersion = s.getVersion();
        Long commandVersion = c.getVersion();
        if (commandVersion == null) { commandVersion = ContactMechPurposeTypeState.VERSION_NULL; }
        if (!(stateVersion == null && commandVersion.equals(ContactMechPurposeTypeState.VERSION_NULL)) && !commandVersion.equals(stateVersion)) {
            throw DomainError.named("concurrencyConflict", "Conflict between state version (%1$s) and command version (%2$s)", stateVersion, commandVersion);
        }
    }

    public static class SimpleContactMechPurposeTypeApplicationService extends AbstractContactMechPurposeTypeApplicationService {
        public SimpleContactMechPurposeTypeApplicationService(ContactMechPurposeTypeStateRepository stateRepository, ContactMechPurposeTypeStateQueryRepository stateQueryRepository)
        {
            super(stateRepository, stateQueryRepository);
        }
    }

}

