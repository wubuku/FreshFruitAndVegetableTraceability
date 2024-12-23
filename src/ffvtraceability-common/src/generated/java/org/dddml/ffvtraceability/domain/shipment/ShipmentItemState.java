// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.shipment;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface ShipmentItemState
{
    Long VERSION_ZERO = 0L;

    Long VERSION_NULL = VERSION_ZERO - 1;

    String getShipmentItemSeqId();

    String getProductId();

    java.math.BigDecimal getQuantity();

    String getShipmentContentDescription();

    Long getVersion();

    String getCreatedBy();

    OffsetDateTime getCreatedAt();

    String getUpdatedBy();

    OffsetDateTime getUpdatedAt();

    String getShipmentId();

    interface MutableShipmentItemState extends ShipmentItemState {
        void setShipmentItemSeqId(String shipmentItemSeqId);

        void setProductId(String productId);

        void setQuantity(java.math.BigDecimal quantity);

        void setShipmentContentDescription(String shipmentContentDescription);

        void setVersion(Long version);

        void setCreatedBy(String createdBy);

        void setCreatedAt(OffsetDateTime createdAt);

        void setUpdatedBy(String updatedBy);

        void setUpdatedAt(OffsetDateTime updatedAt);

        void setShipmentId(String shipmentId);


        void mutate(Event e);

        //void when(ShipmentItemEvent.ShipmentItemStateCreated e);

        //void when(ShipmentItemEvent.ShipmentItemStateMergePatched e);

    }

    interface SqlShipmentItemState extends MutableShipmentItemState {
        ShipmentItemId getShipmentItemId();

        void setShipmentItemId(ShipmentItemId shipmentItemId);


        boolean isStateUnsaved();

        boolean getForReapplying();
    }
}

