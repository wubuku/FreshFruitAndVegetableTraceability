package org.dddml.ffvtraceability.domain.listener;

import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.domain.TenantContext;
import org.dddml.ffvtraceability.domain.order.OrderAggregate;
import org.dddml.ffvtraceability.domain.service.PurchaseOrderFulfillmentService;
import org.dddml.ffvtraceability.domain.shipment.ShipmentApplicationService;
import org.dddml.ffvtraceability.domain.shipment.ShipmentState;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptAggregate;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptApplicationService;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptEvent;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptState;
import org.dddml.ffvtraceability.specialization.Event;
import org.dddml.ffvtraceability.specialization.SpringDomainEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class OrderFulfillmentListener {
    private static final long RECEIPT_PROCESSING_DELAY_MS = 10000L;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ShipmentReceiptApplicationService shipmentReceiptApplicationService;

    @Autowired
    private ShipmentApplicationService shipmentApplicationService;

    @Autowired
    private PurchaseOrderFulfillmentService purchaseOrderFulfillmentService;

    @EventListener
    @Async("asyncEventExecutor")
    public void handleOrderEvents(SpringDomainEventPublisher.AggregateEventEnvelope<OrderAggregate> eventEnvelope) {
        System.out.println("Event received: " + eventEnvelope);
        //todo
    }

    @EventListener
    @Async("asyncEventExecutor")
    public void handleShipmentReceiptEvents(SpringDomainEventPublisher.AggregateEventEnvelope<ShipmentReceiptAggregate> eventEnvelope) {
        if (eventEnvelope.getDomainEvents() == null) {
            return;
        }
        for (Event e : eventEnvelope.getDomainEvents()) {
            String receiptId;
            if (e instanceof ShipmentReceiptEvent.ShipmentReceiptStateCreated) {
                //ShipmentReceiptEvent.ShipmentReceiptStateCreated srCreated = (ShipmentReceiptEvent.ShipmentReceiptStateCreated) e;
            } else if (e instanceof ShipmentReceiptEvent.ShipmentReceiptStateMergePatched) {
                //ShipmentReceiptEvent.ShipmentReceiptStateMergePatched srMergePatched = (ShipmentReceiptEvent.ShipmentReceiptStateMergePatched) e;
            } else if (e instanceof ShipmentReceiptEvent.ShipmentReceiptStateDeleted) {
                //ShipmentReceiptEvent.ShipmentReceiptStateDeleted srDeleted = (ShipmentReceiptEvent.ShipmentReceiptStateDeleted) e;
            } else {
                continue;
            }
            //
            // NOTE: Get some sleep and wait for the Shipment Receipt to be saved to the database.
            //
            try {
                Thread.sleep(RECEIPT_PROCESSING_DELAY_MS);
            } catch (InterruptedException ex) {
                //throw new RuntimeException(ex);
                logger.error("Sleep interrupted while waiting for Shipment Receipt to be saved to the database.", ex);
                continue;
            }
            ShipmentReceiptEvent srEvent = (ShipmentReceiptEvent) e;
            receiptId = srEvent.getReceiptId();
            String tenantId = srEvent.getTenantId();
            TenantContext.setTenantId(tenantId);
            try {
                ShipmentReceiptState shipmentReceiptState = shipmentReceiptApplicationService.get(receiptId);
                if (shipmentReceiptState == null) {
                    continue;
                }
                String orderId = shipmentReceiptState.getOrderId();
                if (orderId == null && shipmentReceiptState.getShipmentId() != null) {
                    ShipmentState shipmentState = shipmentApplicationService.get(shipmentReceiptState.getShipmentId());
                    orderId = shipmentState.getPrimaryOrderId();
                }
                if (orderId == null || orderId.trim().isEmpty()) {
                    continue;
                }
                // Check if a purchase order???
                purchaseOrderFulfillmentService.allocateAndUpdateFulfillmentStatus(orderId, new TempCommand());
            } finally {
                TenantContext.setTenantId(null);
            }
        } // end for
    }

    static class TempCommand implements Command {
        @Override
        public String getCommandType() {
            return "TempCommand";
        }

        @Override
        public void setCommandType(String commandType) {
        }

        @Override
        public String getCommandId() {
            return UUID.randomUUID().toString();
        }

        @Override
        public void setCommandId(String commandId) {
        }

        @Override
        public String getRequesterId() {
            return OrderFulfillmentListener.class.getName();
        }

        @Override
        public void setRequesterId(String requesterId) {
        }

        @Override
        public Map<String, Object> getCommandContext() {
            return Map.of();
        }

    }

}
