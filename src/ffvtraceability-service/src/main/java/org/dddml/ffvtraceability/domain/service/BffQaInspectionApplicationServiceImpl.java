package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffQaInspectionDto;
import org.dddml.ffvtraceability.domain.mapper.BffQaInspectionMapper;
import org.dddml.ffvtraceability.domain.qainspection.AbstractQaInspectionCommand;
import org.dddml.ffvtraceability.domain.qainspection.QaInspectionApplicationService;
import org.dddml.ffvtraceability.domain.qainspection.QaInspectionState;
import org.dddml.ffvtraceability.domain.repository.BffQaInspectionProjection;
import org.dddml.ffvtraceability.domain.repository.BffQaInspectionRepository;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptApplicationService;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Autowired
    private ShipmentReceiptApplicationService shipmentReceiptApplicationService;

    public static String getQaInspectionActionByStatusId(String statusId) {
        return switch (statusId) {
            case STATUS_ID_APPROVED -> "Approve";
            case STATUS_ID_REJECTED -> "Reject";
            case STATUS_ID_ON_HOLD -> "Hold";
            default -> throw new IllegalArgumentException("Invalid statusId: " + statusId);
        };
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
        // NOTE: ReceiptId 不能为空，如果 QaInspectionId为 null，那么使之等于 ReceiptId
        //（按照当前的场景，这样做可以简化后续的处理逻辑）
        BffQaInspectionDto inspectionDto = c.getQaInspection();
        String receiptId = inspectionDto.getReceiptId();
        if (receiptId != null) {
            receiptId = receiptId.trim();
        }
        if (receiptId == null || receiptId.isEmpty()) {
            throw new IllegalArgumentException("ReceiptId can't be null");
        }
        ShipmentReceiptState shipmentReceiptState = shipmentReceiptApplicationService.get(receiptId);
        if (shipmentReceiptState == null) {
            throw new IllegalArgumentException("ReceiptId为" + receiptId + "的收货信息不存在");
        }
        String qaInspectionId = inspectionDto.getQaInspectionId();
        if (qaInspectionId != null) {
            qaInspectionId = qaInspectionId.trim();
            if (qaInspectionApplicationService.get(qaInspectionId) != null) {
                throw new IllegalArgumentException("QaInspectionId 为 " + qaInspectionId + " 的记录已经存在");
            }
        } else {
            qaInspectionId = receiptId;//IdUtils.randomId();
        }
//        if (inspectionDto.getReceiptId() != null && !inspectionDto.getReceiptId().isEmpty()) {
//            if (inspectionDto.getQaInspectionId() != null
//                    && !inspectionDto.getQaInspectionId().equals(inspectionDto.getReceiptId())) {
//                throw new IllegalArgumentException("When receiptId is not empty, qaInspectionId must be null or equal to receiptId. ReceiptId: "
//                        + inspectionDto.getReceiptId() + ", QaInspectionId: " + inspectionDto.getQaInspectionId());
//            }
//        }

        if (!AVAILABLE_STATUS_IDS.contains(c.getQaInspection().getStatusId())) {
            throw new IllegalArgumentException("Invalid statusId: " + c.getQaInspection().getStatusId());
        }
        AbstractQaInspectionCommand.SimpleCreateQaInspection createQaInspection = new AbstractQaInspectionCommand.SimpleCreateQaInspection();
//        createQaInspection.setQaInspectionId(
//                c.getQaInspection().getQaInspectionId() != null ? c.getQaInspection().getQaInspectionId()
//                        : (c.getQaInspection().getReceiptId() != null ? c.getQaInspection().getReceiptId()
//                        : IdUtils.randomId()
//                )
//        );
        createQaInspection.setQaInspectionId(qaInspectionId);
        createQaInspection.setComments(c.getQaInspection().getComments());
        createQaInspection.setQaInspectionAction(getQaInspectionActionByStatusId(c.getQaInspection().getStatusId()));
        createQaInspection.setInspectedBy(c.getRequesterId());
        createQaInspection.setInspectedAt(OffsetDateTime.now());
        createQaInspection.setReceiptId(c.getQaInspection().getReceiptId());
        createQaInspection.setCommandId(c.getCommandId() != null ? c.getCommandId() : createQaInspection.getQaInspectionId());
        createQaInspection.setRequesterId(c.getRequesterId());
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
        //这里并没有修改mergePatchQaInspection.setReceiptId();
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
        //确保前端传过来的质检数组中的ReceiptId不能相互重复
        List<String> receiptIds = new ArrayList<>();
        Arrays.stream(c.getQaInspections()).forEach(bffQaInspectionDto -> {
            String receiptId = bffQaInspectionDto.getReceiptId();
            if (receiptId != null) {
                receiptId = receiptId.trim();
                bffQaInspectionDto.setReceiptId(receiptId);
            }
            if (receiptId == null || receiptId.isEmpty()) {
                throw new IllegalArgumentException("ReceiptId can't be null");
            }
            if (receiptIds.contains(receiptId)) {
                throw new IllegalArgumentException("重复的ReceiptId");
            }
            receiptIds.add(receiptId);
        });
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
        //NOTE：这个方法既能同时批量添加又能批量修改，还能批量部分修改部分添加
        //所以我们设定：如果提供了qaInspectionId为修改，而没有提供qaInspectionId，提供了receiptId则为添加
        List<BffQaInspectionDto> needToAdded = new ArrayList<>();
        List<BffQaInspectionDto> needToUpdated = new ArrayList<>();
        for (BffQaInspectionDto qaInspection : c.getQaInspections()) {
            String qaInspectionId = qaInspection.getQaInspectionId();
            if (qaInspectionId != null) {
                qaInspectionId = qaInspectionId.trim();
                qaInspection.setQaInspectionId(qaInspectionId);
            }
            String receiptId = qaInspection.getReceiptId();
            if (receiptId != null) {
                receiptId = receiptId.trim();
                qaInspection.setReceiptId(receiptId);
            }
            //有qaInspectionId->修改/无有qaInspectionId有receiptId->添加
            if (qaInspectionId != null && !qaInspectionId.isEmpty()) {
                needToUpdated.add(qaInspection);
            } else if (receiptId != null && !receiptId.isEmpty()) {
                needToAdded.add(qaInspection);
            } else {
                throw new IllegalArgumentException("QaInspectionId and ReceiptId can't both be null.");
            }
        }
        //修改
        List<String> qaInspectionIds = new ArrayList<>();
        needToUpdated.forEach(bffQaInspectionDto -> {
            if (qaInspectionIds.contains(bffQaInspectionDto.getQaInspectionId())) {
                throw new IllegalArgumentException("Duplicate QaInspectionId:" + bffQaInspectionDto.getQaInspectionId());
            }
            qaInspectionIds.add(bffQaInspectionDto.getQaInspectionId());
        });
        for (BffQaInspectionDto qaInspection : needToUpdated) {
            // 创建 UpdateQaInspection 命令
            BffQaInspectionServiceCommands.UpdateQaInspection updateCommand =
                    new BffQaInspectionServiceCommands.UpdateQaInspection();
            // 设置质检信息
            updateCommand.setQaInspection(qaInspection);
            updateCommand.setRequesterId(c.getRequesterId());
            updateCommand.setCommandId(UUID.randomUUID().toString());
            // 调用已有的更新方法
            when(updateCommand);
        }
        //添加
        List<String> receiptIds = new ArrayList<>();
        needToAdded.forEach(bffQaInspectionDto -> {
            if (receiptIds.contains(bffQaInspectionDto.getReceiptId())) {
                throw new IllegalArgumentException("Duplicate receiptId:" + bffQaInspectionDto.getReceiptId());
            }
            receiptIds.add(bffQaInspectionDto.getReceiptId());
        });
        for (BffQaInspectionDto qaInspection : needToAdded) {
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
