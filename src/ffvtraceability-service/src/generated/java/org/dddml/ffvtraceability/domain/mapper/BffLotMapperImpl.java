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
        bffLotDto.setGs1Batch( bffLotProjection.getGs1Batch() );
        bffLotDto.setLotId( bffLotProjection.getLotId() );
        bffLotDto.setQuantity( bffLotProjection.getQuantity() );

        return bffLotDto;
    }

    @Override
    public BffLotDto toBffLotDto(LotState lotState) {
        if ( lotState == null ) {
            return null;
        }

        BffLotDto bffLotDto = new BffLotDto();

        bffLotDto.setExpirationDate( lotState.getExpirationDate() );
        bffLotDto.setLotId( lotState.getLotId() );
        bffLotDto.setQuantity( lotState.getQuantity() );

        return bffLotDto;
    }

    @Override
    public AbstractLotCommand.SimpleCreateLot toCreateLot(BffLotDto bffLotDto) {
        if ( bffLotDto == null ) {
            return null;
        }

        AbstractLotCommand.SimpleCreateLot simpleCreateLot = new AbstractLotCommand.SimpleCreateLot();

        simpleCreateLot.setLotId( bffLotDto.getLotId() );
        simpleCreateLot.setExpirationDate( bffLotDto.getExpirationDate() );
        simpleCreateLot.setQuantity( bffLotDto.getQuantity() );

        return simpleCreateLot;
    }

    @Override
    public AbstractLotCommand.SimpleMergePatchLot toMergePatchLot(BffLotDto bffLotDto) {
        if ( bffLotDto == null ) {
            return null;
        }

        AbstractLotCommand.SimpleMergePatchLot simpleMergePatchLot = new AbstractLotCommand.SimpleMergePatchLot();

        simpleMergePatchLot.setLotId( bffLotDto.getLotId() );
        simpleMergePatchLot.setExpirationDate( bffLotDto.getExpirationDate() );
        simpleMergePatchLot.setQuantity( bffLotDto.getQuantity() );

        return simpleMergePatchLot;
    }
}
