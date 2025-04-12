package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffRawItemInventoryGroupDto;
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
    public Page<BffRawItemInventoryGroupDto> when(BffInventoryItemServiceCommands.GetInventoryItems c) {
        return PageUtils.toPage(
                bffInventoryItemRepository.findAllRawItemInventories(PageRequest.of(c.getPage(), c.getSize()),
                        c.getProductId(), c.getProductName(), c.getSupplierId(), c.getFacilityId()),
                bffInventoryItemMapper::toBffRawItemInventoryGroupDto);
    }

}
