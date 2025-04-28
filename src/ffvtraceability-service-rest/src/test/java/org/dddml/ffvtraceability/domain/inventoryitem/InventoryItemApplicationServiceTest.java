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
import java.util.Iterator;
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
        System.out.println("===> 测试开始，设置租户ID: TEST_TENANT");
    }

    @Test
    void shouldCreateNewInventoryItemWithDetail() {
        System.out.println("\n===== 测试场景: 创建新库存项目 =====");

        // Given
        InventoryItemCommands.RecordInventoryEntry c = new InventoryItemCommands.RecordInventoryEntry();
        // 不设置ID，让系统自动生成

        String lotId = "LOT" + (System.currentTimeMillis() ^ UUID.randomUUID().hashCode());
        System.out.println("===> 生成新的批次ID: " + lotId);

        // 设置库存项目属性
        InventoryItemAttributes invAttributes = new InventoryItemAttributes();
        invAttributes.setProductId("WIP_CAKE_BASE");
        invAttributes.setLotId(lotId);
        invAttributes.setFacilityId("MAIN_WAREHOUSE");
        invAttributes.setLocationSeqId("SHELF_A1");
        invAttributes.setUomId("PCS");
        c.setInventoryItemAttributes(invAttributes);

        System.out.println("===> 设置库存项目属性:");
        System.out.println("    产品ID: " + invAttributes.getProductId());
        System.out.println("    批次ID: " + invAttributes.getLotId());
        System.out.println("    设施ID: " + invAttributes.getFacilityId());
        System.out.println("    位置序列ID: " + invAttributes.getLocationSeqId());
        System.out.println("    计量单位: " + invAttributes.getUomId());

        setInventoryItemDetailAttributes(c);

        // 设置数量变化
        c.setQuantityOnHandDiff(new BigDecimal("100.00"));
        c.setAvailableToPromiseDiff(new BigDecimal("100.00"));
        c.setAccountingQuantityDiff(new BigDecimal("100.00"));

        System.out.println("===> 设置数量变化:");
        System.out.println("    库存数量变化: " + c.getQuantityOnHandDiff());
        System.out.println("    可用数量变化: " + c.getAvailableToPromiseDiff());
        System.out.println("    会计数量变化: " + c.getAccountingQuantityDiff());

        // When
        System.out.println("===> 执行录入库存命令...");
        inventoryItemApplicationService.when(c);
        System.out.println("===> 命令执行完成");

        // Then
        // 由于ID是自动生成的，我们需要通过其他属性来查询
        System.out.println("===> 通过批次ID查询库存项目: " + lotId);
        Iterable<InventoryItemState> items = inventoryItemApplicationService.getByProperty(
                "lotId",
                lotId,
                null,
                null,
                null);

        // 验证结果
        InventoryItemState item = items.iterator().next();
        System.out.println("===> 查询结果:");
        System.out.println("    库存项目ID: " + item.getInventoryItemId());
        System.out.println("    产品ID: " + item.getProductId());
        System.out.println("    批次ID: " + item.getLotId());
        System.out.println("    设施ID: " + item.getFacilityId());
        System.out.println("    位置序列ID: " + item.getLocationSeqId());
        System.out.println("    计量单位: " + item.getUomId());
        System.out.println("    总库存数量: " + item.getQuantityOnHandTotal());
        System.out.println("    总可用数量: " + item.getAvailableToPromiseTotal());
        System.out.println("    总会计数量: " + item.getAccountingQuantityTotal());

        assertThat(item).isNotNull();
        assertThat(item.getProductId()).isEqualTo("WIP_CAKE_BASE");
        assertThat(item.getLotId()).isEqualTo(lotId);
        assertThat(item.getFacilityId()).isEqualTo("MAIN_WAREHOUSE");
        assertThat(item.getLocationSeqId()).isEqualTo("SHELF_A1");
        assertThat(item.getUomId()).isEqualTo("PCS");
        assertThat(item.getQuantityOnHandTotal()).isEqualByComparingTo(new BigDecimal("100.00"));

        // 验证明细
        int detailsCount = 0;
        if (item.getDetails() != null) {
            Iterator<InventoryItemDetailState> detailsIterator = item.getDetails().iterator();
            while (detailsIterator.hasNext()) {
                detailsIterator.next();
                detailsCount++;
            }
        }
        System.out.println("===> 库存明细条目数量: " + detailsCount);
        assertThat(item.getDetails()).hasSize(1);

        InventoryItemDetailState detail = item.getDetails().iterator().next();
        System.out.println("===> 库存明细信息:");
        System.out.println("    装运ID: " + detail.getShipmentId());
        System.out.println("    收据ID: " + detail.getReceiptId());
        System.out.println("    库存数量变化: " + detail.getQuantityOnHandDiff());
        System.out.println("    描述: " + detail.getDescription());

        assertThat(detail.getShipmentId()).isEqualTo("SHIP001");
        assertThat(detail.getReceiptId()).isEqualTo("RCPT001");
        assertThat(detail.getQuantityOnHandDiff()).isEqualByComparingTo(new BigDecimal("100.00"));

        System.out.println("===== 测试完成: 创建新库存项目 =====\n");
    }

    private static void setInventoryItemDetailAttributes(InventoryItemCommands.RecordInventoryEntry c) {
        // 设置库存明细属性
        InventoryItemDetailAttributes detailAttributes = new InventoryItemDetailAttributes();
        detailAttributes.setShipmentId("SHIP001");
        detailAttributes.setReceiptId("RCPT001");
        detailAttributes.setDescription(UUID.randomUUID().toString());
        c.setInventoryItemDetailAttributes(detailAttributes);

        System.out.println("===> 设置库存明细属性:");
        System.out.println("    装运ID: " + detailAttributes.getShipmentId());
        System.out.println("    收据ID: " + detailAttributes.getReceiptId());
        System.out.println("    描述: " + detailAttributes.getDescription());
    }

    @Test
    void shouldUpdateExistingInventoryItemWithNewDetail() {
        System.out.println("\n===== 测试场景: 更新已存在的库存项目 =====");

        String lotId = "LOT" + (System.currentTimeMillis() ^ UUID.randomUUID().hashCode());
        System.out.println("===> 生成新的批次ID: " + lotId);

        // First create an inventory item
        System.out.println("===> 步骤1: 首先创建一个库存项目");
        InventoryItemCommands.RecordInventoryEntry c1 = new InventoryItemCommands.RecordInventoryEntry();
        InventoryItemAttributes invAttributes = new InventoryItemAttributes();
        invAttributes.setProductId("WIP_CAKE_BASE");
        invAttributes.setLotId(lotId);
        c1.setInventoryItemAttributes(invAttributes);
        setInventoryItemDetailAttributes(c1);
        c1.setQuantityOnHandDiff(new BigDecimal("50.00"));
        String commandId1 = UUID.randomUUID().toString();
        c1.setCommandId(commandId1);

        System.out.println("===> 设置初始库存数量: " + c1.getQuantityOnHandDiff());
        System.out.println("===> 命令ID: " + commandId1);

        System.out.println("===> 执行录入库存命令...");
        inventoryItemApplicationService.when(c1);
        System.out.println("===> 命令执行完成");

        // Get the created item
        System.out.println("===> 查询创建的库存项目");
        InventoryItemState item = inventoryItemApplicationService
                .getByProperty("lotId", lotId, null, null, null)
                .iterator().next();

        int detailsCount1 = 0;
        if (item.getDetails() != null) {
            Iterator<InventoryItemDetailState> detailsIterator = item.getDetails().iterator();
            while (detailsIterator.hasNext()) {
                detailsIterator.next();
                detailsCount1++;
            }
        }

        System.out.println("===> 查询结果:");
        System.out.println("    库存项目ID: " + item.getInventoryItemId());
        System.out.println("    产品ID: " + item.getProductId());
        System.out.println("    批次ID: " + item.getLotId());
        System.out.println("    总库存数量: " + item.getQuantityOnHandTotal());
        System.out.println("    版本: " + item.getVersion());
        System.out.println("    明细条目数量: " + detailsCount1);

        // Then update it with new detail
        System.out.println("\n===> 步骤2: 使用新的明细更新库存项目");
        InventoryItemCommands.RecordInventoryEntry c2 = new InventoryItemCommands.RecordInventoryEntry();
        c2.setInventoryItemId(item.getInventoryItemId());
        setInventoryItemDetailAttributes(c2);
        c2.setQuantityOnHandDiff(new BigDecimal("25.00"));
        c2.setVersion(0L);
        String commandId2 = UUID.randomUUID().toString();
        c2.setCommandId(commandId2);

        System.out.println("===> 设置第二次库存数量变化: " + c2.getQuantityOnHandDiff());
        System.out.println("===> 命令ID: " + commandId2);
        System.out.println("===> 设置库存项目ID: " + c2.getInventoryItemId());
        System.out.println("===> 设置版本: " + c2.getVersion());

        System.out.println("===> 执行更新库存命令...");
        inventoryItemApplicationService.when(c2);
        System.out.println("===> 命令执行完成");

        // Verify the update
        System.out.println("===> 查询更新后的库存项目");
        InventoryItemState updatedItem = inventoryItemApplicationService.get(item.getInventoryItemId());

        int detailsCount2 = 0;
        if (updatedItem.getDetails() != null) {
            Iterator<InventoryItemDetailState> detailsIterator = updatedItem.getDetails().iterator();
            while (detailsIterator.hasNext()) {
                detailsIterator.next();
                detailsCount2++;
            }
        }

        System.out.println("===> 更新后的库存项目信息:");
        System.out.println("    库存项目ID: " + updatedItem.getInventoryItemId());
        System.out.println("    产品ID: " + updatedItem.getProductId());
        System.out.println("    批次ID: " + updatedItem.getLotId());
        System.out.println("    总库存数量: " + updatedItem.getQuantityOnHandTotal() + " (应为 50 + 25 = 75)");
        System.out.println("    版本: " + updatedItem.getVersion() + " (应该已增加)");
        System.out.println("    明细条目数量: " + detailsCount2 + " (应该有2个明细条目)");

        if (updatedItem.getDetails() != null) {
            System.out.println("===> 库存明细列表:");
            int index = 1;
            Iterator<InventoryItemDetailState> detailsIterator = updatedItem.getDetails().iterator();
            while (detailsIterator.hasNext()) {
                InventoryItemDetailState detail = detailsIterator.next();
                System.out.println("    明细 #" + index + ":");
                System.out.println("        装运ID: " + detail.getShipmentId());
                System.out.println("        收据ID: " + detail.getReceiptId());
                System.out.println("        库存数量变化: " + detail.getQuantityOnHandDiff());
                System.out.println("        描述: " + detail.getDescription());
                index++;
            }
        }

        assertThat(updatedItem.getQuantityOnHandTotal()).isEqualByComparingTo(new BigDecimal("75.00"));
        assertThat(updatedItem.getDetails()).hasSize(2);

        System.out.println("===== 测试完成: 更新已存在的库存项目 =====\n");
    }
}
