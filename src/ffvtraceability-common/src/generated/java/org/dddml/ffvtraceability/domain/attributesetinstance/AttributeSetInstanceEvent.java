// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.attributesetinstance;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface AttributeSetInstanceEvent extends Event {

    interface SqlAttributeSetInstanceEvent extends AttributeSetInstanceEvent {
        AttributeSetInstanceEventId getAttributeSetInstanceEventId();

        boolean getEventReadOnly();

        void setEventReadOnly(boolean readOnly);
    }

    String getAttributeSetInstanceId();

    //void setAttributeSetInstanceId(String attributeSetInstanceId);

    Long getVersion();
    
    //void setVersion(Long version);

    String getCreatedBy();

    void setCreatedBy(String createdBy);

    OffsetDateTime getCreatedAt();

    void setCreatedAt(OffsetDateTime createdAt);

    String getCommandId();

    void setCommandId(String commandId);

    String getTenantId();

    void setTenantId(String tenantId);

    interface AttributeSetInstanceStateEvent extends AttributeSetInstanceEvent {
        org.dddml.ffvtraceability.domain.ConsistentEqualityMap getAttributes();

        void setAttributes(org.dddml.ffvtraceability.domain.ConsistentEqualityMap attributes);

    }

    interface AttributeSetInstanceStateCreated extends AttributeSetInstanceStateEvent
    {
    
    }


}

