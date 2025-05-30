// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.workeffortpurposetype.hibernate;

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
import org.dddml.ffvtraceability.domain.workeffortpurposetype.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.specialization.jpa.*;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class HibernateWorkEffortPurposeTypeStateQueryRepository implements WorkEffortPurposeTypeStateQueryRepository {
    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        EntityManager em = this.entityManager;
        String currentTenantId = TenantContext.getTenantId();
        if (currentTenantId == null || currentTenantId.isEmpty()) {
            throw new IllegalStateException("Tenant context not set");
        }
        if (TenantSupport.SUPER_TENANT_ID != null && !TenantSupport.SUPER_TENANT_ID.isEmpty()
            && TenantSupport.SUPER_TENANT_ID.equals(currentTenantId)) {
            return em;
        }
        org.hibernate.Session session = em.unwrap(org.hibernate.Session.class);
        org.hibernate.Filter filter = session.enableFilter("tenantFilter");
        filter.setParameter("tenantId", currentTenantId);
        filter.validate();
        return em;
    }

    private static final Set<String> readOnlyPropertyPascalCaseNames = new HashSet<String>(Arrays.asList("WorkEffortPurposeTypeId", "ParentTypeId", "Description", "Version", "CreatedBy", "CreatedAt", "UpdatedBy", "UpdatedAt"));

    private ReadOnlyProxyGenerator readOnlyProxyGenerator;

    public ReadOnlyProxyGenerator getReadOnlyProxyGenerator() {
        return readOnlyProxyGenerator;
    }

    public void setReadOnlyProxyGenerator(ReadOnlyProxyGenerator readOnlyProxyGenerator) {
        this.readOnlyProxyGenerator = readOnlyProxyGenerator;
    }

    @Transactional(readOnly = true)
    public WorkEffortPurposeTypeState get(String id) {
        WorkEffortPurposeTypeState state = (WorkEffortPurposeTypeState)getEntityManager().find(AbstractWorkEffortPurposeTypeState.SimpleWorkEffortPurposeTypeState.class, id);
        return state;
    }

    @Transactional(readOnly = true)
    public Iterable<WorkEffortPurposeTypeState> getAll(Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractWorkEffortPurposeTypeState.SimpleWorkEffortPurposeTypeState> cq = cb.createQuery(AbstractWorkEffortPurposeTypeState.SimpleWorkEffortPurposeTypeState.class);
        Root<AbstractWorkEffortPurposeTypeState.SimpleWorkEffortPurposeTypeState> root = cq.from(AbstractWorkEffortPurposeTypeState.SimpleWorkEffortPurposeTypeState.class);
        cq.select(root);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractWorkEffortPurposeTypeState.SimpleWorkEffortPurposeTypeState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(WorkEffortPurposeTypeState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Iterable<WorkEffortPurposeTypeState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractWorkEffortPurposeTypeState.SimpleWorkEffortPurposeTypeState> cq = cb.createQuery(AbstractWorkEffortPurposeTypeState.SimpleWorkEffortPurposeTypeState.class);
        Root<AbstractWorkEffortPurposeTypeState.SimpleWorkEffortPurposeTypeState> root = cq.from(AbstractWorkEffortPurposeTypeState.SimpleWorkEffortPurposeTypeState.class);
        cq.select(root);
        JpaUtils.criteriaAddFilterAndOrders(cb, cq, root, filter, orders);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractWorkEffortPurposeTypeState.SimpleWorkEffortPurposeTypeState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(WorkEffortPurposeTypeState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Iterable<WorkEffortPurposeTypeState> get(org.dddml.support.criterion.Criterion filter, List<String> orders, Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractWorkEffortPurposeTypeState.SimpleWorkEffortPurposeTypeState> cq = cb.createQuery(AbstractWorkEffortPurposeTypeState.SimpleWorkEffortPurposeTypeState.class);
        Root<AbstractWorkEffortPurposeTypeState.SimpleWorkEffortPurposeTypeState> root = cq.from(AbstractWorkEffortPurposeTypeState.SimpleWorkEffortPurposeTypeState.class);
        cq.select(root);
        JpaUtils.criteriaAddFilterAndOrders(cb, cq, root, filter, orders);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractWorkEffortPurposeTypeState.SimpleWorkEffortPurposeTypeState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(WorkEffortPurposeTypeState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WorkEffortPurposeTypeState getFirst(Iterable<Map.Entry<String, Object>> filter, List<String> orders) {
        List<WorkEffortPurposeTypeState> list = (List<WorkEffortPurposeTypeState>)get(filter, orders, 0, 1);
        if (list == null || list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    @Transactional(readOnly = true)
    public WorkEffortPurposeTypeState getFirst(Map.Entry<String, Object> keyValue, List<String> orders) {
        List<Map.Entry<String, Object>> filter = new ArrayList<>();
        filter.add(keyValue);
        return getFirst(filter, orders);
    }

    @Transactional(readOnly = true)
    public Iterable<WorkEffortPurposeTypeState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults) {
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
        Root<AbstractWorkEffortPurposeTypeState.SimpleWorkEffortPurposeTypeState> root = cq.from(AbstractWorkEffortPurposeTypeState.SimpleWorkEffortPurposeTypeState.class);
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
        Root<AbstractWorkEffortPurposeTypeState.SimpleWorkEffortPurposeTypeState> root = cq.from(AbstractWorkEffortPurposeTypeState.SimpleWorkEffortPurposeTypeState.class);
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

