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

@RequestMapping(path = "BffSuppliers", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class BffSupplierServiceResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BffSupplierApplicationService bffSupplierApplicationService;

    @GetMapping
    public Page<BffSupplierDto> getSuppliers(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "20") Integer size,
        @RequestParam(value = "active", required = false) String active
    ) {
        BffSupplierServiceCommands.GetSuppliers getSuppliers = new BffSupplierServiceCommands.GetSuppliers();
        getSuppliers.setPage(page);
        getSuppliers.setSize(size);
        getSuppliers.setActive(active);
        
        getSuppliers.setRequesterId(SecurityContextUtil.getRequesterId());
        return bffSupplierApplicationService.when(getSuppliers);
        
    }

    @GetMapping("{supplierId}")
    public BffSupplierDto getSupplier(
        @PathVariable("supplierId") String supplierId,
        @RequestParam(value = "includesFacilities", required = false) Boolean includesFacilities
    ) {
        BffSupplierServiceCommands.GetSupplier getSupplier = new BffSupplierServiceCommands.GetSupplier();
        getSupplier.setSupplierId(supplierId);
        getSupplier.setIncludesFacilities(includesFacilities);
        
        getSupplier.setRequesterId(SecurityContextUtil.getRequesterId());
        return bffSupplierApplicationService.when(getSupplier);
        
    }

    @PostMapping
    public String createSupplier(
        @RequestBody @Valid BffSupplierDto supplier
    ) {
        BffSupplierServiceCommands.CreateSupplier createSupplier = new BffSupplierServiceCommands.CreateSupplier();
        createSupplier.setSupplier(supplier);
        
        createSupplier.setRequesterId(SecurityContextUtil.getRequesterId());
        return bffSupplierApplicationService.when(createSupplier);
        
    }

    @PutMapping("{supplierId}")
    public void updateSupplier(
        @PathVariable("supplierId") String supplierId,
        @RequestBody @Valid BffSupplierDto supplier
    ) {
        BffSupplierServiceCommands.UpdateSupplier updateSupplier = new BffSupplierServiceCommands.UpdateSupplier();
        updateSupplier.setSupplierId(supplierId);
        updateSupplier.setSupplier(supplier);
        
        updateSupplier.setRequesterId(SecurityContextUtil.getRequesterId());
        bffSupplierApplicationService.when(updateSupplier);
        
    }

    @PutMapping("{supplierId}/active")
    public void activateSupplier(
        @PathVariable("supplierId") String supplierId,
        @RequestBody Boolean active
    ) {
        BffSupplierServiceCommands.ActivateSupplier activateSupplier = new BffSupplierServiceCommands.ActivateSupplier();
        activateSupplier.setSupplierId(supplierId);
        activateSupplier.setActive(active);
        
        activateSupplier.setRequesterId(SecurityContextUtil.getRequesterId());
        bffSupplierApplicationService.when(activateSupplier);
        
    }

    @PutMapping("{supplierId}/BusinessContact")
    public void updateBusinessContact(
        @PathVariable("supplierId") String supplierId,
        @RequestBody @Valid BffBusinessContactDto businessContact
    ) {
        BffSupplierServiceCommands.UpdateBusinessContact updateBusinessContact = new BffSupplierServiceCommands.UpdateBusinessContact();
        updateBusinessContact.setSupplierId(supplierId);
        updateBusinessContact.setBusinessContact(businessContact);
        
        updateBusinessContact.setRequesterId(SecurityContextUtil.getRequesterId());
        bffSupplierApplicationService.when(updateBusinessContact);
        
    }

    @PostMapping("batchAddSuppliers")
    public void batchAddSuppliers(
        @RequestBody @Valid BffSupplierDto[] suppliers
    ) {
        BffSupplierServiceCommands.BatchAddSuppliers batchAddSuppliers = new BffSupplierServiceCommands.BatchAddSuppliers();
        batchAddSuppliers.setSuppliers(suppliers);
        
        batchAddSuppliers.setRequesterId(SecurityContextUtil.getRequesterId());
        bffSupplierApplicationService.when(batchAddSuppliers);
        
    }

    @PostMapping("batchActivateSuppliers")
    public void batchActivateSuppliers(
        @RequestBody String[] supplierIds
    ) {
        BffSupplierServiceCommands.BatchActivateSuppliers batchActivateSuppliers = new BffSupplierServiceCommands.BatchActivateSuppliers();
        batchActivateSuppliers.setSupplierIds(supplierIds);
        
        batchActivateSuppliers.setRequesterId(SecurityContextUtil.getRequesterId());
        bffSupplierApplicationService.when(batchActivateSuppliers);
        
    }

    @PostMapping("batchDeactivateSuppliers")
    public void batchDeactivateSuppliers(
        @RequestBody String[] supplierIds
    ) {
        BffSupplierServiceCommands.BatchDeactivateSuppliers batchDeactivateSuppliers = new BffSupplierServiceCommands.BatchDeactivateSuppliers();
        batchDeactivateSuppliers.setSupplierIds(supplierIds);
        
        batchDeactivateSuppliers.setRequesterId(SecurityContextUtil.getRequesterId());
        bffSupplierApplicationService.when(batchDeactivateSuppliers);
        
    }

}

