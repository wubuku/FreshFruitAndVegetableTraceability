// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.inventoryitemtype;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.specialization.DomainError;

public interface InventoryItemTypeCommand extends Command {

    String getInventoryItemTypeId();

    void setInventoryItemTypeId(String inventoryItemTypeId);

    Long getVersion();

    void setVersion(Long version);

    static void throwOnInvalidStateTransition(InventoryItemTypeState state, Command c) {
        if (state.getVersion() == null) {
            if (isCreationCommand((InventoryItemTypeCommand)c)) {
                return;
            }
            throw DomainError.named("premature", "Can't do anything to unexistent aggregate");
        }
        if (isCreationCommand((InventoryItemTypeCommand)c))
            throw DomainError.named("rebirth", "Can't create aggregate that already exists");
    }

    static boolean isCreationCommand(InventoryItemTypeCommand c) {
        if ((c instanceof InventoryItemTypeCommand.CreateInventoryItemType) 
            && (COMMAND_TYPE_CREATE.equals(c.getCommandType()) || c.getVersion().equals(InventoryItemTypeState.VERSION_NULL)))
            return true;
        if ((c instanceof InventoryItemTypeCommand.MergePatchInventoryItemType))
            return false;
        if (c.getCommandType() != null) {
            String commandType = c.getCommandType();
        }

        if (c.getVersion().equals(InventoryItemTypeState.VERSION_NULL))
            return true;
        return false;
    }

    interface CreateOrMergePatchInventoryItemType extends InventoryItemTypeCommand {
        String getParentTypeId();

        void setParentTypeId(String parentTypeId);

        String getHasTable();

        void setHasTable(String hasTable);

        String getDescription();

        void setDescription(String description);

    }

    interface CreateInventoryItemType extends CreateOrMergePatchInventoryItemType {
    }

    interface MergePatchInventoryItemType extends CreateOrMergePatchInventoryItemType {
        Boolean getIsPropertyParentTypeIdRemoved();

        void setIsPropertyParentTypeIdRemoved(Boolean removed);

        Boolean getIsPropertyHasTableRemoved();

        void setIsPropertyHasTableRemoved(Boolean removed);

        Boolean getIsPropertyDescriptionRemoved();

        void setIsPropertyDescriptionRemoved(Boolean removed);


    }

    interface DeleteInventoryItemType extends InventoryItemTypeCommand {
    }

}

