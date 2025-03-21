// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.geopoint;

import java.util.*;
import java.util.function.Consumer;
import org.dddml.support.criterion.Criterion;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;

public abstract class AbstractGeoPointApplicationService implements GeoPointApplicationService {
    private GeoPointStateRepository stateRepository;

    protected GeoPointStateRepository getStateRepository() {
        return stateRepository;
    }

    private GeoPointStateQueryRepository stateQueryRepository;

    protected GeoPointStateQueryRepository getStateQueryRepository() {
        return stateQueryRepository;
    }

    public AbstractGeoPointApplicationService(GeoPointStateRepository stateRepository, GeoPointStateQueryRepository stateQueryRepository) {
        this.stateRepository = stateRepository;
        this.stateQueryRepository = stateQueryRepository;
    }

    public void when(GeoPointCommand.CreateGeoPoint c) {
        update(c, s -> {
        // //////////////////////////
        throwOnConcurrencyConflict(s, c);
        GeoPointState.SqlGeoPointState ss = ((GeoPointState.SqlGeoPointState)s);
        ss.setGeoPointTypeEnumId(c.getGeoPointTypeEnumId());
        ss.setDescription(c.getDescription());
        ss.setLatitude(c.getLatitude());
        ss.setLongitude(c.getLongitude());
        ss.setElevation(c.getElevation());
        ss.setElevationUomId(c.getElevationUomId());
        ss.setInformation(c.getInformation());
        ss.setCreatedBy(c.getRequesterId());
        ss.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        ss.setCommandId(c.getCommandId());
        // //////////////////////////
        });
    }

    public void when(GeoPointCommand.MergePatchGeoPoint c) {
        update(c, s -> {
        // //////////////////////////////////
        throwOnConcurrencyConflict(s, c);
        GeoPointState.SqlGeoPointState ss = ((GeoPointState.SqlGeoPointState)s);
        if (c.getGeoPointTypeEnumId() == null) {
            if (c.getIsPropertyGeoPointTypeEnumIdRemoved() != null && c.getIsPropertyGeoPointTypeEnumIdRemoved()) {
                ss.setGeoPointTypeEnumId(null);
            }
        } else {
            ss.setGeoPointTypeEnumId(c.getGeoPointTypeEnumId());
        }
        if (c.getDescription() == null) {
            if (c.getIsPropertyDescriptionRemoved() != null && c.getIsPropertyDescriptionRemoved()) {
                ss.setDescription(null);
            }
        } else {
            ss.setDescription(c.getDescription());
        }
        if (c.getLatitude() == null) {
            if (c.getIsPropertyLatitudeRemoved() != null && c.getIsPropertyLatitudeRemoved()) {
                ss.setLatitude(null);
            }
        } else {
            ss.setLatitude(c.getLatitude());
        }
        if (c.getLongitude() == null) {
            if (c.getIsPropertyLongitudeRemoved() != null && c.getIsPropertyLongitudeRemoved()) {
                ss.setLongitude(null);
            }
        } else {
            ss.setLongitude(c.getLongitude());
        }
        if (c.getElevation() == null) {
            if (c.getIsPropertyElevationRemoved() != null && c.getIsPropertyElevationRemoved()) {
                ss.setElevation(null);
            }
        } else {
            ss.setElevation(c.getElevation());
        }
        if (c.getElevationUomId() == null) {
            if (c.getIsPropertyElevationUomIdRemoved() != null && c.getIsPropertyElevationUomIdRemoved()) {
                ss.setElevationUomId(null);
            }
        } else {
            ss.setElevationUomId(c.getElevationUomId());
        }
        if (c.getInformation() == null) {
            if (c.getIsPropertyInformationRemoved() != null && c.getIsPropertyInformationRemoved()) {
                ss.setInformation(null);
            }
        } else {
            ss.setInformation(c.getInformation());
        }
        ss.setUpdatedBy(c.getRequesterId());
        ss.setUpdatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        ss.setCommandId(c.getCommandId());
        // //////////////////////////////////
        });
    }

    public GeoPointState get(String id) {
        GeoPointState state = getStateRepository().get(id, true);
        return state;
    }

    public Iterable<GeoPointState> getAll(Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().getAll(firstResult, maxResults);
    }

    public Iterable<GeoPointState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().get(filter, orders, firstResult, maxResults);
    }

    public Iterable<GeoPointState> get(Criterion filter, List<String> orders, Integer firstResult, Integer maxResults) {
        return getStateQueryRepository().get(filter, orders, firstResult, maxResults);
    }

    public Iterable<GeoPointState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults) {
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

    protected void update(GeoPointCommand c, Consumer<GeoPointState> action) {
        String aggregateId = c.getGeoPointId();
        EventStoreAggregateId eventStoreAggregateId = toEventStoreAggregateId(aggregateId);
        GeoPointState state = getStateRepository().get(aggregateId, false);
        boolean duplicate = isDuplicateCommand(c, eventStoreAggregateId, state);
        if (duplicate) { return; }

        GeoPointCommand.throwOnInvalidStateTransition(state, c);
        action.accept(state);
        persist(eventStoreAggregateId, c.getVersion() == null ? GeoPointState.VERSION_NULL : c.getVersion(), state); // State version may be null!

    }

    private DomainEventPublisher domainEventPublisher;

    public void setDomainEventPublisher(DomainEventPublisher domainEventPublisher) {
        this.domainEventPublisher = domainEventPublisher;
    }

    public DomainEventPublisher getDomainEventPublisher() {
        if (domainEventPublisher != null) { return domainEventPublisher; }
        return ApplicationContext.current.get(DomainEventPublisher.class);
    }

    private void persist(EventStoreAggregateId eventStoreAggregateId, long version, GeoPointState state) {
        getStateRepository().save(state);
    }

    protected boolean isDuplicateCommand(GeoPointCommand command, EventStoreAggregateId eventStoreAggregateId, GeoPointState state) {
        boolean duplicate = false;
        if (command.getVersion() == null) { command.setVersion(GeoPointState.VERSION_NULL); }
        if (state.getVersion() != null && state.getVersion() == command.getVersion() + 1) {
            if (command.getCommandId() != null && command.getCommandId().equals(state.getCommandId())) {
                duplicate = true;
            }
        }
        return duplicate;
    }

    protected static void throwOnConcurrencyConflict(GeoPointState s, GeoPointCommand c) {
        Long stateVersion = s.getVersion();
        Long commandVersion = c.getVersion();
        if (commandVersion == null) { commandVersion = GeoPointState.VERSION_NULL; }
        if (!(stateVersion == null && commandVersion.equals(GeoPointState.VERSION_NULL)) && !commandVersion.equals(stateVersion)) {
            throw DomainError.named("concurrencyConflict", "Conflict between state version (%1$s) and command version (%2$s)", stateVersion, commandVersion);
        }
    }

    public static class SimpleGeoPointApplicationService extends AbstractGeoPointApplicationService {
        public SimpleGeoPointApplicationService(GeoPointStateRepository stateRepository, GeoPointStateQueryRepository stateQueryRepository)
        {
            super(stateRepository, stateQueryRepository);
        }
    }

}

