// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.partyrole;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.specialization.DomainError;

public interface PartyRoleCommand extends Command {

    PartyRoleId getPartyRoleId();

    void setPartyRoleId(PartyRoleId partyRoleId);

    Long getVersion();

    void setVersion(Long version);

    static void throwOnInvalidStateTransition(PartyRoleState state, Command c) {
        if (state.getVersion() == null) {
            if (isCreationCommand((PartyRoleCommand)c)) {
                return;
            }
            throw DomainError.named("premature", "Can't do anything to unexistent aggregate");
        }
        if (state.getDeleted() != null && state.getDeleted()) {
            throw DomainError.named("zombie", "Can't do anything to deleted aggregate.");
        }
        if (isCreationCommand((PartyRoleCommand)c))
            throw DomainError.named("rebirth", "Can't create aggregate that already exists");
    }

    static boolean isCreationCommand(PartyRoleCommand c) {
        if ((c instanceof PartyRoleCommand.CreatePartyRole) 
            && (COMMAND_TYPE_CREATE.equals(c.getCommandType()) || c.getVersion().equals(PartyRoleState.VERSION_NULL)))
            return true;
        if ((c instanceof PartyRoleCommand.MergePatchPartyRole))
            return false;
        if ((c instanceof PartyRoleCommand.DeletePartyRole))
            return false;
        if (c.getCommandType() != null) {
            String commandType = c.getCommandType();
        }

        if (c.getVersion().equals(PartyRoleState.VERSION_NULL))
            return true;
        return false;
    }

    interface CreateOrMergePatchPartyRole extends PartyRoleCommand
    {

        Boolean getActive();

        void setActive(Boolean active);

    }

    interface CreatePartyRole extends CreateOrMergePatchPartyRole
    {
    }

    interface MergePatchPartyRole extends CreateOrMergePatchPartyRole
    {
        Boolean getIsPropertyActiveRemoved();

        void setIsPropertyActiveRemoved(Boolean removed);


    }

    interface DeletePartyRole extends PartyRoleCommand
    {
    }

}
