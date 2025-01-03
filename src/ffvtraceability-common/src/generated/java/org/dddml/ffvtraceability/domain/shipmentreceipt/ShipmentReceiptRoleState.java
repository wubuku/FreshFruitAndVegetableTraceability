// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.shipmentreceipt;

import java.util.*;
import java.math.*;
import org.dddml.ffvtraceability.domain.partyrole.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface ShipmentReceiptRoleState
{
    Long VERSION_ZERO = 0L;

    Long VERSION_NULL = VERSION_ZERO - 1;

    PartyRoleId getPartyRoleId();

    Long getVersion();

    String getCreatedBy();

    OffsetDateTime getCreatedAt();

    String getUpdatedBy();

    OffsetDateTime getUpdatedAt();

    String getShipmentReceiptReceiptId();

    interface MutableShipmentReceiptRoleState extends ShipmentReceiptRoleState {
        void setPartyRoleId(PartyRoleId partyRoleId);

        void setVersion(Long version);

        void setCreatedBy(String createdBy);

        void setCreatedAt(OffsetDateTime createdAt);

        void setUpdatedBy(String updatedBy);

        void setUpdatedAt(OffsetDateTime updatedAt);

        void setShipmentReceiptReceiptId(String shipmentReceiptReceiptId);


        void mutate(Event e);

        //void when(ShipmentReceiptRoleEvent.ShipmentReceiptRoleStateCreated e);

        //void when(ShipmentReceiptRoleEvent.ShipmentReceiptRoleStateMergePatched e);

    }

    interface SqlShipmentReceiptRoleState extends MutableShipmentReceiptRoleState {
        ShipmentReceiptRoleId getShipmentReceiptRoleId();

        void setShipmentReceiptRoleId(ShipmentReceiptRoleId shipmentReceiptRoleId);


        boolean isStateUnsaved();

        boolean getForReapplying();
    }
}

