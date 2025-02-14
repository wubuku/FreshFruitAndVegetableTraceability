package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffBusinessContactDto;
import org.dddml.ffvtraceability.domain.BffFacilityDto;
import org.dddml.ffvtraceability.domain.BffFacilityLocationDto;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.domain.contactmech.*;
import org.dddml.ffvtraceability.domain.facility.*;
import org.dddml.ffvtraceability.domain.facilitycontactmech.AbstractFacilityContactMechCommand;
import org.dddml.ffvtraceability.domain.facilitycontactmech.FacilityContactMechApplicationService;
import org.dddml.ffvtraceability.domain.facilitycontactmech.FacilityContactMechId;
import org.dddml.ffvtraceability.domain.facilitycontactmech.FacilityContactMechState;
import org.dddml.ffvtraceability.domain.facilitylocation.AbstractFacilityLocationCommand;
import org.dddml.ffvtraceability.domain.facilitylocation.FacilityLocationApplicationService;
import org.dddml.ffvtraceability.domain.facilitylocation.FacilityLocationId;
import org.dddml.ffvtraceability.domain.facilitylocation.FacilityLocationState;
import org.dddml.ffvtraceability.domain.mapper.BffBusinessContactMapper;
import org.dddml.ffvtraceability.domain.mapper.BffFacilityLocationMapper;
import org.dddml.ffvtraceability.domain.mapper.BffFacilityMapper;
import org.dddml.ffvtraceability.domain.repository.*;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.domain.util.PageUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static org.dddml.ffvtraceability.domain.constants.BffFacilityConstants.*;
import static org.dddml.ffvtraceability.domain.util.IndicatorUtils.INDICATOR_NO;
import static org.dddml.ffvtraceability.domain.util.IndicatorUtils.INDICATOR_YES;

@Service
@Transactional
public class BffFacilityApplicationServiceImpl implements BffFacilityApplicationService {

    private static final String ERROR_FACILITY_NOT_FOUND = "Facility not found: %s";
    private static final String ERROR_FACILITY_ALREADY_EXISTS = "Facility already exists: %s";
    private static final String ERROR_FACILITY_SUPPLIER = "Duplicate facility: %s";
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
    @Autowired
    private FacilityContactMechApplicationService facilityContactMechApplicationService;
    @Autowired
    private BffGeoRepository bffGeoRepository;
    @Autowired
    private BffBusinessContactRepository bffBusinessContactRepository;
    @Autowired
    private BffFacilityContactMechRepository bffFacilityContactMechRepository;
    @Autowired
    private BffBusinessContactService bffBusinessContactService;
    @Autowired
    private BffBusinessContactMapper bffBusinessContactMapper;
    @Autowired
    private ContactMechApplicationService contactMechApplicationService;

