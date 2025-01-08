package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffGeoDto;
import org.dddml.ffvtraceability.domain.repository.BffGeoProjection;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class BffGeoMapperImpl implements BffGeoMapper {

    @Override
    public BffGeoDto toBffGeoDto(BffGeoProjection bffGeoProjection) {
        if ( bffGeoProjection == null ) {
            return null;
        }

        BffGeoDto bffGeoDto = new BffGeoDto();

        bffGeoDto.setGeoId( bffGeoProjection.getGeoId() );
        bffGeoDto.setGeoTypeId( bffGeoProjection.getGeoTypeId() );
        bffGeoDto.setGeoName( bffGeoProjection.getGeoName() );
        bffGeoDto.setGeoCode( bffGeoProjection.getGeoCode() );
        bffGeoDto.setGeoSecCode( bffGeoProjection.getGeoSecCode() );
        bffGeoDto.setAbbreviation( bffGeoProjection.getAbbreviation() );
        bffGeoDto.setWellKnownText( bffGeoProjection.getWellKnownText() );
        bffGeoDto.setSequenceNumber( bffGeoProjection.getSequenceNumber() );
        bffGeoDto.setParentGeoId( bffGeoProjection.getParentGeoId() );

        return bffGeoDto;
    }
}
