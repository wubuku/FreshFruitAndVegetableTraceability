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

        state.setActivationNumber( attributes.getActivationNumber() );
        state.setActivationValidThru( attributes.getActivationValidThru() );
        state.setBinNumber( attributes.getBinNumber() );
        state.setComments( attributes.getComments() );
        state.setContainerId( attributes.getContainerId() );
        state.setCurrencyUomId( attributes.getCurrencyUomId() );
        state.setDatetimeManufactured( attributes.getDatetimeManufactured() );
        state.setDatetimeReceived( attributes.getDatetimeReceived() );
        state.setExpireDate( attributes.getExpireDate() );
        state.setFacilityId( attributes.getFacilityId() );
        state.setFixedAssetId( attributes.getFixedAssetId() );
        state.setInventoryItemTypeId( attributes.getInventoryItemTypeId() );
        state.setLocationSeqId( attributes.getLocationSeqId() );
        state.setLotId( attributes.getLotId() );
        state.setOwnerPartyId( attributes.getOwnerPartyId() );
        state.setPartyId( attributes.getPartyId() );
        state.setProductId( attributes.getProductId() );
        state.setSerialNumber( attributes.getSerialNumber() );
        state.setSoftIdentifier( attributes.getSoftIdentifier() );
        state.setStatusId( attributes.getStatusId() );
        state.setUomId( attributes.getUomId() );
    }

    @Override
    public void updateInventoryItemDetailState(InventoryItemDetailState.MutableInventoryItemDetailState state, InventoryItemDetailAttributes attributes) {
        if ( attributes == null ) {
            return;
        }

        state.setDescription( attributes.getDescription() );
        state.setEffectiveDate( attributes.getEffectiveDate() );
        state.setFixedAssetId( attributes.getFixedAssetId() );
        state.setItemIssuanceId( attributes.getItemIssuanceId() );
        state.setMaintHistSeqId( attributes.getMaintHistSeqId() );
        state.setOrderId( attributes.getOrderId() );
        state.setOrderItemSeqId( attributes.getOrderItemSeqId() );
        state.setPhysicalInventoryId( attributes.getPhysicalInventoryId() );
        state.setReasonEnumId( attributes.getReasonEnumId() );
        state.setReceiptId( attributes.getReceiptId() );
        state.setReturnId( attributes.getReturnId() );
        state.setReturnItemSeqId( attributes.getReturnItemSeqId() );
        state.setShipGroupSeqId( attributes.getShipGroupSeqId() );
        state.setShipmentId( attributes.getShipmentId() );
        state.setShipmentItemSeqId( attributes.getShipmentItemSeqId() );
        state.setWorkEffortId( attributes.getWorkEffortId() );
    }
}
