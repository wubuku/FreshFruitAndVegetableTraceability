package org.dddml.ffvtraceability.domain.mapper;

import org.dddml.ffvtraceability.domain.BffInventoryItemDto;
import org.dddml.ffvtraceability.domain.BffRawItemInventoryGroupDto;
import org.dddml.ffvtraceability.domain.BffRawItemInventoryItemDto;
import org.dddml.ffvtraceability.domain.BffWipInventoryGroupDto;
import org.dddml.ffvtraceability.domain.repository.BffInventoryItemProjection;
import org.dddml.ffvtraceability.domain.repository.BffRawItemInventoryGroupProjection;
import org.dddml.ffvtraceability.domain.repository.BffRawItemInventoryItemProjection;
import org.dddml.ffvtraceability.domain.repository.BffWipInventoryGroupProjection;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring"//,
        //unmappedTargetPolicy = ReportingPolicy.ERROR,
        //unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface BffInventoryTransferMapper {
    default OffsetDateTime instantToOffsetDateTime(Instant instant) {
        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
    }


}
