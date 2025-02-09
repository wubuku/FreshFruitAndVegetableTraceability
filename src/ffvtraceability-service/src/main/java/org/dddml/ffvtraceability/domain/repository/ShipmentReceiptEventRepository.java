package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.order.AbstractOrderEvent;
import org.dddml.ffvtraceability.domain.order.OrderEventId;
import org.dddml.ffvtraceability.domain.shipmentreceipt.AbstractShipmentReceiptEvent;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptEvent;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptEventId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentReceiptEventRepository extends JpaRepository<AbstractShipmentReceiptEvent, ShipmentReceiptEventId> {
}
