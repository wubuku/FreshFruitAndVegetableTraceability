// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.uomtype;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.specialization.DomainError;

public interface UomTypeCommand extends Command {

    String getUomTypeId();

    void setUomTypeId(String uomTypeId);

    Long getVersion();

    void setVersion(Long version);

    static void throwOnInvalidStateTransition(UomTypeState state, Command c) {
        if (state.getVersion() == null) {
            if (isCommandCreate((UomTypeCommand)c)) {
                return;
            }
            throw DomainError.named("premature", "Can't do anything to unexistent aggregate");
        }
        if (state.getDeleted() != null && state.getDeleted()) {
            throw DomainError.named("zombie", "Can't do anything to deleted aggregate.");
        }
        if (isCommandCreate((UomTypeCommand)c))
            throw DomainError.named("rebirth", "Can't create aggregate that already exists");
    }

    static boolean isCommandCreate(UomTypeCommand c) {
        if ((c instanceof UomTypeCommand.CreateUomType) 
            && (COMMAND_TYPE_CREATE.equals(c.getCommandType()) || c.getVersion().equals(UomTypeState.VERSION_NULL)))
            return true;
        if ((c instanceof UomTypeCommand.MergePatchUomType))
            return false;
        if ((c instanceof UomTypeCommand.DeleteUomType))
            return false;
        if (c.getVersion().equals(UomTypeState.VERSION_NULL))
            return true;
        return false;
    }

    interface CreateOrMergePatchUomType extends UomTypeCommand
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

    interface CreateUomType extends CreateOrMergePatchUomType
    {
    }

    interface MergePatchUomType extends CreateOrMergePatchUomType
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

    interface DeleteUomType extends UomTypeCommand
    {
    }

}
