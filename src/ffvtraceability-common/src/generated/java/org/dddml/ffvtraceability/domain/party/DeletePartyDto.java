// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.party;


public class DeletePartyDto extends AbstractPartyCommandDto implements PartyCommand.DeleteParty {

    public DeletePartyDto() {
        this.commandType = COMMAND_TYPE_DELETE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_DELETE;
    }

    private String partyTypeId;

    public String getPartyTypeId() {
        return this.partyTypeId;
    }

    public void setPartyTypeId(String partyTypeId) {
        this.partyTypeId = partyTypeId;
    }

    public PartyCommand.DeleteParty toDeleteParty()
    {
        AbstractPartyCommand.SimpleDeleteParty command = new AbstractPartyCommand.SimpleDeleteParty();
        ((AbstractPartyCommandDto)this).copyTo(command);
        command.setPartyTypeId(this.getPartyTypeId());
        return command;
    }
}

