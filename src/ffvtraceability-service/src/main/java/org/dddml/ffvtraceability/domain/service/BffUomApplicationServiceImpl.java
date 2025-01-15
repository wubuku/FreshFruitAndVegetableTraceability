package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffUomDto;
import org.dddml.ffvtraceability.domain.mapper.BffUomMapper;
import org.dddml.ffvtraceability.domain.repository.BffUomRepository;
import org.dddml.ffvtraceability.domain.uom.AbstractUomCommand;
import org.dddml.ffvtraceability.domain.uom.UomApplicationService;
import org.dddml.ffvtraceability.domain.uom.UomState;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.domain.util.IndicatorUtils;
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
        BffUomDto uomDto = c.getUom();
        AbstractUomCommand.SimpleCreateUom createUom = bffUomMapper.toCreateUom(uomDto);
        createUom.setUomId(uomDto.getUomId() != null ? uomDto.getUomId() : IdUtils.randomId());
        createUom.setActive(IndicatorUtils.asIndicatorDefaultYes(uomDto.getActive()));
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
        BffUomDto uomDto = c.getUom();
        uomDto.setUomId(uomId); // Id 有可能是通过 URL 传入的，所以需要设置
        AbstractUomCommand.SimpleMergePatchUom mergePatchUom = bffUomMapper.toMergePatchUom(uomDto);
        mergePatchUom.setActive(IndicatorUtils.asIndicatorDefaultYes(uomDto.getActive()));

        mergePatchUom.setVersion(uomState.getVersion()); // 乐观锁。需要设置状态对象中的版本号
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
