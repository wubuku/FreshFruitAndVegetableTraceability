package org.dddml.ffvtraceability.domain.mapper;

import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.repository.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface BffSalesOrderMapper {
    default OffsetDateTime instantToOffsetDateTime(Instant instant) {
        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
    }

    //@Mapping(source = "createdAtInstant", target = "createdAt")
    BffSalesOrderDto toBffSalesOrderDto(BffSalesOrderAndItemProjection salesOrderAndItemProjection);


    @Mapping(source = "itemStatusId", target = "statusId")
    @Mapping(source = "itemSyncStatusId", target = "syncStatusId")
    @Mapping(source = "itemFulfillmentStatusId", target = "fulfillmentStatusId")
    BffSalesOrderItemDto toBffSalesOrderItemDto(BffSalesOrderAndItemProjection salesOrderAndItemProjection);

    BffSalesOrderFulfillmentDto toBffSalesOrderFulfillmentDto(BffSalesOrderFulfillmentProjection salesOrderFulfillmentProjection);
}
