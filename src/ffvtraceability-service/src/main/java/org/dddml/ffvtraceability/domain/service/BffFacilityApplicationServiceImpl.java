package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffBusinessContactDto;
import org.dddml.ffvtraceability.domain.BffFacilityDto;
import org.dddml.ffvtraceability.domain.BffFacilityLocationDto;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.domain.contactmech.ContactMechTypeId;
import org.dddml.ffvtraceability.domain.facility.*;
import org.dddml.ffvtraceability.domain.facilitycontactmech.AbstractFacilityContactMechCommand;
import org.dddml.ffvtraceability.domain.facilitycontactmech.FacilityContactMechApplicationService;
import org.dddml.ffvtraceability.domain.facilitycontactmech.FacilityContactMechId;
import org.dddml.ffvtraceability.domain.facilitycontactmech.FacilityContactMechState;
import org.dddml.ffvtraceability.domain.facilitylocation.AbstractFacilityLocationCommand;
import org.dddml.ffvtraceability.domain.facilitylocation.FacilityLocationApplicationService;
import org.dddml.ffvtraceability.domain.facilitylocation.FacilityLocationId;
import org.dddml.ffvtraceability.domain.facilitylocation.FacilityLocationState;
import org.dddml.ffvtraceability.domain.mapper.BffFacilityLocationMapper;
import org.dddml.ffvtraceability.domain.mapper.BffFacilityMapper;
import org.dddml.ffvtraceability.domain.repository.*;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.domain.util.PageUtils;
import org.dddml.ffvtraceability.domain.util.TelecomNumberUtil;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.dddml.ffvtraceability.domain.constants.BffFacilityConstants.FACILITY_IDENTIFICATION_TYPE_FFRN;
import static org.dddml.ffvtraceability.domain.constants.BffFacilityConstants.FACILITY_IDENTIFICATION_TYPE_GLN;
import static org.dddml.ffvtraceability.domain.util.IndicatorUtils.INDICATOR_NO;
import static org.dddml.ffvtraceability.domain.util.IndicatorUtils.INDICATOR_YES;

@Service
@Transactional
public class BffFacilityApplicationServiceImpl implements BffFacilityApplicationService {

    private static final String ERROR_STATE_NOT_FOUND = "State not found: %s";
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

