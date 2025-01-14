package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffShipmentBoxTypeDto;
import org.dddml.ffvtraceability.domain.repository.BffShipmentBoxTypeProjection;
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
}
