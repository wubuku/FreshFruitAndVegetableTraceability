// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.statusitem;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.specialization.DomainError;

public interface StatusItemCommand extends Command {

    String getStatusId();

    void setStatusId(String statusId);

    Long getVersion();

    void setVersion(Long version);

    static void throwOnInvalidStateTransition(StatusItemState state, Command c) {
        if (state.getVersion() == null) {
            if (isCreationCommand((StatusItemCommand)c)) {
                return;
            }
            throw DomainError.named("premature", "Can't do anything to unexistent aggregate");
        }
        if (isCreationCommand((StatusItemCommand)c))
            throw DomainError.named("rebirth", "Can't create aggregate that already exists");
    }

    static boolean isCreationCommand(StatusItemCommand c) {
        if ((c instanceof StatusItemCommand.CreateStatusItem) 
            && (COMMAND_TYPE_CREATE.equals(c.getCommandType()) || c.getVersion().equals(StatusItemState.VERSION_NULL)))
            return true;
        if ((c instanceof StatusItemCommand.MergePatchStatusItem))
            return false;
        if (c.getCommandType() != null) {
            String commandType = c.getCommandType();
        }

        if (c.getVersion().equals(StatusItemState.VERSION_NULL))
            return true;
        return false;
    }

    interface CreateOrMergePatchStatusItem extends StatusItemCommand {
        String getStatusTypeId();

        void setStatusTypeId(String statusTypeId);

        String getStatusCode();

        void setStatusCode(String statusCode);

        String getSequenceId();

        void setSequenceId(String sequenceId);

        String getDescription();

        void setDescription(String description);

    }

    interface CreateStatusItem extends CreateOrMergePatchStatusItem {
    }

    interface MergePatchStatusItem extends CreateOrMergePatchStatusItem {
        Boolean getIsPropertyStatusTypeIdRemoved();

        void setIsPropertyStatusTypeIdRemoved(Boolean removed);

        Boolean getIsPropertyStatusCodeRemoved();

        void setIsPropertyStatusCodeRemoved(Boolean removed);

        Boolean getIsPropertySequenceIdRemoved();

        void setIsPropertySequenceIdRemoved(Boolean removed);

        Boolean getIsPropertyDescriptionRemoved();

        void setIsPropertyDescriptionRemoved(Boolean removed);


    }

    interface DeleteStatusItem extends StatusItemCommand {
    }

}

