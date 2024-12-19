package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffSupplierDto;
import org.dddml.ffvtraceability.domain.mapper.BffSupplierMapper;
import org.dddml.ffvtraceability.domain.party.AbstractPartyCommand;
import org.dddml.ffvtraceability.domain.party.PartyApplicationService;
import org.dddml.ffvtraceability.domain.party.PartyIdentificationCommand;
import org.dddml.ffvtraceability.domain.party.PartyState;
import org.dddml.ffvtraceability.domain.partyrole.AbstractPartyRoleCommand;
import org.dddml.ffvtraceability.domain.partyrole.PartyRoleApplicationService;
import org.dddml.ffvtraceability.domain.partyrole.PartyRoleId;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class BffSupplierApplicationServiceImpl implements BffSupplierApplicationService {
    /**
     * GGN (GLOBALG.A.P. Number)
     */
    public static final String PARTY_IDENTIFICATION_TYPE_GGN = "GGN";

    /**
     * GLN (Global Location Number)
     */
    public static final String PARTY_IDENTIFICATION_TYPE_GLN = "GLN";

    public static final String DEFAULT_PREFERRED_CURRENCY_UOM_ID = "USD"; //todo get from config?

    public static final String PARTY_ROLE_SUPPLIER = "SUPPLIER";

    @Autowired
    private PartyApplicationService partyApplicationService;

    @Autowired
    private PartyRoleApplicationService partyRoleApplicationService;

    @Autowired
    private BffSupplierMapper bffSupplierMapper;

    @Override
    public Page<BffSupplierDto> when(BffSupplierServiceCommands.GetSuppliers c) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public BffSupplierDto when(BffSupplierServiceCommands.GetSupplier c) {
        PartyState party = partyApplicationService.get(c.getSupplierId());
        if (party != null) {
            BffSupplierDto dto = bffSupplierMapper.toBffSupplierDto(party);
            party.getPartyIdentifications().stream().forEach(x -> {
                if (x.getPartyIdentificationTypeId().equals(PARTY_IDENTIFICATION_TYPE_GLN)) {
                    dto.setGln(x.getIdValue());
                } else if (x.getPartyIdentificationTypeId().equals(PARTY_IDENTIFICATION_TYPE_GGN)) {
                    dto.setGgn(x.getIdValue());
                }
            });

            return dto;
        }
        return null;
    }

    @Override
    @Transactional
    public String when(BffSupplierServiceCommands.CreateSupplier c) {
        AbstractPartyCommand.SimpleCreateParty createParty = new AbstractPartyCommand.SimpleCreateParty();
        createParty.setPartyId(c.getSupplier().getSupplierId() != null ? c.getSupplier().getSupplierId() : IdUtils.randomId());
        createParty.setExternalId(c.getSupplier().getExternalId());
        createParty.setDescription(c.getSupplier().getDescription());
        createParty.setPreferredCurrencyUomId(c.getSupplier().getPreferredCurrencyUomId() != null ? c.getSupplier().getPreferredCurrencyUomId() : DEFAULT_PREFERRED_CURRENCY_UOM_ID);
        if (c.getSupplier().getGgn() != null) {
            PartyIdentificationCommand.CreatePartyIdentification createPartyIdentification = createParty.newCreatePartyIdentification();
            createPartyIdentification.setPartyIdentificationTypeId(PARTY_IDENTIFICATION_TYPE_GGN);
            createPartyIdentification.setIdValue(c.getSupplier().getGgn());
            createParty.getCreatePartyIdentificationCommands().add(createPartyIdentification);
        }
        if (c.getSupplier().getGln() != null) {
            PartyIdentificationCommand.CreatePartyIdentification createPartyIdentification = createParty.newCreatePartyIdentification();
            createPartyIdentification.setPartyIdentificationTypeId(PARTY_IDENTIFICATION_TYPE_GLN);
            createPartyIdentification.setIdValue(c.getSupplier().getGln());
            createParty.getCreatePartyIdentificationCommands().add(createPartyIdentification);
        }
        createParty.setCommandId(createParty.getPartyId()); //c.getCommandId());
        createParty.setRequesterId(c.getRequesterId());
        partyApplicationService.when(createParty);

        AbstractPartyRoleCommand.SimpleCreatePartyRole createPartyRole = new AbstractPartyRoleCommand.SimpleCreatePartyRole();
        PartyRoleId partyRoleId = new PartyRoleId();
        partyRoleId.setPartyId(createParty.getPartyId());
        partyRoleId.setRoleTypeId(PARTY_ROLE_SUPPLIER);
        createPartyRole.setPartyRoleId(partyRoleId);
        createPartyRole.setActive(true);
        createPartyRole.setCommandId(createParty.getPartyId() + "-SUPPLIER"); //c.getCommandId());
        createPartyRole.setRequesterId(c.getRequesterId());
        partyRoleApplicationService.when(createPartyRole);

        return createParty.getPartyId();
    }

    @Override
    public void when(BffSupplierServiceCommands.UpdateSupplier c) {
        String supplierId = c.getSupplierId();
        PartyState partyState = partyApplicationService.get(supplierId);
        if (partyState == null) {
            throw new IllegalArgumentException("Supplier not found:" + c.getSupplierId());
        }
        BffSupplierDto bffSupplier = c.getSupplier();
        AbstractPartyCommand.SimpleMergePatchParty mergePatchParty = new AbstractPartyCommand.SimpleMergePatchCompany();
        mergePatchParty.setPartyId(supplierId);
        mergePatchParty.setVersion(partyState.getVersion());//乐观锁
        mergePatchParty.setRequesterId(c.getRequesterId());
        mergePatchParty.setExternalId(bffSupplier.getExternalId());
        mergePatchParty.setDescription(bffSupplier.getDescription());
        //检查GGN
        var oldGgn = partyState.getPartyIdentifications().stream()
                .filter(t -> t.getPartyIdentificationTypeId().equals(PARTY_IDENTIFICATION_TYPE_GGN))
                .findFirst();
        // 检查是否存在 PartyIdentification 
        if (oldGgn.isPresent()) {//原来有 GGN
            // 如果bffSupplier.getGgn()不等于现有的Ggn，更新IdValue
            if (StringUtils.hasText(bffSupplier.getGgn())) {
                if (!oldGgn.get().getIdValue().equals(bffSupplier.getGgn())) {
                    PartyIdentificationCommand.MergePatchPartyIdentification m = mergePatchParty.newMergePatchPartyIdentification();
                    m.setPartyIdentificationTypeId(PARTY_IDENTIFICATION_TYPE_GGN);
                    m.setIdValue(bffSupplier.getGgn());
                    mergePatchParty.getPartyIdentificationCommands().add(m);
                }
            } else {
                PartyIdentificationCommand.RemovePartyIdentification r = mergePatchParty.newRemovePartyIdentification();
                r.setPartyIdentificationTypeId(PARTY_IDENTIFICATION_TYPE_GGN);
                mergePatchParty.getPartyIdentificationCommands().add(r);
            }
        } else { //原来没有GGN且bffSupplier.getGgn()不为空，添加新的PartyIdentification
            if (StringUtils.hasText(bffSupplier.getGgn())) {
                PartyIdentificationCommand.CreatePartyIdentification createPartyIdentification = mergePatchParty.newCreatePartyIdentification();
                createPartyIdentification.setPartyIdentificationTypeId(PARTY_IDENTIFICATION_TYPE_GGN);
                createPartyIdentification.setIdValue(bffSupplier.getGgn());
                mergePatchParty.getPartyIdentificationCommands().add(createPartyIdentification);
            }
        }
        // 检查GLN
        var oldGln = partyState.getPartyIdentifications().stream()
                .filter(t -> t.getPartyIdentificationTypeId().equals(PARTY_IDENTIFICATION_TYPE_GLN))
                .findFirst();
        // 检查是否存在 PartyIdentification 
        if (oldGln.isPresent()) { // 原来有 GLN
            // 如果bffSupplier.getGln()不等于现有的Gln，更新IdValue
            if (StringUtils.hasText(bffSupplier.getGln())) {
                if (!oldGln.get().getIdValue().equals(bffSupplier.getGln())) {
                    PartyIdentificationCommand.MergePatchPartyIdentification m = mergePatchParty.newMergePatchPartyIdentification();
                    m.setPartyIdentificationTypeId(PARTY_IDENTIFICATION_TYPE_GLN);
                    m.setIdValue(bffSupplier.getGln());
                    mergePatchParty.getPartyIdentificationCommands().add(m);
                }
            } else {
                PartyIdentificationCommand.RemovePartyIdentification r = mergePatchParty.newRemovePartyIdentification();
                r.setPartyIdentificationTypeId(PARTY_IDENTIFICATION_TYPE_GLN);
                mergePatchParty.getPartyIdentificationCommands().add(r);
            }
        } else { // 原来没有GLN且bffSupplier.getGln()不为空，添加新的PartyIdentification
            if (StringUtils.hasText(bffSupplier.getGln())) {
                PartyIdentificationCommand.CreatePartyIdentification createPartyIdentification = mergePatchParty.newCreatePartyIdentification();
                createPartyIdentification.setPartyIdentificationTypeId(PARTY_IDENTIFICATION_TYPE_GLN);
                createPartyIdentification.setIdValue(bffSupplier.getGln());
                mergePatchParty.getPartyIdentificationCommands().add(createPartyIdentification);
            }
        }
        partyApplicationService.when(mergePatchParty);
    }

    @Override
    public void when(BffSupplierServiceCommands.ActivateSupplier c) {

    }
}
