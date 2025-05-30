// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.shipment;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface ShipmentEvent extends Event {

    interface SqlShipmentEvent extends ShipmentEvent {
        ShipmentEventId getShipmentEventId();

        boolean getEventReadOnly();

        void setEventReadOnly(boolean readOnly);
    }

    interface ShipmentActionEvent extends ShipmentEvent {
        String getValue();

        void setValue(String value);

    }

    interface ShipmentQaActionEvent extends ShipmentEvent {
        String getValue();

        void setValue(String value);

    }

    String getShipmentId();

    //void setShipmentId(String shipmentId);

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

    interface ShipmentStateEvent extends ShipmentEvent {
        String getShipmentTypeId();

        void setShipmentTypeId(String shipmentTypeId);

        String getStatusId();

        void setStatusId(String statusId);

        String getQaStatusId();

        void setQaStatusId(String qaStatusId);

        String getPrimaryOrderId();

        void setPrimaryOrderId(String primaryOrderId);

        String getPrimaryReturnId();

        void setPrimaryReturnId(String primaryReturnId);

        String getPrimaryShipGroupSeqId();

        void setPrimaryShipGroupSeqId(String primaryShipGroupSeqId);

        String getPicklistBinId();

        void setPicklistBinId(String picklistBinId);

        OffsetDateTime getEstimatedReadyDate();

        void setEstimatedReadyDate(OffsetDateTime estimatedReadyDate);

        OffsetDateTime getEstimatedShipDate();

        void setEstimatedShipDate(OffsetDateTime estimatedShipDate);

        String getEstimatedShipWorkEffId();

        void setEstimatedShipWorkEffId(String estimatedShipWorkEffId);

        OffsetDateTime getEstimatedArrivalDate();

        void setEstimatedArrivalDate(OffsetDateTime estimatedArrivalDate);

        String getEstimatedArrivalWorkEffId();

        void setEstimatedArrivalWorkEffId(String estimatedArrivalWorkEffId);

        OffsetDateTime getLatestCancelDate();

        void setLatestCancelDate(OffsetDateTime latestCancelDate);

        java.math.BigDecimal getEstimatedShipCost();

        void setEstimatedShipCost(java.math.BigDecimal estimatedShipCost);

        String getCurrencyUomId();

        void setCurrencyUomId(String currencyUomId);

        String getHandlingInstructions();

        void setHandlingInstructions(String handlingInstructions);

        String getOriginFacilityId();

        void setOriginFacilityId(String originFacilityId);

        String getDestinationFacilityId();

        void setDestinationFacilityId(String destinationFacilityId);

        String getOriginContactMechId();

        void setOriginContactMechId(String originContactMechId);

        String getOriginTelecomNumberId();

        void setOriginTelecomNumberId(String originTelecomNumberId);

        String getDestinationContactMechId();

        void setDestinationContactMechId(String destinationContactMechId);

        String getDestinationTelecomNumberId();

        void setDestinationTelecomNumberId(String destinationTelecomNumberId);

        String getPartyIdTo();

        void setPartyIdTo(String partyIdTo);

        String getPartyIdFrom();

        void setPartyIdFrom(String partyIdFrom);

        java.math.BigDecimal getAdditionalShippingCharge();

        void setAdditionalShippingCharge(java.math.BigDecimal additionalShippingCharge);

        String getAddtlShippingChargeDesc();

        void setAddtlShippingChargeDesc(String addtlShippingChargeDesc);

        String getReceivedBy();

        void setReceivedBy(String receivedBy);

        OffsetDateTime getDatetimeReceived();

        void setDatetimeReceived(OffsetDateTime datetimeReceived);

    }

    interface ShipmentStateCreated extends ShipmentStateEvent
    {
        Iterable<ShipmentItemEvent.ShipmentItemStateCreated> getShipmentItemEvents();
        
        void addShipmentItemEvent(ShipmentItemEvent.ShipmentItemStateCreated e);

        ShipmentItemEvent.ShipmentItemStateCreated newShipmentItemStateCreated(String shipmentItemSeqId);

        Iterable<ShipmentPackageEvent.ShipmentPackageStateCreated> getShipmentPackageEvents();
        
        void addShipmentPackageEvent(ShipmentPackageEvent.ShipmentPackageStateCreated e);

        ShipmentPackageEvent.ShipmentPackageStateCreated newShipmentPackageStateCreated(String shipmentPackageSeqId);

    
    }


    interface ShipmentStateMergePatched extends ShipmentStateEvent
    {
        Boolean getIsPropertyShipmentTypeIdRemoved();

        void setIsPropertyShipmentTypeIdRemoved(Boolean removed);

        Boolean getIsPropertyStatusIdRemoved();

        void setIsPropertyStatusIdRemoved(Boolean removed);

        Boolean getIsPropertyQaStatusIdRemoved();

        void setIsPropertyQaStatusIdRemoved(Boolean removed);

        Boolean getIsPropertyPrimaryOrderIdRemoved();

        void setIsPropertyPrimaryOrderIdRemoved(Boolean removed);

        Boolean getIsPropertyPrimaryReturnIdRemoved();

        void setIsPropertyPrimaryReturnIdRemoved(Boolean removed);

        Boolean getIsPropertyPrimaryShipGroupSeqIdRemoved();

        void setIsPropertyPrimaryShipGroupSeqIdRemoved(Boolean removed);

        Boolean getIsPropertyPicklistBinIdRemoved();

        void setIsPropertyPicklistBinIdRemoved(Boolean removed);

        Boolean getIsPropertyEstimatedReadyDateRemoved();

        void setIsPropertyEstimatedReadyDateRemoved(Boolean removed);

        Boolean getIsPropertyEstimatedShipDateRemoved();

        void setIsPropertyEstimatedShipDateRemoved(Boolean removed);

        Boolean getIsPropertyEstimatedShipWorkEffIdRemoved();

        void setIsPropertyEstimatedShipWorkEffIdRemoved(Boolean removed);

        Boolean getIsPropertyEstimatedArrivalDateRemoved();

        void setIsPropertyEstimatedArrivalDateRemoved(Boolean removed);

        Boolean getIsPropertyEstimatedArrivalWorkEffIdRemoved();

        void setIsPropertyEstimatedArrivalWorkEffIdRemoved(Boolean removed);

        Boolean getIsPropertyLatestCancelDateRemoved();

        void setIsPropertyLatestCancelDateRemoved(Boolean removed);

        Boolean getIsPropertyEstimatedShipCostRemoved();

        void setIsPropertyEstimatedShipCostRemoved(Boolean removed);

        Boolean getIsPropertyCurrencyUomIdRemoved();

        void setIsPropertyCurrencyUomIdRemoved(Boolean removed);

        Boolean getIsPropertyHandlingInstructionsRemoved();

        void setIsPropertyHandlingInstructionsRemoved(Boolean removed);

        Boolean getIsPropertyOriginFacilityIdRemoved();

        void setIsPropertyOriginFacilityIdRemoved(Boolean removed);

        Boolean getIsPropertyDestinationFacilityIdRemoved();

        void setIsPropertyDestinationFacilityIdRemoved(Boolean removed);

        Boolean getIsPropertyOriginContactMechIdRemoved();

        void setIsPropertyOriginContactMechIdRemoved(Boolean removed);

        Boolean getIsPropertyOriginTelecomNumberIdRemoved();

        void setIsPropertyOriginTelecomNumberIdRemoved(Boolean removed);

        Boolean getIsPropertyDestinationContactMechIdRemoved();

        void setIsPropertyDestinationContactMechIdRemoved(Boolean removed);

        Boolean getIsPropertyDestinationTelecomNumberIdRemoved();

        void setIsPropertyDestinationTelecomNumberIdRemoved(Boolean removed);

        Boolean getIsPropertyPartyIdToRemoved();

        void setIsPropertyPartyIdToRemoved(Boolean removed);

        Boolean getIsPropertyPartyIdFromRemoved();

        void setIsPropertyPartyIdFromRemoved(Boolean removed);

        Boolean getIsPropertyAdditionalShippingChargeRemoved();

        void setIsPropertyAdditionalShippingChargeRemoved(Boolean removed);

        Boolean getIsPropertyAddtlShippingChargeDescRemoved();

        void setIsPropertyAddtlShippingChargeDescRemoved(Boolean removed);

        Boolean getIsPropertyReceivedByRemoved();

        void setIsPropertyReceivedByRemoved(Boolean removed);

        Boolean getIsPropertyDatetimeReceivedRemoved();

        void setIsPropertyDatetimeReceivedRemoved(Boolean removed);


        Iterable<ShipmentItemEvent> getShipmentItemEvents();
        
        void addShipmentItemEvent(ShipmentItemEvent e);

        ShipmentItemEvent.ShipmentItemStateCreated newShipmentItemStateCreated(String shipmentItemSeqId);

        ShipmentItemEvent.ShipmentItemStateMergePatched newShipmentItemStateMergePatched(String shipmentItemSeqId);

        Iterable<ShipmentPackageEvent> getShipmentPackageEvents();
        
        void addShipmentPackageEvent(ShipmentPackageEvent e);

        ShipmentPackageEvent.ShipmentPackageStateCreated newShipmentPackageStateCreated(String shipmentPackageSeqId);

        ShipmentPackageEvent.ShipmentPackageStateMergePatched newShipmentPackageStateMergePatched(String shipmentPackageSeqId);


    }


}

