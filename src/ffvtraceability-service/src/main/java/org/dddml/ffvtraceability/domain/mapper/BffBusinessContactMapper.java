package org.dddml.ffvtraceability.domain.mapper;


import org.dddml.ffvtraceability.domain.BffBusinessContactDto;
import org.dddml.ffvtraceability.domain.repository.BffFacilityContactMechRepository;
import org.dddml.ffvtraceability.domain.repository.BffPartyContactMechRepository;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BffBusinessContactMapper {
    BffBusinessContactDto toBffFacilityBusinessContactDto(BffFacilityContactMechRepository.BffFacilityBusinessContactProjection projection);

    BffBusinessContactDto toBffPartyBusinessContactDto(BffPartyContactMechRepository.BffPartyBusinessContactProjection projection);
}
