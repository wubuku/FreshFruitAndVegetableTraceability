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

        bffRawItemDto.setDepthUomId( bffRawItem.getDepthUomId() );
        bffRawItemDto.setDescription( bffRawItem.getDescription() );
        bffRawItemDto.setDiameterUomId( bffRawItem.getDiameterUomId() );
        bffRawItemDto.setGtin( bffRawItem.getGtin() );
        bffRawItemDto.setHeightUomId( bffRawItem.getHeightUomId() );
        bffRawItemDto.setLargeImageUrl( bffRawItem.getLargeImageUrl() );
        bffRawItemDto.setMediumImageUrl( bffRawItem.getMediumImageUrl() );
        bffRawItemDto.setPiecesIncluded( bffRawItem.getPiecesIncluded() );
        bffRawItemDto.setProductDepth( bffRawItem.getProductDepth() );
        bffRawItemDto.setProductDiameter( bffRawItem.getProductDiameter() );
        bffRawItemDto.setProductHeight( bffRawItem.getProductHeight() );
        bffRawItemDto.setProductId( bffRawItem.getProductId() );
        bffRawItemDto.setProductName( bffRawItem.getProductName() );
        bffRawItemDto.setProductWeight( bffRawItem.getProductWeight() );
        bffRawItemDto.setProductWidth( bffRawItem.getProductWidth() );
        bffRawItemDto.setQuantityIncluded( bffRawItem.getQuantityIncluded() );
        bffRawItemDto.setQuantityUomId( bffRawItem.getQuantityUomId() );
        bffRawItemDto.setShippingDepth( bffRawItem.getShippingDepth() );
        bffRawItemDto.setShippingHeight( bffRawItem.getShippingHeight() );
        bffRawItemDto.setShippingWeight( bffRawItem.getShippingWeight() );
        bffRawItemDto.setShippingWidth( bffRawItem.getShippingWidth() );
        bffRawItemDto.setSmallImageUrl( bffRawItem.getSmallImageUrl() );
        bffRawItemDto.setStatusId( bffRawItem.getStatusId() );
        bffRawItemDto.setSupplierId( bffRawItem.getSupplierId() );
        bffRawItemDto.setSupplierName( bffRawItem.getSupplierName() );
        bffRawItemDto.setWeightUomId( bffRawItem.getWeightUomId() );
        bffRawItemDto.setWidthUomId( bffRawItem.getWidthUomId() );

        return bffRawItemDto;
    }

    @Override
    public BffRawItemDto toBffRawItemDto(ProductState productState) {
        if ( productState == null ) {
            return null;
        }

        BffRawItemDto bffRawItemDto = new BffRawItemDto();

        bffRawItemDto.setDepthUomId( productState.getDepthUomId() );
        bffRawItemDto.setDescription( productState.getDescription() );
        bffRawItemDto.setDiameterUomId( productState.getDiameterUomId() );
        bffRawItemDto.setHeightUomId( productState.getHeightUomId() );
        bffRawItemDto.setLargeImageUrl( productState.getLargeImageUrl() );
        bffRawItemDto.setMediumImageUrl( productState.getMediumImageUrl() );
        bffRawItemDto.setPiecesIncluded( productState.getPiecesIncluded() );
        bffRawItemDto.setProductDepth( productState.getProductDepth() );
        bffRawItemDto.setProductDiameter( productState.getProductDiameter() );
        bffRawItemDto.setProductHeight( productState.getProductHeight() );
        bffRawItemDto.setProductId( productState.getProductId() );
        bffRawItemDto.setProductName( productState.getProductName() );
        bffRawItemDto.setProductWeight( productState.getProductWeight() );
        bffRawItemDto.setProductWidth( productState.getProductWidth() );
        bffRawItemDto.setQuantityIncluded( productState.getQuantityIncluded() );
        bffRawItemDto.setQuantityUomId( productState.getQuantityUomId() );
        bffRawItemDto.setShippingDepth( productState.getShippingDepth() );
        bffRawItemDto.setShippingHeight( productState.getShippingHeight() );
        bffRawItemDto.setShippingWeight( productState.getShippingWeight() );
        bffRawItemDto.setShippingWidth( productState.getShippingWidth() );
        bffRawItemDto.setSmallImageUrl( productState.getSmallImageUrl() );
        bffRawItemDto.setWeightUomId( productState.getWeightUomId() );
        bffRawItemDto.setWidthUomId( productState.getWidthUomId() );

        return bffRawItemDto;
    }
}
