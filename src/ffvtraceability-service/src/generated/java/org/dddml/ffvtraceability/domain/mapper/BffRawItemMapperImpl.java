package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffRawItemDto;
import org.dddml.ffvtraceability.domain.product.AbstractProductCommand;
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

        bffRawItemDto.setActive( bffRawItem.getActive() );
        bffRawItemDto.setDefaultShipmentBoxTypeId( bffRawItem.getDefaultShipmentBoxTypeId() );
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

        bffRawItemDto.setActive( productState.getActive() );
        bffRawItemDto.setDefaultShipmentBoxTypeId( productState.getDefaultShipmentBoxTypeId() );
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

    @Override
    public AbstractProductCommand.SimpleCreateProduct toCreateProduct(BffRawItemDto bffRawItemDto) {
        if ( bffRawItemDto == null ) {
            return null;
        }

        AbstractProductCommand.SimpleCreateProduct simpleCreateProduct = new AbstractProductCommand.SimpleCreateProduct();

        simpleCreateProduct.setProductId( bffRawItemDto.getProductId() );
        simpleCreateProduct.setActive( bffRawItemDto.getActive() );
        simpleCreateProduct.setDefaultShipmentBoxTypeId( bffRawItemDto.getDefaultShipmentBoxTypeId() );
        simpleCreateProduct.setDepthUomId( bffRawItemDto.getDepthUomId() );
        simpleCreateProduct.setDescription( bffRawItemDto.getDescription() );
        simpleCreateProduct.setDiameterUomId( bffRawItemDto.getDiameterUomId() );
        simpleCreateProduct.setHeightUomId( bffRawItemDto.getHeightUomId() );
        simpleCreateProduct.setLargeImageUrl( bffRawItemDto.getLargeImageUrl() );
        simpleCreateProduct.setMediumImageUrl( bffRawItemDto.getMediumImageUrl() );
        simpleCreateProduct.setPiecesIncluded( bffRawItemDto.getPiecesIncluded() );
        simpleCreateProduct.setProductDepth( bffRawItemDto.getProductDepth() );
        simpleCreateProduct.setProductDiameter( bffRawItemDto.getProductDiameter() );
        simpleCreateProduct.setProductHeight( bffRawItemDto.getProductHeight() );
        simpleCreateProduct.setProductName( bffRawItemDto.getProductName() );
        simpleCreateProduct.setProductWeight( bffRawItemDto.getProductWeight() );
        simpleCreateProduct.setProductWidth( bffRawItemDto.getProductWidth() );
        simpleCreateProduct.setQuantityIncluded( bffRawItemDto.getQuantityIncluded() );
        simpleCreateProduct.setQuantityUomId( bffRawItemDto.getQuantityUomId() );
        simpleCreateProduct.setShippingDepth( bffRawItemDto.getShippingDepth() );
        simpleCreateProduct.setShippingHeight( bffRawItemDto.getShippingHeight() );
        simpleCreateProduct.setShippingWeight( bffRawItemDto.getShippingWeight() );
        simpleCreateProduct.setShippingWidth( bffRawItemDto.getShippingWidth() );
        simpleCreateProduct.setSmallImageUrl( bffRawItemDto.getSmallImageUrl() );
        simpleCreateProduct.setWeightUomId( bffRawItemDto.getWeightUomId() );
        simpleCreateProduct.setWidthUomId( bffRawItemDto.getWidthUomId() );

        return simpleCreateProduct;
    }

    @Override
    public AbstractProductCommand.SimpleMergePatchProduct toMergePatchProduct(BffRawItemDto bffRawItemDto) {
        if ( bffRawItemDto == null ) {
            return null;
        }

        AbstractProductCommand.SimpleMergePatchProduct simpleMergePatchProduct = new AbstractProductCommand.SimpleMergePatchProduct();

        simpleMergePatchProduct.setProductId( bffRawItemDto.getProductId() );
        simpleMergePatchProduct.setActive( bffRawItemDto.getActive() );
        simpleMergePatchProduct.setDefaultShipmentBoxTypeId( bffRawItemDto.getDefaultShipmentBoxTypeId() );
        simpleMergePatchProduct.setDepthUomId( bffRawItemDto.getDepthUomId() );
        simpleMergePatchProduct.setDescription( bffRawItemDto.getDescription() );
        simpleMergePatchProduct.setDiameterUomId( bffRawItemDto.getDiameterUomId() );
        simpleMergePatchProduct.setHeightUomId( bffRawItemDto.getHeightUomId() );
        simpleMergePatchProduct.setLargeImageUrl( bffRawItemDto.getLargeImageUrl() );
        simpleMergePatchProduct.setMediumImageUrl( bffRawItemDto.getMediumImageUrl() );
        simpleMergePatchProduct.setPiecesIncluded( bffRawItemDto.getPiecesIncluded() );
        simpleMergePatchProduct.setProductDepth( bffRawItemDto.getProductDepth() );
        simpleMergePatchProduct.setProductDiameter( bffRawItemDto.getProductDiameter() );
        simpleMergePatchProduct.setProductHeight( bffRawItemDto.getProductHeight() );
        simpleMergePatchProduct.setProductName( bffRawItemDto.getProductName() );
        simpleMergePatchProduct.setProductWeight( bffRawItemDto.getProductWeight() );
        simpleMergePatchProduct.setProductWidth( bffRawItemDto.getProductWidth() );
        simpleMergePatchProduct.setQuantityIncluded( bffRawItemDto.getQuantityIncluded() );
        simpleMergePatchProduct.setQuantityUomId( bffRawItemDto.getQuantityUomId() );
        simpleMergePatchProduct.setShippingDepth( bffRawItemDto.getShippingDepth() );
        simpleMergePatchProduct.setShippingHeight( bffRawItemDto.getShippingHeight() );
        simpleMergePatchProduct.setShippingWeight( bffRawItemDto.getShippingWeight() );
        simpleMergePatchProduct.setShippingWidth( bffRawItemDto.getShippingWidth() );
        simpleMergePatchProduct.setSmallImageUrl( bffRawItemDto.getSmallImageUrl() );
        simpleMergePatchProduct.setWeightUomId( bffRawItemDto.getWeightUomId() );
        simpleMergePatchProduct.setWidthUomId( bffRawItemDto.getWidthUomId() );

        return simpleMergePatchProduct;
    }
}
