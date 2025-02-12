package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffQaInspectionDto;
import org.dddml.ffvtraceability.domain.qainspection.QaInspectionState;
import org.dddml.ffvtraceability.domain.repository.BffQaInspectionProjection;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class BffQaInspectionMapperImpl implements BffQaInspectionMapper {

    @Override
    public BffQaInspectionDto toBffQaInspectionDto(BffQaInspectionProjection bffQaInspectionProjection) {
        if ( bffQaInspectionProjection == null ) {
            return null;
        }

        BffQaInspectionDto bffQaInspectionDto = new BffQaInspectionDto();

        bffQaInspectionDto.setComments( bffQaInspectionProjection.getComments() );
        bffQaInspectionDto.setCreatedAt( instantToOffsetDateTime( bffQaInspectionProjection.getCreatedAt() ) );
        bffQaInspectionDto.setCreatedBy( bffQaInspectionProjection.getCreatedBy() );
        bffQaInspectionDto.setInspectedAt( instantToOffsetDateTime( bffQaInspectionProjection.getInspectedAt() ) );
        bffQaInspectionDto.setInspectedBy( bffQaInspectionProjection.getInspectedBy() );
        bffQaInspectionDto.setInspectionFacilityId( bffQaInspectionProjection.getInspectionFacilityId() );
        bffQaInspectionDto.setInspectionTypeId( bffQaInspectionProjection.getInspectionTypeId() );
        bffQaInspectionDto.setLotId( bffQaInspectionProjection.getLotId() );
        bffQaInspectionDto.setProductId( bffQaInspectionProjection.getProductId() );
        bffQaInspectionDto.setQaInspectionId( bffQaInspectionProjection.getQaInspectionId() );
        bffQaInspectionDto.setReceiptId( bffQaInspectionProjection.getReceiptId() );
        bffQaInspectionDto.setStatusId( bffQaInspectionProjection.getStatusId() );
        bffQaInspectionDto.setSupplierId( bffQaInspectionProjection.getSupplierId() );
        bffQaInspectionDto.setUpdatedAt( instantToOffsetDateTime( bffQaInspectionProjection.getUpdatedAt() ) );

        return bffQaInspectionDto;
    }

    @Override
    public BffQaInspectionDto toBffQaInspectionDto(QaInspectionState qaInspectionState) {
        if ( qaInspectionState == null ) {
            return null;
        }

        BffQaInspectionDto bffQaInspectionDto = new BffQaInspectionDto();

        bffQaInspectionDto.setComments( qaInspectionState.getComments() );
        bffQaInspectionDto.setCreatedAt( qaInspectionState.getCreatedAt() );
        bffQaInspectionDto.setCreatedBy( qaInspectionState.getCreatedBy() );
        bffQaInspectionDto.setInspectedAt( qaInspectionState.getInspectedAt() );
        bffQaInspectionDto.setInspectedBy( qaInspectionState.getInspectedBy() );
        bffQaInspectionDto.setInspectionFacilityId( qaInspectionState.getInspectionFacilityId() );
        bffQaInspectionDto.setInspectionTypeId( qaInspectionState.getInspectionTypeId() );
        bffQaInspectionDto.setQaInspectionId( qaInspectionState.getQaInspectionId() );
        bffQaInspectionDto.setReceiptId( qaInspectionState.getReceiptId() );
        bffQaInspectionDto.setStatusId( qaInspectionState.getStatusId() );
        bffQaInspectionDto.setUpdatedAt( qaInspectionState.getUpdatedAt() );

        return bffQaInspectionDto;
    }
}
