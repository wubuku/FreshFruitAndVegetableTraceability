// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.shipment;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;


public class ShipmentStateDto {

    private String shipmentId;

    public String getShipmentId()
    {
        return this.shipmentId;
    }

    public void setShipmentId(String shipmentId)
    {
        this.shipmentId = shipmentId;
    }

    private String shipmentTypeId;

    public String getShipmentTypeId()
    {
        return this.shipmentTypeId;
    }

    public void setShipmentTypeId(String shipmentTypeId)
    {
        this.shipmentTypeId = shipmentTypeId;
    }

    private String statusId;

    public String getStatusId()
    {
        return this.statusId;
    }

    public void setStatusId(String statusId)
    {
        this.statusId = statusId;
    }

    private String primaryOrderId;

    public String getPrimaryOrderId()
    {
        return this.primaryOrderId;
    }

    public void setPrimaryOrderId(String primaryOrderId)
    {
        this.primaryOrderId = primaryOrderId;
    }

    private String primaryReturnId;

    public String getPrimaryReturnId()
    {
        return this.primaryReturnId;
    }

    public void setPrimaryReturnId(String primaryReturnId)
    {
        this.primaryReturnId = primaryReturnId;
    }

    private String primaryShipGroupSeqId;

    public String getPrimaryShipGroupSeqId()
    {
        return this.primaryShipGroupSeqId;
    }

    public void setPrimaryShipGroupSeqId(String primaryShipGroupSeqId)
    {
        this.primaryShipGroupSeqId = primaryShipGroupSeqId;
    }

    private String picklistBinId;

    public String getPicklistBinId()
    {
        return this.picklistBinId;
    }

    public void setPicklistBinId(String picklistBinId)
    {
        this.picklistBinId = picklistBinId;
    }

    private OffsetDateTime estimatedReadyDate;

    public OffsetDateTime getEstimatedReadyDate()
    {
        return this.estimatedReadyDate;
    }

    public void setEstimatedReadyDate(OffsetDateTime estimatedReadyDate)
    {
        this.estimatedReadyDate = estimatedReadyDate;
    }

    private OffsetDateTime estimatedShipDate;

    public OffsetDateTime getEstimatedShipDate()
    {
        return this.estimatedShipDate;
    }

    public void setEstimatedShipDate(OffsetDateTime estimatedShipDate)
    {
        this.estimatedShipDate = estimatedShipDate;
    }

    private String estimatedShipWorkEffId;

    public String getEstimatedShipWorkEffId()
    {
        return this.estimatedShipWorkEffId;
    }

    public void setEstimatedShipWorkEffId(String estimatedShipWorkEffId)
    {
        this.estimatedShipWorkEffId = estimatedShipWorkEffId;
    }

    private OffsetDateTime estimatedArrivalDate;

    public OffsetDateTime getEstimatedArrivalDate()
    {
        return this.estimatedArrivalDate;
    }

    public void setEstimatedArrivalDate(OffsetDateTime estimatedArrivalDate)
    {
        this.estimatedArrivalDate = estimatedArrivalDate;
    }

    private String estimatedArrivalWorkEffId;

    public String getEstimatedArrivalWorkEffId()
    {
        return this.estimatedArrivalWorkEffId;
    }

    public void setEstimatedArrivalWorkEffId(String estimatedArrivalWorkEffId)
    {
        this.estimatedArrivalWorkEffId = estimatedArrivalWorkEffId;
    }

    private OffsetDateTime latestCancelDate;

    public OffsetDateTime getLatestCancelDate()
    {
        return this.latestCancelDate;
    }

    public void setLatestCancelDate(OffsetDateTime latestCancelDate)
    {
        this.latestCancelDate = latestCancelDate;
    }

    private java.math.BigDecimal estimatedShipCost;

    public java.math.BigDecimal getEstimatedShipCost()
    {
        return this.estimatedShipCost;
    }

