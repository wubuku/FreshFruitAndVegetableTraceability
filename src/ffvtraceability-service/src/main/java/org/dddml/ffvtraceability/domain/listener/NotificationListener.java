package org.dddml.ffvtraceability.domain.listener;

import org.dddml.ffvtraceability.domain.bffnotification.AbstractBffNotificationCommand;
import org.dddml.ffvtraceability.domain.bffnotification.BffNotificationApplicationService;
import org.dddml.ffvtraceability.domain.bffnotification.BffNotificationCommand;
import org.dddml.ffvtraceability.domain.order.OrderAggregate;
import org.dddml.ffvtraceability.domain.order.OrderApplicationService;
import org.dddml.ffvtraceability.domain.order.OrderEvent;
import org.dddml.ffvtraceability.domain.order.OrderHeaderState;
import org.dddml.ffvtraceability.domain.shipment.ShipmentAggregate;
import org.dddml.ffvtraceability.domain.shipment.ShipmentApplicationService;
import org.dddml.ffvtraceability.domain.shipment.ShipmentEvent;
import org.dddml.ffvtraceability.domain.shipment.ShipmentState;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.specialization.Event;
import org.dddml.ffvtraceability.specialization.SpringDomainEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {
    /**
     * NotificationTypeId:销售订单 订单创建
     */
    public final static String NOTIFICATION_PURCHASE_ORDER_CREATED = "PO_CREATED";

    /**
     * NotificationTypeId:销售订单 订单修改
     */
    public final static String NOTIFICATION_PURCHASE_ORDER_UPDATED = "PO_UPDATED";

    /**
     * NotificationTypeId: 收货单 订单创建
     */
    public final static String NOTIFICATION_RECEIVING_DOCUMENT_CREATED = "RD_CREATED";

    /**
     * NotificationTypeId:收货单 订单修改
     */
    public final static String NOTIFICATION_RECEIVING_DOCUMENT_UPDATED = "RD_UPDATED";
    /**
     * NotificationTypeId:订单取消
     */
    public final static String NOTIFICATION_PURCHASE_ORDER_CANCELLED = "PO_CANCELLED";

    /**
     * ReferenceDocumentTypeId 订单类型 采购订单
     */
    public final static String REFERENCE_DOCUMENT_PURCHASE_ORDER = "PURCHASE_ORDER";

    /**
     * ReferenceDocumentTypeId 订单类型 收货单
     */
    public final static String REFERENCE_DOCUMENT_RECEIPT = "RECEIVING_DOCUMENT";


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BffNotificationApplicationService bffNotificationApplicationService;
    @Autowired
    private OrderApplicationService orderApplicationService;
    @Autowired
    private ShipmentApplicationService shipmentApplicationService;

    @EventListener
    public void extractOrderEventToNotification(SpringDomainEventPublisher.AggregateEventEnvelope<OrderAggregate> eventEnvelope) {
        String notificationTypeId = null;
        String notificationContent = null;
        for (Event e : eventEnvelope.getDomainEvents()) {
            if (e instanceof OrderEvent.OrderStateEvent.OrderStateCreated) {
                notificationTypeId = NOTIFICATION_PURCHASE_ORDER_CREATED;
                notificationContent = "New purchase order added";
                logger.info("Processing OrderStateCreated event: {}", e);
            } else if (e instanceof OrderEvent.OrderStateEvent.OrderStateMergePatched) {
                notificationTypeId = NOTIFICATION_PURCHASE_ORDER_UPDATED;
                notificationContent = "Purchase order updated";
                logger.info("Processing OrderStateMergePatched event: {}", e);
            } else {
                continue;
            }
            OrderEvent.OrderStateEvent event = (OrderEvent.OrderStateEvent) e;
            String orderId = event.getOrderId();
            String tenantId = event.getTenantId();
            OrderHeaderState orderHeaderState = orderApplicationService.get(orderId);
            if (orderHeaderState == null) {
                logger.error("Can't find purchase order:{}", orderId);
                continue;
            }
            BffNotificationCommand.CreateBffNotification createBffNotification = new AbstractBffNotificationCommand.SimpleCreateBffNotification();
            createBffNotification.setReferenceDocumentId(orderId);
            createBffNotification.setReferenceDocumentNumber(orderId);
            createBffNotification.setBffNotificationId(IdUtils.randomId());
            createBffNotification.setNotificationTypeId(notificationTypeId);
            createBffNotification.setReferenceDocumentTypeId(REFERENCE_DOCUMENT_PURCHASE_ORDER);
            createBffNotification.setNotificationContent(notificationContent);
            createBffNotification.setStatusId("Unread");
            createBffNotification.setPriority(0L);
            createBffNotification.setRequesterId(orderHeaderState.getCreatedBy());
            createBffNotification.setCommandId(createBffNotification.getBffNotificationId());

            bffNotificationApplicationService.when(createBffNotification);
        }
    }


    @EventListener
    public void extractReceiptEventToNotification(SpringDomainEventPublisher.AggregateEventEnvelope<ShipmentAggregate> eventEnvelope) {
        String notificationTypeId = null;
        String notificationContent = null;
        for (Event e : eventEnvelope.getDomainEvents()) {
            if (e instanceof ShipmentEvent.ShipmentStateEvent.ShipmentStateCreated) {
                notificationTypeId = NOTIFICATION_RECEIVING_DOCUMENT_CREATED;
                notificationContent = "New receiving document added";
                logger.info("Processing OrderStateCreated event: {}", e);
            } else if (e instanceof ShipmentEvent.ShipmentStateEvent.ShipmentStateMergePatched) {
                notificationTypeId = NOTIFICATION_RECEIVING_DOCUMENT_UPDATED;
                notificationContent = "Receiving document updated";
                logger.info("Processing OrderStateMergePatched event: {}", e);
            } else {
                continue;
            }
            //TODO FIXME 上面两个类型的事件并不能转换为OrderEvent.OrderStateEvent所以会报错。/2025/4/15发现
//            OrderEvent.OrderStateEvent event = (OrderEvent.OrderStateEvent) e;
//            String orderId = event.getOrderId();
//            String tenantId = event.getTenantId();
//            ShipmentState shipmentState = shipmentApplicationService.get(orderId);
//            if (shipmentState == null) {
//                logger.error("Can't find Receiving document:{}", orderId);
//                continue;
//            }
//            BffNotificationCommand.CreateBffNotification createBffNotification = new AbstractBffNotificationCommand.SimpleCreateBffNotification();
//            createBffNotification.setReferenceDocumentId(orderId);
//            createBffNotification.setReferenceDocumentNumber(orderId);
//            createBffNotification.setBffNotificationId(IdUtils.randomId());
//            createBffNotification.setNotificationTypeId(notificationTypeId);
//            createBffNotification.setReferenceDocumentTypeId(REFERENCE_DOCUMENT_RECEIPT);
//            createBffNotification.setNotificationContent(notificationContent);
//            createBffNotification.setStatusId("Unread");
//            createBffNotification.setPriority(0L);
//            createBffNotification.setRequesterId(shipmentState.getCreatedBy());
//            createBffNotification.setCommandId(createBffNotification.getBffNotificationId());
//
//            bffNotificationApplicationService.when(createBffNotification);
        }
    }

}
