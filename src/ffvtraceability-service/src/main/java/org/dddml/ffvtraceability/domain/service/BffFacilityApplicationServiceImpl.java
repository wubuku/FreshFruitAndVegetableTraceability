package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffFacilityDto;
import org.dddml.ffvtraceability.domain.BffFacilityLocationDto;
import org.dddml.ffvtraceability.domain.facility.*;
import org.dddml.ffvtraceability.domain.facilitylocation.AbstractFacilityLocationCommand;
import org.dddml.ffvtraceability.domain.facilitylocation.FacilityLocationApplicationService;
import org.dddml.ffvtraceability.domain.facilitylocation.FacilityLocationId;
import org.dddml.ffvtraceability.domain.facilitylocation.FacilityLocationState;
import org.dddml.ffvtraceability.domain.mapper.BffFacilityLocationMapper;
import org.dddml.ffvtraceability.domain.mapper.BffFacilityMapper;
import org.dddml.ffvtraceability.domain.repository.BffFacilityLocationRepository;
import org.dddml.ffvtraceability.domain.repository.BffFacilityRepository;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.domain.util.PageUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.dddml.ffvtraceability.domain.util.IndicatorUtils.INDICATOR_NO;
import static org.dddml.ffvtraceability.domain.util.IndicatorUtils.INDICATOR_YES;

@Service
public class BffFacilityApplicationServiceImpl implements BffFacilityApplicationService {
    public static final String FACILITY_IDENTIFICATION_TYPE_FFRN = "FFRN";
    public static final String FACILITY_IDENTIFICATION_TYPE_GLN = "GLN";

    @Autowired
    private BffFacilityRepository bffFacilityRepository;

    @Autowired
    private FacilityApplicationService facilityApplicationService;

    @Autowired
    private BffFacilityMapper bffFacilityMapper;

    @Autowired
    private FacilityLocationApplicationService facilityLocationApplicationService;

    @Autowired
    private BffFacilityLocationRepository bffFacilityLocationRepository;

    @Autowired
    private BffFacilityLocationMapper bffFacilityLocationMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<BffFacilityDto> when(BffFacilityServiceCommands.GetFacilities c) {
        return PageUtils.toPage(
                bffFacilityRepository.findAllFacilities(PageRequest.of(c.getPage(), c.getSize()), c.getActive()),
                bffFacilityMapper::toBffFacilityDto
        );
    }

    @Override
    @Transactional(readOnly = true)
    public BffFacilityDto when(BffFacilityServiceCommands.GetFacility c) {
        FacilityState facilityState = facilityApplicationService.get(c.getFacilityId());
        if (facilityState == null) {
            return null;
        }
        BffFacilityDto dto = bffFacilityMapper.toBffFacilityDto(facilityState);
        facilityState.getFacilityIdentifications().stream().forEach(x -> {
            if (x.getFacilityIdentificationTypeId().equals(FACILITY_IDENTIFICATION_TYPE_GLN)) {
                dto.setGln(x.getIdValue());
            } else if (x.getFacilityIdentificationTypeId().equals(FACILITY_IDENTIFICATION_TYPE_FFRN)) {
                dto.setFfrn(x.getIdValue());
            }
        });
        return dto;
    }

    @Override
    @Transactional
    public String when(BffFacilityServiceCommands.CreateFacility c) {
        String facilityId = c.getFacility().getFacilityId() != null ? c.getFacility().getFacilityId() : IdUtils.randomId();
        AbstractFacilityCommand.SimpleCreateFacility createFacility = new AbstractFacilityCommand.SimpleCreateFacility();
        createFacility.setFacilityId(facilityId);
        createFacility.setFacilityTypeId(c.getFacility().getFacilityTypeId());
        createFacility.setParentFacilityId(c.getFacility().getParentFacilityId());
        createFacility.setOwnerPartyId(c.getFacility().getOwnerPartyId());
        createFacility.setFacilityName(c.getFacility().getFacilityName());
        createFacility.setDescription(c.getFacility().getDescription());
        createFacility.setFacilitySize(c.getFacility().getFacilitySize());
        createFacility.setFacilitySizeUomId(c.getFacility().getFacilitySizeUomId());
        createFacility.setGeoPointId(c.getFacility().getGeoPointId());
        createFacility.setGeoId(c.getFacility().getGeoId());
        createFacility.setActive(INDICATOR_YES); // 默认激活
        createFacility.setCommandId(createFacility.getFacilityId());
        createFacility.setRequesterId(c.getRequesterId());

        if (c.getFacility().getFfrn() != null) {
            addFacilityIdentification(createFacility, FACILITY_IDENTIFICATION_TYPE_FFRN, c.getFacility().getFfrn());
        }
        if (c.getFacility().getGln() != null) {
            addFacilityIdentification(createFacility, FACILITY_IDENTIFICATION_TYPE_GLN, c.getFacility().getGln());
        }

        facilityApplicationService.when(createFacility);
        return createFacility.getFacilityId();
    }

