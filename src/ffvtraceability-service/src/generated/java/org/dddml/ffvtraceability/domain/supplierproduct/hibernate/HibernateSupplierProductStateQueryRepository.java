// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.supplierproduct.hibernate;

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
import org.dddml.ffvtraceability.domain.supplierproduct.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.specialization.jpa.*;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class HibernateSupplierProductStateQueryRepository implements SupplierProductStateQueryRepository {
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

    private static final Set<String> readOnlyPropertyPascalCaseNames = new HashSet<String>(Arrays.asList("SupplierProductTenantizedId", "AvailableThruDate", "BrandName", "Gtin", "QuantityIncluded", "PiecesIncluded", "CaseUomId", "OrganicCertifications", "MaterialCompositionDescription", "CountryOfOrigin", "CertificationCodes", "IndividualsPerPackage", "ProduceVariety", "HsCode", "StorageConditions", "ShelfLifeDescription", "HandlingInstructions", "WeightUomId", "ShippingWeight", "ProductWeight", "DimensionsDescription", "SupplierPrefOrderId", "SupplierRatingTypeId", "StandardLeadTimeDays", "OrderQtyIncrements", "UnitsIncluded", "QuantityUomId", "AgreementId", "AgreementItemSeqId", "LastPrice", "ShippingPrice", "SupplierProductId", "SupplierProductName", "CanDropShip", "Comments", "TaxInPrice", "TaxAmount", "TaxPercentage", "LimitQuantityPerCustomer", "LimitQuantityPerOrder", "ProductPriceTypeId", "ShipmentMethodTypeId", "Version", "CreatedBy", "CreatedAt", "UpdatedBy", "UpdatedAt"));

    private ReadOnlyProxyGenerator readOnlyProxyGenerator;

    public ReadOnlyProxyGenerator getReadOnlyProxyGenerator() {
        return readOnlyProxyGenerator;
    }

    public void setReadOnlyProxyGenerator(ReadOnlyProxyGenerator readOnlyProxyGenerator) {
        this.readOnlyProxyGenerator = readOnlyProxyGenerator;
    }

    @Transactional(readOnly = true)
    public SupplierProductState get(SupplierProductTenantizedId id) {
        SupplierProductState state = (SupplierProductState)getEntityManager().find(AbstractSupplierProductState.SimpleSupplierProductState.class, id);
        return state;
    }

    @Transactional(readOnly = true)
    public Iterable<SupplierProductState> getAll(Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractSupplierProductState.SimpleSupplierProductState> cq = cb.createQuery(AbstractSupplierProductState.SimpleSupplierProductState.class);
        Root<AbstractSupplierProductState.SimpleSupplierProductState> root = cq.from(AbstractSupplierProductState.SimpleSupplierProductState.class);
        cq.select(root);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractSupplierProductState.SimpleSupplierProductState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(SupplierProductState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Iterable<SupplierProductState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractSupplierProductState.SimpleSupplierProductState> cq = cb.createQuery(AbstractSupplierProductState.SimpleSupplierProductState.class);
        Root<AbstractSupplierProductState.SimpleSupplierProductState> root = cq.from(AbstractSupplierProductState.SimpleSupplierProductState.class);
        cq.select(root);
        JpaUtils.criteriaAddFilterAndOrders(cb, cq, root, filter, orders);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractSupplierProductState.SimpleSupplierProductState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(SupplierProductState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Iterable<SupplierProductState> get(org.dddml.support.criterion.Criterion filter, List<String> orders, Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractSupplierProductState.SimpleSupplierProductState> cq = cb.createQuery(AbstractSupplierProductState.SimpleSupplierProductState.class);
        Root<AbstractSupplierProductState.SimpleSupplierProductState> root = cq.from(AbstractSupplierProductState.SimpleSupplierProductState.class);
        cq.select(root);
        JpaUtils.criteriaAddFilterAndOrders(cb, cq, root, filter, orders);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractSupplierProductState.SimpleSupplierProductState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(SupplierProductState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SupplierProductState getFirst(Iterable<Map.Entry<String, Object>> filter, List<String> orders) {
        List<SupplierProductState> list = (List<SupplierProductState>)get(filter, orders, 0, 1);
        if (list == null || list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    @Transactional(readOnly = true)
    public SupplierProductState getFirst(Map.Entry<String, Object> keyValue, List<String> orders) {
        List<Map.Entry<String, Object>> filter = new ArrayList<>();
        filter.add(keyValue);
        return getFirst(filter, orders);
    }

    @Transactional(readOnly = true)
    public Iterable<SupplierProductState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults) {
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
        Root<AbstractSupplierProductState.SimpleSupplierProductState> root = cq.from(AbstractSupplierProductState.SimpleSupplierProductState.class);
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
        Root<AbstractSupplierProductState.SimpleSupplierProductState> root = cq.from(AbstractSupplierProductState.SimpleSupplierProductState.class);
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

