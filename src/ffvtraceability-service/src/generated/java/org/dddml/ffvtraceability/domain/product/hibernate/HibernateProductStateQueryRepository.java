// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.product.hibernate;

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
import org.dddml.ffvtraceability.domain.product.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.specialization.jpa.*;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class HibernateProductStateQueryRepository implements ProductStateQueryRepository {
    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    private static final Set<String> readOnlyPropertyPascalCaseNames = new HashSet<String>(Arrays.asList("ProductId", "ProductTypeId", "PrimaryProductCategoryId", "ManufacturerPartyId", "FacilityId", "IntroductionDate", "ReleaseDate", "SupportDiscontinuationDate", "SalesDiscontinuationDate", "SalesDiscWhenNotAvail", "InternalName", "BrandName", "Comments", "ProductName", "Description", "LongDescription", "PriceDetailText", "SmallImageUrl", "MediumImageUrl", "LargeImageUrl", "DetailImageUrl", "OriginalImageUrl", "DetailScreen", "InventoryMessage", "QuantityUomId", "QuantityIncluded", "PiecesIncluded", "RequireAmount", "FixedAmount", "AmountUomTypeId", "WeightUomId", "ShippingWeight", "ProductWeight", "HeightUomId", "ProductHeight", "ShippingHeight", "WidthUomId", "ProductWidth", "ShippingWidth", "DepthUomId", "ProductDepth", "ShippingDepth", "DiameterUomId", "ProductDiameter", "ProductRating", "RatingTypeEnum", "Returnable", "Taxable", "ChargeShipping", "AutoCreateKeywords", "IncludeInPromotions", "IsVirtual", "IsVariant", "VirtualVariantMethodEnum", "OriginGeoId", "RequirementMethodEnumId", "BillOfMaterialLevel", "InShippingBox", "DefaultShipmentBoxTypeId", "LotIdFilledIn", "OrderDecimalQuantity", "GoodIdentifications", "Version", "CreatedBy", "CreatedAt", "UpdatedBy", "UpdatedAt", "Active", "Deleted"));

    private ReadOnlyProxyGenerator readOnlyProxyGenerator;

    public ReadOnlyProxyGenerator getReadOnlyProxyGenerator() {
        return readOnlyProxyGenerator;
    }

    public void setReadOnlyProxyGenerator(ReadOnlyProxyGenerator readOnlyProxyGenerator) {
        this.readOnlyProxyGenerator = readOnlyProxyGenerator;
    }

    @Transactional(readOnly = true)
    public ProductState get(String id) {
        ProductState state = (ProductState)getEntityManager().find(AbstractProductState.SimpleProductState.class, id);
        return state;
    }

    @Transactional(readOnly = true)
    public Iterable<ProductState> getAll(Integer firstResult, Integer maxResults) {
        return getAll(ProductState.class, firstResult, maxResults);
    }
    
    @Transactional(readOnly = true)
    public Iterable<ProductState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults) {
        return get(ProductState.class, filter, orders, firstResult, maxResults);
    }

    @Transactional(readOnly = true)
    public Iterable<ProductState> get(org.dddml.support.criterion.Criterion filter, List<String> orders, Integer firstResult, Integer maxResults) {
        return get(ProductState.class, filter, orders, firstResult, maxResults);
    }

    @Transactional(readOnly = true)
    public ProductState getFirst(Iterable<Map.Entry<String, Object>> filter, List<String> orders) {
        return getFirst(ProductState.class, filter, orders);
    }

    @Transactional(readOnly = true)
    public ProductState getFirst(Map.Entry<String, Object> keyValue, List<String> orders) {
        return getFirst(ProductState.class, keyValue, orders);
    }

    @Transactional(readOnly = true)
    public Iterable<ProductState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults) {
        return getByProperty(ProductState.class, propertyName, propertyValue, orders, firstResult, maxResults);
    }

    @Transactional(readOnly = true)
    public long getCount(Iterable<Map.Entry<String, Object>> filter) {
        return getCount(ProductState.class, filter);
    }

    @Transactional(readOnly = true)
    public long getCount(org.dddml.support.criterion.Criterion filter) {
        return getCount(ProductState.class, filter);
    }
    // //////////////////////////////////////

    @Transactional(readOnly = true)
    public Iterable<ProductState> getAll(Class<? extends ProductState> stateType, Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractProductState.SimpleProductState> cq = cb.createQuery(AbstractProductState.SimpleProductState.class);
        Root<AbstractProductState.SimpleProductState> root = cq.from(AbstractProductState.SimpleProductState.class);
        cq.select(root);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractProductState.SimpleProductState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(ProductState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Iterable<ProductState> get(Class<? extends ProductState> stateType, Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractProductState.SimpleProductState> cq = cb.createQuery(AbstractProductState.SimpleProductState.class);
        Root<AbstractProductState.SimpleProductState> root = cq.from(AbstractProductState.SimpleProductState.class);
        cq.select(root);
        JpaUtils.criteriaAddFilterAndOrders(cb, cq, root, filter, orders);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractProductState.SimpleProductState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(ProductState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Iterable<ProductState> get(Class<? extends ProductState> stateType, org.dddml.support.criterion.Criterion filter, List<String> orders, Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractProductState.SimpleProductState> cq = cb.createQuery(AbstractProductState.SimpleProductState.class);
        Root<AbstractProductState.SimpleProductState> root = cq.from(AbstractProductState.SimpleProductState.class);
        cq.select(root);
        JpaUtils.criteriaAddFilterAndOrders(cb, cq, root, filter, orders);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractProductState.SimpleProductState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(ProductState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductState getFirst(Class<? extends ProductState> stateType, Iterable<Map.Entry<String, Object>> filter, List<String> orders) {
        List<ProductState> list = (List<ProductState>)get(stateType, filter, orders, 0, 1);
        if (list == null || list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    @Transactional(readOnly = true)
    public ProductState getFirst(Class<? extends ProductState> stateType, Map.Entry<String, Object> keyValue, List<String> orders) {
        List<Map.Entry<String, Object>> filter = new ArrayList<>();
        filter.add(keyValue);
        return getFirst(stateType, filter, orders);
    }

    @Transactional(readOnly = true)
    public Iterable<ProductState> getByProperty(Class<? extends ProductState> stateType, String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults) {
        Map.Entry<String, Object> keyValue = new AbstractMap.SimpleEntry<>(propertyName, propertyValue);
        List<Map.Entry<String, Object>> filter = new ArrayList<>();
        filter.add(keyValue);
        return get(filter, orders, firstResult, maxResults);
    }

    @Transactional(readOnly = true)
    public long getCount(Class<? extends ProductState> stateType, Iterable<Map.Entry<String, Object>> filter) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<AbstractProductState.SimpleProductState> root = cq.from(AbstractProductState.SimpleProductState.class);
        cq.select(cb.count(root));
        if (filter != null) {
            JpaUtils.criteriaAddFilter(cb, cq, root, filter);
        }
        addNotDeletedRestriction(cb, cq, root);
        return em.createQuery(cq).getSingleResult();
    }

    @Transactional(readOnly = true)
    public long getCount(Class<? extends ProductState> stateType, org.dddml.support.criterion.Criterion filter) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<AbstractProductState.SimpleProductState> root = cq.from(AbstractProductState.SimpleProductState.class);
        cq.select(cb.count(root));
        if (filter != null) {
            JpaUtils.criteriaAddFilter(cb, cq, root, filter);
        }
        addNotDeletedRestriction(cb, cq, root);
        return em.createQuery(cq).getSingleResult();
    }

    @Transactional(readOnly = true)
    public GoodIdentificationState getGoodIdentification(String productId, String goodIdentificationTypeId) {
        ProductGoodIdentificationId entityId = new ProductGoodIdentificationId(productId, goodIdentificationTypeId);
        return (GoodIdentificationState) getEntityManager().find(AbstractGoodIdentificationState.SimpleGoodIdentificationState.class, entityId);
    }

    @Transactional(readOnly = true)
    public Iterable<GoodIdentificationState> getGoodIdentifications(String productId, org.dddml.support.criterion.Criterion filter, List<String> orders) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractGoodIdentificationState.SimpleGoodIdentificationState> cq = cb.createQuery(AbstractGoodIdentificationState.SimpleGoodIdentificationState.class);
        Root<AbstractGoodIdentificationState.SimpleGoodIdentificationState> root = cq.from(AbstractGoodIdentificationState.SimpleGoodIdentificationState.class);
        cq.select(root);

        Predicate partIdCondition = cb.and(
            cb.equal(root.get("productGoodIdentificationId").get("productId"), productId)
        );
        cq.where(partIdCondition);

        // Add filter and orders
        JpaUtils.criteriaAddFilterAndOrders(cb, cq, root, filter, orders);

        TypedQuery<AbstractGoodIdentificationState.SimpleGoodIdentificationState> query = em.createQuery(cq);
        return query.getResultList().stream().map(GoodIdentificationState.class::cast).collect(Collectors.toList());
    }

    protected void addNotDeletedRestriction(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<?> root) {
    }

}
