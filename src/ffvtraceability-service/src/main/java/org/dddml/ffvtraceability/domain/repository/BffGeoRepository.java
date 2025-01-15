package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.geo.AbstractGeoState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BffGeoRepository extends JpaRepository<AbstractGeoState.SimpleGeoState, String> {

    String NORTH_AMERICAN_STATE_OR_PROVINCE_BASE_QUERY = """
            SELECT 
                ga.geo_id as parentGeoId,
                ga.geo_id_to as geoId,
                tg.abbreviation as abbreviation,
                tg.geo_name as geoName,
                tg.geo_type_id as geoTypeId
            FROM geo_assoc ga
            LEFT JOIN geo g ON ga.geo_id = g.geo_id
            LEFT JOIN geo tg ON ga.geo_id_to = tg.geo_id
            WHERE g.geo_type_id = 'COUNTRY' 
            AND (g.geo_id = 'USA' OR g.geo_id = 'CAN')
            AND (tg.geo_type_id = 'STATE' OR tg.geo_type_id = 'PROVINCE')
            """;
    String STATE_OR_PROVINCE_BASE_QUERY = """
            SELECT 
                ga.geo_id as parentGeoId,
                ga.geo_id_to as geoId,
                tg.abbreviation as abbreviation,
                tg.geo_name as geoName,
                tg.geo_type_id as geoTypeId
            FROM geo_assoc ga
            LEFT JOIN geo g ON ga.geo_id = g.geo_id
            LEFT JOIN geo tg ON ga.geo_id_to = tg.geo_id
            WHERE g.geo_type_id = 'COUNTRY' 
            AND (tg.geo_type_id = 'STATE' OR tg.geo_type_id = 'PROVINCE')
            """;

    @Query(value = NORTH_AMERICAN_STATE_OR_PROVINCE_BASE_QUERY + """
            ORDER BY g.geo_id, tg.geo_name
            """,
            nativeQuery = true)
    List<StateProvinceProjection> findAllNorthAmericanStatesAndProvinces();

    @Query(value = STATE_OR_PROVINCE_BASE_QUERY + """
            AND g.geo_id = :countryId
            ORDER BY ga.geo_id, tg.geo_name
            """,
            nativeQuery = true)
    List<StateProvinceProjection> findStatesAndProvincesByCountryId(@Param("countryId") String countryId);

    @Query(value = "select g.geo_id as geoId,g.geo_type_id as geoTypeId,g.geo_name as geoName,g.geo_code as geoCode,g.geo_sec_code as geoSecCode,g.abbreviation from geo g where g.geo_type_id='COUNTRY'", nativeQuery = true)
    List<BffGeoProjection> findAllCountries();

    @Query(value = NORTH_AMERICAN_STATE_OR_PROVINCE_BASE_QUERY + """
            AND (
                LOWER(ga.geo_id_to) = LOWER(:keyword)
                OR LOWER(tg.geo_name) LIKE LOWER(:keyword) || '%'
            )
            ORDER BY 
                CASE 
                    WHEN LOWER(ga.geo_id_to) = LOWER(:keyword) THEN 0
                    WHEN LOWER(tg.geo_name) LIKE LOWER(:keyword) || '%' THEN 1
                    ELSE 2
                END
            LIMIT 1
            """,
            nativeQuery = true)
    Optional<StateProvinceProjection> findOneNorthAmericanStateOrProvinceByKeyword(
            @Param("keyword") String keyword
    );

    interface StateProvinceProjection extends BffGeoProjection {
    }
}