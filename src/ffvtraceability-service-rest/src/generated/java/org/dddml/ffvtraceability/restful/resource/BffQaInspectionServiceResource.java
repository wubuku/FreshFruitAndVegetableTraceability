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

@RequestMapping(path = "BffQaInspections", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class BffQaInspectionServiceResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BffQaInspectionApplicationService bffQaInspectionApplicationService;

    @GetMapping
    public BffQaInspectionDto[] getQaInspections(
        @RequestParam(value = "receivingDocumentId") String receivingDocumentId,
        @RequestParam(value = "receiptId", required = false) String receiptId
    ) {
        BffQaInspectionServiceCommands.GetQaInspections getQaInspections = new BffQaInspectionServiceCommands.GetQaInspections();
        getQaInspections.setReceivingDocumentId(receivingDocumentId);
        getQaInspections.setReceiptId(receiptId);
        try {
        getQaInspections.setRequesterId(SecurityContextUtil.getRequesterId());
        return java.util.stream.StreamSupport.stream((bffQaInspectionApplicationService.when(getQaInspections)).spliterator(), false).collect(java.util.stream.Collectors.toList()).toArray(new BffQaInspectionDto[0]);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @GetMapping("{qaInspectionId}")
    public BffQaInspectionDto getQaInspection(
        @PathVariable("qaInspectionId") String qaInspectionId
    ) {
        BffQaInspectionServiceCommands.GetQaInspection getQaInspection = new BffQaInspectionServiceCommands.GetQaInspection();
        getQaInspection.setQaInspectionId(qaInspectionId);
        try {
        getQaInspection.setRequesterId(SecurityContextUtil.getRequesterId());
        return bffQaInspectionApplicationService.when(getQaInspection);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @PostMapping
    public String createQaInspection(
        @RequestBody BffQaInspectionDto qaInspection
    ) {
        BffQaInspectionServiceCommands.CreateQaInspection createQaInspection = new BffQaInspectionServiceCommands.CreateQaInspection();
        createQaInspection.setQaInspection(qaInspection);
        try {
        createQaInspection.setRequesterId(SecurityContextUtil.getRequesterId());
        return bffQaInspectionApplicationService.when(createQaInspection);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @PutMapping("{qaInspectionId}")
    public void updateQaInspection(
        @PathVariable("qaInspectionId") String qaInspectionId,
        @RequestBody BffQaInspectionDto qaInspection
    ) {
        BffQaInspectionServiceCommands.UpdateQaInspection updateQaInspection = new BffQaInspectionServiceCommands.UpdateQaInspection();
        updateQaInspection.setQaInspectionId(qaInspectionId);
        updateQaInspection.setQaInspection(qaInspection);
        try {
        updateQaInspection.setRequesterId(SecurityContextUtil.getRequesterId());
        bffQaInspectionApplicationService.when(updateQaInspection);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @PostMapping("batchAddQaInspections")
    public void batchAddQaInspections(
        @RequestBody BffQaInspectionDto[] qaInspections
    ) {
        BffQaInspectionServiceCommands.BatchAddQaInspections batchAddQaInspections = new BffQaInspectionServiceCommands.BatchAddQaInspections();
        batchAddQaInspections.setQaInspections(qaInspections);
        try {
        batchAddQaInspections.setRequesterId(SecurityContextUtil.getRequesterId());
        bffQaInspectionApplicationService.when(batchAddQaInspections);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

}
