package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.inventoryitem.*;
import org.dddml.ffvtraceability.domain.inventorytransfer.AbstractInventoryTransferCommand;
import org.dddml.ffvtraceability.domain.inventorytransfer.InventoryTransferApplicationService;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.specialization.DomainError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.dddml.ffvtraceability.domain.constants.BffStockInOutConstants.INVENTORY_TRANSFER_IN;
import static org.dddml.ffvtraceability.domain.constants.BffStockInOutConstants.INVENTORY_TRANSFER_OUT;

@Service
public class BffInventoryTransferApplicationServiceImpl implements BffInventoryTransferApplicationService {
    @Autowired
    private InventoryItemApplicationService inventoryItemApplicationService;
    @Autowired
    private InventoryTransferApplicationService inventoryTransferApplicationService;

    @Override
    @Transactional
    public String when(BffInventoryTransferServiceCommands.LocationAdjustment c) {
        BigDecimal adjustmentQty = c.getAdjustmentQuantity();
        // 防御性校验（空值 + 非正数）
        if (adjustmentQty == null || adjustmentQty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainError("Adjustment quantity must be greater than 0");
        }
        if (c.getFacilityIdTo() == null || c.getFacilityIdTo().isBlank()) {
            throw new DomainError("Warehouse can't be null");
        }
        if (c.getLocationSeqIdTo() == null || c.getLocationSeqIdTo().isBlank()) {
            throw new DomainError("Location can't be null");
        }
        InventoryItemState inventoryItemState = inventoryItemApplicationService.get(c.getInventoryItemId());
        if (inventoryItemState == null) {
            throw new DomainError("Inventory not found");
        }
        if (inventoryItemState.getQuantityOnHandTotal().compareTo(c.getAdjustmentQuantity()) < 0) {
            throw new DomainError("Inventory quantity not enough");
        }
        if (inventoryItemState.getFacilityId().equals(c.getFacilityIdTo()) && inventoryItemState.getLocationSeqId().equals(c.getLocationSeqIdTo())) {
            throw new DomainError("Inventory location can't be the same");
        }
        OffsetDateTime now = OffsetDateTime.now();
        AbstractInventoryTransferCommand.SimpleCreateInventoryTransfer createInventoryTransfer = new AbstractInventoryTransferCommand.SimpleCreateInventoryTransfer();
        createInventoryTransfer.setInventoryTransferId(IdUtils.randomId());
        createInventoryTransfer.setInventoryItemId(c.getInventoryItemId());
        createInventoryTransfer.setComments(c.getComments());
        createInventoryTransfer.setFacilityId(inventoryItemState.getFacilityId());
        createInventoryTransfer.setLocationSeqId(inventoryItemState.getLocationSeqId());
        createInventoryTransfer.setFacilityIdTo(c.getFacilityIdTo());
        createInventoryTransfer.setLocationSeqIdTo(c.getLocationSeqIdTo());
        createInventoryTransfer.setSendDate(now);
        createInventoryTransfer.setReceiveDate(now);
        createInventoryTransfer.setRequesterId(c.getRequesterId());
        inventoryTransferApplicationService.when(createInventoryTransfer);

        //先源库原仓出库
        InventoryItemCommands.RecordInventoryEntry recordInventoryEntry = new InventoryItemCommands.RecordInventoryEntry();
        InventoryItemAttributes inventoryItemAttributes = new InventoryItemAttributes();
        inventoryItemAttributes.setLotId(inventoryItemState.getLotId());
        inventoryItemAttributes.setProductId(inventoryItemState.getProductId());
        inventoryItemAttributes.setFacilityId(inventoryItemState.getFacilityId());
        inventoryItemAttributes.setLocationSeqId(inventoryItemState.getLocationSeqId());

        InventoryItemDetailAttributes inventoryItemDetailAttributes = new InventoryItemDetailAttributes();
        inventoryItemDetailAttributes.setInventoryTransferId(createInventoryTransfer.getInventoryTransferId());
        inventoryItemDetailAttributes.setReasonEnumId(INVENTORY_TRANSFER_OUT);

        recordInventoryEntry.setQuantityOnHandDiff(c.getAdjustmentQuantity().negate());

        recordInventoryEntry.setInventoryItemAttributes(inventoryItemAttributes);
        recordInventoryEntry.setInventoryItemDetailAttributes(inventoryItemDetailAttributes);
        recordInventoryEntry.setRequesterId(c.getRequesterId());
        recordInventoryEntry.setCommandId(UUID.randomUUID().toString());
        inventoryItemApplicationService.when(recordInventoryEntry);

        //再从目的仓库目的仓位入库
        InventoryItemCommands.RecordInventoryEntry recordInventoryEntryIn = new InventoryItemCommands.RecordInventoryEntry();
        InventoryItemAttributes inventoryItemAttributesIn = new InventoryItemAttributes();
        inventoryItemAttributesIn.setLotId(inventoryItemState.getLotId());
        inventoryItemAttributesIn.setProductId(inventoryItemState.getProductId());
        inventoryItemAttributesIn.setFacilityId(c.getFacilityIdTo());
        inventoryItemAttributesIn.setLocationSeqId(c.getLocationSeqIdTo());

        InventoryItemDetailAttributes inventoryItemDetailAttributesIn = new InventoryItemDetailAttributes();
        inventoryItemDetailAttributesIn.setInventoryTransferId(createInventoryTransfer.getInventoryTransferId());
        inventoryItemDetailAttributesIn.setReasonEnumId(INVENTORY_TRANSFER_IN);

        recordInventoryEntryIn.setQuantityOnHandDiff(c.getAdjustmentQuantity());

        recordInventoryEntryIn.setInventoryItemAttributes(inventoryItemAttributesIn);
        recordInventoryEntryIn.setInventoryItemDetailAttributes(inventoryItemDetailAttributesIn);
        recordInventoryEntryIn.setRequesterId(c.getRequesterId());
        recordInventoryEntryIn.setCommandId(UUID.randomUUID().toString());
        inventoryItemApplicationService.when(recordInventoryEntryIn);

        return createInventoryTransfer.getInventoryTransferId();
    }
}
