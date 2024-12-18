package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffSupplierDto;
import org.dddml.ffvtraceability.domain.party.*;
import org.dddml.ffvtraceability.domain.partyrole.AbstractPartyRoleCommand;
import org.dddml.ffvtraceability.domain.partyrole.PartyRoleApplicationService;
import org.dddml.ffvtraceability.domain.partyrole.PartyRoleId;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

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


    @Override
    public Page<BffSupplierDto> when(BffSupplierServiceCommands.GetSuppliers c) {
        return null;
    }

    @Override
    public BffSupplierDto when(BffSupplierServiceCommands.GetSupplier c) {
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
        // 检查是否存在PartyIdentification A
        if (oldGgn.isPresent()) {//原来有 GGN
            // 如果bffSupplier.getGgn()不等于现有的Ggn，更新IdValue
            if (StringUtils.hasText(bffSupplier.getGgn())) {
                if (!oldGgn.get().getIdValue().equals(bffSupplier.getGgn())) {
                    //修改已有的
                }
            } else {
                //删除原有的PartyIdentification
            }
        } else { //原来没有GGN且bffSupplier.getGgn()不为空，添加新的PartyIdentification
            if (StringUtils.hasText(bffSupplier.getGgn())) {
                PartyIdentificationCommand.CreatePartyIdentification createPartyIdentification = mergePatchParty.newCreatePartyIdentification();
                createPartyIdentification.setPartyIdentificationTypeId(PARTY_IDENTIFICATION_TYPE_GGN);
                createPartyIdentification.setIdValue(bffSupplier.getGgn());
                mergePatchParty.getPartyIdentificationCommands().add(createPartyIdentification);
            }
        }
        partyApplicationService.when(mergePatchParty);
    }

    @Override
    public void when(BffSupplierServiceCommands.ActivateSupplier c) {

    }
}
