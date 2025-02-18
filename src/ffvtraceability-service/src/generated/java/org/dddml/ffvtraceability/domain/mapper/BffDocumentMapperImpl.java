package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffDocumentDto;
import org.dddml.ffvtraceability.domain.document.DocumentState;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class BffDocumentMapperImpl implements BffDocumentMapper {

    @Override
    public BffDocumentDto toBffDocumentDto(DocumentState documentState) {
        if ( documentState == null ) {
            return null;
        }

        BffDocumentDto bffDocumentDto = new BffDocumentDto();

        bffDocumentDto.setComments( documentState.getComments() );
        bffDocumentDto.setDocumentId( documentState.getDocumentId() );
        bffDocumentDto.setDocumentLocation( documentState.getDocumentLocation() );
        bffDocumentDto.setDocumentText( documentState.getDocumentText() );
        bffDocumentDto.setDocumentTypeId( documentState.getDocumentTypeId() );

        return bffDocumentDto;
    }
}
