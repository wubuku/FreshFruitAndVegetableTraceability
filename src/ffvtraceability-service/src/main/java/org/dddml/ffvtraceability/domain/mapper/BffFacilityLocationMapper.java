package org.dddml.ffvtraceability.domain.mapper;

import org.dddml.ffvtraceability.domain.BffFacilityLocationDto;
import org.dddml.ffvtraceability.domain.repository.BffFacilityLocationProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring"//,
        //unmappedTargetPolicy = ReportingPolicy.ERROR,
        //unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface BffFacilityLocationMapper {
//    default OffsetDateTime instantToOffsetDateTime(Instant instant) {
//        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
//    }

    BffFacilityLocationDto toBffFacilityLocationDto(BffFacilityLocationProjection bffFacilityLocationProjection);
}
