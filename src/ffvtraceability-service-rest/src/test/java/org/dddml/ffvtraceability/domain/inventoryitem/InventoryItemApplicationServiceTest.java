package org.dddml.ffvtraceability.domain.inventoryitem;

import org.dddml.ffvtraceability.FfvTraceabilityApplication;
import org.dddml.ffvtraceability.domain.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = FfvTraceabilityApplication.class)
@Transactional
@Rollback(false)
class InventoryItemApplicationServiceTest {

    @Autowired
    private InventoryItemApplicationService inventoryItemApplicationService;

    @BeforeEach
    void setUp() {
        TenantContext.setTenantId("TEST_TENANT");
    }

    @Test
    void shouldCreateNewInventoryItemWithDetail() {
        // Given
        InventoryItemCommands.RecordInventoryEntry c = new InventoryItemCommands.RecordInventoryEntry();
        // 不设置ID，让系统自动生成

        String lotId = "LOT" + (System.currentTimeMillis() ^ UUID.randomUUID().hashCode());
        // 设置库存项目属性
        InventoryItemAttributes invAttributes = new InventoryItemAttributes();
        invAttributes.setProductId("WIP_CAKE_BASE");
        invAttributes.setLotId(lotId);
        invAttributes.setFacilityId("MAIN_WAREHOUSE");
        invAttributes.setLocationSeqId("SHELF_A1");
        invAttributes.setUomId("PCS");
        c.setInventoryItemAttributes(invAttributes);

        setInventoryItemDetailAttributes(c);

        // 设置数量变化
        c.setQuantityOnHandDiff(new BigDecimal("100.00"));
        c.setAvailableToPromiseDiff(new BigDecimal("100.00"));
        c.setAccountingQuantityDiff(new BigDecimal("100.00"));

        // When
        inventoryItemApplicationService.when(c);

        // Then
        // 由于ID是自动生成的，我们需要通过其他属性来查询
        Iterable<InventoryItemState> items = inventoryItemApplicationService.getByProperty(
            "lotId", 
            lotId,
            null, 
            null, 
            null
        );

        // 验证结果
        InventoryItemState item = items.iterator().next();
        assertThat(item).isNotNull();
        assertThat(item.getProductId()).isEqualTo("WIP_CAKE_BASE");
        assertThat(item.getLotId()).isEqualTo(lotId);
        assertThat(item.getFacilityId()).isEqualTo("MAIN_WAREHOUSE");
        assertThat(item.getLocationSeqId()).isEqualTo("SHELF_A1");
        assertThat(item.getUomId()).isEqualTo("PCS");
        assertThat(item.getQuantityOnHandTotal()).isEqualByComparingTo(new BigDecimal("100.00"));
        
        // 验证明细
        assertThat(item.getDetails()).hasSize(1);
        InventoryItemDetailState detail = item.getDetails().iterator().next();
        assertThat(detail.getShipmentId()).isEqualTo("SHIP001");
        assertThat(detail.getReceiptId()).isEqualTo("RCPT001");
        assertThat(detail.getQuantityOnHandDiff()).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    private static void setInventoryItemDetailAttributes(InventoryItemCommands.RecordInventoryEntry c) {
        // 设置库存明细属性
        InventoryItemDetailAttributes detailAttributes = new InventoryItemDetailAttributes();
        detailAttributes.setShipmentId("SHIP001");
        detailAttributes.setReceiptId("RCPT001");
        detailAttributes.setDescription(UUID.randomUUID().toString());
        c.setInventoryItemDetailAttributes(detailAttributes);
    }

    @Test
    void shouldUpdateExistingInventoryItemWithNewDetail() {
        String lotId = "LOT" + (System.currentTimeMillis() ^ UUID.randomUUID().hashCode());

        // First create an inventory item
        InventoryItemCommands.RecordInventoryEntry c1 = new InventoryItemCommands.RecordInventoryEntry();
        InventoryItemAttributes invAttributes = new InventoryItemAttributes();
        invAttributes.setProductId("WIP_CAKE_BASE");
        invAttributes.setLotId(lotId);
        c1.setInventoryItemAttributes(invAttributes);
        setInventoryItemDetailAttributes(c1);
        c1.setQuantityOnHandDiff(new BigDecimal("50.00"));
        c1.setCommandId(UUID.randomUUID().toString());
        inventoryItemApplicationService.when(c1);

        // Get the created item
        InventoryItemState item = inventoryItemApplicationService
            .getByProperty("lotId", lotId, null, null, null)
            .iterator().next();

        // Then update it with new detail
        InventoryItemCommands.RecordInventoryEntry c2 = new InventoryItemCommands.RecordInventoryEntry();
        c2.setInventoryItemId(item.getInventoryItemId());
        setInventoryItemDetailAttributes(c2);
        c2.setQuantityOnHandDiff(new BigDecimal("25.00"));
        c2.setVersion(0L);
        c2.setCommandId(UUID.randomUUID().toString());
        inventoryItemApplicationService.when(c2);

        // Verify the update
        InventoryItemState updatedItem = inventoryItemApplicationService.get(item.getInventoryItemId());
        assertThat(updatedItem.getQuantityOnHandTotal()).isEqualByComparingTo(new BigDecimal("75.00"));
        assertThat(updatedItem.getDetails()).hasSize(2);
    }
}
