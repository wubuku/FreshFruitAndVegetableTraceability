// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.lotidentificationtype;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface LotIdentificationTypeEvent extends Event {

    interface SqlLotIdentificationTypeEvent extends LotIdentificationTypeEvent {
        LotIdentificationTypeEventId getLotIdentificationTypeEventId();

        boolean getEventReadOnly();

        void setEventReadOnly(boolean readOnly);
    }

    String getLotIdentificationTypeId();

    //void setLotIdentificationTypeId(String lotIdentificationTypeId);

    Long getVersion();
    
    //void setVersion(Long version);

    String getCreatedBy();

    void setCreatedBy(String createdBy);

    OffsetDateTime getCreatedAt();

    void setCreatedAt(OffsetDateTime createdAt);

    String getCommandId();

    void setCommandId(String commandId);

    interface LotIdentificationTypeStateEvent extends LotIdentificationTypeEvent {
        String getDescription();

        void setDescription(String description);

    }

    interface LotIdentificationTypeStateCreated extends LotIdentificationTypeStateEvent
    {
    
    }


    interface LotIdentificationTypeStateMergePatched extends LotIdentificationTypeStateEvent
    {
        Boolean getIsPropertyDescriptionRemoved();

        void setIsPropertyDescriptionRemoved(Boolean removed);



    }

    interface LotIdentificationTypeStateDeleted extends LotIdentificationTypeStateEvent
    {
    }


}
