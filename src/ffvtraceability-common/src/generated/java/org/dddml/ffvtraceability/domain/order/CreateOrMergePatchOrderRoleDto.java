// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.order;

import org.dddml.ffvtraceability.domain.partyrole.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public class CreateOrMergePatchOrderRoleDto extends AbstractOrderRoleCommandDto implements OrderRoleCommand.CreateOrMergePatchOrderRole {

    /**
     * Active
     */
    private Boolean active;

    public Boolean getActive()
    {
        return this.active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }


    private Boolean isPropertyActiveRemoved;

    public Boolean getIsPropertyActiveRemoved()
    {
        return this.isPropertyActiveRemoved;
    }

    public void setIsPropertyActiveRemoved(Boolean removed)
    {
        this.isPropertyActiveRemoved = removed;
    }

    public void copyTo(CreateOrMergePatchOrderRole command)
    {
        ((AbstractOrderRoleCommandDto) this).copyTo(command);
        command.setActive(this.getActive());
    }

    public OrderRoleCommand toCommand()
    {
        if (getCommandType() == null) {
            setCommandType(COMMAND_TYPE_MERGE_PATCH);
        }
        if (COMMAND_TYPE_CREATE.equals(getCommandType())) {
            AbstractOrderRoleCommand.SimpleCreateOrderRole command = new AbstractOrderRoleCommand.SimpleCreateOrderRole();
            copyTo((AbstractOrderRoleCommand.AbstractCreateOrderRole) command);
            return command;
        } else if (COMMAND_TYPE_MERGE_PATCH.equals(getCommandType())) {
            AbstractOrderRoleCommand.SimpleMergePatchOrderRole command = new AbstractOrderRoleCommand.SimpleMergePatchOrderRole();
            copyTo((AbstractOrderRoleCommand.SimpleMergePatchOrderRole) command);
            return command;
        } 
        else if (COMMAND_TYPE_REMOVE.equals(getCommandType())) {
            AbstractOrderRoleCommand.SimpleRemoveOrderRole command = new AbstractOrderRoleCommand.SimpleRemoveOrderRole();
            ((AbstractOrderRoleCommandDto) this).copyTo(command);
            return command;
        }
        throw new UnsupportedOperationException("Unknown command type:" + getCommandType());
    }


    public OrderRoleCommand toSubclass() {
        if (getCommandType() == null) {
            setCommandType(COMMAND_TYPE_MERGE_PATCH);
        }
        if (COMMAND_TYPE_CREATE.equals(getCommandType()) || null == getCommandType()) {
            CreateOrderRoleDto command = new CreateOrderRoleDto();
            copyTo((CreateOrderRole) command);
            return command;
        } else if (COMMAND_TYPE_MERGE_PATCH.equals(getCommandType())) {
            MergePatchOrderRoleDto command = new MergePatchOrderRoleDto();
            copyTo((MergePatchOrderRole) command);
            return command;
        } 
        else if (COMMAND_TYPE_REMOVE.equals(getCommandType())) {
            RemoveOrderRoleDto command = new RemoveOrderRoleDto();
            ((AbstractOrderRoleCommandDto) this).copyTo(command);
            return command;
        }
        throw new UnsupportedOperationException("Unknown command type:" + getCommandType());
    }

    public void copyTo(CreateOrderRole command)
    {
        copyTo((CreateOrMergePatchOrderRole) command);
    }

    public void copyTo(MergePatchOrderRole command)
    {
        copyTo((CreateOrMergePatchOrderRole) command);
        command.setIsPropertyActiveRemoved(this.getIsPropertyActiveRemoved());
    }

    public static class CreateOrderRoleDto extends CreateOrMergePatchOrderRoleDto implements OrderRoleCommand.CreateOrderRole
    {
        public CreateOrderRoleDto() {
            this.commandType = COMMAND_TYPE_CREATE;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }
        public OrderRoleCommand.CreateOrderRole toCreateOrderRole()
        {
            return (OrderRoleCommand.CreateOrderRole) toCommand();
        }

    }

    public static class MergePatchOrderRoleDto extends CreateOrMergePatchOrderRoleDto implements OrderRoleCommand.MergePatchOrderRole
    {
        public MergePatchOrderRoleDto() {
            this.commandType = COMMAND_TYPE_MERGE_PATCH;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_MERGE_PATCH;
        }
        public OrderRoleCommand.MergePatchOrderRole toMergePatchOrderRole()
        {
            return (OrderRoleCommand.MergePatchOrderRole) toCommand();
        }

    }

}
