// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.inventoryitem.hibernate;

import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import jakarta.persistence.criteria.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.dddml.ffvtraceability.domain.inventoryitem.*;
import org.dddml.ffvtraceability.specialization.*;
import org.springframework.transaction.annotation.Transactional;

@Component("inventoryItemDetailEventDao")
public class HibernateInventoryItemDetailEventDao implements InventoryItemDetailEventDao {
    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    public void save(InventoryItemDetailEvent e) {
        InventoryItemDetailState state = ((AbstractInventoryItemDetailEvent.AbstractInventoryItemDetailStateCreated)e).getInventoryItemDetailState();
        getEntityManager().persist(e);
        getEntityManager().persist(e);

        if (e instanceof Saveable) {
            Saveable saveable = (Saveable) e;
            saveable.save();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<InventoryItemDetailEvent> findByInventoryItemEventId(InventoryItemEventId inventoryItemEventId) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InventoryItemDetailState> cq = cb.createQuery(InventoryItemDetailState.class);
        Root<InventoryItemDetailState> root = cq.from(InventoryItemDetailState.class);
        cq.select(root);

        java.util.List<Predicate> predicates = new java.util.ArrayList<>();
        predicates.add(cb.equal(root.get("inventoryItemDetailId").get("inventoryItemId"), inventoryItemEventId.getInventoryItemId()));
        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        return em.createQuery(cq).getResultList()
                .stream()
                .map(s -> new AbstractInventoryItemDetailEvent.SimpleInventoryItemDetailStateCreated(s))
                .collect(java.util.stream.Collectors.toList());
    }

}

