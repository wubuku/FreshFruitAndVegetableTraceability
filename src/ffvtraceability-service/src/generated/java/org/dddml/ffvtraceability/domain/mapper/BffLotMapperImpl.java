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

        bffLotDto.setLotId( bffLotProjection.getLotId() );
        bffLotDto.setGs1Batch( bffLotProjection.getGs1Batch() );
        bffLotDto.setQuantity( bffLotProjection.getQuantity() );
        bffLotDto.setExpirationDate( instantToOffsetDateTime( bffLotProjection.getExpirationDate() ) );
        bffLotDto.setActive( bffLotProjection.getActive() );
        bffLotDto.setInternalId( bffLotProjection.getInternalId() );
        bffLotDto.setGtin( bffLotProjection.getGtin() );
        bffLotDto.setSourceFacilityId( bffLotProjection.getSourceFacilityId() );
        bffLotDto.setPalletSscc( bffLotProjection.getPalletSscc() );
        bffLotDto.setPackDate( instantToOffsetDateTime( bffLotProjection.getPackDate() ) );
        bffLotDto.setHarvestDate( instantToOffsetDateTime( bffLotProjection.getHarvestDate() ) );
        bffLotDto.setSerialNumber( bffLotProjection.getSerialNumber() );

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
        bffLotDto.setInternalId( lotState.getInternalId() );
        bffLotDto.setGtin( lotState.getGtin() );
        bffLotDto.setSourceFacilityId( lotState.getSourceFacilityId() );
        bffLotDto.setPalletSscc( lotState.getPalletSscc() );
        bffLotDto.setPackDate( lotState.getPackDate() );
        bffLotDto.setHarvestDate( lotState.getHarvestDate() );
        bffLotDto.setSerialNumber( lotState.getSerialNumber() );

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
        simpleCreateLot.setGtin( bffLotDto.getGtin() );
        simpleCreateLot.setGs1Batch( bffLotDto.getGs1Batch() );
        simpleCreateLot.setSourceFacilityId( bffLotDto.getSourceFacilityId() );
        simpleCreateLot.setInternalId( bffLotDto.getInternalId() );
        simpleCreateLot.setPalletSscc( bffLotDto.getPalletSscc() );
        simpleCreateLot.setPackDate( bffLotDto.getPackDate() );
        simpleCreateLot.setHarvestDate( bffLotDto.getHarvestDate() );
        simpleCreateLot.setSerialNumber( bffLotDto.getSerialNumber() );

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
        simpleMergePatchLot.setGtin( bffLotDto.getGtin() );
        simpleMergePatchLot.setGs1Batch( bffLotDto.getGs1Batch() );
        simpleMergePatchLot.setSourceFacilityId( bffLotDto.getSourceFacilityId() );
        simpleMergePatchLot.setInternalId( bffLotDto.getInternalId() );
        simpleMergePatchLot.setPalletSscc( bffLotDto.getPalletSscc() );
        simpleMergePatchLot.setPackDate( bffLotDto.getPackDate() );
        simpleMergePatchLot.setHarvestDate( bffLotDto.getHarvestDate() );
        simpleMergePatchLot.setSerialNumber( bffLotDto.getSerialNumber() );

        return simpleMergePatchLot;
    }
}
