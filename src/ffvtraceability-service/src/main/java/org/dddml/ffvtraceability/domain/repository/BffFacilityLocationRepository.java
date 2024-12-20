package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.facilitylocation.AbstractFacilityLocationState;
import org.dddml.ffvtraceability.domain.facilitylocation.FacilityLocationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BffFacilityLocationRepository extends JpaRepository<
        AbstractFacilityLocationState.SimpleFacilityLocationState, FacilityLocationId> {

    @Query(value = """
            SELECT 
                fl.location_seq_id as locationSeqId,
                fl.location_type_enum_id as locationTypeEnumId,
                fl.area_id as areaId,
                fl.aisle_id as aisleId,
                fl.section_id as sectionId,
                fl.level_id as levelId,
                fl.position_id as positionId,
                fl.geo_point_id as geoPointId,
                fl.active as active
            FROM facility_location fl
            WHERE fl.facility_id = :facilityId
                AND (:active IS NULL OR fl.active = :active)
                AND (fl.deleted IS NULL OR fl.deleted = false)
            ORDER BY fl.location_seq_id
            """,
            nativeQuery = true)
    List<BffFacilityLocationProjection> findFacilityLocations(
            @Param("facilityId") String facilityId,
            @Param("active") String active
    );

}