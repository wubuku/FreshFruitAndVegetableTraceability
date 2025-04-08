package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.product.AbstractProductState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public interface BffRawItemRepository extends JpaRepository<AbstractProductState.SimpleProductState, String> {

    String PRIORITY_SUPPLIER_SUBQUERY = """
            SELECT DISTINCT ON (sp.product_id)
                sp.product_id,
                sp.party_id,
                COALESCE(o.short_description,o.organization_name, o.last_name) as supplier_name,
                sp.available_from_date
            FROM supplier_product sp
            LEFT JOIN party o ON o.party_id = sp.party_id
            WHERE sp.available_from_date <= CURRENT_TIMESTAMP
                AND (sp.available_thru_date IS NULL OR sp.available_thru_date > CURRENT_TIMESTAMP)
            ORDER BY sp.product_id, sp.available_from_date DESC
            """;

    String WHERE_CONDITIONS = """
            WHERE p.product_type_id = 'RAW_MATERIAL'
                AND (:supplierId is null or priority_party.party_id = :supplierId)
                AND (:active IS NULL OR p.active = :active)
            """;

    @Query(value = """
            SELECT
                p.product_id as productId,
                p.product_name as productName,
                p.description as description,
                p.small_image_url as smallImageUrl,
                p.medium_image_url as mediumImageUrl,
                p.large_image_url as largeImageUrl,
                p.quantity_uom_id as quantityUomId,
                p.quantity_included as quantityIncluded,
                p.pieces_included as piecesIncluded,
                p.weight_uom_id as weightUomId,
                p.shipping_weight as shippingWeight,
                p.product_weight as productWeight,
                p.height_uom_id as heightUomId,
                p.product_height as productHeight,
                p.shipping_height as shippingHeight,
                p.width_uom_id as widthUomId,
                p.product_width as productWidth,
                p.shipping_width as shippingWidth,
                p.depth_uom_id as depthUomId,
                p.case_uom_id as caseUomId,
                p.product_depth as productDepth,
                p.shipping_depth as shippingDepth,
                p.diameter_uom_id as diameterUomId,
                p.product_diameter as productDiameter,
                p.default_shipment_box_type_id as defaultShipmentBoxTypeId,
                p.certification_codes as certificationCodes,
                p.brand_name as brandName,
                p.produce_variety as produceVariety,
                p.organic_certifications as organicCertifications,
                p.country_of_origin as countryOfOrigin,
                p.shelf_life_description as shelfLifeDescription,
                p.handling_instructions as handlingInstructions,
                p.storage_conditions as storageConditions,
                p.material_composition_description as materialCompositionDescription,
                p.active as active,
                p.individuals_per_package as individualsPerPackage,
                gi.id_value as gtin,
                ii.id_value as internalId,
                priority_party.party_id as supplierId,
                priority_party.supplier_name as supplierName
            
            FROM product p
            LEFT JOIN (
                SELECT
                    gi.product_id,
                    gi.id_value
                FROM good_identification gi
                WHERE gi.good_identification_type_id = 'GTIN'
            ) gi ON gi.product_id = p.product_id
            
            LEFT JOIN (
                SELECT
                    gi.product_id,
                    gi.id_value
                FROM good_identification gi
                WHERE gi.good_identification_type_id = 'INTERNAL_ID'
            ) ii ON ii.product_id = p.product_id
            
            LEFT JOIN (
            """ + PRIORITY_SUPPLIER_SUBQUERY + """
            ) priority_party ON priority_party.product_id = p.product_id
            """ + WHERE_CONDITIONS + """
            ORDER BY p.created_at DESC
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM product p
                    LEFT JOIN (
                    """ + PRIORITY_SUPPLIER_SUBQUERY + """
                    ) priority_party ON priority_party.product_id = p.product_id
                    """ + WHERE_CONDITIONS,
            nativeQuery = true)
    Page<BffRawItemProjection> findAllRawItems(Pageable pageable,
                                               @Param("supplierId") String supplierId,
                                               @Param("active") String active);
    //String tenantId
    //todo AND p.tenant_id = :tenantId
    //todo WHERE p.product_type_id = 'RAW_MATERIAL' ??? 这个地方应该过滤出"原材料"类型的产品？


    // NOTE: 这个查询保证了每个产品只返回一个供应商：
    // `DISTINCT ON (sp.product_id)` - PostgreSQL 特有的语法，它会为每个 product_id 只保留一行。
    // `ORDER BY sp.product_id, sp.available_from_date DESC` - 配合 DISTINCT ON 使用，确保保留的是最新的供应商记录。
    // 所以即使一个产品有多个供应商，这个查询也只会返回 available_from_date 最新的那个供应商的信息。

    @Query(value = """
            SELECT 
                sp.product_id as productId,
                sp.party_id as partyId,
                sp.currency_uom_id as currencyUomId,
                sp.minimum_order_quantity as minimumOrderQuantity,
                sp.available_from_date as availableFromDate,
                sp.version as version
            FROM supplier_product sp
            WHERE sp.product_id = :productId
                AND sp.party_id = :partyId
                AND sp.currency_uom_id = :currencyUomId
                AND sp.minimum_order_quantity = :minimumOrderQuantity
                AND sp.available_from_date <= :currentTime
            ORDER BY sp.available_from_date DESC
            LIMIT 1
            """, nativeQuery = true)
    BffSupplierProductAssocProjection findSupplierProductAssociation(
            @Param("productId") String productId,
            @Param("partyId") String partyId,
            @Param("currencyUomId") String currencyUomId,
            @Param("minimumOrderQuantity") BigDecimal minimumOrderQuantity,
            @Param("currentTime") OffsetDateTime currentTime);


    @Query(value = """
            SELECT partyInfo.product_id, partyInfo.party_id as supplierId, partyInfo.supplier_name as supplierName FROM (
            """ + PRIORITY_SUPPLIER_SUBQUERY + """
            )  AS partyInfo WHERE partyInfo.product_id = :productId
            """, nativeQuery = true)
    BffSupplierProductAssocProjection findSupplierProductAssociationByProductId(
            @Param("productId") String productId);

    @Query(value = """
            SELECT COUNT(*) FROM good_identification 
            WHERE good_identification_type_id = :identificationTypeId
            and id_value = :idValue
            """, nativeQuery = true)
    Integer countByIdentificationTypeIdAndIdValue(@Param("identificationTypeId") String identificationTypeId,
                                                  @Param("idValue") String idValue);

    @Query(value = """
            SELECT product_id FROM good_identification 
            WHERE good_identification_type_id = :identificationTypeId
            and id_value = :idValue
            limit 1
            """, nativeQuery = true)
    String queryProductIdByIdentificationTypeIdAndIdValue(@Param("identificationTypeId") String identificationTypeId,
                                                          @Param("idValue") String idValue);

    @Query(value = """
            SELECT 
            product_id as productId,
            party_id as supplierId,
            currency_uom_id as currencyUomId,
            minimum_order_quantity as minimumOrderQuantity,
            available_from_date as availableFromDate,
            available_thru_date as availableThruDate,
            version as version,
            gtin as gtin,
            quantity_included as quantityIncluded,
            pieces_included as piecesIncluded,
            product_weight as productWeight,
            
            case_uom_id as caseUomId,
            organic_certifications as organicCertifications,
            brand_name as brandName,
            material_composition_description as materialCompositionDescription,
            country_of_origin as countryOfOrigin,
            certification_codes as certificationCodes,
            individuals_per_package as individualsPerPackage
            from supplier_product
            WHERE product_id = :productId
            """, nativeQuery = true)
    List<BffSupplierRawItemProjection> findSupplierRawItemByProductId(@Param("productId") String productId);
}