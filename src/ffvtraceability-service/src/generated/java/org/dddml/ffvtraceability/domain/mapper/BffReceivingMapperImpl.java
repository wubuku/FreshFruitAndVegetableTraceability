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
        bffReceivingDocumentDto.setDestinationFacilityId( documentItemProjection.getDestinationFacilityId() );
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

        bffReceivingItemDto.setCasesAccepted( documentItemProjection.getCasesAccepted() );
        bffReceivingItemDto.setCasesRejected( documentItemProjection.getCasesRejected() );
        bffReceivingItemDto.setGtin( documentItemProjection.getGtin() );
        bffReceivingItemDto.setItemDescription( documentItemProjection.getItemDescription() );
        bffReceivingItemDto.setLocationSeqId( documentItemProjection.getLocationSeqId() );
        bffReceivingItemDto.setLotId( documentItemProjection.getLotId() );
        bffReceivingItemDto.setOrderId( documentItemProjection.getOrderId() );
        bffReceivingItemDto.setOrderItemSeqId( documentItemProjection.getOrderItemSeqId() );
        bffReceivingItemDto.setOutstandingOrderQuantity( documentItemProjection.getOutstandingOrderQuantity() );
        bffReceivingItemDto.setProductId( documentItemProjection.getProductId() );
        bffReceivingItemDto.setProductName( documentItemProjection.getProductName() );
        bffReceivingItemDto.setQaInspectionStatusId( documentItemProjection.getQaInspectionStatusId() );
        bffReceivingItemDto.setQuantityAccepted( documentItemProjection.getQuantityAccepted() );
        bffReceivingItemDto.setQuantityRejected( documentItemProjection.getQuantityRejected() );
        bffReceivingItemDto.setReceiptId( documentItemProjection.getReceiptId() );
        bffReceivingItemDto.setRejectionId( documentItemProjection.getRejectionId() );
        bffReceivingItemDto.setReturnId( documentItemProjection.getReturnId() );
        bffReceivingItemDto.setReturnItemSeqId( documentItemProjection.getReturnItemSeqId() );
        bffReceivingItemDto.setShipmentId( documentItemProjection.getShipmentId() );
        bffReceivingItemDto.setShipmentItemSeqId( documentItemProjection.getShipmentItemSeqId() );
        bffReceivingItemDto.setShipmentPackageSeqId( documentItemProjection.getShipmentPackageSeqId() );

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
