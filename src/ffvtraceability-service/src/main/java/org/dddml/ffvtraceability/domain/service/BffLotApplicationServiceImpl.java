package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffLotDto;
import org.dddml.ffvtraceability.domain.lot.*;
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

import java.util.*;

import static org.dddml.ffvtraceability.domain.constants.BffLotConstants.LOT_IDENTIFICATION_TYPE_TLC_CASE_GTIN_BATCH;
import static org.dddml.ffvtraceability.domain.constants.BffLotConstants.LOT_TYPE_ID_PRIMARY_TLC;
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

    public static String formatTlcCaseGtinAndBatch(String caseGtin, String caseBatch) {
        if (caseGtin == null || caseGtin.trim().isEmpty() || caseBatch == null || caseBatch.trim().isEmpty()) {
            throw new IllegalArgumentException("Case GTIN and Case Batch can't be null");
        }
        // 使用格式 `(01){GTIN}(10){GS1_BATCH}` 来格式化
        // AI (01) 表示 GTIN
        // AI (10) 表示 Batch/Lot Number
        return String.format("(01)%s(10)%s", caseGtin.trim(), caseBatch.trim());
    }

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
            throw new IllegalArgumentException("Lot id can't be null");
        }
        LotState lotState = lotApplicationService.get(c.getLotId());
        if (lotState == null) {
            return null;
        }
        BffLotDto dto = bffLotMapper.toBffLotDto(lotState);
