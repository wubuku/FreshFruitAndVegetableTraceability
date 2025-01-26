package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffLotDto;
import org.dddml.ffvtraceability.domain.lot.AbstractLotCommand;
import org.dddml.ffvtraceability.domain.lot.LotApplicationService;
import org.dddml.ffvtraceability.domain.lot.LotIdentificationCommand;
import org.dddml.ffvtraceability.domain.lot.LotState;
import org.dddml.ffvtraceability.domain.mapper.BffLotMapper;
import org.dddml.ffvtraceability.domain.repository.BffLotRepository;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.domain.util.IndicatorUtils;
import org.dddml.ffvtraceability.domain.util.PageUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.dddml.ffvtraceability.domain.constants.BffLotConstants.LOT_IDENTIFICATION_TYPE_GS1_BATCH;
import static org.dddml.ffvtraceability.domain.util.IndicatorUtils.INDICATOR_NO;
import static org.dddml.ffvtraceability.domain.util.IndicatorUtils.INDICATOR_YES;

@Service
@Transactional
public class BffLotApplicationServiceImpl implements BffLotApplicationService {

    @Autowired
    private LotApplicationService lotApplicationService;

    @Autowired
    private BffLotRepository bffLotRepository;

    @Autowired
    private BffLotMapper bffLotMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<BffLotDto> when(BffLotServiceCommands.GetLots c) {
        return PageUtils.toPage(
                bffLotRepository.findAllLots(PageRequest.of(c.getPage(), c.getSize()), c.getActive(), c.getKeyword()),
                bffLotMapper::toBffLotDto
        );
    }

    @Override
    @Transactional(readOnly = true)
    public BffLotDto when(BffLotServiceCommands.GetLot c) {
        if (c.getLotId() == null) {
            throw new NullPointerException("Lot id can't be null");
        }
        LotState lotState = lotApplicationService.get(c.getLotId());
        if (lotState == null) {
            return null;
        }
        BffLotDto dto = bffLotMapper.toBffLotDto(lotState);
        lotState.getLotIdentifications().stream().forEach(x -> {
            if (x.getLotIdentificationTypeId().equals(LOT_IDENTIFICATION_TYPE_GS1_BATCH)) {
                dto.setGs1Batch(x.getIdValue());
            }
        });
        return dto;
    }

    @Override
    @Transactional
    public String when(BffLotServiceCommands.CreateLot c) {
        BffLotDto lotDto = c.getLot();
        if (lotDto == null) {
            throw new NullPointerException("Lot information can't be null");
        }
        if (lotDto.getLotId() != null && !lotDto.getLotId().isEmpty()) {
            if (lotApplicationService.get(lotDto.getLotId()) != null) {
                throw new IllegalArgumentException(String.format("已经存在的批次:%s", lotDto.getLotId()));
            }
        }
        AbstractLotCommand.SimpleCreateLot createLot = bffLotMapper.toCreateLot(lotDto);
        createLot.setLotId(lotDto.getLotId() != null ? lotDto.getLotId() : IdUtils.randomId());
        createLot.setActive(IndicatorUtils.asIndicatorDefaultYes(lotDto.getActive())); // 将前端传入的 active 规范化
        createLot.setCommandId(c.getCommandId() != null ? c.getCommandId() : createLot.getLotId());
        createLot.setRequesterId(c.getRequesterId());
        if (lotDto.getGs1Batch() != null) {
            LotIdentificationCommand.CreateLotIdentification createLotIdentification = createLot.newCreateLotIdentification();
            createLotIdentification.setLotIdentificationTypeId(LOT_IDENTIFICATION_TYPE_GS1_BATCH);
            createLotIdentification.setIdValue(lotDto.getGs1Batch());
            createLot.getCreateLotIdentificationCommands().add(createLotIdentification);
        }
        //createLot.setGs1Batch(lotDto.getGs1Batch());
        lotApplicationService.when(createLot);
        return createLot.getLotId();
    }

    @Override
    @Transactional
    public void when(BffLotServiceCommands.UpdateLot c) {
        String lotId = c.getLotId();
        BffLotDto lotDto = c.getLot();
        lotDto.setLotId(lotId);
        LotState lotState = lotApplicationService.get(lotId);
        if (lotState == null) {
            throw new IllegalArgumentException("Lot not found: " + lotId);
        }
        AbstractLotCommand.SimpleMergePatchLot mergePatchLot = bffLotMapper.toMergePatchLot(lotDto);

        mergePatchLot.setVersion(lotState.getVersion());
        mergePatchLot.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchLot.setRequesterId(c.getRequesterId());
        lotApplicationService.when(mergePatchLot);
    }

    @Override
    @Transactional
    public void when(BffLotServiceCommands.ActivateLot c) {
        String lotId = c.getLotId();
        LotState lotState = lotApplicationService.get(lotId);
        if (lotState == null) {
            throw new IllegalArgumentException("Lot not found: " + lotId);
        }
        AbstractLotCommand.SimpleMergePatchLot mergePatchLot = new AbstractLotCommand.SimpleMergePatchLot();
        mergePatchLot.setLotId(lotId);
        mergePatchLot.setVersion(lotState.getVersion());
        mergePatchLot.setActive(c.getActive() ? INDICATOR_YES : INDICATOR_NO);
        mergePatchLot.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchLot.setRequesterId(c.getRequesterId());
        lotApplicationService.when(mergePatchLot);
    }

    @Override
    public void when(BffLotServiceCommands.BatchAddLots c) {

        //todo
    }
}
