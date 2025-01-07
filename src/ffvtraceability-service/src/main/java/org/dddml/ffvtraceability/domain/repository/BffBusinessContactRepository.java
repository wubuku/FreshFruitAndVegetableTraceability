package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.contactmech.AbstractContactMechState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BffBusinessContactRepository extends JpaRepository<AbstractContactMechState, String> {

    @Query(value = """
            SELECT 
                pa.contact_mech_id as contactMechId,
                pa.to_name as toName,
                pa.address1 as address1,
                pa.city as city,
                pa.postal_code as postalCode,
                pa.state_province_geo_id as stateProvinceGeoId
            FROM contact_mech pa
            WHERE pa.to_name = :businessName
            AND pa.postal_code = :zipCode
            AND pa.state_province_geo_id = :stateProvinceGeoId
            AND pa.city = :city
            AND pa.address1 = :address1
            LIMIT 1
            """, nativeQuery = true)
    Optional<PostalAddressProjection> findOnePostalAddressByBusinessInfo(
            @Param("businessName") String businessName,
            @Param("zipCode") String zipCode,
            @Param("stateProvinceGeoId") String stateProvinceGeoId,
            @Param("city") String city,
            @Param("address1") String address1
    );

    @Query(value = """
            SELECT 
                tn.contact_mech_id as contactMechId,
                tn.country_code as countryCode,
                tn.area_code as areaCode,
                tn.contact_number as contactNumber
            FROM contact_mech tn
            WHERE tn.country_code = :countryCode
            AND tn.area_code = :areaCode
            AND tn.contact_number = :contactNumber
            LIMIT 1
            """, nativeQuery = true)
    Optional<TelecomNumber> findOneTelecomNumberByPhoneInfo(
            @Param("countryCode") String countryCode,
            @Param("areaCode") String areaCode,
            @Param("contactNumber") String contactNumber
    );

    @Query(value = """
            SELECT DISTINCT ON (pcm.party_id)
                pa.contact_mech_id as contactMechId,
                pa.to_name as toName,
                pa.address1 as address1,
                pa.city as city,
                pa.postal_code as postalCode,
                pa.state_province_geo_id as stateProvinceGeoId
            FROM party_contact_mech pcm
            JOIN contact_mech pa ON pa.contact_mech_id = pcm.contact_mech_id
            WHERE pcm.party_id = :partyId
            AND pa.contact_mech_type_id = 'POSTAL_ADDRESS'
            AND pcm.from_date <= CURRENT_TIMESTAMP
            AND (pcm.thru_date IS NULL OR pcm.thru_date > CURRENT_TIMESTAMP)
            ORDER BY pcm.party_id, pcm.from_date DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<PostalAddressProjection> findPartyCurrentPostalAddressByPartyId(
            @Param("partyId") String partyId
    );

    @Query(value = """
            SELECT DISTINCT ON (pcm.party_id)
                tn.contact_mech_id as contactMechId,
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
    Optional<TelecomNumber> findPartyCurrentTelecomNumberByPartyId(
            @Param("partyId") String partyId
    );

    interface PostalAddressProjection {
        String getContactMechId();

        String getToName();

        String getAddress1();

        String getCity();

        String getPostalCode();

        String getStateProvinceGeoId();
    }

    interface TelecomNumber {
        String getContactMechId();

        String getCountryCode();

        String getAreaCode();

        String getContactNumber();

    }
}