    @Override
    @Transactional(readOnly = true)
    public Page<BffFacilityDto> when(BffFacilityServiceCommands.GetFacilities c) {
        var page = PageUtils.toPage(
                bffFacilityRepository.findAllFacilities(PageRequest.of(c.getPage(), c.getSize()),
                        c.getActive(), c.getOwnerPartyId()),
                bffFacilityMapper::toBffFacilityDto);
        if (c.getIncludedBusinessContacts() != null && c.getIncludedBusinessContacts()) {
            page.getContent().forEach(dto -> {
                bffFacilityContactMechRepository.findFacilityContactByFacilityId(dto.getFacilityId())
                        .ifPresent(contact -> dto.setBusinessContacts(Collections.singletonList(bffBusinessContactMapper.toBffBusinessContactDto(contact))));
            });
        }
        return page;
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
            } else if (x.getFacilityIdentificationTypeId().equals(FACILITY_IDENTIFICATION_TYPE_INTERNAL_ID)) {
                dto.setInternalId(x.getIdValue());
            }
        });
        bffFacilityContactMechRepository.findFacilityContactByFacilityId(c.getFacilityId())
                .ifPresent(contact -> dto.setBusinessContacts(Collections.singletonList(bffBusinessContactMapper.toBffBusinessContactDto(contact))));
        return dto;
    }

    @Override
    @Transactional
    public String when(BffFacilityServiceCommands.CreateFacility c) {
        BffFacilityDto facility = c.getFacility();
        if (facility.getFacilityId() != null && !facility.getFacilityId().trim().isEmpty()) {
            String facilityId = facility.getFacilityId().trim();
            FacilityState facilityState = facilityApplicationService.get(facilityId);
            if (facilityState != null) {
                throw new IllegalArgumentException(String.format(ERROR_FACILITY_ALREADY_EXISTS, facilityId));
            }
            facility.setFacilityId(facilityId);
        }
        return createSingleFacility(c.getFacility(), c);
    }

    @Override
    @Transactional
    public void when(BffFacilityServiceCommands.BatchAddFacilities c) {
        List<String> facilityIds = new ArrayList<>();
        Arrays.stream(c.getFacilities()).forEach(facility -> {
            if (facility.getFacilityId() != null && !facility.getFacilityId().trim().isEmpty()) {
                String facilityId = facility.getFacilityId().trim();
                if (facilityIds.contains(facilityId)) {
                    throw new IllegalArgumentException(String.format(ERROR_FACILITY_SUPPLIER, facilityId));
                }
                FacilityState facilityState = facilityApplicationService.get(facilityId);
                if (facilityState != null) {
                    throw new IllegalArgumentException(String.format(ERROR_FACILITY_ALREADY_EXISTS, facilityId));
                }
                facility.setFacilityId(facilityId);// 保证不存在空格
                facilityIds.add(facilityId);
            }
        });// 这一段纯粹是检查

        // 下面这一段才是真正的添加
        Arrays.stream(c.getFacilities()).forEach(facility -> {
            createSingleFacility(facility, c);
        });
    }

    private String createSingleFacility(BffFacilityDto facility, Command c) {
        AbstractFacilityCommand.SimpleCreateFacility createFacility = new AbstractFacilityCommand.SimpleCreateFacility();
        createFacility.setFacilityId(facility.getFacilityId() != null ? facility.getFacilityId() : IdUtils.randomId());
        createFacility.setFacilityTypeId(facility.getFacilityTypeId());
        createFacility.setParentFacilityId(facility.getParentFacilityId());
        createFacility.setOwnerPartyId(facility.getOwnerPartyId()); // TODO 为 null 时填入当前租户对应的 PartyId？
        createFacility.setFacilityName(facility.getFacilityName());
        createFacility.setDescription(facility.getDescription());
        createFacility.setFacilitySize(facility.getFacilitySize());
        createFacility.setSequenceNumber(facility.getSequenceNumber());
        createFacility.setFacilityLevel(facility.getFacilityLevel());
        createFacility.setFacilitySizeUomId(facility.getFacilitySizeUomId());
        createFacility.setGeoPointId(facility.getGeoPointId());
        createFacility.setGeoId(facility.getGeoId());
        createFacility.setActive(INDICATOR_YES); // 默认激活
        createFacility.setCommandId(createFacility.getFacilityId());
        createFacility.setRequesterId(c.getRequesterId());

        if (facility.getFfrn() != null) {
            addFacilityIdentification(createFacility, FACILITY_IDENTIFICATION_TYPE_FFRN, facility.getFfrn());
        }
        if (facility.getGln() != null) {
            addFacilityIdentification(createFacility, FACILITY_IDENTIFICATION_TYPE_GLN, facility.getGln());
        }
        if (facility.getInternalId() != null) {
            addFacilityIdentification(createFacility, FACILITY_IDENTIFICATION_TYPE_INTERNAL_ID, facility.getInternalId());
        }

        facilityApplicationService.when(createFacility);

        if (facility.getBusinessContacts() != null && !facility.getBusinessContacts().isEmpty()) {
            String contactMechId = bffBusinessContactService.createMiscContact(facility.getBusinessContacts().get(0), c);
            createFacilityContactMechAssociation(createFacility.getFacilityId(), contactMechId, "-PE", c);
        }
        return createFacility.getFacilityId();
    }

    private void addFacilityIdentification(
            AbstractFacilityCommand.SimpleCreateFacility createFacility,
            String identificationTypeId,
            String idValue) {
        if (idValue != null) {
            FacilityIdentificationCommand.CreateFacilityIdentification createFacilityIdentification = createFacility
                    .newCreateFacilityIdentification();
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

        updateFacilityIdentification(facilityState, mergePatchFacility, FACILITY_IDENTIFICATION_TYPE_FFRN,
                c.getFacility().getFfrn());
        updateFacilityIdentification(facilityState, mergePatchFacility, FACILITY_IDENTIFICATION_TYPE_GLN,
                c.getFacility().getGln());
        updateFacilityIdentification(facilityState, mergePatchFacility, FACILITY_IDENTIFICATION_TYPE_INTERNAL_ID,
                c.getFacility().getInternalId());

        facilityApplicationService.when(mergePatchFacility);

        updateOrCreateFacilityBusinessContact(facilityId, c.getFacility().getBusinessContacts().get(0), c);
//        if (c.getFacility().getBusinessContacts() != null && !c.getFacility().getBusinessContacts().isEmpty()) {
//            updateOrCreateFacilityBusinessContact(facilityId, c.getFacility().getBusinessContacts().get(0), c);
//        }
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
                    FacilityIdentificationCommand.MergePatchFacilityIdentification m = mergePatchFacility
                            .newMergePatchFacilityIdentification();
                    m.setFacilityIdentificationTypeId(identificationTypeId);
                    m.setIdValue(newValue);
                    mergePatchFacility.getFacilityIdentificationCommands().add(m);
                }
            } else {
                // 如果新值为空,则删除
                FacilityIdentificationCommand.RemoveFacilityIdentification r = mergePatchFacility
                        .newRemoveFacilityIdentification();
                r.setFacilityIdentificationTypeId(identificationTypeId);
                mergePatchFacility.getFacilityIdentificationCommands().add(r);
            }
        } else {
            // 如果不存在且新值不为空,则创建
            if (StringUtils.hasText(newValue)) {
                FacilityIdentificationCommand.CreateFacilityIdentification createFacilityIdentification = mergePatchFacility
                        .newCreateFacilityIdentification();
                createFacilityIdentification.setFacilityIdentificationTypeId(identificationTypeId);
                createFacilityIdentification.setIdValue(newValue);
                mergePatchFacility.getFacilityIdentificationCommands().add(createFacilityIdentification);
            }
        }
    }

    @Override
    @Transactional
    public void when(BffFacilityServiceCommands.ActivateFacility c) {
        boolean active = c.getActive() != null && c.getActive();
        updateFacilityActive(c.getFacilityId(), active, c);
    }

    @Override
    @Transactional
    public void when(BffFacilityServiceCommands.BatchActivateFacilities c) {
        batchUpdateFacilityActive(c.getFacilityIds(), true, c);
    }

    @Override
    @Transactional
    public void when(BffFacilityServiceCommands.BatchDeactivateFacilities c) {
        batchUpdateFacilityActive(c.getFacilityIds(), false, c);
    }

    private void batchUpdateFacilityActive(String[] facilityIds, boolean active, Command c) {
        Arrays.stream(facilityIds).forEach(facilityId -> updateFacilityActive(facilityId, active, c));
    }

    private void updateFacilityActive(String facilityId, boolean active, Command c) {
        FacilityState facilityState = facilityApplicationService.get(facilityId);
        if (facilityState == null) {
            throw new IllegalArgumentException("Facility not found: " + facilityId);
        }

        AbstractFacilityCommand.SimpleMergePatchFacility mergePatchFacility = new AbstractFacilityCommand.SimpleMergePatchFacility();
        mergePatchFacility.setFacilityId(facilityId);
        mergePatchFacility.setVersion(facilityState.getVersion());
        mergePatchFacility.setActive(active ? INDICATOR_YES : INDICATOR_NO);
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
                new FacilityLocationId(c.getFacilityId(), c.getLocationSeqId()));
        if (locationState != null) {
            return bffFacilityLocationMapper.toBffFacilityLocationDto(locationState);
        }
        return null;
    }

    private void createSingleFacilityLocation(
            String facilityId,
            BffFacilityLocationDto location,
            String commandId,
            String requesterId) {
        AbstractFacilityLocationCommand.SimpleCreateFacilityLocation createLocation = new AbstractFacilityLocationCommand.SimpleCreateFacilityLocation();
        FacilityLocationId locationId = new FacilityLocationId();
        locationId.setFacilityId(facilityId);
        if (location.getLocationSeqId() != null) {
            locationId.setLocationSeqId(location.getLocationSeqId());
            if (facilityLocationApplicationService.get(locationId) != null) {
                throw new IllegalArgumentException(String.format("Location already exists. Location: %s,%s", facilityId, location.getLocationSeqId()));
            }
        } else {
            locationId.setLocationSeqId(IdUtils.randomId());
        }
        createLocation.setFacilityLocationId(locationId);
        createLocation.setLocationTypeEnumId(location.getLocationTypeEnumId());
        createLocation.setAreaId(location.getAreaId());
        createLocation.setAisleId(location.getAisleId());
        createLocation.setSectionId(location.getSectionId());
        createLocation.setLevelId(location.getLevelId());
        createLocation.setPositionId(location.getPositionId());
        createLocation.setGeoPointId(location.getGeoPointId());
        createLocation.setLocationName(location.getLocationName());
        createLocation.setLocationCode(location.getLocationCode());
        createLocation.setDescription(location.getDescription());
        createLocation.setGln(location.getGln());
        createLocation.setActive(INDICATOR_YES); // 默认激活
        createLocation.setCommandId(commandId != null ? commandId : locationId.getLocationSeqId());
        createLocation.setRequesterId(requesterId);

        facilityLocationApplicationService.when(createLocation);
    }

    @Override
    @Transactional
    public void when(BffFacilityServiceCommands.CreateFacilityLocation c) {
        if (c.getFacilityLocation() == null) {
            throw new IllegalArgumentException("FacilityLocation cant be null");
        }
        if (c.getFacilityLocation().getLocationName() == null && c.getFacilityLocation().getLocationSeqId() == null) {
            throw new IllegalArgumentException("LocationSeqId and LocationName cannot both be null");
        }
        createSingleFacilityLocation(
                c.getFacilityId(),
                c.getFacilityLocation(),
                c.getCommandId(),
                c.getRequesterId());
    }

    @Override
    @Transactional
    public void when(BffFacilityServiceCommands.BatchAddFacilityLocations c) {
        if (c.getFacilityId() == null) {
            throw new IllegalArgumentException("FacilityId is required.");
        }
        //首先查看一下有没有重复的LocationSeqId
        List<String> locationSeqIds = new ArrayList<>();
        Arrays.stream(c.getFacilityLocations()).forEach(location -> {
            if (location.getLocationSeqId() != null) {
                if (locationSeqIds.contains(location.getLocationSeqId())) {
                    throw new IllegalArgumentException(String.format("Duplicate Location: %s", location.getLocationSeqId()));
                }
                locationSeqIds.add(location.getLocationSeqId());
            } else {
                if (location.getLocationName() == null || location.getLocationName().isBlank()) {
                    throw new IllegalArgumentException("LocationSeqId and LocationName cannot both be null");
                }
            }
        });
        Arrays.stream(c.getFacilityLocations())
                .forEach(location -> {
                    createSingleFacilityLocation(
                            c.getFacilityId(),
                            location,
                            c.getCommandId(),
                            c.getRequesterId());
                });
    }

    @Override
    @Transactional
    public void when(BffFacilityServiceCommands.UpdateFacilityLocation c) {
        FacilityLocationId locationId = new FacilityLocationId(c.getFacilityId(), c.getLocationSeqId());
        FacilityLocationState locationState = facilityLocationApplicationService.get(locationId);
        if (locationState == null) {
            throw new IllegalArgumentException("Facility location not found: " + locationId);
        }

        AbstractFacilityLocationCommand.SimpleMergePatchFacilityLocation mergePatchLocation = new AbstractFacilityLocationCommand.SimpleMergePatchFacilityLocation();
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
        boolean active = c.getActive() != null && c.getActive();
        updateFacilityLocationActive(
                c.getFacilityId(),
                c.getLocationSeqId(),
                active,
                c);
    }

    @Override
    @Transactional
    public void when(BffFacilityServiceCommands.BatchDeactivateLocations c) {
        Arrays.stream(c.getLocationSeqIds()).forEach(locationSeqId -> updateFacilityLocationActive(
                c.getFacilityId(),
                locationSeqId,
                false,
                c));
    }

    @Override
    @Transactional
    public void when(BffFacilityServiceCommands.BatchActivateLocations c) {
        Arrays.stream(c.getLocationSeqIds()).forEach(locationSeqId -> updateFacilityLocationActive(
                c.getFacilityId(),
                locationSeqId,
                true,
                c));
    }

    private void updateFacilityLocationActive(
            String facilityId,
            String locationSeqId,
            boolean active,
            Command c) {
        FacilityLocationId locationId = new FacilityLocationId(facilityId, locationSeqId);
        FacilityLocationState locationState = facilityLocationApplicationService.get(locationId);
        if (locationState == null) {
            throw new IllegalArgumentException("Facility location not found: " + locationId);
        }

        AbstractFacilityLocationCommand.SimpleMergePatchFacilityLocation mergePatchLocation = new AbstractFacilityLocationCommand.SimpleMergePatchFacilityLocation();
        mergePatchLocation.setFacilityLocationId(locationId);
        mergePatchLocation.setVersion(locationState.getVersion());
        mergePatchLocation.setActive(active ? INDICATOR_YES : INDICATOR_NO);
        mergePatchLocation.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchLocation.setRequesterId(c.getRequesterId());

        facilityLocationApplicationService.when(mergePatchLocation);
    }

    @Override
    public void when(BffFacilityServiceCommands.UpdateBusinessContact c) {
        String facilityId = c.getFacilityId();
        FacilityState f = facilityApplicationService.get(facilityId);
        if (f == null) {
            throw new IllegalArgumentException(String.format("Facility not found: %s", c.getFacilityId()));
        }
        BffBusinessContactDto bizContact = c.getBusinessContact();
        updateOrCreateFacilityBusinessContact(facilityId, bizContact, c);
    }

    private void updateOrCreateFacilityBusinessContact(String facilityId, BffBusinessContactDto bizContact, Command c) {

        var optional = bffFacilityContactMechRepository.findFacilityContactByFacilityId(facilityId);
        if (optional.isEmpty()) {//添加
            String contactMechId = bffBusinessContactService.createMiscContact(bizContact, c);
            createFacilityContactMechAssociation(facilityId, contactMechId, "-PE", c);
        } else {//更新
            String contactMechId = optional.get().getContactMechId();
            Optional<AbstractContactMechState> state = bffBusinessContactRepository.findById(contactMechId);
            if (state.isPresent()) {
                ContactMechCommand.MergePatchContactMech mergePatchContactMech
                        = new AbstractContactMechCommand.SimpleMergePatchMiscContactMech();
                mergePatchContactMech.setVersion(state.get().getVersion());
                mergePatchContactMech.setToName(bizContact.getBusinessName());
                mergePatchContactMech.setContactMechId(contactMechId);
                if (bizContact.getStateProvinceGeoId() != null) {
                    Optional<BffGeoRepository.StateProvinceProjection> stateProvince
                            = bffGeoRepository.findStateOrProvinceById(bizContact.getStateProvinceGeoId());
                    if (stateProvince.isEmpty()) {
                        throw new IllegalArgumentException(String.format("State or province not found: %s", bizContact.getStateProvinceGeoId()));
                    }
                    mergePatchContactMech.setCountryGeoId(stateProvince.get().getParentGeoId());
                    mergePatchContactMech.setStateProvinceGeoId(stateProvince.get().getGeoId());
                } else {
                    mergePatchContactMech.setCountryGeoId(bizContact.getCountryGeoId());
                }
                mergePatchContactMech.setCity(bizContact.getCity());
                mergePatchContactMech.setAddress1(bizContact.getPhysicalLocationAddress());
                mergePatchContactMech.setPostalCode(bizContact.getZipCode());
                mergePatchContactMech.setTelecomContactNumber(bizContact.getPhoneNumber());

                mergePatchContactMech.setEmail(bizContact.getEmail());
                mergePatchContactMech.setAskForRole(bizContact.getContactRole());
                mergePatchContactMech.setAskForName(bizContact.getBusinessName());
                mergePatchContactMech.setPhysicalLocationAddress(bizContact.getPhysicalLocationAddress());

                contactMechApplicationService.when(mergePatchContactMech);

                updateOrCreateFacilityContactMechAssociation(facilityId, contactMechId, ContactMechTypeId.MISC_CONTACT_MECH, "-PE", c);
            }
        }
        /*
        // 处理邮政地址
        if (bizContact.getStateProvinceGeoId() != null && bizContact.getStateProvinceGeoId().isEmpty()) {
            Optional<BffBusinessContactRepository.PostalAddressProjection> pa = bffBusinessContactRepository
                    .findOnePostalAddressByBusinessInfo(
                            bizContact.getBusinessName(), bizContact.getZipCode(),
                            bizContact.getStateProvinceGeoId(), bizContact.getCity(), bizContact.getPhysicalLocationAddress());
            if (pa.isPresent()) {
                updateOrCreateFacilityContactMechAssociation(facilityId, pa.get().getContactMechId(),
                        ContactMechTypeId.POSTAL_ADDRESS, "-PP", c);
            } else {
                // 创建新的邮政地址
                String contactMechId = bffBusinessContactService.createPostalAddress(bizContact, c);
                createFacilityContactMechAssociation(facilityId, contactMechId, "-PP", c);
            }
        }

        // 处理电话号码
        TelecomNumberUtil.TelecomNumberDto tn = TelecomNumberUtil.parse(bizContact.getPhoneNumber());
        Optional<BffBusinessContactRepository.TelecomNumberProjection> telecomNumber = bffBusinessContactRepository
                .findOneTelecomNumberByPhoneInfo(
                        tn.getCountryCode(), tn.getAreaCode(), tn.getContactNumber());

        if (telecomNumber.isPresent()) {
            String tnContactMechId = telecomNumber.get().getContactMechId();
            updateOrCreateFacilityContactMechAssociation(facilityId, tnContactMechId, ContactMechTypeId.TELECOM_NUMBER,
                    "-PT", c);
        } else {
            String contactMechId = bffBusinessContactService.createTelecomNumber(bizContact, c);
            createFacilityContactMechAssociation(facilityId, contactMechId, "-PT", c);
        }*/
    }

    private void updateOrCreateFacilityContactMechAssociation(
            String facilityId,
            String contactMechId,
            String contactMechTypeId,
            String commandIdSuffix,
            Command c) {
        Optional<BffFacilityContactMechRepository.FacilityContactMechIdProjection> pcmIdPrj = bffFacilityContactMechRepository
                .findFacilityCurrentContactMechByContactMechType(facilityId, contactMechTypeId);
        if (!pcmIdPrj.isPresent()) {
            createFacilityContactMechAssociation(facilityId, contactMechId, commandIdSuffix, c);
        } else {
            OffsetDateTime fromDate = OffsetDateTime.ofInstant(pcmIdPrj.get().getFromDate(), ZoneOffset.UTC);
            FacilityContactMechState pcm = facilityContactMechApplicationService.get(new FacilityContactMechId(
                    facilityId, contactMechId, fromDate));
            if (pcm == null) {//FIXME 感觉这里永远不会是null
                createFacilityContactMechAssociation(facilityId, contactMechId, commandIdSuffix, c);
            } else {
                AbstractFacilityContactMechCommand.SimpleMergePatchFacilityContactMech mergePatchFacilityContactMech = new AbstractFacilityContactMechCommand.SimpleMergePatchFacilityContactMech();
                mergePatchFacilityContactMech.setFacilityContactMechId(pcm.getFacilityContactMechId());
                mergePatchFacilityContactMech.setVersion(pcm.getVersion());
                mergePatchFacilityContactMech.setThruDate(OffsetDateTime.now().plusYears(100));
                mergePatchFacilityContactMech.setCommandId(
                        c.getCommandId() != null ? c.getCommandId() + commandIdSuffix : UUID.randomUUID().toString());
                mergePatchFacilityContactMech.setRequesterId(c.getRequesterId());
                facilityContactMechApplicationService.when(mergePatchFacilityContactMech);
            }
        }
    }

    private void createFacilityContactMechAssociation(
            String facilityId,
            String contactMechId,
            String commandIdSuffix,
            Command c) {
        AbstractFacilityContactMechCommand.SimpleCreateFacilityContactMech createFacilityContactMechBase = buildFacilityContactMechCreationCommand(
                facilityId, contactMechId,
                OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC));
        createFacilityContactMechBase.setCommandId(
                c.getCommandId() != null ? c.getCommandId() + commandIdSuffix : UUID.randomUUID().toString());
        createFacilityContactMechBase.setRequesterId(c.getRequesterId());
        facilityContactMechApplicationService.when(createFacilityContactMechBase);
    }

    private AbstractFacilityContactMechCommand.SimpleCreateFacilityContactMech buildFacilityContactMechCreationCommand(
            String facilityId,
            String contactMechId,
            OffsetDateTime fromDate) {
        AbstractFacilityContactMechCommand.SimpleCreateFacilityContactMech createFacilityContactMech = new AbstractFacilityContactMechCommand.SimpleCreateFacilityContactMech();
        createFacilityContactMech
                .setFacilityContactMechId(new FacilityContactMechId(facilityId, contactMechId, fromDate));
        createFacilityContactMech.setThruDate(OffsetDateTime.now().plusYears(100));
        return createFacilityContactMech;
    }
}