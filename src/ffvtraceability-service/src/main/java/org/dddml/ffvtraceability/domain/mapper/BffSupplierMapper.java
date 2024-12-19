package org.dddml.ffvtraceability.domain.mapper;


import org.dddml.ffvtraceability.domain.BffSupplierDto;
import org.dddml.ffvtraceability.domain.party.PartyState;
import org.dddml.ffvtraceability.domain.repository.BffSupplierProjection;
import org.dddml.ffvtraceability.domain.repository.BffSupplierRepository;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BffSupplierMapper {
    BffSupplierDto toBffSupplierDto(PartyState partyState);

    BffSupplierDto toBffSupplierDto(BffSupplierProjection bffSupplierProjection);
}
