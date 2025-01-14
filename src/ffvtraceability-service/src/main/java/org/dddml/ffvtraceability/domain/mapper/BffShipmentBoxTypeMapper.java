package org.dddml.ffvtraceability.domain.mapper;

import org.dddml.ffvtraceability.domain.BffShipmentBoxTypeDto;
import org.dddml.ffvtraceability.domain.repository.BffShipmentBoxTypeProjection;
import org.dddml.ffvtraceability.domain.shipmentboxtype.AbstractShipmentBoxTypeCommand;
import org.dddml.ffvtraceability.domain.shipmentboxtype.ShipmentBoxTypeState;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring"//,
        //unmappedTargetPolicy = ReportingPolicy.ERROR,
        //unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface BffShipmentBoxTypeMapper {
    default OffsetDateTime instantToOffsetDateTime(Instant instant) {
        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
    }

    BffShipmentBoxTypeDto toBffShipmentBoxTypeDto(BffShipmentBoxTypeProjection bffShipmentBoxTypeProjection);

    BffShipmentBoxTypeDto toBffShipmentBoxTypeDto(ShipmentBoxTypeState shipmentBoxTypeState);

    AbstractShipmentBoxTypeCommand.SimpleCreateShipmentBoxType toCreateShipmentBoxType(BffShipmentBoxTypeDto bffShipmentBoxTypeDto);

    AbstractShipmentBoxTypeCommand.SimpleMergePatchShipmentBoxType toMergePatchShipmentBoxType(BffShipmentBoxTypeDto bffShipmentBoxTypeDto);
}
