package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffInventoryItemDetailDto;
import org.dddml.ffvtraceability.domain.BffInventoryItemDto;
import org.dddml.ffvtraceability.domain.BffInventoryItemGroupDto;
import org.dddml.ffvtraceability.domain.mapper.BffInventoryItemMapper;
import org.dddml.ffvtraceability.domain.repository.BffInventoryItemRepository;
import org.dddml.ffvtraceability.domain.util.PageUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class BffInventoryItemApplicationServiceImpl implements BffInventoryItemApplicationService {
    @Autowired
    private BffInventoryItemRepository bffInventoryItemRepository;
    @Autowired
    private BffInventoryItemMapper bffInventoryItemMapper;

    @Override
    public Page<BffInventoryItemGroupDto> when(BffInventoryItemServiceCommands.GetInventoryItems c) {
        return PageUtils.toPage(
                bffInventoryItemRepository.findAllInventoryItems(PageRequest.of(c.getPage(), c.getSize()),
                        c.getProductTypeId(), c.getProductId(), c.getSupplierId(), c.getFacilityId()),
                bffInventoryItemMapper::toBffInventoryItemGroupDto);
    }

    @Override
    public BffInventoryItemDto when(BffInventoryItemServiceCommands.GetInventoryItem c) {
        return null;
    }

    @Override
    public BffInventoryItemDetailDto when(BffInventoryItemServiceCommands.GetInventoryItemDetail c) {
        return null;
    }
}
