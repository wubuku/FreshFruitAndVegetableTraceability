// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.facility.hibernate;

import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import jakarta.persistence.criteria.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.dddml.ffvtraceability.domain.facility.*;
import org.dddml.ffvtraceability.specialization.*;
import org.springframework.transaction.annotation.Transactional;

@Component("facilityIdentificationEventDao")
public class HibernateFacilityIdentificationEventDao implements FacilityIdentificationEventDao {
    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    public void save(FacilityIdentificationEvent e) {
        getEntityManager().persist(e);
        if (e instanceof Saveable) {
            Saveable saveable = (Saveable) e;
            saveable.save();
        }
    }


    @Transactional(readOnly = true)
    @Override
    public Iterable<FacilityIdentificationEvent> findByFacilityEventId(FacilityEventId facilityEventId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<FacilityIdentificationEvent> cq = cb.createQuery(FacilityIdentificationEvent.class);
        Root<AbstractFacilityIdentificationEvent> root = cq.from(AbstractFacilityIdentificationEvent.class);

        Predicate condition = cb.and(
            cb.equal(root.get("facilityIdentificationEventId").get("facilityId"), facilityEventId.getFacilityId()),
            cb.equal(root.get("facilityIdentificationEventId").get("facilityVersion"), facilityEventId.getVersion())
        );

        cq.where(condition);

        return getEntityManager().createQuery(cq).getResultList();
    }

}
