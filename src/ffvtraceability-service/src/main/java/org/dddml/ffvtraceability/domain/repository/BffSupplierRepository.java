package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.BffSupplierDto;
import org.dddml.ffvtraceability.domain.party.AbstractPartyState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BffSupplierRepository extends JpaRepository<AbstractPartyState, String> {
    @Query(value = """
            SELECT 
                p.party_id as supplierId,
                p.external_id as externalId,
                p.preferred_currency_uom_id as preferredCurrencyUomId,
                p.description as description,
                p.status_id as statusId,
                COALESCE(p.organization_name, p.last_name) as supplierName,
                ggn.id_value as ggn,
                gln.id_value as gln,
                ii.id_value as internalId,
                ti.id_value as taxId,
                gcp.id_value as gs1CompanyPrefix
            FROM party p
            JOIN party_role pr ON pr.party_id = p.party_id
            LEFT JOIN (
                SELECT 
                    pi.party_id,
                    pi.id_value
                FROM party_identification pi
                WHERE pi.party_identification_type_id = 'GGN'
            ) ggn ON ggn.party_id = p.party_id
            LEFT JOIN (
                SELECT 
                    pi.party_id,
                    pi.id_value
                FROM party_identification pi
                WHERE pi.party_identification_type_id = 'GLN'
            ) gln ON gln.party_id = p.party_id
                        
            LEFT JOIN (
                SELECT 
                    pi.party_id,
                    pi.id_value
                FROM party_identification pi
                WHERE pi.party_identification_type_id = 'INTERNAL_ID'
            ) ii ON ii.party_id = p.party_id
                        
                        
            LEFT JOIN (
                SELECT 
                    pi.party_id,
                    pi.id_value
                FROM party_identification pi
                WHERE pi.party_identification_type_id = 'TAX_ID'
            ) ti ON ti.party_id = p.party_id
                        
                        
            LEFT JOIN (
                SELECT 
                    pi.party_id,
                    pi.id_value
                FROM party_identification pi
                WHERE pi.party_identification_type_id = 'GCP'
            ) gcp ON gcp.party_id = p.party_id
                        
            WHERE pr.role_type_id = 'SUPPLIER'
                AND (:statusId IS NULL OR p.status_id = :statusId)
            ORDER BY p.created_at DESC
            """,
            countQuery = """
                    SELECT COUNT(DISTINCT p.party_id)
                    FROM party p
                    JOIN party_role pr ON pr.party_id = p.party_id
                    WHERE pr.role_type_id = 'SUPPLIER'
                        AND (:statusId IS NULL OR p.status_id = :statusId)
                    """,
            nativeQuery = true)
    Page<BffSupplierProjection> findAllSuppliers(Pageable pageable, @Param("statusId") String statusId);

    @Query(value = """
            SELECT
                p.party_id as supplierId,
                p.external_id as externalId,
                p.preferred_currency_uom_id as preferredCurrencyUomId,
                p.description as description,
                p.status_id as statusId,
                p.email AS email,
                p.web_site AS webSite,
                COALESCE(p.organization_name, p.last_name) as supplierName,
                pr.bank_account_information AS bankAccountInformation,
                pr.certification_codes AS certificationCodes,
                pr.supplier_short_name AS supplierShortName,
                pr.tpa_number AS tpaNumber,
                pr.supplier_product_type_description AS supplierProductTypeDescription,
                pr.supplier_type_enum_id AS supplierTypeEnumId,
                ggn.id_value as ggn,
                gln.id_value as gln,
                ii.id_value as internalId,
                ti.id_value as taxId,
                gcp.id_value as gs1CompanyPrefix
            FROM party p
            JOIN party_role pr ON pr.party_id = p.party_id
            LEFT JOIN (
                SELECT
                    pi.party_id,
                    pi.id_value
                FROM party_identification pi
                WHERE pi.party_identification_type_id = 'GGN'
            ) ggn ON ggn.party_id = p.party_id
            LEFT JOIN (
                SELECT
                    pi.party_id,
                    pi.id_value
                FROM party_identification pi
                WHERE pi.party_identification_type_id = 'GLN'
            ) gln ON gln.party_id = p.party_id
                     
            LEFT JOIN (
                SELECT
                    pi.party_id,
                    pi.id_value
                FROM party_identification pi
                WHERE pi.party_identification_type_id = 'INTERNAL_ID'
            ) ii ON ii.party_id = p.party_id
            
            LEFT JOIN (
                SELECT
                    pi.party_id,
                    pi.id_value
                FROM party_identification pi
                WHERE pi.party_identification_type_id = 'TAX_ID'
            ) ti ON ti.party_id = p.party_id
            
            LEFT JOIN (
                SELECT
                    pi.party_id,
                    pi.id_value
                FROM party_identification pi
                WHERE pi.party_identification_type_id = 'GCP'
            ) gcp ON gcp.party_id = p.party_id
            
            WHERE pr.role_type_id = 'SUPPLIER'
            AND p.party_id=:supplierId
            """, nativeQuery = true)
    Optional<BffSupplierProjection> findSupplierById(@Param("supplierId") String supplierId);
}