package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffLotDto;
import org.dddml.ffvtraceability.domain.lot.LotState;
import org.dddml.ffvtraceability.domain.repository.BffLotProjection;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class BffLotMapperImpl implements BffLotMapper {

    @Override
    public BffLotDto toBffLotDto(BffLotProjection bffLotProjection) {
        if ( bffLotProjection == null ) {
            return null;
        }

        BffLotDto bffLotDto = new BffLotDto();

        bffLotDto.setExpirationDate( instantToOffsetDateTime( bffLotProjection.getExpirationDateInstant() ) );
        bffLotDto.setLotId( bffLotProjection.getLotId() );
        bffLotDto.setGs1Batch( bffLotProjection.getGs1Batch() );
        bffLotDto.setQuantity( bffLotProjection.getQuantity() );

        return bffLotDto;
    }

    @Override
    public BffLotDto toBffLotDto(LotState lotState) {
        if ( lotState == null ) {
            return null;
        }

        BffLotDto bffLotDto = new BffLotDto();

        bffLotDto.setLotId( lotState.getLotId() );
        bffLotDto.setQuantity( lotState.getQuantity() );
        bffLotDto.setExpirationDate( lotState.getExpirationDate() );

        return bffLotDto;
    }
}
