// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.document.hibernate;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.dddml.ffvtraceability.domain.document.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.specialization.hibernate.*;
import org.springframework.transaction.annotation.Transactional;

@Repository("documentStateRepository")
public class HibernateDocumentStateRepository implements DocumentStateRepository {
    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    private static final Set<String> readOnlyPropertyPascalCaseNames = new HashSet<String>(Arrays.asList("DocumentId", "DocumentTypeId", "Comments", "DocumentLocation", "DocumentText", "Version", "CreatedBy", "CreatedAt", "UpdatedBy", "UpdatedAt"));
    
    private ReadOnlyProxyGenerator readOnlyProxyGenerator;
    
    public ReadOnlyProxyGenerator getReadOnlyProxyGenerator() {
        return readOnlyProxyGenerator;
    }

    public void setReadOnlyProxyGenerator(ReadOnlyProxyGenerator readOnlyProxyGenerator) {
        this.readOnlyProxyGenerator = readOnlyProxyGenerator;
    }

    @Transactional(readOnly = true)
    public DocumentState get(String id, boolean nullAllowed) {
        return get(DocumentState.class, id, nullAllowed);
    }

    @Transactional(readOnly = true)
    public DocumentState get(Class<? extends DocumentState> type, String id, boolean nullAllowed) {
        DocumentState.SqlDocumentState state = (DocumentState.SqlDocumentState)getEntityManager().find(AbstractDocumentState.SimpleDocumentState.class, id);
        if (state != null && !type.isAssignableFrom(state.getClass())) {
            throw new ClassCastException(String.format("state is NOT instance of %1$s", type.getName()));
        }
        if (!nullAllowed && state == null) {
            state = (DocumentState.SqlDocumentState)newEmptyState(type);
            state.setDocumentId(id);
        }
        return state;
    }

    private DocumentState newEmptyState(Class<? extends DocumentState> type) {
        DocumentState state = null;
        Class<? extends AbstractDocumentState> clazz = null;
        if (state != null) {
            // do nothing.
        }
        else if (type.equals(DocumentState.class)) {
            clazz = AbstractDocumentState.SimpleDocumentState.class;
        }
        else {
            throw new IllegalArgumentException("type");
        }
        try {
            state = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("type", e);
        }
        return state;
    }

    public void save(DocumentState state) {
        DocumentState s = state;
        if (getReadOnlyProxyGenerator() != null) {
            s = (DocumentState) getReadOnlyProxyGenerator().getTarget(state);
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

    public void merge(DocumentState detached) {
        DocumentState persistent = getEntityManager().find(AbstractDocumentState.SimpleDocumentState.class, detached.getDocumentId());
        if (persistent != null) {
            merge(persistent, detached);
            entityManager.merge(persistent);
        } else {
            entityManager.persist(detached);
        }
        entityManager.flush();
    }

    private void merge(DocumentState persistent, DocumentState detached) {
        ((AbstractDocumentState) persistent).merge(detached);
    }

}

