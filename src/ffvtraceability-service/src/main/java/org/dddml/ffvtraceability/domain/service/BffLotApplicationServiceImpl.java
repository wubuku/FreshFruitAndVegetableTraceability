package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffLotDto;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.stereotype.Service;

@Service
public class BffLotApplicationServiceImpl implements BffLotApplicationService {
    @Override
    public Page<BffLotDto> when(BffLotServiceCommands.GetLots c) {
        return null;
    }

    @Override
    public BffLotDto when(BffLotServiceCommands.GetLot c) {
        return null;
    }

    @Override
    public void when(BffLotServiceCommands.CreateLot c) {

    }

    @Override
    public void when(BffLotServiceCommands.UpdateLot c) {

    }

    @Override
    public void when(BffLotServiceCommands.ActivateLot c) {

    }
}
