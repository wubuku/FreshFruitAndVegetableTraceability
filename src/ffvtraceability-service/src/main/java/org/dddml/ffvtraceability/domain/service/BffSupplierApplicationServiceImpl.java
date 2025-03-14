package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffBusinessContactDto;
import org.dddml.ffvtraceability.domain.BffFacilityDto;
import org.dddml.ffvtraceability.domain.BffSupplierDto;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.domain.contactmech.*;
import org.dddml.ffvtraceability.domain.mapper.BffBusinessContactMapper;
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
    private BffGeoRepository bffGeoRepository;
    @Autowired
    private BffBusinessContactMapper bffBusinessContactMapper;
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
        bffPartyContactMechRepository.findPartyContactByPartyId(c.getSupplierId()).ifPresent(contact -> {
            dto.setBusinessContacts(Collections.singletonList(bffBusinessContactMapper.toBffPartyBusinessContactDto(contact)));
        });
        if (c.getIncludesFacilities() != null && c.getIncludesFacilities()) {
            enrichFacilityDetails(dto, c.getSupplierId());
        }
        return dto;
    }

    private void enrichFacilityDetails(BffSupplierDto dto, String supplierId) {
        List<BffFacilityProjection> facilityProjections = bffFacilityRepository.findFacilitiesByOwnerPartyId(supplierId);
        if (dto != null && !facilityProjections.isEmpty()) {
            List<BffFacilityDto> facilityDtos = new ArrayList<>();
            facilityProjections.forEach(bffFacilityProjection -> {
                BffFacilityDto bffFacilityDto = bffFacilityMapper.toBffFacilityDto(bffFacilityProjection);
                bffFacilityContactMechRepository.findFacilityContactByFacilityId(bffFacilityDto.getFacilityId())
                        .ifPresent(contact -> bffFacilityDto.setBusinessContacts(
                                Collections.singletonList(bffBusinessContactMapper.toBffFacilityBusinessContactDto(contact))));
                facilityDtos.add(bffFacilityDto);
            });
            dto.setFacilities(facilityDtos);
        }
    }

    @Override
    @Transactional
    public String when(BffSupplierServiceCommands.CreateSupplier c) {
        String partyId = createSupplierParty(c.getSupplier(), c);
        //联系方式
        if (c.getSupplier().getBusinessContacts() != null && !c.getSupplier().getBusinessContacts().isEmpty()) {
            String contactMechId = bffBusinessContactService.createMiscContact(c.getSupplier().getBusinessContacts().get(0), c);
            createPartyContactMechAssociation(partyId, contactMechId, "-PE", c);
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
        mergePatchParty.setShortDescription(bffSupplier.getSupplierShortName());
        mergePatchParty.setExternalId(bffSupplier.getExternalId());
        mergePatchParty.setDescription(bffSupplier.getDescription());
        mergePatchParty.setWebSite(bffSupplier.getWebSite());
        mergePatchParty.setEmail(bffSupplier.getEmail());
        mergePatchParty.setTelephone(bffSupplier.getTelephone());
        if (bffSupplier.getPreferredCurrencyUomId() != null) {
            mergePatchParty.setPreferredCurrencyUomId(bffSupplier.getPreferredCurrencyUomId());
        }
        //以上为Supplier(Party本身）基础资料的修改
        if (bffSupplier.getInternalId() != null && !bffSupplier.getInternalId().isBlank()) {
            bffSupplier.setInternalId(bffSupplier.getInternalId().trim());
            String partyId = bffSupplierRepository.queryByPartyIdentificationTypeIdAndIdValue(PARTY_IDENTIFICATION_TYPE_INTERNAL_ID, bffSupplier.getInternalId());
            if (partyId != null && !partyId.equals(supplierId)) {
                throw new IllegalArgumentException(String.format("Duplicate vendor number:%s", c.getSupplierId()));
            }
        }
        updatePartyIdentification(partyState, mergePatchParty, PARTY_IDENTIFICATION_TYPE_INTERNAL_ID, bffSupplier.getInternalId());
        updatePartyIdentification(partyState, mergePatchParty, PARTY_IDENTIFICATION_TYPE_GGN, bffSupplier.getGgn());
        updatePartyIdentification(partyState, mergePatchParty, PARTY_IDENTIFICATION_TYPE_GLN, bffSupplier.getGln());
        updatePartyIdentification(partyState, mergePatchParty, PARTY_IDENTIFICATION_TYPE_GS1_COMPANY_PREFIX, bffSupplier.getGs1CompanyPrefix());
        updatePartyIdentification(partyState, mergePatchParty, PARTY_IDENTIFICATION_TYPE_TAX_ID, bffSupplier.getTaxId());


//        if (StringUtils.hasText(c.getSupplier().getStatusId())
//                && (PARTY_STATUS_ACTIVE.equals(c.getSupplier().getStatusId())
//                || PARTY_STATUS_INACTIVE.equals(c.getSupplier().getStatusId()))) {
//            mergePatchParty.setStatusId(c.getSupplier().getStatusId());
//        }
        partyApplicationService.when(mergePatchParty);

        //修改PartyRole


        PartyRoleId partyRoleId = new PartyRoleId();
        partyRoleId.setPartyId(bffSupplier.getSupplierId());
        partyRoleId.setRoleTypeId(PARTY_ROLE_SUPPLIER);
        PartyRoleState partyRole = partyRoleApplicationService.get(partyRoleId);
        if (partyRole != null) {//有就改
            AbstractPartyRoleCommand.SimpleMergePatchPartyRole simpleMergePatchPartyRole = new AbstractPartyRoleCommand.SimpleMergePatchPartyRole();
            simpleMergePatchPartyRole.setPartyRoleId(partyRoleId);
            simpleMergePatchPartyRole.setVersion(partyRole.getVersion());
            simpleMergePatchPartyRole.setRequesterId(c.getRequesterId());
            simpleMergePatchPartyRole.setBankAccountInformation(bffSupplier.getBankAccountInformation());
            simpleMergePatchPartyRole.setCertificationCodes(bffSupplier.getCertificationCodes());
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
        //前端传过来没有Id的设施列表，这部分会用来表示要添加的设施列表
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
        //客户端传过来的需要添加的设施列表（一部分是前端传过来没有Id的，另一部分是跟原有的列表比对但是在原有列表中不存在的）
        List<BffFacilityDto> needToAddedDtos = new ArrayList<>();
        needToAddedNoId.forEach(dto -> {
            dto.setOwnerPartyId(bffSupplier.getSupplierId());
            needToAddedDtos.add(dto);
        });
        needToAddedHasIds.forEach(facilityId -> {
            bffSupplier.getFacilities().forEach(facilityDto -> {
                if (facilityId.equals(facilityDto.getFacilityId())) {
                    facilityDto.setOwnerPartyId(bffSupplier.getSupplierId());
                    needToAddedDtos.add(facilityDto);
                }
            });
        });
        if (!needToAddedDtos.isEmpty()) {
            BffFacilityServiceCommands.BatchAddFacilities batchAddFacilities = new BffFacilityServiceCommands.BatchAddFacilities();
            batchAddFacilities.setFacilities(needToAddedDtos.toArray(new BffFacilityDto[0]));
            batchAddFacilities.setRequesterId(c.getRequesterId());
            bffFacilityApplicationService.when(batchAddFacilities);
        }
        //以上为批量添加处理

        //数据库里存在但是前端未传过来的设施列表做批量禁用处理
        BffFacilityServiceCommands.BatchDeactivateFacilities batchDeactivateFacilities = new BffFacilityServiceCommands.BatchDeactivateFacilities();
        batchDeactivateFacilities.setFacilityIds(needToDeletedIds.toArray(new String[0]));
        batchDeactivateFacilities.setRequesterId(c.getRequesterId());
        bffFacilityApplicationService.when(batchDeactivateFacilities);

        //数据库里面存在前端也传过来的做更新处理（判断依据：Id相同）
        if (!needToUpdateIds.isEmpty()) {
            BffFacilityServiceCommands.UpdateFacility updateFacility = new BffFacilityServiceCommands.UpdateFacility();
            needToUpdateIds.forEach(facilityId -> {
                bffSupplier.getFacilities().forEach(facility -> {
                    if (facilityId.equals(facility.getFacilityId())) {
                        updateFacility.setFacilityId(facilityId);
                        updateFacility.setFacility(facility);
                        updateFacility.setRequesterId(c.getRequesterId());
                        bffFacilityApplicationService.when(updateFacility);
                    }
                });
            });
        }
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
        createParty.setShortDescription(supplier.getSupplierShortName());
        createParty.setOrganizationName(supplier.getSupplierName());
        createParty.setExternalId(supplier.getExternalId());
        createParty.setDescription(supplier.getDescription());
        createParty.setWebSite(supplier.getWebSite());
        createParty.setEmail(supplier.getEmail());
        createParty.setTelephone(supplier.getTelephone());


        createParty.setPreferredCurrencyUomId(supplier.getPreferredCurrencyUomId() != null ? supplier.getPreferredCurrencyUomId() : DEFAULT_PREFERRED_CURRENCY_UOM_ID);
        if (supplier.getGgn() != null && !supplier.getGgn().isBlank()) {
            supplier.setGgn(supplier.getGgn().trim());
            addPartyIdentification(createParty, PARTY_IDENTIFICATION_TYPE_GGN, supplier.getGgn());
        }
        if (supplier.getGln() != null && !supplier.getGln().isBlank()) {
            supplier.setGln(supplier.getGln().trim());
            addPartyIdentification(createParty, PARTY_IDENTIFICATION_TYPE_GLN, supplier.getGln());
        }
        if (supplier.getInternalId() != null && !supplier.getInternalId().isBlank()) {
            supplier.setInternalId(supplier.getInternalId().trim());
            if (bffSupplierRepository.countByPartyIdentificationTypeIdAndIdValue(PARTY_IDENTIFICATION_TYPE_INTERNAL_ID, supplier.getInternalId()) > 0) {
                throw new IllegalArgumentException(String.format("Vendor Number:%s is already in use. Please try a different one.", supplier.getInternalId()));
            }
            addPartyIdentification(createParty, PARTY_IDENTIFICATION_TYPE_INTERNAL_ID, supplier.getInternalId());
        }
        if (supplier.getGs1CompanyPrefix() != null && !supplier.getGs1CompanyPrefix().isBlank()) {
            supplier.setGs1CompanyPrefix(supplier.getGs1CompanyPrefix().trim());
            addPartyIdentification(createParty, PARTY_IDENTIFICATION_TYPE_GS1_COMPANY_PREFIX, supplier.getGs1CompanyPrefix());
        }
        if (supplier.getTaxId() != null && !supplier.getTaxId().isBlank()) {
            supplier.setTaxId(supplier.getTaxId().trim());
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
        //createPartyRole.setSupplierShortName(supplier.getSupplierShortName());
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
                String contactMechId = bffBusinessContactService.createMiscContact(supplierDto.getBusinessContacts().get(0), c);
                createPartyContactMechAssociation(partyId, contactMechId, "-PE", c);
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
        var optional = bffPartyContactMechRepository.findPartyContactByPartyId(partyId);
        if (optional.isEmpty()) {
            String contactMechId = bffBusinessContactService.createMiscContact(bizContact, c);
            createPartyContactMechAssociation(partyId, contactMechId, "-PE", c);
        } else {
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
                mergePatchContactMech.setTelecomAreaCode(bizContact.getTelecomAreaCode());
                mergePatchContactMech.setTelecomCountryCode(bizContact.getTelecomCountryCode());
                mergePatchContactMech.setTelecomContactNumber(bizContact.getPhoneNumber());

                mergePatchContactMech.setEmail(bizContact.getEmail());
                mergePatchContactMech.setAskForRole(bizContact.getContactRole());
                mergePatchContactMech.setAskForName(bizContact.getBusinessName());
                mergePatchContactMech.setPhysicalLocationAddress(bizContact.getPhysicalLocationAddress());

                contactMechApplicationService.when(mergePatchContactMech);

                updateOrCreatePartyContactMechAssociation(partyId, contactMechId, ContactMechTypeId.MISC_CONTACT_MECH, "-PE", c);
            }
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
        if (pcmIdPrj.isEmpty()) {
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
