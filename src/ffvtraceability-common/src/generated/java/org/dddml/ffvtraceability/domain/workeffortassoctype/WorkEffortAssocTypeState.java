// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.workeffortassoctype;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface WorkEffortAssocTypeState
{
    Long VERSION_ZERO = 0L;

    Long VERSION_NULL = VERSION_ZERO - 1;

    String getWorkEffortAssocTypeId();

    String getParentTypeId();

    String getHasTable();

    String getDescription();

    Long getVersion();

    String getCreatedBy();

    OffsetDateTime getCreatedAt();

    String getUpdatedBy();

    OffsetDateTime getUpdatedAt();

    String getTenantId();

    String getCommandId();

    interface MutableWorkEffortAssocTypeState extends WorkEffortAssocTypeState {
        void setWorkEffortAssocTypeId(String workEffortAssocTypeId);

        void setParentTypeId(String parentTypeId);

        void setHasTable(String hasTable);

        void setDescription(String description);

        void setVersion(Long version);

        void setCreatedBy(String createdBy);

        void setCreatedAt(OffsetDateTime createdAt);

        void setUpdatedBy(String updatedBy);

        void setUpdatedAt(OffsetDateTime updatedAt);

        void setTenantId(String tenantId);

        void setCommandId(String commandId);

    }

    interface SqlWorkEffortAssocTypeState extends MutableWorkEffortAssocTypeState {

        boolean isStateUnsaved();

        boolean getForReapplying();
    }
}

