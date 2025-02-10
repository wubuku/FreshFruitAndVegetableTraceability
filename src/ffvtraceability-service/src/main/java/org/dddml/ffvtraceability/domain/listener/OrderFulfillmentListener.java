package org.dddml.ffvtraceability.domain.listener;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Component
public class OrderFulfillmentListener {
    private static final long RECEIPT_PROCESSING_DELAY_MS = 5000L;
    private static final long ORDER_PROCESSING_DELAY_MS = 5000L;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DelayQueue<DelayedOrderId> orderProcessingQueue = new DelayQueue<>();
    private final Set<String> queuedOrderIds = new HashSet<>();
    private volatile boolean isRunning = true;

    @Autowired
    @Qualifier("orderProcessingExecutor")
    private TaskExecutor taskExecutor;

    @Autowired
    private ShipmentReceiptApplicationService shipmentReceiptApplicationService;

    @Autowired
    private ShipmentApplicationService shipmentApplicationService;

    @Autowired
    private PurchaseOrderFulfillmentService purchaseOrderFulfillmentService;

    @PostConstruct
    public void init() {
        taskExecutor.execute(this::processDelayedOrders);
    }

    @PreDestroy
    public void shutdown() {
        isRunning = false;
    }

    private void processDelayedOrders() {
        while (isRunning) {
            try {
                DelayedOrderId delayedOrderId = orderProcessingQueue.poll(1, TimeUnit.SECONDS);
                if (delayedOrderId == null) {
                    continue;
                }
                String orderId = delayedOrderId.getOrderId();
                String tenantId = delayedOrderId.getTenantId();
                synchronized (queuedOrderIds) {
                    queuedOrderIds.remove(orderId);
                }
                try {
                    logger.info("Processing delayed order allocation for orderId: {}", orderId);
                    TenantContext.setTenantId(tenantId);
                    purchaseOrderFulfillmentService.allocateAndUpdateFulfillmentStatus(
                            orderId, new TempCommand());
                } catch (Exception ex) {
                    logger.error("Error processing delayed order: {}", orderId, ex);
                } finally {
                    TenantContext.setTenantId(null);
                }
            } catch (InterruptedException e) {
                logger.warn("Order processing interrupted", e);
                Thread.currentThread().interrupt();
                break;
            }
        }
        logger.info("Order processing thread shutting down");
    }

    private void queueOrderForProcessing(String orderId, String tenantId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            return;
        }
        synchronized (queuedOrderIds) {
            if (queuedOrderIds.contains(orderId)) {
                orderProcessingQueue.removeIf(
                        delayed -> delayed.getOrderId().equals(orderId));
            }
            queuedOrderIds.add(orderId);
            orderProcessingQueue.offer(
                    new DelayedOrderId(orderId, tenantId, ORDER_PROCESSING_DELAY_MS));
            logger.debug("Queued orderId: {} for delayed processing", orderId);
        }
    }

    @EventListener
    @Async("asyncEventExecutor")
    public void handleOrderEvents(SpringDomainEventPublisher.AggregateEventEnvelope<OrderAggregate> eventEnvelope) {
        //
        //todo System.out.println("Event received: " + eventEnvelope);
        //
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
                logger.info("Processing OrderAllocationUpdated event: {}", e);
                ShipmentReceiptEvent.OrderAllocationUpdated orderAllocationUpdated = (ShipmentReceiptEvent.OrderAllocationUpdated) e;
                previousOrderId = orderAllocationUpdated.getPreviousOrderId();
            } else {
                //logger.info("Skipping unknown event type: {}", e.getClass().getName());
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

                if (e instanceof ShipmentReceiptEvent.OrderAllocationUpdated) {
                    if (previousOrderId != null && !orderId.equals(previousOrderId)) {
                        logger.info("Receipt reassigned from order {} to {}", previousOrderId, orderId);
                        queueOrderForProcessing(previousOrderId, tenantId);
                    }
                    // NOTE: Avoid infinite loop messages!
                    continue;
                }

                queueOrderForProcessing(orderId, tenantId);
            } catch (Exception ex) {
                logger.error("Error processing ShipmentReceipt event", ex);
            } finally {
                TenantContext.setTenantId(null);
            }
        }
    }

    private static class DelayedOrderId implements Delayed {
        private final String orderId;
        private final String tenantId;
        private final long expiryTime;

        public DelayedOrderId(String orderId, String tenantId, long delayMs) {
            this.orderId = orderId;
            this.tenantId = tenantId;
            this.expiryTime = System.currentTimeMillis() + delayMs;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long diff = expiryTime - System.currentTimeMillis();
            return unit.convert(diff, TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed other) {
            return Long.compare(getDelay(TimeUnit.MILLISECONDS),
                    other.getDelay(TimeUnit.MILLISECONDS));
        }

        public String getOrderId() {
            return orderId;
        }

        public String getTenantId() {
            return tenantId;
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
