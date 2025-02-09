package org.dddml.ffvtraceability.domain.listener;

import org.dddml.ffvtraceability.domain.order.OrderAggregate;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptAggregate;
import org.dddml.ffvtraceability.specialization.SpringDomainEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderFulfillmentListener {

    @EventListener
    @Async("asyncEventExecutor")
    @Transactional
    public void handleOrderEvents(SpringDomainEventPublisher.AggregateEventEnvelope<OrderAggregate> eventEnvelope) {
        System.out.println("Event received: " + eventEnvelope);
        //todo
    }

    @EventListener
    @Async("asyncEventExecutor")
    @Transactional
    public void handleShipmentReceiptEvents(SpringDomainEventPublisher.AggregateEventEnvelope<ShipmentReceiptAggregate> eventEnvelope) {
        //todo
    }

}
