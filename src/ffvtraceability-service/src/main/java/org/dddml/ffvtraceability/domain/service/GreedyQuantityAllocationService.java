package org.dddml.ffvtraceability.domain.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class GreedyQuantityAllocationService<D, F> implements QuantityAllocationService<D, F> {

    @Override
    public AllocationResultWrapper<D, F> allocateQuantities(
            List<QuantityFulfillmentItem<F>> fulfillmentItems,
            List<QuantityDemandItem<D>> demandItems
    ) {
        // 1. 参数空值检查
        if (fulfillmentItems == null || demandItems == null) {
            throw new IllegalArgumentException("Input collections cannot be null");
        }

        // 2. 数量非负检查
        for (QuantityFulfillmentItem<F> item : fulfillmentItems) {
            if (item.getFulfilledQuantity().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Fulfilled quantity cannot be negative");
            }
        }

        for (QuantityDemandItem<D> item : demandItems) {
            if (item.getDemandedQuantity().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Demanded quantity cannot be negative");
            }
        }

        // 3. 空集合快速返回
        if (fulfillmentItems.isEmpty() || demandItems.isEmpty()) {
            return new AllocationResultWrapper<>() {
                @Override
                public List<AllocationResult<D, F>> getAllocatedResults() {
                    return Collections.emptyList();
                }

                @Override
                public List<UnallocatedFulfillment<F>> getUnallocatedFulfillments() {
                    if (fulfillmentItems.isEmpty()) {
                        return Collections.emptyList();
                    }
                    // 如果有履行项但没有需求项，所有履行项都作为未分配返回
                    return fulfillmentItems.stream()
                            .map(item -> createUnallocatedFulfillment(
                                    item.getFulfillmentItemId(),
                                    item.getFulfilledQuantity(),
                                    BigDecimal.ZERO,
                                    item.getFulfilledQuantity()
                            ))
                            .collect(Collectors.toList());
                }
            };
        }

        // 结果集合
        List<AllocationResult<D, F>> allocatedResults = new ArrayList<>();
        List<UnallocatedFulfillment<F>> unallocatedFulfillments = new ArrayList<>();

        // 记录每个需求项的已分配数量
        Map<D, BigDecimal> allocatedQtyByDemand = new HashMap<>();

        // 1. 对履行项按数量降序排序(优先分配大批量)
        List<QuantityFulfillmentItem<F>> sortedFulfillmentItems = new ArrayList<>(fulfillmentItems);
        sortedFulfillmentItems.sort((a, b) ->
                b.getFulfilledQuantity().compareTo(a.getFulfilledQuantity()));

        // 2. 对每个履行项进行分配
        for (QuantityFulfillmentItem<F> fulfillmentItem : sortedFulfillmentItems) {
            BigDecimal remainingQty = fulfillmentItem.getFulfilledQuantity();
            BigDecimal allocatedQty = BigDecimal.ZERO;

            // 找出未完全分配的需求项
            List<QuantityDemandItem<D>> availableDemandItems = demandItems.stream()
                    .filter(demandItem -> {
                        BigDecimal allocated = allocatedQtyByDemand
                                .getOrDefault(demandItem.getDemandItemId(), BigDecimal.ZERO);
                        return allocated.compareTo(demandItem.getDemandedQuantity()) < 0;
                    })
                    .sorted((a, b) -> {
                        // 按未分配数量降序排序
                        BigDecimal outstandingA = a.getDemandedQuantity()
                                .subtract(allocatedQtyByDemand
                                        .getOrDefault(a.getDemandItemId(), BigDecimal.ZERO));
                        BigDecimal outstandingB = b.getDemandedQuantity()
                                .subtract(allocatedQtyByDemand
                                        .getOrDefault(b.getDemandItemId(), BigDecimal.ZERO));
                        return outstandingB.compareTo(outstandingA);
                    })
                    .collect(Collectors.toList());

            // 对当前履行项进行分配
            for (QuantityDemandItem<D> demandItem : availableDemandItems) {
                if (remainingQty.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }

                BigDecimal allocated = allocatedQtyByDemand
                        .getOrDefault(demandItem.getDemandItemId(), BigDecimal.ZERO);
                BigDecimal outstanding = demandItem.getDemandedQuantity().subtract(allocated);

                // 计算本次分配数量
                BigDecimal allocateQty = outstanding.min(remainingQty);

                // 验证分配数量的合法性
                if (allocateQty.compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }
                if (allocateQty.compareTo(outstanding) > 0) {
                    throw new IllegalStateException("Allocation quantity exceeds outstanding demand quantity");
                }
                if (allocateQty.compareTo(remainingQty) > 0) {
                    throw new IllegalStateException("Allocation quantity exceeds remaining fulfillment quantity");
                }

                // 记录分配结果
                allocatedResults.add(createAllocationResult(
                        demandItem.getDemandItemId(),
                        fulfillmentItem.getFulfillmentItemId(),
                        allocateQty
                ));

                // 更新已分配数量
                allocatedQtyByDemand.merge(
                        demandItem.getDemandItemId(),
                        allocateQty,
                        BigDecimal::add
                );

                allocatedQty = allocatedQty.add(allocateQty);
                remainingQty = remainingQty.subtract(allocateQty);
            }

            // 记录未分配完的履行数量
            if (remainingQty.compareTo(BigDecimal.ZERO) > 0) {
                unallocatedFulfillments.add(createUnallocatedFulfillment(
                        fulfillmentItem.getFulfillmentItemId(),
                        fulfillmentItem.getFulfilledQuantity(),
                        allocatedQty,
                        remainingQty
                ));
            }
        }

        // 返回不可修改的结果集合
        return new AllocationResultWrapper<>() {
            @Override
            public List<AllocationResult<D, F>> getAllocatedResults() {
                return Collections.unmodifiableList(allocatedResults);
            }

            @Override
            public List<UnallocatedFulfillment<F>> getUnallocatedFulfillments() {
                return Collections.unmodifiableList(unallocatedFulfillments);
            }
        };
    }

    // 辅助方法：创建分配结果
    private AllocationResult<D, F> createAllocationResult(
            D demandItemId,
            F fulfillmentItemId,
            BigDecimal allocatedQuantity) {
        return new AllocationResult<>() {
            @Override
            public D getDemandItemId() {
                return demandItemId;
            }

            @Override
            public F getFulfillmentItemId() {
                return fulfillmentItemId;
            }

            @Override
            public BigDecimal getAllocatedQuantity() {
                return allocatedQuantity;
            }
        };
    }

    // 辅助方法：创建未分配履行记录
    private UnallocatedFulfillment<F> createUnallocatedFulfillment(
            F fulfillmentItemId,
            BigDecimal totalQuantity,
            BigDecimal allocatedQuantity,
            BigDecimal unallocatedQuantity) {
        return new UnallocatedFulfillment<>() {
            @Override
            public F getFulfillmentItemId() {
                return fulfillmentItemId;
            }

            @Override
            public BigDecimal getTotalQuantity() {
                return totalQuantity;
            }

            @Override
            public BigDecimal getAllocatedQuantity() {
                return allocatedQuantity;
            }

            @Override
            public BigDecimal getUnallocatedQuantity() {
                return unallocatedQuantity;
            }
        };
    }
}