    public void setEstimatedShipCost(java.math.BigDecimal estimatedShipCost)
    {
        this.estimatedShipCost = estimatedShipCost;
    }

    private String currencyUomId;

    public String getCurrencyUomId()
    {
        return this.currencyUomId;
    }

    public void setCurrencyUomId(String currencyUomId)
    {
        this.currencyUomId = currencyUomId;
    }

    private String handlingInstructions;

    public String getHandlingInstructions()
    {
        return this.handlingInstructions;
    }

    public void setHandlingInstructions(String handlingInstructions)
    {
        this.handlingInstructions = handlingInstructions;
    }

    private String originFacilityId;

    public String getOriginFacilityId()
    {
        return this.originFacilityId;
    }

    public void setOriginFacilityId(String originFacilityId)
    {
        this.originFacilityId = originFacilityId;
    }

    private String destinationFacilityId;

    public String getDestinationFacilityId()
    {
        return this.destinationFacilityId;
    }

    public void setDestinationFacilityId(String destinationFacilityId)
    {
        this.destinationFacilityId = destinationFacilityId;
    }

    private String originContactMechId;

    public String getOriginContactMechId()
    {
        return this.originContactMechId;
    }

    public void setOriginContactMechId(String originContactMechId)
    {
        this.originContactMechId = originContactMechId;
    }

    private String originTelecomNumberId;

    public String getOriginTelecomNumberId()
    {
        return this.originTelecomNumberId;
    }

    public void setOriginTelecomNumberId(String originTelecomNumberId)
    {
        this.originTelecomNumberId = originTelecomNumberId;
    }

    private String destinationContactMechId;

    public String getDestinationContactMechId()
    {
        return this.destinationContactMechId;
    }

    public void setDestinationContactMechId(String destinationContactMechId)
    {
        this.destinationContactMechId = destinationContactMechId;
    }

    private String destinationTelecomNumberId;

    public String getDestinationTelecomNumberId()
    {
        return this.destinationTelecomNumberId;
    }

    public void setDestinationTelecomNumberId(String destinationTelecomNumberId)
    {
        this.destinationTelecomNumberId = destinationTelecomNumberId;
    }

    private String partyIdTo;

    public String getPartyIdTo()
    {
        return this.partyIdTo;
    }

    public void setPartyIdTo(String partyIdTo)
    {
        this.partyIdTo = partyIdTo;
    }

    private String partyIdFrom;

    public String getPartyIdFrom()
    {
        return this.partyIdFrom;
    }

    public void setPartyIdFrom(String partyIdFrom)
    {
        this.partyIdFrom = partyIdFrom;
    }

    private java.math.BigDecimal additionalShippingCharge;

    public java.math.BigDecimal getAdditionalShippingCharge()
    {
        return this.additionalShippingCharge;
    }

    public void setAdditionalShippingCharge(java.math.BigDecimal additionalShippingCharge)
    {
        this.additionalShippingCharge = additionalShippingCharge;
    }

    private String addtlShippingChargeDesc;

    public String getAddtlShippingChargeDesc()
    {
        return this.addtlShippingChargeDesc;
    }

    public void setAddtlShippingChargeDesc(String addtlShippingChargeDesc)
    {
        this.addtlShippingChargeDesc = addtlShippingChargeDesc;
    }

    private Long version;

    public Long getVersion()
    {
        return this.version;
    }

    public void setVersion(Long version)
    {
        this.version = version;
    }

    private String createdBy;

