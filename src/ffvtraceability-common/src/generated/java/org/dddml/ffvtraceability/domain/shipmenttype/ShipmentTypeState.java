// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.shipmenttype;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface ShipmentTypeState
{
    Long VERSION_ZERO = 0L;

    Long VERSION_NULL = VERSION_ZERO - 1;

    String getShipmentTypeId();

    String getParentTypeId();

    String getHasTable();

    String getDescription();

    Long getVersion();

    String getCreatedBy();

    OffsetDateTime getCreatedAt();

    String getUpdatedBy();

    OffsetDateTime getUpdatedAt();

    Boolean getDeleted();

    interface MutableShipmentTypeState extends ShipmentTypeState {
        void setShipmentTypeId(String shipmentTypeId);

        void setParentTypeId(String parentTypeId);

        void setHasTable(String hasTable);

        void setDescription(String description);

        void setVersion(Long version);

        void setCreatedBy(String createdBy);

        void setCreatedAt(OffsetDateTime createdAt);

        void setUpdatedBy(String updatedBy);

        void setUpdatedAt(OffsetDateTime updatedAt);

        void setDeleted(Boolean deleted);


        void mutate(Event e);

        //void when(ShipmentTypeEvent.ShipmentTypeStateCreated e);

        //void when(ShipmentTypeEvent.ShipmentTypeStateMergePatched e);

        //void when(ShipmentTypeEvent.ShipmentTypeStateDeleted e);
    }

    interface SqlShipmentTypeState extends MutableShipmentTypeState {

        boolean isStateUnsaved();

        boolean getForReapplying();
    }
}
