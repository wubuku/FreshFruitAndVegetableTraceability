package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffShipmentBoxTypeDto;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BffShipmentBoxTypeApplicationServiceImpl implements BffShipmentBoxTypeApplicationService {

    @Override
    @Transactional(readOnly = true)
    public Page<BffShipmentBoxTypeDto> when(BffShipmentBoxTypeServiceCommands.GetShipmentBoxTypes c) {
        return null; //todo
    }

    @Override
    @Transactional(readOnly = true)
    public BffShipmentBoxTypeDto when(BffShipmentBoxTypeServiceCommands.GetShipmentBoxType c) {
        return null; //todo
    }

    @Override
    public String when(BffShipmentBoxTypeServiceCommands.CreateShipmentBoxType c) {
        return ""; //todo
    }

    @Override
    public void when(BffShipmentBoxTypeServiceCommands.UpdateShipmentBoxType c) {
        //todo
    }

    @Override
    public void when(BffShipmentBoxTypeServiceCommands.ActivateShipmentBoxType c) {
        //todo
    }

    @Override
    public void when(BffShipmentBoxTypeServiceCommands.BatchAddShipmentBoxTypes c) {
        //todo
    }
}
