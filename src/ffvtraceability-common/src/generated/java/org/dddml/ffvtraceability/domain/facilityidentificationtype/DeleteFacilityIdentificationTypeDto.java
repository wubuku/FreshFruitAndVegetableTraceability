// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.facilityidentificationtype;


public class DeleteFacilityIdentificationTypeDto extends AbstractFacilityIdentificationTypeCommandDto implements FacilityIdentificationTypeCommand.DeleteFacilityIdentificationType {

    public DeleteFacilityIdentificationTypeDto() {
        this.commandType = COMMAND_TYPE_DELETE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_DELETE;
    }

    public FacilityIdentificationTypeCommand.DeleteFacilityIdentificationType toDeleteFacilityIdentificationType()
    {
        AbstractFacilityIdentificationTypeCommand.SimpleDeleteFacilityIdentificationType command = new AbstractFacilityIdentificationTypeCommand.SimpleDeleteFacilityIdentificationType();
        ((AbstractFacilityIdentificationTypeCommandDto)this).copyTo(command);
        return command;
    }
}

