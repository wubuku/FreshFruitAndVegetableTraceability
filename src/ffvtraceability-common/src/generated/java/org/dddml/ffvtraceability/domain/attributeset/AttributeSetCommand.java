// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.attributeset;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.specialization.DomainError;

public interface AttributeSetCommand extends Command {

    String getAttributeSetId();

    void setAttributeSetId(String attributeSetId);

    Long getVersion();

    void setVersion(Long version);

    static void throwOnInvalidStateTransition(AttributeSetState state, Command c) {
        if (state.getVersion() == null) {
            if (isCreationCommand((AttributeSetCommand)c)) {
                return;
            }
            throw DomainError.named("premature", "Can't do anything to unexistent aggregate");
        }
        if (isCreationCommand((AttributeSetCommand)c))
            throw DomainError.named("rebirth", "Can't create aggregate that already exists");
    }

    static boolean isCreationCommand(AttributeSetCommand c) {
        if ((c instanceof AttributeSetCommand.CreateAttributeSet) 
            && (COMMAND_TYPE_CREATE.equals(c.getCommandType()) || c.getVersion().equals(AttributeSetState.VERSION_NULL)))
            return true;
        if ((c instanceof AttributeSetCommand.MergePatchAttributeSet))
            return false;
        if (c.getCommandType() != null) {
            String commandType = c.getCommandType();
        }

        if (c.getVersion().equals(AttributeSetState.VERSION_NULL))
            return true;
        return false;
    }

    interface CreateOrMergePatchAttributeSet extends AttributeSetCommand {
        String getAttributeSetName();

        void setAttributeSetName(String attributeSetName);

        String getDescription();

        void setDescription(String description);

    }

    interface CreateAttributeSet extends CreateOrMergePatchAttributeSet {
        CreateAttributeUseCommandCollection getCreateAttributeUseCommands();

        AttributeUseCommand.CreateAttributeUse newCreateAttributeUse();

    }

    interface MergePatchAttributeSet extends CreateOrMergePatchAttributeSet {
        Boolean getIsPropertyAttributeSetNameRemoved();

        void setIsPropertyAttributeSetNameRemoved(Boolean removed);

        Boolean getIsPropertyDescriptionRemoved();

        void setIsPropertyDescriptionRemoved(Boolean removed);


        AttributeUseCommandCollection getAttributeUseCommands();

        AttributeUseCommand.CreateAttributeUse newCreateAttributeUse();

        AttributeUseCommand.MergePatchAttributeUse newMergePatchAttributeUse();

        AttributeUseCommand.RemoveAttributeUse newRemoveAttributeUse();

    }

    interface DeleteAttributeSet extends AttributeSetCommand {
    }

    interface CreateAttributeUseCommandCollection extends Iterable<AttributeUseCommand.CreateAttributeUse> {
        void add(AttributeUseCommand.CreateAttributeUse c);

        void remove(AttributeUseCommand.CreateAttributeUse c);

        void clear();
    }

    interface AttributeUseCommandCollection extends Iterable<AttributeUseCommand> {
        void add(AttributeUseCommand c);

        void remove(AttributeUseCommand c);

        void clear();
    }

}

