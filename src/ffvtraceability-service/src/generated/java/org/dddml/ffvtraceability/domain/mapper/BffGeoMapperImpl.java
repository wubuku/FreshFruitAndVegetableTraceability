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

    @Override
    public AbstractGeoCommand.SimpleCreateGeo toCreateGeo(BffGeoDto bffGeoDto) {
        if ( bffGeoDto == null ) {
            return null;
        }

        AbstractGeoCommand.SimpleCreateGeo simpleCreateGeo = new AbstractGeoCommand.SimpleCreateGeo();

        simpleCreateGeo.setGeoId( bffGeoDto.getGeoId() );
        simpleCreateGeo.setAbbreviation( bffGeoDto.getAbbreviation() );
        simpleCreateGeo.setGeoCode( bffGeoDto.getGeoCode() );
        simpleCreateGeo.setGeoName( bffGeoDto.getGeoName() );
        simpleCreateGeo.setGeoSecCode( bffGeoDto.getGeoSecCode() );
        simpleCreateGeo.setGeoTypeId( bffGeoDto.getGeoTypeId() );
        simpleCreateGeo.setSequenceNumber( bffGeoDto.getSequenceNumber() );
        simpleCreateGeo.setWellKnownText( bffGeoDto.getWellKnownText() );

        return simpleCreateGeo;
    }

    @Override
    public AbstractGeoCommand.SimpleMergePatchGeo toMergePatchGeo(BffGeoDto bffGeoDto) {
        if ( bffGeoDto == null ) {
            return null;
        }

        AbstractGeoCommand.SimpleMergePatchGeo simpleMergePatchGeo = new AbstractGeoCommand.SimpleMergePatchGeo();

        simpleMergePatchGeo.setGeoId( bffGeoDto.getGeoId() );
        simpleMergePatchGeo.setAbbreviation( bffGeoDto.getAbbreviation() );
        simpleMergePatchGeo.setGeoCode( bffGeoDto.getGeoCode() );
        simpleMergePatchGeo.setGeoName( bffGeoDto.getGeoName() );
        simpleMergePatchGeo.setGeoSecCode( bffGeoDto.getGeoSecCode() );
        simpleMergePatchGeo.setGeoTypeId( bffGeoDto.getGeoTypeId() );
        simpleMergePatchGeo.setSequenceNumber( bffGeoDto.getSequenceNumber() );
        simpleMergePatchGeo.setWellKnownText( bffGeoDto.getWellKnownText() );

        return simpleMergePatchGeo;
    }
}
