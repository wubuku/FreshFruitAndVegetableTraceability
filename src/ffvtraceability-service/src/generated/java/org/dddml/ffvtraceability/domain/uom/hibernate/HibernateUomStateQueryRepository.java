// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.uom.hibernate;

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
import org.dddml.ffvtraceability.domain.uom.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.specialization.jpa.*;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class HibernateUomStateQueryRepository implements UomStateQueryRepository {
    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    private static final Set<String> readOnlyPropertyPascalCaseNames = new HashSet<String>(Arrays.asList("UomId", "UomTypeId", "Abbreviation", "NumericCode", "Gs1AI", "Description", "Version", "CreatedBy", "CreatedAt", "UpdatedBy", "UpdatedAt", "Active", "Deleted"));

    private ReadOnlyProxyGenerator readOnlyProxyGenerator;

    public ReadOnlyProxyGenerator getReadOnlyProxyGenerator() {
        return readOnlyProxyGenerator;
    }

    public void setReadOnlyProxyGenerator(ReadOnlyProxyGenerator readOnlyProxyGenerator) {
        this.readOnlyProxyGenerator = readOnlyProxyGenerator;
    }

    @Transactional(readOnly = true)
    public UomState get(String id) {
        UomState state = (UomState)getEntityManager().find(AbstractUomState.SimpleUomState.class, id);
        return state;
    }

    @Transactional(readOnly = true)
    public Iterable<UomState> getAll(Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractUomState.SimpleUomState> cq = cb.createQuery(AbstractUomState.SimpleUomState.class);
        Root<AbstractUomState.SimpleUomState> root = cq.from(AbstractUomState.SimpleUomState.class);
        cq.select(root);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractUomState.SimpleUomState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(UomState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Iterable<UomState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractUomState.SimpleUomState> cq = cb.createQuery(AbstractUomState.SimpleUomState.class);
        Root<AbstractUomState.SimpleUomState> root = cq.from(AbstractUomState.SimpleUomState.class);
        cq.select(root);
        JpaUtils.criteriaAddFilterAndOrders(cb, cq, root, filter, orders);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractUomState.SimpleUomState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(UomState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Iterable<UomState> get(org.dddml.support.criterion.Criterion filter, List<String> orders, Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractUomState.SimpleUomState> cq = cb.createQuery(AbstractUomState.SimpleUomState.class);
        Root<AbstractUomState.SimpleUomState> root = cq.from(AbstractUomState.SimpleUomState.class);
        cq.select(root);
        JpaUtils.criteriaAddFilterAndOrders(cb, cq, root, filter, orders);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractUomState.SimpleUomState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(UomState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UomState getFirst(Iterable<Map.Entry<String, Object>> filter, List<String> orders) {
        List<UomState> list = (List<UomState>)get(filter, orders, 0, 1);
        if (list == null || list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    @Transactional(readOnly = true)
    public UomState getFirst(Map.Entry<String, Object> keyValue, List<String> orders) {
        List<Map.Entry<String, Object>> filter = new ArrayList<>();
        filter.add(keyValue);
        return getFirst(filter, orders);
    }

    @Transactional(readOnly = true)
    public Iterable<UomState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults) {
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
        Root<AbstractUomState.SimpleUomState> root = cq.from(AbstractUomState.SimpleUomState.class);
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
        Root<AbstractUomState.SimpleUomState> root = cq.from(AbstractUomState.SimpleUomState.class);
        cq.select(cb.count(root));
        if (filter != null) {
            JpaUtils.criteriaAddFilter(cb, cq, root, filter);
        }
        addNotDeletedRestriction(cb, cq, root);
        return em.createQuery(cq).getSingleResult();
    }

    protected void addNotDeletedRestriction(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<?> root) {
        Predicate isNull = cb.isNull(root.get("deleted"));
        Predicate isFalse = cb.equal(root.get("deleted"), false);
        Predicate notDeleted = cb.or(isNull, isFalse);
        cq.where(cq.getRestriction() == null ? notDeleted : cb.and(cq.getRestriction(), notDeleted));
    }

}
