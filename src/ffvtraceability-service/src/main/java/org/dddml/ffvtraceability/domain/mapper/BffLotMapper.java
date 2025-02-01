package org.dddml.ffvtraceability.domain.mapper;

import org.dddml.ffvtraceability.domain.BffLotDto;
import org.dddml.ffvtraceability.domain.BffRawItemDto;
import org.dddml.ffvtraceability.domain.lot.AbstractLotCommand;
import org.dddml.ffvtraceability.domain.lot.LotState;
import org.dddml.ffvtraceability.domain.repository.BffLotProjection;
import org.dddml.ffvtraceability.domain.repository.BffRawItemProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring"// ,
// unmappedTargetPolicy = ReportingPolicy.ERROR,
// unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface BffLotMapper {
    default OffsetDateTime instantToOffsetDateTime(Instant instant) {
        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
    }

    BffLotDto toBffLotDto(BffLotProjection bffLotProjection);

    BffLotDto toBffLotDto(LotState lotState);

    AbstractLotCommand.SimpleCreateLot toCreateLot(BffLotDto bffLotDto);

    AbstractLotCommand.SimpleMergePatchLot toMergePatchLot(BffLotDto bffLotDto);
}
