// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.order.hibernate;

import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import jakarta.persistence.criteria.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.dddml.ffvtraceability.domain.order.*;
import org.dddml.ffvtraceability.specialization.*;
import org.springframework.transaction.annotation.Transactional;

@Component("orderContactMechEventDao")
public class HibernateOrderContactMechEventDao implements OrderContactMechEventDao {
    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    public void save(OrderContactMechEvent e) {
        getEntityManager().persist(e);
        if (e instanceof Saveable) {
            Saveable saveable = (Saveable) e;
            saveable.save();
        }
    }


    @Transactional(readOnly = true)
    @Override
    public Iterable<OrderContactMechEvent> findByOrderEventId(OrderEventId orderEventId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<OrderContactMechEvent> cq = cb.createQuery(OrderContactMechEvent.class);
        Root<AbstractOrderContactMechEvent> root = cq.from(AbstractOrderContactMechEvent.class);

        Predicate condition = cb.and(
            cb.equal(root.get("orderContactMechEventId").get("orderId"), orderEventId.getOrderId()),
            cb.equal(root.get("orderContactMechEventId").get("orderHeaderVersion"), orderEventId.getVersion())
        );

        cq.where(condition);

        return getEntityManager().createQuery(cq).getResultList();
    }

}
