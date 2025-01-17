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
        bffLotDto.setActive( bffLotProjection.getActive() );

        return bffLotDto;
    }

    @Override
    public BffLotDto toBffLotDto(LotState lotState) {
        if ( lotState == null ) {
            return null;
        }

        BffLotDto bffLotDto = new BffLotDto();

        bffLotDto.setLotId( lotState.getLotId() );
        bffLotDto.setGs1Batch( lotState.getGs1Batch() );
        bffLotDto.setQuantity( lotState.getQuantity() );
        bffLotDto.setExpirationDate( lotState.getExpirationDate() );
        bffLotDto.setActive( lotState.getActive() );

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
        simpleCreateLot.setActive( bffLotDto.getActive() );
        simpleCreateLot.setGs1Batch( bffLotDto.getGs1Batch() );

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
        simpleMergePatchLot.setActive( bffLotDto.getActive() );
        simpleMergePatchLot.setGs1Batch( bffLotDto.getGs1Batch() );

        return simpleMergePatchLot;
    }
}
