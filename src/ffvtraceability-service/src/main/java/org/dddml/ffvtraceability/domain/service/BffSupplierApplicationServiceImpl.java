package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffBusinessContactDto;
import org.dddml.ffvtraceability.domain.BffFacilityDto;
import org.dddml.ffvtraceability.domain.BffSupplierDto;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.domain.contactmech.ContactMechApplicationService;
import org.dddml.ffvtraceability.domain.contactmech.ContactMechStateRepository;
import org.dddml.ffvtraceability.domain.contactmech.ContactMechTypeId;
import org.dddml.ffvtraceability.domain.mapper.BffFacilityMapper;
import org.dddml.ffvtraceability.domain.mapper.BffSupplierMapper;
import org.dddml.ffvtraceability.domain.party.*;
import org.dddml.ffvtraceability.domain.partycontactmech.AbstractPartyContactMechCommand;
import org.dddml.ffvtraceability.domain.partycontactmech.PartyContactMechApplicationService;
import org.dddml.ffvtraceability.domain.partycontactmech.PartyContactMechId;
import org.dddml.ffvtraceability.domain.partycontactmech.PartyContactMechState;
import org.dddml.ffvtraceability.domain.partyrole.AbstractPartyRoleCommand;
import org.dddml.ffvtraceability.domain.partyrole.PartyRoleApplicationService;
import org.dddml.ffvtraceability.domain.partyrole.PartyRoleId;
import org.dddml.ffvtraceability.domain.partyrole.PartyRoleState;
import org.dddml.ffvtraceability.domain.repository.*;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.domain.util.IndicatorUtils;
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
import java.util.*;
import java.util.stream.Collectors;

import static org.dddml.ffvtraceability.domain.constants.BffPartyConstants.*;

@Service
@Transactional
public class BffSupplierApplicationServiceImpl implements BffSupplierApplicationService {

    private static final String ERROR_SUPPLIER_NOT_FOUND = "Supplier not found: %s";
    private static final String ERROR_SUPPLIER_ALREADY_EXISTS = "Supplier already exists: %s";
    private static final String ERROR_DUPLICATE_SUPPLIER = "Duplicate supplier: %s";


    @Autowired
    private PartyApplicationService partyApplicationService;
    @Autowired
    private PartyRoleApplicationService partyRoleApplicationService;
    @Autowired
    private BffSupplierMapper bffSupplierMapper;
    @Autowired
    private BffSupplierRepository bffSupplierRepository;
    @Autowired
    private ContactMechStateRepository contactMechStateRepository;
    @Autowired
    private BffFacilityRepository bffFacilityRepository;
    @Autowired
    private BffFacilityMapper bffFacilityMapper;
    @Autowired
    private BffFacilityContactMechRepository bffFacilityContactMechRepository;

    @Autowired
    private PartyContactMechApplicationService partyContactMechApplicationService;
    @Autowired
    private BffBusinessContactRepository bffBusinessContactRepository;
    @Autowired
    private BffPartyContactMechRepository bffPartyContactMechRepository;

    @Autowired
    private BffBusinessContactService bffBusinessContactService;
    @Autowired
    private ContactMechApplicationService contactMechApplicationService;
    @Autowired
    private BffFacilityApplicationService bffFacilityApplicationService;

    @Override
    @Transactional(readOnly = true)
    public Page<BffSupplierDto> when(BffSupplierServiceCommands.GetSuppliers c) {
        String statusId = null;
        if (c.getActive() != null) {
            statusId = IndicatorUtils.toBoolean(c.getActive()) ? PARTY_STATUS_ACTIVE : PARTY_STATUS_INACTIVE;
        }
        return PageUtils.toPage(
                bffSupplierRepository.findAllSuppliers(PageRequest.of(c.getPage(), c.getSize()), statusId),
                bffSupplierMapper::toBffSupplierDto
        );
    }

