// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.lot;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;

public abstract class AbstractLotAggregate extends AbstractAggregate implements LotAggregate {
    private LotState.MutableLotState state;

    private List<Event> changes = new ArrayList<Event>();

    public AbstractLotAggregate(LotState state) {
        this.state = (LotState.MutableLotState)state;
    }

    public LotState getState() {
        return this.state;
    }

    public List<Event> getChanges() {
        return this.changes;
    }

    public void create(LotCommand.CreateLot c) {
        if (c.getVersion() == null) { c.setVersion(LotState.VERSION_NULL); }
        LotEvent e = map(c);
        apply(e);
    }

    public void mergePatch(LotCommand.MergePatchLot c) {
        LotEvent e = map(c);
        apply(e);
    }

    public void delete(LotCommand.DeleteLot c) {
        LotEvent e = map(c);
        apply(e);
    }

    public void throwOnInvalidStateTransition(Command c) {
        LotCommand.throwOnInvalidStateTransition(this.state, c);
    }

    protected void apply(Event e) {
        onApplying(e);
        state.mutate(e);
        changes.add(e);
    }

    protected LotEvent map(LotCommand.CreateLot c) {
        LotEventId stateEventId = new LotEventId(c.getLotId(), c.getVersion());
        LotEvent.LotStateCreated e = newLotStateCreated(stateEventId);
        e.setGs1Batch(c.getGs1Batch());
        e.setQuantity(c.getQuantity());
        e.setExpirationDate(c.getExpirationDate());
        ((AbstractLotEvent)e).setCommandId(c.getCommandId());
        e.setCreatedBy(c.getRequesterId());
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        Long version = c.getVersion();
        for (LotIdentificationCommand.CreateLotIdentification innerCommand : c.getCreateLotIdentificationCommands()) {
            throwOnInconsistentCommands(c, innerCommand);
            LotIdentificationEvent.LotIdentificationStateCreated innerEvent = mapCreate(innerCommand, c, version, this.state);
            e.addLotIdentificationEvent(innerEvent);
        }

        return e;
    }

    protected LotEvent map(LotCommand.MergePatchLot c) {
        LotEventId stateEventId = new LotEventId(c.getLotId(), c.getVersion());
        LotEvent.LotStateMergePatched e = newLotStateMergePatched(stateEventId);
        e.setGs1Batch(c.getGs1Batch());
        e.setQuantity(c.getQuantity());
        e.setExpirationDate(c.getExpirationDate());
        e.setIsPropertyGs1BatchRemoved(c.getIsPropertyGs1BatchRemoved());
        e.setIsPropertyQuantityRemoved(c.getIsPropertyQuantityRemoved());
        e.setIsPropertyExpirationDateRemoved(c.getIsPropertyExpirationDateRemoved());
        ((AbstractLotEvent)e).setCommandId(c.getCommandId());
        e.setCreatedBy(c.getRequesterId());
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        Long version = c.getVersion();
        for (LotIdentificationCommand innerCommand : c.getLotIdentificationCommands()) {
            throwOnInconsistentCommands(c, innerCommand);
            LotIdentificationEvent innerEvent = map(innerCommand, c, version, this.state);
            e.addLotIdentificationEvent(innerEvent);
        }

        return e;
    }

    protected LotEvent map(LotCommand.DeleteLot c) {
        LotEventId stateEventId = new LotEventId(c.getLotId(), c.getVersion());
        LotEvent.LotStateDeleted e = newLotStateDeleted(stateEventId);
        ((AbstractLotEvent)e).setCommandId(c.getCommandId());
        e.setCreatedBy(c.getRequesterId());
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        return e;
    }


    protected LotIdentificationEvent map(LotIdentificationCommand c, LotCommand outerCommand, Long version, LotState outerState) {
        LotIdentificationCommand.CreateLotIdentification create = (c.getCommandType().equals(CommandType.CREATE)) ? ((LotIdentificationCommand.CreateLotIdentification)c) : null;
        if(create != null) {
            return mapCreate(create, outerCommand, version, outerState);
        }

        LotIdentificationCommand.MergePatchLotIdentification merge = (c.getCommandType().equals(CommandType.MERGE_PATCH)) ? ((LotIdentificationCommand.MergePatchLotIdentification)c) : null;
        if(merge != null) {
            return mapMergePatch(merge, outerCommand, version, outerState);
        }

        LotIdentificationCommand.RemoveLotIdentification remove = (c.getCommandType().equals(CommandType.REMOVE)) ? ((LotIdentificationCommand.RemoveLotIdentification)c) : null;
        if (remove != null) {
            return mapRemove(remove, outerCommand, version, outerState);
        }
        throw new UnsupportedOperationException();
    }

