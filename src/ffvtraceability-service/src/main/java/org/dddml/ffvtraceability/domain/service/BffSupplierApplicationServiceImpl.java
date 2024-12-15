package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffSupplierDto;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BffSupplierApplicationServiceImpl implements BffSupplierApplicationService {
    @Override
    public Page<BffSupplierDto> when(BffSupplierServiceCommands.GetSuppliers c) {
        return null;
    }

    @Override
    public BffSupplierDto when(BffSupplierServiceCommands.GetSupplier c) {
        return null;
    }

    @Override
    public void when(BffSupplierServiceCommands.CreateSupplier c) {

    }

    @Override
    public void when(BffSupplierServiceCommands.UpdateSupplier c) {

    }

    @Override
    public void when(BffSupplierServiceCommands.ActivateSupplier c) {

    }
}
