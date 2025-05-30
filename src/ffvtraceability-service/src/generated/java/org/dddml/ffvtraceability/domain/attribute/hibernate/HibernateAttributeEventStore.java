// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.attribute.hibernate;

import java.io.Serializable;
import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.specialization.hibernate.AbstractHibernateEventStore;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.dddml.ffvtraceability.domain.attribute.*;

@Component("attributeEventStore")
public class HibernateAttributeEventStore extends AbstractHibernateEventStore {
    @Override
    protected Serializable getEventId(EventStoreAggregateId eventStoreAggregateId, long version)
    {
        return new AttributeEventId((String) eventStoreAggregateId.getId(), Long.valueOf(version));
    }

    @Override
    protected Class getSupportedEventType()
    {
        return AbstractAttributeEvent.class;
    }

    @Transactional(readOnly = true)
    @Override
    public EventStream loadEventStream(Class eventType, EventStoreAggregateId eventStoreAggregateId, long version) {
        Class supportedEventType = AbstractAttributeEvent.class;
        if (!eventType.isAssignableFrom(supportedEventType)) {
            throw new UnsupportedOperationException();
        }
        String idObj = (String) eventStoreAggregateId.getId();
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<AbstractAttributeEvent> query = cb.createQuery(AbstractAttributeEvent.class);
        Root<AbstractAttributeEvent> root = query.from(AbstractAttributeEvent.class);
   
        query.select(root)
                .where(cb.and(
                    cb.equal(root.get("attributeEventId").get("attributeId"), idObj),
                    cb.lessThanOrEqualTo(root.get("attributeEventId").get("version"), version)
                ))
                .orderBy(cb.asc(root.get("attributeEventId").get("version")));

        List<AbstractAttributeEvent> es = getEntityManager().createQuery(query).getResultList();
        for (Object e : es) {
            ((AbstractAttributeEvent) e).setEventReadOnly(true);
        }
        EventStream eventStream = new EventStream();
        if (es.size() > 0) {
            eventStream.setSteamVersion(((AbstractAttributeEvent) es.get(es.size() - 1)).getAttributeEventId().getVersion());
        } else {
        }
        eventStream.setEvents(new ArrayList<>(es));
        return eventStream;
    }

}

