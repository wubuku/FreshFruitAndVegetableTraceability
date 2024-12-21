package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffPurchaseOrderDto;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BffPurchaseOrderApplicationServiceImpl implements BffPurchaseOrderApplicationService {
    @Override
    @Transactional(readOnly = true)
    public Page<BffPurchaseOrderDto> when(BffPurchaseOrderServiceCommands.GetPurchaseOrderItems c) {
        return null; // todo
    }

    @Override
    @Transactional(readOnly = true)
    public BffPurchaseOrderDto when(BffPurchaseOrderServiceCommands.GetPurchaseOrder c) {
        return null; //todo
    }
}
