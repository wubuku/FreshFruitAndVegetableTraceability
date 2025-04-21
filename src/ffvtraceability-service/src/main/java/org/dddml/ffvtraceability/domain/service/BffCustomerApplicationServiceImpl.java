package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffBusinessContactDto;
import org.dddml.ffvtraceability.domain.BffCustomerDto;
import org.dddml.ffvtraceability.domain.BffFacilityDto;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.domain.contactmech.*;
import org.dddml.ffvtraceability.domain.mapper.BffBusinessContactMapper;
import org.dddml.ffvtraceability.domain.mapper.BffCustomerMapper;
import org.dddml.ffvtraceability.domain.mapper.BffFacilityMapper;
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
public class BffCustomerApplicationServiceImpl implements BffCustomerApplicationService {

    private static final String ERROR_CUSTOMER_NOT_FOUND = "Customer not found: %s";
    private static final String ERROR_CUSTOMER_ALREADY_EXISTS = "Customer already exists: %s";
    private static final String ERROR_DUPLICATE_CUSTOMER = "Duplicate customer: %s";

    @Autowired
    private PartyApplicationService partyApplicationService;
    @Autowired
    private PartyRoleApplicationService partyRoleApplicationService;
    @Autowired
    private BffCustomerMapper bffCustomerMapper;
    @Autowired
    private BffGeoRepository bffGeoRepository;
    @Autowired
    private BffBusinessContactMapper bffBusinessContactMapper;
    @Autowired
    private BffCustomerRepository bffCustomerRepository;
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
    public Page<BffCustomerDto> when(BffCustomerServiceCommands.GetCustomers c) {
        String statusId = null;
        if (c.getActive() != null) {
            statusId = IndicatorUtils.toBoolean(c.getActive()) ? PARTY_STATUS_ACTIVE : PARTY_STATUS_INACTIVE;
        }
        return PageUtils.toPage(
                bffCustomerRepository.findAllCustomers(PageRequest.of(c.getPage(), c.getSize()), statusId),
                bffCustomerMapper::toBffCustomerDto);
    }

    @Override
    @Transactional(readOnly = true)
    public BffCustomerDto when(BffCustomerServiceCommands.GetCustomer c) {
        Optional<BffCustomerProjection> projection = bffCustomerRepository.findCustomerById(c.getCustomerId());
        if (projection.isEmpty()) {
            return null;
        }
        BffCustomerDto dto = bffCustomerMapper.toBffCustomerDto(projection.get());
        bffPartyContactMechRepository.findPartyContactByPartyId(c.getCustomerId()).ifPresent(contact -> {
            dto.setBusinessContacts(
                    Collections.singletonList(bffBusinessContactMapper.toBffPartyBusinessContactDto(contact)));
        });
        if (c.getIncludesFacilities() != null && c.getIncludesFacilities()) {
            enrichFacilityDetails(dto, c.getCustomerId());
        }
        return dto;
    }

    private void enrichFacilityDetails(BffCustomerDto dto, String customerId) {
        List<BffFacilityProjection> facilityProjections = bffFacilityRepository
                .findFacilitiesByOwnerPartyId(customerId);
        if (dto != null && !facilityProjections.isEmpty()) {
            List<BffFacilityDto> facilityDtos = new ArrayList<>();
            facilityProjections.forEach(bffFacilityProjection -> {
                BffFacilityDto bffFacilityDto = bffFacilityMapper.toBffFacilityDto(bffFacilityProjection);
                bffFacilityContactMechRepository.findFacilityContactByFacilityId(bffFacilityDto.getFacilityId())
                        .ifPresent(contact -> bffFacilityDto.setBusinessContacts(
                                Collections.singletonList(
                                        bffBusinessContactMapper.toBffFacilityBusinessContactDto(contact))));
                facilityDtos.add(bffFacilityDto);
            });
            dto.setFacilities(facilityDtos);
        }
    }

