package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.inventoryitem.*;
import org.dddml.ffvtraceability.domain.mapper.BffInventoryItemMapper;
import org.dddml.ffvtraceability.domain.physicalinventory.AbstractPhysicalInventoryCommand;
import org.dddml.ffvtraceability.domain.physicalinventory.InventoryItemVarianceCommand;
import org.dddml.ffvtraceability.domain.physicalinventory.PhysicalInventoryApplicationService;
import org.dddml.ffvtraceability.domain.repository.BffFacilityLocationRepository;
import org.dddml.ffvtraceability.domain.repository.BffInventoryItemRepository;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.specialization.DomainError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.dddml.ffvtraceability.domain.constants.BffStockInOutConstants.VARIANCE_REASON_TYPE_QUANTITY_ADJUSTMENT;

@Service
public class BffPhysicalInventoryApplicationServiceImpl implements BffPhysicalInventoryApplicationService {

    @Autowired
    private InventoryItemApplicationService inventoryItemApplicationService;
    @Autowired
    private BffInventoryItemRepository bffInventoryItemRepository;
    @Autowired
    private BffFacilityLocationRepository bffFacilityLocationRepository;
    @Autowired
    private BffInventoryItemMapper bffInventoryItemMapper;
    @Autowired
    private PhysicalInventoryApplicationService physicalInventoryApplicationService;

    @Override
    @Transactional
    public String when(BffPhysicalInventoryServiceCommands.CreatePhysicalInventory c) {
        if (c.getPhysicalInventory() == null || c.getPhysicalInventory().getVariances().isEmpty()) {
            throw new DomainError("Variance can't be empty");
        }
        OffsetDateTime now = OffsetDateTime.now();
        AbstractPhysicalInventoryCommand.SimpleCreatePhysicalInventory createPhysicalInventory = new AbstractPhysicalInventoryCommand.SimpleCreatePhysicalInventory();
        createPhysicalInventory.setPhysicalInventoryId(IdUtils.randomId());
        createPhysicalInventory.setPhysicalInventoryDate(now);
        createPhysicalInventory.setGeneralComments(c.getPhysicalInventory().getGeneralComments());
        createPhysicalInventory.setCommandId(c.getCommandId() == null ? createPhysicalInventory.getPhysicalInventoryId() : c.getCommandId());
        createPhysicalInventory.setRequesterId(c.getRequesterId());
        c.getPhysicalInventory().getVariances().forEach(variance -> {
            InventoryItemState inventoryItemState = inventoryItemApplicationService.get(variance.getInventoryItemId());
            if (inventoryItemState == null) {
                throw new DomainError("Inventory not found");
            }
            if (variance.getQuantityOnHandVar() == null) {
                throw new DomainError("Quantity can't be null");
            }
            if (variance.getQuantityOnHandVar().compareTo(BigDecimal.ZERO) == 0) {
                throw new DomainError("Quantity can't be 0");
            }
            if (variance.getVarianceReasonId() == null || variance.getVarianceReasonId().isBlank()) {
                throw new DomainError("Variance reason can't be null");
            }
            //目前只有数量调整才允许负数
            if (!VARIANCE_REASON_TYPE_QUANTITY_ADJUSTMENT.equals(variance.getVarianceReasonId())) {
                if (variance.getQuantityOnHandVar().compareTo(BigDecimal.ZERO) < 0) {
                    throw new DomainError("The adjustment quantity can't be negative");
                }
            }
            //如果是（数量调整并且数量是负数）或者（不是数量调整）
            if (!VARIANCE_REASON_TYPE_QUANTITY_ADJUSTMENT.equals(variance.getVarianceReasonId()) || variance.getQuantityOnHandVar().compareTo(BigDecimal.ZERO) < 0) {
                //等同于：
                // if ((VARIANCE_REASON_TYPE_QUANTITY_ADJUSTMENT.equals(variance.getVarianceReasonId()) && variance.getQuantityOnHandVar().compareTo(BigDecimal.ZERO) < 0
                // || !VARIANCE_REASON_TYPE_QUANTITY_ADJUSTMENT.equals(variance.getVarianceReasonId())) {
                BigDecimal adjustmentQuantityAbs = variance.getQuantityOnHandVar().abs();
                if (adjustmentQuantityAbs.compareTo(inventoryItemState.getQuantityOnHandTotal()) > 0) {
                    throw new DomainError("The adjustment quantity cannot exceed the inventory quantity");
                }
            }
            InventoryItemVarianceCommand.CreateInventoryItemVariance createInventoryItemVariance
                    = createPhysicalInventory.newCreateInventoryItemVariance();
            createInventoryItemVariance.setComments(variance.getComments());
            createInventoryItemVariance.setInventoryItemId(variance.getInventoryItemId());
            if (VARIANCE_REASON_TYPE_QUANTITY_ADJUSTMENT.equals(variance.getVarianceReasonId())) {
                createInventoryItemVariance.setQuantityOnHandVar(variance.getQuantityOnHandVar());
            } else {
                createInventoryItemVariance.setQuantityOnHandVar(variance.getQuantityOnHandVar().negate());
            }
            createInventoryItemVariance.setVarianceReasonId(variance.getVarianceReasonId());
            createInventoryItemVariance.setCommandId(c.getCommandId() == null ? createPhysicalInventory.getCommandId() : c.getCommandId());
            createInventoryItemVariance.setRequesterId(c.getRequesterId());
            createPhysicalInventory.getCreateInventoryItemVarianceCommands().add(createInventoryItemVariance);


            //调整库存
            InventoryItemCommands.RecordInventoryEntry recordInventoryEntry = new InventoryItemCommands.RecordInventoryEntry();
            InventoryItemAttributes inventoryItemAttributes = new InventoryItemAttributes();
            inventoryItemAttributes.setLotId(inventoryItemState.getLotId());
            inventoryItemAttributes.setProductId(inventoryItemState.getProductId());
            inventoryItemAttributes.setFacilityId(inventoryItemState.getFacilityId());
            inventoryItemAttributes.setLocationSeqId(inventoryItemState.getLocationSeqId());

            InventoryItemDetailAttributes inventoryItemDetailAttributes = new InventoryItemDetailAttributes();
            inventoryItemDetailAttributes.setPhysicalInventoryId(createPhysicalInventory.getPhysicalInventoryId());
            inventoryItemDetailAttributes.setReasonEnumId(variance.getVarianceReasonId());

            if (VARIANCE_REASON_TYPE_QUANTITY_ADJUSTMENT.equals(variance.getVarianceReasonId())) {
                recordInventoryEntry.setQuantityOnHandDiff(variance.getQuantityOnHandVar());
            } else {
                recordInventoryEntry.setQuantityOnHandDiff(variance.getQuantityOnHandVar().negate());
            }

            recordInventoryEntry.setInventoryItemAttributes(inventoryItemAttributes);
            recordInventoryEntry.setInventoryItemDetailAttributes(inventoryItemDetailAttributes);
            recordInventoryEntry.setRequesterId(c.getRequesterId());
            recordInventoryEntry.setCommandId(UUID.randomUUID().toString());
            inventoryItemApplicationService.when(recordInventoryEntry);
        });
        physicalInventoryApplicationService.when(createPhysicalInventory);


        return createPhysicalInventory.getPhysicalInventoryId();
    }
}
