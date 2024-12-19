package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.facility.AbstractFacilityState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BffFacilityRepository extends JpaRepository<AbstractFacilityState.SimpleFacilityState, String> {
    @Query(value = """
            SELECT 
                f.facility_id as facilityId,
                f.facility_type_id as facilityTypeId,
                f.parent_facility_id as parentFacilityId,
                f.owner_party_id as ownerPartyId,
                f.facility_name as facilityName,
                f.description as description,
                f.facility_size as facilitySize,
                f.facility_size_uom_id as facilitySizeUomId,
                f.geo_point_id as geoPointId,
                f.geo_id as geoId,
                f.active as active
            FROM facility f
            WHERE (:active IS NULL OR f.active = :active)
                AND (f.deleted IS NULL OR f.deleted = false)
            ORDER BY f.created_at DESC
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM facility f
                    WHERE (:active IS NULL OR f.active = :active)
                        AND (f.deleted IS NULL OR f.deleted = false)
                    """,
            nativeQuery = true)
    Page<BffFacilityProjection> findAllFacilities(Pageable pageable, @Param("active") String active);
}