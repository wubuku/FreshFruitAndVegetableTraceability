package org.dddml.ffvtraceability.domain.mapper;

import org.dddml.ffvtraceability.domain.BffPurchaseOrderDto;
import org.dddml.ffvtraceability.domain.BffPurchaseOrderItemDto;
import org.dddml.ffvtraceability.domain.repository.BffPurchaseOrderAndItemProjection;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface BffPurchaseOrderMapper {
    default OffsetDateTime instantToOffsetDateTime(Instant instant) {
        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
    }

    //@Mapping(source = "createdAtInstant", target = "createdAt")
    BffPurchaseOrderDto toBffPurchaseOrderDto(BffPurchaseOrderAndItemProjection purchaseOrderAndItemProjection);

    BffPurchaseOrderItemDto toBffPurchaseOrderItemDto(BffPurchaseOrderAndItemProjection purchaseOrderAndItemProjection);

}
