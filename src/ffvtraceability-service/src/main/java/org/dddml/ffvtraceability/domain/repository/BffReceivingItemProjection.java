package org.dddml.ffvtraceability.domain.repository;

public interface BffReceivingItemProjection {
    String getReceiptId();

    String getProductId();

    String getLotId();

    String getLocationSeqId();

    String getItemDescription();

    java.math.BigDecimal getQuantityAccepted();

    java.math.BigDecimal getQuantityRejected();

    Long getCasesAccepted();

    Long getCasesRejected();

    String getOrderId();

    String getOrderItemSeqId();

    String getReturnId();

    String getReturnItemSeqId();

    String getRejectionId();

    String getShipmentId();

    String getShipmentItemSeqId();

    String getShipmentPackageSeqId();
}
