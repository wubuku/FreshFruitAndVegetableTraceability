// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.geopoint.hibernate;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.dddml.ffvtraceability.domain.geopoint.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.specialization.hibernate.*;
import org.springframework.transaction.annotation.Transactional;

@Repository("geoPointStateRepository")
public class HibernateGeoPointStateRepository implements GeoPointStateRepository {
    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    private static final Set<String> readOnlyPropertyPascalCaseNames = new HashSet<String>(Arrays.asList("GeoPointId", "GeoPointTypeEnumId", "Description", "Latitude", "Longitude", "Elevation", "ElevationUomId", "Information", "Version", "CreatedBy", "CreatedAt", "UpdatedBy", "UpdatedAt", "Active", "Deleted"));
    
    private ReadOnlyProxyGenerator readOnlyProxyGenerator;
    
    public ReadOnlyProxyGenerator getReadOnlyProxyGenerator() {
        return readOnlyProxyGenerator;
    }

    public void setReadOnlyProxyGenerator(ReadOnlyProxyGenerator readOnlyProxyGenerator) {
        this.readOnlyProxyGenerator = readOnlyProxyGenerator;
    }

    @Transactional(readOnly = true)
    public GeoPointState get(String id, boolean nullAllowed) {
        GeoPointState.SqlGeoPointState state = (GeoPointState.SqlGeoPointState)getEntityManager().find(AbstractGeoPointState.SimpleGeoPointState.class, id);
        if (!nullAllowed && state == null) {
            state = new AbstractGeoPointState.SimpleGeoPointState();
            state.setGeoPointId(id);
        }
        return state;
    }

    @Transactional
    public void save(GeoPointState state) {
        GeoPointState s = state;
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

    public void merge(GeoPointState detached) {
        GeoPointState persistent = getEntityManager().find(AbstractGeoPointState.SimpleGeoPointState.class, detached.getGeoPointId());
        if (persistent != null) {
            merge(persistent, detached);
            entityManager.merge(persistent);
        } else {
            entityManager.persist(detached);
        }
        entityManager.flush();
    }

    private void merge(GeoPointState persistent, GeoPointState detached) {
        ((AbstractGeoPointState) persistent).merge(detached);
    }

}
