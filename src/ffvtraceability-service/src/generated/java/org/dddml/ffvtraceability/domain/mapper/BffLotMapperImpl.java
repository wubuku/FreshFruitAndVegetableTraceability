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

        bffLotDto.setActive( bffLotProjection.getActive() );
        bffLotDto.setExpirationDate( instantToOffsetDateTime( bffLotProjection.getExpirationDate() ) );
        bffLotDto.setGs1Batch( bffLotProjection.getGs1Batch() );
        bffLotDto.setGtin( bffLotProjection.getGtin() );
        bffLotDto.setHarvestDate( instantToOffsetDateTime( bffLotProjection.getHarvestDate() ) );
        bffLotDto.setInternalId( bffLotProjection.getInternalId() );
        bffLotDto.setLotId( bffLotProjection.getLotId() );
        bffLotDto.setPackDate( instantToOffsetDateTime( bffLotProjection.getPackDate() ) );
        bffLotDto.setPalletSscc( bffLotProjection.getPalletSscc() );
        bffLotDto.setQuantity( bffLotProjection.getQuantity() );
        bffLotDto.setSerialNumber( bffLotProjection.getSerialNumber() );
        bffLotDto.setSourceFacilityId( bffLotProjection.getSourceFacilityId() );

        return bffLotDto;
    }

    @Override
    public BffLotDto toBffLotDto(LotState lotState) {
        if ( lotState == null ) {
            return null;
        }

        BffLotDto bffLotDto = new BffLotDto();

        bffLotDto.setActive( lotState.getActive() );
        bffLotDto.setExpirationDate( lotState.getExpirationDate() );
        bffLotDto.setGs1Batch( lotState.getGs1Batch() );
        bffLotDto.setGtin( lotState.getGtin() );
        bffLotDto.setHarvestDate( lotState.getHarvestDate() );
        bffLotDto.setInternalId( lotState.getInternalId() );
        bffLotDto.setLotId( lotState.getLotId() );
        bffLotDto.setPackDate( lotState.getPackDate() );
        bffLotDto.setPalletSscc( lotState.getPalletSscc() );
        bffLotDto.setQuantity( lotState.getQuantity() );
        bffLotDto.setSerialNumber( lotState.getSerialNumber() );
        bffLotDto.setSourceFacilityId( lotState.getSourceFacilityId() );

        return bffLotDto;
    }

    @Override
    public AbstractLotCommand.SimpleCreateLot toCreateLot(BffLotDto bffLotDto) {
        if ( bffLotDto == null ) {
            return null;
        }

        AbstractLotCommand.SimpleCreateLot simpleCreateLot = new AbstractLotCommand.SimpleCreateLot();

        simpleCreateLot.setLotId( bffLotDto.getLotId() );
        simpleCreateLot.setActive( bffLotDto.getActive() );
        simpleCreateLot.setExpirationDate( bffLotDto.getExpirationDate() );
        simpleCreateLot.setGs1Batch( bffLotDto.getGs1Batch() );
        simpleCreateLot.setGtin( bffLotDto.getGtin() );
        simpleCreateLot.setHarvestDate( bffLotDto.getHarvestDate() );
        simpleCreateLot.setInternalId( bffLotDto.getInternalId() );
        simpleCreateLot.setPackDate( bffLotDto.getPackDate() );
        simpleCreateLot.setPalletSscc( bffLotDto.getPalletSscc() );
        simpleCreateLot.setQuantity( bffLotDto.getQuantity() );
        simpleCreateLot.setSerialNumber( bffLotDto.getSerialNumber() );
        simpleCreateLot.setSourceFacilityId( bffLotDto.getSourceFacilityId() );

        return simpleCreateLot;
    }

    @Override
    public AbstractLotCommand.SimpleMergePatchLot toMergePatchLot(BffLotDto bffLotDto) {
        if ( bffLotDto == null ) {
            return null;
        }

        AbstractLotCommand.SimpleMergePatchLot simpleMergePatchLot = new AbstractLotCommand.SimpleMergePatchLot();

        simpleMergePatchLot.setLotId( bffLotDto.getLotId() );
        simpleMergePatchLot.setActive( bffLotDto.getActive() );
        simpleMergePatchLot.setExpirationDate( bffLotDto.getExpirationDate() );
        simpleMergePatchLot.setGs1Batch( bffLotDto.getGs1Batch() );
        simpleMergePatchLot.setGtin( bffLotDto.getGtin() );
        simpleMergePatchLot.setHarvestDate( bffLotDto.getHarvestDate() );
        simpleMergePatchLot.setInternalId( bffLotDto.getInternalId() );
        simpleMergePatchLot.setPackDate( bffLotDto.getPackDate() );
        simpleMergePatchLot.setPalletSscc( bffLotDto.getPalletSscc() );
        simpleMergePatchLot.setQuantity( bffLotDto.getQuantity() );
        simpleMergePatchLot.setSerialNumber( bffLotDto.getSerialNumber() );
        simpleMergePatchLot.setSourceFacilityId( bffLotDto.getSourceFacilityId() );

        return simpleMergePatchLot;
    }
}
