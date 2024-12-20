package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.uom.AbstractUomState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BffUomRepository extends JpaRepository<AbstractUomState.SimpleUomState, String> {
    @Query(value = """
            SELECT 
                u.uom_id as uomId,
                u.uom_type_id as uomTypeId,
                u.abbreviation as abbreviation,
                u.numeric_code as numericCode,
                u.description as description,
                u.gs1_ai as gs1AI,
                u.active as active
            FROM uom u
            WHERE (:active IS NULL OR u.active = :active)
            ORDER BY u.created_at DESC
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM uom u
                    WHERE (:active IS NULL OR u.active = :active)
                    """,
            nativeQuery = true)
    Page<BffUomProjection> findAllUnitsOfMeasure(Pageable pageable, @Param("active") String active);
}