    @Override
    @Transactional(readOnly = true)
    public BffSupplierDto when(BffSupplierServiceCommands.GetSupplier c) {
        Optional<BffSupplierProjection> projection = bffSupplierRepository.findSupplierById(c.getSupplierId());
        if (projection.isEmpty()) {
            return null;
        }
        BffSupplierDto dto = bffSupplierMapper.toBffSupplierDto(projection.get());
        enrichSupplierBusinessContactDetails(dto, c.getSupplierId());
        enrichFacilityDetails(dto, c.getSupplierId());
        return dto;
    }

    private void enrichFacilityDetails(BffSupplierDto dto, String supplierId) {
        List<BffFacilityProjection> facilityProjections = bffFacilityRepository.findFacilitiesByOwnerPartyId(supplierId);
        if (dto != null && !facilityProjections.isEmpty()) {
            List<BffFacilityDto> facilityDtos = new ArrayList<>();
            facilityProjections.forEach(bffFacilityProjection -> {
                BffFacilityDto bffFacilityDto = bffFacilityMapper.toBffFacilityDto(bffFacilityProjection);
                enrichFacilityBusinessContactDetails(bffFacilityDto, bffFacilityDto.getFacilityId());
                facilityDtos.add(bffFacilityDto);
            });
            dto.setFacilities(facilityDtos);
        }
    }

    private void enrichFacilityBusinessContactDetails(BffFacilityDto dto, String facilityId) {
        BffBusinessContactDto facilityContact = BffFacilityApplicationServiceImpl.getBusinessContact(
                bffFacilityContactMechRepository, facilityId
        );
        if (facilityContact != null) {
            dto.setBusinessContacts(Collections.singletonList(facilityContact));
        }
    }

    private void enrichSupplierBusinessContactDetails(BffSupplierDto dto, String supplierId) {

        bffPartyContactMechRepository.findPartyCurrentPostalAddressByPartyId(supplierId).ifPresent(x -> {
            BffBusinessContactDto bc = new BffBusinessContactDto();
            bc.setBusinessName(x.getToName());
            bc.setPhysicalLocationAddress(x.getAddress1());
            bc.setCity(x.getCity());
            bc.setStateProvinceGeoId(x.getStateProvinceGeoId());
            bc.setCountryGeoId(x.getCountryGeoId());
            bc.setState(x.getStateProvinceGeoName());
            bc.setCountry(x.getCountryGeoName());
            bc.setZipCode(x.getPostalCode());
            dto.setBusinessContacts(Collections.singletonList(bc));
        });

        bffPartyContactMechRepository.findPartyCurrentTelecomNumberByPartyId(supplierId).ifPresent(x -> {
            if (dto.getBusinessContacts() == null) {
                dto.setBusinessContacts(Collections.singletonList(new BffBusinessContactDto()));
            }
            dto.getBusinessContacts().get(0).setPhoneNumber(
                    TelecomNumberUtil.format(x.getCountryCode(), x.getAreaCode(), x.getContactNumber())
            );
        });

        bffPartyContactMechRepository.findPartyCurrentMisContactMechByPartyId(supplierId).ifPresent(x -> {
            if (dto.getBusinessContacts() == null) {
                dto.setBusinessContacts(Collections.singletonList(new BffBusinessContactDto()));
            }
            dto.getBusinessContacts().get(0).setEmail(x.getEmail());
            dto.getBusinessContacts().get(0).setContactRole(x.getAskForRole());
        });
    }

    @Override
    @Transactional
    public String when(BffSupplierServiceCommands.CreateSupplier c) {
        String partyId = createSupplierParty(c.getSupplier(), c);
        //联系方式
        if (c.getSupplier().getBusinessContacts() != null && !c.getSupplier().getBusinessContacts().isEmpty()) {
            createPartyBusinessContact(partyId, c.getSupplier().getBusinessContacts().get(0), c);
        }
        //设施
        if (c.getSupplier().getFacilities() != null && !c.getSupplier().getFacilities().isEmpty()) {
            BffFacilityServiceCommands.BatchAddFacilities batchAddFacilities = new BffFacilityServiceCommands.BatchAddFacilities();
            c.getSupplier().getFacilities().forEach(bffFacilityDto -> {
                bffFacilityDto.setOwnerPartyId(partyId);//将设施的ownerPartyId改为当前生成的Party的Id
            });
            batchAddFacilities.setFacilities(c.getSupplier().getFacilities().toArray(new BffFacilityDto[0]));
            batchAddFacilities.setRequesterId(c.getRequesterId());
            bffFacilityApplicationService.when(batchAddFacilities);
        }
        return partyId;
    }

