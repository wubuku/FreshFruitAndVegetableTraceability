package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffBusinessContactDto;
import org.dddml.ffvtraceability.domain.BffSupplierDto;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.domain.contactmech.AbstractContactMechCommand;
import org.dddml.ffvtraceability.domain.contactmech.ContactMechApplicationService;
import org.dddml.ffvtraceability.domain.mapper.BffSupplierMapper;
import org.dddml.ffvtraceability.domain.party.*;
import org.dddml.ffvtraceability.domain.partycontactmech.*;
import org.dddml.ffvtraceability.domain.partyrole.AbstractPartyRoleCommand;
import org.dddml.ffvtraceability.domain.partyrole.PartyRoleApplicationService;
import org.dddml.ffvtraceability.domain.partyrole.PartyRoleId;
import org.dddml.ffvtraceability.domain.repository.BffBusinessContactRepository;
import org.dddml.ffvtraceability.domain.repository.BffGeoRepository;
import org.dddml.ffvtraceability.domain.repository.BffSupplierRepository;
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
import static org.dddml.ffvtraceability.domain.constants.BffSupplierConstants.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BffSupplierApplicationServiceImpl implements BffSupplierApplicationService {

    @Autowired
    private PartyApplicationService partyApplicationService;

    @Autowired
    private PartyRoleApplicationService partyRoleApplicationService;

    @Autowired
    private BffSupplierMapper bffSupplierMapper;

    @Autowired
    private BffSupplierRepository bffSupplierRepository;

    @Autowired
    private ContactMechApplicationService contactMechApplicationService;

    @Autowired
    private PartyContactMechApplicationService partyContactMechApplicationService;

    @Autowired
    private BffGeoRepository bffGeoRepository;

    @Autowired
    private BffBusinessContactRepository bffBusinessContactRepository;

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
        PartyState partyState = partyApplicationService.get(c.getSupplierId());
        if (partyState == null) {
            return null;
        }
        BffSupplierDto dto = bffSupplierMapper.toBffSupplierDto(partyState);
        partyState.getPartyIdentifications().stream().forEach(x -> {
            if (x.getPartyIdentificationTypeId().equals(PARTY_IDENTIFICATION_TYPE_GLN)) {
                dto.setGln(x.getIdValue());
            } else if (x.getPartyIdentificationTypeId().equals(PARTY_IDENTIFICATION_TYPE_GGN)) {
                dto.setGgn(x.getIdValue());
            }
        });
        bffBusinessContactRepository.findPartyCurrentPostalAddressByPartyId(c.getSupplierId()).ifPresent(x -> {
            BffBusinessContactDto bc = new BffBusinessContactDto();
            bc.setBusinessName(x.getToName());
            bc.setPhysicalLocationAddress(x.getAddress1());
            bc.setCity(x.getCity());
            bc.setState(x.getStateProvinceGeoId());// Is this OK?
            bc.setZipCode(x.getPostalCode());
            dto.setBusinessContacts(List.of(bc));
        });
        bffBusinessContactRepository.findPartyCurrentTelecomNumberByPartyId(c.getSupplierId()).ifPresent(x -> {
            if (dto.getBusinessContacts() == null) {
                dto.setBusinessContacts(List.of(new BffBusinessContactDto()));
            }
            dto.getBusinessContacts().get(0).setPhoneNumber(
                    TelecomNumberUtil.format(x.getCountryCode(), x.getAreaCode(), x.getContactNumber())
            );
        });
        return dto;
    }

    @Override
    @Transactional
    public String when(BffSupplierServiceCommands.CreateSupplier c) {
        AbstractPartyCommand.SimpleCreateOrganization createParty = new AbstractPartyCommand.SimpleCreateOrganization();
        createParty.setPartyId(c.getSupplier().getSupplierId() != null ? c.getSupplier().getSupplierId() : IdUtils.randomId());
        createParty.setOrganizationName(c.getSupplier().getSupplierName());
        createParty.setExternalId(c.getSupplier().getExternalId());
        createParty.setDescription(c.getSupplier().getDescription());
        createParty.setPreferredCurrencyUomId(c.getSupplier().getPreferredCurrencyUomId() != null ? c.getSupplier().getPreferredCurrencyUomId() : DEFAULT_PREFERRED_CURRENCY_UOM_ID);
        if (c.getSupplier().getGgn() != null) {
            addPartyIdentification(createParty, PARTY_IDENTIFICATION_TYPE_GGN, c.getSupplier().getGgn());
        }
        if (c.getSupplier().getGln() != null) {
            addPartyIdentification(createParty, PARTY_IDENTIFICATION_TYPE_GLN, c.getSupplier().getGln());
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
        partyRoleApplicationService.when(createPartyRole);

        if (c.getSupplier().getBusinessContacts() != null && !c.getSupplier().getBusinessContacts().isEmpty()) {
            createPartyBusinessContact(createParty.getPartyId(), c.getSupplier().getBusinessContacts().get(0), c);
        }
        return createParty.getPartyId();
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
            throw new IllegalArgumentException("Supplier not found:" + c.getSupplierId());
        }
        BffSupplierDto bffSupplier = c.getSupplier();
        AbstractPartyCommand.SimpleMergePatchOrganization mergePatchParty = new AbstractPartyCommand.SimpleMergePatchOrganization();
        mergePatchParty.setPartyId(supplierId);
        mergePatchParty.setOrganizationName(c.getSupplier().getSupplierName());
        mergePatchParty.setVersion(partyState.getVersion());//乐观锁
        mergePatchParty.setRequesterId(c.getRequesterId());
        mergePatchParty.setExternalId(bffSupplier.getExternalId());
        mergePatchParty.setDescription(bffSupplier.getDescription());
        updatePartyIdentification(partyState, mergePatchParty, PARTY_IDENTIFICATION_TYPE_GGN, bffSupplier.getGgn());
        updatePartyIdentification(partyState, mergePatchParty, PARTY_IDENTIFICATION_TYPE_GLN, bffSupplier.getGln());
        if (StringUtils.hasText(c.getSupplier().getStatusId()) && (
                PARTY_STATUS_ACTIVE.equals(c.getSupplier().getStatusId())
                        || PARTY_STATUS_INACTIVE.equals(c.getSupplier().getStatusId())
        )) {
            mergePatchParty.setStatusId(c.getSupplier().getStatusId());
        }
        partyApplicationService.when(mergePatchParty);

        if (c.getSupplier().getBusinessContacts() != null && !c.getSupplier().getBusinessContacts().isEmpty()) {
            updatePartyBusinessContact(supplierId, c.getSupplier().getBusinessContacts().get(0), c);
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
            throw new IllegalArgumentException("Supplier not found:" + c.getSupplierId());
        }
        AbstractPartyCommand.SimpleMergePatchParty mergePatchParty = new AbstractPartyCommand.SimpleMergePatchParty();
        mergePatchParty.setPartyId(supplierId);
        mergePatchParty.setVersion(partyState.getVersion());
        if (c.getActive()) {
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
            throw new IllegalArgumentException("Supplier not found:" + c.getSupplierId());
        }
        BffBusinessContactDto bizContact = c.getBusinessContact();
        updatePartyBusinessContact(partyId, bizContact, c);
    }

    private void updatePartyBusinessContact(String partyId, BffBusinessContactDto bizContact, Command c) {
        // 处理邮政地址
        Optional<BffBusinessContactRepository.PostalAddressProjection> pa = bffBusinessContactRepository.findOnePostalAddressByBusinessInfo(
                bizContact.getBusinessName(), bizContact.getZipCode(),
                bizContact.getState(), bizContact.getCity(), bizContact.getPhysicalLocationAddress()
        );
        if (pa.isPresent()) {
            handlePartyContactMechAssociation(partyId, pa.get().getContactMechId(), "-PP", c);
        } else {
            // 创建新的邮政地址
            Optional<BffGeoRepository.StateProvinceProjection> stateProvince
                    = bffGeoRepository.findOneNorthAmericanStateOrProvinceByKeyword(bizContact.getState());
            if (!stateProvince.isPresent()) {
                throw new IllegalArgumentException("State not found:" + bizContact.getState());
            }
            AbstractContactMechCommand.SimpleCreatePostalAddress createPostalAddress = newCreatePostalAddress(bizContact, stateProvince.get());
            createPostalAddress.setCommandId(c.getCommandId() != null ? c.getCommandId() + "-P" : UUID.randomUUID().toString());
            createPostalAddress.setRequesterId(c.getRequesterId());
            contactMechApplicationService.when(createPostalAddress);

            handlePartyContactMechAssociation(partyId, createPostalAddress.getContactMechId(), "-PP", c);
        }

        // 处理电话号码
        TelecomNumberUtil.TelecomNumberDto tn = TelecomNumberUtil.parse(bizContact.getPhoneNumber());
        Optional<BffBusinessContactRepository.TelecomNumber> telecomNumber = bffBusinessContactRepository.findOneTelecomNumberByPhoneInfo(
                tn.getCountryCode(), tn.getAreaCode(), tn.getContactNumber());

        String tnContactMechId;
        if (telecomNumber.isPresent()) {
            tnContactMechId = telecomNumber.get().getContactMechId();
            handlePartyContactMechAssociation(partyId, tnContactMechId, "-PT", c);
        } else {
            // 创建新的电话号码
            AbstractContactMechCommand.SimpleCreateTelecomNumber createTelecomNumber = newCreateTelecomNumber(bizContact);
            createTelecomNumber.setCommandId(c.getCommandId() != null ? c.getCommandId() + "-T" : UUID.randomUUID().toString());
            createTelecomNumber.setRequesterId(c.getRequesterId());
            contactMechApplicationService.when(createTelecomNumber);

            handlePartyContactMechAssociation(partyId, createTelecomNumber.getContactMechId(), "-PT", c);
        }
    }

    private void createPartyBusinessContact(
            String partyId, BffBusinessContactDto bizContact, Command c
    ) {
        if (bizContact.getPhysicalLocationAddress() != null && !bizContact.getPhysicalLocationAddress().trim().isEmpty()) {
            Optional<BffGeoRepository.StateProvinceProjection> stateProvince
                    = bffGeoRepository.findOneNorthAmericanStateOrProvinceByKeyword(bizContact.getState());
            if (stateProvince.isEmpty()) {
                throw new IllegalArgumentException("State not found:" + bizContact.getState());
            }
            AbstractContactMechCommand.SimpleCreatePostalAddress createPostalAddress = newCreatePostalAddress(bizContact, stateProvince.get());
            createPostalAddress.setCommandId(c.getCommandId() != null ? c.getCommandId() + "-P" : UUID.randomUUID().toString());
            createPostalAddress.setRequesterId(c.getRequesterId());
            contactMechApplicationService.when(createPostalAddress);

            createPartyContactMechAssociation(partyId, createPostalAddress.getContactMechId(), "-PP", c);
        }

        if (bizContact.getPhoneNumber() != null && !bizContact.getPhoneNumber().trim().isEmpty()) {
            AbstractContactMechCommand.SimpleCreateTelecomNumber createTelecomNumber = newCreateTelecomNumber(bizContact);
            createTelecomNumber.setCommandId(c.getCommandId() != null ? c.getCommandId() + "-T" : UUID.randomUUID().toString());
            createTelecomNumber.setRequesterId(c.getRequesterId());
            contactMechApplicationService.when(createTelecomNumber);

            createPartyContactMechAssociation(partyId, createTelecomNumber.getContactMechId(), "-PT", c);
        }
    }

    private AbstractPartyContactMechBaseCommand.SimpleCreatePartyContactMechBase newCreatePartyContactMechBase(
            String partyId,
            String contactMechId
    ) {
        AbstractPartyContactMechBaseCommand.SimpleCreatePartyContactMechBase createPartyContactMechBase = new AbstractPartyContactMechBaseCommand.SimpleCreatePartyContactMechBase();
        createPartyContactMechBase.setPartyContactMechBaseId(new PartyContactMechBaseId(partyId, contactMechId));
        PartyContactMechCommand.CreatePartyContactMech createPartyContactMech = createPartyContactMechBase.newCreatePartyContactMech();
        createPartyContactMech.setFromDate(OffsetDateTime.now());
        createPartyContactMech.setThruDate(OffsetDateTime.now().plusYears(100));
        createPartyContactMechBase.getCreatePartyContactMechCommands().add(createPartyContactMech);
        return createPartyContactMechBase;
    }

    private AbstractContactMechCommand.SimpleCreateTelecomNumber newCreateTelecomNumber(BffBusinessContactDto bizContact) {
        TelecomNumberUtil.TelecomNumberDto telecomNumberDto = TelecomNumberUtil.parse(bizContact.getPhoneNumber());
        AbstractContactMechCommand.SimpleCreateTelecomNumber createTelecomNumber = new AbstractContactMechCommand.SimpleCreateTelecomNumber();
        createTelecomNumber.setContactMechId(IdUtils.randomId());
        createTelecomNumber.setCountryCode(telecomNumberDto.getCountryCode());
        createTelecomNumber.setAreaCode(telecomNumberDto.getAreaCode());
        createTelecomNumber.setContactNumber(telecomNumberDto.getContactNumber());
        return createTelecomNumber;
    }

    private AbstractContactMechCommand.SimpleCreatePostalAddress newCreatePostalAddress(
            BffBusinessContactDto bizContact, BffGeoRepository.StateProvinceProjection stateProvince
    ) {
        AbstractContactMechCommand.SimpleCreatePostalAddress createPostalAddress = new AbstractContactMechCommand.SimpleCreatePostalAddress();
        createPostalAddress.setContactMechId(IdUtils.randomId());
        createPostalAddress.setToName(bizContact.getBusinessName());
        createPostalAddress.setPostalCode(bizContact.getZipCode());
        createPostalAddress.setStateProvinceGeoId(stateProvince.getGeoId());
        createPostalAddress.setCity(bizContact.getCity());
        createPostalAddress.setAddress1(bizContact.getPhysicalLocationAddress());
        return createPostalAddress;
    }

    private void handlePartyContactMechAssociation(
            String partyId,
            String contactMechId,
            String commandIdSuffix,
            Command c
    ) {
        PartyContactMechBaseState pcmb = partyContactMechApplicationService.get(new PartyContactMechBaseId(partyId, contactMechId));
        if (pcmb == null) {
            AbstractPartyContactMechBaseCommand.SimpleCreatePartyContactMechBase createPartyContactMechBase
                    = newCreatePartyContactMechBase(partyId, contactMechId);
            createPartyContactMechBase.setCommandId(c.getCommandId() != null ? c.getCommandId() + commandIdSuffix : UUID.randomUUID().toString());
            createPartyContactMechBase.setRequesterId(c.getRequesterId());
            partyContactMechApplicationService.when(createPartyContactMechBase);
        } else {
            AbstractPartyContactMechBaseCommand.SimpleMergePatchPartyContactMechBase mergePatchPartyContactMechBase
                    = new AbstractPartyContactMechBaseCommand.SimpleMergePatchPartyContactMechBase();
            mergePatchPartyContactMechBase.setPartyContactMechBaseId(pcmb.getPartyContactMechBaseId());
            mergePatchPartyContactMechBase.setVersion(pcmb.getVersion());

            Optional<PartyContactMechState> pcm = pcmb.getContactMechanisms().stream().filter(x ->
                    x.getPartyContactMechBaseId().getContactMechId().equals(contactMechId)
                            && x.getFromDate().isBefore(OffsetDateTime.now())
                            && x.getThruDate().isAfter(OffsetDateTime.now())
            ).findFirst();
            if (pcm.isPresent()) {
                PartyContactMechCommand.MergePatchPartyContactMech mergePatchPartyContactMech = mergePatchPartyContactMechBase.newMergePatchPartyContactMech();
                mergePatchPartyContactMech.setFromDate(OffsetDateTime.now());
                mergePatchPartyContactMech.setThruDate(OffsetDateTime.now().plusYears(100));
                mergePatchPartyContactMechBase.getPartyContactMechCommands().add(mergePatchPartyContactMech);
            } else {
                PartyContactMechCommand.CreatePartyContactMech createPartyContactMech = mergePatchPartyContactMechBase.newCreatePartyContactMech();
                createPartyContactMech.setFromDate(OffsetDateTime.now());
                createPartyContactMech.setThruDate(OffsetDateTime.now().plusYears(100));
                mergePatchPartyContactMechBase.getPartyContactMechCommands().add(createPartyContactMech);
            }
            mergePatchPartyContactMechBase.setCommandId(c.getCommandId() != null ? c.getCommandId() + commandIdSuffix : UUID.randomUUID().toString());
            mergePatchPartyContactMechBase.setRequesterId(c.getRequesterId());
            partyContactMechApplicationService.when(mergePatchPartyContactMechBase);
        }
    }

    private void createPartyContactMechAssociation(
            String partyId,
            String contactMechId,
            String commandIdSuffix,
            Command c
    ) {
        AbstractPartyContactMechBaseCommand.SimpleCreatePartyContactMechBase createPartyContactMechBase
                = newCreatePartyContactMechBase(partyId, contactMechId);
        createPartyContactMechBase.setCommandId(c.getCommandId() != null ? c.getCommandId() + commandIdSuffix : UUID.randomUUID().toString());
        createPartyContactMechBase.setRequesterId(c.getRequesterId());
        partyContactMechApplicationService.when(createPartyContactMechBase);
    }

}
