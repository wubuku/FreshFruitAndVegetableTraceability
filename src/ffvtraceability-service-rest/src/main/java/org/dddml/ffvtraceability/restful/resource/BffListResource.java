package org.dddml.ffvtraceability.restful.resource;

import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.service.*;
import org.dddml.ffvtraceability.specialization.DomainErrorUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(path = "BffLists", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class BffListResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BffRawItemApplicationService bffRawItemApplicationService;
    @Autowired
    private BffSupplierApplicationService bffSupplierApplicationService;
    @Autowired
    private BffFacilityApplicationService bffFacilityApplicationService;
    @Autowired
    private BffUomApplicationService bffUomApplicationService;
    @Autowired
    private BffShipmentBoxTypeApplicationService bffShipmentBoxTypeApplicationService;

    @GetMapping("/ShipmentBoxTypes")
    public List<? extends BffShipmentBoxTypeDto> getShipmentBoxTypes(
            @RequestParam(value = "active", required = false) String active,
            @RequestParam(value = "shipmentBoxTypeId", required = false) String shipmentBoxTypeId
    ) {
        BffShipmentBoxTypeServiceCommands.GetShipmentBoxTypes getShipmentBoxTypes = new BffShipmentBoxTypeServiceCommands.GetShipmentBoxTypes();
        getShipmentBoxTypes.setPage(0);
        getShipmentBoxTypes.setSize(Integer.MAX_VALUE);
        getShipmentBoxTypes.setActive(active);
        getShipmentBoxTypes.setShipmentBoxTypeId(shipmentBoxTypeId);
        try {
            getShipmentBoxTypes.setRequesterId(SecurityContextUtil.getRequesterId());
            return bffShipmentBoxTypeApplicationService.when(getShipmentBoxTypes).getContent();
        } catch (Exception ex) {
            logger.info(ex.getMessage(), ex);
            throw DomainErrorUtils.convertException(ex);
        }
    }

    @GetMapping("RawItems") // 因为对应的分页查询接口的路径名是 "BffRawItems"
    public List<? extends BffRawItemDto> getRawItems(
            @RequestParam(value = "active", required = false) String active
    ) {
        BffRawItemServiceCommands.GetRawItems getRawItems = new BffRawItemServiceCommands.GetRawItems();
        getRawItems.setPage(0);
        getRawItems.setSize(Integer.MAX_VALUE);
        getRawItems.setActive(active);
        try {
            getRawItems.setRequesterId(SecurityContextUtil.getRequesterId());
            Page<BffRawItemDto> rawItemDtoPage = bffRawItemApplicationService.when(getRawItems);
            return rawItemDtoPage.getContent();
        } catch (Exception ex) {
            logger.info(ex.getMessage(), ex);
            throw DomainErrorUtils.convertException(ex);
        }
    }

    @GetMapping("/Suppliers")
    public List<? extends BffSupplierDto> getSuppliers(
            @RequestParam(value = "active", required = false) String active
    ) {
        BffSupplierServiceCommands.GetSuppliers getSuppliers = new BffSupplierServiceCommands.GetSuppliers();
        getSuppliers.setPage(0);
        getSuppliers.setSize(Integer.MAX_VALUE);
        getSuppliers.setActive(active);
        try {
            getSuppliers.setRequesterId(SecurityContextUtil.getRequesterId());
            return bffSupplierApplicationService.when(getSuppliers).getContent();
        } catch (Exception ex) {
            logger.info(ex.getMessage(), ex);
            throw DomainErrorUtils.convertException(ex);
        }
    }

    @GetMapping("/Facilities")
    public List<? extends BffFacilityDto> getFacilities(
            @RequestParam(value = "active", required = false) String active
    ) {
        BffFacilityServiceCommands.GetFacilities getFacilities = new BffFacilityServiceCommands.GetFacilities();
        getFacilities.setPage(0);
        getFacilities.setSize(Integer.MAX_VALUE);
        getFacilities.setActive(active);
        try {
            getFacilities.setRequesterId(SecurityContextUtil.getRequesterId());
            return bffFacilityApplicationService.when(getFacilities).getContent();
        } catch (Exception ex) {
            logger.info(ex.getMessage(), ex);
            throw DomainErrorUtils.convertException(ex);
        }
    }


    @GetMapping("/UnitsOfMeasure")
    public List<? extends BffUomDto> getUnitsOfMeasure(
            @RequestParam(value = "active", required = false) String active,
            @RequestParam(value = "uomTypeId", required = false) String uomTypeId
    ) {
        BffUomServiceCommands.GetUnitsOfMeasure getUnitsOfMeasure = new BffUomServiceCommands.GetUnitsOfMeasure();
        getUnitsOfMeasure.setPage(0);
        getUnitsOfMeasure.setSize(Integer.MAX_VALUE);
        getUnitsOfMeasure.setActive(active);
        getUnitsOfMeasure.setUomTypeId(uomTypeId);
        try {
            getUnitsOfMeasure.setRequesterId(SecurityContextUtil.getRequesterId());
            return bffUomApplicationService.when(getUnitsOfMeasure).getContent();
        } catch (Exception ex) {
            logger.info(ex.getMessage(), ex);
            throw DomainErrorUtils.convertException(ex);
        }
    }
}
