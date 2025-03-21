// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.workeffortassoc;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.specialization.DomainError;

public interface WorkEffortAssocCommand extends Command {

    WorkEffortAssocId getWorkEffortAssocId();

    void setWorkEffortAssocId(WorkEffortAssocId workEffortAssocId);

    Long getVersion();

    void setVersion(Long version);

    static void throwOnInvalidStateTransition(WorkEffortAssocState state, Command c) {
        if (state.getVersion() == null) {
            if (isCreationCommand((WorkEffortAssocCommand)c)) {
                return;
            }
            throw DomainError.named("premature", "Can't do anything to unexistent aggregate");
        }
        if (isCreationCommand((WorkEffortAssocCommand)c))
            throw DomainError.named("rebirth", "Can't create aggregate that already exists");
    }

    static boolean isCreationCommand(WorkEffortAssocCommand c) {
        if ((c instanceof WorkEffortAssocCommand.CreateWorkEffortAssoc) 
            && (COMMAND_TYPE_CREATE.equals(c.getCommandType()) || c.getVersion().equals(WorkEffortAssocState.VERSION_NULL)))
            return true;
        if ((c instanceof WorkEffortAssocCommand.MergePatchWorkEffortAssoc))
            return false;
        if (c.getCommandType() != null) {
            String commandType = c.getCommandType();
        }

        if (c.getVersion().equals(WorkEffortAssocState.VERSION_NULL))
            return true;
        return false;
    }

    interface CreateOrMergePatchWorkEffortAssoc extends WorkEffortAssocCommand {
        Long getSequenceNum();

        void setSequenceNum(Long sequenceNum);

        OffsetDateTime getThruDate();

        void setThruDate(OffsetDateTime thruDate);

    }

    interface CreateWorkEffortAssoc extends CreateOrMergePatchWorkEffortAssoc {
    }

    interface MergePatchWorkEffortAssoc extends CreateOrMergePatchWorkEffortAssoc {
        Boolean getIsPropertySequenceNumRemoved();

        void setIsPropertySequenceNumRemoved(Boolean removed);

        Boolean getIsPropertyThruDateRemoved();

        void setIsPropertyThruDateRemoved(Boolean removed);


    }

    interface DeleteWorkEffortAssoc extends WorkEffortAssocCommand {
    }

}

