package org.dddml.ffvtraceability.domain.mapper;

import org.dddml.ffvtraceability.domain.BffPurchaseOrderDto;
import org.dddml.ffvtraceability.domain.BffPurchaseOrderFulfillmentDto;
import org.dddml.ffvtraceability.domain.BffPurchaseOrderItemDto;
import org.dddml.ffvtraceability.domain.repository.BffPurchaseOrderAndItemProjection;
import org.dddml.ffvtraceability.domain.repository.BffPurchaseOrderFulfillmentProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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

    @Mapping(source = "itemStatusId", target = "statusId")
    @Mapping(source = "itemSyncStatusId", target = "syncStatusId")
    @Mapping(source = "itemFulfillmentStatusId", target = "fulfillmentStatusId")
    BffPurchaseOrderItemDto toBffPurchaseOrderItemDto(BffPurchaseOrderAndItemProjection purchaseOrderAndItemProjection);

    BffPurchaseOrderFulfillmentDto toBffPurchaseOrderFulfillmentDto(BffPurchaseOrderFulfillmentProjection purchaseOrderFulfillmentProjection);
}
