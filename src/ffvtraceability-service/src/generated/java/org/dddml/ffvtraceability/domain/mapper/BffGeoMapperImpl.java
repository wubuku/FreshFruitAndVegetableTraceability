package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffGeoDto;
import org.dddml.ffvtraceability.domain.geo.AbstractGeoCommand;
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

    @Override
    public AbstractGeoCommand.SimpleCreateGeo toCreateGeo(BffGeoDto bffGeoDto) {
        if ( bffGeoDto == null ) {
            return null;
        }

        AbstractGeoCommand.SimpleCreateGeo simpleCreateGeo = new AbstractGeoCommand.SimpleCreateGeo();

        simpleCreateGeo.setGeoId( bffGeoDto.getGeoId() );
        simpleCreateGeo.setGeoTypeId( bffGeoDto.getGeoTypeId() );
        simpleCreateGeo.setGeoName( bffGeoDto.getGeoName() );
        simpleCreateGeo.setGeoCode( bffGeoDto.getGeoCode() );
        simpleCreateGeo.setGeoSecCode( bffGeoDto.getGeoSecCode() );
        simpleCreateGeo.setAbbreviation( bffGeoDto.getAbbreviation() );
        simpleCreateGeo.setWellKnownText( bffGeoDto.getWellKnownText() );
        simpleCreateGeo.setSequenceNumber( bffGeoDto.getSequenceNumber() );

        return simpleCreateGeo;
    }

    @Override
    public AbstractGeoCommand.SimpleMergePatchGeo toMergePatchGeo(BffGeoDto bffGeoDto) {
        if ( bffGeoDto == null ) {
            return null;
        }

        AbstractGeoCommand.SimpleMergePatchGeo simpleMergePatchGeo = new AbstractGeoCommand.SimpleMergePatchGeo();

        simpleMergePatchGeo.setGeoId( bffGeoDto.getGeoId() );
        simpleMergePatchGeo.setGeoTypeId( bffGeoDto.getGeoTypeId() );
        simpleMergePatchGeo.setGeoName( bffGeoDto.getGeoName() );
        simpleMergePatchGeo.setGeoCode( bffGeoDto.getGeoCode() );
        simpleMergePatchGeo.setGeoSecCode( bffGeoDto.getGeoSecCode() );
        simpleMergePatchGeo.setAbbreviation( bffGeoDto.getAbbreviation() );
        simpleMergePatchGeo.setWellKnownText( bffGeoDto.getWellKnownText() );
        simpleMergePatchGeo.setSequenceNumber( bffGeoDto.getSequenceNumber() );

        return simpleMergePatchGeo;
    }
}
