package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffDocumentDto;
import org.dddml.ffvtraceability.domain.BffReceivingDocumentDto;
import org.dddml.ffvtraceability.domain.BffReceivingItemDto;
import org.dddml.ffvtraceability.domain.repository.BffReceivingDocumentItemProjection;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class BffReceivingMapperImpl implements BffReceivingMapper {

    @Override
    public BffReceivingDocumentDto toBffReceivingDocumentDto(BffReceivingDocumentItemProjection documentItemProjection) {
        if ( documentItemProjection == null ) {
            return null;
        }

        BffReceivingDocumentDto bffReceivingDocumentDto = new BffReceivingDocumentDto();

        bffReceivingDocumentDto.setCreatedAt( instantToOffsetDateTime( documentItemProjection.getCreatedAt() ) );
        bffReceivingDocumentDto.setCreatedBy( documentItemProjection.getCreatedBy() );
        bffReceivingDocumentDto.setDestinationFacilityId( documentItemProjection.getDestinationFacilityId() );
        bffReceivingDocumentDto.setDestinationFacilityName( documentItemProjection.getDestinationFacilityName() );
        bffReceivingDocumentDto.setDocumentId( documentItemProjection.getDocumentId() );
        bffReceivingDocumentDto.setOriginFacilityId( documentItemProjection.getOriginFacilityId() );
        bffReceivingDocumentDto.setOriginFacilityName( documentItemProjection.getOriginFacilityName() );
        bffReceivingDocumentDto.setPartyIdFrom( documentItemProjection.getPartyIdFrom() );
        bffReceivingDocumentDto.setPartyIdTo( documentItemProjection.getPartyIdTo() );
        bffReceivingDocumentDto.setPartyNameFrom( documentItemProjection.getPartyNameFrom() );
        bffReceivingDocumentDto.setPrimaryOrderId( documentItemProjection.getPrimaryOrderId() );
        bffReceivingDocumentDto.setPrimaryReturnId( documentItemProjection.getPrimaryReturnId() );
        bffReceivingDocumentDto.setPrimaryShipGroupSeqId( documentItemProjection.getPrimaryShipGroupSeqId() );
        bffReceivingDocumentDto.setQaInspectionStatusId( documentItemProjection.getQaInspectionStatusId() );
        bffReceivingDocumentDto.setQaStatusId( documentItemProjection.getQaStatusId() );
        bffReceivingDocumentDto.setStatusId( documentItemProjection.getStatusId() );

        return bffReceivingDocumentDto;
    }

    @Override
    public BffReceivingItemDto toBffReceivingItemDto(BffReceivingDocumentItemProjection documentItemProjection) {
        if ( documentItemProjection == null ) {
            return null;
        }

        BffReceivingItemDto bffReceivingItemDto = new BffReceivingItemDto();

        bffReceivingItemDto.setCaseUomId( documentItemProjection.getCaseUomId() );
        bffReceivingItemDto.setCasesAccepted( documentItemProjection.getCasesAccepted() );
        bffReceivingItemDto.setCasesRejected( documentItemProjection.getCasesRejected() );
        bffReceivingItemDto.setComments( documentItemProjection.getComments() );
        bffReceivingItemDto.setDeleted( documentItemProjection.getDeleted() );
        bffReceivingItemDto.setGtin( documentItemProjection.getGtin() );
        bffReceivingItemDto.setInspectedBy( documentItemProjection.getInspectedBy() );
        bffReceivingItemDto.setInternalId( documentItemProjection.getInternalId() );
        bffReceivingItemDto.setItemDescription( documentItemProjection.getItemDescription() );
        bffReceivingItemDto.setLocationName( documentItemProjection.getLocationName() );
        bffReceivingItemDto.setLocationSeqId( documentItemProjection.getLocationSeqId() );
        bffReceivingItemDto.setLotId( documentItemProjection.getLotId() );
        bffReceivingItemDto.setOrderId( documentItemProjection.getOrderId() );
        bffReceivingItemDto.setOrderItemSeqId( documentItemProjection.getOrderItemSeqId() );
        bffReceivingItemDto.setOutstandingOrderQuantity( documentItemProjection.getOutstandingOrderQuantity() );
        bffReceivingItemDto.setPiecesIncluded( documentItemProjection.getPiecesIncluded() );
        bffReceivingItemDto.setProductId( documentItemProjection.getProductId() );
        bffReceivingItemDto.setProductName( documentItemProjection.getProductName() );
        bffReceivingItemDto.setQaInspectionStatusId( documentItemProjection.getQaInspectionStatusId() );
        bffReceivingItemDto.setQuantityAccepted( documentItemProjection.getQuantityAccepted() );
        bffReceivingItemDto.setQuantityIncluded( documentItemProjection.getQuantityIncluded() );
        bffReceivingItemDto.setQuantityRejected( documentItemProjection.getQuantityRejected() );
        bffReceivingItemDto.setQuantityUomId( documentItemProjection.getQuantityUomId() );
        bffReceivingItemDto.setReceiptId( documentItemProjection.getReceiptId() );
        bffReceivingItemDto.setRejectionId( documentItemProjection.getRejectionId() );
        bffReceivingItemDto.setReturnId( documentItemProjection.getReturnId() );
        bffReceivingItemDto.setReturnItemSeqId( documentItemProjection.getReturnItemSeqId() );
        bffReceivingItemDto.setShipmentId( documentItemProjection.getShipmentId() );
        bffReceivingItemDto.setShipmentItemSeqId( documentItemProjection.getShipmentItemSeqId() );
        bffReceivingItemDto.setShipmentPackageSeqId( documentItemProjection.getShipmentPackageSeqId() );
        bffReceivingItemDto.setSmallImageUrl( documentItemProjection.getSmallImageUrl() );

        return bffReceivingItemDto;
    }

    @Override
    public BffDocumentDto toReferenceDocument(BffReceivingDocumentItemProjection documentItemProjection) {
        if ( documentItemProjection == null ) {
            return null;
        }

        BffDocumentDto bffDocumentDto = new BffDocumentDto();

        bffDocumentDto.setDocumentId( documentItemProjection.getReferenceDocumentId() );
        bffDocumentDto.setComments( documentItemProjection.getReferenceComments() );
        bffDocumentDto.setDocumentLocation( documentItemProjection.getReferenceDocumentLocation() );
        bffDocumentDto.setDocumentText( documentItemProjection.getReferenceDocumentText() );

        return bffDocumentDto;
    }
}
