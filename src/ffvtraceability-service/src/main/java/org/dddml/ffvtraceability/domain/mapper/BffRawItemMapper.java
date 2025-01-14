package org.dddml.ffvtraceability.domain.mapper;

import org.dddml.ffvtraceability.domain.BffRawItemDto;
import org.dddml.ffvtraceability.domain.product.AbstractProductCommand;
import org.dddml.ffvtraceability.domain.product.ProductCommand;
import org.dddml.ffvtraceability.domain.product.ProductState;
import org.dddml.ffvtraceability.domain.repository.BffRawItemProjection;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring"//,
        //unmappedTargetPolicy = ReportingPolicy.ERROR,
        //unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface BffRawItemMapper {
    BffRawItemDto toBffRawItemDto(BffRawItemProjection bffRawItem);

    BffRawItemDto toBffRawItemDto(ProductState productState);

    AbstractProductCommand.SimpleCreateProduct toCreateProduct(BffRawItemDto bffRawItemDto);

    AbstractProductCommand.SimpleMergePatchProduct toMergePatchProduct(BffRawItemDto bffRawItemDto);
}
