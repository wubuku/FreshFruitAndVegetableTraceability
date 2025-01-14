package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffUomDto;
import org.dddml.ffvtraceability.domain.mapper.BffUomMapper;
import org.dddml.ffvtraceability.domain.repository.BffUomRepository;
import org.dddml.ffvtraceability.domain.uom.AbstractUomCommand;
import org.dddml.ffvtraceability.domain.uom.UomApplicationService;
import org.dddml.ffvtraceability.domain.uom.UomState;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.domain.util.PageUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.dddml.ffvtraceability.domain.util.IndicatorUtils.INDICATOR_NO;
import static org.dddml.ffvtraceability.domain.util.IndicatorUtils.INDICATOR_YES;

@Service
@Transactional
public class BffUomApplicationServiceImpl implements BffUomApplicationService {
    @Autowired
    private UomApplicationService uomApplicationService;

    @Autowired
    private BffUomRepository bffUomRepository;

    @Autowired
    private BffUomMapper bffUomMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<BffUomDto> when(BffUomServiceCommands.GetUnitsOfMeasure c) {
        return PageUtils.toPage(
                bffUomRepository.findAllUnitsOfMeasure(
                        PageRequest.of(c.getPage(), c.getSize()), c.getActive(), c.getUomTypeId()
                ),
                bffUomMapper::toBffUomDto
        );
    }

    @Override
    @Transactional(readOnly = true)
    public BffUomDto when(BffUomServiceCommands.GetUnitOfMeasure c) {
        UomState uomState = uomApplicationService.get(c.getUomId());
        if (uomState != null) {
            return bffUomMapper.toBffUomDto(uomState);
        }
        return null;
    }

    @Override
    @Transactional
    public String when(BffUomServiceCommands.CreateUnitOfMeasure c) {
        AbstractUomCommand.SimpleCreateUom createUom = new AbstractUomCommand.SimpleCreateUom();
        createUom.setUomId(c.getUom().getUomId() != null ? c.getUom().getUomId() : IdUtils.randomId());
        createUom.setUomTypeId(c.getUom().getUomTypeId());
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
    @Transactional
    public void when(BffUomServiceCommands.UpdateUnitOfMeasure c) {
        String uomId = c.getUomId();
        UomState uomState = uomApplicationService.get(uomId);
        if (uomState == null) {
            throw new IllegalArgumentException("Unit of measure not found: " + uomId);
        }
        AbstractUomCommand.SimpleMergePatchUom mergePatchUom = new AbstractUomCommand.SimpleMergePatchUom();
        mergePatchUom.setUomId(uomId);
        mergePatchUom.setUomTypeId(c.getUom().getUomTypeId());
        mergePatchUom.setVersion(uomState.getVersion());
        mergePatchUom.setAbbreviation(c.getUom().getAbbreviation());
        mergePatchUom.setDescription(c.getUom().getDescription());
        mergePatchUom.setNumericCode(c.getUom().getNumericCode());
        mergePatchUom.setGs1AI(c.getUom().getGs1AI());
        mergePatchUom.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchUom.setRequesterId(c.getRequesterId());
        uomApplicationService.when(mergePatchUom);
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
        mergePatchUom.setActive(c.getActive() ? INDICATOR_YES : INDICATOR_NO);
        mergePatchUom.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchUom.setRequesterId(c.getRequesterId());
        uomApplicationService.when(mergePatchUom);
    }

    @Override
    public void when(BffUomServiceCommands.BatchAddUnitsOfMeasure c) {
        //todo
    }
}
