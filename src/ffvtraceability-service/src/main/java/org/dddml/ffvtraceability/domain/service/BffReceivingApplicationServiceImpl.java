package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffReceivingDocumentDto;
import org.dddml.ffvtraceability.domain.BffReceivingItemDto;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.stereotype.Service;

@Service
public class BffReceivingApplicationServiceImpl implements BffReceivingApplicationService {
    @Override
    public Page<BffReceivingDocumentDto> when(BffReceivingServiceCommands.GetReceivingDocuments c) {
        return null;
    }

    @Override
    public BffReceivingDocumentDto when(BffReceivingServiceCommands.GetReceivingDocument c) {
        return null;
    }

    @Override
    public BffReceivingItemDto when(BffReceivingServiceCommands.GetReceivingItem c) {
        return null;
    }

    @Override
    public void when(BffReceivingServiceCommands.CreateReceivingDocument c) {

    }

    @Override
    public void when(BffReceivingServiceCommands.UpdateReceivingPrimaryOrderId c) {

    }

    @Override
    public void when(BffReceivingServiceCommands.UpdateReceivingReferenceDocuments c) {

    }

    @Override
    public void when(BffReceivingServiceCommands.CreateReceivingItem c) {

    }

    @Override
    public void when(BffReceivingServiceCommands.DeleteReceivingItem c) {

    }

    @Override
    public void when(BffReceivingServiceCommands.UpdateReceivingItem c) {

    }
}