    @Override
    @Transactional
    public String when(BffCustomerServiceCommands.CreateCustomer c) {
        String partyId = createCustomerParty(c.getCustomer(), c);
        // 联系方式
        if (c.getCustomer().getBusinessContacts() != null && !c.getCustomer().getBusinessContacts().isEmpty()) {
            String contactMechId = bffBusinessContactService
                    .createMiscContact(c.getCustomer().getBusinessContacts().get(0), c);
            createPartyContactMechAssociation(partyId, contactMechId, "-PE", c);
        }
        // 设施
        if (c.getCustomer().getFacilities() != null && !c.getCustomer().getFacilities().isEmpty()) {
            BffFacilityServiceCommands.BatchAddFacilities batchAddFacilities = new BffFacilityServiceCommands.BatchAddFacilities();
            c.getCustomer().getFacilities().forEach(bffFacilityDto -> {
                bffFacilityDto.setOwnerPartyId(partyId);// 将设施的ownerPartyId改为当前生成的Party的Id
            });
            batchAddFacilities.setFacilities(c.getCustomer().getFacilities().toArray(new BffFacilityDto[0]));
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
            PartyIdentificationCommand.CreatePartyIdentification createPartyIdentification = createParty
                    .newCreatePartyIdentification();
            createPartyIdentification.setPartyIdentificationTypeId(identificationTypeId);
            createPartyIdentification.setIdValue(idValue);
            createParty.getCreatePartyIdentificationCommands().add(createPartyIdentification);
        }
    }