    private void addPartyIdentification(
            AbstractPartyCommand.SimpleCreateParty createParty,
            String identificationTypeId,
            String idValue) {
        if (idValue != null) {
            PartyIdentificationCommand.CreatePartyIdentification createPartyIdentification = createParty.newCreatePartyIdentification();
            createPartyIdentification.setPartyIdentificationTypeId(identificationTypeId);
            createPartyIdentification.setIdValue(idValue);
            createParty.getCreatePartyIdentificationCommands().add(createPartyIdentification);
        }
    }

    @Override
    @Transactional
    public void when(BffSupplierServiceCommands.UpdateSupplier c) {

        String supplierId = c.getSupplierId();
        PartyState partyState = partyApplicationService.get(supplierId);
        if (partyState == null) {
            throw new IllegalArgumentException(String.format(ERROR_SUPPLIER_NOT_FOUND, c.getSupplierId()));
        }
        BffSupplierDto bffSupplier = c.getSupplier();
        AbstractPartyCommand.SimpleMergePatchOrganization mergePatchParty = new AbstractPartyCommand.SimpleMergePatchOrganization();
        mergePatchParty.setPartyId(supplierId);
        mergePatchParty.setOrganizationName(bffSupplier.getSupplierName());
        mergePatchParty.setVersion(partyState.getVersion());// 乐观锁
        mergePatchParty.setRequesterId(c.getRequesterId());
        mergePatchParty.setExternalId(bffSupplier.getExternalId());
        mergePatchParty.setDescription(bffSupplier.getDescription());
        mergePatchParty.setWebSite(bffSupplier.getWebSite());
        mergePatchParty.setTelephone(bffSupplier.getTelephone());
        if (bffSupplier.getPreferredCurrencyUomId() != null) {
            mergePatchParty.setPreferredCurrencyUomId(bffSupplier.getPreferredCurrencyUomId());
        }
        //以上为Supplier(Party本身）基础资料的修改


        updatePartyIdentification(partyState, mergePatchParty, PARTY_IDENTIFICATION_TYPE_GGN, bffSupplier.getGgn());
        updatePartyIdentification(partyState, mergePatchParty, PARTY_IDENTIFICATION_TYPE_GLN, bffSupplier.getGln());
        updatePartyIdentification(partyState, mergePatchParty, PARTY_IDENTIFICATION_TYPE_GS1_COMPANY_PREFIX, bffSupplier.getGgn());
        updatePartyIdentification(partyState, mergePatchParty, PARTY_IDENTIFICATION_TYPE_TAX_ID, bffSupplier.getGln());
        updatePartyIdentification(partyState, mergePatchParty, PARTY_IDENTIFICATION_TYPE_INTERNAL_ID, bffSupplier.getGln());


        if (StringUtils.hasText(c.getSupplier().getStatusId())
                && (PARTY_STATUS_ACTIVE.equals(c.getSupplier().getStatusId())
                || PARTY_STATUS_INACTIVE.equals(c.getSupplier().getStatusId()))) {
            mergePatchParty.setStatusId(c.getSupplier().getStatusId());
        }
        partyApplicationService.when(mergePatchParty);

        //修改PartyRole
        PartyRoleId partyRoleId = new PartyRoleId();
        partyRoleId.setPartyId(bffSupplier.getSupplierId());
        partyRoleId.setRoleTypeId(PARTY_ROLE_SUPPLIER);
        PartyRoleState partyRole = partyRoleApplicationService.get(partyRoleId);
        if (partyRole != null) {//有就改
            AbstractPartyRoleCommand.SimpleMergePatchPartyRole simpleMergePatchPartyRole = new AbstractPartyRoleCommand.SimpleMergePatchPartyRole();
            simpleMergePatchPartyRole.setPartyRoleId(partyRoleId);
            simpleMergePatchPartyRole.setRequesterId(c.getRequesterId());
            simpleMergePatchPartyRole.setBankAccountInformation(bffSupplier.getBankAccountInformation());
            simpleMergePatchPartyRole.setCertificationCodes(bffSupplier.getCertificationCodes());
            simpleMergePatchPartyRole.setSupplierShortName(bffSupplier.getSupplierShortName());
            simpleMergePatchPartyRole.setTpaNumber(bffSupplier.getTpaNumber());
            simpleMergePatchPartyRole.setSupplierProductTypeDescription(bffSupplier.getSupplierProductTypeDescription());
            simpleMergePatchPartyRole.setSupplierTypeEnumId(bffSupplier.getSupplierTypeEnumId());
            partyRoleApplicationService.when(simpleMergePatchPartyRole);
        } else {//没有就添加
            AbstractPartyRoleCommand.SimpleCreatePartyRole createPartyRole = new AbstractPartyRoleCommand.SimpleCreatePartyRole();
            partyRoleId.setRoleTypeId(PARTY_ROLE_SUPPLIER);
            createPartyRole.setPartyRoleId(partyRoleId);
            createPartyRole.setCommandId(c.getCommandId() + "-SPP");
            createPartyRole.setRequesterId(c.getRequesterId());
            createPartyRole.setBankAccountInformation(bffSupplier.getBankAccountInformation());
            createPartyRole.setCertificationCodes(bffSupplier.getCertificationCodes());
            createPartyRole.setSupplierShortName(bffSupplier.getSupplierShortName());
            createPartyRole.setTpaNumber(bffSupplier.getTpaNumber());
            createPartyRole.setSupplierProductTypeDescription(bffSupplier.getSupplierProductTypeDescription());
            createPartyRole.setSupplierTypeEnumId(bffSupplier.getSupplierTypeEnumId());
            partyRoleApplicationService.when(createPartyRole);
        }

        //修改联系方式
        if (c.getSupplier().getBusinessContacts() != null && !c.getSupplier().getBusinessContacts().isEmpty()) {
            updateOrCreatePartyBusinessContact(supplierId, c.getSupplier().getBusinessContacts().get(0), c);
        }

        //修改其关联设施列表
        List<BffFacilityProjection> facilityProjections = bffFacilityRepository.findFacilitiesByOwnerPartyId(supplierId);
        List<String> originalFacilityIds = new ArrayList<>();
        facilityProjections.forEach(bffFacilityProjection -> originalFacilityIds.add(bffFacilityProjection.getFacilityId()));
        List<String> newFacilityIds = bffSupplier.getFacilities().stream().map(BffFacilityDto::getFacilityId).filter(Objects::nonNull).collect(Collectors.toList());
        List<BffFacilityDto> needToAddedNoId = bffSupplier.getFacilities().stream().filter(bffFacilityDto -> bffFacilityDto.getFacilityId() == null).collect(Collectors.toList());
        List<String> needToUpdateIds = new ArrayList<>();
        List<String> needToAddedHasIds = new ArrayList<>();
        List<String> needToDeletedIds = new ArrayList<>();
        newFacilityIds.forEach(newId -> {
            if (originalFacilityIds.contains(newId)) {
                needToUpdateIds.add(newId);//原来有，现在也有，属于更新的
            } else {
                needToAddedHasIds.add(newId);//有Id,但是并不在原有的列表中，那么也属于应该添加的
            }
        });
        originalFacilityIds.forEach(oldId -> {
            if (!newFacilityIds.contains(oldId)) {
                needToDeletedIds.add(oldId);
            }
        });
    }

