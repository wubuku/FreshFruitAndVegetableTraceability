package org.dddml.ffvtraceability.domain.mapper;

import org.dddml.ffvtraceability.domain.BffGeoDto;
import org.dddml.ffvtraceability.domain.BffLotDto;
import org.dddml.ffvtraceability.domain.lot.LotState;
import org.dddml.ffvtraceability.domain.repository.BffGeoProjection;
import org.dddml.ffvtraceability.domain.repository.BffLotProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring"//,
        //unmappedTargetPolicy = ReportingPolicy.ERROR,
        //unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface BffGeoMapper {
    BffGeoDto toBffGeoDto(BffGeoProjection bffGeoProjection);
}
