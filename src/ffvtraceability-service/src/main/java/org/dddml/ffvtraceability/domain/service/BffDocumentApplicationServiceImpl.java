package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffDocumentDto;
import org.dddml.ffvtraceability.domain.document.AbstractDocumentCommand;
import org.dddml.ffvtraceability.domain.document.DocumentApplicationService;
import org.dddml.ffvtraceability.domain.document.DocumentState;
import org.dddml.ffvtraceability.domain.document.DocumentTypeId;
import org.dddml.ffvtraceability.domain.mapper.BffDocumentMapper;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.dddml.support.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BffDocumentApplicationServiceImpl implements BffDocumentApplicationService {
    @Autowired
    private DocumentApplicationService documentApplicationService;

    @Autowired
    private BffDocumentMapper bffDocumentMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<BffDocumentDto> when(BffDocumentServiceCommands.GetDocuments c) {
        int firstResult = c.getPage() * c.getSize();
        int maxResults = c.getSize();
        List<BffDocumentDto> ds = StreamSupport.stream(documentApplicationService.get(
                        (Criterion) null, Collections.emptyList(), firstResult, maxResults).spliterator(), false
                ).map(x -> bffDocumentMapper.toBffDocumentDto(x))
                .collect(Collectors.toUnmodifiableList());
        return Page.builder(ds).size(c.getSize()).number(c.getPage()).build();
    }

    @Override
    @Transactional(readOnly = true)
    public BffDocumentDto when(BffDocumentServiceCommands.GetDocument c) {
        DocumentState d = documentApplicationService.get(c.getDocumentId());
        if (d == null) {
            return null;
        }
        return bffDocumentMapper.toBffDocumentDto(d);
    }

    @Override
    @Transactional
    public String when(BffDocumentServiceCommands.CreateDocument c) {
        AbstractDocumentCommand.SimpleCreateDocument createDocument = new AbstractDocumentCommand.SimpleCreateDocument();
        createDocument.setDocumentId(c.getDocument().getDocumentId() != null ? c.getDocument().getDocumentId() : IdUtils.randomId());
        createDocument.setDocumentLocation(c.getDocument().getDocumentLocation());
        if (c.getDocument().getDocumentTypeId() != null && !c.getDocument().getDocumentTypeId().isBlank()) {
            createDocument.setDocumentTypeId(c.getDocument().getDocumentTypeId());
        } else {
            createDocument.setDocumentTypeId(DocumentTypeId.DOCUMENT); // 目前只有一种文档类型，先硬编码
        }
        createDocument.setDocumentText(c.getDocument().getDocumentText());
        createDocument.setComments(c.getDocument().getComments());
        createDocument.setContentType(c.getDocument().getContentType());
        createDocument.setCommandId(createDocument.getDocumentId());
        createDocument.setRequesterId(c.getRequesterId());
        documentApplicationService.when(createDocument);
        return createDocument.getDocumentId();
    }

    @Override
    @Transactional
    public void when(BffDocumentServiceCommands.UpdateDocument c) {
        String documentId = c.getDocumentId();
        DocumentState d = documentApplicationService.get(documentId);
        if (d == null) {
            throw new IllegalArgumentException("Document not found: " + documentId);
        }
        AbstractDocumentCommand.SimpleMergePatchDocument mergePatchDocument = new AbstractDocumentCommand.SimpleMergePatchDocument();
        mergePatchDocument.setDocumentId(documentId);
        mergePatchDocument.setVersion(d.getVersion()); // 注意，我们默认实体是使用了乐观锁的，所以这里要传入“当前版本号”
        if (c.getDocument().getDocumentTypeId() != null && !c.getDocument().getDocumentTypeId().isBlank()) {
            mergePatchDocument.setDocumentTypeId(c.getDocument().getDocumentTypeId());
        } else {
            mergePatchDocument.setDocumentTypeId(DocumentTypeId.DOCUMENT); // 目前只有一种文档类型，先硬编码
        }
        mergePatchDocument.setDocumentLocation(c.getDocument().getDocumentLocation());
        mergePatchDocument.setDocumentText(c.getDocument().getDocumentText());
        mergePatchDocument.setContentType(c.getDocument().getContentType());
        mergePatchDocument.setComments(c.getDocument().getComments());
        mergePatchDocument.setCommandId(c.getCommandId() == null ? UUID.randomUUID().toString() : c.getCommandId());
        mergePatchDocument.setRequesterId(c.getRequesterId());
        documentApplicationService.when(mergePatchDocument);
    }
}
