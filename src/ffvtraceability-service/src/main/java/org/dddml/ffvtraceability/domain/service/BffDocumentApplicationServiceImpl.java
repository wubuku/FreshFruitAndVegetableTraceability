package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffDocumentDto;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.stereotype.Service;

@Service
public class BffDocumentApplicationServiceImpl implements BffDocumentApplicationService {
    @Override
    public Page<BffDocumentDto> when(BffDocumentServiceCommands.GetDocuments c) {
        return null;
    }

    @Override
    public BffDocumentDto when(BffDocumentServiceCommands.GetDocument c) {
        return null;
    }

    @Override
    public void when(BffDocumentServiceCommands.CreateDocument c) {

    }

    @Override
    public void when(BffDocumentServiceCommands.UpdateDocument c) {

    }
}
