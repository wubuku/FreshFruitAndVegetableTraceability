package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffSupplierDto;
import org.dddml.ffvtraceability.domain.mapper.BffSupplierMapper;
import org.dddml.ffvtraceability.domain.party.*;
import org.dddml.ffvtraceability.domain.partyrole.AbstractPartyRoleCommand;
import org.dddml.ffvtraceability.domain.partyrole.PartyRoleApplicationService;
import org.dddml.ffvtraceability.domain.partyrole.PartyRoleId;
import org.dddml.ffvtraceability.domain.repository.BffSupplierRepository;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.domain.util.IndicatorUtils;
import org.dddml.ffvtraceability.domain.util.PageUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Service
public class BffSupplierApplicationServiceImpl implements BffSupplierApplicationService {
    public static final String DEFAULT_PREFERRED_CURRENCY_UOM_ID = "USD"; //todo get from config?

    public static final String PARTY_ROLE_SUPPLIER = "SUPPLIER";

    public static final String PARTY_STATUS_ACTIVE = "ACTIVE";
    public static final String PARTY_STATUS_INACTIVE = "INACTIVE";

    /**
     * GGN (GLOBALG.A.P. Number)
     */
    public static final String PARTY_IDENTIFICATION_TYPE_GGN = "GGN";

    /**
     * GLN (Global Location Number)
     */
    public static final String PARTY_IDENTIFICATION_TYPE_GLN = "GLN";

    @Autowired
    private PartyApplicationService partyApplicationService;

    @Autowired
    private PartyRoleApplicationService partyRoleApplicationService;

    @Autowired
    private BffSupplierMapper bffSupplierMapper;

    @Autowired
    private BffSupplierRepository bffSupplierRepository;

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
        createParty.setCommandId(createParty.getPartyId()); //c.getCommandId());
        createParty.setRequesterId(c.getRequesterId());
        partyApplicationService.when(createParty);

        AbstractPartyRoleCommand.SimpleCreatePartyRole createPartyRole = new AbstractPartyRoleCommand.SimpleCreatePartyRole();
        PartyRoleId partyRoleId = new PartyRoleId();
        partyRoleId.setPartyId(createParty.getPartyId());
        partyRoleId.setRoleTypeId(PARTY_ROLE_SUPPLIER);
        createPartyRole.setPartyRoleId(partyRoleId);
        createPartyRole.setCommandId(createParty.getPartyId() + "-SUPPLIER"); //c.getCommandId());
        createPartyRole.setRequesterId(c.getRequesterId());
        partyRoleApplicationService.when(createPartyRole);

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
    public void when(BffSupplierServiceCommands.UpdateBusinessContact c) {
        //todo
    }
}
