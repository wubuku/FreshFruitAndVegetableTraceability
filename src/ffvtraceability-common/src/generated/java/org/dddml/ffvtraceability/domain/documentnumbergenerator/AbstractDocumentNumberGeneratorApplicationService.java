// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.documentnumbergenerator;

import java.util.*;
import java.util.function.Consumer;
import org.dddml.support.criterion.Criterion;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;

public abstract class AbstractDocumentNumberGeneratorApplicationService implements DocumentNumberGeneratorApplicationService {
    private DocumentNumberGeneratorStateRepository stateRepository;

    protected DocumentNumberGeneratorStateRepository getStateRepository() {
        return stateRepository;
    }

    private DocumentNumberGeneratorStateQueryRepository stateQueryRepository;

    protected DocumentNumberGeneratorStateQueryRepository getStateQueryRepository() {
        return stateQueryRepository;
    }

    public AbstractDocumentNumberGeneratorApplicationService(DocumentNumberGeneratorStateRepository stateRepository, DocumentNumberGeneratorStateQueryRepository stateQueryRepository) {
        this.stateRepository = stateRepository;
        this.stateQueryRepository = stateQueryRepository;
    }

    public void when(DocumentNumberGeneratorCommand.CreateDocumentNumberGenerator c) {
        update(c, s -> {
        // //////////////////////////
        throwOnConcurrencyConflict(s, c);
        DocumentNumberGeneratorState.SqlDocumentNumberGeneratorState ss = ((DocumentNumberGeneratorState.SqlDocumentNumberGeneratorState)s);
        ss.setPrefix(c.getPrefix());
        ss.setDateFormat(c.getDateFormat());
        ss.setTimeZoneId(c.getTimeZoneId());
        ss.setSequenceLength(c.getSequenceLength());
        ss.setLastGeneratedDate(c.getLastGeneratedDate());
        ss.setCurrentSequence(c.getCurrentSequence());
        ss.setDescription(c.getDescription());
        ss.setCreatedBy(c.getRequesterId());
        ss.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        ss.setCommandId(c.getCommandId());
        // //////////////////////////
        });
    }

