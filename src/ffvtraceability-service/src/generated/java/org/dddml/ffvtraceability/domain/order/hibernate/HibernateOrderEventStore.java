// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.order.hibernate;

import java.io.Serializable;
import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.partyrole.*;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.specialization.hibernate.AbstractHibernateEventStore;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.dddml.ffvtraceability.domain.order.*;

@Component("orderEventStore")
public class HibernateOrderEventStore extends AbstractHibernateEventStore {
    @Override
    protected Serializable getEventId(EventStoreAggregateId eventStoreAggregateId, long version)
    {
        return new OrderEventId((String) eventStoreAggregateId.getId(), Long.valueOf(version));
    }

    @Override
    protected Class getSupportedEventType()
    {
        return AbstractOrderEvent.class;
    }

    @Transactional(readOnly = true)
    @Override
    public EventStream loadEventStream(Class eventType, EventStoreAggregateId eventStoreAggregateId, long version) {
        Class supportedEventType = AbstractOrderEvent.class;
        if (!eventType.isAssignableFrom(supportedEventType)) {
            throw new UnsupportedOperationException();
        }
        String idObj = (String) eventStoreAggregateId.getId();
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<AbstractOrderEvent> query = cb.createQuery(AbstractOrderEvent.class);
        Root<AbstractOrderEvent> root = query.from(AbstractOrderEvent.class);
   
        query.select(root)
                .where(cb.and(
                    cb.equal(root.get("orderEventId").get("orderId"), idObj),
                    cb.lessThanOrEqualTo(root.get("orderEventId").get("version"), version)
                ))
                .orderBy(cb.asc(root.get("orderEventId").get("version")));

        List<AbstractOrderEvent> es = getEntityManager().createQuery(query).getResultList();
        for (Object e : es) {
            ((AbstractOrderEvent) e).setEventReadOnly(true);
        }
        EventStream eventStream = new EventStream();
        if (es.size() > 0) {
            eventStream.setSteamVersion(((AbstractOrderEvent) es.get(es.size() - 1)).getOrderEventId().getVersion());
        } else {
        }
        eventStream.setEvents(new ArrayList<>(es));
        return eventStream;
    }

}
