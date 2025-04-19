package org.dddml.ffvtraceability.domain.mapper;


import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.party.AbstractPartyCommand;
import org.dddml.ffvtraceability.domain.party.PartyState;
import org.dddml.ffvtraceability.domain.repository.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface BffProductAssocMapper {
    default OffsetDateTime instantToOffsetDateTime(Instant instant) {
        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
    }
    BffProductAssociationDto toBffProductAssociationDto(BffProductAssociationDtoProjection bffProductAssociationDtoProjection);
}
