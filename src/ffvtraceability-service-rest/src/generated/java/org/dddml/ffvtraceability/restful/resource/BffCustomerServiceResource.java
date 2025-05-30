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

@RequestMapping(path = "BffCustomers", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class BffCustomerServiceResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BffCustomerApplicationService bffCustomerApplicationService;

    @GetMapping
    public Page<BffCustomerDto> getCustomers(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "20") Integer size,
        @RequestParam(value = "active", required = false) String active
    ) {
        BffCustomerServiceCommands.GetCustomers getCustomers = new BffCustomerServiceCommands.GetCustomers();
        getCustomers.setPage(page);
        getCustomers.setSize(size);
        getCustomers.setActive(active);
        
        getCustomers.setRequesterId(SecurityContextUtil.getRequesterId());
        return bffCustomerApplicationService.when(getCustomers);
        
    }

    @GetMapping("{customerId}")
    public BffCustomerDto getCustomer(
        @PathVariable("customerId") String customerId,
        @RequestParam(value = "includesFacilities", required = false) Boolean includesFacilities
    ) {
        BffCustomerServiceCommands.GetCustomer getCustomer = new BffCustomerServiceCommands.GetCustomer();
        getCustomer.setCustomerId(customerId);
        getCustomer.setIncludesFacilities(includesFacilities);
        
        getCustomer.setRequesterId(SecurityContextUtil.getRequesterId());
        return bffCustomerApplicationService.when(getCustomer);
        
    }

    @PostMapping
    public String createCustomer(
        @RequestBody @Valid BffCustomerDto customer
    ) {
        BffCustomerServiceCommands.CreateCustomer createCustomer = new BffCustomerServiceCommands.CreateCustomer();
        createCustomer.setCustomer(customer);
        
        createCustomer.setRequesterId(SecurityContextUtil.getRequesterId());
        return bffCustomerApplicationService.when(createCustomer);
        
    }

    @PutMapping("{customerId}")
    public void updateCustomer(
        @PathVariable("customerId") String customerId,
        @RequestBody @Valid BffCustomerDto customer
    ) {
        BffCustomerServiceCommands.UpdateCustomer updateCustomer = new BffCustomerServiceCommands.UpdateCustomer();
        updateCustomer.setCustomerId(customerId);
        updateCustomer.setCustomer(customer);
        
        updateCustomer.setRequesterId(SecurityContextUtil.getRequesterId());
        bffCustomerApplicationService.when(updateCustomer);
        
    }

    @PutMapping("{customerId}/active")
    public void activateCustomer(
        @PathVariable("customerId") String customerId,
        @RequestBody Boolean active
    ) {
        BffCustomerServiceCommands.ActivateCustomer activateCustomer = new BffCustomerServiceCommands.ActivateCustomer();
        activateCustomer.setCustomerId(customerId);
        activateCustomer.setActive(active);
        
        activateCustomer.setRequesterId(SecurityContextUtil.getRequesterId());
        bffCustomerApplicationService.when(activateCustomer);
        
    }

    @PutMapping("{customerId}/BusinessContact")
    public void updateBusinessContact(
        @PathVariable("customerId") String customerId,
        @RequestBody @Valid BffBusinessContactDto businessContact
    ) {
        BffCustomerServiceCommands.UpdateBusinessContact updateBusinessContact = new BffCustomerServiceCommands.UpdateBusinessContact();
        updateBusinessContact.setCustomerId(customerId);
        updateBusinessContact.setBusinessContact(businessContact);
        
        updateBusinessContact.setRequesterId(SecurityContextUtil.getRequesterId());
        bffCustomerApplicationService.when(updateBusinessContact);
        
    }

    @PostMapping("batchAddCustomers")
    public void batchAddCustomers(
        @RequestBody @Valid BffCustomerDto[] customers
    ) {
        BffCustomerServiceCommands.BatchAddCustomers batchAddCustomers = new BffCustomerServiceCommands.BatchAddCustomers();
        batchAddCustomers.setCustomers(customers);
        
        batchAddCustomers.setRequesterId(SecurityContextUtil.getRequesterId());
        bffCustomerApplicationService.when(batchAddCustomers);
        
    }

    @PostMapping("batchActivateCustomers")
    public void batchActivateCustomers(
        @RequestBody String[] customerIds
    ) {
        BffCustomerServiceCommands.BatchActivateCustomers batchActivateCustomers = new BffCustomerServiceCommands.BatchActivateCustomers();
        batchActivateCustomers.setCustomerIds(customerIds);
        
        batchActivateCustomers.setRequesterId(SecurityContextUtil.getRequesterId());
        bffCustomerApplicationService.when(batchActivateCustomers);
        
    }

    @PostMapping("batchDeactivateCustomers")
    public void batchDeactivateCustomers(
        @RequestBody String[] customerIds
    ) {
        BffCustomerServiceCommands.BatchDeactivateCustomers batchDeactivateCustomers = new BffCustomerServiceCommands.BatchDeactivateCustomers();
        batchDeactivateCustomers.setCustomerIds(customerIds);
        
        batchDeactivateCustomers.setRequesterId(SecurityContextUtil.getRequesterId());
        bffCustomerApplicationService.when(batchDeactivateCustomers);
        
    }

}

