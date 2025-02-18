package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffShipmentBoxTypeDto;
import org.dddml.ffvtraceability.domain.repository.BffShipmentBoxTypeProjection;
import org.dddml.ffvtraceability.domain.shipmentboxtype.AbstractShipmentBoxTypeCommand;
import org.dddml.ffvtraceability.domain.shipmentboxtype.ShipmentBoxTypeState;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class BffShipmentBoxTypeMapperImpl implements BffShipmentBoxTypeMapper {

    @Override
    public BffShipmentBoxTypeDto toBffShipmentBoxTypeDto(BffShipmentBoxTypeProjection bffShipmentBoxTypeProjection) {
        if ( bffShipmentBoxTypeProjection == null ) {
            return null;
        }

        BffShipmentBoxTypeDto bffShipmentBoxTypeDto = new BffShipmentBoxTypeDto();

        bffShipmentBoxTypeDto.setActive( bffShipmentBoxTypeProjection.getActive() );
        bffShipmentBoxTypeDto.setBoxHeight( bffShipmentBoxTypeProjection.getBoxHeight() );
        bffShipmentBoxTypeDto.setBoxLength( bffShipmentBoxTypeProjection.getBoxLength() );
        bffShipmentBoxTypeDto.setBoxTypeName( bffShipmentBoxTypeProjection.getBoxTypeName() );
        bffShipmentBoxTypeDto.setBoxWeight( bffShipmentBoxTypeProjection.getBoxWeight() );
        bffShipmentBoxTypeDto.setBoxWidth( bffShipmentBoxTypeProjection.getBoxWidth() );
        bffShipmentBoxTypeDto.setDescription( bffShipmentBoxTypeProjection.getDescription() );
        bffShipmentBoxTypeDto.setDimensionUomId( bffShipmentBoxTypeProjection.getDimensionUomId() );
        bffShipmentBoxTypeDto.setShipmentBoxTypeId( bffShipmentBoxTypeProjection.getShipmentBoxTypeId() );
        bffShipmentBoxTypeDto.setWeightUomId( bffShipmentBoxTypeProjection.getWeightUomId() );

        return bffShipmentBoxTypeDto;
    }

    @Override
    public BffShipmentBoxTypeDto toBffShipmentBoxTypeDto(ShipmentBoxTypeState shipmentBoxTypeState) {
        if ( shipmentBoxTypeState == null ) {
            return null;
        }

        BffShipmentBoxTypeDto bffShipmentBoxTypeDto = new BffShipmentBoxTypeDto();

        bffShipmentBoxTypeDto.setActive( shipmentBoxTypeState.getActive() );
        bffShipmentBoxTypeDto.setBoxHeight( shipmentBoxTypeState.getBoxHeight() );
        bffShipmentBoxTypeDto.setBoxLength( shipmentBoxTypeState.getBoxLength() );
        bffShipmentBoxTypeDto.setBoxTypeName( shipmentBoxTypeState.getBoxTypeName() );
        bffShipmentBoxTypeDto.setBoxWeight( shipmentBoxTypeState.getBoxWeight() );
        bffShipmentBoxTypeDto.setBoxWidth( shipmentBoxTypeState.getBoxWidth() );
        bffShipmentBoxTypeDto.setDescription( shipmentBoxTypeState.getDescription() );
        bffShipmentBoxTypeDto.setDimensionUomId( shipmentBoxTypeState.getDimensionUomId() );
        bffShipmentBoxTypeDto.setShipmentBoxTypeId( shipmentBoxTypeState.getShipmentBoxTypeId() );
        bffShipmentBoxTypeDto.setWeightUomId( shipmentBoxTypeState.getWeightUomId() );

        return bffShipmentBoxTypeDto;
    }

    @Override
    public AbstractShipmentBoxTypeCommand.SimpleCreateShipmentBoxType toCreateShipmentBoxType(BffShipmentBoxTypeDto bffShipmentBoxTypeDto) {
        if ( bffShipmentBoxTypeDto == null ) {
            return null;
        }

        AbstractShipmentBoxTypeCommand.SimpleCreateShipmentBoxType simpleCreateShipmentBoxType = new AbstractShipmentBoxTypeCommand.SimpleCreateShipmentBoxType();

        simpleCreateShipmentBoxType.setShipmentBoxTypeId( bffShipmentBoxTypeDto.getShipmentBoxTypeId() );
        simpleCreateShipmentBoxType.setActive( bffShipmentBoxTypeDto.getActive() );
        simpleCreateShipmentBoxType.setBoxHeight( bffShipmentBoxTypeDto.getBoxHeight() );
        simpleCreateShipmentBoxType.setBoxLength( bffShipmentBoxTypeDto.getBoxLength() );
        simpleCreateShipmentBoxType.setBoxTypeName( bffShipmentBoxTypeDto.getBoxTypeName() );
        simpleCreateShipmentBoxType.setBoxWeight( bffShipmentBoxTypeDto.getBoxWeight() );
        simpleCreateShipmentBoxType.setBoxWidth( bffShipmentBoxTypeDto.getBoxWidth() );
        simpleCreateShipmentBoxType.setDescription( bffShipmentBoxTypeDto.getDescription() );
        simpleCreateShipmentBoxType.setDimensionUomId( bffShipmentBoxTypeDto.getDimensionUomId() );
        simpleCreateShipmentBoxType.setWeightUomId( bffShipmentBoxTypeDto.getWeightUomId() );

        return simpleCreateShipmentBoxType;
    }

    @Override
    public AbstractShipmentBoxTypeCommand.SimpleMergePatchShipmentBoxType toMergePatchShipmentBoxType(BffShipmentBoxTypeDto bffShipmentBoxTypeDto) {
        if ( bffShipmentBoxTypeDto == null ) {
            return null;
        }

        AbstractShipmentBoxTypeCommand.SimpleMergePatchShipmentBoxType simpleMergePatchShipmentBoxType = new AbstractShipmentBoxTypeCommand.SimpleMergePatchShipmentBoxType();

        simpleMergePatchShipmentBoxType.setShipmentBoxTypeId( bffShipmentBoxTypeDto.getShipmentBoxTypeId() );
        simpleMergePatchShipmentBoxType.setActive( bffShipmentBoxTypeDto.getActive() );
        simpleMergePatchShipmentBoxType.setBoxHeight( bffShipmentBoxTypeDto.getBoxHeight() );
        simpleMergePatchShipmentBoxType.setBoxLength( bffShipmentBoxTypeDto.getBoxLength() );
        simpleMergePatchShipmentBoxType.setBoxTypeName( bffShipmentBoxTypeDto.getBoxTypeName() );
        simpleMergePatchShipmentBoxType.setBoxWeight( bffShipmentBoxTypeDto.getBoxWeight() );
        simpleMergePatchShipmentBoxType.setBoxWidth( bffShipmentBoxTypeDto.getBoxWidth() );
        simpleMergePatchShipmentBoxType.setDescription( bffShipmentBoxTypeDto.getDescription() );
        simpleMergePatchShipmentBoxType.setDimensionUomId( bffShipmentBoxTypeDto.getDimensionUomId() );
        simpleMergePatchShipmentBoxType.setWeightUomId( bffShipmentBoxTypeDto.getWeightUomId() );

        return simpleMergePatchShipmentBoxType;
    }
}