    private void updatePartyIdentification(
            PartyState partyState,
            AbstractPartyCommand.SimpleMergePatchParty mergePatchParty,
            String identificationTypeId,
            String newValue) {

        Optional<PartyIdentificationState> oldIdentification = partyState.getPartyIdentifications().stream()
                .filter(t -> t.getPartyIdentificationTypeId().equals(identificationTypeId))
                .findFirst();

        if (oldIdentification.isPresent()) {
            // 如果已存在标识
            if (StringUtils.hasText(newValue)) {
                // 如果新值不同,则更新
                if (!oldIdentification.get().getIdValue().equals(newValue)) {
                    PartyIdentificationCommand.MergePatchPartyIdentification m = mergePatchParty.newMergePatchPartyIdentification();
                    m.setPartyIdentificationTypeId(identificationTypeId);
                    m.setIdValue(newValue);
                    mergePatchParty.getPartyIdentificationCommands().add(m);
                }
            } else {
                // 如果新值为空,则删除
                PartyIdentificationCommand.RemovePartyIdentification r = mergePatchParty.newRemovePartyIdentification();
                r.setPartyIdentificationTypeId(identificationTypeId);
                mergePatchParty.getPartyIdentificationCommands().add(r);
            }
        } else {
            // 如果不存在且新值不为空,则创建
            if (StringUtils.hasText(newValue)) {
                PartyIdentificationCommand.CreatePartyIdentification createPartyIdentification = mergePatchParty.newCreatePartyIdentification();
                createPartyIdentification.setPartyIdentificationTypeId(identificationTypeId);
                createPartyIdentification.setIdValue(newValue);
                mergePatchParty.getPartyIdentificationCommands().add(createPartyIdentification);
            }
        }
    }

