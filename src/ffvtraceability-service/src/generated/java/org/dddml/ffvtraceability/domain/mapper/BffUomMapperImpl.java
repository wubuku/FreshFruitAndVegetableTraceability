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

        bffUomDto.setUomId( bffUomProjection.getUomId() );
        bffUomDto.setUomTypeId( bffUomProjection.getUomTypeId() );
        bffUomDto.setAbbreviation( bffUomProjection.getAbbreviation() );
        bffUomDto.setNumericCode( bffUomProjection.getNumericCode() );
        bffUomDto.setDescription( bffUomProjection.getDescription() );
        bffUomDto.setGs1AI( bffUomProjection.getGs1AI() );
        bffUomDto.setActive( bffUomProjection.getActive() );
        bffUomDto.setUomName( bffUomProjection.getUomName() );

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
        bffUomDto.setActive( uomState.getActive() );
        bffUomDto.setUomName( uomState.getUomName() );

        return bffUomDto;
    }

    @Override
    public AbstractUomCommand.SimpleCreateUom toCreateUom(BffUomDto bffUomDto) {
        if ( bffUomDto == null ) {
            return null;
        }

        AbstractUomCommand.SimpleCreateUom simpleCreateUom = new AbstractUomCommand.SimpleCreateUom();

        simpleCreateUom.setUomId( bffUomDto.getUomId() );
        simpleCreateUom.setUomTypeId( bffUomDto.getUomTypeId() );
        simpleCreateUom.setAbbreviation( bffUomDto.getAbbreviation() );
        simpleCreateUom.setNumericCode( bffUomDto.getNumericCode() );
        simpleCreateUom.setGs1AI( bffUomDto.getGs1AI() );
        simpleCreateUom.setDescription( bffUomDto.getDescription() );
        simpleCreateUom.setActive( bffUomDto.getActive() );
        simpleCreateUom.setUomName( bffUomDto.getUomName() );

        return simpleCreateUom;
    }

    @Override
    public AbstractUomCommand.SimpleMergePatchUom toMergePatchUom(BffUomDto bffUomDto) {
        if ( bffUomDto == null ) {
            return null;
        }

        AbstractUomCommand.SimpleMergePatchUom simpleMergePatchUom = new AbstractUomCommand.SimpleMergePatchUom();

        simpleMergePatchUom.setUomId( bffUomDto.getUomId() );
        simpleMergePatchUom.setUomTypeId( bffUomDto.getUomTypeId() );
        simpleMergePatchUom.setAbbreviation( bffUomDto.getAbbreviation() );
        simpleMergePatchUom.setNumericCode( bffUomDto.getNumericCode() );
        simpleMergePatchUom.setGs1AI( bffUomDto.getGs1AI() );
        simpleMergePatchUom.setDescription( bffUomDto.getDescription() );
        simpleMergePatchUom.setActive( bffUomDto.getActive() );
        simpleMergePatchUom.setUomName( bffUomDto.getUomName() );

        return simpleMergePatchUom;
    }
}
