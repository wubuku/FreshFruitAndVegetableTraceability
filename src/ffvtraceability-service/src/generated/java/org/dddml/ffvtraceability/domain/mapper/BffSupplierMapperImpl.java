package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffSupplierDto;
import org.dddml.ffvtraceability.domain.party.AbstractPartyCommand;
import org.dddml.ffvtraceability.domain.party.PartyState;
import org.dddml.ffvtraceability.domain.repository.BffSupplierProjection;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class BffSupplierMapperImpl implements BffSupplierMapper {

    @Override
    public BffSupplierDto toBffSupplierDto(PartyState partyState) {
        if ( partyState == null ) {
            return null;
        }

        BffSupplierDto bffSupplierDto = new BffSupplierDto();

        bffSupplierDto.setDescription( partyState.getDescription() );
        bffSupplierDto.setExternalId( partyState.getExternalId() );
        bffSupplierDto.setPreferredCurrencyUomId( partyState.getPreferredCurrencyUomId() );
        bffSupplierDto.setStatusId( partyState.getStatusId() );

        return bffSupplierDto;
    }

    @Override
    public BffSupplierDto toBffSupplierDto(BffSupplierProjection bffSupplierProjection) {
        if ( bffSupplierProjection == null ) {
            return null;
        }

        BffSupplierDto bffSupplierDto = new BffSupplierDto();

        bffSupplierDto.setDescription( bffSupplierProjection.getDescription() );
        bffSupplierDto.setExternalId( bffSupplierProjection.getExternalId() );
        bffSupplierDto.setGgn( bffSupplierProjection.getGgn() );
        bffSupplierDto.setGln( bffSupplierProjection.getGln() );
        bffSupplierDto.setPreferredCurrencyUomId( bffSupplierProjection.getPreferredCurrencyUomId() );
        bffSupplierDto.setStatusId( bffSupplierProjection.getStatusId() );
        bffSupplierDto.setSupplierId( bffSupplierProjection.getSupplierId() );
        bffSupplierDto.setSupplierName( bffSupplierProjection.getSupplierName() );

        return bffSupplierDto;
    }

    @Override
    public AbstractPartyCommand.SimpleCreateParty toCreateParty(BffSupplierDto bffSupplierDto) {
        if ( bffSupplierDto == null ) {
            return null;
        }

        AbstractPartyCommand.SimpleCreateParty simpleCreateParty = new AbstractPartyCommand.SimpleCreateParty();

        simpleCreateParty.setDescription( bffSupplierDto.getDescription() );
        simpleCreateParty.setExternalId( bffSupplierDto.getExternalId() );
        simpleCreateParty.setPreferredCurrencyUomId( bffSupplierDto.getPreferredCurrencyUomId() );
        simpleCreateParty.setStatusId( bffSupplierDto.getStatusId() );

        return simpleCreateParty;
    }

    @Override
    public AbstractPartyCommand.SimpleMergePatchParty toMergePatchParty(BffSupplierDto bffSupplierDto) {
        if ( bffSupplierDto == null ) {
            return null;
        }

        AbstractPartyCommand.SimpleMergePatchParty simpleMergePatchParty = new AbstractPartyCommand.SimpleMergePatchParty();

        simpleMergePatchParty.setDescription( bffSupplierDto.getDescription() );
        simpleMergePatchParty.setExternalId( bffSupplierDto.getExternalId() );
        simpleMergePatchParty.setPreferredCurrencyUomId( bffSupplierDto.getPreferredCurrencyUomId() );
        simpleMergePatchParty.setStatusId( bffSupplierDto.getStatusId() );

        return simpleMergePatchParty;
    }
}