    public void when(DocumentNumberGeneratorCommand.MergePatchDocumentNumberGenerator c) {
        update(c, s -> {
        // //////////////////////////////////
        throwOnConcurrencyConflict(s, c);
        DocumentNumberGeneratorState.SqlDocumentNumberGeneratorState ss = ((DocumentNumberGeneratorState.SqlDocumentNumberGeneratorState)s);
        if (c.getPrefix() == null) {
            if (c.getIsPropertyPrefixRemoved() != null && c.getIsPropertyPrefixRemoved()) {
                ss.setPrefix(null);
            }
        } else {
            ss.setPrefix(c.getPrefix());
        }
        if (c.getDateFormat() == null) {
            if (c.getIsPropertyDateFormatRemoved() != null && c.getIsPropertyDateFormatRemoved()) {
                ss.setDateFormat(null);
            }
        } else {
            ss.setDateFormat(c.getDateFormat());
        }
        if (c.getTimeZoneId() == null) {
            if (c.getIsPropertyTimeZoneIdRemoved() != null && c.getIsPropertyTimeZoneIdRemoved()) {
                ss.setTimeZoneId(null);
            }
        } else {
            ss.setTimeZoneId(c.getTimeZoneId());
        }
        if (c.getSequenceLength() == null) {
            if (c.getIsPropertySequenceLengthRemoved() != null && c.getIsPropertySequenceLengthRemoved()) {
                ss.setSequenceLength(null);
            }
        } else {
            ss.setSequenceLength(c.getSequenceLength());
        }
        if (c.getLastGeneratedDate() == null) {
            if (c.getIsPropertyLastGeneratedDateRemoved() != null && c.getIsPropertyLastGeneratedDateRemoved()) {
                ss.setLastGeneratedDate(null);
            }
        } else {
            ss.setLastGeneratedDate(c.getLastGeneratedDate());
        }
        if (c.getCurrentSequence() == null) {
            if (c.getIsPropertyCurrentSequenceRemoved() != null && c.getIsPropertyCurrentSequenceRemoved()) {
                ss.setCurrentSequence(null);
            }
        } else {
            ss.setCurrentSequence(c.getCurrentSequence());
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

    public String when(DocumentNumberGeneratorCommands.GenerateNextNumber c) {
        throw new UnsupportedOperationException(String.format(
            "Method '%s' is not implemented for non-event-sourcing mode", 
            "GenerateNextNumber"));
    }

    public DocumentNumberGeneratorState get(String id) {
        DocumentNumberGeneratorState state = getStateRepository().get(id, true);
        return state;
    }

    public Iterable<DocumentNumberGeneratorState> getAll(Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().getAll(firstResult, maxResults);
    }

    public Iterable<DocumentNumberGeneratorState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().get(filter, orders, firstResult, maxResults);
    }

    public Iterable<DocumentNumberGeneratorState> get(Criterion filter, List<String> orders, Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().get(filter, orders, firstResult, maxResults);
    }

    public Iterable<DocumentNumberGeneratorState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults) {
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

    protected void update(DocumentNumberGeneratorCommand c, Consumer<DocumentNumberGeneratorState> action) {
        String aggregateId = c.getGeneratorId();
        EventStoreAggregateId eventStoreAggregateId = toEventStoreAggregateId(aggregateId);
        DocumentNumberGeneratorState state = getStateRepository().get(aggregateId, false);
        boolean duplicate = isDuplicateCommand(c, eventStoreAggregateId, state);
        if (duplicate) { return; }

        DocumentNumberGeneratorCommand.throwOnInvalidStateTransition(state, c);
        action.accept(state);
        persist(eventStoreAggregateId, c.getVersion() == null ? DocumentNumberGeneratorState.VERSION_NULL : c.getVersion(), state); // State version may be null!

    }

    private DomainEventPublisher domainEventPublisher;

    public void setDomainEventPublisher(DomainEventPublisher domainEventPublisher) {
        this.domainEventPublisher = domainEventPublisher;
    }

    public DomainEventPublisher getDomainEventPublisher() {
        if (domainEventPublisher != null) { return domainEventPublisher; }
        return ApplicationContext.current.get(DomainEventPublisher.class);
    }

    private void persist(EventStoreAggregateId eventStoreAggregateId, long version, DocumentNumberGeneratorState state) {
        getStateRepository().save(state);
    }

    protected boolean isDuplicateCommand(DocumentNumberGeneratorCommand command, EventStoreAggregateId eventStoreAggregateId, DocumentNumberGeneratorState state) {
        boolean duplicate = false;
        if (command.getVersion() == null) { command.setVersion(DocumentNumberGeneratorState.VERSION_NULL); }
        if (state.getVersion() != null && state.getVersion() == command.getVersion() + 1) {
            if (command.getCommandId() != null && command.getCommandId().equals(state.getCommandId())) {
                duplicate = true;
            }
        }
        return duplicate;
    }

    protected static void throwOnConcurrencyConflict(DocumentNumberGeneratorState s, DocumentNumberGeneratorCommand c) {
        Long stateVersion = s.getVersion();
        Long commandVersion = c.getVersion();
        if (commandVersion == null) { commandVersion = DocumentNumberGeneratorState.VERSION_NULL; }
        if (!(stateVersion == null && commandVersion.equals(DocumentNumberGeneratorState.VERSION_NULL)) && !commandVersion.equals(stateVersion)) {
            throw DomainError.named("concurrencyConflict", "Conflict between state version (%1$s) and command version (%2$s)", stateVersion, commandVersion);
        }
    }

    public static class SimpleDocumentNumberGeneratorApplicationService extends AbstractDocumentNumberGeneratorApplicationService {
        public SimpleDocumentNumberGeneratorApplicationService(DocumentNumberGeneratorStateRepository stateRepository, DocumentNumberGeneratorStateQueryRepository stateQueryRepository)
        {
            super(stateRepository, stateQueryRepository);
        }
    }

}

