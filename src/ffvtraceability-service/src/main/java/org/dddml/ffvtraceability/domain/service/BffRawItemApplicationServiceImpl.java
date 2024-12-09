package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffRawItemDto;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.stereotype.Service;

@Service
public class BffRawItemApplicationServiceImpl implements BffRawItemApplicationService {
    @Override
    public Page<BffRawItemDto> when(BffRawItemServiceCommands.GetRawItems c) {
        return null;
    }

    @Override
    public BffRawItemDto when(BffRawItemServiceCommands.GetRawItem c) {
        return null;
    }

    @Override
    public void when(BffRawItemServiceCommands.CreateRawItem c) {

    }

    @Override
    public void when(BffRawItemServiceCommands.UpdateRawItem c) {

    }

    @Override
    public void when(BffRawItemServiceCommands.ActivateRawItem c) {

    }

    @Override
    public void when(BffRawItemServiceCommands.BatchAddRawItems c) {

    }
}
