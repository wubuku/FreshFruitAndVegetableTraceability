// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.party;


public class RemovePartyIdentificationDto extends CreateOrMergePatchPartyIdentificationDto implements PartyIdentificationCommand.RemovePartyIdentification {

    public RemovePartyIdentificationDto() {
        this.commandType = COMMAND_TYPE_REMOVE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_REMOVE;
    }

    public PartyIdentificationCommand.RemovePartyIdentification toRemovePartyIdentification()
    {
        AbstractPartyIdentificationCommand.SimpleRemovePartyIdentification command = new AbstractPartyIdentificationCommand.SimpleRemovePartyIdentification();
        ((AbstractPartyIdentificationCommandDto)this).copyTo(command);
        return command;
    }
}