    @Override
    @Transactional
    public void when(BffSupplierServiceCommands.ActivateSupplier c) {
        String supplierId = c.getSupplierId();
        PartyState partyState = partyApplicationService.get(supplierId);
        if (partyState == null) {
            throw new IllegalArgumentException(String.format(ERROR_SUPPLIER_NOT_FOUND, c.getSupplierId()));
        }
        AbstractPartyCommand.SimpleMergePatchOrganization mergePatchParty = new AbstractPartyCommand.SimpleMergePatchOrganization();
        mergePatchParty.setPartyId(supplierId);
        mergePatchParty.setVersion(partyState.getVersion());
        if (c.getActive() != null && c.getActive()) {
            mergePatchParty.setStatusId(PARTY_STATUS_ACTIVE);
        } else {
            mergePatchParty.setStatusId(PARTY_STATUS_INACTIVE);
        }
        mergePatchParty.setRequesterId(c.getRequesterId());
        mergePatchParty.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        partyApplicationService.when(mergePatchParty);
    }

    @Override
    @Transactional
    public void when(BffSupplierServiceCommands.UpdateBusinessContact c) {
        String partyId = c.getSupplierId();
        PartyState p = partyApplicationService.get(partyId);
        if (p == null) {
            throw new IllegalArgumentException(String.format(ERROR_SUPPLIER_NOT_FOUND, c.getSupplierId()));
        }
        BffBusinessContactDto bizContact = c.getBusinessContact();
        updateOrCreatePartyBusinessContact(partyId, bizContact, c);
    }

