package org.dddml.ffvtraceability.domain.mapper;

import org.dddml.ffvtraceability.domain.repository.BffSupplierProductAssocProjection;
import org.dddml.ffvtraceability.domain.supplierproduct.SupplierProductAssocId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring"//,
        //unmappedTargetPolicy = ReportingPolicy.ERROR,
        //unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface BffSupplierProductAssocIdMapper {
    default OffsetDateTime instantToOffsetDateTime(Instant instant) {
        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
    }

    SupplierProductAssocId toSupplierProductAssocId(BffSupplierProductAssocProjection supplierProductAssocProjection);


}
