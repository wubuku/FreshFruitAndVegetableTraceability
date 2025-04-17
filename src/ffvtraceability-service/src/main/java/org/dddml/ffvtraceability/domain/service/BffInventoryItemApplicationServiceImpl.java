package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.mapper.BffInventoryItemMapper;
import org.dddml.ffvtraceability.domain.repository.BffInventoryItemRepository;
import org.dddml.ffvtraceability.domain.util.PageUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;

import static org.dddml.ffvtraceability.domain.constants.BffProductConstants.PRODUCT_TYPES_NOT_RAW;

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
    public Page<BffProductInventoryGroupDto> when(BffInventoryItemServiceCommands.GetProductInventories c) {
        if (c.getProductTypeId() == null || c.getProductTypeId().isBlank()) {
            throw new IllegalArgumentException("Product type is required");
        }
        if (!PRODUCT_TYPES_NOT_RAW.contains(c.getProductTypeId())) {
            throw new IllegalArgumentException("Product type is not supported");
        }
        return PageUtils.toPage(
                bffInventoryItemRepository.findAllProductInventories(PageRequest.of(c.getPage(), c.getSize()),
                        c.getProductTypeId(), c.getProductId(), c.getProductName(), c.getFacilityId()),
                bffInventoryItemMapper::toBffProductInventoryGroupDto);
    }

    @Override
    public Page<BffInventoryItemDto> when(BffInventoryItemServiceCommands.GetRawInventoriesByProductAndLot c) {
        return PageUtils.toPage(
                bffInventoryItemRepository.findRawItemInventories(PageRequest.of(c.getPage(), c.getSize()),
                        c.getProductId(), c.getLotId()),
                bffInventoryItemMapper::toBffInventoryItemDto);
    }

    @Override
    public Page<BffRawItemInventoryItemDto> when(BffInventoryItemServiceCommands.GetRawItemInventoryItems c) {
        Page<BffRawItemInventoryItemDto> page = PageUtils.toPage(
                bffInventoryItemRepository.findRawItemInventoryItems(PageRequest.of(c.getPage(), c.getSize()),
                        c.getProductId(), c.getSupplierId(), c.getFacilityId()),
                bffInventoryItemMapper::toBffRawItemInventoryItemDto);
        page.getContent().forEach(item -> {
            bffInventoryItemRepository.getInventoryItemReceiving(item.getProductId(), item.getLotId()).ifPresent(receiving -> {
                item.setReceivedAt(receiving.getReceivedAt().atOffset(ZoneOffset.UTC));
                item.setOrderId(receiving.getOrderId());
                item.setQaStatusId(receiving.getQaStatusId());
                item.setReceivingDocumentId(receiving.getReceivingDocumentId());
            });
        });
        return page;
    }

    @Override
    public Page<BffInventoryItemDto> when(BffInventoryItemServiceCommands.GetProductInventoriesByProductAndLot c) {
        return PageUtils.toPage(
                bffInventoryItemRepository.findProductInventories(PageRequest.of(c.getPage(), c.getSize()),
                        c.getProductId(), c.getLotId()),
                bffInventoryItemMapper::toBffInventoryItemDto);
    }

    @Override
    public Page<BffRawItemInventoryItemDto> when(BffInventoryItemServiceCommands.GetRawItemInventoryDetails c) {
//        return PageUtils.toPage(
//                bffInventoryItemRepository.getRawItemInventoriesGroupByLot(PageRequest.of(c.getPage(), c.getSize()),
//                        c.getProductId(), c.getSupplierId(), c.getFacilityId()),
//                bffInventoryItemMapper::toBffInventoryByLotNoDto);
        return null;
    }

    @Override
    public Page<BffInventoryByLotNoDto> when(BffInventoryItemServiceCommands.GetRawItemInventoriesByLotNo c) {
        return PageUtils.toPage(
                bffInventoryItemRepository.getRawItemInventoriesGroupByLot(PageRequest.of(c.getPage(), c.getSize()),
                        c.getProductId(), c.getSupplierId(), c.getFacilityId()),
                bffInventoryItemMapper::toBffInventoryByLotNoDto);
    }
}
