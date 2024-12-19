package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffUomDto;
import org.dddml.ffvtraceability.domain.uom.AbstractUomCommand;
import org.dddml.ffvtraceability.domain.uom.UomApplicationService;
import org.dddml.ffvtraceability.domain.uom.UomState;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BffUomApplicationServiceImpl implements BffUomApplicationService {
    public static final String PARTY_STATUS_ACTIVE = "ACTIVE";
    public static final String PARTY_STATUS_INACTIVE = "INACTIVE";

    @Autowired
    private UomApplicationService uomApplicationService;

    @Override
    public Page<BffUomDto> when(BffUomServiceCommands.GetUnitsOfMeasure c) {
        return null;
    }

    @Override
    public BffUomDto when(BffUomServiceCommands.GetUnitOfMeasure c) {
        return null;
    }

    @Override
    @Transactional
    public String when(BffUomServiceCommands.CreateUnitOfMeasure c) {
        AbstractUomCommand.SimpleCreateUom createUom = new AbstractUomCommand.SimpleCreateUom();
        createUom.setUomId(c.getUom().getUomId() != null ? c.getUom().getUomId() : IdUtils.randomId());
        createUom.setUomTypeId("UOM"); //todo hardcoded?
        createUom.setAbbreviation(c.getUom().getAbbreviation());
        createUom.setDescription(c.getUom().getDescription());
        createUom.setNumericCode(c.getUom().getNumericCode());
        createUom.setGs1AI(c.getUom().getGs1AI());
        createUom.setCommandId(c.getCommandId() != null ? c.getCommandId() : createUom.getUomId());
        createUom.setRequesterId(c.getRequesterId());
        uomApplicationService.when(createUom);
        return createUom.getUomId();
    }

    @Override
    public void when(BffUomServiceCommands.UpdateUnitOfMeasure c) {

    }

    @Override
    @Transactional
    public void when(BffUomServiceCommands.ActivateUnitOfMeasure c) {
        UomState uomState = uomApplicationService.get(c.getUomId());
        if (uomState == null) {
            throw new IllegalArgumentException("Unit of measure not found: " + c.getUomId());
        }
        AbstractUomCommand.SimpleMergePatchUom mergePatchUom = new AbstractUomCommand.SimpleMergePatchUom();
        mergePatchUom.setUomId(c.getUomId());
        mergePatchUom.setVersion(uomState.getVersion());
        mergePatchUom.setActive(c.getActive() ? PARTY_STATUS_ACTIVE : PARTY_STATUS_INACTIVE);
        mergePatchUom.setCommandId(c.getCommandId()!= null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchUom.setRequesterId(c.getRequesterId());
        uomApplicationService.when(mergePatchUom);
    }
}
