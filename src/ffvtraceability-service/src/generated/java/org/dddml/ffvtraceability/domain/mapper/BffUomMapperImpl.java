package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffUomDto;
import org.dddml.ffvtraceability.domain.repository.BffUomProjection;
import org.dddml.ffvtraceability.domain.uom.UomState;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class BffUomMapperImpl implements BffUomMapper {

    @Override
    public BffUomDto toBffUomDto(BffUomProjection bffUomProjection) {
        if ( bffUomProjection == null ) {
            return null;
        }

        BffUomDto bffUomDto = new BffUomDto();

        bffUomDto.setUomId( bffUomProjection.getUomId() );
        bffUomDto.setUomTypeId( bffUomProjection.getUomTypeId() );
        bffUomDto.setAbbreviation( bffUomProjection.getAbbreviation() );
        bffUomDto.setNumericCode( bffUomProjection.getNumericCode() );
        bffUomDto.setDescription( bffUomProjection.getDescription() );
        bffUomDto.setGs1AI( bffUomProjection.getGs1AI() );

        return bffUomDto;
    }

    @Override
    public BffUomDto toBffUomDto(UomState uomState) {
        if ( uomState == null ) {
            return null;
        }

        BffUomDto bffUomDto = new BffUomDto();

        bffUomDto.setUomId( uomState.getUomId() );
        bffUomDto.setUomTypeId( uomState.getUomTypeId() );
        bffUomDto.setAbbreviation( uomState.getAbbreviation() );
        bffUomDto.setNumericCode( uomState.getNumericCode() );
        bffUomDto.setDescription( uomState.getDescription() );
        bffUomDto.setGs1AI( uomState.getGs1AI() );

        return bffUomDto;
    }
}
