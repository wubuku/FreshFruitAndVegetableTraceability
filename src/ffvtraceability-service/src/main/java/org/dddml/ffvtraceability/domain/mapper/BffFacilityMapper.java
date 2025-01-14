package org.dddml.ffvtraceability.domain.mapper;

import org.dddml.ffvtraceability.domain.BffFacilityDto;
import org.dddml.ffvtraceability.domain.facility.AbstractFacilityCommand;
import org.dddml.ffvtraceability.domain.facility.FacilityState;
import org.dddml.ffvtraceability.domain.repository.BffFacilityProjection;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring"//,
        //unmappedTargetPolicy = ReportingPolicy.ERROR,
        //unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface BffFacilityMapper {
    default OffsetDateTime instantToOffsetDateTime(Instant instant) {
        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
    }

    BffFacilityDto toBffFacilityDto(BffFacilityProjection bffFacilityProjection);

    BffFacilityDto toBffFacilityDto(FacilityState facilityState);

    AbstractFacilityCommand.SimpleCreateFacility toCreateFacility(BffFacilityDto bffFacilityDto);

    AbstractFacilityCommand.SimpleMergePatchFacility toMergePatchFacility(BffFacilityDto bffFacilityDto);
}
