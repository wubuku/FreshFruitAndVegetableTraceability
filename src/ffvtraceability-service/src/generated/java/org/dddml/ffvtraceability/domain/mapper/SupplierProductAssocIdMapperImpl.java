package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.repository.SupplierProductAssocIdProjection;
import org.dddml.ffvtraceability.domain.supplierproduct.SupplierProductAssocId;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class SupplierProductAssocIdMapperImpl implements SupplierProductAssocIdMapper {

    @Override
    public SupplierProductAssocId toSupplierProductAssocId(SupplierProductAssocIdProjection supplierProductAssocIdProjection) {
        if ( supplierProductAssocIdProjection == null ) {
            return null;
        }

        SupplierProductAssocId supplierProductAssocId = new SupplierProductAssocId();

        supplierProductAssocId.setAvailableFromDate( instantToOffsetDateTime( supplierProductAssocIdProjection.getAvailableFromDateInstant() ) );
        supplierProductAssocId.setProductId( supplierProductAssocIdProjection.getProductId() );
        supplierProductAssocId.setPartyId( supplierProductAssocIdProjection.getPartyId() );
        supplierProductAssocId.setCurrencyUomId( supplierProductAssocIdProjection.getCurrencyUomId() );
        supplierProductAssocId.setMinimumOrderQuantity( supplierProductAssocIdProjection.getMinimumOrderQuantity() );

        return supplierProductAssocId;
    }
}
