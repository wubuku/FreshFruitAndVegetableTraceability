// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.order;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface OrderContactMechState
{
    Long VERSION_ZERO = 0L;

    Long VERSION_NULL = VERSION_ZERO - 1;

    String getContactMechPurposeTypeId();

    String getContactMechId();

    Long getVersion();

    String getCreatedBy();

    OffsetDateTime getCreatedAt();

    String getUpdatedBy();

    OffsetDateTime getUpdatedAt();

    Boolean getDeleted();

    String getOrderId();

    interface MutableOrderContactMechState extends OrderContactMechState {
        void setContactMechPurposeTypeId(String contactMechPurposeTypeId);

        void setContactMechId(String contactMechId);

        void setVersion(Long version);

        void setCreatedBy(String createdBy);

        void setCreatedAt(OffsetDateTime createdAt);

        void setUpdatedBy(String updatedBy);

        void setUpdatedAt(OffsetDateTime updatedAt);

        void setDeleted(Boolean deleted);

        void setOrderId(String orderId);


        void mutate(Event e);

        //void when(OrderContactMechEvent.OrderContactMechStateCreated e);

        //void when(OrderContactMechEvent.OrderContactMechStateMergePatched e);

        //void when(OrderContactMechEvent.OrderContactMechStateRemoved e);
    }

    interface SqlOrderContactMechState extends MutableOrderContactMechState {
        OrderContactMechId getOrderContactMechId();

        void setOrderContactMechId(OrderContactMechId orderContactMechId);


        boolean isStateUnsaved();

        boolean getForReapplying();
    }
}
