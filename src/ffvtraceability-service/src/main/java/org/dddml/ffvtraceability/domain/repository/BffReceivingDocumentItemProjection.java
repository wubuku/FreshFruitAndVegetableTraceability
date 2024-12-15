package org.dddml.ffvtraceability.domain.repository;

public interface BffReceivingDocumentItemProjection extends BffReceivingDocumentProjection, BffReceivingItemProjection {

    String getReferenceDocumentId();

    //String getReferenceDocumentTypeId();

    String getReferenceComments();

    String getReferenceDocumentLocation();

    String getReferenceDocumentText();
}
