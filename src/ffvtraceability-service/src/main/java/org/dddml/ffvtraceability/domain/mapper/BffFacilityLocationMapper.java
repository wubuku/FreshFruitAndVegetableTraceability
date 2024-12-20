package org.dddml.ffvtraceability.domain.mapper;

import org.dddml.ffvtraceability.domain.BffFacilityLocationDto;
import org.dddml.ffvtraceability.domain.facilitylocation.FacilityLocationState;
import org.dddml.ffvtraceability.domain.repository.BffFacilityLocationProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring"//,
        //unmappedTargetPolicy = ReportingPolicy.ERROR,
        //unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface BffFacilityLocationMapper {
//    default OffsetDateTime instantToOffsetDateTime(Instant instant) {
//        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
//    }

    BffFacilityLocationDto toBffFacilityLocationDto(BffFacilityLocationProjection bffFacilityLocationProjection);

    @Mapping(target = "locationSeqId", expression = "java(facilityLocationState.getFacilityLocationId().getLocationSeqId())")
    BffFacilityLocationDto toBffFacilityLocationDto(FacilityLocationState facilityLocationState);
}
