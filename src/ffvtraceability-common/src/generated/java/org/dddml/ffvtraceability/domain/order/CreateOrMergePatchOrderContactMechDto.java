// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.order;

import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public class CreateOrMergePatchOrderContactMechDto extends AbstractOrderContactMechCommandDto implements OrderContactMechCommand.CreateOrMergePatchOrderContactMech {

    /**
     * Contact Mech Id
     */
    private String contactMechId;

    public String getContactMechId()
    {
        return this.contactMechId;
    }

    public void setContactMechId(String contactMechId)
    {
        this.contactMechId = contactMechId;
    }


    private Boolean isPropertyContactMechIdRemoved;

    public Boolean getIsPropertyContactMechIdRemoved()
    {
        return this.isPropertyContactMechIdRemoved;
    }

    public void setIsPropertyContactMechIdRemoved(Boolean removed)
    {
        this.isPropertyContactMechIdRemoved = removed;
    }

    public void copyTo(CreateOrMergePatchOrderContactMech command)
    {
        ((AbstractOrderContactMechCommandDto) this).copyTo(command);
        command.setContactMechId(this.getContactMechId());
    }

    public OrderContactMechCommand toCommand()
    {
        if (getCommandType() == null) {
            setCommandType(COMMAND_TYPE_MERGE_PATCH);
        }
        if (COMMAND_TYPE_CREATE.equals(getCommandType())) {
            AbstractOrderContactMechCommand.SimpleCreateOrderContactMech command = new AbstractOrderContactMechCommand.SimpleCreateOrderContactMech();
            copyTo((AbstractOrderContactMechCommand.AbstractCreateOrderContactMech) command);
            return command;
        } else if (COMMAND_TYPE_MERGE_PATCH.equals(getCommandType())) {
            AbstractOrderContactMechCommand.SimpleMergePatchOrderContactMech command = new AbstractOrderContactMechCommand.SimpleMergePatchOrderContactMech();
            copyTo((AbstractOrderContactMechCommand.SimpleMergePatchOrderContactMech) command);
            return command;
        } 
        else if (COMMAND_TYPE_REMOVE.equals(getCommandType())) {
            AbstractOrderContactMechCommand.SimpleRemoveOrderContactMech command = new AbstractOrderContactMechCommand.SimpleRemoveOrderContactMech();
            ((AbstractOrderContactMechCommandDto) this).copyTo(command);
            return command;
        }
        throw new UnsupportedOperationException("Unknown command type:" + getCommandType());
    }


    public OrderContactMechCommand toSubclass() {
        if (getCommandType() == null) {
            setCommandType(COMMAND_TYPE_MERGE_PATCH);
        }
        if (COMMAND_TYPE_CREATE.equals(getCommandType()) || null == getCommandType()) {
            CreateOrderContactMechDto command = new CreateOrderContactMechDto();
            copyTo((CreateOrderContactMech) command);
            return command;
        } else if (COMMAND_TYPE_MERGE_PATCH.equals(getCommandType())) {
            MergePatchOrderContactMechDto command = new MergePatchOrderContactMechDto();
            copyTo((MergePatchOrderContactMech) command);
            return command;
        } 
        else if (COMMAND_TYPE_REMOVE.equals(getCommandType())) {
            RemoveOrderContactMechDto command = new RemoveOrderContactMechDto();
            ((AbstractOrderContactMechCommandDto) this).copyTo(command);
            return command;
        }
        throw new UnsupportedOperationException("Unknown command type:" + getCommandType());
    }

    public void copyTo(CreateOrderContactMech command)
    {
        copyTo((CreateOrMergePatchOrderContactMech) command);
    }

    public void copyTo(MergePatchOrderContactMech command)
    {
        copyTo((CreateOrMergePatchOrderContactMech) command);
        command.setIsPropertyContactMechIdRemoved(this.getIsPropertyContactMechIdRemoved());
    }

    public static class CreateOrderContactMechDto extends CreateOrMergePatchOrderContactMechDto implements OrderContactMechCommand.CreateOrderContactMech
    {
        public CreateOrderContactMechDto() {
            this.commandType = COMMAND_TYPE_CREATE;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }
        public OrderContactMechCommand.CreateOrderContactMech toCreateOrderContactMech()
        {
            return (OrderContactMechCommand.CreateOrderContactMech) toCommand();
        }

    }

    public static class MergePatchOrderContactMechDto extends CreateOrMergePatchOrderContactMechDto implements OrderContactMechCommand.MergePatchOrderContactMech
    {
        public MergePatchOrderContactMechDto() {
            this.commandType = COMMAND_TYPE_MERGE_PATCH;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_MERGE_PATCH;
        }
        public OrderContactMechCommand.MergePatchOrderContactMech toMergePatchOrderContactMech()
        {
            return (OrderContactMechCommand.MergePatchOrderContactMech) toCommand();
        }

    }

}

