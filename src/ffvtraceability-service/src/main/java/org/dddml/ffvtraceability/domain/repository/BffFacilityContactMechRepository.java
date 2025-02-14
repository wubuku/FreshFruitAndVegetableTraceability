package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.contactmech.AbstractContactMechState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface BffFacilityContactMechRepository extends JpaRepository<AbstractContactMechState, String> {

    @Query(value = """
            SELECT DISTINCT ON (pcm.facility_id)
                pcm.facility_id as facilityId,
                pcm.contact_mech_id as contactMechId,
                pcm.from_date as fromDate,
                pa.to_name as toName,
                pa.address1 as address1,
                pa.city as city,
                pa.postal_code as postalCode,
                pa.state_province_geo_id as stateProvinceGeoId,
                pa.country_geo_id as countryGeoId,
                gc.geo_name as countryGeoName,
                gp.geo_name as stateProvinceGeoName
            FROM facility_contact_mech pcm
            JOIN contact_mech pa ON pa.contact_mech_id = pcm.contact_mech_id
            left join geo gc on pa.country_geo_id = gc.geo_id
            left join geo gp on pa.state_province_geo_id = gp.geo_id
            WHERE pcm.facility_id = :facilityId
            AND pa.contact_mech_type_id = 'POSTAL_ADDRESS'
            AND pcm.from_date <= CURRENT_TIMESTAMP
            AND (pcm.thru_date IS NULL OR pcm.thru_date > CURRENT_TIMESTAMP)
            ORDER BY pcm.facility_id, pcm.from_date DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<FacilityPostalAddressProjection> findFacilityCurrentPostalAddressByFacilityId(
            @Param("facilityId") String facilityId
    );

    @Query(value = """
            SELECT DISTINCT ON (pcm.facility_id)
                pcm.facility_id as facilityId,
                pcm.contact_mech_id as contactMechId,
                pcm.from_date as fromDate,
                pa.to_name as businessName,
                pa.address1 as physicalLocationAddress,
                pa.city as city,
                pa.postal_code as zipCode,
                pa.state_province_geo_id as stateProvinceGeoId,
                pa.country_geo_id as countryGeoId,
                pa.telecom_contact_number as contactNumber,
                gc.geo_name as country,
                gp.geo_name as state,
                pa.email as email,
                pa.ask_for_role as askForRole
            FROM facility_contact_mech pcm
            JOIN contact_mech pa ON pa.contact_mech_id = pcm.contact_mech_id
            left join geo gc on pa.country_geo_id = gc.geo_id
            left join geo gp on pa.state_province_geo_id = gp.geo_id
            WHERE pcm.facility_id = :facilityId
            AND pa.contact_mech_type_id = 'MISC_CONTACT_MECH'
            AND pcm.from_date <= CURRENT_TIMESTAMP
            AND (pcm.thru_date IS NULL OR pcm.thru_date > CURRENT_TIMESTAMP)
            ORDER BY pcm.facility_id, pcm.from_date DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<BffBusinessContactProjection> findFacilityContactByFacilityId(@Param("facilityId") String facilityId);

    @Query(value = """
            SELECT DISTINCT ON (pcm.facility_id)
                pcm.facility_id as facilityId,
                pcm.contact_mech_id as contactMechId,
                pcm.from_date as fromDate,
                tn.country_code as countryCode,
                tn.area_code as areaCode,
                tn.contact_number as contactNumber
            FROM facility_contact_mech pcm
            JOIN contact_mech tn ON tn.contact_mech_id = pcm.contact_mech_id
            WHERE pcm.facility_id = :facilityId
            AND tn.contact_mech_type_id = 'TELECOM_NUMBER'
            AND pcm.from_date <= CURRENT_TIMESTAMP
            AND (pcm.thru_date IS NULL OR pcm.thru_date > CURRENT_TIMESTAMP)
            ORDER BY pcm.facility_id, pcm.from_date DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<FacilityTelecomNumberProjection> findFacilityCurrentTelecomNumberByFacilityId(
            @Param("facilityId") String facilityId
    );


    @Query(value = """
            SELECT DISTINCT ON (pcm.facility_id)
                pcm.facility_id as facilityId,
                pcm.contact_mech_id as contactMechId,
                pcm.from_date as fromDate,
                tn.email as email,
                tn.ask_for_role as askForRole,
                tn.contact_number as contactNumber
            FROM facility_contact_mech pcm
            JOIN contact_mech tn ON tn.contact_mech_id = pcm.contact_mech_id
            WHERE pcm.facility_id = :facilityId
            AND tn.contact_mech_type_id = 'MISC_CONTACT_MECH'
            AND pcm.from_date <= CURRENT_TIMESTAMP
            AND (pcm.thru_date IS NULL OR pcm.thru_date > CURRENT_TIMESTAMP)
            ORDER BY pcm.facility_id, pcm.from_date DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<FacilityMiscContactMechProjection> findFacilityCurrentMisContactMechByFacilityId(
            @Param("facilityId") String facilityId
    );


    @Query(value = """
            SELECT DISTINCT ON (pcm.facility_id)
                pcm.facility_id as facilityId,
                pcm.contact_mech_id as contactMechId,
                pcm.from_date as fromDate
            FROM facility_contact_mech pcm
            JOIN contact_mech tn ON tn.contact_mech_id = pcm.contact_mech_id
            WHERE pcm.facility_id = :facilityId
            AND tn.contact_mech_type_id = :contactMechTypeId
            ORDER BY pcm.facility_id, pcm.from_date DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<FacilityContactMechIdProjection> findFacilityCurrentContactMechByContactMechType(
            @Param("facilityId") String facilityId,
            @Param("contactMechTypeId") String contactMechTypeId
    );


    interface FacilityContactMechIdProjection {
        String getFacilityId();

        String getContactMechId();

        Instant getFromDate();
    }


    interface FacilityMiscContactMechProjection extends FacilityContactMechIdProjection {
        String getEmail();

        String getAskForRole();
    }

    interface FacilityPostalAddressProjection extends BffBusinessContactRepository.PostalAddressProjection {
        String getFacilityId();

        Instant getFromDate();
    }

    interface FacilityTelecomNumberProjection extends BffBusinessContactRepository.TelecomNumberProjection {
        String getFacilityId();

        Instant getFromDate();
    }

}
