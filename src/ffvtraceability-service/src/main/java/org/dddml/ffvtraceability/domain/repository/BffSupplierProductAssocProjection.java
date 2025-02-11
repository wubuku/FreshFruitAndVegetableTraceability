package org.dddml.ffvtraceability.domain.repository;

public interface BffSupplierProductAssocProjection extends SupplierProductAssocIdProjection {
    Long getVersion();

    String getSupplierId();

    String getSupplierName();
}
