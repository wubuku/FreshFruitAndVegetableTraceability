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

import java.util.Collections;
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
        logger.info("Received ShipmentReceipt events envelope: {}", eventEnvelope);
        if (eventEnvelope.getDomainEvents() == null) {
            logger.warn("No domain events found in envelope");
            return;
        }
        for (Event e : eventEnvelope.getDomainEvents()) {
            String receiptId;
            String previousOrderId = null;
            if (e instanceof ShipmentReceiptEvent.ShipmentReceiptStateCreated) {
                logger.info("Processing ShipmentReceiptStateCreated event: {}", e);
            } else if (e instanceof ShipmentReceiptEvent.ShipmentReceiptStateMergePatched) {
                logger.info("Processing ShipmentReceiptStateMergePatched event: {}", e);
            } else if (e instanceof ShipmentReceiptEvent.ShipmentReceiptStateDeleted) {
                logger.info("Processing ShipmentReceiptStateDeleted event: {}", e);
            } else if (e instanceof ShipmentReceiptEvent.OrderAllocationUpdated) {
                logger.info("Skipping OrderAllocationUpdated event: {}", e);
                continue;
            } else {
                logger.info("Skipping unknown event type: {}", e.getClass().getName());
                continue;
            }

            try {
                logger.info("Waiting {}ms for database update...", RECEIPT_PROCESSING_DELAY_MS);
                Thread.sleep(RECEIPT_PROCESSING_DELAY_MS);
            } catch (InterruptedException ex) {
                logger.error("Sleep interrupted while waiting for Shipment Receipt to be saved to the database.", ex);
                continue;
            }

            ShipmentReceiptEvent srEvent = (ShipmentReceiptEvent) e;
            receiptId = srEvent.getReceiptId();
            String tenantId = srEvent.getTenantId();
            logger.info("Processing receipt ID: {}, tenant ID: {}", receiptId, tenantId);

            TenantContext.setTenantId(tenantId);
            try {
                logger.info("Fetching ShipmentReceiptState for receipt ID: {}", receiptId);
                ShipmentReceiptState shipmentReceiptState = shipmentReceiptApplicationService.get(receiptId);
                if (shipmentReceiptState == null) {
                    logger.warn("ShipmentReceiptState not found for receipt ID: {}", receiptId);
                    continue;
                }
                logger.info("Found ShipmentReceiptState: {}", shipmentReceiptState);

                String orderId = shipmentReceiptState.getOrderId();
                logger.info("Initial orderId from receipt: {}", orderId);

                if (orderId == null && shipmentReceiptState.getShipmentId() != null) {
                    String shipmentId = shipmentReceiptState.getShipmentId();
                    logger.info("Fetching ShipmentState for shipment ID: {}", shipmentId);
                    ShipmentState shipmentState = shipmentApplicationService.get(shipmentId);
                    if (shipmentState != null) {
                        orderId = shipmentState.getPrimaryOrderId();
                        logger.info("Found orderId from shipment: {}", orderId);
                    } else {
                        logger.warn("ShipmentState not found for shipment ID: {}", shipmentId);
                    }
                }

                if (orderId == null || orderId.trim().isEmpty()) {
                    logger.warn("No valid orderId found, skipping processing");
                    continue;
                }

                if (previousOrderId != null && !orderId.equals(previousOrderId)) {
                    logger.info("Receipt reassigned from order {} to {}", previousOrderId, orderId);
                    logger.info("Processing previous order allocation for orderId: {}", previousOrderId);
                    purchaseOrderFulfillmentService.allocateAndUpdateFulfillmentStatus(previousOrderId, new TempCommand());
                    continue;
                }

                logger.info("Processing order allocation for orderId: {}", orderId);
                purchaseOrderFulfillmentService.allocateAndUpdateFulfillmentStatus(orderId, new TempCommand());
            } catch (Exception ex) {
                logger.error("Error processing ShipmentReceipt event", ex);
            } finally {
                TenantContext.setTenantId(null);
            }
        }
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
            return Collections.emptyMap();
        }

    }

}
