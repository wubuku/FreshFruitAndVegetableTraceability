package org.dddml.ffvtraceability.domain.mapper;

import org.dddml.ffvtraceability.domain.BffRawItemDto;
import org.dddml.ffvtraceability.domain.BffSupplierRawItemDto;
import org.dddml.ffvtraceability.domain.product.AbstractProductCommand;
import org.dddml.ffvtraceability.domain.product.ProductState;
import org.dddml.ffvtraceability.domain.repository.BffRawItemProjection;
import org.dddml.ffvtraceability.domain.repository.BffSupplierRawItemProjection;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring"//,
        //unmappedTargetPolicy = ReportingPolicy.ERROR,
        //unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface BffRawItemMapper {

    default OffsetDateTime instantToOffsetDateTime(Instant instant) {
        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
    }

    BffRawItemDto toBffRawItemDto(BffRawItemProjection bffRawItem);

    BffRawItemDto toBffRawItemDto(ProductState productState);

    AbstractProductCommand.SimpleCreateProduct toCreateProduct(BffRawItemDto bffRawItemDto);

    AbstractProductCommand.SimpleMergePatchProduct toMergePatchProduct(BffRawItemDto bffRawItemDto);

    BffSupplierRawItemDto toBffSupplierRawItemDto(BffSupplierRawItemProjection bffSupplierRawItemProjection);
}
