package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.FfvTraceabilityApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dddml.ffvtraceability.domain.service.LotTracingService.*;

@SpringBootTest(classes = FfvTraceabilityApplication.class)
@Transactional
class LotTracingServiceTest {

    @Autowired
    private LotTracingService lotTracingService;

    @Test
    void shouldTraceAllInputLotsForChocolateCake() {
        // 追溯巧克力蛋糕的所有原材料批次（平面列表）
        List<LotTracingService.TracingNode> allInputs =
                lotTracingService.findAllInputLots("CHOC_CAKE_STD", "CHOC_CAKE_LOT_001");

        System.out.println("\n=== All Input Lots for Chocolate Cake (Flat List) ===");
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
    void shouldPrintInputTreeForChocolateCake() {
        // 追溯巧克力蛋糕的所有原材料批次（树形结构）
        TracingTree tree = lotTracingService.findInputLotsAsTree(
                "CHOC_CAKE_STD", "CHOC_CAKE_LOT_001"
        );

        System.out.println("\n=== Input Tree for Chocolate Cake ===");
        System.out.println(tree.print());

        // 验证直接原料
        List<LotTracingService.TracingTree> level1 = tree.getChildren();
        assertThat(level1)
                .extracting(t -> t.getNode().getLotId())
                .contains("CAKE_BASE_LOT_001", "CREAM_FILL_LOT_001");

        // 验证间接原料
        List<LotTracingService.TracingTree> level2 = level1.stream()
                .filter(t -> t.getNode().getLotId().equals("CAKE_BASE_LOT_001"))
                .findFirst()
                .map(TracingTree::getChildren)
                .orElse(Collections.emptyList());

        assertThat(level2)
                .extracting(t -> t.getNode().getLotId())
                .contains("FLOUR_LOT_001", "EGG_LOT_001", "SUGAR_LOT_001");
    }

    @Test
    void shouldTraceAllOutputLotsForFlour() {
        // 追溯面粉批次被用于生产的所有产品批次（平面列表）
        List<LotTracingService.TracingNode> allOutputs =
                lotTracingService.findAllOutputLots("RAW_FLOUR", "FLOUR_LOT_001");

        System.out.println("\n=== All Output Lots for Flour (Flat List) ===");
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
    void shouldPrintOutputTreeForFlour() {
        // 追溯面粉批次被用于生产的所有产品批次（树形结构）
        TracingTree tree = lotTracingService.findOutputLotsAsTree(
                "RAW_FLOUR", "FLOUR_LOT_001"
        );

        System.out.println("\n=== Output Tree for Flour ===");
        System.out.println(tree.print());

        // 验证直接产品
        List<TracingTree> level1 = tree.getChildren();
        assertThat(level1)
                .extracting(t -> t.getNode().getLotId())
                .contains("CAKE_BASE_LOT_001");

        // 验证间接产品
        List<TracingTree> level2 = level1.stream()
                .filter(t -> t.getNode().getLotId().equals("CAKE_BASE_LOT_001"))
                .findFirst()
                .map(TracingTree::getChildren)
                .orElse(Collections.emptyList());

        assertThat(level2)
                .extracting(t -> t.getNode().getLotId())
                .contains("CHOC_CAKE_LOT_001");
    }

    @Test
    void shouldHandleNoInputsCase() {
        // 原材料批次不应该有投入批次（平面列表）
        List<LotTracingService.TracingNode> allInputs =
                lotTracingService.findAllInputLots("RAW_FLOUR", "FLOUR_LOT_001");
        assertThat(allInputs).isEmpty();

        // 原材料批次不应该有投入批次（树形结构）
        TracingTree inputTree = lotTracingService.findInputLotsAsTree(
                "RAW_FLOUR", "FLOUR_LOT_001"
        );
        assertThat(inputTree.getChildren()).isEmpty();
    }

    @Test
    void shouldHandleNoOutputsCase() {
        // 成品批次不应该有产出批次（平面列表）
        List<LotTracingService.TracingNode> allOutputs =
                lotTracingService.findAllOutputLots("CHOC_CAKE_STD", "CHOC_CAKE_LOT_001");
        assertThat(allOutputs).isEmpty();

        // 成品批次不应该有产出批次（树形结构）
        TracingTree outputTree = lotTracingService.findOutputLotsAsTree(
                "CHOC_CAKE_STD", "CHOC_CAKE_LOT_001"
        );
        assertThat(outputTree.getChildren()).isEmpty();
    }

    @Test
    void shouldPrintOutputTreeForMilk() {
        // 追溯牛奶批次被用于生产的所有产品批次（树形结构）
        TracingTree tree = lotTracingService.findOutputLotsAsTree(
                "RAW_MILK", "MILK_LOT_001"
        );

        System.out.println("\n=== Output Tree for Milk ===");
        System.out.println(tree.print());

        // 验证直接产品 - 蛋糕胚和巧克力酱
        List<TracingTree> level1 = tree.getChildren();
        assertThat(level1)
                .extracting(t -> t.getNode().getLotId())
                .contains("CAKE_BASE_LOT_001", "CHOC_SAUCE_LOT_001");

        // 验证间接产品 - 巧克力蛋糕
        // 从蛋糕胚追溯
        List<TracingTree> cakeBaseOutputs = level1.stream()
                .filter(t -> t.getNode().getLotId().equals("CAKE_BASE_LOT_001"))
                .findFirst()
                .map(TracingTree::getChildren)
                .orElse(Collections.emptyList());

        assertThat(cakeBaseOutputs)
                .extracting(t -> t.getNode().getLotId())
                .contains("CHOC_CAKE_LOT_001");

        // 从巧克力酱追溯
        List<TracingTree> chocSauceOutputs = level1.stream()
                .filter(t -> t.getNode().getLotId().equals("CHOC_SAUCE_LOT_001"))
                .findFirst()
                .map(TracingTree::getChildren)
                .orElse(Collections.emptyList());

        assertThat(chocSauceOutputs)
                .extracting(t -> t.getNode().getLotId())
                .contains("CHOC_CAKE_LOT_001");
    }

    @Test
    void shouldTraceSimpleCakeInputs() {
        // 追溯简单蛋糕(使用日期作为批次号)的所有原材料批次
        List<LotTracingService.TracingNode> allInputs =
                lotTracingService.findAllInputLots("CHOC_CAKE_STD", "FG_20240102");

        System.out.println("\n=== All Input Lots for Simple Cake (Flat List) ===");
        allInputs.forEach(node -> {
            System.out.println("Found lot: {");
            System.out.println("  productId: " + node.getProductId());
            System.out.println("  lotId: " + node.getLotId());
            System.out.println("  workEffortId: " + node.getWorkEffortId());
            System.out.println("}");
        });

        // 验证直接原料 - WIP批次
        assertThat(allInputs)
                .extracting("lotId")
                .contains("WIP_20240102");

        // 验证间接原料 - 原材料批次
        assertThat(allInputs)
                .extracting("lotId")
                .contains("FLOUR_LOT_001", "EGG_LOT_001", "SUGAR_LOT_001", "MILK_LOT_001");
    }

    @Test
    void shouldPrintSimpleCakeInputTree() {
        // 追溯简单蛋糕的所有原材料批次（树形结构）
        TracingTree tree = lotTracingService.findInputLotsAsTree(
                "CHOC_CAKE_STD", "FG_20240102"
        );

        System.out.println("\n=== Input Tree for Simple Cake ===");
        System.out.println(tree.print());

        // 验证直接原料 - WIP批次
        List<TracingTree> level1 = tree.getChildren();
        assertThat(level1)
                .extracting(t -> t.getNode().getLotId())
                .contains("WIP_20240102");

        // 验证间接原料 - 原材料批次
        List<TracingTree> level2 = level1.stream()
                .filter(t -> t.getNode().getLotId().equals("WIP_20240102"))
                .findFirst()
                .map(TracingTree::getChildren)
                .orElse(Collections.emptyList());

        assertThat(level2)
                .extracting(t -> t.getNode().getLotId())
                .contains("FLOUR_LOT_001", "EGG_LOT_001", "SUGAR_LOT_001", "MILK_LOT_001");
    }

    @Test
    void shouldTraceSimpleCakeRawMaterialOutputs() {
        // 追溯面粉批次到简单蛋糕的产出追溯
        TracingTree flourTree = lotTracingService.findOutputLotsAsTree(
                "RAW_FLOUR", "FLOUR_LOT_001"
        );

        System.out.println("\n=== Output Tree for Flour (Including Simple Cake) ===");
        System.out.println(flourTree.print());

        // 验证面粉批次可以追溯到简单蛋糕
        List<String> flourOutputLotIds = lotTracingService.findAllOutputLots("RAW_FLOUR", "FLOUR_LOT_001")
                .stream()
                .map(TracingNode::getLotId)
                .collect(Collectors.toList());

        assertThat(flourOutputLotIds)
                .contains("WIP_20240102", "FG_20240102");  // 应该包含WIP和成品批次

        // 追溯牛奶批次到简单蛋糕的产出追溯
        TracingTree milkTree = lotTracingService.findOutputLotsAsTree(
                "RAW_MILK", "MILK_LOT_001"
        );

        System.out.println("\n=== Output Tree for Milk (Including Simple Cake) ===");
        System.out.println(milkTree.print());

        // 验证牛奶批次可以追溯到简单蛋糕
        List<String> milkOutputLotIds = lotTracingService.findAllOutputLots("RAW_MILK", "MILK_LOT_001")
                .stream()
                .map(TracingNode::getLotId)
                .collect(Collectors.toList());

        assertThat(milkOutputLotIds)
                .contains("WIP_20240102", "FG_20240102");  // 应该包含WIP和成品批次
    }
} 