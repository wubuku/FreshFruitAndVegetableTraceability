package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.party.AbstractPartyState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BffSupplierRepository extends JpaRepository<AbstractPartyState, String> {
    @Query(value = """
            SELECT 
                p.party_id as supplierId,
                p.external_id as externalId,
                p.preferred_currency_uom_id as preferredCurrencyUomId,
                p.description as description,
                p.status_id as statusId,
                ggn.id_value as ggn,
                gln.id_value as gln
            FROM party p
            JOIN party_role pr ON pr.party_id = p.party_id
            LEFT JOIN (
                SELECT 
                    pi.party_id,
                    pi.id_value
                FROM party_identification pi
                WHERE pi.party_identification_type_id = 'GGN'
                    AND (pi.deleted IS NULL OR pi.deleted = false)
            ) ggn ON ggn.party_id = p.party_id
            LEFT JOIN (
                SELECT 
                    pi.party_id,
                    pi.id_value
                FROM party_identification pi
                WHERE pi.party_identification_type_id = 'GLN'
                    AND (pi.deleted IS NULL OR pi.deleted = false)
            ) gln ON gln.party_id = p.party_id
            WHERE pr.role_type_id = 'SUPPLIER'
                AND (:statusId IS NULL OR p.status_id = :statusId)
                AND (p.deleted IS NULL OR p.deleted = false)
                AND (pr.deleted IS NULL OR pr.deleted = false)
            ORDER BY p.created_at DESC
            """,
            countQuery = """
                    SELECT COUNT(DISTINCT p.party_id)
                    FROM party p
                    JOIN party_role pr ON pr.party_id = p.party_id
                    WHERE pr.role_type_id = 'SUPPLIER'
                        AND (:statusId IS NULL OR p.status_id = :statusId)
                        AND (p.deleted IS NULL OR p.deleted = false)
                        AND (pr.deleted IS NULL OR pr.deleted = false)
                    """,
            nativeQuery = true)
    Page<BffSupplierProjection> findAllSuppliers(Pageable pageable, @Param("statusId") String statusId);
}