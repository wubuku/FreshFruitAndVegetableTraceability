// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.article.hibernate;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.dddml.ffvtraceability.domain.article.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.specialization.hibernate.*;
import org.springframework.transaction.annotation.Transactional;

@Repository("articleStateRepository")
public class HibernateArticleStateRepository implements ArticleStateRepository {
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

    private static final Set<String> readOnlyPropertyPascalCaseNames = new HashSet<String>(Arrays.asList("ArticleId", "Title", "Body", "Author", "Comments", "Tags", "Version", "CreatedBy", "CreatedAt", "UpdatedBy", "UpdatedAt"));
    
    private ReadOnlyProxyGenerator readOnlyProxyGenerator;
    
    public ReadOnlyProxyGenerator getReadOnlyProxyGenerator() {
        return readOnlyProxyGenerator;
    }

    public void setReadOnlyProxyGenerator(ReadOnlyProxyGenerator readOnlyProxyGenerator) {
        this.readOnlyProxyGenerator = readOnlyProxyGenerator;
    }

    @Transactional(readOnly = true)
    public ArticleState get(Long id, boolean nullAllowed) {
        ArticleState.SqlArticleState state = (ArticleState.SqlArticleState)getEntityManager().find(AbstractArticleState.SimpleArticleState.class, id);
        if (!nullAllowed && state == null) {
            state = new AbstractArticleState.SimpleArticleState();
            state.setArticleId(id);
        }
        if (getReadOnlyProxyGenerator() != null && state != null) {
            return (ArticleState) getReadOnlyProxyGenerator().createProxy(state, new Class[]{ArticleState.SqlArticleState.class, Saveable.class}, "getStateReadOnly", readOnlyPropertyPascalCaseNames);
        }
        return state;
    }

    @Transactional
    public void save(ArticleState state) {
        ArticleState s = state;
        if (getReadOnlyProxyGenerator() != null) {
            s = (ArticleState) getReadOnlyProxyGenerator().getTarget(state);
        }
        if (s.getVersion() == null) {
            entityManager.persist(s);
        } else {
            entityManager.merge(s);
        }

        if (s instanceof Saveable) {
            Saveable saveable = (Saveable) s;
            saveable.save();
        }
        entityManager.flush();
    }

    public void merge(ArticleState detached) {
        ArticleState persistent = getEntityManager().find(AbstractArticleState.SimpleArticleState.class, detached.getArticleId());
        if (persistent != null) {
            merge(persistent, detached);
            entityManager.merge(persistent);
        } else {
            entityManager.persist(detached);
        }
        entityManager.flush();
    }

    private void merge(ArticleState persistent, ArticleState detached) {
        ((AbstractArticleState) persistent).merge(detached);
    }

}

