package org.dddml.ffvtraceability.domain.repository;

public interface BffReceivingDocumentProjection {
    String getDocumentId();

    String getStatusId();

    String getPartyIdTo();

    String getPartyIdFrom();

    String getOriginFacilityId();

    String getDestinationFacilityId();

    String getPrimaryOrderId();

    String getPrimaryReturnId();

    String getPrimaryShipGroupSeqId();

    //java.util.List<BffReceivingItemDto> getReceivingItems();

    //java.util.List<String> getReferenceDocuments();
}
