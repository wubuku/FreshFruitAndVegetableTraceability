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
public class HibernatePostalAddressStateQueryRepository implements PostalAddressStateQueryRepository {
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
    public PostalAddressState get(String id) {
        PostalAddressState state = (PostalAddressState)getEntityManager().find(AbstractPostalAddressState.SimplePostalAddressState.class, id);
        return state;
    }

    @Transactional(readOnly = true)
    public Iterable<PostalAddressState> getAll(Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractPostalAddressState.SimplePostalAddressState> cq = cb.createQuery(AbstractPostalAddressState.SimplePostalAddressState.class);
        Root<AbstractPostalAddressState.SimplePostalAddressState> root = cq.from(AbstractPostalAddressState.SimplePostalAddressState.class);
        cq.select(root);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractPostalAddressState.SimplePostalAddressState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(PostalAddressState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Iterable<PostalAddressState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractPostalAddressState.SimplePostalAddressState> cq = cb.createQuery(AbstractPostalAddressState.SimplePostalAddressState.class);
        Root<AbstractPostalAddressState.SimplePostalAddressState> root = cq.from(AbstractPostalAddressState.SimplePostalAddressState.class);
        cq.select(root);
        JpaUtils.criteriaAddFilterAndOrders(cb, cq, root, filter, orders);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractPostalAddressState.SimplePostalAddressState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(PostalAddressState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Iterable<PostalAddressState> get(org.dddml.support.criterion.Criterion filter, List<String> orders, Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractPostalAddressState.SimplePostalAddressState> cq = cb.createQuery(AbstractPostalAddressState.SimplePostalAddressState.class);
        Root<AbstractPostalAddressState.SimplePostalAddressState> root = cq.from(AbstractPostalAddressState.SimplePostalAddressState.class);
        cq.select(root);
        JpaUtils.criteriaAddFilterAndOrders(cb, cq, root, filter, orders);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractPostalAddressState.SimplePostalAddressState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(PostalAddressState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostalAddressState getFirst(Iterable<Map.Entry<String, Object>> filter, List<String> orders) {
        List<PostalAddressState> list = (List<PostalAddressState>)get(filter, orders, 0, 1);
        if (list == null || list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    @Transactional(readOnly = true)
    public PostalAddressState getFirst(Map.Entry<String, Object> keyValue, List<String> orders) {
        List<Map.Entry<String, Object>> filter = new ArrayList<>();
        filter.add(keyValue);
        return getFirst(filter, orders);
    }

    @Transactional(readOnly = true)
    public Iterable<PostalAddressState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults) {
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
        Root<AbstractPostalAddressState.SimplePostalAddressState> root = cq.from(AbstractPostalAddressState.SimplePostalAddressState.class);
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
        Root<AbstractPostalAddressState.SimplePostalAddressState> root = cq.from(AbstractPostalAddressState.SimplePostalAddressState.class);
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
