// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.tenant.hibernate;

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
import org.dddml.ffvtraceability.domain.tenant.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.specialization.jpa.*;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class HibernateTenantStateQueryRepository implements TenantStateQueryRepository {
    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    private static final Set<String> readOnlyPropertyPascalCaseNames = new HashSet<String>(Arrays.asList("TenantId", "PartyId", "TimeZoneId", "DateTimeFormat", "Description", "LongDescription", "Version", "CreatedBy", "CreatedAt", "UpdatedBy", "UpdatedAt"));

    private ReadOnlyProxyGenerator readOnlyProxyGenerator;

    public ReadOnlyProxyGenerator getReadOnlyProxyGenerator() {
        return readOnlyProxyGenerator;
    }

    public void setReadOnlyProxyGenerator(ReadOnlyProxyGenerator readOnlyProxyGenerator) {
        this.readOnlyProxyGenerator = readOnlyProxyGenerator;
    }

    @Transactional(readOnly = true)
    public TenantState get(String id) {
        TenantState state = (TenantState)getEntityManager().find(AbstractTenantState.SimpleTenantState.class, id);
        return state;
    }

    @Transactional(readOnly = true)
    public Iterable<TenantState> getAll(Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractTenantState.SimpleTenantState> cq = cb.createQuery(AbstractTenantState.SimpleTenantState.class);
        Root<AbstractTenantState.SimpleTenantState> root = cq.from(AbstractTenantState.SimpleTenantState.class);
        cq.select(root);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractTenantState.SimpleTenantState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(TenantState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Iterable<TenantState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractTenantState.SimpleTenantState> cq = cb.createQuery(AbstractTenantState.SimpleTenantState.class);
        Root<AbstractTenantState.SimpleTenantState> root = cq.from(AbstractTenantState.SimpleTenantState.class);
        cq.select(root);
        JpaUtils.criteriaAddFilterAndOrders(cb, cq, root, filter, orders);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractTenantState.SimpleTenantState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(TenantState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Iterable<TenantState> get(org.dddml.support.criterion.Criterion filter, List<String> orders, Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractTenantState.SimpleTenantState> cq = cb.createQuery(AbstractTenantState.SimpleTenantState.class);
        Root<AbstractTenantState.SimpleTenantState> root = cq.from(AbstractTenantState.SimpleTenantState.class);
        cq.select(root);
        JpaUtils.criteriaAddFilterAndOrders(cb, cq, root, filter, orders);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractTenantState.SimpleTenantState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(TenantState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TenantState getFirst(Iterable<Map.Entry<String, Object>> filter, List<String> orders) {
        List<TenantState> list = (List<TenantState>)get(filter, orders, 0, 1);
        if (list == null || list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    @Transactional(readOnly = true)
    public TenantState getFirst(Map.Entry<String, Object> keyValue, List<String> orders) {
        List<Map.Entry<String, Object>> filter = new ArrayList<>();
        filter.add(keyValue);
        return getFirst(filter, orders);
    }

    @Transactional(readOnly = true)
    public Iterable<TenantState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults) {
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
        Root<AbstractTenantState.SimpleTenantState> root = cq.from(AbstractTenantState.SimpleTenantState.class);
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
        Root<AbstractTenantState.SimpleTenantState> root = cq.from(AbstractTenantState.SimpleTenantState.class);
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

