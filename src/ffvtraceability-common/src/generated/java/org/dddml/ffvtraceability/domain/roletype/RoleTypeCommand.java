// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.roletype;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.specialization.DomainError;

public interface RoleTypeCommand extends Command {

    String getRoleTypeId();

    void setRoleTypeId(String roleTypeId);

    Long getVersion();

    void setVersion(Long version);

    static void throwOnInvalidStateTransition(RoleTypeState state, Command c) {
        if (state.getVersion() == null) {
            if (isCreationCommand((RoleTypeCommand)c)) {
                return;
            }
            throw DomainError.named("premature", "Can't do anything to unexistent aggregate");
        }
        if (isCreationCommand((RoleTypeCommand)c))
            throw DomainError.named("rebirth", "Can't create aggregate that already exists");
    }

    static boolean isCreationCommand(RoleTypeCommand c) {
        if ((c instanceof RoleTypeCommand.CreateRoleType) 
            && (COMMAND_TYPE_CREATE.equals(c.getCommandType()) || c.getVersion().equals(RoleTypeState.VERSION_NULL)))
            return true;
        if ((c instanceof RoleTypeCommand.MergePatchRoleType))
            return false;
        if (c.getCommandType() != null) {
            String commandType = c.getCommandType();
        }

        if (c.getVersion().equals(RoleTypeState.VERSION_NULL))
            return true;
        return false;
    }

    interface CreateOrMergePatchRoleType extends RoleTypeCommand {
        String getParentTypeId();

        void setParentTypeId(String parentTypeId);

        String getHasTable();

        void setHasTable(String hasTable);

        String getDescription();

        void setDescription(String description);

    }

    interface CreateRoleType extends CreateOrMergePatchRoleType {
    }

    interface MergePatchRoleType extends CreateOrMergePatchRoleType {
        Boolean getIsPropertyParentTypeIdRemoved();

        void setIsPropertyParentTypeIdRemoved(Boolean removed);

        Boolean getIsPropertyHasTableRemoved();

        void setIsPropertyHasTableRemoved(Boolean removed);

        Boolean getIsPropertyDescriptionRemoved();

        void setIsPropertyDescriptionRemoved(Boolean removed);


    }

    interface DeleteRoleType extends RoleTypeCommand {
    }

}

