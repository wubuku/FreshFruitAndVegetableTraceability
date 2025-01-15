package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.domain.order.OrderItemId;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PurchaseOrderFulfillmentService {

    /**
     * 进行（收货）数量的分配并更新采购订单的履行状态。
     * 这个方法可以重复执行，既可以用于第一次分配，也可以用于重新分配。
     */
    String allocateAndUpdateFulfillmentStatus(String orderId, Command command);

    class AllocationResult {
        final List<QuantityAllocationService.AllocationResult<OrderItemId, String>> finalResults;
        final List<QuantityAllocationService.UnallocatedFulfillment<String>> totalUnallocatedFulfillments;
        final Map<OrderItemId, BigDecimal> totalAllocatedQuantities;

        AllocationResult(
                List<QuantityAllocationService.AllocationResult<OrderItemId, String>> finalResults,
                List<QuantityAllocationService.UnallocatedFulfillment<String>> totalUnallocatedFulfillments,
                Map<OrderItemId, BigDecimal> totalAllocatedQuantities) {
            this.finalResults = finalResults;
            this.totalUnallocatedFulfillments = totalUnallocatedFulfillments;
            this.totalAllocatedQuantities = totalAllocatedQuantities;
        }

        public List<QuantityAllocationService.AllocationResult<OrderItemId, String>> getFinalResults() {
            return finalResults;
        }

        public List<QuantityAllocationService.UnallocatedFulfillment<String>> getTotalUnallocatedFulfillments() {
            return totalUnallocatedFulfillments;
        }

        public Map<OrderItemId, BigDecimal> getTotalAllocatedQuantities() {
            return totalAllocatedQuantities;
        }
    }
} 