package org.dddml.ffvtraceability.domain.mapper;

import org.dddml.ffvtraceability.domain.BffLotDto;
import org.dddml.ffvtraceability.domain.BffUomDto;
import org.dddml.ffvtraceability.domain.repository.BffLotProjection;
import org.dddml.ffvtraceability.domain.repository.BffUomProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface BffUomMapper {
//    default OffsetDateTime instantToOffsetDateTime(Instant instant) {
//        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
//    }

    BffUomDto toBffUomDto(BffUomProjection bffUomProjection);
}
