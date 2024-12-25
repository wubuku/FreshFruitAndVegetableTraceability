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
        createQaInspection.setStatusId(c.getQaInspection().getStatusId());
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
        mergePatchQaInspection.setStatusId(c.getQaInspection().getStatusId());
        mergePatchQaInspection.setInspectedBy(c.getRequesterId());
        mergePatchQaInspection.setInspectedAt(OffsetDateTime.now());

        mergePatchQaInspection.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchQaInspection.setRequesterId(c.getRequesterId());

        qaInspectionApplicationService.when(mergePatchQaInspection);
    }
}