    static BffBusinessContactDto getBusinessContact(
            BffFacilityContactMechRepository bffFacilityContactMechRepository,
            String facilityId
    ) {
        AtomicReference<BffBusinessContactDto> bc = new AtomicReference<>();
        bffFacilityContactMechRepository.findFacilityCurrentPostalAddressByFacilityId(facilityId).ifPresent(x -> {
            bc.set(new BffBusinessContactDto());
            bc.get().setBusinessName(x.getToName());
            bc.get().setPhysicalLocationAddress(x.getAddress1());
            bc.get().setCity(x.getCity());
            bc.get().setState(x.getStateProvinceGeoName());
            bc.get().setStateProvinceGeoId(x.getStateProvinceGeoId());
            bc.get().setCountryGeoId(x.getCountryGeoId());
            bc.get().setCountry(x.getCountryGeoName());
            bc.get().setZipCode(x.getPostalCode());
        });

        bffFacilityContactMechRepository.findFacilityCurrentTelecomNumberByFacilityId(facilityId).ifPresent(x -> {
            if (bc.get() == null) {
                bc.set(new BffBusinessContactDto());
            }
            bc.get().setPhoneNumber(
                    TelecomNumberUtil.format(x.getCountryCode(), x.getAreaCode(), x.getContactNumber())
            );
        });
        return bc.get();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BffFacilityDto> when(BffFacilityServiceCommands.GetFacilities c) {
        return PageUtils.toPage(
                bffFacilityRepository.findAllFacilities(PageRequest.of(c.getPage(), c.getSize()),
                        c.getActive(), c.getOwnerPartyId()
                ),
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
        enrichBusinessContactDetails(dto, c.getFacilityId());
        return dto;
    }

    private void enrichBusinessContactDetails(BffFacilityDto dto, String facilityId) {
        BffBusinessContactDto bc = getBusinessContact(bffFacilityContactMechRepository, facilityId);
        if (bc != null) {
            dto.setBusinessContacts(Collections.singletonList(bc));
        }
    }

    @Override
    @Transactional
    public String when(BffFacilityServiceCommands.CreateFacility c) {
        String facilityId = c.getFacility().getFacilityId() != null ? c.getFacility().getFacilityId() : IdUtils.randomId();
        AbstractFacilityCommand.SimpleCreateFacility createFacility = new AbstractFacilityCommand.SimpleCreateFacility();
        createFacility.setFacilityId(facilityId);
        createFacility.setFacilityTypeId(c.getFacility().getFacilityTypeId());
        createFacility.setParentFacilityId(c.getFacility().getParentFacilityId());
        createFacility.setOwnerPartyId(c.getFacility().getOwnerPartyId()); // TODO 为 null 时填入当前租户对应的 PartyId？
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

        if (c.getFacility().getBusinessContacts() != null && !c.getFacility().getBusinessContacts().isEmpty()) {
            createFacilityBusinessContact(createFacility.getFacilityId(), c.getFacility().getBusinessContacts().get(0), c);
        }

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

        if (c.getFacility().getBusinessContacts() != null && !c.getFacility().getBusinessContacts().isEmpty()) {
            updateOrCreateFacilityBusinessContact(facilityId, c.getFacility().getBusinessContacts().get(0), c);
        }
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
    public void when(BffFacilityServiceCommands.BatchAddFacilityLocations c) {
        //todo
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
        // 处理邮政地址
        Optional<BffBusinessContactRepository.PostalAddressProjection> pa = bffBusinessContactRepository.findOnePostalAddressByBusinessInfo(
                bizContact.getBusinessName(), bizContact.getZipCode(),
                bizContact.getState(), bizContact.getCity(), bizContact.getPhysicalLocationAddress()
        );
        if (pa.isPresent()) {
            updateOrCreateFacilityContactMechAssociation(facilityId, pa.get().getContactMechId(), ContactMechTypeId.POSTAL_ADDRESS, "-PP", c);
        } else {
            // 创建新的邮政地址
            String contactMechId = bffBusinessContactService.createPostalAddress(bizContact, c);
            createFacilityContactMechAssociation(facilityId, contactMechId, "-PP", c);
        }

        // 处理电话号码
        TelecomNumberUtil.TelecomNumberDto tn = TelecomNumberUtil.parse(bizContact.getPhoneNumber());
        Optional<BffBusinessContactRepository.TelecomNumberProjection> telecomNumber = bffBusinessContactRepository.findOneTelecomNumberByPhoneInfo(
                tn.getCountryCode(), tn.getAreaCode(), tn.getContactNumber());

        if (telecomNumber.isPresent()) {
            String tnContactMechId = telecomNumber.get().getContactMechId();
            updateOrCreateFacilityContactMechAssociation(facilityId, tnContactMechId, ContactMechTypeId.TELECOM_NUMBER, "-PT", c);
        } else {
            String contactMechId = bffBusinessContactService.createTelecomNumber(bizContact, c);
            createFacilityContactMechAssociation(facilityId, contactMechId, "-PT", c);
        }
    }

    private void createFacilityBusinessContact(
            String facilityId, BffBusinessContactDto bizContact, Command c
    ) {
        if (bizContact.getPhysicalLocationAddress() != null && !bizContact.getPhysicalLocationAddress().trim().isEmpty()) {
            if (bizContact.getStateProvinceGeoId() != null) {
                Optional<BffGeoRepository.StateProvinceProjection> stateProvince
                        = bffGeoRepository.findStateOrProvinceById(bizContact.getStateProvinceGeoId());
                if (!stateProvince.isPresent()) {
                    throw new IllegalArgumentException(String.format(ERROR_STATE_NOT_FOUND, bizContact.getStateProvinceGeoId()));
                }
            } else {
                throw new NullPointerException("State or province id cant be null");
            }
            String contactMechId = bffBusinessContactService.createPostalAddress(bizContact, c);
            createFacilityContactMechAssociation(facilityId, contactMechId, "-PP", c);
        }

        if (bizContact.getPhoneNumber() != null && !bizContact.getPhoneNumber().trim().isEmpty()) {
            String contactMechId = bffBusinessContactService.createTelecomNumber(bizContact, c);
            createFacilityContactMechAssociation(facilityId, contactMechId, "-PT", c);
        }
    }

    private void updateOrCreateFacilityContactMechAssociation(
            String facilityId,
            String contactMechId,
            String contactMechTypeId,
            String commandIdSuffix,
            Command c
    ) {
        Optional<BffFacilityContactMechRepository.FacilityContactMechIdProjection> pcmIdPrj = bffFacilityContactMechRepository
                .findFacilityCurrentContactMechByContactMechType(facilityId, contactMechTypeId);
        if (!pcmIdPrj.isPresent()) {
            createFacilityContactMechAssociation(facilityId, contactMechId, commandIdSuffix, c);
        } else {
            OffsetDateTime fromDate = OffsetDateTime.ofInstant(pcmIdPrj.get().getFromDate(), ZoneOffset.UTC);
            FacilityContactMechState pcm = facilityContactMechApplicationService.get(new FacilityContactMechId(
                    facilityId, contactMechId, fromDate
            ));
            if (pcm == null) {
                createFacilityContactMechAssociation(facilityId, contactMechId, commandIdSuffix, c);
            } else {
                AbstractFacilityContactMechCommand.SimpleMergePatchFacilityContactMech mergePatchFacilityContactMech
                        = new AbstractFacilityContactMechCommand.SimpleMergePatchFacilityContactMech();
                mergePatchFacilityContactMech.setFacilityContactMechId(pcm.getFacilityContactMechId());
                mergePatchFacilityContactMech.setVersion(pcm.getVersion());
                mergePatchFacilityContactMech.setThruDate(OffsetDateTime.now().plusYears(100));
                mergePatchFacilityContactMech.setCommandId(c.getCommandId() != null ? c.getCommandId() + commandIdSuffix : UUID.randomUUID().toString());
                mergePatchFacilityContactMech.setRequesterId(c.getRequesterId());
                facilityContactMechApplicationService.when(mergePatchFacilityContactMech);
            }
        }
    }

    private void createFacilityContactMechAssociation(
            String facilityId,
            String contactMechId,
            String commandIdSuffix,
            Command c
    ) {
        AbstractFacilityContactMechCommand.SimpleCreateFacilityContactMech createFacilityContactMechBase
                = buildFacilityContactMechCreationCommand(facilityId, contactMechId,
                OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC)
        );
        createFacilityContactMechBase.setCommandId(c.getCommandId() != null ? c.getCommandId() + commandIdSuffix : UUID.randomUUID().toString());
        createFacilityContactMechBase.setRequesterId(c.getRequesterId());
        facilityContactMechApplicationService.when(createFacilityContactMechBase);
    }

    private AbstractFacilityContactMechCommand.SimpleCreateFacilityContactMech buildFacilityContactMechCreationCommand(
            String facilityId,
            String contactMechId,
            OffsetDateTime fromDate
    ) {
        AbstractFacilityContactMechCommand.SimpleCreateFacilityContactMech createFacilityContactMech =
                new AbstractFacilityContactMechCommand.SimpleCreateFacilityContactMech();
        createFacilityContactMech.setFacilityContactMechId(new FacilityContactMechId(facilityId, contactMechId, fromDate));
        createFacilityContactMech.setThruDate(OffsetDateTime.now().plusYears(100));
        return createFacilityContactMech;
    }
}