package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.contactmech.AbstractContactMechState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface BffPartyContactMechRepository extends JpaRepository<AbstractContactMechState, String> {

    @Query(value = """
            SELECT DISTINCT ON (pcm.party_id)
                pcm.party_id as partyId,
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
            FROM party_contact_mech pcm
            JOIN contact_mech pa ON pa.contact_mech_id = pcm.contact_mech_id
            left join geo gc on pa.country_geo_id = gc.geo_id
            left join geo gp on pa.state_province_geo_id = gp.geo_id
            WHERE pcm.party_id = :partyId
            AND pa.contact_mech_type_id = 'POSTAL_ADDRESS'
            AND pcm.from_date <= CURRENT_TIMESTAMP
            AND (pcm.thru_date IS NULL OR pcm.thru_date > CURRENT_TIMESTAMP)
            ORDER BY pcm.party_id, pcm.from_date DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<PartyPostalAddressProjection> findPartyCurrentPostalAddressByPartyId(
            @Param("partyId") String partyId
    );

    @Query(value = """
            SELECT DISTINCT ON (pcm.party_id)
                pcm.party_id as partyId,
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
            FROM party_contact_mech pcm
            JOIN contact_mech pa ON pa.contact_mech_id = pcm.contact_mech_id
            left join geo gc on pa.country_geo_id = gc.geo_id
            left join geo gp on pa.state_province_geo_id = gp.geo_id
            WHERE pcm.party_id = :partyId
            AND pa.contact_mech_type_id = 'MISC_CONTACT_MECH'
            AND pcm.from_date <= CURRENT_TIMESTAMP
            AND (pcm.thru_date IS NULL OR pcm.thru_date > CURRENT_TIMESTAMP)
            ORDER BY pcm.party_id, pcm.from_date DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<BffPartyBusinessContactProjection> findPartyContactByPartyId(@Param("partyId") String partyId);

    @Query(value = """
            SELECT DISTINCT ON (pcm.party_id)
                pcm.party_id as partyId,
                pcm.contact_mech_id as contactMechId,
                pcm.from_date as fromDate,
                tn.country_code as countryCode,
                tn.area_code as areaCode,
                tn.contact_number as contactNumber
            FROM party_contact_mech pcm
            JOIN contact_mech tn ON tn.contact_mech_id = pcm.contact_mech_id
            WHERE pcm.party_id = :partyId
            AND tn.contact_mech_type_id = 'TELECOM_NUMBER'
            AND pcm.from_date <= CURRENT_TIMESTAMP
            AND (pcm.thru_date IS NULL OR pcm.thru_date > CURRENT_TIMESTAMP)
            ORDER BY pcm.party_id, pcm.from_date DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<PartyTelecomNumberProjection> findPartyCurrentTelecomNumberByPartyId(
            @Param("partyId") String partyId
    );

    @Query(value = """
            SELECT DISTINCT ON (pcm.party_id)
                pcm.party_id as partyId,
                pcm.contact_mech_id as contactMechId,
                pcm.from_date as fromDate,
                tn.email as email,
                tn.ask_for_role as askForRole,
                tn.contact_number as contactNumber
            FROM party_contact_mech pcm
            JOIN contact_mech tn ON tn.contact_mech_id = pcm.contact_mech_id
            WHERE pcm.party_id = :partyId
            AND tn.contact_mech_type_id = 'MISC_CONTACT_MECH'
            AND pcm.from_date <= CURRENT_TIMESTAMP
            AND (pcm.thru_date IS NULL OR pcm.thru_date > CURRENT_TIMESTAMP)
            ORDER BY pcm.party_id, pcm.from_date DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<PartyMiscContactMechProjection> findPartyCurrentMisContactMechByPartyId(
            @Param("partyId") String partyId
    );


    @Query(value = """
            SELECT DISTINCT ON (pcm.party_id)
                pcm.party_id as partyId,
                pcm.contact_mech_id as contactMechId,
                pcm.from_date as fromDate
            FROM party_contact_mech pcm
            JOIN contact_mech tn ON tn.contact_mech_id = pcm.contact_mech_id
            WHERE pcm.party_id = :partyId
            AND tn.contact_mech_type_id = :contactMechTypeId
            ORDER BY pcm.party_id, pcm.from_date DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<PartyContactMechIdProjection> findPartyCurrentContactMechByContactMechType(
            @Param("partyId") String partyId,
            @Param("contactMechTypeId") String contactMechTypeId
    );


    interface PartyContactMechIdProjection {
        String getPartyId();

        String getContactMechId();

        Instant getFromDate();
    }

    interface BffPartyBusinessContactProjection extends BffBusinessContactProjection, PartyContactMechIdProjection {
    }

    interface PartyMiscContactMechProjection extends PartyContactMechIdProjection {
        String getEmail();

        String getAskForRole();
    }

    interface PartyPostalAddressProjection extends BffBusinessContactRepository.PostalAddressProjection {
        String getPartyId();

        Instant getFromDate();
    }

    interface PartyTelecomNumberProjection extends BffBusinessContactRepository.TelecomNumberProjection {
        String getPartyId();

        Instant getFromDate();
    }

}
