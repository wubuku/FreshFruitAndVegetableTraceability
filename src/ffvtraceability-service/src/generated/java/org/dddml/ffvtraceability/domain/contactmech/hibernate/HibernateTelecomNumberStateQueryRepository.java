// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.contactmech.hibernate;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.hibernate.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.stream.Collectors;
import org.dddml.ffvtraceability.domain.contactmech.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.specialization.jpa.*;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class HibernateTelecomNumberStateQueryRepository implements TelecomNumberStateQueryRepository {
    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    private static final Set<String> readOnlyPropertyPascalCaseNames = new HashSet<String>(Arrays.asList("ContactMechId", "ContactMechTypeId", "InfoString", "ToName", "AttnName", "Address1", "Address2", "Directions", "City", "PostalCode", "PostalCodeExt", "CountryGeoId", "StateProvinceGeoId", "PrefectureGeoId", "CountyGeoId", "TownGeoId", "AssocTelecomContactMechId", "PostalCodeGeoId", "GeoPointId", "CountryCode", "AreaCode", "ContactNumber", "AskForName", "Version", "CreatedBy", "CreatedAt", "UpdatedBy", "UpdatedAt"));

    private ReadOnlyProxyGenerator readOnlyProxyGenerator;

    public ReadOnlyProxyGenerator getReadOnlyProxyGenerator() {
        return readOnlyProxyGenerator;
    }

    public void setReadOnlyProxyGenerator(ReadOnlyProxyGenerator readOnlyProxyGenerator) {
        this.readOnlyProxyGenerator = readOnlyProxyGenerator;
    }

    @Transactional(readOnly = true)
    public TelecomNumberState get(String id) {
        TelecomNumberState state = (TelecomNumberState)getEntityManager().find(AbstractTelecomNumberState.SimpleTelecomNumberState.class, id);
        return state;
    }

    @Transactional(readOnly = true)
    public Iterable<TelecomNumberState> getAll(Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractTelecomNumberState.SimpleTelecomNumberState> cq = cb.createQuery(AbstractTelecomNumberState.SimpleTelecomNumberState.class);
        Root<AbstractTelecomNumberState.SimpleTelecomNumberState> root = cq.from(AbstractTelecomNumberState.SimpleTelecomNumberState.class);
        cq.select(root);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractTelecomNumberState.SimpleTelecomNumberState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(TelecomNumberState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Iterable<TelecomNumberState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractTelecomNumberState.SimpleTelecomNumberState> cq = cb.createQuery(AbstractTelecomNumberState.SimpleTelecomNumberState.class);
        Root<AbstractTelecomNumberState.SimpleTelecomNumberState> root = cq.from(AbstractTelecomNumberState.SimpleTelecomNumberState.class);
        cq.select(root);
        JpaUtils.criteriaAddFilterAndOrders(cb, cq, root, filter, orders);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractTelecomNumberState.SimpleTelecomNumberState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(TelecomNumberState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Iterable<TelecomNumberState> get(org.dddml.support.criterion.Criterion filter, List<String> orders, Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractTelecomNumberState.SimpleTelecomNumberState> cq = cb.createQuery(AbstractTelecomNumberState.SimpleTelecomNumberState.class);
        Root<AbstractTelecomNumberState.SimpleTelecomNumberState> root = cq.from(AbstractTelecomNumberState.SimpleTelecomNumberState.class);
        cq.select(root);
        JpaUtils.criteriaAddFilterAndOrders(cb, cq, root, filter, orders);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractTelecomNumberState.SimpleTelecomNumberState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(TelecomNumberState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TelecomNumberState getFirst(Iterable<Map.Entry<String, Object>> filter, List<String> orders) {
        List<TelecomNumberState> list = (List<TelecomNumberState>)get(filter, orders, 0, 1);
        if (list == null || list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    @Transactional(readOnly = true)
    public TelecomNumberState getFirst(Map.Entry<String, Object> keyValue, List<String> orders) {
        List<Map.Entry<String, Object>> filter = new ArrayList<>();
        filter.add(keyValue);
        return getFirst(filter, orders);
    }

    @Transactional(readOnly = true)
    public Iterable<TelecomNumberState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults) {
        Map.Entry<String, Object> keyValue = new AbstractMap.SimpleEntry<>(propertyName, propertyValue);
        List<Map.Entry<String, Object>> filter = new ArrayList<>();
        filter.add(keyValue);
        return get(filter, orders, firstResult, maxResults);
    }

    @Transactional(readOnly = true)
    public long getCount(Iterable<Map.Entry<String, Object>> filter) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<AbstractTelecomNumberState.SimpleTelecomNumberState> root = cq.from(AbstractTelecomNumberState.SimpleTelecomNumberState.class);
        cq.select(cb.count(root));
        if (filter != null) {
            JpaUtils.criteriaAddFilter(cb, cq, root, filter);
        }
        addNotDeletedRestriction(cb, cq, root);
        return em.createQuery(cq).getSingleResult();
    }

    @Transactional(readOnly = true)
    public long getCount(org.dddml.support.criterion.Criterion filter) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<AbstractTelecomNumberState.SimpleTelecomNumberState> root = cq.from(AbstractTelecomNumberState.SimpleTelecomNumberState.class);
        cq.select(cb.count(root));
        if (filter != null) {
            JpaUtils.criteriaAddFilter(cb, cq, root, filter);
        }
        addNotDeletedRestriction(cb, cq, root);
        return em.createQuery(cq).getSingleResult();
    }

    protected void addNotDeletedRestriction(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<?> root) {
    }

}
