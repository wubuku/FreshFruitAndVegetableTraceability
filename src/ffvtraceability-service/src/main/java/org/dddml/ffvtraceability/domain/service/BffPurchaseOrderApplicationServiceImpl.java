package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffPurchaseOrderDto;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.stereotype.Service;

@Service
public class BffPurchaseOrderApplicationServiceImpl implements BffPurchaseOrderApplicationService {
    @Override
    public Page<BffPurchaseOrderDto> when(BffPurchaseOrderServiceCommands.GetPurchaseOrderItems c) {
        return null;
    }

    @Override
    public BffPurchaseOrderDto when(BffPurchaseOrderServiceCommands.GetPurchaseOrder c) {
        return null;
    }
}
