package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.FfvTraceabilityApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = FfvTraceabilityApplication.class)
@Transactional
class LotTracingServiceTest {

    @Autowired
    private LotTracingService lotTracingService;

    @Test
    void shouldTraceAllInputLotsForChocolateCake() {
        // 追溯巧克力蛋糕的所有原材料批次
        List<LotTracingService.TracingNode> allInputs =
                lotTracingService.findAllInputLots("CHOC_CAKE_STD", "CHOC_CAKE_LOT_001");

        System.out.println("\n=== All Input Lots for Chocolate Cake ===");
        allInputs.forEach(node -> {
            System.out.println("Found lot: {");
            System.out.println("  productId: " + node.getProductId());
            System.out.println("  lotId: " + node.getLotId());
            System.out.println("  workEffortId: " + node.getWorkEffortId());
            System.out.println("}");
        });

        // 验证直接原料
        assertThat(allInputs)
                .extracting("lotId")
                .contains("CAKE_BASE_LOT_001", "CREAM_FILL_LOT_001");

        // 验证间接原料（蛋糕胚的原料）
        assertThat(allInputs)
                .extracting("lotId")
                .contains("FLOUR_LOT_001", "EGG_LOT_001", "SUGAR_LOT_001");
    }

    @Test
    void shouldTraceAllOutputLotsForFlour() {
        // 追溯面粉批次被用于生产的所有产品批次
        List<LotTracingService.TracingNode> allOutputs =
                lotTracingService.findAllOutputLots("RAW_FLOUR", "FLOUR_LOT_001");

        System.out.println("\n=== All Output Lots for Flour ===");
        allOutputs.forEach(node -> {
            System.out.println("Found lot: {");
            System.out.println("  productId: " + node.getProductId());
            System.out.println("  lotId: " + node.getLotId());
            System.out.println("  workEffortId: " + node.getWorkEffortId());
            System.out.println("}");
        });

        // 验证直接产品（蛋糕胚）
        assertThat(allOutputs)
                .extracting("lotId")
                .contains("CAKE_BASE_LOT_001");

        // 验证间接产品（巧克力蛋糕）
        assertThat(allOutputs)
                .extracting("lotId")
                .contains("CHOC_CAKE_LOT_001");
    }

    @Test
    void shouldHandleNoInputsCase() {
        // 原材料批次不应该有投入批次
        List<LotTracingService.TracingNode> allInputs =
                lotTracingService.findAllInputLots("RAW_FLOUR", "FLOUR_LOT_001");

        assertThat(allInputs).isEmpty();
    }

    @Test
    void shouldHandleNoOutputsCase() {
        // 成品批次不应该有产出批次
        List<LotTracingService.TracingNode> allOutputs =
                lotTracingService.findAllOutputLots("CHOC_CAKE_STD", "CHOC_CAKE_LOT_001");

        assertThat(allOutputs).isEmpty();
    }
} 