package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffBusinessContactDto;
import org.dddml.ffvtraceability.domain.BffSupplierDto;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.domain.contactmech.ContactMechApplicationService;
import org.dddml.ffvtraceability.domain.contactmech.ContactMechTypeId;
import org.dddml.ffvtraceability.domain.mapper.BffSupplierMapper;
import org.dddml.ffvtraceability.domain.party.*;
import org.dddml.ffvtraceability.domain.partycontactmech.AbstractPartyContactMechCommand;
import org.dddml.ffvtraceability.domain.partycontactmech.PartyContactMechApplicationService;
import org.dddml.ffvtraceability.domain.partycontactmech.PartyContactMechId;
import org.dddml.ffvtraceability.domain.partycontactmech.PartyContactMechState;
import org.dddml.ffvtraceability.domain.partyrole.AbstractPartyRoleCommand;
import org.dddml.ffvtraceability.domain.partyrole.PartyRoleApplicationService;
import org.dddml.ffvtraceability.domain.partyrole.PartyRoleId;
import org.dddml.ffvtraceability.domain.repository.BffBusinessContactRepository;
import org.dddml.ffvtraceability.domain.repository.BffGeoRepository;
import org.dddml.ffvtraceability.domain.repository.BffPartyContactMechRepository;
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

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.dddml.ffvtraceability.domain.constants.BffSupplierConstants.*;

@Service
public class BffSupplierApplicationServiceImpl implements BffSupplierApplicationService {

    private static final String ERROR_SUPPLIER_NOT_FOUND = "Supplier not found: %s";
    private static final String ERROR_STATE_NOT_FOUND = "State not found: %s";

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
    @Autowired
    private BffPartyContactMechRepository bffPartyContactMechRepository;

    @Autowired
    private BffBusinessContactService bffBusinessContactService;

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
        enrichBusinessContactDetails(dto, c.getSupplierId());
        return dto;
    }

    private void enrichBusinessContactDetails(BffSupplierDto dto, String supplierId) {
        bffPartyContactMechRepository.findPartyCurrentPostalAddressByPartyId(supplierId).ifPresent(x -> {
            BffBusinessContactDto bc = new BffBusinessContactDto();
            bc.setBusinessName(x.getToName());
            bc.setPhysicalLocationAddress(x.getAddress1());
            bc.setCity(x.getCity());
            bc.setState(x.getStateProvinceGeoId());
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
            throw new IllegalArgumentException(String.format(ERROR_SUPPLIER_NOT_FOUND, c.getSupplierId()));
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
            updateOrCreatePartyBusinessContact(supplierId, c.getSupplier().getBusinessContacts().get(0), c);
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
            throw new IllegalArgumentException(String.format(ERROR_SUPPLIER_NOT_FOUND, c.getSupplierId()));
        }
        BffBusinessContactDto bizContact = c.getBusinessContact();
        updateOrCreatePartyBusinessContact(partyId, bizContact, c);
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
            Optional<BffGeoRepository.StateProvinceProjection> stateProvince
                    = bffGeoRepository.findOneNorthAmericanStateOrProvinceByKeyword(bizContact.getState());
            if (!stateProvince.isPresent()) {
                throw new IllegalArgumentException(String.format(ERROR_STATE_NOT_FOUND, bizContact.getState()));
            }
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
            Optional<BffGeoRepository.StateProvinceProjection> stateProvince
                    = bffGeoRepository.findOneNorthAmericanStateOrProvinceByKeyword(bizContact.getState());
            if (!stateProvince.isPresent()) {
                throw new IllegalArgumentException(String.format(ERROR_STATE_NOT_FOUND, bizContact.getState()));
            }
            String contactMechId = bffBusinessContactService.createPostalAddress(bizContact, c);
            createPartyContactMechAssociation(partyId, contactMechId, "-PP", c);
        }

        if (bizContact.getPhoneNumber() != null && !bizContact.getPhoneNumber().trim().isEmpty()) {
            String contactMechId = bffBusinessContactService.createTelecomNumber(bizContact, c);
            createPartyContactMechAssociation(partyId, contactMechId, "-PT", c);
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
