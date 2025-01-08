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

        bffGeoDto.setAbbreviation( bffGeoProjection.getAbbreviation() );
        bffGeoDto.setGeoCode( bffGeoProjection.getGeoCode() );
        bffGeoDto.setGeoId( bffGeoProjection.getGeoId() );
        bffGeoDto.setGeoName( bffGeoProjection.getGeoName() );
        bffGeoDto.setGeoSecCode( bffGeoProjection.getGeoSecCode() );
        bffGeoDto.setGeoTypeId( bffGeoProjection.getGeoTypeId() );
        bffGeoDto.setParentGeoId( bffGeoProjection.getParentGeoId() );
        bffGeoDto.setSequenceNumber( bffGeoProjection.getSequenceNumber() );
        bffGeoDto.setWellKnownText( bffGeoProjection.getWellKnownText() );

        return bffGeoDto;
    }
}
