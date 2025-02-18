package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffUomDto;
import org.dddml.ffvtraceability.domain.repository.BffUomProjection;
import org.dddml.ffvtraceability.domain.uom.AbstractUomCommand;
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

        bffUomDto.setAbbreviation( bffUomProjection.getAbbreviation() );
        bffUomDto.setActive( bffUomProjection.getActive() );
        bffUomDto.setDescription( bffUomProjection.getDescription() );
        bffUomDto.setGs1AI( bffUomProjection.getGs1AI() );
        bffUomDto.setNumericCode( bffUomProjection.getNumericCode() );
        bffUomDto.setUomId( bffUomProjection.getUomId() );
        bffUomDto.setUomName( bffUomProjection.getUomName() );
        bffUomDto.setUomTypeId( bffUomProjection.getUomTypeId() );

        return bffUomDto;
    }

    @Override
    public BffUomDto toBffUomDto(UomState uomState) {
        if ( uomState == null ) {
            return null;
        }

        BffUomDto bffUomDto = new BffUomDto();

        bffUomDto.setAbbreviation( uomState.getAbbreviation() );
        bffUomDto.setActive( uomState.getActive() );
        bffUomDto.setDescription( uomState.getDescription() );
        bffUomDto.setGs1AI( uomState.getGs1AI() );
        bffUomDto.setNumericCode( uomState.getNumericCode() );
        bffUomDto.setUomId( uomState.getUomId() );
        bffUomDto.setUomName( uomState.getUomName() );
        bffUomDto.setUomTypeId( uomState.getUomTypeId() );

        return bffUomDto;
    }

    @Override
    public AbstractUomCommand.SimpleCreateUom toCreateUom(BffUomDto bffUomDto) {
        if ( bffUomDto == null ) {
            return null;
        }

        AbstractUomCommand.SimpleCreateUom simpleCreateUom = new AbstractUomCommand.SimpleCreateUom();

        simpleCreateUom.setUomId( bffUomDto.getUomId() );
        simpleCreateUom.setAbbreviation( bffUomDto.getAbbreviation() );
        simpleCreateUom.setActive( bffUomDto.getActive() );
        simpleCreateUom.setDescription( bffUomDto.getDescription() );
        simpleCreateUom.setGs1AI( bffUomDto.getGs1AI() );
        simpleCreateUom.setNumericCode( bffUomDto.getNumericCode() );
        simpleCreateUom.setUomName( bffUomDto.getUomName() );
        simpleCreateUom.setUomTypeId( bffUomDto.getUomTypeId() );

        return simpleCreateUom;
    }

    @Override
    public AbstractUomCommand.SimpleMergePatchUom toMergePatchUom(BffUomDto bffUomDto) {
        if ( bffUomDto == null ) {
            return null;
        }

        AbstractUomCommand.SimpleMergePatchUom simpleMergePatchUom = new AbstractUomCommand.SimpleMergePatchUom();

        simpleMergePatchUom.setUomId( bffUomDto.getUomId() );
        simpleMergePatchUom.setAbbreviation( bffUomDto.getAbbreviation() );
        simpleMergePatchUom.setActive( bffUomDto.getActive() );
        simpleMergePatchUom.setDescription( bffUomDto.getDescription() );
        simpleMergePatchUom.setGs1AI( bffUomDto.getGs1AI() );
        simpleMergePatchUom.setNumericCode( bffUomDto.getNumericCode() );
        simpleMergePatchUom.setUomName( bffUomDto.getUomName() );
        simpleMergePatchUom.setUomTypeId( bffUomDto.getUomTypeId() );

        return simpleMergePatchUom;
    }
}
