package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.order.AbstractOrderEvent;
import org.dddml.ffvtraceability.domain.order.OrderEventId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEventRepository extends JpaRepository<AbstractOrderEvent, OrderEventId> {
}