//        lotState.getLotIdentifications().stream().forEach(x -> {
//            if (x.getLotIdentificationTypeId().equals(LOT_IDENTIFICATION_TYPE_GS1_BATCH)) {
//                dto.setGs1Batch(x.getIdValue());
//            }
//        });
        return dto;
    }

    @Override
    @Transactional
    public String when(BffLotServiceCommands.CreateLot c) {
        BffLotDto lotDto = c.getLot();
        if (lotDto == null) {
            throw new IllegalArgumentException("Lot information can't be null");
        }
        if (lotDto.getLotId() != null && !lotDto.getLotId().isEmpty()) {
            if (lotApplicationService.get(lotDto.getLotId()) != null) {
                throw new IllegalArgumentException(String.format("Lot already exists: %s", lotDto.getLotId()));
            }
        }
        AbstractLotCommand.SimpleCreateLot createLot = bffLotMapper.toCreateLot(lotDto);
        createLot.setLotId(lotDto.getLotId() != null && !lotDto.getLotId().isEmpty() ? lotDto.getLotId() : IdUtils.randomId());
        createLot.setActive(IndicatorUtils.asIndicatorDefaultYes(lotDto.getActive())); // 将前端传入的 active 规范化
        createLot.setCommandId(c.getCommandId() != null ? c.getCommandId() : createLot.getLotId());
        createLot.setRequesterId(c.getRequesterId());
        lotApplicationService.when(createLot);
        return createLot.getLotId();
    }

    @Override
    @Transactional
    public void when(BffLotServiceCommands.UpdateLot c) {
        String lotId = c.getLotId();
        BffLotDto lotDto = c.getLot();
        if (lotDto == null) {
            throw new IllegalArgumentException("Lot information can't be null");
        }
        LotState lotState = lotApplicationService.get(lotId);
        if (lotState == null) {
            throw new IllegalArgumentException("Lot not found: " + lotId);
        }
        AbstractLotCommand.SimpleMergePatchLot mergePatchLot = bffLotMapper.toMergePatchLot(lotDto);

        mergePatchLot.setVersion(lotState.getVersion());
        mergePatchLot.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchLot.setLotId(lotId);
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
        mergePatchLot.setActive(c.getActive() != null && c.getActive() ? INDICATOR_YES : INDICATOR_NO);
        mergePatchLot.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchLot.setRequesterId(c.getRequesterId());
        lotApplicationService.when(mergePatchLot);
    }

    @Override
    public void when(BffLotServiceCommands.BatchAddLots c) {
        List<String> lotIds = new ArrayList<>();
        //前端传过来的dto列表中，如果dto包括了id，那么这些id不能重复
        Arrays.stream(c.getLots()).forEach(dto -> {
            if (dto.getLotId() != null && !dto.getLotId().isEmpty()) {
                if (lotIds.contains(dto.getLotId())) {
                    throw new IllegalArgumentException(String.format("Duplicate lot Id: %s", dto.getLotId()));
                } else {
                    lotIds.add(dto.getLotId());
                }
            }
            BffLotServiceCommands.CreateLot createLot = new BffLotServiceCommands.CreateLot();
            createLot.setRequesterId(c.getRequesterId());
            createLot.setLot(dto);
            when(createLot);
        });
    }

    @Override
    public BffLotDto when(BffLotServiceCommands.GetPrimaryTlc c) {
        return bffLotRepository.findPrimaryTlcByCaseGtinAndBatch(c.getGtin(), c.getGs1Batch())
                .map(bffLotMapper::toBffLotDto).orElse(null);
    }

    @Override
    public Page<BffLotDto> when(BffLotServiceCommands.GetAllPrimaryTlcs c) {
        return PageUtils.toPage(
                bffLotRepository.findAllPrimaryTlcs(
                        PageRequest.of(c.getPage(), c.getSize()), c.getGtin(), c.getGs1Batch()
                ), bffLotMapper::toBffLotDto
        );
    }

    @Override
    public String when(BffLotServiceCommands.CreatePrimaryTlc c) {
        BffLotDto lotDto = c.getPrimaryTlc();
        if (lotDto == null) {
            throw new IllegalArgumentException("Lot information can't be null");
        }
        if (lotDto.getLotId() != null && !lotDto.getLotId().isEmpty()) {
            if (lotApplicationService.get(lotDto.getLotId()) != null) {
                throw new IllegalArgumentException(String.format("Lot already exists: %s", lotDto.getLotId()));
            }
        }
        AbstractLotCommand.SimpleCreateLot createLot = bffLotMapper.toCreateLot(lotDto);
        createLot.setLotId(lotDto.getLotId() != null && !lotDto.getLotId().isEmpty() ? lotDto.getLotId() : IdUtils.randomId());
        createLot.setActive(IndicatorUtils.asIndicatorDefaultYes(lotDto.getActive())); // 将前端传入的 active 规范化
        createLot.setCommandId(c.getCommandId() != null ? c.getCommandId() : createLot.getLotId());
        createLot.setRequesterId(c.getRequesterId());
        createLot.setLotTypeId(LOT_TYPE_ID_PRIMARY_TLC);

        LotIdentificationCommand.CreateLotIdentification createLotIdentification = createLot.newCreateLotIdentification();
        createLotIdentification.setLotIdentificationTypeId(LOT_IDENTIFICATION_TYPE_TLC_CASE_GTIN_BATCH);
        createLotIdentification.setGtin(lotDto.getGtin());
        createLotIdentification.setGs1Batch(lotDto.getGs1Batch());
        createLotIdentification.setIdValue(formatTlcCaseGtinAndBatch(lotDto.getGtin(), lotDto.getGs1Batch()));
        createLot.getCreateLotIdentificationCommands().add(createLotIdentification);

        lotApplicationService.when(createLot);
        return createLot.getLotId();
    }

    @Override
    public void when(BffLotServiceCommands.ConvertToPrimaryTlc c) {
        String lotId = c.getLotId();
        BffLotDto lotDto = c.getPrimaryTlc();

        if (lotDto == null) {
            throw new IllegalArgumentException("Lot information can't be null");
        }

        // 检查lot是否存在
        LotState lotState = lotApplicationService.get(lotId);
        if (lotState == null) {
            throw new IllegalArgumentException("Lot not found: " + lotId);
        }

        // 创建更新命令
        AbstractLotCommand.SimpleMergePatchLot mergePatchLot = bffLotMapper.toMergePatchLot(lotDto);
        mergePatchLot.setLotId(lotId);
        mergePatchLot.setVersion(lotState.getVersion());
        mergePatchLot.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchLot.setRequesterId(c.getRequesterId());

        // 设置为Primary TLC类型
        mergePatchLot.setLotTypeId(LOT_TYPE_ID_PRIMARY_TLC);

        Optional<LotIdentificationState> lotIdentificationState = lotState.getLotIdentifications().stream()
                .filter(x -> LOT_IDENTIFICATION_TYPE_TLC_CASE_GTIN_BATCH.equals(x.getLotIdentificationTypeId()))
                .findAny();
        if (lotIdentificationState.isPresent()) {
            LotIdentificationCommand.MergePatchLotIdentification updateLotIdentification = mergePatchLot.newMergePatchLotIdentification();
            updateLotIdentification.setLotIdentificationTypeId(LOT_IDENTIFICATION_TYPE_TLC_CASE_GTIN_BATCH);
            updateLotIdentification.setGtin(lotDto.getGtin());
            updateLotIdentification.setGs1Batch(lotDto.getGs1Batch());
            updateLotIdentification.setIdValue(formatTlcCaseGtinAndBatch(lotDto.getGtin(), lotDto.getGs1Batch()));
            mergePatchLot.getLotIdentificationCommands().add(updateLotIdentification);
        } else {
            LotIdentificationCommand.CreateLotIdentification createLotIdentification = mergePatchLot.newCreateLotIdentification();
            createLotIdentification.setLotIdentificationTypeId(LOT_IDENTIFICATION_TYPE_TLC_CASE_GTIN_BATCH);
            createLotIdentification.setGtin(lotDto.getGtin());
            createLotIdentification.setGs1Batch(lotDto.getGs1Batch());
            createLotIdentification.setIdValue(formatTlcCaseGtinAndBatch(lotDto.getGtin(), lotDto.getGs1Batch()));
            mergePatchLot.getLotIdentificationCommands().add(createLotIdentification);
        }

        // 执行更新
        lotApplicationService.when(mergePatchLot);
    }
}
