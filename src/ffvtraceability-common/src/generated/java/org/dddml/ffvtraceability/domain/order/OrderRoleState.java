// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.order;

import java.util.*;
import java.math.*;
import org.dddml.ffvtraceability.domain.partyrole.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface OrderRoleState
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

    String getOrderId();

    interface MutableOrderRoleState extends OrderRoleState {
        void setPartyRoleId(PartyRoleId partyRoleId);

        void setVersion(Long version);

        void setCreatedBy(String createdBy);

        void setCreatedAt(OffsetDateTime createdAt);

        void setUpdatedBy(String updatedBy);

        void setUpdatedAt(OffsetDateTime updatedAt);

        void setDeleted(Boolean deleted);

        void setOrderId(String orderId);


        void mutate(Event e);

        //void when(OrderRoleEvent.OrderRoleStateCreated e);

        //void when(OrderRoleEvent.OrderRoleStateMergePatched e);

        //void when(OrderRoleEvent.OrderRoleStateRemoved e);
    }

    interface SqlOrderRoleState extends MutableOrderRoleState {
        OrderRoleId getOrderRoleId();

        void setOrderRoleId(OrderRoleId orderRoleId);


        boolean isStateUnsaved();

        boolean getForReapplying();
    }
}
