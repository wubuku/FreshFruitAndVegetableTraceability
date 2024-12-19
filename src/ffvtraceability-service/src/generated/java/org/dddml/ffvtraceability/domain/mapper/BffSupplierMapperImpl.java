package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffSupplierDto;
import org.dddml.ffvtraceability.domain.party.PartyState;
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
}
