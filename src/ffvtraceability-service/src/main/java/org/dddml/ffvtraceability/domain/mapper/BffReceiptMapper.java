package org.dddml.ffvtraceability.domain.mapper;

import org.dddml.ffvtraceability.domain.BffReceivingDocumentDto;
import org.dddml.ffvtraceability.domain.BffReceivingItemDto;
import org.dddml.ffvtraceability.domain.repository.BffReceivingDocumentItemProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring" //,
        //unmappedTargetPolicy = ReportingPolicy.ERROR,
        //unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface BffReceiptMapper {
    BffReceivingDocumentDto toBffReceivingDocumentDto(BffReceivingDocumentItemProjection documentItemProjection);

    BffReceivingItemDto toBffReceivingItemDto(BffReceivingDocumentItemProjection documentItemProjection);
}
