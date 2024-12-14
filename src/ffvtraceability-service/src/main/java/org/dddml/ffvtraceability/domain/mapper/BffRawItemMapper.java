package org.dddml.ffvtraceability.domain.mapper;

import org.dddml.ffvtraceability.domain.BffRawItemDto;
import org.dddml.ffvtraceability.domain.repository.BffRawItemProjection;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface BffRawItemMapper {
    BffRawItemDto toBffRawItemDto(BffRawItemProjection bffRawItem);
}