    private String createSupplierParty(BffSupplierDto supplier, Command c) {
        AbstractPartyCommand.SimpleCreateOrganization createParty = new AbstractPartyCommand.SimpleCreateOrganization();
        if (supplier.getSupplierId() != null) {
            var partyState = partyApplicationService.get(supplier.getSupplierId());
            if (partyState != null) {
                throw new IllegalArgumentException(String.format(ERROR_SUPPLIER_ALREADY_EXISTS, supplier.getSupplierId()));
            }
            createParty.setPartyId(supplier.getSupplierId());
        } else {
            createParty.setPartyId(IdUtils.randomId());
        }
        createParty.setOrganizationName(supplier.getSupplierName());
        createParty.setExternalId(supplier.getExternalId());
        createParty.setDescription(supplier.getDescription());
        createParty.setWebSite(supplier.getWebSite());
        createParty.setEmail(supplier.getEmail());
        createParty.setTelephone(supplier.getTelephone());


        createParty.setPreferredCurrencyUomId(supplier.getPreferredCurrencyUomId() != null ? supplier.getPreferredCurrencyUomId() : DEFAULT_PREFERRED_CURRENCY_UOM_ID);
        if (supplier.getGgn() != null) {
            addPartyIdentification(createParty, PARTY_IDENTIFICATION_TYPE_GGN, supplier.getGgn());
        }
        if (supplier.getGln() != null) {
            addPartyIdentification(createParty, PARTY_IDENTIFICATION_TYPE_GLN, supplier.getGln());
        }
        if (supplier.getInternalId() != null) {
            addPartyIdentification(createParty, PARTY_IDENTIFICATION_TYPE_INTERNAL_ID, supplier.getInternalId());
        }
        if (supplier.getGs1CompanyPrefix() != null) {
            addPartyIdentification(createParty, PARTY_IDENTIFICATION_TYPE_GS1_COMPANY_PREFIX, supplier.getGs1CompanyPrefix());
        }
        if (supplier.getTaxId() != null) {
            addPartyIdentification(createParty, PARTY_IDENTIFICATION_TYPE_TAX_ID, supplier.getTaxId());
        }
        createParty.setStatusId(PARTY_STATUS_ACTIVE); // default status
        createParty.setCommandId(c.getCommandId() != null ? c.getCommandId() : createParty.getPartyId());
        createParty.setRequesterId(c.getRequesterId());
        partyApplicationService.when(createParty);

        AbstractPartyRoleCommand.SimpleCreatePartyRole createPartyRole = new AbstractPartyRoleCommand.SimpleCreatePartyRole();
        PartyRoleId partyRoleId = new PartyRoleId();
        partyRoleId.setPartyId(createParty.getPartyId());
        partyRoleId.setRoleTypeId(PARTY_ROLE_SUPPLIER);
        createPartyRole.setPartyRoleId(partyRoleId);
        createPartyRole.setCommandId(createParty.getCommandId() + "-SPP");
        createPartyRole.setRequesterId(c.getRequesterId());
        createPartyRole.setBankAccountInformation(supplier.getBankAccountInformation());
        createPartyRole.setCertificationCodes(supplier.getCertificationCodes());
        createPartyRole.setSupplierShortName(supplier.getSupplierShortName());
        createPartyRole.setTpaNumber(supplier.getTpaNumber());
        createPartyRole.setSupplierProductTypeDescription(supplier.getSupplierProductTypeDescription());
        createPartyRole.setSupplierTypeEnumId(supplier.getSupplierTypeEnumId());
        partyRoleApplicationService.when(createPartyRole);

        return createParty.getPartyId();
    }

    @Override
    @Transactional
    public void when(BffSupplierServiceCommands.BatchAddSuppliers c) {
        //首先查看提供了supplier Id的记录，看是否有重复。
        List<String> supplierIds = new ArrayList<>();
        for (var supplier : c.getSuppliers()) {
            if (supplier.getSupplierId() != null && !supplier.getSupplierId().isEmpty()) {
                if (supplierIds.contains(supplier.getSupplierId())) {
                    throw new IllegalArgumentException(String.format(ERROR_DUPLICATE_SUPPLIER, supplier.getSupplierId()));
                }
                supplierIds.add(supplier.getSupplierId());
            }
        }
        //循环以添加新的供应商
        for (BffSupplierDto supplierDto : c.getSuppliers()) {
            String partyId = createSupplierParty(supplierDto, c);
            if (supplierDto.getBusinessContacts() != null && !supplierDto.getBusinessContacts().isEmpty()) {
                createPartyBusinessContact(partyId, supplierDto.getBusinessContacts().get(0), c);
            }
            //设施
            if (supplierDto.getFacilities() != null && !supplierDto.getFacilities().isEmpty()) {
                BffFacilityServiceCommands.BatchAddFacilities batchAddFacilities = new BffFacilityServiceCommands.BatchAddFacilities();
                supplierDto.getFacilities().forEach(bffFacilityDto -> {
                    bffFacilityDto.setOwnerPartyId(partyId);//将设施的ownerPartyId改为当前生成的Party的Id
                });
                batchAddFacilities.setFacilities(supplierDto.getFacilities().toArray(new BffFacilityDto[0]));
                batchAddFacilities.setRequesterId(c.getRequesterId());
                bffFacilityApplicationService.when(batchAddFacilities);
            }
        }
    }

