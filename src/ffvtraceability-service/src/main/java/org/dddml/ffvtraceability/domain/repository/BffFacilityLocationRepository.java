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
                fl.location_code as locationCode,
                fl.gln as gln,
                fl.description as description,
                fl.location_name as locationName,
                fl.active as active
            FROM facility_location fl
            WHERE fl.facility_id = :facilityId
                AND (:active IS NULL OR fl.active = :active)
            ORDER BY fl.location_seq_id
            """,
            nativeQuery = true)
    List<BffFacilityLocationProjection> findFacilityLocations(
            @Param("facilityId") String facilityId,
            @Param("active") String active
    );

    @Query(value = """
            SELECT 
                fl.location_seq_id as locationSeqId,
                fl.location_type_enum_id as locationTypeEnumId,
               fl.facility_id AS facilityId,
               f.facility_name AS facilityName,
                fl.area_id as areaId,
                fl.aisle_id as aisleId,
                fl.section_id as sectionId,
                fl.level_id as levelId,
                fl.position_id as positionId,
                fl.geo_point_id as geoPointId,
                fl.location_code as locationCode,
                fl.gln as gln,
                fl.description as description,
                fl.location_name as locationName,
                fl.active as active
            FROM facility_location fl
            left join facility f ON fl.facility_id = f.facility_id
            WHERE (:active IS NULL OR fl.active = :active)
            ORDER BY fl.location_seq_id
            """,
            nativeQuery = true)
    List<BffFacilityLocationProjection> findAllLocations(@Param("active") String active);

    @Query(value = """
            SELECT count(*) from facility_location where location_name = :locationName
            """, nativeQuery = true)
    Integer countyByLocationName(@Param("locationName") String locationName);

    @Query(value = """
            SELECT facility_id as facilityId,location_seq_id as locationSeqId from facility_location where location_name = :locationName
            limit 1
            """, nativeQuery = true)
    BffFacilityLocationIdProjection findFacilityLocationIdByLocationName(@Param("locationName") String locationName);


    @Query(value = """
            SELECT count(*) from facility_location where location_code = :locationCode
            """, nativeQuery = true)
    Integer countyByLocationCode(@Param("locationCode") String locationCode);


    @Query(value = """
            SELECT facility_id as facilityId,location_seq_id as locationSeqId from facility_location where location_code = :locationCode
            limit 1
            """, nativeQuery = true)
    BffFacilityLocationIdProjection findFacilityLocationIdByLocationCode(@Param("locationCode") String locationCode);

    interface BffFacilityLocationIdProjection {
        String getFacilityId();

        String getLocationSeqId();
    }

}