package org.dddml.ffvtraceability.domain.mapper;


import org.dddml.ffvtraceability.domain.BffQaInspectionDto;
import org.dddml.ffvtraceability.domain.qainspection.QaInspectionState;
import org.dddml.ffvtraceability.domain.repository.BffQaInspectionProjection;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring"//,
        //unmappedTargetPolicy = ReportingPolicy.ERROR,
        //unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface BffQaInspectionMapper {
    default OffsetDateTime instantToOffsetDateTime(Instant instant) {
        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
    }

    BffQaInspectionDto toBffQaInspectionDto(BffQaInspectionProjection bffQaInspectionProjection);

    BffQaInspectionDto toBffQaInspectionDto(QaInspectionState qaInspectionState);
}
