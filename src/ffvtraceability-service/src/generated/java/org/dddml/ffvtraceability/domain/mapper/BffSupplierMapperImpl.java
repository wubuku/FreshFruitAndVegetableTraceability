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

        bffSupplierDto.setExternalId( partyState.getExternalId() );
        bffSupplierDto.setPreferredCurrencyUomId( partyState.getPreferredCurrencyUomId() );
        bffSupplierDto.setDescription( partyState.getDescription() );
        bffSupplierDto.setStatusId( partyState.getStatusId() );

        return bffSupplierDto;
    }

    @Override
    public BffSupplierDto toBffSupplierDto(BffSupplierProjection bffSupplierProjection) {
        if ( bffSupplierProjection == null ) {
            return null;
        }

        BffSupplierDto bffSupplierDto = new BffSupplierDto();

        bffSupplierDto.setSupplierId( bffSupplierProjection.getSupplierId() );
        bffSupplierDto.setSupplierName( bffSupplierProjection.getSupplierName() );
        bffSupplierDto.setGgn( bffSupplierProjection.getGgn() );
        bffSupplierDto.setGln( bffSupplierProjection.getGln() );
        bffSupplierDto.setExternalId( bffSupplierProjection.getExternalId() );
        bffSupplierDto.setPreferredCurrencyUomId( bffSupplierProjection.getPreferredCurrencyUomId() );
        bffSupplierDto.setDescription( bffSupplierProjection.getDescription() );
        bffSupplierDto.setStatusId( bffSupplierProjection.getStatusId() );
        bffSupplierDto.setSupplierShortName( bffSupplierProjection.getSupplierShortName() );
        bffSupplierDto.setTaxId( bffSupplierProjection.getTaxId() );
        bffSupplierDto.setGs1CompanyPrefix( bffSupplierProjection.getGs1CompanyPrefix() );
        bffSupplierDto.setInternalId( bffSupplierProjection.getInternalId() );
        bffSupplierDto.setSupplierTypeId( bffSupplierProjection.getSupplierTypeId() );
        bffSupplierDto.setTpaNumber( bffSupplierProjection.getTpaNumber() );
        bffSupplierDto.setCertificationCodes( bffSupplierProjection.getCertificationCodes() );
        bffSupplierDto.setBankAccountInformation( bffSupplierProjection.getBankAccountInformation() );
        bffSupplierDto.setTelephone( bffSupplierProjection.getTelephone() );
        bffSupplierDto.setEmail( bffSupplierProjection.getEmail() );
        bffSupplierDto.setWebSite( bffSupplierProjection.getWebSite() );

        return bffSupplierDto;
    }

    @Override
    public AbstractPartyCommand.SimpleCreateParty toCreateParty(BffSupplierDto bffSupplierDto) {
        if ( bffSupplierDto == null ) {
            return null;
        }

        AbstractPartyCommand.SimpleCreateParty simpleCreateParty = new AbstractPartyCommand.SimpleCreateParty();

        simpleCreateParty.setExternalId( bffSupplierDto.getExternalId() );
        simpleCreateParty.setPreferredCurrencyUomId( bffSupplierDto.getPreferredCurrencyUomId() );
        simpleCreateParty.setDescription( bffSupplierDto.getDescription() );
        simpleCreateParty.setStatusId( bffSupplierDto.getStatusId() );

        return simpleCreateParty;
    }

    @Override
    public AbstractPartyCommand.SimpleMergePatchParty toMergePatchParty(BffSupplierDto bffSupplierDto) {
        if ( bffSupplierDto == null ) {
            return null;
        }

        AbstractPartyCommand.SimpleMergePatchParty simpleMergePatchParty = new AbstractPartyCommand.SimpleMergePatchParty();

        simpleMergePatchParty.setExternalId( bffSupplierDto.getExternalId() );
        simpleMergePatchParty.setPreferredCurrencyUomId( bffSupplierDto.getPreferredCurrencyUomId() );
        simpleMergePatchParty.setDescription( bffSupplierDto.getDescription() );
        simpleMergePatchParty.setStatusId( bffSupplierDto.getStatusId() );

        return simpleMergePatchParty;
    }
}
