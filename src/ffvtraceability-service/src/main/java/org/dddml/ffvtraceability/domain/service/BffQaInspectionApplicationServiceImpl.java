package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffQaInspectionDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BffQaInspectionApplicationServiceImpl implements  BffQaInspectionApplicationService {
    @Override
    public Iterable<BffQaInspectionDto> when(BffQaInspectionServiceCommands.GetQaInspections c) {
        return null;
    }

    @Override
    public BffQaInspectionDto when(BffQaInspectionServiceCommands.GetQaInspection c) {
        return null;
    }

    @Override
    public String when(BffQaInspectionServiceCommands.CreateQaInspection c) {
        return "";
    }

    @Override
    public void when(BffQaInspectionServiceCommands.UpdateQaInspection c) {

    }
}
