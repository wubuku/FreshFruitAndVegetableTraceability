// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.roletype.hibernate;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.dddml.ffvtraceability.domain.roletype.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.specialization.hibernate.*;
import org.springframework.transaction.annotation.Transactional;

@Repository("roleTypeStateRepository")
public class HibernateRoleTypeStateRepository implements RoleTypeStateRepository {
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

    private static final Set<String> readOnlyPropertyPascalCaseNames = new HashSet<String>(Arrays.asList("RoleTypeId", "ParentTypeId", "HasTable", "Description", "Version", "CreatedBy", "CreatedAt", "UpdatedBy", "UpdatedAt"));
    
    private ReadOnlyProxyGenerator readOnlyProxyGenerator;
    
    public ReadOnlyProxyGenerator getReadOnlyProxyGenerator() {
        return readOnlyProxyGenerator;
    }

    public void setReadOnlyProxyGenerator(ReadOnlyProxyGenerator readOnlyProxyGenerator) {
        this.readOnlyProxyGenerator = readOnlyProxyGenerator;
    }

    @Transactional(readOnly = true)
    public RoleTypeState get(String id, boolean nullAllowed) {
        RoleTypeState.SqlRoleTypeState state = (RoleTypeState.SqlRoleTypeState)getEntityManager().find(AbstractRoleTypeState.SimpleRoleTypeState.class, id);
        if (!nullAllowed && state == null) {
            state = new AbstractRoleTypeState.SimpleRoleTypeState();
            state.setRoleTypeId(id);
        }
        return state;
    }

    @Transactional
    public void save(RoleTypeState state) {
        RoleTypeState s = state;
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

    public void merge(RoleTypeState detached) {
        RoleTypeState persistent = getEntityManager().find(AbstractRoleTypeState.SimpleRoleTypeState.class, detached.getRoleTypeId());
        if (persistent != null) {
            merge(persistent, detached);
            entityManager.merge(persistent);
        } else {
            entityManager.persist(detached);
        }
        entityManager.flush();
    }

    private void merge(RoleTypeState persistent, RoleTypeState detached) {
        ((AbstractRoleTypeState) persistent).merge(detached);
    }

}

