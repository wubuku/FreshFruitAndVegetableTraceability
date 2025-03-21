// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.shipment;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.specialization.DomainError;

public interface ShipmentCommand extends Command {

    String getShipmentId();

    void setShipmentId(String shipmentId);

    Long getVersion();

    void setVersion(Long version);

    static void throwOnInvalidStateTransition(ShipmentState state, Command c) {
        if (state.getVersion() == null) {
            if (isCreationCommand((ShipmentCommand)c)) {
                return;
            }
            throw DomainError.named("premature", "Can't do anything to unexistent aggregate");
        }
        if (isCreationCommand((ShipmentCommand)c))
            throw DomainError.named("rebirth", "Can't create aggregate that already exists");
    }

    static boolean isCreationCommand(ShipmentCommand c) {
        if ((c instanceof ShipmentCommand.CreateShipment) 
            && (COMMAND_TYPE_CREATE.equals(c.getCommandType()) || c.getVersion().equals(ShipmentState.VERSION_NULL)))
            return true;
        if ((c instanceof ShipmentCommand.MergePatchShipment))
            return false;
        if (c.getCommandType() != null) {
            String commandType = c.getCommandType();
            if (commandType.equals("ShipmentAction"))
                return false;
            if (commandType.equals("ShipmentQaAction"))
                return false;
        }

        if (c.getVersion().equals(ShipmentState.VERSION_NULL))
            return true;
        return false;
    }

    interface CreateOrMergePatchShipment extends ShipmentCommand {
        String getShipmentAction();

        void setShipmentAction(String shipmentAction);
                
        String getShipmentQaAction();

        void setShipmentQaAction(String shipmentQaAction);
                
        String getShipmentTypeId();

        void setShipmentTypeId(String shipmentTypeId);

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

    interface CreateShipment extends CreateOrMergePatchShipment {
        CreateShipmentItemCommandCollection getCreateShipmentItemCommands();

        ShipmentItemCommand.CreateShipmentItem newCreateShipmentItem();

        CreateShipmentPackageCommandCollection getCreateShipmentPackageCommands();

        ShipmentPackageCommand.CreateShipmentPackage newCreateShipmentPackage();

    }

    interface MergePatchShipment extends CreateOrMergePatchShipment {
        Boolean getIsPropertyShipmentTypeIdRemoved();

        void setIsPropertyShipmentTypeIdRemoved(Boolean removed);

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


        ShipmentItemCommandCollection getShipmentItemCommands();

        ShipmentItemCommand.CreateShipmentItem newCreateShipmentItem();

        ShipmentItemCommand.MergePatchShipmentItem newMergePatchShipmentItem();

        ShipmentItemCommand.RemoveShipmentItem newRemoveShipmentItem();

        ShipmentPackageCommandCollection getShipmentPackageCommands();

        ShipmentPackageCommand.CreateShipmentPackage newCreateShipmentPackage();

        ShipmentPackageCommand.MergePatchShipmentPackage newMergePatchShipmentPackage();

        ShipmentPackageCommand.RemoveShipmentPackage newRemoveShipmentPackage();

    }

    interface DeleteShipment extends ShipmentCommand {
    }

    interface CreateShipmentItemCommandCollection extends Iterable<ShipmentItemCommand.CreateShipmentItem> {
        void add(ShipmentItemCommand.CreateShipmentItem c);

        void remove(ShipmentItemCommand.CreateShipmentItem c);

        void clear();
    }

    interface ShipmentItemCommandCollection extends Iterable<ShipmentItemCommand> {
        void add(ShipmentItemCommand c);

        void remove(ShipmentItemCommand c);

        void clear();
    }

    interface CreateShipmentPackageCommandCollection extends Iterable<ShipmentPackageCommand.CreateShipmentPackage> {
        void add(ShipmentPackageCommand.CreateShipmentPackage c);

        void remove(ShipmentPackageCommand.CreateShipmentPackage c);

        void clear();
    }

    interface ShipmentPackageCommandCollection extends Iterable<ShipmentPackageCommand> {
        void add(ShipmentPackageCommand c);

        void remove(ShipmentPackageCommand c);

        void clear();
    }

}

