// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.order;

import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public interface OrderItemEventDao {
    void save(OrderItemEvent e);

    Iterable<OrderItemEvent> findByOrderEventId(OrderEventId orderEventId);

}
