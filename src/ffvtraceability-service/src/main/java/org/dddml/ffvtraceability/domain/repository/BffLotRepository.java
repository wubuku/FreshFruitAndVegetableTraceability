package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.lot.AbstractLotState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BffLotRepository extends JpaRepository<AbstractLotState.SimpleLotState, String> {
    //
    // NOTE: Cannot project java.time.Instant to java.time.OffsetDateTime in native queries.
    //
    String COMMON_FROM_JOIN =
            "FROM lot l " +
                    "LEFT JOIN lot_identification li ON l.lot_id = li.lot_id " +
                    "    AND li.lot_identification_type_id = 'GS1_BATCH' ";

    String COMMON_WHERE =
            "WHERE (:active IS NULL OR l.active = :active) " +
                    "    AND (:keyword IS NULL OR " +
                    "        LOWER(l.lot_id) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                    "        LOWER(li.id_value) LIKE LOWER(CONCAT('%', :keyword, '%'))) ";

    String COMMON_SELECT =
            "SELECT " +
                    "    l.lot_id as lotId, " +
                    "    l.expiration_date as expirationDate, " +
                    "    l.quantity as quantity, " +
                    "    l.active as active, " +
                    "    l.internal_id as internalId, " +
                    "    l.gtin as gtin, " +
                    "    l.source_facility_id as sourceFacilityId, " +
                    "    l.pallet_sscc as palletSscc, " +
                    "    l.pack_date as packDate, " +
                    "    l.harvest_date as harvestDate, " +
                    "    l.serial_number as serialNumber, " +
                    "    li.id_value as gs1Batch ";

    @Query(value =
            COMMON_SELECT +
                    COMMON_FROM_JOIN +
                    COMMON_WHERE +
                    "ORDER BY l.lot_id",
            countQuery =
                    "SELECT COUNT(*) " +
                            COMMON_FROM_JOIN +
                            COMMON_WHERE,
            nativeQuery = true)
    Page<BffLotProjection> findAllLots(Pageable pageable, @Param("active") String active, @Param("keyword") String keyword);

    @Query(value =
            COMMON_SELECT +
                    "FROM lot l " +
                    "LEFT JOIN lot_identification li ON l.lot_id = li.lot_id " +
                    "WHERE li.lot_identification_type_id = 'TLC_CASE_GTIN_BATCH' " +
                    "    AND li.gtin = :caseGtin " +
                    "    AND li.gs1_batch = :caseBatch",
            nativeQuery = true)
    Optional<BffLotProjection> findByCaseGtinAndBatch(
            @Param("caseGtin") String caseGtin,
            @Param("caseBatch") String caseBatch
    );

}