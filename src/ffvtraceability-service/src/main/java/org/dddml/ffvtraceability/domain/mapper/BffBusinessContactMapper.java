package org.dddml.ffvtraceability.domain.mapper;


import org.dddml.ffvtraceability.domain.BffBusinessContactDto;
import org.dddml.ffvtraceability.domain.repository.BffBusinessContactProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BffBusinessContactMapper {
    BffBusinessContactDto toBffBusinessContact(BffBusinessContactProjection projection);
}
