package org.dddml.ffvtraceability.domain.repository;

public interface BffDocumentProjection {
    String getDocumentId();

    String getDocumentTypeId();

    String getComments();

    String getDocumentLocation();

    String getDocumentText();
}
