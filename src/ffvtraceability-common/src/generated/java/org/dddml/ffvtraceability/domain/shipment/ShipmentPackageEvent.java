// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.shipment;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface ShipmentPackageEvent extends Event {

    interface SqlShipmentPackageEvent extends ShipmentPackageEvent {
        ShipmentPackageEventId getShipmentPackageEventId();

        boolean getEventReadOnly();

        void setEventReadOnly(boolean readOnly);
    }

    String getShipmentPackageSeqId();

    //void setShipmentPackageSeqId(String shipmentPackageSeqId);

    String getCreatedBy();

    void setCreatedBy(String createdBy);

    OffsetDateTime getCreatedAt();

    void setCreatedAt(OffsetDateTime createdAt);

    String getCommandId();

    void setCommandId(String commandId);

    interface ShipmentPackageStateEvent extends ShipmentPackageEvent {
        Long getVersion();

        void setVersion(Long version);

        String getShipmentBoxTypeId();

        void setShipmentBoxTypeId(String shipmentBoxTypeId);

        OffsetDateTime getDateCreated();

        void setDateCreated(OffsetDateTime dateCreated);

        java.math.BigDecimal getBoxLength();

        void setBoxLength(java.math.BigDecimal boxLength);

        java.math.BigDecimal getBoxHeight();

        void setBoxHeight(java.math.BigDecimal boxHeight);

        java.math.BigDecimal getBoxWidth();

        void setBoxWidth(java.math.BigDecimal boxWidth);

        String getDimensionUomId();

        void setDimensionUomId(String dimensionUomId);

        java.math.BigDecimal getWeight();

        void setWeight(java.math.BigDecimal weight);

        String getWeightUomId();

        void setWeightUomId(String weightUomId);

        java.math.BigDecimal getInsuredValue();

        void setInsuredValue(java.math.BigDecimal insuredValue);

    }

    interface ShipmentPackageStateCreated extends ShipmentPackageStateEvent
    {
        Iterable<ShipmentPackageContentEvent.ShipmentPackageContentStateCreated> getShipmentPackageContentEvents();
        
        void addShipmentPackageContentEvent(ShipmentPackageContentEvent.ShipmentPackageContentStateCreated e);

        ShipmentPackageContentEvent.ShipmentPackageContentStateCreated newShipmentPackageContentStateCreated(String shipmentItemSeqId);

    
    }


    interface ShipmentPackageStateMergePatched extends ShipmentPackageStateEvent
    {
        Boolean getIsPropertyShipmentBoxTypeIdRemoved();

        void setIsPropertyShipmentBoxTypeIdRemoved(Boolean removed);

        Boolean getIsPropertyDateCreatedRemoved();

        void setIsPropertyDateCreatedRemoved(Boolean removed);

        Boolean getIsPropertyBoxLengthRemoved();

        void setIsPropertyBoxLengthRemoved(Boolean removed);

        Boolean getIsPropertyBoxHeightRemoved();

        void setIsPropertyBoxHeightRemoved(Boolean removed);

        Boolean getIsPropertyBoxWidthRemoved();

        void setIsPropertyBoxWidthRemoved(Boolean removed);

        Boolean getIsPropertyDimensionUomIdRemoved();

        void setIsPropertyDimensionUomIdRemoved(Boolean removed);

        Boolean getIsPropertyWeightRemoved();

        void setIsPropertyWeightRemoved(Boolean removed);

        Boolean getIsPropertyWeightUomIdRemoved();

        void setIsPropertyWeightUomIdRemoved(Boolean removed);

        Boolean getIsPropertyInsuredValueRemoved();

        void setIsPropertyInsuredValueRemoved(Boolean removed);


        Iterable<ShipmentPackageContentEvent> getShipmentPackageContentEvents();
        
        void addShipmentPackageContentEvent(ShipmentPackageContentEvent e);

        ShipmentPackageContentEvent.ShipmentPackageContentStateCreated newShipmentPackageContentStateCreated(String shipmentItemSeqId);

        ShipmentPackageContentEvent.ShipmentPackageContentStateMergePatched newShipmentPackageContentStateMergePatched(String shipmentItemSeqId);


    }


}

