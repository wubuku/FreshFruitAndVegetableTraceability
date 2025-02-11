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

        bffReceivingDocumentDto.setDocumentId( documentItemProjection.getDocumentId() );
        bffReceivingDocumentDto.setStatusId( documentItemProjection.getStatusId() );
        bffReceivingDocumentDto.setPartyIdTo( documentItemProjection.getPartyIdTo() );
        bffReceivingDocumentDto.setPartyIdFrom( documentItemProjection.getPartyIdFrom() );
        bffReceivingDocumentDto.setPartyNameFrom( documentItemProjection.getPartyNameFrom() );
        bffReceivingDocumentDto.setOriginFacilityId( documentItemProjection.getOriginFacilityId() );
        bffReceivingDocumentDto.setOriginFacilityName( documentItemProjection.getOriginFacilityName() );
        bffReceivingDocumentDto.setDestinationFacilityId( documentItemProjection.getDestinationFacilityId() );
        bffReceivingDocumentDto.setDestinationFacilityName( documentItemProjection.getDestinationFacilityName() );
        bffReceivingDocumentDto.setPrimaryOrderId( documentItemProjection.getPrimaryOrderId() );
        bffReceivingDocumentDto.setPrimaryReturnId( documentItemProjection.getPrimaryReturnId() );
        bffReceivingDocumentDto.setPrimaryShipGroupSeqId( documentItemProjection.getPrimaryShipGroupSeqId() );
        bffReceivingDocumentDto.setQaStatusId( documentItemProjection.getQaStatusId() );
        bffReceivingDocumentDto.setQaInspectionStatusId( documentItemProjection.getQaInspectionStatusId() );
        bffReceivingDocumentDto.setCreatedAt( instantToOffsetDateTime( documentItemProjection.getCreatedAt() ) );

        return bffReceivingDocumentDto;
    }

    @Override
    public BffReceivingItemDto toBffReceivingItemDto(BffReceivingDocumentItemProjection documentItemProjection) {
        if ( documentItemProjection == null ) {
            return null;
        }

        BffReceivingItemDto bffReceivingItemDto = new BffReceivingItemDto();

        bffReceivingItemDto.setReceiptId( documentItemProjection.getReceiptId() );
        bffReceivingItemDto.setProductId( documentItemProjection.getProductId() );
        bffReceivingItemDto.setProductName( documentItemProjection.getProductName() );
        bffReceivingItemDto.setGtin( documentItemProjection.getGtin() );
        bffReceivingItemDto.setLotId( documentItemProjection.getLotId() );
        bffReceivingItemDto.setLocationSeqId( documentItemProjection.getLocationSeqId() );
        bffReceivingItemDto.setItemDescription( documentItemProjection.getItemDescription() );
        bffReceivingItemDto.setQuantityAccepted( documentItemProjection.getQuantityAccepted() );
        bffReceivingItemDto.setQuantityRejected( documentItemProjection.getQuantityRejected() );
        bffReceivingItemDto.setCasesAccepted( documentItemProjection.getCasesAccepted() );
        bffReceivingItemDto.setCasesRejected( documentItemProjection.getCasesRejected() );
        bffReceivingItemDto.setOrderId( documentItemProjection.getOrderId() );
        bffReceivingItemDto.setOrderItemSeqId( documentItemProjection.getOrderItemSeqId() );
        bffReceivingItemDto.setReturnId( documentItemProjection.getReturnId() );
        bffReceivingItemDto.setReturnItemSeqId( documentItemProjection.getReturnItemSeqId() );
        bffReceivingItemDto.setRejectionId( documentItemProjection.getRejectionId() );
        bffReceivingItemDto.setShipmentId( documentItemProjection.getShipmentId() );
        bffReceivingItemDto.setShipmentItemSeqId( documentItemProjection.getShipmentItemSeqId() );
        bffReceivingItemDto.setShipmentPackageSeqId( documentItemProjection.getShipmentPackageSeqId() );
        bffReceivingItemDto.setOutstandingOrderQuantity( documentItemProjection.getOutstandingOrderQuantity() );
        bffReceivingItemDto.setQaInspectionStatusId( documentItemProjection.getQaInspectionStatusId() );
        bffReceivingItemDto.setDeleted( documentItemProjection.getDeleted() );

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
