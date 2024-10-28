package org.dddml.ffvtraceability.specialization.jpa;

import jakarta.persistence.criteria.*;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;

public class JpaUtils {

    private JpaUtils() {
    }

    /**
     * Applies pagination parameters to a TypedQuery.
     * 
     * @param query       The query to apply pagination to
     * @param firstResult The position of the first result to retrieve (0-based)
     * @param maxResults  The maximum number of results to retrieve
     * @return The same query instance with pagination applied
     */
    public static <T> TypedQuery<T> applyPagination(TypedQuery<T> query, Integer firstResult, Integer maxResults) {
        if (firstResult != null) {
            query.setFirstResult(firstResult);
        }
        if (maxResults != null) {
            query.setMaxResults(maxResults);
        }
        return query;
    }

    /**
     * Adds filter predicates and ordering to a criteria query.
     * Will replace any existing WHERE clause in the criteria query.
     * 
     * @param cb     The criteria builder
     * @param cq     The criteria query to modify
     * @param root   The root entity
     * @param filter The filter conditions as key-value pairs
     * @param orders List of property names for ordering, prefix with "-" for
     *               descending order
     * @return The created filter predicate, or null if no filter was applied
     */
    public static <CQ, T> Predicate criteriaAddFilterAndOrders(CriteriaBuilder cb,
            CriteriaQuery<CQ> cq, Root<T> root, Iterable<Map.Entry<String, Object>> filter, List<String> orders) {
        Predicate filterPredicate = null;
        if (filter != null) {
            filterPredicate = criteriaAddFilter(cb, cq, root, filter);
        }
        if (orders != null) {
            criteriaAddOrders(cb, cq, root, orders);
        }
        return filterPredicate;
    }

    /**
     * Adds filter predicates and ordering to a criteria query using a Criterion
     * object.
     * Will replace any existing WHERE clause in the criteria query.
     * 
     * @param cb     The criteria builder
     * @param cq     The criteria query to modify
     * @param root   The root entity
     * @param filter The filter conditions as a Criterion object
     * @param orders List of property names for ordering, prefix with "-" for
     *               descending order
     * @return The created filter predicate, or null if no filter was applied
     */
    public static Predicate criteriaAddFilterAndOrders(CriteriaBuilder cb, CriteriaQuery<?> cq,
            Root<?> root, org.dddml.support.criterion.Criterion filter, List<String> orders) {
        Predicate filterPredicate = null;
        if (filter != null) {
            filterPredicate = JpaCriterionUtils.toJpaPredicate(cb, root, filter);
            cq.where(combinePredicates(cb, cq.getRestriction(), filterPredicate));
        }
        if (orders != null) {
            criteriaAddOrders(cb, cq, root, orders);
        }
        return filterPredicate;
    }

    /**
     * Adds filter predicates to a criteria query based on key-value pairs.
     * Will replace any existing WHERE clause in the criteria query.
     * All conditions are combined with AND.
     * 
     * @param cb     The criteria builder
     * @param cq     The criteria query to modify
     * @param root   The root entity
     * @param filter The filter conditions as key-value pairs
     * @return The created filter predicate
     */
    public static <CQ, T> Predicate criteriaAddFilter(CriteriaBuilder cb, CriteriaQuery<CQ> cq, Root<T> root,
            Iterable<Map.Entry<String, Object>> filter) {
        Predicate[] predicates = StreamSupport.stream(filter.spliterator(), false)
                .map(entry -> criteriaCreatePredicate(cb, root, entry))
                .toArray(Predicate[]::new);
        Predicate filterPredicate = cb.and(predicates);
        cq.where(combinePredicates(cb, cq.getRestriction(), filterPredicate));
        return filterPredicate;
    }

    /**
     * Adds filter predicates to a criteria query using a Criterion object.
     * Will replace any existing WHERE clause in the criteria query.
     * 
     * @param cb     The criteria builder
     * @param cq     The criteria query to modify
     * @param root   The root entity
     * @param filter The filter conditions as a Criterion object
     * @return The created filter predicate
     */
    public static <CQ, T> Predicate criteriaAddFilter(CriteriaBuilder cb, CriteriaQuery<CQ> cq, Root<T> root,
            org.dddml.support.criterion.Criterion filter) {
        Predicate filterPredicate = JpaCriterionUtils.toJpaPredicate(cb, root, filter);
        cq.where(combinePredicates(cb, cq.getRestriction(), filterPredicate));
        return filterPredicate;
    }

    /**
     * Adds ORDER BY clauses to a criteria query.
     * Will replace any existing ORDER BY clause in the criteria query.
     * 
     * @param cb     The criteria builder
     * @param cq     The criteria query to modify
     * @param root   The root entity
     * @param orders List of property names for ordering, prefix with "-" for
     *               descending order
     */
    public static void criteriaAddOrders(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<?> root, List<String> orders) {
        List<Order> orderList = orders.stream()
                .map(order -> {
                    boolean isDesc = order.startsWith("-");
                    String propertyName = isDesc ? order.substring(1) : order;
                    return isDesc ? cb.desc(root.get(propertyName)) : cb.asc(root.get(propertyName));
                })
                .collect(Collectors.toList());
        cq.orderBy(orderList);
    }

    /**
     * Adds a property criterion to an existing disjunction (OR) predicate.
     * 
     * @param cb            The criteria builder
     * @param disjunction   The existing disjunction predicate
     * @param root          The root entity
     * @param propertyName  The name of the property to filter on
     * @param propertyValue The value to compare against (null means IS NULL)
     * @return A new disjunction predicate combining the existing one with the new
     *         condition
     */
    public static Predicate disjunctionAddPropertyCriterion(CriteriaBuilder cb, Predicate disjunction, Root<?> root,
            String propertyName, Object propertyValue) {
        // HINT: renamed from disjunctionAddCriterion
        Predicate predicate = (propertyValue == null)
                ? cb.isNull(root.get(propertyName))
                : cb.equal(root.get(propertyName), propertyValue);
        return cb.or(disjunction, predicate);
    }

    /**
     * Adds a single property criterion to a criteria query.
     * Will combine with any existing WHERE clause using AND.
     * 
     * @param cb            The criteria builder
     * @param cq            The criteria query to modify
     * @param root          The root entity
     * @param propertyName  The name of the property to filter on
     * @param propertyValue The value to compare against (null means IS NULL)
     * @return The created predicate (before combining with existing conditions)
     */
    public static Predicate criteriaAddPropertyCriterion(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<?> root,
            String propertyName, Object propertyValue) {
        // HINT: renamed from criteriaAddCriterion
        Predicate predicate = (propertyValue == null)
                ? cb.isNull(root.get(propertyName))
                : cb.equal(root.get(propertyName), propertyValue);
        Predicate combinedPredicate = combinePredicates(cb, cq.getRestriction(), predicate);
        cq.where(combinedPredicate);
        return predicate;
    }

    private static Predicate criteriaCreatePredicate(CriteriaBuilder cb, Root<?> root,
            Map.Entry<String, Object> filterPair) {
        if (filterPair.getValue() == null) {
            return cb.isNull(root.get(filterPair.getKey()));
        } else {
            return cb.equal(root.get(filterPair.getKey()), filterPair.getValue());
        }
    }

    private static Predicate combinePredicates(CriteriaBuilder cb, Predicate existing, Predicate newPredicate) {
        if (existing == null) {
            return newPredicate;
        }
        return cb.and(existing, newPredicate);
    }
}
