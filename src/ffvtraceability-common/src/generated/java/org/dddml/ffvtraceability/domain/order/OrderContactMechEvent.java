// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.order;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface OrderContactMechEvent extends Event {

    interface SqlOrderContactMechEvent extends OrderContactMechEvent {
        OrderContactMechEventId getOrderContactMechEventId();

        boolean getEventReadOnly();

        void setEventReadOnly(boolean readOnly);
    }

    String getContactMechPurposeTypeId();

    //void setContactMechPurposeTypeId(String contactMechPurposeTypeId);

    String getCreatedBy();

    void setCreatedBy(String createdBy);

    OffsetDateTime getCreatedAt();

    void setCreatedAt(OffsetDateTime createdAt);

    String getCommandId();

    void setCommandId(String commandId);

    interface OrderContactMechStateEvent extends OrderContactMechEvent {
        Long getVersion();

        void setVersion(Long version);

        String getContactMechId();

        void setContactMechId(String contactMechId);

    }

    interface OrderContactMechStateCreated extends OrderContactMechStateEvent
    {
    
    }


    interface OrderContactMechStateMergePatched extends OrderContactMechStateEvent
    {
        Boolean getIsPropertyContactMechIdRemoved();

        void setIsPropertyContactMechIdRemoved(Boolean removed);



    }

    interface OrderContactMechStateRemoved extends OrderContactMechStateEvent
    {
    }


}