    public String getCreatedBy()
    {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    private OffsetDateTime createdAt;

    public OffsetDateTime getCreatedAt()
    {
        return this.createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    private String updatedBy;

    public String getUpdatedBy()
    {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy)
    {
        this.updatedBy = updatedBy;
    }

    private OffsetDateTime updatedAt;

    public OffsetDateTime getUpdatedAt()
    {
        return this.updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    private ShipmentItemStateDto[] shipmentItems;

    public ShipmentItemStateDto[] getShipmentItems()
    {
        return this.shipmentItems;
    }    

    public void setShipmentItems(ShipmentItemStateDto[] shipmentItems)
    {
        this.shipmentItems = shipmentItems;
    }

    private ShipmentPackageStateDto[] shipmentPackages;

    public ShipmentPackageStateDto[] getShipmentPackages()
    {
        return this.shipmentPackages;
    }    

    public void setShipmentPackages(ShipmentPackageStateDto[] shipmentPackages)
    {
        this.shipmentPackages = shipmentPackages;
    }


    public static class DtoConverter extends AbstractStateDtoConverter
    {
        public static Collection<String> collectionFieldNames = Arrays.asList(new String[]{"ShipmentItems", "ShipmentPackages"});

        @Override
        protected boolean isCollectionField(String fieldName) {
            return CollectionUtils.collectionContainsIgnoringCase(collectionFieldNames, fieldName);
        }

        public ShipmentStateDto[] toShipmentStateDtoArray(Iterable<ShipmentState> states) {
            return toShipmentStateDtoList(states).toArray(new ShipmentStateDto[0]);
        }

        public List<ShipmentStateDto> toShipmentStateDtoList(Iterable<ShipmentState> states) {
            ArrayList<ShipmentStateDto> stateDtos = new ArrayList();
            for (ShipmentState s : states) {
                ShipmentStateDto dto = toShipmentStateDto(s);
                stateDtos.add(dto);
            }
            return stateDtos;
        }

        public ShipmentStateDto toShipmentStateDto(ShipmentState state)
        {
            if(state == null) {
                return null;
            }
            ShipmentStateDto dto = new ShipmentStateDto();
            if (returnedFieldsContains("ShipmentId")) {
                dto.setShipmentId(state.getShipmentId());
            }
            if (returnedFieldsContains("ShipmentTypeId")) {
                dto.setShipmentTypeId(state.getShipmentTypeId());
            }
            if (returnedFieldsContains("StatusId")) {
                dto.setStatusId(state.getStatusId());
            }
            if (returnedFieldsContains("PrimaryOrderId")) {
                dto.setPrimaryOrderId(state.getPrimaryOrderId());
            }
            if (returnedFieldsContains("PrimaryReturnId")) {
                dto.setPrimaryReturnId(state.getPrimaryReturnId());
            }
            if (returnedFieldsContains("PrimaryShipGroupSeqId")) {
                dto.setPrimaryShipGroupSeqId(state.getPrimaryShipGroupSeqId());
            }
            if (returnedFieldsContains("PicklistBinId")) {
                dto.setPicklistBinId(state.getPicklistBinId());
            }
            if (returnedFieldsContains("EstimatedReadyDate")) {
                dto.setEstimatedReadyDate(state.getEstimatedReadyDate());
            }
            if (returnedFieldsContains("EstimatedShipDate")) {
                dto.setEstimatedShipDate(state.getEstimatedShipDate());
            }
            if (returnedFieldsContains("EstimatedShipWorkEffId")) {
                dto.setEstimatedShipWorkEffId(state.getEstimatedShipWorkEffId());
            }
            if (returnedFieldsContains("EstimatedArrivalDate")) {
                dto.setEstimatedArrivalDate(state.getEstimatedArrivalDate());
            }
            if (returnedFieldsContains("EstimatedArrivalWorkEffId")) {
                dto.setEstimatedArrivalWorkEffId(state.getEstimatedArrivalWorkEffId());
            }
            if (returnedFieldsContains("LatestCancelDate")) {
                dto.setLatestCancelDate(state.getLatestCancelDate());
            }
            if (returnedFieldsContains("EstimatedShipCost")) {
                dto.setEstimatedShipCost(state.getEstimatedShipCost());
            }
            if (returnedFieldsContains("CurrencyUomId")) {
                dto.setCurrencyUomId(state.getCurrencyUomId());
            }
            if (returnedFieldsContains("HandlingInstructions")) {
                dto.setHandlingInstructions(state.getHandlingInstructions());
            }
            if (returnedFieldsContains("OriginFacilityId")) {
                dto.setOriginFacilityId(state.getOriginFacilityId());
            }
            if (returnedFieldsContains("DestinationFacilityId")) {
                dto.setDestinationFacilityId(state.getDestinationFacilityId());
            }
            if (returnedFieldsContains("OriginContactMechId")) {
                dto.setOriginContactMechId(state.getOriginContactMechId());
            }
            if (returnedFieldsContains("OriginTelecomNumberId")) {
                dto.setOriginTelecomNumberId(state.getOriginTelecomNumberId());
            }
            if (returnedFieldsContains("DestinationContactMechId")) {
                dto.setDestinationContactMechId(state.getDestinationContactMechId());
            }
            if (returnedFieldsContains("DestinationTelecomNumberId")) {
                dto.setDestinationTelecomNumberId(state.getDestinationTelecomNumberId());
            }
            if (returnedFieldsContains("PartyIdTo")) {
                dto.setPartyIdTo(state.getPartyIdTo());
            }
            if (returnedFieldsContains("PartyIdFrom")) {
                dto.setPartyIdFrom(state.getPartyIdFrom());
            }
            if (returnedFieldsContains("AdditionalShippingCharge")) {
                dto.setAdditionalShippingCharge(state.getAdditionalShippingCharge());
            }
            if (returnedFieldsContains("AddtlShippingChargeDesc")) {
                dto.setAddtlShippingChargeDesc(state.getAddtlShippingChargeDesc());
            }
            if (returnedFieldsContains("Version")) {
                dto.setVersion(state.getVersion());
            }
            if (returnedFieldsContains("CreatedBy")) {
                dto.setCreatedBy(state.getCreatedBy());
            }
            if (returnedFieldsContains("CreatedAt")) {
                dto.setCreatedAt(state.getCreatedAt());
            }
            if (returnedFieldsContains("UpdatedBy")) {
                dto.setUpdatedBy(state.getUpdatedBy());
            }
            if (returnedFieldsContains("UpdatedAt")) {
                dto.setUpdatedAt(state.getUpdatedAt());
            }
            if (returnedFieldsContains("ShipmentItems")) {
                ArrayList<ShipmentItemStateDto> arrayList = new ArrayList();
                if (state.getShipmentItems() != null) {
                    ShipmentItemStateDto.DtoConverter conv = new ShipmentItemStateDto.DtoConverter();
                    String returnFS = CollectionUtils.mapGetValueIgnoringCase(getReturnedFields(), "ShipmentItems");
                    if(returnFS != null) { conv.setReturnedFieldsString(returnFS); } else { conv.setAllFieldsReturned(this.getAllFieldsReturned()); }
                    for (ShipmentItemState s : state.getShipmentItems()) {
                        arrayList.add(conv.toShipmentItemStateDto(s));
                    }
                }
                dto.setShipmentItems(arrayList.toArray(new ShipmentItemStateDto[0]));
            }
            if (returnedFieldsContains("ShipmentPackages")) {
                ArrayList<ShipmentPackageStateDto> arrayList = new ArrayList();
                if (state.getShipmentPackages() != null) {
                    ShipmentPackageStateDto.DtoConverter conv = new ShipmentPackageStateDto.DtoConverter();
                    String returnFS = CollectionUtils.mapGetValueIgnoringCase(getReturnedFields(), "ShipmentPackages");
                    if(returnFS != null) { conv.setReturnedFieldsString(returnFS); } else { conv.setAllFieldsReturned(this.getAllFieldsReturned()); }
                    for (ShipmentPackageState s : state.getShipmentPackages()) {
                        arrayList.add(conv.toShipmentPackageStateDto(s));
                    }
                }
                dto.setShipmentPackages(arrayList.toArray(new ShipmentPackageStateDto[0]));
            }
            return dto;
        }

    }
}
