package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffRawItemDto;
import org.dddml.ffvtraceability.domain.product.ProductState;
import org.dddml.ffvtraceability.domain.repository.BffRawItemProjection;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class BffRawItemMapperImpl implements BffRawItemMapper {

    @Override
    public BffRawItemDto toBffRawItemDto(BffRawItemProjection bffRawItem) {
        if ( bffRawItem == null ) {
            return null;
        }

        BffRawItemDto bffRawItemDto = new BffRawItemDto();

        bffRawItemDto.setDescription( bffRawItem.getDescription() );
        bffRawItemDto.setGtin( bffRawItem.getGtin() );
        bffRawItemDto.setLargeImageUrl( bffRawItem.getLargeImageUrl() );
        bffRawItemDto.setMediumImageUrl( bffRawItem.getMediumImageUrl() );
        bffRawItemDto.setPiecesIncluded( bffRawItem.getPiecesIncluded() );
        bffRawItemDto.setProductId( bffRawItem.getProductId() );
        bffRawItemDto.setProductName( bffRawItem.getProductName() );
        bffRawItemDto.setQuantityIncluded( bffRawItem.getQuantityIncluded() );
        bffRawItemDto.setQuantityUomId( bffRawItem.getQuantityUomId() );
        bffRawItemDto.setSmallImageUrl( bffRawItem.getSmallImageUrl() );
        bffRawItemDto.setStatusId( bffRawItem.getStatusId() );
        bffRawItemDto.setSupplierId( bffRawItem.getSupplierId() );
        bffRawItemDto.setSupplierName( bffRawItem.getSupplierName() );

        return bffRawItemDto;
    }

    @Override
    public BffRawItemDto toBffRawItemDto(ProductState productState) {
        if ( productState == null ) {
            return null;
        }

        BffRawItemDto bffRawItemDto = new BffRawItemDto();

        bffRawItemDto.setDescription( productState.getDescription() );
        bffRawItemDto.setLargeImageUrl( productState.getLargeImageUrl() );
        bffRawItemDto.setMediumImageUrl( productState.getMediumImageUrl() );
        bffRawItemDto.setPiecesIncluded( productState.getPiecesIncluded() );
        bffRawItemDto.setProductId( productState.getProductId() );
        bffRawItemDto.setProductName( productState.getProductName() );
        bffRawItemDto.setQuantityIncluded( productState.getQuantityIncluded() );
        bffRawItemDto.setQuantityUomId( productState.getQuantityUomId() );
        bffRawItemDto.setSmallImageUrl( productState.getSmallImageUrl() );

        return bffRawItemDto;
    }
}
