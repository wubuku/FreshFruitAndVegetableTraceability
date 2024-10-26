package org.dddml.ffvtraceability.specialization.jpa;

import jakarta.persistence.criteria.*;
import org.dddml.support.criterion.*;

import java.util.ArrayList;
import java.util.List;

public class JpaCriterionUtils {
    private JpaCriterionUtils() {
    }

    public static <T> Predicate toJpaPredicate(CriteriaBuilder cb, Root<T> root,
            org.dddml.support.criterion.Criterion criterion) {
        if (criterion instanceof SimpleExpression) {
            SimpleExpression e = (SimpleExpression) criterion;
            String op = e.getOp().trim().toUpperCase();
            Path<Object> path = root.get(e.getPropertyName());
            Object value = e.getValue();

            switch (op) {
                case Restrictions.OP_EQ: return cb.equal(path, value);
                case Restrictions.OP_GT: return cb.greaterThan(path.as(Comparable.class), (Comparable) value);
                case Restrictions.OP_LT: return cb.lessThan(path.as(Comparable.class), (Comparable) value);
                case Restrictions.OP_GE: return cb.greaterThanOrEqualTo(path.as(Comparable.class), (Comparable) value);
                case Restrictions.OP_LE: return cb.lessThanOrEqualTo(path.as(Comparable.class), (Comparable) value);
                case Restrictions.OP_LIKE: return cb.like(path.as(String.class), value.toString());
                default: throw new UnsupportedOperationException("Unsupported operation: " + op);
            }
        } else if (criterion instanceof InsensitiveLikeExpression) {
            InsensitiveLikeExpression e = (InsensitiveLikeExpression) criterion;
            return cb.like(cb.lower(root.get(e.getPropertyName())), ((String) e.getValue()).toLowerCase());
        } else if (criterion instanceof InExpression) {
            InExpression e = (InExpression) criterion;
            return root.get(e.getPropertyName()).in(e.getValues());
        } else if (criterion instanceof NullExpression) {
            NullExpression e = (NullExpression) criterion;
            return cb.isNull(root.get(e.getPropertyName()));
        } else if (criterion instanceof NotNullExpression) {
            NotNullExpression e = (NotNullExpression) criterion;
            return cb.isNotNull(root.get(e.getPropertyName()));
        } else if (criterion instanceof BetweenExpression) {
            BetweenExpression e = (BetweenExpression) criterion;
            return cb.between(root.get(e.getPropertyName()), (Comparable) e.getLo(), (Comparable) e.getHi());
        } else if (criterion instanceof AndExpression) {
            AndExpression e = (AndExpression) criterion;
            return cb.and(toJpaPredicate(cb, root, e.getLeftHandSide()),
                    toJpaPredicate(cb, root, e.getRightHandSide()));
        } else if (criterion instanceof OrExpression) {
            OrExpression e = (OrExpression) criterion;
            return cb.or(toJpaPredicate(cb, root, e.getLeftHandSide()), toJpaPredicate(cb, root, e.getRightHandSide()));
        } else if (criterion instanceof NotExpression) {
            NotExpression e = (NotExpression) criterion;
            return cb.not(toJpaPredicate(cb, root, e.getCriterion()));
        } else if (criterion instanceof Disjunction) {
            Disjunction e = (Disjunction) criterion;
            List<Predicate> predicates = new ArrayList<>();
            for (org.dddml.support.criterion.Criterion c : e.getCriteria()) {
                predicates.add(toJpaPredicate(cb, root, c));
            }
            return cb.or(predicates.toArray(new Predicate[0]));
        } else if (criterion instanceof Conjunction) {
            Conjunction e = (Conjunction) criterion;
            List<Predicate> predicates = new ArrayList<>();
            for (org.dddml.support.criterion.Criterion c : e.getCriteria()) {
                predicates.add(toJpaPredicate(cb, root, c));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        } else if (criterion instanceof EqPropertyExpression) {
            EqPropertyExpression e = (EqPropertyExpression) criterion;
            return cb.equal(root.get(e.getLhsPropertyName()), root.get(e.getRhsPropertyName()));
        } else if (criterion instanceof GtPropertyExpression) {
            GtPropertyExpression e = (GtPropertyExpression) criterion;
            return cb.greaterThan(root.get(e.getLhsPropertyName()), root.get(e.getRhsPropertyName()));
        } else if (criterion instanceof LtPropertyExpression) {
            LtPropertyExpression e = (LtPropertyExpression) criterion;
            return cb.lessThan(root.get(e.getLhsPropertyName()), root.get(e.getRhsPropertyName()));
        } else if (criterion instanceof GePropertyExpression) {
            GePropertyExpression e = (GePropertyExpression) criterion;
            return cb.greaterThanOrEqualTo(root.get(e.getLhsPropertyName()), root.get(e.getRhsPropertyName()));
        } else if (criterion instanceof LePropertyExpression) {
            LePropertyExpression e = (LePropertyExpression) criterion;
            return cb.lessThanOrEqualTo(root.get(e.getLhsPropertyName()), root.get(e.getRhsPropertyName()));
        }

        throw new UnsupportedOperationException(String.format("Not supported criterion. type name: %1$s, %2$s",
                criterion.getClass().getName(), criterion));
    }
}
