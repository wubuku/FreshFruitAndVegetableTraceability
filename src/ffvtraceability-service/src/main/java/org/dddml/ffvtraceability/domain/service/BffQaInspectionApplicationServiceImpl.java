package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffQaInspectionDto;
import org.dddml.ffvtraceability.domain.mapper.BffQaInspectionMapper;
import org.dddml.ffvtraceability.domain.qainspection.AbstractQaInspectionCommand;
import org.dddml.ffvtraceability.domain.qainspection.QaInspectionApplicationService;
import org.dddml.ffvtraceability.domain.qainspection.QaInspectionState;
import org.dddml.ffvtraceability.domain.repository.BffQaInspectionProjection;
import org.dddml.ffvtraceability.domain.repository.BffQaInspectionRepository;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class BffQaInspectionApplicationServiceImpl implements BffQaInspectionApplicationService {
    public static final String STATUS_ID_APPROVED = "APPROVED";
    public static final String STATUS_ID_REJECTED = "REJECTED";
    public static final String STATUS_ID_ON_HOLD = "ON_HOLD";
    public static final List<String> AVAILABLE_STATUS_IDS = List.of(STATUS_ID_APPROVED, STATUS_ID_REJECTED, STATUS_ID_ON_HOLD);

    @Autowired
    private QaInspectionApplicationService qaInspectionApplicationService;
    @Autowired
    private BffQaInspectionRepository bffQaInspectionRepository;
    @Autowired
    private BffQaInspectionMapper bffQaInspectionMapper;

    public static String getQaInspectionActionByStatusId(String statusId) {
        switch (statusId) {
            case STATUS_ID_APPROVED:
                return "Approve";
            case STATUS_ID_REJECTED:
                return "Reject";
            case STATUS_ID_ON_HOLD:
                return "Hold";
            default:
                throw new IllegalArgumentException("Invalid statusId: " + statusId);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<BffQaInspectionDto> when(BffQaInspectionServiceCommands.GetQaInspections c) {
        List<BffQaInspectionProjection> projections = bffQaInspectionRepository
                .findQaInspections(c.getReceivingDocumentId(), c.getReceiptId());

        return projections.stream()
                .map(bffQaInspectionMapper::toBffQaInspectionDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public BffQaInspectionDto when(BffQaInspectionServiceCommands.GetQaInspection c) {
        QaInspectionState qaInspectionState = qaInspectionApplicationService.get(c.getQaInspectionId());
        if (qaInspectionState == null) {
            return null;
        }
        return bffQaInspectionMapper.toBffQaInspectionDto(qaInspectionState);
    }

    @Transactional
    @Override
    public String when(BffQaInspectionServiceCommands.CreateQaInspection c) {
        // NOTE: 当 ReceiptId 不为空时，要求 QaInspectionId 要么为 null，要么必须等于 ReceiptId？
        //（按照当前的场景，这样做可以简化后续的处理逻辑）
        BffQaInspectionDto inspectionDto = c.getQaInspection();
        if (inspectionDto.getReceiptId() != null && !inspectionDto.getReceiptId().isEmpty()) {
            if (inspectionDto.getQaInspectionId() != null
                    && !inspectionDto.getQaInspectionId().equals(inspectionDto.getReceiptId())) {
                throw new IllegalArgumentException("When receiptId is not empty, qaInspectionId must be null or equal to receiptId. ReceiptId: "
                        + inspectionDto.getReceiptId() + ", QaInspectionId: " + inspectionDto.getQaInspectionId());
            }
        }

        if (!AVAILABLE_STATUS_IDS.contains(c.getQaInspection().getStatusId())) {
            throw new IllegalArgumentException("Invalid statusId: " + c.getQaInspection().getStatusId());
        }
        AbstractQaInspectionCommand.SimpleCreateQaInspection createQaInspection = new AbstractQaInspectionCommand.SimpleCreateQaInspection();
        createQaInspection.setQaInspectionId(
                c.getQaInspection().getQaInspectionId() != null ? c.getQaInspection().getQaInspectionId()
                        : (c.getQaInspection().getReceiptId() != null ? c.getQaInspection().getReceiptId()
                        : IdUtils.randomId()
                )
        );
        createQaInspection.setComments(c.getQaInspection().getComments());
        createQaInspection.setQaInspectionAction(getQaInspectionActionByStatusId(c.getQaInspection().getStatusId()));
        createQaInspection.setInspectedBy(c.getRequesterId());
        createQaInspection.setInspectedAt(OffsetDateTime.now());
        createQaInspection.setReceiptId(c.getQaInspection().getReceiptId());
        createQaInspection.setCommandId(c.getCommandId() != null ? c.getCommandId() : createQaInspection.getQaInspectionId());
        createQaInspection.setRequesterId(c.getRequesterId());
        //createQaInspection.setInspectionFacilityId(); // Not used yet.
        //createQaInspection.setInspectionFacilityId(); // Not used yet.

        qaInspectionApplicationService.when(createQaInspection);
        return createQaInspection.getQaInspectionId();
    }

    @Transactional
    @Override
    public void when(BffQaInspectionServiceCommands.UpdateQaInspection c) {
        if (!AVAILABLE_STATUS_IDS.contains(c.getQaInspection().getStatusId())) {
            throw new IllegalArgumentException("Invalid statusId: " + c.getQaInspection().getStatusId());
        }

        QaInspectionState qaInspectionState = qaInspectionApplicationService.get(c.getQaInspectionId());
        if (qaInspectionState == null) {
            throw new IllegalArgumentException("QA Inspection not found: " + c.getQaInspectionId());
        }

        AbstractQaInspectionCommand.SimpleMergePatchQaInspection mergePatchQaInspection =
                new AbstractQaInspectionCommand.SimpleMergePatchQaInspection();
        mergePatchQaInspection.setQaInspectionId(c.getQaInspectionId());
        mergePatchQaInspection.setVersion(qaInspectionState.getVersion());
        mergePatchQaInspection.setComments(c.getQaInspection().getComments());
        mergePatchQaInspection.setQaInspectionAction(getQaInspectionActionByStatusId(c.getQaInspection().getStatusId()));
        mergePatchQaInspection.setInspectedBy(c.getRequesterId());
        mergePatchQaInspection.setInspectedAt(OffsetDateTime.now());

        mergePatchQaInspection.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchQaInspection.setRequesterId(c.getRequesterId());

        qaInspectionApplicationService.when(mergePatchQaInspection);
    }

    @Override
    @Transactional
    public void when(BffQaInspectionServiceCommands.BatchAddQaInspections c) {
        if (c.getQaInspections() == null) {
            return;
        }

        for (BffQaInspectionDto qaInspection : c.getQaInspections()) {
            // 创建 CreateQaInspection 命令
            BffQaInspectionServiceCommands.CreateQaInspection createCommand =
                    new BffQaInspectionServiceCommands.CreateQaInspection();
            // 设置质检信息
            createCommand.setQaInspection(qaInspection);
            createCommand.setRequesterId(c.getRequesterId());
            createCommand.setCommandId(UUID.randomUUID().toString());
            // 调用已有的创建方法
            when(createCommand);
        }
    }

    @Override
    @Transactional
    public void when(BffQaInspectionServiceCommands.BatchAddOrUpdateQaInspections c) {
        for (BffQaInspectionDto qaInspection : c.getQaInspections()) {
            boolean exists = false;
            if (qaInspection.getQaInspectionId() != null) {
                QaInspectionState qaInspectionState = qaInspectionApplicationService.get(qaInspection.getQaInspectionId());
                if (qaInspectionState != null) {
                    exists = true;
                }
                if (exists) {
                    // 创建 UpdateQaInspection 命令
                    BffQaInspectionServiceCommands.UpdateQaInspection updateCommand =
                            new BffQaInspectionServiceCommands.UpdateQaInspection();
                    // 设置质检信息
                    updateCommand.setQaInspection(qaInspection);
                    updateCommand.setRequesterId(c.getRequesterId());
                    updateCommand.setCommandId(UUID.randomUUID().toString());
                    // 调用已有的更新方法
                    when(updateCommand);
                } else {
                    // 创建 CreateQaInspection 命令
                    BffQaInspectionServiceCommands.CreateQaInspection createCommand =
                            new BffQaInspectionServiceCommands.CreateQaInspection();
                    // 设置质检信息
                    createCommand.setQaInspection(qaInspection);
                    createCommand.setRequesterId(c.getRequesterId());
                    createCommand.setCommandId(UUID.randomUUID().toString());
                    // 调用已有的创建方法
                    when(createCommand);
                }
            }
        }
    }
}
