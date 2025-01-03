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

@RequestMapping(path = "BffFacilities", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class BffFacilityServiceResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BffFacilityApplicationService bffFacilityApplicationService;

    @GetMapping
    public Page<BffFacilityDto> getFacilities(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "20") Integer size,
        @RequestParam(value = "active", required = false) String active
    ) {
        BffFacilityServiceCommands.GetFacilities getFacilities = new BffFacilityServiceCommands.GetFacilities();
        getFacilities.setPage(page);
        getFacilities.setSize(size);
        getFacilities.setActive(active);
        try {
        getFacilities.setRequesterId(SecurityContextUtil.getRequesterId());
        return bffFacilityApplicationService.when(getFacilities);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @GetMapping("{facilityId}")
    public BffFacilityDto getFacility(
        @PathVariable("facilityId") String facilityId
    ) {
        BffFacilityServiceCommands.GetFacility getFacility = new BffFacilityServiceCommands.GetFacility();
        getFacility.setFacilityId(facilityId);
        try {
        getFacility.setRequesterId(SecurityContextUtil.getRequesterId());
        return bffFacilityApplicationService.when(getFacility);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @PostMapping
    public String createFacility(
        @RequestBody @Valid BffFacilityDto facility
    ) {
        BffFacilityServiceCommands.CreateFacility createFacility = new BffFacilityServiceCommands.CreateFacility();
        createFacility.setFacility(facility);
        try {
        createFacility.setRequesterId(SecurityContextUtil.getRequesterId());
        return bffFacilityApplicationService.when(createFacility);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @PutMapping("{facilityId}")
    public void updateFacility(
        @PathVariable("facilityId") String facilityId,
        @RequestBody @Valid BffFacilityDto facility
    ) {
        BffFacilityServiceCommands.UpdateFacility updateFacility = new BffFacilityServiceCommands.UpdateFacility();
        updateFacility.setFacilityId(facilityId);
        updateFacility.setFacility(facility);
        try {
        updateFacility.setRequesterId(SecurityContextUtil.getRequesterId());
        bffFacilityApplicationService.when(updateFacility);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @PutMapping("{facilityId}/active")
    public void activateFacility(
        @PathVariable("facilityId") String facilityId,
        @RequestBody Boolean active
    ) {
        BffFacilityServiceCommands.ActivateFacility activateFacility = new BffFacilityServiceCommands.ActivateFacility();
        activateFacility.setFacilityId(facilityId);
        activateFacility.setActive(active);
        try {
        activateFacility.setRequesterId(SecurityContextUtil.getRequesterId());
        bffFacilityApplicationService.when(activateFacility);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @GetMapping("{facilityId}/Locations")
    public BffFacilityLocationDto[] getFacilityLocations(
        @PathVariable("facilityId") String facilityId,
        @RequestParam(value = "active", required = false) String active
    ) {
        BffFacilityServiceCommands.GetFacilityLocations getFacilityLocations = new BffFacilityServiceCommands.GetFacilityLocations();
        getFacilityLocations.setFacilityId(facilityId);
        getFacilityLocations.setActive(active);
        try {
        getFacilityLocations.setRequesterId(SecurityContextUtil.getRequesterId());
        return java.util.stream.StreamSupport.stream((bffFacilityApplicationService.when(getFacilityLocations)).spliterator(), false).collect(java.util.stream.Collectors.toList()).toArray(new BffFacilityLocationDto[0]);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @GetMapping("{facilityId}/Locations/{locationSeqId}")
    public BffFacilityLocationDto getFacilityLocation(
        @PathVariable("facilityId") String facilityId,
        @PathVariable("locationSeqId") String locationSeqId
    ) {
        BffFacilityServiceCommands.GetFacilityLocation getFacilityLocation = new BffFacilityServiceCommands.GetFacilityLocation();
        getFacilityLocation.setFacilityId(facilityId);
        getFacilityLocation.setLocationSeqId(locationSeqId);
        try {
        getFacilityLocation.setRequesterId(SecurityContextUtil.getRequesterId());
        return bffFacilityApplicationService.when(getFacilityLocation);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @PostMapping("{facilityId}/Locations")
    public void createFacilityLocation(
        @PathVariable("facilityId") String facilityId,
        @RequestBody BffFacilityLocationDto facilityLocation
    ) {
        BffFacilityServiceCommands.CreateFacilityLocation createFacilityLocation = new BffFacilityServiceCommands.CreateFacilityLocation();
        createFacilityLocation.setFacilityId(facilityId);
        createFacilityLocation.setFacilityLocation(facilityLocation);
        try {
        createFacilityLocation.setRequesterId(SecurityContextUtil.getRequesterId());
        bffFacilityApplicationService.when(createFacilityLocation);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @PutMapping("{facilityId}/Locations/{locationSeqId}")
    public void updateFacilityLocation(
        @PathVariable("facilityId") String facilityId,
        @PathVariable("locationSeqId") String locationSeqId,
        @RequestBody BffFacilityLocationDto facilityLocation
    ) {
        BffFacilityServiceCommands.UpdateFacilityLocation updateFacilityLocation = new BffFacilityServiceCommands.UpdateFacilityLocation();
        updateFacilityLocation.setFacilityId(facilityId);
        updateFacilityLocation.setLocationSeqId(locationSeqId);
        updateFacilityLocation.setFacilityLocation(facilityLocation);
        try {
        updateFacilityLocation.setRequesterId(SecurityContextUtil.getRequesterId());
        bffFacilityApplicationService.when(updateFacilityLocation);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @PutMapping("{facilityId}/Locations/{locationSeqId}/active")
    public void activateFacilityLocation(
        @PathVariable("facilityId") String facilityId,
        @PathVariable("locationSeqId") String locationSeqId,
        @RequestBody Boolean active
    ) {
        BffFacilityServiceCommands.ActivateFacilityLocation activateFacilityLocation = new BffFacilityServiceCommands.ActivateFacilityLocation();
        activateFacilityLocation.setFacilityId(facilityId);
        activateFacilityLocation.setLocationSeqId(locationSeqId);
        activateFacilityLocation.setActive(active);
        try {
        activateFacilityLocation.setRequesterId(SecurityContextUtil.getRequesterId());
        bffFacilityApplicationService.when(activateFacilityLocation);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

}

