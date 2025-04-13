package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffRawItemInventoryDetailGroupDto;
import org.dddml.ffvtraceability.domain.BffRawItemInventoryGroupDto;
import org.dddml.ffvtraceability.domain.BffRawItemInventoryItemDto;
import org.dddml.ffvtraceability.domain.BffWipInventoryGroupDto;
import org.dddml.ffvtraceability.domain.mapper.BffInventoryItemMapper;
import org.dddml.ffvtraceability.domain.repository.BffInventoryItemRepository;
import org.dddml.ffvtraceability.domain.util.PageUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static org.dddml.ffvtraceability.domain.constants.BffProductConstants.PRODUCT_TYPES_WIP;

@Service
public class BffInventoryItemApplicationServiceImpl implements BffInventoryItemApplicationService {
    @Autowired
    private BffInventoryItemRepository bffInventoryItemRepository;
    @Autowired
    private BffInventoryItemMapper bffInventoryItemMapper;

    @Override
    public Page<BffRawItemInventoryGroupDto> when(BffInventoryItemServiceCommands.GetRawItemInventories c) {
        return PageUtils.toPage(
                bffInventoryItemRepository.findAllRawItemInventories(PageRequest.of(c.getPage(), c.getSize()),
                        c.getProductId(), c.getProductName(), c.getSupplierId(), c.getFacilityId()),
                bffInventoryItemMapper::toBffRawItemInventoryGroupDto);
    }

    @Override
    public Page<BffWipInventoryGroupDto> when(BffInventoryItemServiceCommands.GetWipInventories c) {
        if (c.getProductTypeId() == null || c.getProductTypeId().isBlank()) {
            throw new IllegalArgumentException("Product type is required");
        }
        if (!PRODUCT_TYPES_WIP.contains(c.getProductTypeId())) {
            throw new IllegalArgumentException("Product type is not supported");
        }
        return PageUtils.toPage(
                bffInventoryItemRepository.findAllWipInventories(PageRequest.of(c.getPage(), c.getSize()),
                        c.getProductTypeId(), c.getProductId(), c.getProductName(), c.getFacilityId()),
                bffInventoryItemMapper::toBffWipInventoryGroupDto);
    }

    @Override
    public Page<BffRawItemInventoryItemDto> when(BffInventoryItemServiceCommands.GetRawItemInventoryItems c) {
        return PageUtils.toPage(
                bffInventoryItemRepository.findRawItemInventoryItems(PageRequest.of(c.getPage(), c.getSize()),
                        c.getProductId(), c.getSupplierId(), c.getFacilityId()),
                bffInventoryItemMapper::toBffRawItemInventoryItemDto);
    }
}
