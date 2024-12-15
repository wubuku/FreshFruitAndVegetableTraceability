package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.shipmentreceipt.AbstractShipmentReceiptState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BffReceiptRepository extends JpaRepository<AbstractShipmentReceiptState.SimpleShipmentReceiptState, String> {

    @Query(value = "SELECT " +
            "   s.shipment_id as documentId, " +
            "   s.status_id as statusId, " +
            "   s.party_id_to as partyIdTo, " +
            "   s.party_id_from as partyIdFrom, " +
            "   s.origin_facility_id as originFacilityId, " +
            "   s.destination_facility_id as destinationFacilityId, " +
            "   s.primary_order_id as primaryOrderId, " +
            "   sr.receipt_id as receiptId, " +
            "   sr.product_id as productId, " +
            "   sr.lot_id as lotId, " +
            "   sr.location_seq_id as locationSeqId, " +
            "   sr.item_description as itemDescription, " +
            "   sr.quantity_accepted as quantityAccepted, " +
            "   sr.quantity_rejected as quantityRejected, " +
            "   sr.cases_accepted as casesAccepted, " +
            "   sr.cases_rejected as casesRejected, " +
            "   sr.datetime_received as datetimeReceived " +
            "FROM shipment s " +
            "LEFT JOIN shipment_receipt sr ON s.shipment_id = sr.shipment_id " +
            "ORDER BY s.shipment_id, sr.receipt_id",
            countQuery = "SELECT COUNT(DISTINCT s.shipment_id) FROM shipment s",
            nativeQuery = true
    )
    Page<BffReceivingDocumentItemProjection> findAllReceivingDocumentsWithItems(Pageable pageable);
}
