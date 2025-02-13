package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.FfvTraceabilityApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = FfvTraceabilityApplication.class)
@Transactional
class LotTracingRepositoryTest {

    @Autowired
    private LotTracingRepository lotTracingRepository;

    @Test
    void shouldTraceInputLotsForCakeBase() {
        String productId = "WIP_CAKE_BASE";
        String lotId = "CAKE_BASE_LOT_001";

        System.out.println("\n=== Test Parameters ===");
        System.out.println("Product ID: " + productId);
        System.out.println("Lot ID: " + lotId);

        // 执行查询
        List<LotTracingRepository.LotTracingNodeProjection> inputLots =
                lotTracingRepository.findInputLots(productId, lotId);

        // 打印查询结果
        System.out.println("\n=== Query Results ===");
        if (inputLots.isEmpty()) {
            System.out.println("No results found!");
        } else {
            inputLots.forEach(lot -> {
                System.out.println("Found lot: {");
                System.out.println("  lotId: " + lot.getLotId());
                System.out.println("  productId: " + lot.getProductId());
                System.out.println("  workEffortId: " + lot.getWorkEffortId());
                System.out.println("}");
            });
        }

        // 打印实际SQL (通过Hibernate日志配置)

        // 断言
        assertThat(inputLots).isNotEmpty()
                .extracting("lotId")
                .contains("FLOUR_LOT_001", "EGG_LOT_001", "SUGAR_LOT_001");
    }

    @Test
    void shouldTraceOutputLotsForFlour() {
        // 根据测试数据,面粉批次(FLOUR_LOT_001)被用于生产蛋糕胚
        List<LotTracingRepository.LotTracingNodeProjection> outputLots =
                lotTracingRepository.findOutputLots("RAW_FLOUR", "FLOUR_LOT_001");

        assertThat(outputLots).isNotEmpty()
                .extracting("lotId")
                .contains("CAKE_BASE_LOT_001");

        // 验证产出批次的完整信息
        LotTracingRepository.LotTracingNodeProjection cakeBaseLot = outputLots.stream()
                .filter(lot -> lot.getLotId().equals("CAKE_BASE_LOT_001"))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Cake base lot not found"));

        assertThat(cakeBaseLot.getProductId()).isEqualTo("WIP_CAKE_BASE");
        assertThat(cakeBaseLot.getWorkEffortId()).isEqualTo("PROD_RUN_01");
    }

    @Test
    void shouldTraceMultiLevelForChocolateCake() {
        // 先找到巧克力蛋糕使用的所有直接原料批次
        List<LotTracingRepository.LotTracingNodeProjection> level1Inputs =
                lotTracingRepository.findInputLots("CHOC_CAKE_STD", "CHOC_CAKE_LOT_001");

        assertThat(level1Inputs).isNotEmpty()
                .extracting("lotId")
                .contains("CAKE_BASE_LOT_001", "CREAM_FILL_LOT_001");

        // 再找蛋糕胚使用的原料批次
        List<LotTracingRepository.LotTracingNodeProjection> level2Inputs =
                lotTracingRepository.findInputLots("WIP_CAKE_BASE", "CAKE_BASE_LOT_001");

        assertThat(level2Inputs).isNotEmpty()
                .extracting("lotId")
                .contains("FLOUR_LOT_001", "EGG_LOT_001", "SUGAR_LOT_001");
    }

    @Test
    void shouldHandleNoInputsCase() {
        // 原材料批次不应该有投入批次
        List<LotTracingRepository.LotTracingNodeProjection> inputLots =
                lotTracingRepository.findInputLots("RAW_FLOUR", "FLOUR_LOT_001");

        assertThat(inputLots).isEmpty();
    }

    @Test
    void shouldHandleNoOutputsCase() {
        // 成品批次不应该有产出批次
        List<LotTracingRepository.LotTracingNodeProjection> outputLots =
                lotTracingRepository.findOutputLots("CHOC_CAKE_STD", "CHOC_CAKE_LOT_001");

        assertThat(outputLots).isEmpty();
    }
} 