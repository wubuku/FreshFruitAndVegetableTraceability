// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.lot;


public class RemoveLotIdentificationDto extends CreateOrMergePatchLotIdentificationDto implements LotIdentificationCommand.RemoveLotIdentification {

    public RemoveLotIdentificationDto() {
        this.commandType = COMMAND_TYPE_REMOVE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_REMOVE;
    }

    public LotIdentificationCommand.RemoveLotIdentification toRemoveLotIdentification()
    {
        AbstractLotIdentificationCommand.SimpleRemoveLotIdentification command = new AbstractLotIdentificationCommand.SimpleRemoveLotIdentification();
        ((AbstractLotIdentificationCommandDto)this).copyTo(command);
        return command;
    }
}
