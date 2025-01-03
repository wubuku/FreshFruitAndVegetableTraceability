// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.restful.resource;

import java.util.*;
import jakarta.servlet.http.*;
import jakarta.validation.*;
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

@RequestMapping(path = "BffUnitsOfMeasure", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class BffUomServiceResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BffUomApplicationService bffUomApplicationService;

    @GetMapping
    public Page<BffUomDto> getUnitsOfMeasure(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "20") Integer size,
        @RequestParam(value = "active", required = false) String active
    ) {
        BffUomServiceCommands.GetUnitsOfMeasure getUnitsOfMeasure = new BffUomServiceCommands.GetUnitsOfMeasure();
        getUnitsOfMeasure.setPage(page);
        getUnitsOfMeasure.setSize(size);
        getUnitsOfMeasure.setActive(active);
        try {
        getUnitsOfMeasure.setRequesterId(SecurityContextUtil.getRequesterId());
        return bffUomApplicationService.when(getUnitsOfMeasure);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @GetMapping("{uomId}")
    public BffUomDto getUnitOfMeasure(
        @PathVariable("uomId") String uomId
    ) {
        BffUomServiceCommands.GetUnitOfMeasure getUnitOfMeasure = new BffUomServiceCommands.GetUnitOfMeasure();
        getUnitOfMeasure.setUomId(uomId);
        try {
        getUnitOfMeasure.setRequesterId(SecurityContextUtil.getRequesterId());
        return bffUomApplicationService.when(getUnitOfMeasure);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @PostMapping
    public String createUnitOfMeasure(
        @RequestBody BffUomDto uom
    ) {
        BffUomServiceCommands.CreateUnitOfMeasure createUnitOfMeasure = new BffUomServiceCommands.CreateUnitOfMeasure();
        createUnitOfMeasure.setUom(uom);
        try {
        createUnitOfMeasure.setRequesterId(SecurityContextUtil.getRequesterId());
        return bffUomApplicationService.when(createUnitOfMeasure);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @PutMapping("{uomId}")
    public void updateUnitOfMeasure(
        @PathVariable("uomId") String uomId,
        @RequestBody BffUomDto uom
    ) {
        BffUomServiceCommands.UpdateUnitOfMeasure updateUnitOfMeasure = new BffUomServiceCommands.UpdateUnitOfMeasure();
        updateUnitOfMeasure.setUomId(uomId);
        updateUnitOfMeasure.setUom(uom);
        try {
        updateUnitOfMeasure.setRequesterId(SecurityContextUtil.getRequesterId());
        bffUomApplicationService.when(updateUnitOfMeasure);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @PutMapping("{uomId}/active")
    public void activateUnitOfMeasure(
        @PathVariable("uomId") String uomId,
        @RequestBody Boolean active
    ) {
        BffUomServiceCommands.ActivateUnitOfMeasure activateUnitOfMeasure = new BffUomServiceCommands.ActivateUnitOfMeasure();
        activateUnitOfMeasure.setUomId(uomId);
        activateUnitOfMeasure.setActive(active);
        try {
        activateUnitOfMeasure.setRequesterId(SecurityContextUtil.getRequesterId());
        bffUomApplicationService.when(activateUnitOfMeasure);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

}

