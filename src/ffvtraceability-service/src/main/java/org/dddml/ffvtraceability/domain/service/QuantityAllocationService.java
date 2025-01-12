package org.dddml.ffvtraceability.domain.service;

import java.math.BigDecimal;
import java.util.List;

public interface QuantityAllocationService<D, F> {
    /**
     * 将实际作业行项分配给需求行项
     *
     * @param fulfillmentItems 实际作业行项集合
     * @param demandItems      需求行项集合
     * @return 分配结果
     */
    AllocationResultWrapper<D, F> allocateQuantities(
            List<QuantityFulfillmentItem<F>> fulfillmentItems,
            List<QuantityDemandItem<D>> demandItems
    );

    /**
     * 表示一个数量需求行项
     *
     * @param <D> 需求行项 ID 的类型
     */
    interface QuantityDemandItem<D> {
        D getDemandItemId();  // 需求行项 ID

        BigDecimal getDemandedQuantity();  // 需求数量
    }

    /**
     * 表示一个实际作业行项
     *
     * @param <F> 作业行项 ID 的类型
     */
    interface QuantityFulfillmentItem<F> {
        F getFulfillmentItemId();  // 作业行项 ID

        BigDecimal getFulfilledQuantity();  // 作业数量
    }

    /**
     * 分配结果
     *
     * @param <D> 需求行项 ID 的类型
     * @param <F> 作业行项 ID 的类型
     */
    interface AllocationResult<D, F> {
        D getDemandItemId();

        F getFulfillmentItemId();

        BigDecimal getAllocatedQuantity();
    }

    /**
     * 未分配完的作业行项
     *
     * @param <F> 作业行项 ID 的类型
     */
    interface UnallocatedFulfillment<F> {
        F getFulfillmentItemId();

        BigDecimal getTotalQuantity();

        BigDecimal getAllocatedQuantity();

        BigDecimal getUnallocatedQuantity();
    }

    /**
     * 分配结果包装器
     *
     * @param <D> 需求行项 ID的类型
     * @param <F> 作业行项 ID的类型
     */
    interface AllocationResultWrapper<D, F> {
        List<AllocationResult<D, F>> getAllocatedResults();

        List<UnallocatedFulfillment<F>> getUnallocatedFulfillments();
    }

}
