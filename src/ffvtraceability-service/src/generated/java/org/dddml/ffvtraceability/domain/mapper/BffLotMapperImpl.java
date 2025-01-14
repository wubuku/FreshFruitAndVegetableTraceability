package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffLotDto;
import org.dddml.ffvtraceability.domain.lot.AbstractLotCommand;
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

    @Override
    public AbstractLotCommand.SimpleCreateLot toCreateLot(BffLotDto bffLotDto) {
        if ( bffLotDto == null ) {
            return null;
        }

        AbstractLotCommand.SimpleCreateLot simpleCreateLot = new AbstractLotCommand.SimpleCreateLot();

        simpleCreateLot.setLotId( bffLotDto.getLotId() );
        simpleCreateLot.setQuantity( bffLotDto.getQuantity() );
        simpleCreateLot.setExpirationDate( bffLotDto.getExpirationDate() );

        return simpleCreateLot;
    }

    @Override
    public AbstractLotCommand.SimpleMergePatchLot toMergePatchLot(BffLotDto bffLotDto) {
        if ( bffLotDto == null ) {
            return null;
        }

        AbstractLotCommand.SimpleMergePatchLot simpleMergePatchLot = new AbstractLotCommand.SimpleMergePatchLot();

        simpleMergePatchLot.setLotId( bffLotDto.getLotId() );
        simpleMergePatchLot.setQuantity( bffLotDto.getQuantity() );
        simpleMergePatchLot.setExpirationDate( bffLotDto.getExpirationDate() );

        return simpleMergePatchLot;
    }
}
