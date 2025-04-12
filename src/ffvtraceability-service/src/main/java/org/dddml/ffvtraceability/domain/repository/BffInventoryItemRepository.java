package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.inventoryitem.AbstractInventoryItemState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BffInventoryItemRepository extends JpaRepository<AbstractInventoryItemState.SimpleInventoryItemState, String> {

    @Query(value = """
            SELECT
               g.product_id as productId,
               p.product_name as productName,
               p.quantity_uom_id as quantityUomId,
               g.facility_id as facilityId,
               f.facility_name as facilityName,
               g.total_quantity as quantityOnHandTotal
            FROM
              (SELECT i.product_id,
                      i.facility_id,
                      SUM(i.quantity_on_hand_total) AS total_quantity
               FROM inventory_item i
               --LEFT JOIN lot l ON i.lot_id = l.lot_id
               LEFT JOIN product p ON i.product_id = p.product_id
               LEFT JOIN facility f ON i.facility_id = f.facility_id
               WHERE p.product_type_id = :productTypeId
                 AND (:productId is null or i.product_id = :productId)
                 AND (:facilityId is null or i.facility_id = :facilityId)
                 AND (:productName is null or p.product_name like concat('%', :productName, '%'))
               GROUP BY i.product_id,
                        i.facility_id
                          ) g
            LEFT JOIN product p ON p.product_id=g.product_id
            LEFT JOIN facility f ON g.facility_id=f.facility_id
            """, countQuery = """
            SELECT COUNT(*)
            FROM inventory_item i
               LEFT JOIN product p ON i.product_id = p.product_id
               LEFT JOIN facility f ON i.facility_id = f.facility_id
               WHERE p.product_type_id = :productTypeId
                 AND (:productId is null or i.product_id = :productId)
                 AND (:facilityId is null or i.facility_id = :facilityId)
                 AND (:productName is null or p.product_name like concat('%', :productName, '%'))
               GROUP BY i.product_id,
                        i.facility_id
            """, nativeQuery = true)
    Page<BffWipInventoryGroupProjection> findAllWipInventories(Pageable pageable,
                                                                @Param("productTypeId") String productTypeId,
                                                                @Param("productId") String productId,
                                                                @Param("productName") String productName,
                                                                @Param("facilityId") String facilityId);

    @Query(value = """
            SELECT
               g.product_id as productId,
               p.product_name as productName,
               p.quantity_uom_id as quantityUomId,
               g.supplier_id as supplierId,
               pa.short_description as supplierName,
               g.facility_id as facilityId,
               f.facility_name as facilityName,
               g.total_quantity as quantityOnHandTotal
            FROM
              (SELECT i.product_id,
                      l.supplier_id,
                      i.facility_id,
                      SUM(i.quantity_on_hand_total) AS total_quantity
               FROM inventory_item i
               LEFT JOIN lot l ON i.lot_id = l.lot_id
               LEFT JOIN product p ON i.product_id = p.product_id
               LEFT JOIN facility f ON i.facility_id = f.facility_id
               WHERE p.product_type_id = 'RAW_MATERIAL'
                 AND (:productId is null or i.product_id = :productId)
                 AND (:supplierId is null or l.supplier_id = :supplierId)
                 AND (:facilityId is null or i.facility_id = :facilityId)
                 AND (:productName is null or p.product_name like concat('%', :productName, '%'))
               GROUP BY i.product_id,
                        l.supplier_id,
                        i.facility_id
                          ) g
            LEFT JOIN product p ON p.product_id=g.product_id
            LEFT JOIN facility f ON g.facility_id=f.facility_id
            left join party pa on pa.party_id = g.supplier_id
            """, countQuery = """
            SELECT COUNT(*)
            FROM inventory_item i
               LEFT JOIN lot l ON i.lot_id = l.lot_id
               LEFT JOIN product p ON i.product_id = p.product_id
               LEFT JOIN facility f ON i.facility_id = f.facility_id
               WHERE p.product_type_id = 'RAW_MATERIAL'
                 AND (:productId is null or i.product_id = :productId)
                 AND (:supplierId is null or l.supplier_id = :supplierId)
                 AND (:facilityId is null or i.facility_id = :facilityId)
                 AND (:productName is null or p.product_name like concat('%', :productName, '%'))
               GROUP BY i.product_id,
                        l.supplier_id,
                        i.facility_id
            """, nativeQuery = true)
    Page<BffRawItemInventoryGroupProjection> findAllRawItemInventories(Pageable pageable,
                                                                @Param("productId") String productId,
                                                                @Param("productName") String productName,
                                                                @Param("supplierId") String supplierId,
                                                                @Param("facilityId") String facilityId);
    /*@Query(value = """
            SELECT
            i.inventory_item_id as inventoryItemId,
            i.product_id as productId,
            p.product_name as productName,
            party.party_id as supplierId,
            party.short_description as supplierShortName,
            i.facility_id as facilityId,
            i.lot_id as lotId,
            i.location_seq_id as locationSeqId,
            i.quantity_on_hand_total as quantityOnHandTotal,
            i.comments as comments
            FROM inventory_item i
            left join product p on i.product_id = p.product_id
            left join lot l on i.lot_id = l.lot_id
            left join party on l.supplier_id = party.party_id
            WHERE (:facilityId is null or i.facility_id = :facilityId)
            AND (:productId is null or i.product_id = :productId)
            AND (:supplierId is null or l.supplier_id = :supplierId)
            AND p.product_type_id = :productTypeId
            ORDER BY i.created_at desc
            """, countQuery = """
            SELECT COUNT(*)
            FROM inventory_item i
            left join product p on i.product_id = p.product_id
            left join lot l on i.lot_id = l.lot_id
            left join party on l.supplier_id = party.party_id
            WHERE (:facilityId is null or i.facility_id = :facilityId)
            AND (:productId is null or i.product_id = :productId)
            AND (:supplierId is null or l.supplier_id = :supplierId)
            AND p.product_type_id = :productTypeId
            """, nativeQuery = true)
    Page<BffInventoryItemProjection> findAllInventoryItems(Pageable pageable,
                                                           @Param("productTypeId") String productTypeId,
                                                           @Param("productId") String productId,
                                                           @Param("supplierId") String supplierId,
                                                           @Param("facilityId") String facilityId);*/

//    @Query(value = """
//            SELECT * from inventory_item i
//            left join lot l on i.lot_id = l.lot_id
//            where i.product_id = :productId
//            and i.facility_id = :facilityId
//            and l.supplier_id = :supplierId
//            and l.lot_id = :lotId
//            and i.location_seq_id = :locationSeqId
//            """, nativeQuery = true)
//    Optional<BffInventoryItemProjection>
//    findInventoryItemsByProductAndSupplierAndFacility(@Param("productId") String productId,
//                                                      @Param("supplierId") String supplierId,
//                                                      @Param("facilityId") String facilityId,
//                                                      @Param("locationSeqId") String locationSeqId,
//                                                      @Param("lotId") String lotId);

}