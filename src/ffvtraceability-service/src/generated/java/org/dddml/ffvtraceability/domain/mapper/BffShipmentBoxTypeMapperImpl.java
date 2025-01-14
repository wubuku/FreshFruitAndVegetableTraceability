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

        bffShipmentBoxTypeDto.setShipmentBoxTypeId( bffShipmentBoxTypeProjection.getShipmentBoxTypeId() );
        bffShipmentBoxTypeDto.setDescription( bffShipmentBoxTypeProjection.getDescription() );
        bffShipmentBoxTypeDto.setDimensionUomId( bffShipmentBoxTypeProjection.getDimensionUomId() );
        bffShipmentBoxTypeDto.setBoxLength( bffShipmentBoxTypeProjection.getBoxLength() );
        bffShipmentBoxTypeDto.setBoxWidth( bffShipmentBoxTypeProjection.getBoxWidth() );
        bffShipmentBoxTypeDto.setBoxHeight( bffShipmentBoxTypeProjection.getBoxHeight() );
        bffShipmentBoxTypeDto.setWeightUomId( bffShipmentBoxTypeProjection.getWeightUomId() );
        bffShipmentBoxTypeDto.setBoxWeight( bffShipmentBoxTypeProjection.getBoxWeight() );
        bffShipmentBoxTypeDto.setActive( bffShipmentBoxTypeProjection.getActive() );
        bffShipmentBoxTypeDto.setBoxTypeName( bffShipmentBoxTypeProjection.getBoxTypeName() );

        return bffShipmentBoxTypeDto;
    }

    @Override
    public BffShipmentBoxTypeDto toBffShipmentBoxTypeDto(ShipmentBoxTypeState shipmentBoxTypeState) {
        if ( shipmentBoxTypeState == null ) {
            return null;
        }

        BffShipmentBoxTypeDto bffShipmentBoxTypeDto = new BffShipmentBoxTypeDto();

        bffShipmentBoxTypeDto.setShipmentBoxTypeId( shipmentBoxTypeState.getShipmentBoxTypeId() );
        bffShipmentBoxTypeDto.setDescription( shipmentBoxTypeState.getDescription() );
        bffShipmentBoxTypeDto.setDimensionUomId( shipmentBoxTypeState.getDimensionUomId() );
        bffShipmentBoxTypeDto.setBoxLength( shipmentBoxTypeState.getBoxLength() );
        bffShipmentBoxTypeDto.setBoxWidth( shipmentBoxTypeState.getBoxWidth() );
        bffShipmentBoxTypeDto.setBoxHeight( shipmentBoxTypeState.getBoxHeight() );
        bffShipmentBoxTypeDto.setWeightUomId( shipmentBoxTypeState.getWeightUomId() );
        bffShipmentBoxTypeDto.setBoxWeight( shipmentBoxTypeState.getBoxWeight() );
        bffShipmentBoxTypeDto.setBoxTypeName( shipmentBoxTypeState.getBoxTypeName() );

        return bffShipmentBoxTypeDto;
    }

    @Override
    public AbstractShipmentBoxTypeCommand.SimpleCreateShipmentBoxType toCreateShipmentBoxType(BffShipmentBoxTypeDto bffShipmentBoxTypeDto) {
        if ( bffShipmentBoxTypeDto == null ) {
            return null;
        }

        AbstractShipmentBoxTypeCommand.SimpleCreateShipmentBoxType simpleCreateShipmentBoxType = new AbstractShipmentBoxTypeCommand.SimpleCreateShipmentBoxType();

        simpleCreateShipmentBoxType.setShipmentBoxTypeId( bffShipmentBoxTypeDto.getShipmentBoxTypeId() );
        simpleCreateShipmentBoxType.setDescription( bffShipmentBoxTypeDto.getDescription() );
        simpleCreateShipmentBoxType.setDimensionUomId( bffShipmentBoxTypeDto.getDimensionUomId() );
        simpleCreateShipmentBoxType.setBoxLength( bffShipmentBoxTypeDto.getBoxLength() );
        simpleCreateShipmentBoxType.setBoxWidth( bffShipmentBoxTypeDto.getBoxWidth() );
        simpleCreateShipmentBoxType.setBoxHeight( bffShipmentBoxTypeDto.getBoxHeight() );
        simpleCreateShipmentBoxType.setWeightUomId( bffShipmentBoxTypeDto.getWeightUomId() );
        simpleCreateShipmentBoxType.setBoxWeight( bffShipmentBoxTypeDto.getBoxWeight() );
        simpleCreateShipmentBoxType.setBoxTypeName( bffShipmentBoxTypeDto.getBoxTypeName() );

        return simpleCreateShipmentBoxType;
    }

    @Override
    public AbstractShipmentBoxTypeCommand.SimpleMergePatchShipmentBoxType toMergePatchShipmentBoxType(BffShipmentBoxTypeDto bffShipmentBoxTypeDto) {
        if ( bffShipmentBoxTypeDto == null ) {
            return null;
        }

        AbstractShipmentBoxTypeCommand.SimpleMergePatchShipmentBoxType simpleMergePatchShipmentBoxType = new AbstractShipmentBoxTypeCommand.SimpleMergePatchShipmentBoxType();

        simpleMergePatchShipmentBoxType.setShipmentBoxTypeId( bffShipmentBoxTypeDto.getShipmentBoxTypeId() );
        simpleMergePatchShipmentBoxType.setDescription( bffShipmentBoxTypeDto.getDescription() );
        simpleMergePatchShipmentBoxType.setDimensionUomId( bffShipmentBoxTypeDto.getDimensionUomId() );
        simpleMergePatchShipmentBoxType.setBoxLength( bffShipmentBoxTypeDto.getBoxLength() );
        simpleMergePatchShipmentBoxType.setBoxWidth( bffShipmentBoxTypeDto.getBoxWidth() );
        simpleMergePatchShipmentBoxType.setBoxHeight( bffShipmentBoxTypeDto.getBoxHeight() );
        simpleMergePatchShipmentBoxType.setWeightUomId( bffShipmentBoxTypeDto.getWeightUomId() );
        simpleMergePatchShipmentBoxType.setBoxWeight( bffShipmentBoxTypeDto.getBoxWeight() );
        simpleMergePatchShipmentBoxType.setBoxTypeName( bffShipmentBoxTypeDto.getBoxTypeName() );

        return simpleMergePatchShipmentBoxType;
    }
}
