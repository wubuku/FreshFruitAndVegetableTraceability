// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.article.hibernate;

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
import org.dddml.ffvtraceability.domain.article.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.specialization.jpa.*;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class HibernateArticleStateQueryRepository implements ArticleStateQueryRepository {
    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    private static final Set<String> readOnlyPropertyPascalCaseNames = new HashSet<String>(Arrays.asList("ArticleId", "Title", "Body", "Author", "Comments", "Tags", "Version", "CreatedBy", "CreatedAt", "UpdatedBy", "UpdatedAt", "Active", "Deleted"));

    private ReadOnlyProxyGenerator readOnlyProxyGenerator;

    public ReadOnlyProxyGenerator getReadOnlyProxyGenerator() {
        return readOnlyProxyGenerator;
    }

    public void setReadOnlyProxyGenerator(ReadOnlyProxyGenerator readOnlyProxyGenerator) {
        this.readOnlyProxyGenerator = readOnlyProxyGenerator;
    }

    @Transactional(readOnly = true)
    public ArticleState get(Long id) {
        ArticleState state = (ArticleState)getEntityManager().find(AbstractArticleState.SimpleArticleState.class, id);
        return state;
    }

    @Transactional(readOnly = true)
    public Iterable<ArticleState> getAll(Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractArticleState.SimpleArticleState> cq = cb.createQuery(AbstractArticleState.SimpleArticleState.class);
        Root<AbstractArticleState.SimpleArticleState> root = cq.from(AbstractArticleState.SimpleArticleState.class);
        cq.select(root);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractArticleState.SimpleArticleState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(ArticleState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Iterable<ArticleState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractArticleState.SimpleArticleState> cq = cb.createQuery(AbstractArticleState.SimpleArticleState.class);
        Root<AbstractArticleState.SimpleArticleState> root = cq.from(AbstractArticleState.SimpleArticleState.class);
        cq.select(root);
        JpaUtils.criteriaAddFilterAndOrders(cb, cq, root, filter, orders);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractArticleState.SimpleArticleState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(ArticleState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Iterable<ArticleState> get(org.dddml.support.criterion.Criterion filter, List<String> orders, Integer firstResult, Integer maxResults) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractArticleState.SimpleArticleState> cq = cb.createQuery(AbstractArticleState.SimpleArticleState.class);
        Root<AbstractArticleState.SimpleArticleState> root = cq.from(AbstractArticleState.SimpleArticleState.class);
        cq.select(root);
        JpaUtils.criteriaAddFilterAndOrders(cb, cq, root, filter, orders);
        addNotDeletedRestriction(cb, cq, root);
        TypedQuery<AbstractArticleState.SimpleArticleState> query = em.createQuery(cq);
        JpaUtils.applyPagination(query, firstResult, maxResults);
        return query.getResultList().stream().map(ArticleState.class::cast).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ArticleState getFirst(Iterable<Map.Entry<String, Object>> filter, List<String> orders) {
        List<ArticleState> list = (List<ArticleState>)get(filter, orders, 0, 1);
        if (list == null || list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    @Transactional(readOnly = true)
    public ArticleState getFirst(Map.Entry<String, Object> keyValue, List<String> orders) {
        List<Map.Entry<String, Object>> filter = new ArrayList<>();
        filter.add(keyValue);
        return getFirst(filter, orders);
    }

    @Transactional(readOnly = true)
    public Iterable<ArticleState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults) {
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
        Root<AbstractArticleState.SimpleArticleState> root = cq.from(AbstractArticleState.SimpleArticleState.class);
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
        Root<AbstractArticleState.SimpleArticleState> root = cq.from(AbstractArticleState.SimpleArticleState.class);
        cq.select(cb.count(root));
        if (filter != null) {
            JpaUtils.criteriaAddFilter(cb, cq, root, filter);
        }
        addNotDeletedRestriction(cb, cq, root);
        return em.createQuery(cq).getSingleResult();
    }

    @Transactional(readOnly = true)
    public CommentState getComment(Long articleId, Long commentSeqId) {
        ArticleCommentId entityId = new ArticleCommentId(articleId, commentSeqId);
        return (CommentState) getEntityManager().find(AbstractCommentState.SimpleCommentState.class, entityId);
    }

    @Transactional(readOnly = true)
    public Iterable<CommentState> getComments(Long articleId, org.dddml.support.criterion.Criterion filter, List<String> orders) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AbstractCommentState.SimpleCommentState> cq = cb.createQuery(AbstractCommentState.SimpleCommentState.class);
        Root<AbstractCommentState.SimpleCommentState> root = cq.from(AbstractCommentState.SimpleCommentState.class);
        cq.select(root);

        Predicate partIdCondition = cb.and(
            cb.equal(root.get("articleCommentId").get("articleId"), articleId)
        );
        cq.where(partIdCondition);

        // Add filter and orders
        JpaUtils.criteriaAddFilterAndOrders(cb, cq, root, filter, orders);

        TypedQuery<AbstractCommentState.SimpleCommentState> query = em.createQuery(cq);
        return query.getResultList().stream().map(CommentState.class::cast).collect(Collectors.toList());
    }


    protected void addNotDeletedRestriction(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<AbstractArticleState.SimpleArticleState> root) {
        Predicate isNull = cb.isNull(root.get("deleted"));
        Predicate isFalse = cb.equal(root.get("deleted"), false);
        Predicate notDeleted = cb.or(isNull, isFalse);
        cq.where(cq.getRestriction() == null ? notDeleted : cb.and(cq.getRestriction(), notDeleted));
    }

}