    @Override
    @Transactional
    public void when(BffSupplierServiceCommands.BatchActivateSuppliers c) {
        Arrays.stream(c.getSupplierIds()).forEach(supplierId -> {
            PartyState partyState = partyApplicationService.get(supplierId);
            if (partyState == null) {
                throw new IllegalArgumentException(String.format(ERROR_SUPPLIER_NOT_FOUND, supplierId));
            }
            if (!partyState.getStatusId().equals(PARTY_STATUS_ACTIVE)) {
                AbstractPartyCommand.SimpleMergePatchOrganization mergePatchParty = new AbstractPartyCommand.SimpleMergePatchOrganization();
                mergePatchParty.setPartyId(supplierId);
                mergePatchParty.setVersion(partyState.getVersion());
                mergePatchParty.setStatusId(PARTY_STATUS_ACTIVE);
                mergePatchParty.setRequesterId(c.getRequesterId());
                mergePatchParty.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
                partyApplicationService.when(mergePatchParty);
            }
        });
    }

    @Override
    @Transactional
    public void when(BffSupplierServiceCommands.BatchDeactivateSuppliers c) {
        Arrays.stream(c.getSupplierIds()).forEach(supplierId -> {
            PartyState partyState = partyApplicationService.get(supplierId);
            if (partyState == null) {
                throw new IllegalArgumentException(String.format(ERROR_SUPPLIER_NOT_FOUND, supplierId));
            }
            if (!partyState.getStatusId().equals(PARTY_STATUS_INACTIVE)) {
                AbstractPartyCommand.SimpleMergePatchOrganization mergePatchParty = new AbstractPartyCommand.SimpleMergePatchOrganization();
                mergePatchParty.setPartyId(supplierId);
                mergePatchParty.setVersion(partyState.getVersion());
                mergePatchParty.setStatusId(PARTY_STATUS_INACTIVE);
                mergePatchParty.setRequesterId(c.getRequesterId());
                mergePatchParty.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
                partyApplicationService.when(mergePatchParty);
            }
        });
    }

    private void updateOrCreatePartyBusinessContact(String partyId, BffBusinessContactDto bizContact, Command c) {
        // 处理邮政地址
        Optional<BffBusinessContactRepository.PostalAddressProjection> pa = bffBusinessContactRepository.findOnePostalAddressByBusinessInfo(
                bizContact.getBusinessName(), bizContact.getZipCode(),
                bizContact.getState(), bizContact.getCity(), bizContact.getPhysicalLocationAddress()
        );
        if (pa.isPresent()) {
            updateOrCreatePartyContactMechAssociation(partyId, pa.get().getContactMechId(), ContactMechTypeId.POSTAL_ADDRESS, "-PP", c);
        } else {
            // 创建新的邮政地址
            String contactMechId = bffBusinessContactService.createPostalAddress(bizContact, c);
            createPartyContactMechAssociation(partyId, contactMechId, "-PP", c);
            //handlePartyContactMechAssociation(partyId, createPostalAddress.getContactMechId(), "-PP", c);
        }

        // 处理电话号码
        TelecomNumberUtil.TelecomNumberDto tn = TelecomNumberUtil.parse(bizContact.getPhoneNumber());
        Optional<BffBusinessContactRepository.TelecomNumberProjection> telecomNumber = bffBusinessContactRepository.findOneTelecomNumberByPhoneInfo(
                tn.getCountryCode(), tn.getAreaCode(), tn.getContactNumber());

        String tnContactMechId;
        if (telecomNumber.isPresent()) {
            tnContactMechId = telecomNumber.get().getContactMechId();
            updateOrCreatePartyContactMechAssociation(partyId, tnContactMechId, ContactMechTypeId.TELECOM_NUMBER, "-PT", c);
        } else {
            String contactMechId = bffBusinessContactService.createTelecomNumber(bizContact, c);
            createPartyContactMechAssociation(partyId, contactMechId, "-PT", c);
            //handlePartyContactMechAssociation(partyId, createTelecomNumber.getContactMechId(), "-PT", c);
        }
    }

