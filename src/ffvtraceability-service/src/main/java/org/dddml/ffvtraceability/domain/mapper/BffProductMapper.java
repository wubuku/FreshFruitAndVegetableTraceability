package org.dddml.ffvtraceability.domain.mapper;

import org.dddml.ffvtraceability.domain.BffProductDto;
import org.dddml.ffvtraceability.domain.product.AbstractProductCommand;
import org.dddml.ffvtraceability.domain.product.ProductState;
import org.dddml.ffvtraceability.domain.repository.BffProductProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring"//,
        //unmappedTargetPolicy = ReportingPolicy.ERROR,
        //unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface BffProductMapper {
    BffProductDto toBffProductDto(BffProductProjection bffProduct);

    BffProductDto toBffProductDto(ProductState productState);

    AbstractProductCommand.SimpleCreateProduct toCreateProduct(BffProductDto bffProductDto);

    AbstractProductCommand.SimpleMergePatchProduct toMergePatchProduct(BffProductDto bffProductDto);
}
