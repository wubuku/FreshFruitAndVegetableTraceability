// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.workeffortinventoryassign;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface WorkEffortInventoryAssignEvent extends Event {

    interface SqlWorkEffortInventoryAssignEvent extends WorkEffortInventoryAssignEvent {
        WorkEffortInventoryAssignEventId getWorkEffortInventoryAssignEventId();

        boolean getEventReadOnly();

        void setEventReadOnly(boolean readOnly);
    }

    WorkEffortInventoryAssignId getWorkEffortInventoryAssignId();

    //void setWorkEffortInventoryAssignId(WorkEffortInventoryAssignId workEffortInventoryAssignId);

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

    interface WorkEffortInventoryAssignStateEvent extends WorkEffortInventoryAssignEvent {
        String getStatusId();

        void setStatusId(String statusId);

        Double getQuantity();

        void setQuantity(Double quantity);

    }

    interface WorkEffortInventoryAssignStateCreated extends WorkEffortInventoryAssignStateEvent
    {
    
    }


    interface WorkEffortInventoryAssignStateMergePatched extends WorkEffortInventoryAssignStateEvent
    {
        Boolean getIsPropertyStatusIdRemoved();

        void setIsPropertyStatusIdRemoved(Boolean removed);

        Boolean getIsPropertyQuantityRemoved();

        void setIsPropertyQuantityRemoved(Boolean removed);



    }


}

