package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.repository.BffSupplierProductAssocProjection;
import org.dddml.ffvtraceability.domain.supplierproduct.SupplierProductAssocId;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class BffSupplierProductAssocIdMapperImpl implements BffSupplierProductAssocIdMapper {

    @Override
    public SupplierProductAssocId toSupplierProductAssocId(BffSupplierProductAssocProjection supplierProductAssocProjection) {
        if ( supplierProductAssocProjection == null ) {
            return null;
        }

        SupplierProductAssocId supplierProductAssocId = new SupplierProductAssocId();

        supplierProductAssocId.setAvailableFromDate( instantToOffsetDateTime( supplierProductAssocProjection.getAvailableFromDateInstant() ) );
        supplierProductAssocId.setProductId( supplierProductAssocProjection.getProductId() );
        supplierProductAssocId.setPartyId( supplierProductAssocProjection.getPartyId() );
        supplierProductAssocId.setCurrencyUomId( supplierProductAssocProjection.getCurrencyUomId() );
        supplierProductAssocId.setMinimumOrderQuantity( supplierProductAssocProjection.getMinimumOrderQuantity() );

        return supplierProductAssocId;
    }
}
