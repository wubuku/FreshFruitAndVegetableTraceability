package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffSupplierDto;
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

        return bffSupplierDto;
    }
}
