package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.qainspection.AbstractQaInspectionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BffQaInspectionRepository extends JpaRepository<AbstractQaInspectionState.SimpleQaInspectionState, String> {
    @Query(value = """
            SELECT 
                qi.qa_inspection_id as qaInspectionId,
                qi.receipt_id as receiptId,
                qi.status_id as statusId,
                qi.inspection_type_id as inspectionTypeId,
                sr.product_id as productId,
                sr.lot_id as lotId,
                qi.comments as comments,
                qi.inspection_facility_id as inspectionFacilityId,
                qi.inspected_by as inspectedBy,
                qi.created_by as createdBy,
                qi.inspected_at as inspectedAt,
                qi.created_at as createdAt,
                qi.updated_at as updatedAt
            FROM qa_inspection qi
            JOIN shipment_receipt sr ON qi.receipt_id = sr.receipt_id
            WHERE sr.shipment_id = :receivingDocumentId
            AND (:receiptId IS NULL OR sr.receipt_id = :receiptId)
            AND (sr.deleted IS NULL OR sr.deleted = false)
            ORDER BY qi.inspected_at DESC
            """, nativeQuery = true)
    List<BffQaInspectionProjection> findQaInspections(
            @Param("receivingDocumentId") String receivingDocumentId,
            @Param("receiptId") String receiptId);
    // NOTE: 这里返回的检验信息先忽略了一些属性：
    //   s.party_id_from as supplierId,
    //   JOIN shipment s ON sr.shipment_id = s.shipment_id

}
