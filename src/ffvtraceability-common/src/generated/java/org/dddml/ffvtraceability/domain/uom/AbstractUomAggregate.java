// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.uom;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;

public abstract class AbstractUomAggregate extends AbstractAggregate implements UomAggregate {
    private UomState.MutableUomState state;

    protected List<Event> changes = new ArrayList<Event>();

    public AbstractUomAggregate(UomState state) {
        this.state = (UomState.MutableUomState)state;
    }

    public UomState getState() {
        return this.state;
    }

    public List<Event> getChanges() {
        return this.changes;
    }

    public void create(UomCommand.CreateUom c) {
        if (c.getVersion() == null) { c.setVersion(UomState.VERSION_NULL); }
        UomEvent e = map(c);
        apply(e);
    }

    public void mergePatch(UomCommand.MergePatchUom c) {
        UomEvent e = map(c);
        apply(e);
    }

    public void throwOnInvalidStateTransition(Command c) {
        UomCommand.throwOnInvalidStateTransition(this.state, c);
    }

    protected void apply(Event e) {
        onApplying(e);
        state.mutate(e);
        changes.add(e);
    }

    @Override
    protected void onApplying(Event e) {
        if (state.getVersion() == null) {
            state.setTenantId(TenantContext.getTenantId());
        }
        if (e instanceof UomEvent) {
            UomEvent ee = (UomEvent) e;
            ee.setTenantId(state.getTenantId());
        }
        super.onApplying(e);
    }

    protected UomEvent map(UomCommand.CreateUom c) {
        UomEventId stateEventId = new UomEventId(c.getUomId(), c.getVersion());
        UomEvent.UomStateCreated e = newUomStateCreated(stateEventId);
        e.setUomTypeId(c.getUomTypeId());
        e.setAbbreviation(c.getAbbreviation());
        e.setNumericCode(c.getNumericCode());
        e.setGs1AI(c.getGs1AI());
        e.setDescription(c.getDescription());
        e.setActive(c.getActive());
        e.setUomName(c.getUomName());
        ((AbstractUomEvent)e).setCommandId(c.getCommandId());
        e.setCreatedBy(c.getRequesterId());
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        return e;
    }

    protected UomEvent map(UomCommand.MergePatchUom c) {
        UomEventId stateEventId = new UomEventId(c.getUomId(), c.getVersion());
        UomEvent.UomStateMergePatched e = newUomStateMergePatched(stateEventId);
        e.setUomTypeId(c.getUomTypeId());
        e.setAbbreviation(c.getAbbreviation());
        e.setNumericCode(c.getNumericCode());
        e.setGs1AI(c.getGs1AI());
        e.setDescription(c.getDescription());
        e.setActive(c.getActive());
        e.setUomName(c.getUomName());
        e.setIsPropertyUomTypeIdRemoved(c.getIsPropertyUomTypeIdRemoved());
        e.setIsPropertyAbbreviationRemoved(c.getIsPropertyAbbreviationRemoved());
        e.setIsPropertyNumericCodeRemoved(c.getIsPropertyNumericCodeRemoved());
        e.setIsPropertyGs1AIRemoved(c.getIsPropertyGs1AIRemoved());
        e.setIsPropertyDescriptionRemoved(c.getIsPropertyDescriptionRemoved());
        e.setIsPropertyActiveRemoved(c.getIsPropertyActiveRemoved());
        e.setIsPropertyUomNameRemoved(c.getIsPropertyUomNameRemoved());
        ((AbstractUomEvent)e).setCommandId(c.getCommandId());
        e.setCreatedBy(c.getRequesterId());
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        return e;
    }


    ////////////////////////

    protected UomEvent.UomStateCreated newUomStateCreated(Long version, String commandId, String requesterId) {
        UomEventId stateEventId = new UomEventId(this.state.getUomId(), version);
        UomEvent.UomStateCreated e = newUomStateCreated(stateEventId);
        ((AbstractUomEvent)e).setCommandId(commandId);
        e.setCreatedBy(requesterId);
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        return e;
    }

    protected UomEvent.UomStateMergePatched newUomStateMergePatched(Long version, String commandId, String requesterId) {
        UomEventId stateEventId = new UomEventId(this.state.getUomId(), version);
        UomEvent.UomStateMergePatched e = newUomStateMergePatched(stateEventId);
        ((AbstractUomEvent)e).setCommandId(commandId);
        e.setCreatedBy(requesterId);
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        return e;
    }

    protected UomEvent.UomStateCreated newUomStateCreated(UomEventId stateEventId) {
        return new AbstractUomEvent.SimpleUomStateCreated(stateEventId);
    }

    protected UomEvent.UomStateMergePatched newUomStateMergePatched(UomEventId stateEventId) {
        return new AbstractUomEvent.SimpleUomStateMergePatched(stateEventId);
    }


    public static class SimpleUomAggregate extends AbstractUomAggregate {
        public SimpleUomAggregate(UomState state) {
            super(state);
        }

    }

}

