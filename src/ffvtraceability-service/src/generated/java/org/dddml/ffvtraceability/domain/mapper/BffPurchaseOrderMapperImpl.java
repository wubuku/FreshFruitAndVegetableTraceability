package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffPurchaseOrderDto;
import org.dddml.ffvtraceability.domain.BffPurchaseOrderFulfillmentDto;
import org.dddml.ffvtraceability.domain.BffPurchaseOrderItemDto;
import org.dddml.ffvtraceability.domain.repository.BffPurchaseOrderAndItemProjection;
import org.dddml.ffvtraceability.domain.repository.BffPurchaseOrderFulfillmentProjection;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class BffPurchaseOrderMapperImpl implements BffPurchaseOrderMapper {

    @Override
    public BffPurchaseOrderDto toBffPurchaseOrderDto(BffPurchaseOrderAndItemProjection purchaseOrderAndItemProjection) {
        if ( purchaseOrderAndItemProjection == null ) {
            return null;
        }

        BffPurchaseOrderDto bffPurchaseOrderDto = new BffPurchaseOrderDto();

        bffPurchaseOrderDto.setCreatedAt( instantToOffsetDateTime( purchaseOrderAndItemProjection.getCreatedAt() ) );
        bffPurchaseOrderDto.setCreatedBy( purchaseOrderAndItemProjection.getCreatedBy() );
        bffPurchaseOrderDto.setCurrencyUomId( purchaseOrderAndItemProjection.getCurrencyUomId() );
        bffPurchaseOrderDto.setExternalId( purchaseOrderAndItemProjection.getExternalId() );
        bffPurchaseOrderDto.setFulfillmentStatusId( purchaseOrderAndItemProjection.getFulfillmentStatusId() );
        bffPurchaseOrderDto.setMemo( purchaseOrderAndItemProjection.getMemo() );
        bffPurchaseOrderDto.setOrderDate( instantToOffsetDateTime( purchaseOrderAndItemProjection.getOrderDate() ) );
        bffPurchaseOrderDto.setOrderId( purchaseOrderAndItemProjection.getOrderId() );
        bffPurchaseOrderDto.setOrderName( purchaseOrderAndItemProjection.getOrderName() );
        bffPurchaseOrderDto.setOriginFacilityId( purchaseOrderAndItemProjection.getOriginFacilityId() );
        bffPurchaseOrderDto.setStatusId( purchaseOrderAndItemProjection.getStatusId() );
        bffPurchaseOrderDto.setSupplierId( purchaseOrderAndItemProjection.getSupplierId() );
        bffPurchaseOrderDto.setSupplierName( purchaseOrderAndItemProjection.getSupplierName() );
        bffPurchaseOrderDto.setSyncStatusId( purchaseOrderAndItemProjection.getSyncStatusId() );
        bffPurchaseOrderDto.setUpdatedAt( instantToOffsetDateTime( purchaseOrderAndItemProjection.getUpdatedAt() ) );
        bffPurchaseOrderDto.setUpdatedBy( purchaseOrderAndItemProjection.getUpdatedBy() );

        return bffPurchaseOrderDto;
    }

    @Override
    public BffPurchaseOrderItemDto toBffPurchaseOrderItemDto(BffPurchaseOrderAndItemProjection purchaseOrderAndItemProjection) {
        if ( purchaseOrderAndItemProjection == null ) {
            return null;
        }

        BffPurchaseOrderItemDto bffPurchaseOrderItemDto = new BffPurchaseOrderItemDto();

        bffPurchaseOrderItemDto.setStatusId( purchaseOrderAndItemProjection.getItemStatusId() );
        bffPurchaseOrderItemDto.setSyncStatusId( purchaseOrderAndItemProjection.getItemSyncStatusId() );
        bffPurchaseOrderItemDto.setFulfillmentStatusId( purchaseOrderAndItemProjection.getItemFulfillmentStatusId() );
        bffPurchaseOrderItemDto.setCancelQuantity( purchaseOrderAndItemProjection.getCancelQuantity() );
        bffPurchaseOrderItemDto.setComments( purchaseOrderAndItemProjection.getComments() );
        bffPurchaseOrderItemDto.setEstimatedDeliveryDate( instantToOffsetDateTime( purchaseOrderAndItemProjection.getEstimatedDeliveryDate() ) );
        bffPurchaseOrderItemDto.setEstimatedShipDate( instantToOffsetDateTime( purchaseOrderAndItemProjection.getEstimatedShipDate() ) );
        bffPurchaseOrderItemDto.setExternalId( purchaseOrderAndItemProjection.getExternalId() );
        bffPurchaseOrderItemDto.setGtin( purchaseOrderAndItemProjection.getGtin() );
        bffPurchaseOrderItemDto.setItemDescription( purchaseOrderAndItemProjection.getItemDescription() );
        bffPurchaseOrderItemDto.setOrderItemSeqId( purchaseOrderAndItemProjection.getOrderItemSeqId() );
        bffPurchaseOrderItemDto.setProductId( purchaseOrderAndItemProjection.getProductId() );
        bffPurchaseOrderItemDto.setProductName( purchaseOrderAndItemProjection.getProductName() );
        bffPurchaseOrderItemDto.setQuantity( purchaseOrderAndItemProjection.getQuantity() );
        bffPurchaseOrderItemDto.setSelectedAmount( purchaseOrderAndItemProjection.getSelectedAmount() );
        bffPurchaseOrderItemDto.setSupplierProductId( purchaseOrderAndItemProjection.getSupplierProductId() );
        bffPurchaseOrderItemDto.setUnitPrice( purchaseOrderAndItemProjection.getUnitPrice() );

        return bffPurchaseOrderItemDto;
    }

    @Override
    public BffPurchaseOrderFulfillmentDto toBffPurchaseOrderFulfillmentDto(BffPurchaseOrderFulfillmentProjection purchaseOrderFulfillmentProjection) {
        if ( purchaseOrderFulfillmentProjection == null ) {
            return null;
        }

        BffPurchaseOrderFulfillmentDto bffPurchaseOrderFulfillmentDto = new BffPurchaseOrderFulfillmentDto();

        bffPurchaseOrderFulfillmentDto.setAllocatedQuantity( purchaseOrderFulfillmentProjection.getAllocatedQuantity() );
        bffPurchaseOrderFulfillmentDto.setQaStatusId( purchaseOrderFulfillmentProjection.getQaStatusId() );
        bffPurchaseOrderFulfillmentDto.setReceiptId( purchaseOrderFulfillmentProjection.getReceiptId() );
        bffPurchaseOrderFulfillmentDto.setReceivedAt( instantToOffsetDateTime( purchaseOrderFulfillmentProjection.getReceivedAt() ) );

        return bffPurchaseOrderFulfillmentDto;
    }
}