    protected LotIdentificationEvent.LotIdentificationStateCreated mapCreate(LotIdentificationCommand.CreateLotIdentification c, LotCommand outerCommand, Long version, LotState outerState) {
        ((AbstractCommand)c).setRequesterId(outerCommand.getRequesterId());
        LotIdentificationEventId stateEventId = new LotIdentificationEventId(outerState.getLotId(), c.getLotIdentificationTypeId(), version);
        LotIdentificationEvent.LotIdentificationStateCreated e = newLotIdentificationStateCreated(stateEventId);
        LotIdentificationState s = ((EntityStateCollection.ModifiableEntityStateCollection<String, LotIdentificationState>)outerState.getLotIdentifications()).getOrAddDefault(c.getLotIdentificationTypeId());

        e.setIdValue(c.getIdValue());
        e.setCreatedBy(c.getRequesterId());
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));

        return e;

    }// END map(ICreate... ////////////////////////////

    protected LotIdentificationEvent.LotIdentificationStateMergePatched mapMergePatch(LotIdentificationCommand.MergePatchLotIdentification c, LotCommand outerCommand, Long version, LotState outerState) {
        ((AbstractCommand)c).setRequesterId(outerCommand.getRequesterId());
        LotIdentificationEventId stateEventId = new LotIdentificationEventId(outerState.getLotId(), c.getLotIdentificationTypeId(), version);
        LotIdentificationEvent.LotIdentificationStateMergePatched e = newLotIdentificationStateMergePatched(stateEventId);
        LotIdentificationState s = ((EntityStateCollection.ModifiableEntityStateCollection<String, LotIdentificationState>)outerState.getLotIdentifications()).getOrAddDefault(c.getLotIdentificationTypeId());

        e.setIdValue(c.getIdValue());
        e.setIsPropertyIdValueRemoved(c.getIsPropertyIdValueRemoved());

        e.setCreatedBy(c.getRequesterId());
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));

        return e;

    }// END map(IMergePatch... ////////////////////////////

    protected LotIdentificationEvent.LotIdentificationStateRemoved mapRemove(LotIdentificationCommand.RemoveLotIdentification c, LotCommand outerCommand, Long version, LotState outerState) {
        ((AbstractCommand)c).setRequesterId(outerCommand.getRequesterId());
        LotIdentificationEventId stateEventId = new LotIdentificationEventId(outerState.getLotId(), c.getLotIdentificationTypeId(), version);
        LotIdentificationEvent.LotIdentificationStateRemoved e = newLotIdentificationStateRemoved(stateEventId);

        e.setCreatedBy(c.getRequesterId());
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));

        return e;

    }// END map(IRemove... ////////////////////////////

    protected void throwOnInconsistentCommands(LotCommand command, LotIdentificationCommand innerCommand) {
        AbstractLotCommand properties = command instanceof AbstractLotCommand ? (AbstractLotCommand) command : null;
        AbstractLotIdentificationCommand innerProperties = innerCommand instanceof AbstractLotIdentificationCommand ? (AbstractLotIdentificationCommand) innerCommand : null;
        if (properties == null || innerProperties == null) { return; }
        String outerLotIdName = "LotId";
        String outerLotIdValue = properties.getLotId();
        String innerLotIdName = "LotId";
        String innerLotIdValue = innerProperties.getLotId();
        if (innerLotIdValue == null) {
            innerProperties.setLotId(outerLotIdValue);
        }
        else if (innerLotIdValue != outerLotIdValue 
            && (innerLotIdValue == null || innerLotIdValue != null && !innerLotIdValue.equals(outerLotIdValue))) {
            throw DomainError.named("inconsistentId", "Outer %1$s %2$s NOT equals inner %3$s %4$s", outerLotIdName, outerLotIdValue, innerLotIdName, innerLotIdValue);
        }
    }// END throwOnInconsistentCommands /////////////////////


    ////////////////////////

    protected LotEvent.LotStateCreated newLotStateCreated(Long version, String commandId, String requesterId) {
        LotEventId stateEventId = new LotEventId(this.state.getLotId(), version);
        LotEvent.LotStateCreated e = newLotStateCreated(stateEventId);
        ((AbstractLotEvent)e).setCommandId(commandId);
        e.setCreatedBy(requesterId);
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        return e;
    }

    protected LotEvent.LotStateMergePatched newLotStateMergePatched(Long version, String commandId, String requesterId) {
        LotEventId stateEventId = new LotEventId(this.state.getLotId(), version);
        LotEvent.LotStateMergePatched e = newLotStateMergePatched(stateEventId);
        ((AbstractLotEvent)e).setCommandId(commandId);
        e.setCreatedBy(requesterId);
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        return e;
    }

    protected LotEvent.LotStateDeleted newLotStateDeleted(Long version, String commandId, String requesterId) {
        LotEventId stateEventId = new LotEventId(this.state.getLotId(), version);
        LotEvent.LotStateDeleted e = newLotStateDeleted(stateEventId);
        ((AbstractLotEvent)e).setCommandId(commandId);
        e.setCreatedBy(requesterId);
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        return e;
    }

    protected LotEvent.LotStateCreated newLotStateCreated(LotEventId stateEventId) {
        return new AbstractLotEvent.SimpleLotStateCreated(stateEventId);
    }

    protected LotEvent.LotStateMergePatched newLotStateMergePatched(LotEventId stateEventId) {
        return new AbstractLotEvent.SimpleLotStateMergePatched(stateEventId);
    }

    protected LotEvent.LotStateDeleted newLotStateDeleted(LotEventId stateEventId) {
        return new AbstractLotEvent.SimpleLotStateDeleted(stateEventId);
    }

    protected LotIdentificationEvent.LotIdentificationStateCreated newLotIdentificationStateCreated(LotIdentificationEventId stateEventId) {
        return new AbstractLotIdentificationEvent.SimpleLotIdentificationStateCreated(stateEventId);
    }

    protected LotIdentificationEvent.LotIdentificationStateMergePatched newLotIdentificationStateMergePatched(LotIdentificationEventId stateEventId) {
        return new AbstractLotIdentificationEvent.SimpleLotIdentificationStateMergePatched(stateEventId);
    }

    protected LotIdentificationEvent.LotIdentificationStateRemoved newLotIdentificationStateRemoved(LotIdentificationEventId stateEventId) {
        return new AbstractLotIdentificationEvent.SimpleLotIdentificationStateRemoved(stateEventId);
    }


    public static class SimpleLotAggregate extends AbstractLotAggregate {
        public SimpleLotAggregate(LotState state) {
            super(state);
        }

    }

}
