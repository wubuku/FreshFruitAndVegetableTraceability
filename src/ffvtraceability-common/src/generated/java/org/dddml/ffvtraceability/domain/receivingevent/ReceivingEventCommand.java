// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.receivingevent;

import java.util.*;
import org.dddml.ffvtraceability.domain.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.specialization.DomainError;

public interface ReceivingEventCommand extends Command {

    Long getEventId();

    void setEventId(Long eventId);

    Long getVersion();

    void setVersion(Long version);

    static void throwOnInvalidStateTransition(ReceivingEventState state, Command c) {
        if (state.getVersion() == null) {
            if (isCommandCreate((ReceivingEventCommand)c)) {
                return;
            }
            throw DomainError.named("premature", "Can't do anything to unexistent aggregate");
        }
        if (state.getDeleted() != null && state.getDeleted()) {
            throw DomainError.named("zombie", "Can't do anything to deleted aggregate.");
        }
        if (isCommandCreate((ReceivingEventCommand)c))
            throw DomainError.named("rebirth", "Can't create aggregate that already exists");
    }

    static boolean isCommandCreate(ReceivingEventCommand c) {
        if ((c instanceof ReceivingEventCommand.CreateReceivingEvent) 
            && (COMMAND_TYPE_CREATE.equals(c.getCommandType()) || c.getVersion().equals(ReceivingEventState.VERSION_NULL)))
            return true;
        if ((c instanceof ReceivingEventCommand.MergePatchReceivingEvent))
            return false;
        if ((c instanceof ReceivingEventCommand.DeleteReceivingEvent))
            return false;
        if (c.getVersion().equals(ReceivingEventState.VERSION_NULL))
            return true;
        return false;
    }

    interface CreateOrMergePatchReceivingEvent extends ReceivingEventCommand
    {

        KdeTraceabilityLotCode getTraceabilityLotCode();

        void setTraceabilityLotCode(KdeTraceabilityLotCode traceabilityLotCode);

        KdeQuantityAndUom getQuantityAndUom();

        void setQuantityAndUom(KdeQuantityAndUom quantityAndUom);

        KdeProductDescription getProductDescription();

        void setProductDescription(KdeProductDescription productDescription);

        KdeLocationDescription getShipToLocation();

        void setShipToLocation(KdeLocationDescription shipToLocation);

        KdeLocationDescription getShipFromLocation();

        void setShipFromLocation(KdeLocationDescription shipFromLocation);

        String getReceiveDate();

        void setReceiveDate(String receiveDate);

        KdeTlcSourceOrTlcSourceReference getTlcSourceOrTlcSourceReference();

        void setTlcSourceOrTlcSourceReference(KdeTlcSourceOrTlcSourceReference tlcSourceOrTlcSourceReference);

        KdeReferenceDocument getReferenceDocument();

        void setReferenceDocument(KdeReferenceDocument referenceDocument);

        Boolean getActive();

        void setActive(Boolean active);

    }

    interface CreateReceivingEvent extends CreateOrMergePatchReceivingEvent
    {
    }

    interface MergePatchReceivingEvent extends CreateOrMergePatchReceivingEvent
    {
        Boolean getIsPropertyTraceabilityLotCodeRemoved();

        void setIsPropertyTraceabilityLotCodeRemoved(Boolean removed);

        Boolean getIsPropertyQuantityAndUomRemoved();

        void setIsPropertyQuantityAndUomRemoved(Boolean removed);

        Boolean getIsPropertyProductDescriptionRemoved();

        void setIsPropertyProductDescriptionRemoved(Boolean removed);

        Boolean getIsPropertyShipToLocationRemoved();

        void setIsPropertyShipToLocationRemoved(Boolean removed);

        Boolean getIsPropertyShipFromLocationRemoved();

        void setIsPropertyShipFromLocationRemoved(Boolean removed);

        Boolean getIsPropertyReceiveDateRemoved();

        void setIsPropertyReceiveDateRemoved(Boolean removed);

        Boolean getIsPropertyTlcSourceOrTlcSourceReferenceRemoved();

        void setIsPropertyTlcSourceOrTlcSourceReferenceRemoved(Boolean removed);

        Boolean getIsPropertyReferenceDocumentRemoved();

        void setIsPropertyReferenceDocumentRemoved(Boolean removed);

        Boolean getIsPropertyActiveRemoved();

        void setIsPropertyActiveRemoved(Boolean removed);


    }

    interface DeleteReceivingEvent extends ReceivingEventCommand
    {
    }

}
