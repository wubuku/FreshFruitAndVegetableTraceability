// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.product;


public class RemoveGoodIdentificationDto extends CreateOrMergePatchGoodIdentificationDto implements GoodIdentificationCommand.RemoveGoodIdentification {

    public RemoveGoodIdentificationDto() {
        this.commandType = COMMAND_TYPE_REMOVE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_REMOVE;
    }

    public GoodIdentificationCommand.RemoveGoodIdentification toRemoveGoodIdentification()
    {
        AbstractGoodIdentificationCommand.SimpleRemoveGoodIdentification command = new AbstractGoodIdentificationCommand.SimpleRemoveGoodIdentification();
        ((AbstractGoodIdentificationCommandDto)this).copyTo(command);
        return command;
    }
}

