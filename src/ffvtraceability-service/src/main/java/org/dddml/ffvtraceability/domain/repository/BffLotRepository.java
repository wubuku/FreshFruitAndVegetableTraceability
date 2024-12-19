package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.lot.AbstractLotState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BffLotRepository extends JpaRepository<AbstractLotState.SimpleLotState, String> {

    @Query(value = """
            SELECT
                l.lot_id as lotId,
                l.expiration_date as expirationDateInstant,
                l.quantity as quantity,
                l.active as active,
                li.id_value as gs1Batch
            FROM lot l
            LEFT JOIN lot_identification li ON l.lot_id = li.lot_id
                AND li.lot_identification_type_id = 'GS1_BATCH'
            WHERE (:active IS NULL OR l.active = :active)
                AND (l.deleted IS NULL OR l.deleted = false)
            ORDER BY l.lot_id
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM lot l
                    WHERE (:active IS NULL OR l.active = :active)
                        AND (l.deleted IS NULL OR l.deleted = false)
                    """,
            nativeQuery = true)
    Page<BffLotProjection> findAllLots(Pageable pageable, @Param("active") String active);
    // NOTE: Cannot project java.time.Instant to java.time.OffsetDateTime in native queries.
}