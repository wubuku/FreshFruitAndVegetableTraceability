package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffUomDto;
import org.dddml.ffvtraceability.domain.repository.BffUomProjection;
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
}
