// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.facilityidentification;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;

public abstract class AbstractFacilityIdentificationAggregate extends AbstractAggregate implements FacilityIdentificationAggregate {
    private FacilityIdentificationState.MutableFacilityIdentificationState state;

    private List<Event> changes = new ArrayList<Event>();

    public AbstractFacilityIdentificationAggregate(FacilityIdentificationState state) {
        this.state = (FacilityIdentificationState.MutableFacilityIdentificationState)state;
    }

    public FacilityIdentificationState getState() {
        return this.state;
    }

    public List<Event> getChanges() {
        return this.changes;
    }

    public void create(FacilityIdentificationCommand.CreateFacilityIdentification c) {
        if (c.getVersion() == null) { c.setVersion(FacilityIdentificationState.VERSION_NULL); }
        FacilityIdentificationEvent e = map(c);
        apply(e);
    }

    public void mergePatch(FacilityIdentificationCommand.MergePatchFacilityIdentification c) {
        FacilityIdentificationEvent e = map(c);
        apply(e);
    }

    public void delete(FacilityIdentificationCommand.DeleteFacilityIdentification c) {
        FacilityIdentificationEvent e = map(c);
        apply(e);
    }

    public void throwOnInvalidStateTransition(Command c) {
        FacilityIdentificationCommand.throwOnInvalidStateTransition(this.state, c);
    }

    protected void apply(Event e) {
        onApplying(e);
        state.mutate(e);
        changes.add(e);
    }

    protected FacilityIdentificationEvent map(FacilityIdentificationCommand.CreateFacilityIdentification c) {
        FacilityIdentificationEventId stateEventId = new FacilityIdentificationEventId(c.getFacilityIdentificationTypeId(), c.getVersion());
        FacilityIdentificationEvent.FacilityIdentificationStateCreated e = newFacilityIdentificationStateCreated(stateEventId);
        e.setIdValue(c.getIdValue());
        ((AbstractFacilityIdentificationEvent)e).setCommandId(c.getCommandId());
        e.setCreatedBy(c.getRequesterId());
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        return e;
    }

    protected FacilityIdentificationEvent map(FacilityIdentificationCommand.MergePatchFacilityIdentification c) {
        FacilityIdentificationEventId stateEventId = new FacilityIdentificationEventId(c.getFacilityIdentificationTypeId(), c.getVersion());
        FacilityIdentificationEvent.FacilityIdentificationStateMergePatched e = newFacilityIdentificationStateMergePatched(stateEventId);
        e.setIdValue(c.getIdValue());
        e.setIsPropertyIdValueRemoved(c.getIsPropertyIdValueRemoved());
        ((AbstractFacilityIdentificationEvent)e).setCommandId(c.getCommandId());
        e.setCreatedBy(c.getRequesterId());
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        return e;
    }

    protected FacilityIdentificationEvent map(FacilityIdentificationCommand.DeleteFacilityIdentification c) {
        FacilityIdentificationEventId stateEventId = new FacilityIdentificationEventId(c.getFacilityIdentificationTypeId(), c.getVersion());
        FacilityIdentificationEvent.FacilityIdentificationStateDeleted e = newFacilityIdentificationStateDeleted(stateEventId);
        ((AbstractFacilityIdentificationEvent)e).setCommandId(c.getCommandId());
        e.setCreatedBy(c.getRequesterId());
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        return e;
    }


    ////////////////////////

    protected FacilityIdentificationEvent.FacilityIdentificationStateCreated newFacilityIdentificationStateCreated(Long version, String commandId, String requesterId) {
        FacilityIdentificationEventId stateEventId = new FacilityIdentificationEventId(this.state.getFacilityIdentificationTypeId(), version);
        FacilityIdentificationEvent.FacilityIdentificationStateCreated e = newFacilityIdentificationStateCreated(stateEventId);
        ((AbstractFacilityIdentificationEvent)e).setCommandId(commandId);
        e.setCreatedBy(requesterId);
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        return e;
    }

    protected FacilityIdentificationEvent.FacilityIdentificationStateMergePatched newFacilityIdentificationStateMergePatched(Long version, String commandId, String requesterId) {
        FacilityIdentificationEventId stateEventId = new FacilityIdentificationEventId(this.state.getFacilityIdentificationTypeId(), version);
        FacilityIdentificationEvent.FacilityIdentificationStateMergePatched e = newFacilityIdentificationStateMergePatched(stateEventId);
        ((AbstractFacilityIdentificationEvent)e).setCommandId(commandId);
        e.setCreatedBy(requesterId);
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        return e;
    }

    protected FacilityIdentificationEvent.FacilityIdentificationStateDeleted newFacilityIdentificationStateDeleted(Long version, String commandId, String requesterId) {
        FacilityIdentificationEventId stateEventId = new FacilityIdentificationEventId(this.state.getFacilityIdentificationTypeId(), version);
        FacilityIdentificationEvent.FacilityIdentificationStateDeleted e = newFacilityIdentificationStateDeleted(stateEventId);
        ((AbstractFacilityIdentificationEvent)e).setCommandId(commandId);
        e.setCreatedBy(requesterId);
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        return e;
    }

    protected FacilityIdentificationEvent.FacilityIdentificationStateCreated newFacilityIdentificationStateCreated(FacilityIdentificationEventId stateEventId) {
        return new AbstractFacilityIdentificationEvent.SimpleFacilityIdentificationStateCreated(stateEventId);
    }

    protected FacilityIdentificationEvent.FacilityIdentificationStateMergePatched newFacilityIdentificationStateMergePatched(FacilityIdentificationEventId stateEventId) {
        return new AbstractFacilityIdentificationEvent.SimpleFacilityIdentificationStateMergePatched(stateEventId);
    }

    protected FacilityIdentificationEvent.FacilityIdentificationStateDeleted newFacilityIdentificationStateDeleted(FacilityIdentificationEventId stateEventId) {
        return new AbstractFacilityIdentificationEvent.SimpleFacilityIdentificationStateDeleted(stateEventId);
    }


    public static class SimpleFacilityIdentificationAggregate extends AbstractFacilityIdentificationAggregate {
        public SimpleFacilityIdentificationAggregate(FacilityIdentificationState state) {
            super(state);
        }

    }

}
