// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.party;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface OrganizationState extends PartyState
{
    String getOrganizationName();

    interface MutableOrganizationState extends OrganizationState, PartyState.MutablePartyState {
        void setOrganizationName(String organizationName);

        //void when(OrganizationEvent.OrganizationStateCreated e);

        //void when(OrganizationEvent.OrganizationStateMergePatched e);

    }

    interface SqlOrganizationState extends MutableOrganizationState, PartyState.SqlPartyState {
    }
}

