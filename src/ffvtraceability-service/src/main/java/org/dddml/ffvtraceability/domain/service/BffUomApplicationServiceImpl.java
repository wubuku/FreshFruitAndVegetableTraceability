package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffUomDto;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.stereotype.Service;

@Service
public class BffUomApplicationServiceImpl implements BffUomApplicationService {
    @Override
    public Page<BffUomDto> when(BffUomServiceCommands.GetUnitsOfMeasure c) {
        return null;
    }

    @Override
    public BffUomDto when(BffUomServiceCommands.GetUnitOfMeasure c) {
        return null;
    }

    @Override
    public String when(BffUomServiceCommands.CreateUnitOfMeasure c) {
        return null;//todo
    }

    @Override
    public void when(BffUomServiceCommands.UpdateUnitOfMeasure c) {

    }

    @Override
    public void when(BffUomServiceCommands.ActivateUnitOfMeasure c) {

    }
}
