package org.dddml.ffvtraceability.domain.mapper;

import org.dddml.ffvtraceability.domain.BffDocumentDto;
import org.dddml.ffvtraceability.domain.document.DocumentState;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring"//,
//        unmappedTargetPolicy = ReportingPolicy.ERROR,
//        unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface BffDocumentMapper {
    BffDocumentDto toBffDocumentDto(DocumentState documentState);
}
