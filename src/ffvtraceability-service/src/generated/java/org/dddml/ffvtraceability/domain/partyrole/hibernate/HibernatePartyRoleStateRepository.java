// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.partyrole.hibernate;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.dddml.ffvtraceability.domain.partyrole.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.specialization.hibernate.*;
import org.springframework.transaction.annotation.Transactional;

@Repository("partyRoleStateRepository")
public class HibernatePartyRoleStateRepository implements PartyRoleStateRepository {
    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    private static final Set<String> readOnlyPropertyPascalCaseNames = new HashSet<String>(Arrays.asList("PartyRoleId", "Version", "CreatedBy", "CreatedAt", "UpdatedBy", "UpdatedAt", "Active", "Deleted"));
    
    private ReadOnlyProxyGenerator readOnlyProxyGenerator;
    
    public ReadOnlyProxyGenerator getReadOnlyProxyGenerator() {
        return readOnlyProxyGenerator;
    }

    public void setReadOnlyProxyGenerator(ReadOnlyProxyGenerator readOnlyProxyGenerator) {
        this.readOnlyProxyGenerator = readOnlyProxyGenerator;
    }

    @Transactional(readOnly = true)
    public PartyRoleState get(PartyRoleId id, boolean nullAllowed) {
        PartyRoleState.SqlPartyRoleState state = (PartyRoleState.SqlPartyRoleState)getEntityManager().find(AbstractPartyRoleState.SimplePartyRoleState.class, id);
        if (!nullAllowed && state == null) {
            state = new AbstractPartyRoleState.SimplePartyRoleState();
            state.setPartyRoleId(id);
        }
        if (getReadOnlyProxyGenerator() != null && state != null) {
            return (PartyRoleState) getReadOnlyProxyGenerator().createProxy(state, new Class[]{PartyRoleState.SqlPartyRoleState.class}, "getStateReadOnly", readOnlyPropertyPascalCaseNames);
        }
        return state;
    }

    public void save(PartyRoleState state) {
        PartyRoleState s = state;
        if (getReadOnlyProxyGenerator() != null) {
            s = (PartyRoleState) getReadOnlyProxyGenerator().getTarget(state);
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

    public void merge(PartyRoleState detached) {
        PartyRoleState persistent = getEntityManager().find(AbstractPartyRoleState.SimplePartyRoleState.class, detached.getPartyRoleId());
        if (persistent != null) {
            merge(persistent, detached);
            entityManager.merge(persistent);
        } else {
            entityManager.persist(detached);
        }
        entityManager.flush();
    }

    private void merge(PartyRoleState persistent, PartyRoleState detached) {
        ((AbstractPartyRoleState) persistent).merge(detached);
    }

}
