package org.dddml.ffvtraceability.domain.listener;

import jakarta.annotation.PreDestroy;
import org.dddml.ffvtraceability.domain.TenantContext;
import org.dddml.ffvtraceability.domain.common.DelayedProcessingQueue;
import org.dddml.ffvtraceability.domain.common.TempCommand;
import org.dddml.ffvtraceability.domain.service.CteReceivingEventSynchronizationService;
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

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Component
public class CteReceivingListener {
    private static final long RECEIPT_PROCESSING_DELAY_MS = 5000L;
    private static final long CTE_PROCESSING_DELAY_MS = 5000L;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DelayedProcessingQueue<String, String, DelayedShipmentId> shipmentQueue;

    @Autowired
    private CteReceivingEventSynchronizationService cteReceivingEventSynchronizationService;

    @Autowired
    private ShipmentReceiptApplicationService shipmentReceiptApplicationService;

    @Autowired
    public CteReceivingListener(
            @Qualifier("eventProcessingExecutor") TaskExecutor taskExecutor
    ) {
        this.shipmentQueue = new DelayedProcessingQueue<>(
                taskExecutor,
                this::processCteReceiving,
                "CteReceiving"
        );
    }

    @PreDestroy
    public void shutdown() {
        shipmentQueue.close();
    }

    private void processCteReceiving(String shipmentId, String tenantId) {
        try {
            logger.info("Processing delayed CTE receiving for shipmentId: {}", shipmentId);
            TenantContext.setTenantId(tenantId);
            cteReceivingEventSynchronizationService.synchronizeReceivingEvent(
                    shipmentId,
                    new TempCommand("UpdateReceivingCTE")
            );
        } catch (Exception ex) {
            logger.error("Error processing CTE receiving: {}", shipmentId, ex);
        } finally {
            TenantContext.setTenantId(null);
        }
    }

    private void queueShipmentForProcessing(String shipmentId, String tenantId) {
        if (shipmentId == null || shipmentId.trim().isEmpty()) {
            return;
        }
        shipmentQueue.queueItemForProcessing(
                shipmentId,
                tenantId,
                new DelayedShipmentId(shipmentId, tenantId, CTE_PROCESSING_DELAY_MS)
        );
    }

    @EventListener
    @Async("asyncEventExecutor")
    public void handleShipmentReceiptEvents(SpringDomainEventPublisher.AggregateEventEnvelope<ShipmentReceiptAggregate> eventEnvelope) {
        if (eventEnvelope.getDomainEvents() == null) {
            logger.warn("No domain events found in envelope");
            return;
        }

        for (Event e : eventEnvelope.getDomainEvents()) {
            if (!(e instanceof ShipmentReceiptEvent)) {
                continue;
            }

            ShipmentReceiptEvent srEvent = (ShipmentReceiptEvent) e;
            String receiptId = srEvent.getReceiptId();
            String tenantId = srEvent.getTenantId();

            try {
                logger.info("Waiting {}ms for database update...", RECEIPT_PROCESSING_DELAY_MS);
                Thread.sleep(RECEIPT_PROCESSING_DELAY_MS);
            } catch (InterruptedException ex) {
                logger.error("Sleep interrupted while waiting for Shipment Receipt to be saved to the database.", ex);
                continue;
            }

            try {
                TenantContext.setTenantId(tenantId);
                ShipmentReceiptState shipmentReceiptState = shipmentReceiptApplicationService.get(receiptId);
                if (shipmentReceiptState == null) {
                    logger.warn("ShipmentReceiptState not found for receipt ID: {}", receiptId);
                    continue;
                }

                String shipmentId = shipmentReceiptState.getShipmentId();
                if (shipmentId == null || shipmentId.trim().isEmpty()) {
                    logger.warn("No shipmentId found for receipt: {}", receiptId);
                    continue;
                }

                queueShipmentForProcessing(shipmentId, tenantId);
            } catch (Exception ex) {
                logger.error("Error processing ShipmentReceipt event", ex);
            } finally {
                TenantContext.setTenantId(null);
            }
        }
    }

    private static class DelayedShipmentId implements DelayedProcessingQueue.DelayedItem<String, String> {
        private final String shipmentId;
        private final String tenantId;
        private final long expiryTime;

        public DelayedShipmentId(String shipmentId, String tenantId, long delayMs) {
            this.shipmentId = shipmentId;
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

        @Override
        public String getId() {
            return shipmentId;
        }

        @Override
        public String getContext() {
            return tenantId;
        }
    }
}
