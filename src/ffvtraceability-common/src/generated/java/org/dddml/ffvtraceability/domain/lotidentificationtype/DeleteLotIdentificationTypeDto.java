// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.lotidentificationtype;


public class DeleteLotIdentificationTypeDto extends AbstractLotIdentificationTypeCommandDto implements LotIdentificationTypeCommand.DeleteLotIdentificationType {

    public DeleteLotIdentificationTypeDto() {
        this.commandType = COMMAND_TYPE_DELETE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_DELETE;
    }

    public LotIdentificationTypeCommand.DeleteLotIdentificationType toDeleteLotIdentificationType()
    {
        AbstractLotIdentificationTypeCommand.SimpleDeleteLotIdentificationType command = new AbstractLotIdentificationTypeCommand.SimpleDeleteLotIdentificationType();
        ((AbstractLotIdentificationTypeCommandDto)this).copyTo(command);
        return command;
    }
}

