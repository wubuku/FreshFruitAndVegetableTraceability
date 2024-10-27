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

    public static <CQ, T> Predicate criteriaAddFilter(CriteriaBuilder cb, CriteriaQuery<CQ> cq, Root<T> root,
            Iterable<Map.Entry<String, Object>> filter) {
        Predicate[] predicates = StreamSupport.stream(filter.spliterator(), false)
                .map(entry -> criteriaCreatePredicate(cb, root, entry))
                .toArray(Predicate[]::new);
        Predicate filterPredicate = cb.and(predicates);
        cq.where(combinePredicates(cb, cq.getRestriction(), filterPredicate));
        return filterPredicate;
    }

    public static <CQ, T> Predicate criteriaAddFilter(CriteriaBuilder cb, CriteriaQuery<CQ> cq, Root<T> root,
            org.dddml.support.criterion.Criterion filter) {
        Predicate filterPredicate = JpaCriterionUtils.toJpaPredicate(cb, root, filter);
        cq.where(combinePredicates(cb, cq.getRestriction(), filterPredicate));
        return filterPredicate;
    }

    public static Predicate criteriaCreatePredicate(CriteriaBuilder cb, Root<?> root,
            Map.Entry<String, Object> filterPair) {
        if (filterPair.getValue() == null) {
            return cb.isNull(root.get(filterPair.getKey()));
        } else {
            return cb.equal(root.get(filterPair.getKey()), filterPair.getValue());
        }
    }

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

    public static void disjunctionAddCriterion(CriteriaBuilder cb, Predicate disjunction, Root<?> root,
            String propertyName, Object propertyValue) {
        Predicate predicate = (propertyValue == null)
                ? cb.isNull(root.get(propertyName))
                : cb.equal(root.get(propertyName), propertyValue);
        cb.or(disjunction, predicate);
    }

    public static void criteriaAddCriterion(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<?> root, String propertyName,
            Object propertyValue) {
        Predicate predicate = (propertyValue == null)
                ? cb.isNull(root.get(propertyName))
                : cb.equal(root.get(propertyName), propertyValue);
        cq.where(predicate);
    }

    public static <T> TypedQuery<T> applyPagination(TypedQuery<T> query, Integer firstResult, Integer maxResults) {
        if (firstResult != null) {
            query.setFirstResult(firstResult);
        }
        if (maxResults != null) {
            query.setMaxResults(maxResults);
        }
        return query;
    }

    private static Predicate combinePredicates(CriteriaBuilder cb, Predicate existing, Predicate newPredicate) {
        if (existing == null) {
            return newPredicate;
        }
        return cb.and(existing, newPredicate);
    }
}