    private void addFacilityIdentification(
            AbstractFacilityCommand.SimpleCreateFacility createFacility,
            String identificationTypeId,
            String idValue) {
        if (idValue != null) {
            FacilityIdentificationCommand.CreateFacilityIdentification createFacilityIdentification = createFacility.newCreateFacilityIdentification();
            createFacilityIdentification.setFacilityIdentificationTypeId(identificationTypeId);
            createFacilityIdentification.setIdValue(idValue);
            createFacility.getCreateFacilityIdentificationCommands().add(createFacilityIdentification);
        }
    }

    @Override
    @Transactional
    public void when(BffFacilityServiceCommands.UpdateFacility c) {
        String facilityId = c.getFacilityId();
        FacilityState facilityState = facilityApplicationService.get(facilityId);
        if (facilityState == null) {
            throw new IllegalArgumentException("Facility not found: " + facilityId);
        }
        AbstractFacilityCommand.SimpleMergePatchFacility mergePatchFacility = new AbstractFacilityCommand.SimpleMergePatchFacility();
        mergePatchFacility.setFacilityId(facilityId);
        mergePatchFacility.setVersion(facilityState.getVersion());
        mergePatchFacility.setFacilityTypeId(c.getFacility().getFacilityTypeId());
        mergePatchFacility.setParentFacilityId(c.getFacility().getParentFacilityId());
        mergePatchFacility.setOwnerPartyId(c.getFacility().getOwnerPartyId());
        mergePatchFacility.setFacilityName(c.getFacility().getFacilityName());
        mergePatchFacility.setDescription(c.getFacility().getDescription());
        mergePatchFacility.setFacilitySize(c.getFacility().getFacilitySize());
        mergePatchFacility.setFacilitySizeUomId(c.getFacility().getFacilitySizeUomId());
        mergePatchFacility.setGeoPointId(c.getFacility().getGeoPointId());
        mergePatchFacility.setGeoId(c.getFacility().getGeoId());
        mergePatchFacility.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchFacility.setRequesterId(c.getRequesterId());

        updateFacilityIdentification(facilityState, mergePatchFacility, FACILITY_IDENTIFICATION_TYPE_FFRN, c.getFacility().getFfrn());
        updateFacilityIdentification(facilityState, mergePatchFacility, FACILITY_IDENTIFICATION_TYPE_GLN, c.getFacility().getGln());

        facilityApplicationService.when(mergePatchFacility);
    }

    private void updateFacilityIdentification(
            FacilityState facilityState,
            AbstractFacilityCommand.SimpleMergePatchFacility mergePatchFacility,
            String identificationTypeId,
            String newValue) {

        Optional<FacilityIdentificationState> oldIdentification = facilityState.getFacilityIdentifications().stream()
                .filter(t -> t.getFacilityIdentificationTypeId().equals(identificationTypeId))
                .findFirst();

        if (oldIdentification.isPresent()) {
            // 如果已存在标识
            if (StringUtils.hasText(newValue)) {
                // 如果新值不同,则更新
                if (!oldIdentification.get().getIdValue().equals(newValue)) {
                    FacilityIdentificationCommand.MergePatchFacilityIdentification m =
                            mergePatchFacility.newMergePatchFacilityIdentification();
                    m.setFacilityIdentificationTypeId(identificationTypeId);
                    m.setIdValue(newValue);
                    mergePatchFacility.getFacilityIdentificationCommands().add(m);
                }
            } else {
                // 如果新值为空,则删除
                FacilityIdentificationCommand.RemoveFacilityIdentification r =
                        mergePatchFacility.newRemoveFacilityIdentification();
                r.setFacilityIdentificationTypeId(identificationTypeId);
                mergePatchFacility.getFacilityIdentificationCommands().add(r);
            }
        } else {
            // 如果不存在且新值不为空,则创建
            if (StringUtils.hasText(newValue)) {
                FacilityIdentificationCommand.CreateFacilityIdentification createFacilityIdentification =
                        mergePatchFacility.newCreateFacilityIdentification();
                createFacilityIdentification.setFacilityIdentificationTypeId(identificationTypeId);
                createFacilityIdentification.setIdValue(newValue);
                mergePatchFacility.getFacilityIdentificationCommands().add(createFacilityIdentification);
            }
        }
    }

