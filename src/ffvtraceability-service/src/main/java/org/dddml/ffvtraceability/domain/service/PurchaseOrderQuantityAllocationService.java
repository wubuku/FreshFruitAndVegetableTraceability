package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.order.OrderItemId;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PurchaseOrderQuantityAllocationService extends GreedyQuantityAllocationService<OrderItemId, String> {

    public static QuantityAllocationService.QuantityDemandItem<OrderItemId> createDemandItem(
            OrderItemId id, BigDecimal quantity) {
        return new QuantityAllocationService.QuantityDemandItem<>() {
            @Override
            public OrderItemId getDemandItemId() {
                return id;
            }

            @Override
            public BigDecimal getDemandedQuantity() {
                return quantity;
            }
        };
    }

    public static QuantityAllocationService.QuantityFulfillmentItem<String> createFulfillmentItem(
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
}