    @Override
    @Transactional
    public void when(BffCustomerServiceCommands.UpdateCustomer c) {

        String customerId = c.getCustomerId();
        PartyState partyState = partyApplicationService.get(customerId);
        if (partyState == null) {
            throw new IllegalArgumentException(String.format(ERROR_CUSTOMER_NOT_FOUND, c.getCustomerId()));
        }
        BffCustomerDto bffCustomer = c.getCustomer();
        bffCustomer.setCustomerId(customerId);
        AbstractPartyCommand.SimpleMergePatchOrganization mergePatchParty = new AbstractPartyCommand.SimpleMergePatchOrganization();
        mergePatchParty.setPartyId(customerId);
        mergePatchParty.setOrganizationName(bffCustomer.getCustomerName());
        mergePatchParty.setVersion(partyState.getVersion());// 乐观锁
        mergePatchParty.setRequesterId(c.getRequesterId());
        mergePatchParty.setShortDescription(bffCustomer.getCustomerShortName());
        mergePatchParty.setExternalId(bffCustomer.getExternalId());
        mergePatchParty.setDescription(bffCustomer.getDescription());
        mergePatchParty.setWebSite(bffCustomer.getWebSite());
        mergePatchParty.setEmail(bffCustomer.getEmail());
        mergePatchParty.setTelephone(bffCustomer.getTelephone());
        if (bffCustomer.getPreferredCurrencyUomId() != null) {
            mergePatchParty.setPreferredCurrencyUomId(bffCustomer.getPreferredCurrencyUomId());
        }
        // 以上为Customer(Party本身）基础资料的修改
        if (bffCustomer.getInternalId() != null && !bffCustomer.getInternalId().isBlank()) {
            bffCustomer.setInternalId(bffCustomer.getInternalId().trim());
            String partyId = bffCustomerRepository.queryByPartyIdentificationTypeIdAndIdValue(
                    PARTY_IDENTIFICATION_TYPE_INTERNAL_ID, bffCustomer.getInternalId());
            if (partyId != null && !partyId.equals(customerId)) {
                throw new IllegalArgumentException(String.format(
                        "Customer Number:%s is already in use. Please try a different one.",
                        bffCustomer.getInternalId()));
            }
        }
        updatePartyIdentification(partyState, mergePatchParty, PARTY_IDENTIFICATION_TYPE_INTERNAL_ID,
                bffCustomer.getInternalId());
        updatePartyIdentification(partyState, mergePatchParty, PARTY_IDENTIFICATION_TYPE_GGN, bffCustomer.getGgn());
        updatePartyIdentification(partyState, mergePatchParty, PARTY_IDENTIFICATION_TYPE_GLN, bffCustomer.getGln());
        updatePartyIdentification(partyState, mergePatchParty, PARTY_IDENTIFICATION_TYPE_GS1_COMPANY_PREFIX,
                bffCustomer.getGs1CompanyPrefix());
        updatePartyIdentification(partyState, mergePatchParty, PARTY_IDENTIFICATION_TYPE_TAX_ID,
                bffCustomer.getTaxId());

        // if (StringUtils.hasText(c.getCustomer().getStatusId())
        // && (PARTY_STATUS_ACTIVE.equals(c.getCustomer().getStatusId())
        // || PARTY_STATUS_INACTIVE.equals(c.getCustomer().getStatusId()))) {
        // mergePatchParty.setStatusId(c.getCustomer().getStatusId());
        // }
        partyApplicationService.when(mergePatchParty);

        // 修改PartyRole

        PartyRoleId partyRoleId = new PartyRoleId();
        partyRoleId.setPartyId(bffCustomer.getCustomerId());
        partyRoleId.setRoleTypeId(PARTY_ROLE_CUSTOMER);
        PartyRoleState partyRole = partyRoleApplicationService.get(partyRoleId);
        if (partyRole != null) {// 有就改
            AbstractPartyRoleCommand.SimpleMergePatchPartyRole simpleMergePatchPartyRole = new AbstractPartyRoleCommand.SimpleMergePatchPartyRole();
            simpleMergePatchPartyRole.setPartyRoleId(partyRoleId);
            simpleMergePatchPartyRole.setVersion(partyRole.getVersion());
            simpleMergePatchPartyRole.setRequesterId(c.getRequesterId());
            simpleMergePatchPartyRole.setBankAccountInformation(bffCustomer.getBankAccountInformation());
            simpleMergePatchPartyRole.setCertificationCodes(bffCustomer.getCertificationCodes());
            simpleMergePatchPartyRole.setTpaNumber(bffCustomer.getTpaNumber());
            simpleMergePatchPartyRole.setCustomerProductTypeDescription(bffCustomer.getCustomerProductTypeDescription());
            simpleMergePatchPartyRole.setCustomerTypeEnumId(bffCustomer.getCustomerTypeEnumId());
            simpleMergePatchPartyRole.setPaymentMethodEnumId(bffCustomer.getPaymentMethodEnumId());
            simpleMergePatchPartyRole.setCreditRating(bffCustomer.getCreditRating());
            simpleMergePatchPartyRole.setShippingAddress(bffCustomer.getShippingAddress());
            partyRoleApplicationService.when(simpleMergePatchPartyRole);
        } else {// 没有就添加
            AbstractPartyRoleCommand.SimpleCreatePartyRole createPartyRole = new AbstractPartyRoleCommand.SimpleCreatePartyRole();
            partyRoleId.setRoleTypeId(PARTY_ROLE_CUSTOMER);
            createPartyRole.setPartyRoleId(partyRoleId);
            createPartyRole.setCommandId(c.getCommandId() + "-SPP");
            createPartyRole.setRequesterId(c.getRequesterId());
            createPartyRole.setBankAccountInformation(bffCustomer.getBankAccountInformation());
            createPartyRole.setCertificationCodes(bffCustomer.getCertificationCodes());
            createPartyRole.setTpaNumber(bffCustomer.getTpaNumber());
            createPartyRole.setCustomerProductTypeDescription(bffCustomer.getCustomerProductTypeDescription());
            createPartyRole.setCustomerTypeEnumId(bffCustomer.getCustomerTypeEnumId());
            createPartyRole.setPaymentMethodEnumId(bffCustomer.getPaymentMethodEnumId());
            createPartyRole.setCreditRating(bffCustomer.getCreditRating());
            createPartyRole.setShippingAddress(bffCustomer.getShippingAddress());
            partyRoleApplicationService.when(createPartyRole);
        }

        // 修改联系方式
        if (c.getCustomer().getBusinessContacts() != null && !c.getCustomer().getBusinessContacts().isEmpty()) {
            updateOrCreatePartyBusinessContact(customerId, c.getCustomer().getBusinessContacts().get(0), c);
        }

        // 修改其关联设施列表
        List<BffFacilityProjection> facilityProjections = bffFacilityRepository
                .findFacilitiesByOwnerPartyId(customerId);
        List<String> originalFacilityIds = new ArrayList<>();
        facilityProjections
                .forEach(bffFacilityProjection -> originalFacilityIds.add(bffFacilityProjection.getFacilityId()));
        if (bffCustomer.getFacilities() == null) {
            bffCustomer.setFacilities(new ArrayList<>());
        }//不能让下面那句报错
        List<String> newFacilityIds = bffCustomer.getFacilities().stream().map(BffFacilityDto::getFacilityId)
                .filter(Objects::nonNull).collect(Collectors.toList());
        // 不管前端传的Facility的OwnerId是什么，都要改成当前供应商的Id
        bffCustomer.getFacilities().forEach(bffFacilityDto -> {
            bffFacilityDto.setOwnerPartyId(customerId);
        });

        // 前端传过来没有Id的设施列表，这部分会用来表示要添加的设施列表
        List<BffFacilityDto> needToAddedNoId = bffCustomer.getFacilities().stream()
                .filter(bffFacilityDto -> bffFacilityDto.getFacilityId() == null).collect(Collectors.toList());
        List<String> needToUpdateIds = new ArrayList<>();
        List<String> needToAddedHasIds = new ArrayList<>();
        List<String> needToDeletedIds = new ArrayList<>();
        newFacilityIds.forEach(newId -> {
            if (originalFacilityIds.contains(newId)) {
                needToUpdateIds.add(newId);// 原来有，现在也有，属于更新的
            } else {
                needToAddedHasIds.add(newId);// 有Id,但是并不在原有的列表中，那么也属于应该添加的
            }
        });
        originalFacilityIds.forEach(oldId -> {
            if (!newFacilityIds.contains(oldId)) {
                needToDeletedIds.add(oldId);
            }
        });
        // 客户端传过来的需要添加的设施列表（一部分是前端传过来没有Id的，另一部分是跟原有的列表比对但是在原有列表中不存在的）
        List<BffFacilityDto> needToAddedDtos = new ArrayList<>();
        needToAddedNoId.forEach(dto -> {
            dto.setOwnerPartyId(bffCustomer.getCustomerId());
            needToAddedDtos.add(dto);
        });
        needToAddedHasIds.forEach(facilityId -> {
            bffCustomer.getFacilities().forEach(facilityDto -> {
                if (facilityId.equals(facilityDto.getFacilityId())) {
                    facilityDto.setOwnerPartyId(bffCustomer.getCustomerId());
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
        // 以上为批量添加处理

        // 数据库里存在但是前端未传过来的设施列表做批量禁用处理
        BffFacilityServiceCommands.BatchDeactivateFacilities batchDeactivateFacilities = new BffFacilityServiceCommands.BatchDeactivateFacilities();
        batchDeactivateFacilities.setFacilityIds(needToDeletedIds.toArray(new String[0]));
        batchDeactivateFacilities.setRequesterId(c.getRequesterId());
        bffFacilityApplicationService.when(batchDeactivateFacilities);

        // 数据库里面存在前端也传过来的做更新处理（判断依据：Id相同）
        if (!needToUpdateIds.isEmpty()) {
            BffFacilityServiceCommands.UpdateFacility updateFacility = new BffFacilityServiceCommands.UpdateFacility();
            needToUpdateIds.forEach(facilityId -> {
                bffCustomer.getFacilities().forEach(facility -> {
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
                    PartyIdentificationCommand.MergePatchPartyIdentification m = mergePatchParty
                            .newMergePatchPartyIdentification();
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
                PartyIdentificationCommand.CreatePartyIdentification createPartyIdentification = mergePatchParty
                        .newCreatePartyIdentification();
                createPartyIdentification.setPartyIdentificationTypeId(identificationTypeId);
                createPartyIdentification.setIdValue(newValue);
                mergePatchParty.getPartyIdentificationCommands().add(createPartyIdentification);
            }
        }
    }

    @Override
    @Transactional
    public void when(BffCustomerServiceCommands.ActivateCustomer c) {
        String customerId = c.getCustomerId();
        PartyState partyState = partyApplicationService.get(customerId);
        if (partyState == null) {
            throw new IllegalArgumentException(String.format(ERROR_CUSTOMER_NOT_FOUND, c.getCustomerId()));
        }
        AbstractPartyCommand.SimpleMergePatchOrganization mergePatchParty = new AbstractPartyCommand.SimpleMergePatchOrganization();
        mergePatchParty.setPartyId(customerId);
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
    public void when(BffCustomerServiceCommands.UpdateBusinessContact c) {
        String partyId = c.getCustomerId();
        PartyState p = partyApplicationService.get(partyId);
        if (p == null) {
            throw new IllegalArgumentException(String.format(ERROR_CUSTOMER_NOT_FOUND, c.getCustomerId()));
        }
        BffBusinessContactDto bizContact = c.getBusinessContact();
        updateOrCreatePartyBusinessContact(partyId, bizContact, c);
    }

    private String createCustomerParty(BffCustomerDto customer, Command c) {
        AbstractPartyCommand.SimpleCreateOrganization createParty = new AbstractPartyCommand.SimpleCreateOrganization();
        if (customer.getCustomerId() != null) {
            var partyState = partyApplicationService.get(customer.getCustomerId());
            if (partyState != null) {
                throw new IllegalArgumentException(
                        String.format(ERROR_CUSTOMER_ALREADY_EXISTS, customer.getCustomerId()));
            }
            createParty.setPartyId(customer.getCustomerId());
        } else {
            createParty.setPartyId(IdUtils.randomId());
        }
        createParty.setShortDescription(customer.getCustomerShortName());
        createParty.setOrganizationName(customer.getCustomerName());
        createParty.setExternalId(customer.getExternalId());
        createParty.setDescription(customer.getDescription());
        createParty.setWebSite(customer.getWebSite());
        createParty.setEmail(customer.getEmail());
        createParty.setTelephone(customer.getTelephone());

        createParty.setPreferredCurrencyUomId(
                customer.getPreferredCurrencyUomId() != null ? customer.getPreferredCurrencyUomId()
                        : DEFAULT_PREFERRED_CURRENCY_UOM_ID);
        if (customer.getGgn() != null && !customer.getGgn().isBlank()) {
            customer.setGgn(customer.getGgn().trim());
            addPartyIdentification(createParty, PARTY_IDENTIFICATION_TYPE_GGN, customer.getGgn());
        }
        if (customer.getGln() != null && !customer.getGln().isBlank()) {
            customer.setGln(customer.getGln().trim());
            addPartyIdentification(createParty, PARTY_IDENTIFICATION_TYPE_GLN, customer.getGln());
        }
        if (customer.getInternalId() != null && !customer.getInternalId().isBlank()) {
            customer.setInternalId(customer.getInternalId().trim());
            if (bffCustomerRepository.countByPartyIdentificationTypeIdAndIdValue(PARTY_IDENTIFICATION_TYPE_INTERNAL_ID,
                    customer.getInternalId()) > 0) {
                throw new IllegalArgumentException(String.format(
                        "Customer Number:%s is already in use. Please try a different one.", customer.getInternalId()));
            }
            addPartyIdentification(createParty, PARTY_IDENTIFICATION_TYPE_INTERNAL_ID, customer.getInternalId());
        }
        if (customer.getGs1CompanyPrefix() != null && !customer.getGs1CompanyPrefix().isBlank()) {
            customer.setGs1CompanyPrefix(customer.getGs1CompanyPrefix().trim());
            addPartyIdentification(createParty, PARTY_IDENTIFICATION_TYPE_GS1_COMPANY_PREFIX,
                    customer.getGs1CompanyPrefix());
        }
        if (customer.getTaxId() != null && !customer.getTaxId().isBlank()) {
            customer.setTaxId(customer.getTaxId().trim());
            addPartyIdentification(createParty, PARTY_IDENTIFICATION_TYPE_TAX_ID, customer.getTaxId());
        }
        createParty.setStatusId(PARTY_STATUS_ACTIVE); // default status
        createParty.setCommandId(c.getCommandId() != null ? c.getCommandId() : createParty.getPartyId());
        createParty.setRequesterId(c.getRequesterId());
        partyApplicationService.when(createParty);

        AbstractPartyRoleCommand.SimpleCreatePartyRole createPartyRole = new AbstractPartyRoleCommand.SimpleCreatePartyRole();
        PartyRoleId partyRoleId = new PartyRoleId();
        partyRoleId.setPartyId(createParty.getPartyId());
        partyRoleId.setRoleTypeId(PARTY_ROLE_CUSTOMER);
        createPartyRole.setPartyRoleId(partyRoleId);
        createPartyRole.setCommandId(createParty.getCommandId() + "-SPP");
        createPartyRole.setRequesterId(c.getRequesterId());
        createPartyRole.setBankAccountInformation(customer.getBankAccountInformation());
        createPartyRole.setCertificationCodes(customer.getCertificationCodes());
        createPartyRole.setTpaNumber(customer.getTpaNumber());
        createPartyRole.setCustomerProductTypeDescription(customer.getCustomerProductTypeDescription());
        createPartyRole.setCustomerTypeEnumId(customer.getCustomerTypeEnumId());
        createPartyRole.setPaymentMethodEnumId(customer.getPaymentMethodEnumId());
        createPartyRole.setCreditRating(customer.getCreditRating());
        createPartyRole.setShippingAddress(customer.getShippingAddress());
        partyRoleApplicationService.when(createPartyRole);

        return createParty.getPartyId();
    }

    @Override
    @Transactional
    public void when(BffCustomerServiceCommands.BatchAddCustomers c) {
        // 首先查看提供了customer Id的记录，看是否有重复。
        List<String> customerIds = new ArrayList<>();
        for (var customer : c.getCustomers()) {
            if (customer.getCustomerId() != null && !customer.getCustomerId().isEmpty()) {
                if (customerIds.contains(customer.getCustomerId())) {
                    throw new IllegalArgumentException(
                            String.format(ERROR_DUPLICATE_CUSTOMER, customer.getCustomerId()));
                }
                customerIds.add(customer.getCustomerId());
            }
        }
        // 循环以添加新的供应商
        for (BffCustomerDto customerDto : c.getCustomers()) {
            String partyId = createCustomerParty(customerDto, c);
            if (customerDto.getBusinessContacts() != null && !customerDto.getBusinessContacts().isEmpty()) {
                String contactMechId = bffBusinessContactService
                        .createMiscContact(customerDto.getBusinessContacts().get(0), c);
                createPartyContactMechAssociation(partyId, contactMechId, "-PE", c);
            }
            // 设施
            if (customerDto.getFacilities() != null && !customerDto.getFacilities().isEmpty()) {
                BffFacilityServiceCommands.BatchAddFacilities batchAddFacilities = new BffFacilityServiceCommands.BatchAddFacilities();
                customerDto.getFacilities().forEach(bffFacilityDto -> {
                    bffFacilityDto.setOwnerPartyId(partyId);// 将设施的ownerPartyId改为当前生成的Party的Id
                });
                batchAddFacilities.setFacilities(customerDto.getFacilities().toArray(new BffFacilityDto[0]));
                batchAddFacilities.setRequesterId(c.getRequesterId());
                bffFacilityApplicationService.when(batchAddFacilities);
            }
        }
    }

    @Override
    @Transactional
    public void when(BffCustomerServiceCommands.BatchActivateCustomers c) {
        Arrays.stream(c.getCustomerIds()).forEach(customerId -> {
            PartyState partyState = partyApplicationService.get(customerId);
            if (partyState == null) {
                throw new IllegalArgumentException(String.format(ERROR_CUSTOMER_NOT_FOUND, customerId));
            }
            if (!partyState.getStatusId().equals(PARTY_STATUS_ACTIVE)) {
                AbstractPartyCommand.SimpleMergePatchOrganization mergePatchParty = new AbstractPartyCommand.SimpleMergePatchOrganization();
                mergePatchParty.setPartyId(customerId);
                mergePatchParty.setVersion(partyState.getVersion());
                mergePatchParty.setStatusId(PARTY_STATUS_ACTIVE);
                mergePatchParty.setRequesterId(c.getRequesterId());
                mergePatchParty
                        .setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
                partyApplicationService.when(mergePatchParty);
            }
        });
    }

    @Override
    @Transactional
    public void when(BffCustomerServiceCommands.BatchDeactivateCustomers c) {
        Arrays.stream(c.getCustomerIds()).forEach(customerId -> {
            PartyState partyState = partyApplicationService.get(customerId);
            if (partyState == null) {
                throw new IllegalArgumentException(String.format(ERROR_CUSTOMER_NOT_FOUND, customerId));
            }
            if (!partyState.getStatusId().equals(PARTY_STATUS_INACTIVE)) {
                AbstractPartyCommand.SimpleMergePatchOrganization mergePatchParty = new AbstractPartyCommand.SimpleMergePatchOrganization();
                mergePatchParty.setPartyId(customerId);
                mergePatchParty.setVersion(partyState.getVersion());
                mergePatchParty.setStatusId(PARTY_STATUS_INACTIVE);
                mergePatchParty.setRequesterId(c.getRequesterId());
                mergePatchParty
                        .setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
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
                ContactMechCommand.MergePatchContactMech mergePatchContactMech = new AbstractContactMechCommand.SimpleMergePatchMiscContactMech();
                mergePatchContactMech.setVersion(state.get().getVersion());
                //mergePatchContactMech.setToName(bizContact.getBusinessName());
                mergePatchContactMech.setContactMechId(contactMechId);
                if (bizContact.getStateProvinceGeoId() != null) {
                    Optional<BffGeoRepository.StateProvinceProjection> stateProvince = bffGeoRepository
                            .findStateOrProvinceById(bizContact.getStateProvinceGeoId());
                    if (stateProvince.isEmpty()) {
                        throw new IllegalArgumentException(
                                String.format("State or province not found: %s", bizContact.getStateProvinceGeoId()));
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

                updateOrCreatePartyContactMechAssociation(partyId, contactMechId, ContactMechTypeId.MISC_CONTACT_MECH,
                        "-PE", c);
            }
        }
    }

    private void updateOrCreatePartyContactMechAssociation(
            String partyId,
            String contactMechId,
            String contactMechTypeId,
            String commandIdSuffix,
            Command c) {
        Optional<BffPartyContactMechRepository.PartyContactMechIdProjection> pcmIdPrj = bffPartyContactMechRepository
                .findPartyCurrentContactMechByContactMechType(partyId, contactMechTypeId);
        if (pcmIdPrj.isEmpty()) {
            createPartyContactMechAssociation(partyId, contactMechId, commandIdSuffix, c);
        } else {
            OffsetDateTime fromDate = OffsetDateTime.ofInstant(pcmIdPrj.get().getFromDate(), ZoneOffset.UTC);
            PartyContactMechState pcm = partyContactMechApplicationService.get(new PartyContactMechId(
                    partyId, contactMechId, fromDate));
            if (pcm == null) {
                // Should not happen?
                // throw new
                // IllegalArgumentException(String.format(ERROR_PARTY_CONTACT_MECH_NOT_FOUND,
                // partyId, contactMechId, fromDate));
                createPartyContactMechAssociation(partyId, contactMechId, commandIdSuffix, c);
            } else {
                AbstractPartyContactMechCommand.SimpleMergePatchPartyContactMech mergePatchPartyContactMech = new AbstractPartyContactMechCommand.SimpleMergePatchPartyContactMech();
                mergePatchPartyContactMech.setPartyContactMechId(pcm.getPartyContactMechId());
                mergePatchPartyContactMech.setVersion(pcm.getVersion());
                mergePatchPartyContactMech.setThruDate(OffsetDateTime.now().plusYears(100));
                mergePatchPartyContactMech.setCommandId(
                        c.getCommandId() != null ? c.getCommandId() + commandIdSuffix : UUID.randomUUID().toString());
                mergePatchPartyContactMech.setRequesterId(c.getRequesterId());
                partyContactMechApplicationService.when(mergePatchPartyContactMech);
            }
        }
    }

    private void createPartyContactMechAssociation(
            String partyId,
            String contactMechId,
            String commandIdSuffix,
            Command c) {
        AbstractPartyContactMechCommand.SimpleCreatePartyContactMech createPartyContactMechBase = buildPartyContactMechCreationCommand(
                partyId, contactMechId,
                OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC) // to UTC?
        );
        createPartyContactMechBase.setCommandId(
                c.getCommandId() != null ? c.getCommandId() + commandIdSuffix : UUID.randomUUID().toString());
        createPartyContactMechBase.setRequesterId(c.getRequesterId());
        partyContactMechApplicationService.when(createPartyContactMechBase);
    }

    private AbstractPartyContactMechCommand.SimpleCreatePartyContactMech buildPartyContactMechCreationCommand(
            String partyId,
            String contactMechId,
            OffsetDateTime fromDate) {
        AbstractPartyContactMechCommand.SimpleCreatePartyContactMech createPartyContactMech = new AbstractPartyContactMechCommand.SimpleCreatePartyContactMech();
        createPartyContactMech.setPartyContactMechId(new PartyContactMechId(partyId, contactMechId, fromDate));
        createPartyContactMech.setThruDate(OffsetDateTime.now().plusYears(100));
        return createPartyContactMech;
    }

}
