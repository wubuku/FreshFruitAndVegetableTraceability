// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.facility;


public class DeleteFacilityDto extends AbstractFacilityCommandDto implements FacilityCommand.DeleteFacility {

    public DeleteFacilityDto() {
        this.commandType = COMMAND_TYPE_DELETE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_DELETE;
    }

    public FacilityCommand.DeleteFacility toDeleteFacility()
    {
        AbstractFacilityCommand.SimpleDeleteFacility command = new AbstractFacilityCommand.SimpleDeleteFacility();
        ((AbstractFacilityCommandDto)this).copyTo(command);
        return command;
    }
}
