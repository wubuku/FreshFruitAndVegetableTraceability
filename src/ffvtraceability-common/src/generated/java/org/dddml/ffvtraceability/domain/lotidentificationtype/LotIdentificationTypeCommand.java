// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.lotidentificationtype;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.specialization.DomainError;

public interface LotIdentificationTypeCommand extends Command {

    String getLotIdentificationTypeId();

    void setLotIdentificationTypeId(String lotIdentificationTypeId);

    Long getVersion();

    void setVersion(Long version);

    static void throwOnInvalidStateTransition(LotIdentificationTypeState state, Command c) {
        if (state.getVersion() == null) {
            if (isCreationCommand((LotIdentificationTypeCommand)c)) {
                return;
            }
            throw DomainError.named("premature", "Can't do anything to unexistent aggregate");
        }
        if (state.getDeleted() != null && state.getDeleted()) {
            throw DomainError.named("zombie", "Can't do anything to deleted aggregate.");
        }
        if (isCreationCommand((LotIdentificationTypeCommand)c))
            throw DomainError.named("rebirth", "Can't create aggregate that already exists");
    }

    static boolean isCreationCommand(LotIdentificationTypeCommand c) {
        if ((c instanceof LotIdentificationTypeCommand.CreateLotIdentificationType) 
            && (COMMAND_TYPE_CREATE.equals(c.getCommandType()) || c.getVersion().equals(LotIdentificationTypeState.VERSION_NULL)))
            return true;
        if ((c instanceof LotIdentificationTypeCommand.MergePatchLotIdentificationType))
            return false;
        if ((c instanceof LotIdentificationTypeCommand.DeleteLotIdentificationType))
            return false;
        if (c.getCommandType() != null) {
            String commandType = c.getCommandType();
        }

        if (c.getVersion().equals(LotIdentificationTypeState.VERSION_NULL))
            return true;
        return false;
    }

    interface CreateOrMergePatchLotIdentificationType extends LotIdentificationTypeCommand
    {

        String getDescription();

        void setDescription(String description);

        Boolean getActive();

        void setActive(Boolean active);

    }

    interface CreateLotIdentificationType extends CreateOrMergePatchLotIdentificationType
    {
    }

    interface MergePatchLotIdentificationType extends CreateOrMergePatchLotIdentificationType
    {
        Boolean getIsPropertyDescriptionRemoved();

        void setIsPropertyDescriptionRemoved(Boolean removed);

        Boolean getIsPropertyActiveRemoved();

        void setIsPropertyActiveRemoved(Boolean removed);


    }

    interface DeleteLotIdentificationType extends LotIdentificationTypeCommand
    {
    }

}
