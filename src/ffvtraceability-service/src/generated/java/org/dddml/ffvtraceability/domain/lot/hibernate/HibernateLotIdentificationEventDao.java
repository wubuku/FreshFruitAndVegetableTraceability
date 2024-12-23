// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.lot.hibernate;

import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import jakarta.persistence.criteria.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.dddml.ffvtraceability.domain.lot.*;
import org.dddml.ffvtraceability.specialization.*;
import org.springframework.transaction.annotation.Transactional;

@Component("lotIdentificationEventDao")
public class HibernateLotIdentificationEventDao implements LotIdentificationEventDao {
    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    public void save(LotIdentificationEvent e) {
        getEntityManager().persist(e);
        if (e instanceof Saveable) {
            Saveable saveable = (Saveable) e;
            saveable.save();
        }
    }


    @Transactional(readOnly = true)
    @Override
    public Iterable<LotIdentificationEvent> findByLotEventId(LotEventId lotEventId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<LotIdentificationEvent> cq = cb.createQuery(LotIdentificationEvent.class);
        Root<AbstractLotIdentificationEvent> root = cq.from(AbstractLotIdentificationEvent.class);

        Predicate condition = cb.and(
            cb.equal(root.get("lotIdentificationEventId").get("lotId"), lotEventId.getLotId()),
            cb.equal(root.get("lotIdentificationEventId").get("lotVersion"), lotEventId.getVersion())
        );

        cq.where(condition);

        return getEntityManager().createQuery(cq).getResultList();
    }

}

