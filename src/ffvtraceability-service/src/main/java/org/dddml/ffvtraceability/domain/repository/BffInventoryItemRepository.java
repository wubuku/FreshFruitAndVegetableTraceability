package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.inventoryitem.AbstractInventoryItemState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface BffInventoryItemRepository extends JpaRepository<AbstractInventoryItemState.SimpleInventoryItemState, String> {

    @Query(value = """
            SELECT
               g.product_id as productId,
               p.product_name as productName,
               p.quantity_uom_id as quantityUomId,
               sp.quantity_included as quantityIncluded,
               sp.case_uom_id as caseUomId,
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
            LEFT JOIN party pa on pa.party_id = g.supplier_id
            LEFT JOIN supplier_product sp ON sp.product_id = g.product_id
                     AND sp.party_id = g.supplier_id
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

    @Query(value = """
            SELECT
                i.product_id AS productId,
                i.lot_id AS lotId,
                i.inventory_item_id AS inventoryItemId,
                l.internal_id AS lotNo,
                p.quantity_uom_id AS quantityUomId,
                sp.quantity_included AS quantityIncluded,
                i.quantity_on_hand_total AS quantityOnHandTotal,
                sp.case_uom_id AS caseUomId,
                i.created_at AS receivedAt,
                fl.location_code AS locationCode
             FROM
            inventory_item i
            LEFT JOIN lot l ON i.lot_id = l.lot_id
            LEFT JOIN product p ON p.product_id = i.product_id
            LEFT JOIN supplier_product sp ON l.supplier_id=sp.party_id AND i.product_id=sp.product_id
            LEFT JOIN facility_location fl ON i.location_seq_id = fl.location_seq_id
            WHERE i.product_id = :productId
            AND i.facility_id = :facilityId
            AND l.supplier_id = :supplierId
            """, countQuery = """
            SELECT COUNT(*)
             FROM
            inventory_item i
            LEFT JOIN lot l ON i.lot_id = l.lot_id
            LEFT JOIN product p ON p.product_id = i.product_id
            LEFT JOIN supplier_product sp ON l.supplier_id=sp.party_id AND i.product_id=sp.product_id
            LEFT JOIN facility_location fl ON i.location_seq_id = fl.location_seq_id
            WHERE i.product_id = :productId
            AND i.facility_id = :facilityId
            AND l.supplier_id = :supplierId
            """, nativeQuery = true)
    Page<BffRawItemInventoryItemProjection> findRawItemInventoryItems(Pageable pageable,
                                                                      @Param("productId") String productId,
                                                                      @Param("supplierId") String supplierId,
                                                                      @Param("facilityId") String facilityId);

    @Query(value = """
            SELECT
               i.inventory_item_id as inventoryItemId,
               i.product_id as productId,
               p.product_name as productName,
               i.lot_id as lotId,
               p.quantity_uom_id as quantityUomId,
               sp.quantity_included as quantityIncluded,
               sp.case_uom_id as caseUomId,
               i.facility_id as facilityId,
               f.facility_name as facilityName,
               ii.id_value as facilityInternalId,
               i.location_seq_id as locationSeqId,
               fl.location_name as locationName,
               fl.location_code as locationCode,
               i.quantity_on_hand_total as quantityOnHandTotal
            from inventory_item i
            left join product p on i.product_id = p.product_id
            left join facility f on i.facility_id = f.facility_id
            left join facility_location fl on i.location_seq_id = fl.location_seq_id
            left join lot l on i.lot_id = l.lot_id
            left join supplier_product sp on sp.party_id = l.supplier_id AND i.product_id = sp.product_id
            LEFT JOIN (
                SELECT
                    fi.facility_id,
                    fi.id_value
                FROM facility_identification fi
                WHERE fi.facility_identification_type_id = 'INTERNAL_ID'
            ) ii ON ii.facility_id = f.facility_id
            WHERE (:productId is null or i.product_id = :productId)
                 AND (:lotId is null or i.lot_id = :lotId)
            """, countQuery = """
            SELECT COUNT(*)
            FROM inventory_item i
            WHERE (:productId is null or i.product_id = :productId)
                 AND (:lotId is null or i.lot_id = :lotId)
            """, nativeQuery = true)
    Page<BffInventoryItemProjection> findRawItemInventories(Pageable pageable,
                                                            @Param("productId") String productId,
                                                            @Param("lotId") String lotId);

    @Query(value = """
            SELECT
               g.product_id as productId,
               p.product_name as productName,
               p.quantity_uom_id as quantityUomId,
               p.quantity_included as quantityIncluded,
               p.case_uom_id as caseUomId,
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
    Page<BffProductInventoryGroupProjection> findAllProductInventories(Pageable pageable,
                                                                       @Param("productTypeId") String productTypeId,
                                                                       @Param("productId") String productId,
                                                                       @Param("productName") String productName,
                                                                       @Param("facilityId") String facilityId);

    @Query(value = """
            SELECT
               i.inventory_item_id as inventoryItemId,
               i.product_id as productId,
               p.product_name as productName,
               i.lot_id as lotId,
               p.quantity_uom_id as quantityUomId,
               p.quantity_included as quantityIncluded,
               p.case_uom_id as caseUomId,
               i.facility_id as facilityId,
               f.facility_name as facilityName,
               ii.id_value as facilityInternalId,
               i.location_seq_id as locationSeqId,
               fl.location_name as locationName,
               fl.location_code as locationCode,
               i.quantity_on_hand_total as quantityOnHandTotal
            from inventory_item i
            left join product p on i.product_id = p.product_id
            left join facility f on i.facility_id = f.facility_id
            left join facility_location fl on i.location_seq_id = fl.location_seq_id
            LEFT JOIN (
                SELECT
                    fi.facility_id,
                    fi.id_value
                FROM facility_identification fi
                WHERE fi.facility_identification_type_id = 'INTERNAL_ID'
            ) ii ON ii.facility_id = f.facility_id
            WHERE (:productId is null or i.product_id = :productId)
                 AND (:lotId is null or i.lot_id = :lotId)
            """, countQuery = """
            SELECT COUNT(*)
            FROM inventory_item i
            WHERE (:productId is null or i.product_id = :productId)
                 AND (:lotId is null or i.lot_id = :lotId)
            """, nativeQuery = true)
    Page<BffInventoryItemProjection> findProductInventories(Pageable pageable,
                                                            @Param("productId") String productId,
                                                            @Param("lotId") String lotId);

    @Query(value = """
            SELECT
               g.product_id as productId,
               p.product_name as productName,
               p.quantity_uom_id as quantityUomId,
               sp.quantity_included as quantityIncluded,
               sp.case_uom_id as caseUomId,
               g.supplier_id as supplierId,
               g.lot_id as lotId,
               l.internal_id AS lotNo,
               g.facility_id as facilityId,
               ii.id_value as productInternalId,
               g.quantity_on_hand_total as quantityOnHandTotal
            FROM
              (SELECT i.product_id,
                      l.supplier_id,
                      i.facility_id,
                      i.lot_id,
                      SUM(i.quantity_on_hand_total) AS quantity_on_hand_total
               FROM inventory_item i
               LEFT JOIN lot l ON i.lot_id = l.lot_id
               LEFT JOIN product p ON i.product_id = p.product_id
               -- LEFT JOIN facility f ON i.facility_id = f.facility_id
               WHERE p.product_type_id = 'RAW_MATERIAL'
                 AND (:productId is null or i.product_id = :productId)
                 AND (:supplierId is null or l.supplier_id = :supplierId)
                 AND (:facilityId is null or i.facility_id = :facilityId)
               GROUP BY i.product_id,
                        l.supplier_id,
                        i.facility_id,
                        i.lot_id
                          ) g
            LEFT JOIN product p ON p.product_id=g.product_id
            LEFT JOIN lot l ON l.lot_id = g.lot_id
            LEFT JOIN (
                SELECT
                    gi.product_id,
                    gi.id_value
                FROM good_identification gi
                WHERE gi.good_identification_type_id = 'INTERNAL_ID'
            ) ii ON ii.product_id = p.product_id
            LEFT JOIN supplier_product sp ON sp.product_id = g.product_id
                     AND sp.party_id = g.supplier_id
            """, countQuery = """
            SELECT COUNT(*)
            FROM inventory_item i
               LEFT JOIN lot l ON i.lot_id = l.lot_id
               LEFT JOIN product p ON i.product_id = p.product_id
               WHERE p.product_type_id = 'RAW_MATERIAL'
                 AND (:productId is null or i.product_id = :productId)
                 AND (:supplierId is null or l.supplier_id = :supplierId)
                 AND (:facilityId is null or i.facility_id = :facilityId)
               GROUP BY i.product_id,
                        l.supplier_id,
                        i.facility_id
            """, nativeQuery = true)
    @Deprecated
    Page<BffInventoryByLotNoProjection> getRawItemInventoriesGroupByLot(Pageable pageable,
                                                                        @Param("productId") String productId,
                                                                        @Param("supplierId") String supplierId,
                                                                        @Param("facilityId") String facilityId);

//    @Query(value = """
//            SELECT
//               g.product_id as productId,
//               p.product_name as productName,
//               p.quantity_uom_id as quantityUomId,
//               sp.quantity_included as quantityIncluded,
//               sp.case_uom_id as caseUomId,
//               g.supplier_id as supplierId,
//               g.lot_id as lotId,
//               l.internal_id AS lotNo,
//               g.facility_id as facilityId,
//               ii.id_value as productInternalId,
//               g.quantity_on_hand_total as quantityOnHandTotal
//            FROM
//              (SELECT i.product_id,
//                      l.supplier_id,
//                      i.facility_id,
//                      i.lot_id,
//                      SUM(i.quantity_on_hand_total) AS quantity_on_hand_total
//               FROM inventory_item i
//               LEFT JOIN lot l ON i.lot_id = l.lot_id
//               LEFT JOIN product p ON i.product_id = p.product_id
//               -- LEFT JOIN facility f ON i.facility_id = f.facility_id
//               WHERE p.product_type_id = 'RAW_MATERIAL'
//                 AND (:productId is null or i.product_id = :productId)
//                 AND (:supplierId is null or l.supplier_id = :supplierId)
//                 AND (:facilityId is null or i.facility_id = :facilityId)
//               GROUP BY i.product_id,
//                        l.supplier_id,
//                        i.facility_id,
//                        i.lot_id
//                          ) g
//            LEFT JOIN product p ON p.product_id=g.product_id
//            LEFT JOIN lot l ON l.lot_id = g.lot_id
//            LEFT JOIN (
//                SELECT
//                    gi.product_id,
//                    gi.id_value
//                FROM good_identification gi
//                WHERE gi.good_identification_type_id = 'INTERNAL_ID'
//            ) ii ON ii.product_id = p.product_id
//            LEFT JOIN supplier_product sp ON sp.product_id = g.product_id
//                     AND sp.party_id = g.supplier_id
//            """, countQuery = """
//            SELECT COUNT(*)
//            FROM inventory_item_detail iid
//                LEFT JOIN inventory_item i ON i.inventory_item_id = iid.inventory_item_id
//                LEFT JOIN product p ON p.product_id = iii.product_id
//                LEFT JOIN lot l ON i.lot_id = l.lot_id
//                LEFT JOIN product p ON i.product_id = p.product_id
//            WHERE (:productTypeId is null or p.product_type_id = :productTypeId)
//                AND (:productId is null or i.product_id = :productId)
//                AND (:facilityId is null or i.facility_id = :facilityId)
//            """, nativeQuery = true)
//    Page<BffRawItemInventoryItemProjection> getInventoryItemDetails(Pageable pageable,
//                                                                    @Param("productTypeId") String productTypeId,
//                                                                    @Param("productId") String productId,
//                                                                    @Param("facilityId") String facilityId);

    @Query(value = """
              SELECT
              s.primary_order_id AS orderId,
              qi.status_id AS qaStatusId,
              sr.datetime_received AS receivedAt,
              sr.shipment_id AS receivingDocumentId
              FROM shipment_receipt sr
              LEFT JOIN shipment s ON sr.shipment_id=s.shipment_id
              LEFT JOIN qa_inspection qi ON qi.receipt_id=sr.receipt_id
              WHERE sr.product_id = :productId
              AND sr.lot_id = :lotId
              ORDER BY sr.datetime_received DESC LIMIT 1
            """, nativeQuery = true)
    Optional<BffInventoryItemReceivingProjection> getInventoryItemReceiving(@Param("productId") String productId,
                                                                            @Param("lotId") String lotId);


    @Query(value = """
            SELECT
               i.inventory_item_id as inventoryItemId,
               i.product_id as productId,
               p.product_name as productName,
               i.lot_id as lotId,
               p.quantity_uom_id as quantityUomId,
               sp.quantity_included as quantityIncluded,
               sp.case_uom_id as caseUomId,
               i.facility_id as facilityId,
               f.facility_name as facilityName,
               ii.id_value as facilityInternalId,
               i.location_seq_id as locationSeqId,
               fl.location_name as locationName,
               fl.location_code as locationCode,
               i.quantity_on_hand_total as quantityOnHandTotal
            from inventory_item i
            left join product p on i.product_id = p.product_id
            left join facility f on i.facility_id = f.facility_id
            left join facility_location fl on i.location_seq_id = fl.location_seq_id
            left join lot l on i.lot_id = l.lot_id
            left join supplier_product sp on sp.party_id = l.supplier_id AND i.product_id = sp.product_id
            LEFT JOIN (
                SELECT
                    fi.facility_id,
                    fi.id_value
                FROM facility_identification fi
                WHERE fi.facility_identification_type_id = 'INTERNAL_ID'
            ) ii ON ii.facility_id = f.facility_id
            WHERE i.product_id = :productId
                  and i.lot_id = :lotId
                  and i.location_seq_id = :locationSeqId
                  and i.facility_id = :facilityId
            limit 1
            """, nativeQuery = true)
    Optional<BffInventoryItemProjection> findInventoryItem(@Param("productId") String productId,
                                                           @Param("facilityId") String facilityId,
                                                           @Param("locationSeqId") String locationSeqId,
                                                           @Param("lotId") String lotId);

    interface BffInventoryItemReceivingProjection {

        Instant getReceivedAt();

        String getQaStatusId();

        String getOrderId();

        String getReceivingDocumentId();
    }

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