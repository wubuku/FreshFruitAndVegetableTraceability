// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.partycontactmech;

import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.AbstractCommand;

public abstract class AbstractPartyContactMechCommandDto extends AbstractCommand {

    /**
     * Party Contact Mech Id
     */
    private PartyContactMechId partyContactMechId;

    public PartyContactMechId getPartyContactMechId()
    {
        return this.partyContactMechId;
    }

    public void setPartyContactMechId(PartyContactMechId partyContactMechId)
    {
        this.partyContactMechId = partyContactMechId;
    }

    /**
     * Version
     */
    private Long version;

    public Long getVersion()
    {
        return this.version;
    }

    public void setVersion(Long version)
    {
        this.version = version;
    }


    public void copyTo(PartyContactMechCommand command) {
        command.setPartyContactMechId(this.getPartyContactMechId());
        command.setVersion(this.getVersion());
        
        command.setRequesterId(this.getRequesterId());
        command.setCommandId(this.getCommandId());
    }

}