    private void createPartyBusinessContact(
            String partyId, BffBusinessContactDto bizContact, Command c
    ) {
        if (bizContact.getPhysicalLocationAddress() != null && !bizContact.getPhysicalLocationAddress().trim().isEmpty()) {
            String contactMechId = bffBusinessContactService.createPostalAddress(bizContact, c);
            createPartyContactMechAssociation(partyId, contactMechId, "-PP", c);
        }

        if (bizContact.getPhoneNumber() != null && !bizContact.getPhoneNumber().trim().isEmpty()) {
            String contactMechId = bffBusinessContactService.createTelecomNumber(bizContact, c);
            createPartyContactMechAssociation(partyId, contactMechId, "-PT", c);
        }
        if (bizContact.getEmail() != null && !bizContact.getEmail().trim().isEmpty()) {
            String contactMechId = bffBusinessContactService.createMiscContact(bizContact, c);
            createPartyContactMechAssociation(partyId, contactMechId, "-PE", c);
        }
    }

    private void updateOrCreatePartyContactMechAssociation(
            String partyId,
            String contactMechId,
            String contactMechTypeId,
            String commandIdSuffix,
            Command c
    ) {
        Optional<BffPartyContactMechRepository.PartyContactMechIdProjection> pcmIdPrj = bffPartyContactMechRepository
                .findPartyCurrentContactMechByContactMechType(partyId, contactMechTypeId);
        if (!pcmIdPrj.isPresent()) {
            createPartyContactMechAssociation(partyId, contactMechId, commandIdSuffix, c);
        } else {
            OffsetDateTime fromDate = OffsetDateTime.ofInstant(pcmIdPrj.get().getFromDate(), ZoneOffset.UTC);
            PartyContactMechState pcm = partyContactMechApplicationService.get(new PartyContactMechId(
                    partyId, contactMechId, fromDate
            ));
            if (pcm == null) {
                // Should not happen?
                // throw new IllegalArgumentException(String.format(ERROR_PARTY_CONTACT_MECH_NOT_FOUND, partyId, contactMechId, fromDate));
                createPartyContactMechAssociation(partyId, contactMechId, commandIdSuffix, c);
            } else {
                AbstractPartyContactMechCommand.SimpleMergePatchPartyContactMech mergePatchPartyContactMech
                        = new AbstractPartyContactMechCommand.SimpleMergePatchPartyContactMech();
                mergePatchPartyContactMech.setPartyContactMechId(pcm.getPartyContactMechId());
                mergePatchPartyContactMech.setVersion(pcm.getVersion());
                mergePatchPartyContactMech.setThruDate(OffsetDateTime.now().plusYears(100));
                mergePatchPartyContactMech.setCommandId(c.getCommandId() != null ? c.getCommandId() + commandIdSuffix : UUID.randomUUID().toString());
                mergePatchPartyContactMech.setRequesterId(c.getRequesterId());
                partyContactMechApplicationService.when(mergePatchPartyContactMech);
            }
        }
    }

    private void createPartyContactMechAssociation(
            String partyId,
            String contactMechId,
            String commandIdSuffix,
            Command c
    ) {
        AbstractPartyContactMechCommand.SimpleCreatePartyContactMech createPartyContactMechBase
                = buildPartyContactMechCreationCommand(partyId, contactMechId,
                OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC) // to UTC?
        );
        createPartyContactMechBase.setCommandId(c.getCommandId() != null ? c.getCommandId() + commandIdSuffix : UUID.randomUUID().toString());
        createPartyContactMechBase.setRequesterId(c.getRequesterId());
        partyContactMechApplicationService.when(createPartyContactMechBase);
    }

    private AbstractPartyContactMechCommand.SimpleCreatePartyContactMech buildPartyContactMechCreationCommand(
            String partyId,
            String contactMechId,
            OffsetDateTime fromDate
    ) {
        AbstractPartyContactMechCommand.SimpleCreatePartyContactMech createPartyContactMech = new AbstractPartyContactMechCommand.SimpleCreatePartyContactMech();
        createPartyContactMech.setPartyContactMechId(new PartyContactMechId(partyId, contactMechId, fromDate));
        createPartyContactMech.setThruDate(OffsetDateTime.now().plusYears(100));
        return createPartyContactMech;
    }

}
