// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.orderadjustmenttype;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.specialization.DomainError;

public interface OrderAdjustmentTypeCommand extends Command {

    String getOrderAdjustmentTypeId();

    void setOrderAdjustmentTypeId(String orderAdjustmentTypeId);

    Long getVersion();

    void setVersion(Long version);

    static void throwOnInvalidStateTransition(OrderAdjustmentTypeState state, Command c) {
        if (state.getVersion() == null) {
            if (isCreationCommand((OrderAdjustmentTypeCommand)c)) {
                return;
            }
            throw DomainError.named("premature", "Can't do anything to unexistent aggregate");
        }
        if (state.getDeleted() != null && state.getDeleted()) {
            throw DomainError.named("zombie", "Can't do anything to deleted aggregate.");
        }
        if (isCreationCommand((OrderAdjustmentTypeCommand)c))
            throw DomainError.named("rebirth", "Can't create aggregate that already exists");
    }

    static boolean isCreationCommand(OrderAdjustmentTypeCommand c) {
        if ((c instanceof OrderAdjustmentTypeCommand.CreateOrderAdjustmentType) 
            && (COMMAND_TYPE_CREATE.equals(c.getCommandType()) || c.getVersion().equals(OrderAdjustmentTypeState.VERSION_NULL)))
            return true;
        if ((c instanceof OrderAdjustmentTypeCommand.MergePatchOrderAdjustmentType))
            return false;
        if ((c instanceof OrderAdjustmentTypeCommand.DeleteOrderAdjustmentType))
            return false;
        if (c.getCommandType() != null) {
            String commandType = c.getCommandType();
        }

        if (c.getVersion().equals(OrderAdjustmentTypeState.VERSION_NULL))
            return true;
        return false;
    }

    interface CreateOrMergePatchOrderAdjustmentType extends OrderAdjustmentTypeCommand
    {

        String getParentTypeId();

        void setParentTypeId(String parentTypeId);

        String getHasTable();

        void setHasTable(String hasTable);

        String getDescription();

        void setDescription(String description);

        Boolean getActive();

        void setActive(Boolean active);

    }

    interface CreateOrderAdjustmentType extends CreateOrMergePatchOrderAdjustmentType
    {
    }

    interface MergePatchOrderAdjustmentType extends CreateOrMergePatchOrderAdjustmentType
    {
        Boolean getIsPropertyParentTypeIdRemoved();

        void setIsPropertyParentTypeIdRemoved(Boolean removed);

        Boolean getIsPropertyHasTableRemoved();

        void setIsPropertyHasTableRemoved(Boolean removed);

        Boolean getIsPropertyDescriptionRemoved();

        void setIsPropertyDescriptionRemoved(Boolean removed);

        Boolean getIsPropertyActiveRemoved();

        void setIsPropertyActiveRemoved(Boolean removed);


    }

    interface DeleteOrderAdjustmentType extends OrderAdjustmentTypeCommand
    {
    }

}
