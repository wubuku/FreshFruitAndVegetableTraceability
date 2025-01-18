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
                f.active as active,
                ffrn.id_value as ffrn,
                gln.id_value as gln,
                ii.id_value as internalId
            FROM facility f
            LEFT JOIN (
                SELECT 
                    fi.facility_id,
                    fi.id_value
                FROM facility_identification fi
                WHERE fi.facility_identification_type_id = 'FFRN'
            ) ffrn ON ffrn.facility_id = f.facility_id
            LEFT JOIN (
                SELECT 
                    fi.facility_id,
                    fi.id_value
                FROM facility_identification fi
                WHERE fi.facility_identification_type_id = 'GLN'
            ) gln ON gln.facility_id = f.facility_id
            
            LEFT JOIN (
                SELECT 
                    fi.facility_id,
                    fi.id_value
                FROM facility_identification fi
                WHERE fi.facility_identification_type_id = 'INTERNAL_ID'
            ) ii ON ii.facility_id = f.facility_id
            
            WHERE (:active IS NULL OR f.active = :active)
            AND (:ownerPartyId IS NULL OR f.owner_party_id = :ownerPartyId)
            ORDER BY f.created_at DESC
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM facility f
                    WHERE (:active IS NULL OR f.active = :active)
                    AND (:ownerPartyId IS NULL OR f.owner_party_id = :ownerPartyId)
                    """,
            nativeQuery = true)
    Page<BffFacilityProjection> findAllFacilities(Pageable pageable,
                                                  @Param("active") String active,
                                                  @Param("ownerPartyId") String ownerPartyId
    );

}