// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.partyrole;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface PartyRoleState
{
    Long VERSION_ZERO = 0L;

    Long VERSION_NULL = VERSION_ZERO - 1;

    PartyRoleId getPartyRoleId();

    Long getVersion();

    String getCreatedBy();

    OffsetDateTime getCreatedAt();

    String getUpdatedBy();

    OffsetDateTime getUpdatedAt();

    Boolean getDeleted();

    interface MutablePartyRoleState extends PartyRoleState {
        void setPartyRoleId(PartyRoleId partyRoleId);

        void setVersion(Long version);

        void setCreatedBy(String createdBy);

        void setCreatedAt(OffsetDateTime createdAt);

        void setUpdatedBy(String updatedBy);

        void setUpdatedAt(OffsetDateTime updatedAt);

        void setDeleted(Boolean deleted);


        void mutate(Event e);

        //void when(PartyRoleEvent.PartyRoleStateCreated e);

        //void when(PartyRoleEvent.PartyRoleStateMergePatched e);

        //void when(PartyRoleEvent.PartyRoleStateDeleted e);
    }

    interface SqlPartyRoleState extends MutablePartyRoleState {

        boolean isStateUnsaved();

        boolean getForReapplying();
    }
}
