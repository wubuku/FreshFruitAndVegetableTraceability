package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.inventoryitem.InventoryItemAttributes;
import org.dddml.ffvtraceability.domain.inventoryitem.InventoryItemDetailAttributes;
import org.dddml.ffvtraceability.domain.inventoryitem.InventoryItemDetailState;
import org.dddml.ffvtraceability.domain.inventoryitem.InventoryItemState;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class InventoryItemMapperImpl implements InventoryItemMapper {

    @Override
    public void updateInventoryItemState(InventoryItemState.MutableInventoryItemState state, InventoryItemAttributes attributes) {
        if ( attributes == null ) {
            return;
        }

        state.setInventoryItemTypeId( attributes.getInventoryItemTypeId() );
        state.setProductId( attributes.getProductId() );
        state.setPartyId( attributes.getPartyId() );
        state.setOwnerPartyId( attributes.getOwnerPartyId() );
        state.setStatusId( attributes.getStatusId() );
        state.setDatetimeReceived( attributes.getDatetimeReceived() );
        state.setDatetimeManufactured( attributes.getDatetimeManufactured() );
        state.setExpireDate( attributes.getExpireDate() );
        state.setFacilityId( attributes.getFacilityId() );
        state.setContainerId( attributes.getContainerId() );
        state.setLotId( attributes.getLotId() );
        state.setUomId( attributes.getUomId() );
        state.setBinNumber( attributes.getBinNumber() );
        state.setLocationSeqId( attributes.getLocationSeqId() );
        state.setComments( attributes.getComments() );
        state.setSerialNumber( attributes.getSerialNumber() );
        state.setSoftIdentifier( attributes.getSoftIdentifier() );
        state.setActivationNumber( attributes.getActivationNumber() );
        state.setActivationValidThru( attributes.getActivationValidThru() );
        state.setCurrencyUomId( attributes.getCurrencyUomId() );
        state.setFixedAssetId( attributes.getFixedAssetId() );
    }

    @Override
    public void updateInventoryItemDetailState(InventoryItemDetailState.MutableInventoryItemDetailState state, InventoryItemDetailAttributes attributes) {
        if ( attributes == null ) {
            return;
        }

        state.setEffectiveDate( attributes.getEffectiveDate() );
        state.setOrderId( attributes.getOrderId() );
        state.setOrderItemSeqId( attributes.getOrderItemSeqId() );
        state.setShipGroupSeqId( attributes.getShipGroupSeqId() );
        state.setShipmentId( attributes.getShipmentId() );
        state.setShipmentItemSeqId( attributes.getShipmentItemSeqId() );
        state.setReturnId( attributes.getReturnId() );
        state.setReturnItemSeqId( attributes.getReturnItemSeqId() );
        state.setWorkEffortId( attributes.getWorkEffortId() );
        state.setFixedAssetId( attributes.getFixedAssetId() );
        state.setMaintHistSeqId( attributes.getMaintHistSeqId() );
        state.setItemIssuanceId( attributes.getItemIssuanceId() );
        state.setReceiptId( attributes.getReceiptId() );
        state.setPhysicalInventoryId( attributes.getPhysicalInventoryId() );
        state.setReasonEnumId( attributes.getReasonEnumId() );
        state.setDescription( attributes.getDescription() );
    }
}
