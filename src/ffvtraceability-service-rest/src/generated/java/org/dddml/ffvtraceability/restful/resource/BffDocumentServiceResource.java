// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.restful.resource;

import java.util.*;
import jakarta.servlet.http.*;
import jakarta.validation.constraints.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import org.dddml.support.criterion.*;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import static org.dddml.ffvtraceability.domain.meta.M.*;
import org.dddml.ffvtraceability.domain.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.*;

@RequestMapping(path = "BffDocuments", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class BffDocumentServiceResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BffDocumentApplicationService bffDocumentApplicationService;

    @GetMapping
    public Page<BffDocumentDto> getDocuments(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "20") Integer size
    ) {
        BffDocumentServiceCommands.GetDocuments getDocuments = new BffDocumentServiceCommands.GetDocuments();
        getDocuments.setPage(page);
        getDocuments.setSize(size);
        try {
        getDocuments.setRequesterId(SecurityContextUtil.getRequesterId());
        return bffDocumentApplicationService.when(getDocuments);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @GetMapping("{documentId}")
    public BffDocumentDto getDocument(
        @PathVariable("documentId") String documentId
    ) {
        BffDocumentServiceCommands.GetDocument getDocument = new BffDocumentServiceCommands.GetDocument();
        getDocument.setDocumentId(documentId);
        try {
        getDocument.setRequesterId(SecurityContextUtil.getRequesterId());
        return bffDocumentApplicationService.when(getDocument);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @PostMapping
    public void createDocument(
        @RequestBody BffDocumentDto document
    ) {
        BffDocumentServiceCommands.CreateDocument createDocument = new BffDocumentServiceCommands.CreateDocument();
        createDocument.setDocument(document);
        try {
        createDocument.setRequesterId(SecurityContextUtil.getRequesterId());
        bffDocumentApplicationService.when(createDocument);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @PutMapping("{documentId}")
    public void updateDocument(
        @PathVariable("documentId") String documentId,
        @RequestBody BffDocumentDto document
    ) {
        BffDocumentServiceCommands.UpdateDocument updateDocument = new BffDocumentServiceCommands.UpdateDocument();
        updateDocument.setDocumentId(documentId);
        updateDocument.setDocument(document);
        try {
        updateDocument.setRequesterId(SecurityContextUtil.getRequesterId());
        bffDocumentApplicationService.when(updateDocument);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

}
