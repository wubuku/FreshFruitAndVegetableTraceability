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
        @RequestParam(value = "active", required = false) String active,
        @RequestParam(value = "uomTypeId", required = false) String uomTypeId
    ) {
        BffUomServiceCommands.GetUnitsOfMeasure getUnitsOfMeasure = new BffUomServiceCommands.GetUnitsOfMeasure();
        getUnitsOfMeasure.setPage(page);
        getUnitsOfMeasure.setSize(size);
        getUnitsOfMeasure.setActive(active);
        getUnitsOfMeasure.setUomTypeId(uomTypeId);
        
        getUnitsOfMeasure.setRequesterId(SecurityContextUtil.getRequesterId());
        return bffUomApplicationService.when(getUnitsOfMeasure);
        
    }

    @GetMapping("{uomId}")
    public BffUomDto getUnitOfMeasure(
        @PathVariable("uomId") String uomId
    ) {
        BffUomServiceCommands.GetUnitOfMeasure getUnitOfMeasure = new BffUomServiceCommands.GetUnitOfMeasure();
        getUnitOfMeasure.setUomId(uomId);
        
        getUnitOfMeasure.setRequesterId(SecurityContextUtil.getRequesterId());
        return bffUomApplicationService.when(getUnitOfMeasure);
        
    }

    @PostMapping
    public String createUnitOfMeasure(
        @RequestBody BffUomDto uom
    ) {
        BffUomServiceCommands.CreateUnitOfMeasure createUnitOfMeasure = new BffUomServiceCommands.CreateUnitOfMeasure();
        createUnitOfMeasure.setUom(uom);
        
        createUnitOfMeasure.setRequesterId(SecurityContextUtil.getRequesterId());
        return bffUomApplicationService.when(createUnitOfMeasure);
        
    }

    @PutMapping("{uomId}")
    public void updateUnitOfMeasure(
        @PathVariable("uomId") String uomId,
        @RequestBody BffUomDto uom
    ) {
        BffUomServiceCommands.UpdateUnitOfMeasure updateUnitOfMeasure = new BffUomServiceCommands.UpdateUnitOfMeasure();
        updateUnitOfMeasure.setUomId(uomId);
        updateUnitOfMeasure.setUom(uom);
        
        updateUnitOfMeasure.setRequesterId(SecurityContextUtil.getRequesterId());
        bffUomApplicationService.when(updateUnitOfMeasure);
        
    }

    @PutMapping("{uomId}/active")
    public void activateUnitOfMeasure(
        @PathVariable("uomId") String uomId,
        @RequestBody Boolean active
    ) {
        BffUomServiceCommands.ActivateUnitOfMeasure activateUnitOfMeasure = new BffUomServiceCommands.ActivateUnitOfMeasure();
        activateUnitOfMeasure.setUomId(uomId);
        activateUnitOfMeasure.setActive(active);
        
        activateUnitOfMeasure.setRequesterId(SecurityContextUtil.getRequesterId());
        bffUomApplicationService.when(activateUnitOfMeasure);
        
    }

    @PostMapping("batchAddUnitsOfMeasure")
    public void batchAddUnitsOfMeasure(
        @RequestBody BffUomDto[] unitsOfMeasure
    ) {
        BffUomServiceCommands.BatchAddUnitsOfMeasure batchAddUnitsOfMeasure = new BffUomServiceCommands.BatchAddUnitsOfMeasure();
        batchAddUnitsOfMeasure.setUnitsOfMeasure(unitsOfMeasure);
        
        batchAddUnitsOfMeasure.setRequesterId(SecurityContextUtil.getRequesterId());
        bffUomApplicationService.when(batchAddUnitsOfMeasure);
        
    }

}

