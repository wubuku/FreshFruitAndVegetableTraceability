// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.uom;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface UomEvent extends Event {

    interface SqlUomEvent extends UomEvent {
        UomEventId getUomEventId();

        boolean getEventReadOnly();

        void setEventReadOnly(boolean readOnly);
    }

    String getUomId();

    //void setUomId(String uomId);

    Long getVersion();
    
    //void setVersion(Long version);

    String getCreatedBy();

    void setCreatedBy(String createdBy);

    OffsetDateTime getCreatedAt();

    void setCreatedAt(OffsetDateTime createdAt);

    String getCommandId();

    void setCommandId(String commandId);

    interface UomStateEvent extends UomEvent {
        String getUomTypeId();

        void setUomTypeId(String uomTypeId);

        String getAbbreviation();

        void setAbbreviation(String abbreviation);

        Long getNumericCode();

        void setNumericCode(Long numericCode);

        String getGs1AI();

        void setGs1AI(String gs1AI);

        String getDescription();

        void setDescription(String description);

    }

    interface UomStateCreated extends UomStateEvent
    {
    
    }


    interface UomStateMergePatched extends UomStateEvent
    {
        Boolean getIsPropertyUomTypeIdRemoved();

        void setIsPropertyUomTypeIdRemoved(Boolean removed);

        Boolean getIsPropertyAbbreviationRemoved();

        void setIsPropertyAbbreviationRemoved(Boolean removed);

        Boolean getIsPropertyNumericCodeRemoved();

        void setIsPropertyNumericCodeRemoved(Boolean removed);

        Boolean getIsPropertyGs1AIRemoved();

        void setIsPropertyGs1AIRemoved(Boolean removed);

        Boolean getIsPropertyDescriptionRemoved();

        void setIsPropertyDescriptionRemoved(Boolean removed);



    }

    interface UomStateDeleted extends UomStateEvent
    {
    }


}
