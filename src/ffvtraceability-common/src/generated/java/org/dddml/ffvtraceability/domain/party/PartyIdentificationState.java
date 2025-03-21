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

public interface PartyIdentificationState
{
    Long VERSION_ZERO = 0L;

    Long VERSION_NULL = VERSION_ZERO - 1;

    String getPartyIdentificationTypeId();

    String getIdValue();

    String getVerified();

    Long getVersion();

    String getCreatedBy();

    OffsetDateTime getCreatedAt();

    String getUpdatedBy();

    OffsetDateTime getUpdatedAt();

    Boolean get__Deleted__();

    String getPartyId();

    interface MutablePartyIdentificationState extends PartyIdentificationState {
        void setPartyIdentificationTypeId(String partyIdentificationTypeId);

        void setIdValue(String idValue);

        void setVerified(String verified);

        void setVersion(Long version);

        void setCreatedBy(String createdBy);

        void setCreatedAt(OffsetDateTime createdAt);

        void setUpdatedBy(String updatedBy);

        void setUpdatedAt(OffsetDateTime updatedAt);

        void set__Deleted__(Boolean __Deleted__);

        void setPartyId(String partyId);


        void mutate(Event e);

        //void when(PartyIdentificationEvent.PartyIdentificationStateCreated e);

        //void when(PartyIdentificationEvent.PartyIdentificationStateMergePatched e);

        //void when(PartyIdentificationEvent.PartyIdentificationStateRemoved e);
    }

    interface SqlPartyIdentificationState extends MutablePartyIdentificationState {
        PartyIdentificationId getPartyIdentificationId();

        void setPartyIdentificationId(PartyIdentificationId partyIdentificationId);


        boolean isStateUnsaved();

        boolean getForReapplying();
    }
}

