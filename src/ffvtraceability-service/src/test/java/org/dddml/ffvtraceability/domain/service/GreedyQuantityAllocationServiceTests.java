package org.dddml.ffvtraceability.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GreedyQuantityAllocationServiceTests {
    private static final Logger log = LoggerFactory.getLogger(GreedyQuantityAllocationServiceTests.class);

    private GreedyQuantityAllocationService<String, String> service;

    @BeforeEach
    void setUp() {
        service = new GreedyQuantityAllocationService<>();
    }

    @Test
    void whenInputIsNull_thenThrowException() {
        log.info("Testing null input validation...");
        assertThrows(IllegalArgumentException.class, () ->
                service.allocateQuantities(null, Collections.emptyList()));

        assertThrows(IllegalArgumentException.class, () ->
                service.allocateQuantities(Collections.emptyList(), null));
        log.info("Null input validation passed");
    }

    @Test
    void whenNegativeQuantity_thenThrowException() {
        log.info("Testing negative quantity validation...");
        List<QuantityAllocationService.QuantityFulfillmentItem<String>> fulfillmentItems =
                Collections.singletonList(createFulfillmentItem("F1", new BigDecimal("-1")));

        List<QuantityAllocationService.QuantityDemandItem<String>> demandItems =
                Collections.singletonList(createDemandItem("D1", BigDecimal.ONE));

        List<QuantityAllocationService.QuantityFulfillmentItem<String>> finalFulfillmentItems = fulfillmentItems;
        List<QuantityAllocationService.QuantityDemandItem<String>> finalDemandItems = demandItems;
        assertThrows(IllegalArgumentException.class, () ->
                service.allocateQuantities(finalFulfillmentItems, finalDemandItems));

        fulfillmentItems = Collections.singletonList(createFulfillmentItem("F1", BigDecimal.ONE));
        demandItems = Collections.singletonList(createDemandItem("D1", new BigDecimal("-1")));

        List<QuantityAllocationService.QuantityFulfillmentItem<String>> finalFulfillmentItems1 = fulfillmentItems;
        List<QuantityAllocationService.QuantityDemandItem<String>> finalDemandItems1 = demandItems;
        assertThrows(IllegalArgumentException.class, () ->
                service.allocateQuantities(finalFulfillmentItems1, finalDemandItems1));
        log.info("Negative quantity validation passed");
    }

    @Test
    void whenEmptyInput_thenReturnEmptyResult() {
        log.info("Testing empty input handling...");
        QuantityAllocationService.AllocationResultWrapper<String, String> result = service.allocateQuantities(
                Collections.emptyList(),
                Collections.emptyList()
        );

        assertTrue(result.getAllocatedResults().isEmpty());
        assertTrue(result.getUnallocatedFulfillments().isEmpty());
        log.info("Empty input handling passed");
    }

    @Test
    void whenNoFulfillmentItems_thenReturnEmptyResult() {
        log.info("Testing no fulfillment items scenario...");
        List<QuantityAllocationService.QuantityDemandItem<String>> demandItems =
                Collections.singletonList(createDemandItem("D1", BigDecimal.TEN));

        QuantityAllocationService.AllocationResultWrapper<String, String> result =
                service.allocateQuantities(Collections.emptyList(), demandItems);

        assertTrue(result.getAllocatedResults().isEmpty());
        assertTrue(result.getUnallocatedFulfillments().isEmpty());
        log.info("No fulfillment items scenario passed");
    }

    @Test
    void whenNoDemandItems_thenAllFulfillmentsAreUnallocated() {
        log.info("Testing no demand items scenario...");
        List<QuantityAllocationService.QuantityFulfillmentItem<String>> fulfillmentItems = new ArrayList<>();
        fulfillmentItems.add(createFulfillmentItem("F1", BigDecimal.TEN));
        fulfillmentItems.add(createFulfillmentItem("F2", BigDecimal.valueOf(20)));

        QuantityAllocationService.AllocationResultWrapper<String, String> result =
                service.allocateQuantities(fulfillmentItems, Collections.emptyList());

        assertTrue(result.getAllocatedResults().isEmpty());
        assertEquals(2, result.getUnallocatedFulfillments().size());

        QuantityAllocationService.UnallocatedFulfillment<String> unallocated1 =
                result.getUnallocatedFulfillments().get(0);
        assertEquals("F1", unallocated1.getFulfillmentItemId());
        assertEquals(BigDecimal.TEN, unallocated1.getTotalQuantity());
        assertEquals(BigDecimal.ZERO, unallocated1.getAllocatedQuantity());
        assertEquals(BigDecimal.TEN, unallocated1.getUnallocatedQuantity());

        log.info("No demand items scenario passed. Unallocated items: {}",
                result.getUnallocatedFulfillments().size());
    }

    @Test
    void whenExactMatch_thenFullyAllocated() {
        log.info("Testing exact match scenario...");
        // Fulfillment: F1(100)
        // Demand: D1(100)
        List<QuantityAllocationService.QuantityFulfillmentItem<String>> fulfillmentItems =
                Collections.singletonList(createFulfillmentItem("F1", BigDecimal.valueOf(100)));

        List<QuantityAllocationService.QuantityDemandItem<String>> demandItems =
                Collections.singletonList(createDemandItem("D1", BigDecimal.valueOf(100)));

        QuantityAllocationService.AllocationResultWrapper<String, String> result =
                service.allocateQuantities(fulfillmentItems, demandItems);

        assertEquals(1, result.getAllocatedResults().size());
        assertTrue(result.getUnallocatedFulfillments().isEmpty());

        QuantityAllocationService.AllocationResult<String, String> allocation =
                result.getAllocatedResults().get(0);
        assertEquals("D1", allocation.getDemandItemId());
        assertEquals("F1", allocation.getFulfillmentItemId());
        assertEquals(BigDecimal.valueOf(100), allocation.getAllocatedQuantity());

        log.info("Exact match scenario passed. Allocated quantity: {}",
                allocation.getAllocatedQuantity());
    }

    @Test
    void whenMultipleDemandItems_thenAllocateToLargestFirst() {
        log.info("Testing multiple demand items scenario...");
        // Fulfillment: F1(150)
        // Demand: D1(100), D2(80)
        List<QuantityAllocationService.QuantityFulfillmentItem<String>> fulfillmentItems =
                Collections.singletonList(createFulfillmentItem("F1", BigDecimal.valueOf(150)));

        List<QuantityAllocationService.QuantityDemandItem<String>> demandItems = new ArrayList<>();
        demandItems.add(createDemandItem("D1", BigDecimal.valueOf(100)));
        demandItems.add(createDemandItem("D2", BigDecimal.valueOf(80)));

        QuantityAllocationService.AllocationResultWrapper<String, String> result =
                service.allocateQuantities(fulfillmentItems, demandItems);

        assertEquals(2, result.getAllocatedResults().size());

        QuantityAllocationService.AllocationResult<String, String> allocation1 =
                result.getAllocatedResults().get(0);
        assertEquals("D1", allocation1.getDemandItemId());
        assertEquals("F1", allocation1.getFulfillmentItemId());
        assertEquals(BigDecimal.valueOf(100), allocation1.getAllocatedQuantity());

        QuantityAllocationService.AllocationResult<String, String> allocation2 =
                result.getAllocatedResults().get(1);
        assertEquals("D2", allocation2.getDemandItemId());
        assertEquals("F1", allocation2.getFulfillmentItemId());
        assertEquals(BigDecimal.valueOf(50), allocation2.getAllocatedQuantity());

        log.info("Multiple demand items scenario passed. Allocations: D1={}, D2={}",
                allocation1.getAllocatedQuantity(), allocation2.getAllocatedQuantity());
    }

    @Test
    void whenMultipleFulfillmentItems_thenAllocateLargestFirst() {
        log.info("Testing multiple fulfillment items scenario...");
        // Fulfillment: F1(80), F2(120)
        // Demand: D1(150)
        List<QuantityAllocationService.QuantityFulfillmentItem<String>> fulfillmentItems = new ArrayList<>();
        fulfillmentItems.add(createFulfillmentItem("F1", BigDecimal.valueOf(80)));
        fulfillmentItems.add(createFulfillmentItem("F2", BigDecimal.valueOf(120)));

        List<QuantityAllocationService.QuantityDemandItem<String>> demandItems =
                Collections.singletonList(createDemandItem("D1", BigDecimal.valueOf(150)));

        QuantityAllocationService.AllocationResultWrapper<String, String> result =
                service.allocateQuantities(fulfillmentItems, demandItems);

        assertEquals(2, result.getAllocatedResults().size());

        QuantityAllocationService.AllocationResult<String, String> allocation1 =
                result.getAllocatedResults().get(0);
        assertEquals("D1", allocation1.getDemandItemId());
        assertEquals("F2", allocation1.getFulfillmentItemId());
        assertEquals(BigDecimal.valueOf(120), allocation1.getAllocatedQuantity());

        QuantityAllocationService.AllocationResult<String, String> allocation2 =
                result.getAllocatedResults().get(1);
        assertEquals("D1", allocation2.getDemandItemId());
        assertEquals("F1", allocation2.getFulfillmentItemId());
        assertEquals(BigDecimal.valueOf(30), allocation2.getAllocatedQuantity());

        assertEquals(1, result.getUnallocatedFulfillments().size());
        QuantityAllocationService.UnallocatedFulfillment<String> unallocated =
                result.getUnallocatedFulfillments().get(0);
        assertEquals("F1", unallocated.getFulfillmentItemId());
        assertEquals(BigDecimal.valueOf(80), unallocated.getTotalQuantity());
        assertEquals(BigDecimal.valueOf(30), unallocated.getAllocatedQuantity());
        assertEquals(BigDecimal.valueOf(50), unallocated.getUnallocatedQuantity());

        log.info("Multiple fulfillment items scenario passed. Allocations: F2={}, F1={}, Unallocated: {}",
                allocation1.getAllocatedQuantity(), allocation2.getAllocatedQuantity(),
                unallocated.getUnallocatedQuantity());
    }

    @Test
    void whenMultipleDemandAndFulfillmentItems_thenAllocateCorrectly() {
        log.info("Testing complex scenario with multiple demand and fulfillment items...");

        // Fulfillment Items:
        // F1(80)  - smaller quantity
        // F2(120) - medium quantity
        // F3(200) - largest quantity
        List<QuantityAllocationService.QuantityFulfillmentItem<String>> fulfillmentItems = new ArrayList<>();
        fulfillmentItems.add(createFulfillmentItem("F1", BigDecimal.valueOf(80)));
        fulfillmentItems.add(createFulfillmentItem("F2", BigDecimal.valueOf(120)));
        fulfillmentItems.add(createFulfillmentItem("F3", BigDecimal.valueOf(200)));

        // Demand Items:
        // D1(150) - medium demand
        // D2(180) - largest demand
        // D3(50)  - smallest demand
        List<QuantityAllocationService.QuantityDemandItem<String>> demandItems = new ArrayList<>();
        demandItems.add(createDemandItem("D1", BigDecimal.valueOf(150)));
        demandItems.add(createDemandItem("D2", BigDecimal.valueOf(180)));
        demandItems.add(createDemandItem("D3", BigDecimal.valueOf(50)));

        log.info("Input setup:");
        log.info("Fulfillments: F1(80), F2(120), F3(200)");
        log.info("Demands: D1(150), D2(180), D3(50)");
        log.info("Total fulfillment: 400, Total demand: 380");

        QuantityAllocationService.AllocationResultWrapper<String, String> result =
                service.allocateQuantities(fulfillmentItems, demandItems);

        // Print actual allocation results
        List<QuantityAllocationService.AllocationResult<String, String>> allocations =
                result.getAllocatedResults();
        log.info("\nActual allocation results ({} records):", allocations.size());
        for (int i = 0; i < allocations.size(); i++) {
            QuantityAllocationService.AllocationResult<String, String> allocation = allocations.get(i);
            log.info("Allocation {}: {} -> {}, Quantity: {}",
                    i + 1,
                    allocation.getFulfillmentItemId(),
                    allocation.getDemandItemId(),
                    allocation.getAllocatedQuantity());
        }

        // Print unallocated results
        List<QuantityAllocationService.UnallocatedFulfillment<String>> unallocatedFulfillments =
                result.getUnallocatedFulfillments();
        log.info("\nUnallocated fulfillments ({} records):", unallocatedFulfillments.size());
        for (QuantityAllocationService.UnallocatedFulfillment<String> u : unallocatedFulfillments) {
            log.info("Fulfillment {}: Total={}, Allocated={}, Unallocated={}",
                    u.getFulfillmentItemId(),
                    u.getTotalQuantity(),
                    u.getAllocatedQuantity(),
                    u.getUnallocatedQuantity());
        }

        // Calculate and verify total allocated quantity
        BigDecimal totalAllocated = allocations.stream()
                .map(QuantityAllocationService.AllocationResult::getAllocatedQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("\nTotal allocated quantity: {}", totalAllocated);
        assertEquals(BigDecimal.valueOf(380), totalAllocated,
                "Total allocated quantity should match total demand");

        // Verify all demands are fully satisfied
        Map<String, BigDecimal> allocatedByDemand = new HashMap<>();
        for (QuantityAllocationService.AllocationResult<String, String> allocation : allocations) {
            allocatedByDemand.merge(
                    allocation.getDemandItemId(),
                    allocation.getAllocatedQuantity(),
                    BigDecimal::add
            );
        }

        log.info("\nVerifying demand satisfaction:");
        for (QuantityAllocationService.QuantityDemandItem<String> demand : demandItems) {
            BigDecimal allocated = allocatedByDemand.getOrDefault(
                    demand.getDemandItemId(), BigDecimal.ZERO);
            log.info("Demand {}: Required={}, Allocated={}",
                    demand.getDemandItemId(),
                    demand.getDemandedQuantity(),
                    allocated);
            assertEquals(demand.getDemandedQuantity(), allocated,
                    "Demand " + demand.getDemandItemId() + " should be fully satisfied");
        }

        // Verify unallocated quantities are correct
        log.info("\nVerifying unallocated quantities:");
        Map<String, BigDecimal> allocatedByFulfillment = new HashMap<>();
        for (QuantityAllocationService.AllocationResult<String, String> allocation : allocations) {
            allocatedByFulfillment.merge(
                    allocation.getFulfillmentItemId(),
                    allocation.getAllocatedQuantity(),
                    BigDecimal::add
            );
        }

        for (QuantityAllocationService.QuantityFulfillmentItem<String> fulfillment : fulfillmentItems) {
            BigDecimal allocated = allocatedByFulfillment.getOrDefault(
                    fulfillment.getFulfillmentItemId(), BigDecimal.ZERO);
            BigDecimal unallocated = fulfillment.getFulfilledQuantity().subtract(allocated);
            log.info("Fulfillment {}: Total={}, Allocated={}, Unallocated={}",
                    fulfillment.getFulfillmentItemId(),
                    fulfillment.getFulfilledQuantity(),
                    allocated,
                    unallocated);
        }

        log.info("Complex allocation scenario test completed");
    }

    // Helper methods to create test data
    private QuantityAllocationService.QuantityFulfillmentItem<String> createFulfillmentItem(
            String id, BigDecimal quantity) {
        return new QuantityAllocationService.QuantityFulfillmentItem<>() {
            @Override
            public String getFulfillmentItemId() {
                return id;
            }

            @Override
            public BigDecimal getFulfilledQuantity() {
                return quantity;
            }
        };
    }

    private QuantityAllocationService.QuantityDemandItem<String> createDemandItem(
            String id, BigDecimal quantity) {
        return new QuantityAllocationService.QuantityDemandItem<>() {
            @Override
            public String getDemandItemId() {
                return id;
            }

            @Override
            public BigDecimal getDemandedQuantity() {
                return quantity;
            }
        };
    }
}
