package org.dddml.ffvtraceability.domain.mapper;

import org.dddml.ffvtraceability.domain.BffInventoryItemDto;
import org.dddml.ffvtraceability.domain.repository.BffInventoryItemProjection;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring"//,
        //unmappedTargetPolicy = ReportingPolicy.ERROR,
        //unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface BffInventoryItemMapper {
    default OffsetDateTime instantToOffsetDateTime(Instant instant) {
        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
    }
    BffInventoryItemDto toBffInventoryItemDto(BffInventoryItemProjection bffInventoryItemProjection);
}
