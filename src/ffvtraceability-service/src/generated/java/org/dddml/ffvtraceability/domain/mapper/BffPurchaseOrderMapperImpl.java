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

        bffPurchaseOrderDto.setOrderId( purchaseOrderAndItemProjection.getOrderId() );
        bffPurchaseOrderDto.setOrderName( purchaseOrderAndItemProjection.getOrderName() );
        bffPurchaseOrderDto.setExternalId( purchaseOrderAndItemProjection.getExternalId() );
        bffPurchaseOrderDto.setOrderDate( instantToOffsetDateTime( purchaseOrderAndItemProjection.getOrderDate() ) );
        bffPurchaseOrderDto.setStatusId( purchaseOrderAndItemProjection.getStatusId() );
        bffPurchaseOrderDto.setCurrencyUomId( purchaseOrderAndItemProjection.getCurrencyUomId() );
        bffPurchaseOrderDto.setSyncStatusId( purchaseOrderAndItemProjection.getSyncStatusId() );
        bffPurchaseOrderDto.setOriginFacilityId( purchaseOrderAndItemProjection.getOriginFacilityId() );
        bffPurchaseOrderDto.setMemo( purchaseOrderAndItemProjection.getMemo() );
        bffPurchaseOrderDto.setFulfillmentStatusId( purchaseOrderAndItemProjection.getFulfillmentStatusId() );
        bffPurchaseOrderDto.setSupplierId( purchaseOrderAndItemProjection.getSupplierId() );
        bffPurchaseOrderDto.setSupplierName( purchaseOrderAndItemProjection.getSupplierName() );
        bffPurchaseOrderDto.setCreatedAt( instantToOffsetDateTime( purchaseOrderAndItemProjection.getCreatedAt() ) );
        bffPurchaseOrderDto.setCreatedBy( purchaseOrderAndItemProjection.getCreatedBy() );
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
        bffPurchaseOrderItemDto.setOrderItemSeqId( purchaseOrderAndItemProjection.getOrderItemSeqId() );
        bffPurchaseOrderItemDto.setExternalId( purchaseOrderAndItemProjection.getExternalId() );
        bffPurchaseOrderItemDto.setProductId( purchaseOrderAndItemProjection.getProductId() );
        bffPurchaseOrderItemDto.setSupplierProductId( purchaseOrderAndItemProjection.getSupplierProductId() );
        bffPurchaseOrderItemDto.setProductName( purchaseOrderAndItemProjection.getProductName() );
        bffPurchaseOrderItemDto.setGtin( purchaseOrderAndItemProjection.getGtin() );
        bffPurchaseOrderItemDto.setQuantity( purchaseOrderAndItemProjection.getQuantity() );
        bffPurchaseOrderItemDto.setCancelQuantity( purchaseOrderAndItemProjection.getCancelQuantity() );
        bffPurchaseOrderItemDto.setSelectedAmount( purchaseOrderAndItemProjection.getSelectedAmount() );
        bffPurchaseOrderItemDto.setUnitPrice( purchaseOrderAndItemProjection.getUnitPrice() );
        bffPurchaseOrderItemDto.setItemDescription( purchaseOrderAndItemProjection.getItemDescription() );
        bffPurchaseOrderItemDto.setComments( purchaseOrderAndItemProjection.getComments() );
        bffPurchaseOrderItemDto.setEstimatedShipDate( instantToOffsetDateTime( purchaseOrderAndItemProjection.getEstimatedShipDate() ) );
        bffPurchaseOrderItemDto.setEstimatedDeliveryDate( instantToOffsetDateTime( purchaseOrderAndItemProjection.getEstimatedDeliveryDate() ) );

        return bffPurchaseOrderItemDto;
    }

    @Override
    public BffPurchaseOrderFulfillmentDto toBffPurchaseOrderFulfillmentDto(BffPurchaseOrderFulfillmentProjection purchaseOrderFulfillmentProjection) {
        if ( purchaseOrderFulfillmentProjection == null ) {
            return null;
        }

        BffPurchaseOrderFulfillmentDto bffPurchaseOrderFulfillmentDto = new BffPurchaseOrderFulfillmentDto();

        bffPurchaseOrderFulfillmentDto.setReceiptId( purchaseOrderFulfillmentProjection.getReceiptId() );
        bffPurchaseOrderFulfillmentDto.setAllocatedQuantity( purchaseOrderFulfillmentProjection.getAllocatedQuantity() );
        bffPurchaseOrderFulfillmentDto.setReceivedAt( instantToOffsetDateTime( purchaseOrderFulfillmentProjection.getReceivedAt() ) );
        bffPurchaseOrderFulfillmentDto.setQaStatusId( purchaseOrderFulfillmentProjection.getQaStatusId() );

        return bffPurchaseOrderFulfillmentDto;
    }
}
