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
        bffSupplierDto.setEmail( partyState.getEmail() );
        bffSupplierDto.setExternalId( partyState.getExternalId() );
        bffSupplierDto.setPreferredCurrencyUomId( partyState.getPreferredCurrencyUomId() );
        bffSupplierDto.setStatusId( partyState.getStatusId() );
        bffSupplierDto.setTelephone( partyState.getTelephone() );
        bffSupplierDto.setWebSite( partyState.getWebSite() );

        bffSupplierDto.setActive( PARTY_STATUS_ACTIVE.equals(partyState.getStatusId()) || partyState.getStatusId() == null ? "Y" : "N" );

        return bffSupplierDto;
    }

    @Override
    public BffSupplierDto toBffSupplierDto(BffSupplierProjection bffSupplierProjection) {
        if ( bffSupplierProjection == null ) {
            return null;
        }

        BffSupplierDto bffSupplierDto = new BffSupplierDto();

        bffSupplierDto.setBankAccountInformation( bffSupplierProjection.getBankAccountInformation() );
        bffSupplierDto.setCertificationCodes( bffSupplierProjection.getCertificationCodes() );
        bffSupplierDto.setDescription( bffSupplierProjection.getDescription() );
        bffSupplierDto.setEmail( bffSupplierProjection.getEmail() );
        bffSupplierDto.setExternalId( bffSupplierProjection.getExternalId() );
        bffSupplierDto.setGgn( bffSupplierProjection.getGgn() );
        bffSupplierDto.setGln( bffSupplierProjection.getGln() );
        bffSupplierDto.setGs1CompanyPrefix( bffSupplierProjection.getGs1CompanyPrefix() );
        bffSupplierDto.setInternalId( bffSupplierProjection.getInternalId() );
        bffSupplierDto.setPreferredCurrencyUomId( bffSupplierProjection.getPreferredCurrencyUomId() );
        bffSupplierDto.setStatusId( bffSupplierProjection.getStatusId() );
        bffSupplierDto.setSupplierId( bffSupplierProjection.getSupplierId() );
        bffSupplierDto.setSupplierName( bffSupplierProjection.getSupplierName() );
        bffSupplierDto.setSupplierProductTypeDescription( bffSupplierProjection.getSupplierProductTypeDescription() );
        bffSupplierDto.setSupplierShortName( bffSupplierProjection.getSupplierShortName() );
        bffSupplierDto.setSupplierTypeEnumId( bffSupplierProjection.getSupplierTypeEnumId() );
        bffSupplierDto.setTaxId( bffSupplierProjection.getTaxId() );
        bffSupplierDto.setTelephone( bffSupplierProjection.getTelephone() );
        bffSupplierDto.setTpaNumber( bffSupplierProjection.getTpaNumber() );
        bffSupplierDto.setWebSite( bffSupplierProjection.getWebSite() );

        bffSupplierDto.setActive( PARTY_STATUS_ACTIVE.equals(bffSupplierProjection.getStatusId()) || bffSupplierProjection.getStatusId() == null? "Y" : "N" );

        return bffSupplierDto;
    }

    @Override
    public AbstractPartyCommand.SimpleCreateParty toCreateParty(BffSupplierDto bffSupplierDto) {
        if ( bffSupplierDto == null ) {
            return null;
        }

        AbstractPartyCommand.SimpleCreateParty simpleCreateParty = new AbstractPartyCommand.SimpleCreateParty();

        simpleCreateParty.setDescription( bffSupplierDto.getDescription() );
        simpleCreateParty.setEmail( bffSupplierDto.getEmail() );
        simpleCreateParty.setExternalId( bffSupplierDto.getExternalId() );
        simpleCreateParty.setPreferredCurrencyUomId( bffSupplierDto.getPreferredCurrencyUomId() );
        simpleCreateParty.setStatusId( bffSupplierDto.getStatusId() );
        simpleCreateParty.setTelephone( bffSupplierDto.getTelephone() );
        simpleCreateParty.setWebSite( bffSupplierDto.getWebSite() );

        return simpleCreateParty;
    }

    @Override
    public AbstractPartyCommand.SimpleMergePatchParty toMergePatchParty(BffSupplierDto bffSupplierDto) {
        if ( bffSupplierDto == null ) {
            return null;
        }

        AbstractPartyCommand.SimpleMergePatchParty simpleMergePatchParty = new AbstractPartyCommand.SimpleMergePatchParty();

        simpleMergePatchParty.setDescription( bffSupplierDto.getDescription() );
        simpleMergePatchParty.setEmail( bffSupplierDto.getEmail() );
        simpleMergePatchParty.setExternalId( bffSupplierDto.getExternalId() );
        simpleMergePatchParty.setPreferredCurrencyUomId( bffSupplierDto.getPreferredCurrencyUomId() );
        simpleMergePatchParty.setStatusId( bffSupplierDto.getStatusId() );
        simpleMergePatchParty.setTelephone( bffSupplierDto.getTelephone() );
        simpleMergePatchParty.setWebSite( bffSupplierDto.getWebSite() );

        return simpleMergePatchParty;
    }
}
