package org.dddml.ffvtraceability.domain.mapper;

import org.dddml.ffvtraceability.domain.BffDocumentDto;
import org.dddml.ffvtraceability.domain.BffReceivingDocumentDto;
import org.dddml.ffvtraceability.domain.BffReceivingItemDto;
import org.dddml.ffvtraceability.domain.repository.BffReceivingDocumentItemProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface BffReceivingMapper {
    default OffsetDateTime instantToOffsetDateTime(Instant instant) {
        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
    }

    //@Mapping(source = "createdAtInstant", target = "createdAt")
    BffReceivingDocumentDto toBffReceivingDocumentDto(BffReceivingDocumentItemProjection documentItemProjection);

    BffReceivingItemDto toBffReceivingItemDto(BffReceivingDocumentItemProjection documentItemProjection);

    @Mapping(source = "referenceDocumentId", target = "documentId")
    //@Mapping(source = "referenceDocumentTypeId", target = "documentTypeId")
    @Mapping(source = "referenceComments", target = "comments")
    @Mapping(source = "referenceDocumentLocation", target = "documentLocation")
    @Mapping(source = "referenceDocumentText", target = "documentText")
    @Mapping(source = "referenceDocumentContentType", target = "contentType")
    BffDocumentDto toReferenceDocument(BffReceivingDocumentItemProjection documentItemProjection);
}