    @Override
    @Transactional
    public void when(BffFacilityServiceCommands.ActivateFacility c) {
        String facilityId = c.getFacilityId();
        FacilityState facilityState = facilityApplicationService.get(facilityId);
        if (facilityState == null) {
            throw new IllegalArgumentException("Facility not found: " + facilityId);
        }
        AbstractFacilityCommand.SimpleMergePatchFacility mergePatchFacility = new AbstractFacilityCommand.SimpleMergePatchFacility();
        mergePatchFacility.setFacilityId(facilityId);
        mergePatchFacility.setVersion(facilityState.getVersion());
        mergePatchFacility.setActive(c.getActive() ? INDICATOR_YES : INDICATOR_NO);
        mergePatchFacility.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchFacility.setRequesterId(c.getRequesterId());
        facilityApplicationService.when(mergePatchFacility);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<BffFacilityLocationDto> when(BffFacilityServiceCommands.GetFacilityLocations c) {
        return bffFacilityLocationRepository.findFacilityLocations(c.getFacilityId(), c.getActive())
                .stream()
                .map(bffFacilityLocationMapper::toBffFacilityLocationDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BffFacilityLocationDto when(BffFacilityServiceCommands.GetFacilityLocation c) {
        FacilityLocationState locationState = facilityLocationApplicationService.get(
                new FacilityLocationId(c.getFacilityId(), c.getLocationSeqId())
        );
        if (locationState != null) {
            return bffFacilityLocationMapper.toBffFacilityLocationDto(locationState);
        }
        return null;
    }

    @Override
    @Transactional
    public void when(BffFacilityServiceCommands.CreateFacilityLocation c) {
        if (c.getFacilityId() == null) {
            throw new IllegalArgumentException("FacilityId is required.");
        }
        if (c.getFacilityLocation().getLocationSeqId() == null) {
            throw new IllegalArgumentException("LocationSeqId is required.");
        }
        if (!c.getFacilityLocation().getLocationSeqId().startsWith(c.getFacilityId())) {
            throw new IllegalArgumentException("LocationSeqId must start with FacilityId.");// NOTE: 这个要求合理？
        }

        AbstractFacilityLocationCommand.SimpleCreateFacilityLocation createLocation = new AbstractFacilityLocationCommand.SimpleCreateFacilityLocation();
        FacilityLocationId locationId = new FacilityLocationId(c.getFacilityId(), c.getFacilityLocation().getLocationSeqId());
        createLocation.setFacilityLocationId(locationId);
        createLocation.setLocationTypeEnumId(c.getFacilityLocation().getLocationTypeEnumId());
        createLocation.setAreaId(c.getFacilityLocation().getAreaId());
        createLocation.setAisleId(c.getFacilityLocation().getAisleId());
        createLocation.setSectionId(c.getFacilityLocation().getSectionId());
        createLocation.setLevelId(c.getFacilityLocation().getLevelId());
        createLocation.setPositionId(c.getFacilityLocation().getPositionId());
        createLocation.setGeoPointId(c.getFacilityLocation().getGeoPointId());
        createLocation.setActive(INDICATOR_YES); // 默认激活
        createLocation.setCommandId(c.getCommandId() != null ? c.getCommandId() : locationId.getLocationSeqId());
        createLocation.setRequesterId(c.getRequesterId());

        facilityLocationApplicationService.when(createLocation);
    }

    @Override
    @Transactional
    public void when(BffFacilityServiceCommands.UpdateFacilityLocation c) {
        FacilityLocationId locationId = new FacilityLocationId(c.getFacilityId(), c.getLocationSeqId());
        FacilityLocationState locationState = facilityLocationApplicationService.get(locationId);
        if (locationState == null) {
            throw new IllegalArgumentException("Facility location not found: " + locationId);
        }

        AbstractFacilityLocationCommand.SimpleMergePatchFacilityLocation mergePatchLocation =
                new AbstractFacilityLocationCommand.SimpleMergePatchFacilityLocation();
        mergePatchLocation.setFacilityLocationId(locationId);
        mergePatchLocation.setVersion(locationState.getVersion());
        mergePatchLocation.setLocationTypeEnumId(c.getFacilityLocation().getLocationTypeEnumId());
        mergePatchLocation.setAreaId(c.getFacilityLocation().getAreaId());
        mergePatchLocation.setAisleId(c.getFacilityLocation().getAisleId());
        mergePatchLocation.setSectionId(c.getFacilityLocation().getSectionId());
        mergePatchLocation.setLevelId(c.getFacilityLocation().getLevelId());
        mergePatchLocation.setPositionId(c.getFacilityLocation().getPositionId());
        mergePatchLocation.setGeoPointId(c.getFacilityLocation().getGeoPointId());
        mergePatchLocation.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchLocation.setRequesterId(c.getRequesterId());

        facilityLocationApplicationService.when(mergePatchLocation);
    }

    @Override
    @Transactional
    public void when(BffFacilityServiceCommands.ActivateFacilityLocation c) {
        FacilityLocationId locationId = new FacilityLocationId(c.getFacilityId(), c.getLocationSeqId());
        FacilityLocationState locationState = facilityLocationApplicationService.get(locationId);
        if (locationState == null) {
            throw new IllegalArgumentException("Facility location not found: " + locationId);
        }

        AbstractFacilityLocationCommand.SimpleMergePatchFacilityLocation mergePatchLocation =
                new AbstractFacilityLocationCommand.SimpleMergePatchFacilityLocation();
        mergePatchLocation.setFacilityLocationId(locationId);
        mergePatchLocation.setVersion(locationState.getVersion());
        mergePatchLocation.setActive(c.getActive() ? INDICATOR_YES : INDICATOR_NO);
        mergePatchLocation.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchLocation.setRequesterId(c.getRequesterId());

        facilityLocationApplicationService.when(mergePatchLocation);
    }

}
