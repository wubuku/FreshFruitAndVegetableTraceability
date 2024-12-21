package org.dddml.ffvtraceability.domain.repository;

import java.time.Instant;

public interface BffReceivingDocumentItemProjection extends BffReceivingDocumentProjection, BffReceivingItemProjection {

    String getReferenceDocumentId();

    //String getReferenceDocumentTypeId();

    String getReferenceComments();

    String getReferenceDocumentLocation();

    String getReferenceDocumentText();

    //Instant getCreatedAtInstant();
}
