package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.shipmentreceipt.AbstractShipmentReceiptState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BffReceiptRepository extends JpaRepository<AbstractShipmentReceiptState.SimpleShipmentReceiptState, String> {

    String COMMON_SHIPMENT_RECEIPT_SELECT = """
            SELECT 
                s.shipment_id as documentId,
                s.status_id as statusId,
                s.party_id_to as partyIdTo,
                s.party_id_from as partyIdFrom,
                p.organization_name as partyNameFrom,
                s.origin_facility_id as originFacilityId,
                f.facility_name as originFacilityName,
                s.destination_facility_id as destinationFacilityId,
                s.primary_order_id as primaryOrderId,
                s.created_at as createdAtInstant,
                sr.receipt_id as receiptId,
                sr.product_id as productId,
                prod.product_name as productName,
                gi.id_value as gtin,
                sr.lot_id as lotId,
                sr.location_seq_id as locationSeqId,
                sr.item_description as itemDescription,
                sr.quantity_accepted as quantityAccepted,
                sr.quantity_rejected as quantityRejected,
                sr.cases_accepted as casesAccepted,
                sr.cases_rejected as casesRejected,
                sr.datetime_received as datetimeReceived
            """;

    String COMMON_SHIPMENT_RECEIPT_JOINS = """
            FROM shipment s
            LEFT JOIN shipment_receipt sr ON s.shipment_id = sr.shipment_id
            LEFT JOIN party p ON s.party_id_from = p.party_id
            LEFT JOIN facility f ON s.origin_facility_id = f.facility_id
            LEFT JOIN product prod ON sr.product_id = prod.product_id
            LEFT JOIN good_identification gi ON prod.product_id = gi.product_id 
                AND gi.good_identification_type_id = 'GTIN'
            """;

    @Query(value = """
            WITH filtered_shipments AS (
                SELECT DISTINCT s.shipment_id, s.created_at
                FROM shipment s
                LEFT JOIN shipment_receipt sr ON s.shipment_id = sr.shipment_id
                LEFT JOIN product prod ON sr.product_id = prod.product_id
                LEFT JOIN good_identification gi ON prod.product_id = gi.product_id 
                    AND gi.good_identification_type_id = 'GTIN'
                WHERE (:documentIdOrItem IS NULL 
                    OR s.shipment_id LIKE CONCAT(:documentIdOrItem, '%')
                    OR sr.product_id LIKE CONCAT(:documentIdOrItem, '%')
                    OR gi.id_value LIKE CONCAT(:documentIdOrItem, '%'))
                ORDER BY s.created_at DESC
                LIMIT :pageSize OFFSET :offset
            )
            """ + COMMON_SHIPMENT_RECEIPT_SELECT + COMMON_SHIPMENT_RECEIPT_JOINS + """
            WHERE fs.shipment_id = s.shipment_id
            ORDER BY s.created_at DESC
            """, nativeQuery = true)
    List<BffReceivingDocumentItemProjection> findAllReceivingDocumentsWithItems(
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("documentIdOrItem") String documentIdOrItem);

    @Query(value = COMMON_SHIPMENT_RECEIPT_SELECT + COMMON_SHIPMENT_RECEIPT_JOINS + """
            WHERE s.shipment_id = :documentId
            """, nativeQuery = true)
    List<BffReceivingDocumentItemProjection> findReceivingDocumentWithItems(@Param("documentId") String documentId);

    @Query(value = """
            SELECT 
                s.shipment_id as documentId,
                sr.receipt_id as receiptId,
                sr.product_id as productId,
                prod.product_name as productName,
                gi.id_value as gtin,
                sr.lot_id as lotId,
                sr.location_seq_id as locationSeqId,
                sr.item_description as itemDescription,
                sr.quantity_accepted as quantityAccepted,
                sr.quantity_rejected as quantityRejected,
                sr.cases_accepted as casesAccepted,
                sr.cases_rejected as casesRejected,
                sr.datetime_received as datetimeReceived
            """ + COMMON_SHIPMENT_RECEIPT_JOINS + """
            WHERE sr.shipment_id = :documentId AND sr.receipt_id = :receiptId
            """, nativeQuery = true)
    BffReceivingDocumentItemProjection findReceivingItem(
            @Param("documentId") String documentId,
            @Param("receiptId") String receiptId);

    @Query(value = "SELECT COUNT(DISTINCT s.shipment_id) " +
            "FROM shipment s " +
            "LEFT JOIN shipment_receipt sr ON s.shipment_id = sr.shipment_id " +
            "LEFT JOIN product prod ON sr.product_id = prod.product_id " +
            "LEFT JOIN good_identification gi ON prod.product_id = gi.product_id AND gi.good_identification_type_id = 'GTIN' " +
            "WHERE (:documentIdOrItem IS NULL OR " +
            "      s.shipment_id LIKE CONCAT(:documentIdOrItem, '%') OR " +
            "      sr.product_id LIKE CONCAT(:documentIdOrItem, '%') OR " +
            "      gi.id_value LIKE CONCAT(:documentIdOrItem, '%'))",
            nativeQuery = true)
    long countTotalShipments(@Param("documentIdOrItem") String documentIdOrItem);

    @Query(value = "SELECT " +
            "   sd.shipment_id as documentId, " +
            "   d.document_id as referenceDocumentId, " +
            "   d.comments as referenceComments, " +
            "   d.document_location as referenceDocumentLocation, " +
            "   d.document_text as referenceDocumentText " +
            "FROM shipping_document sd " +
            "JOIN document d ON sd.document_id = d.document_id " +
            "WHERE sd.shipment_id IN :shipmentIds",
            nativeQuery = true)
    List<BffReceivingDocumentItemProjection> findReferenceDocumentsByShipmentIds(@Param("shipmentIds") List<String> shipmentIds);
}
