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

        bffRawItemDto.setProductId( bffRawItem.getProductId() );
        bffRawItemDto.setProductName( bffRawItem.getProductName() );
        bffRawItemDto.setDescription( bffRawItem.getDescription() );
        bffRawItemDto.setGtin( bffRawItem.getGtin() );
        bffRawItemDto.setSmallImageUrl( bffRawItem.getSmallImageUrl() );
        bffRawItemDto.setMediumImageUrl( bffRawItem.getMediumImageUrl() );
        bffRawItemDto.setLargeImageUrl( bffRawItem.getLargeImageUrl() );
        bffRawItemDto.setQuantityUomId( bffRawItem.getQuantityUomId() );
        bffRawItemDto.setQuantityIncluded( bffRawItem.getQuantityIncluded() );
        bffRawItemDto.setPiecesIncluded( bffRawItem.getPiecesIncluded() );
        bffRawItemDto.setStatusId( bffRawItem.getStatusId() );
        bffRawItemDto.setSupplierId( bffRawItem.getSupplierId() );
        bffRawItemDto.setSupplierName( bffRawItem.getSupplierName() );
        bffRawItemDto.setWeightUomId( bffRawItem.getWeightUomId() );
        bffRawItemDto.setShippingWeight( bffRawItem.getShippingWeight() );
        bffRawItemDto.setProductWeight( bffRawItem.getProductWeight() );
        bffRawItemDto.setHeightUomId( bffRawItem.getHeightUomId() );
        bffRawItemDto.setProductHeight( bffRawItem.getProductHeight() );
        bffRawItemDto.setShippingHeight( bffRawItem.getShippingHeight() );
        bffRawItemDto.setWidthUomId( bffRawItem.getWidthUomId() );
        bffRawItemDto.setProductWidth( bffRawItem.getProductWidth() );
        bffRawItemDto.setShippingWidth( bffRawItem.getShippingWidth() );
        bffRawItemDto.setDepthUomId( bffRawItem.getDepthUomId() );
        bffRawItemDto.setProductDepth( bffRawItem.getProductDepth() );
        bffRawItemDto.setShippingDepth( bffRawItem.getShippingDepth() );
        bffRawItemDto.setDiameterUomId( bffRawItem.getDiameterUomId() );
        bffRawItemDto.setProductDiameter( bffRawItem.getProductDiameter() );
        bffRawItemDto.setActive( bffRawItem.getActive() );
        bffRawItemDto.setDefaultShipmentBoxTypeId( bffRawItem.getDefaultShipmentBoxTypeId() );

        return bffRawItemDto;
    }

    @Override
    public BffRawItemDto toBffRawItemDto(ProductState productState) {
        if ( productState == null ) {
            return null;
        }

        BffRawItemDto bffRawItemDto = new BffRawItemDto();

        bffRawItemDto.setProductId( productState.getProductId() );
        bffRawItemDto.setProductName( productState.getProductName() );
        bffRawItemDto.setDescription( productState.getDescription() );
        bffRawItemDto.setSmallImageUrl( productState.getSmallImageUrl() );
        bffRawItemDto.setMediumImageUrl( productState.getMediumImageUrl() );
        bffRawItemDto.setLargeImageUrl( productState.getLargeImageUrl() );
        bffRawItemDto.setQuantityUomId( productState.getQuantityUomId() );
        bffRawItemDto.setQuantityIncluded( productState.getQuantityIncluded() );
        bffRawItemDto.setPiecesIncluded( productState.getPiecesIncluded() );
        bffRawItemDto.setWeightUomId( productState.getWeightUomId() );
        bffRawItemDto.setShippingWeight( productState.getShippingWeight() );
        bffRawItemDto.setProductWeight( productState.getProductWeight() );
        bffRawItemDto.setHeightUomId( productState.getHeightUomId() );
        bffRawItemDto.setProductHeight( productState.getProductHeight() );
        bffRawItemDto.setShippingHeight( productState.getShippingHeight() );
        bffRawItemDto.setWidthUomId( productState.getWidthUomId() );
        bffRawItemDto.setProductWidth( productState.getProductWidth() );
        bffRawItemDto.setShippingWidth( productState.getShippingWidth() );
        bffRawItemDto.setDepthUomId( productState.getDepthUomId() );
        bffRawItemDto.setProductDepth( productState.getProductDepth() );
        bffRawItemDto.setShippingDepth( productState.getShippingDepth() );
        bffRawItemDto.setDiameterUomId( productState.getDiameterUomId() );
        bffRawItemDto.setProductDiameter( productState.getProductDiameter() );
        bffRawItemDto.setActive( productState.getActive() );
        bffRawItemDto.setDefaultShipmentBoxTypeId( productState.getDefaultShipmentBoxTypeId() );

        return bffRawItemDto;
    }

    @Override
    public AbstractProductCommand.SimpleCreateProduct toCreateProduct(BffRawItemDto bffRawItemDto) {
        if ( bffRawItemDto == null ) {
            return null;
        }

        AbstractProductCommand.SimpleCreateProduct simpleCreateProduct = new AbstractProductCommand.SimpleCreateProduct();

        simpleCreateProduct.setProductId( bffRawItemDto.getProductId() );
        simpleCreateProduct.setProductName( bffRawItemDto.getProductName() );
        simpleCreateProduct.setDescription( bffRawItemDto.getDescription() );
        simpleCreateProduct.setSmallImageUrl( bffRawItemDto.getSmallImageUrl() );
        simpleCreateProduct.setMediumImageUrl( bffRawItemDto.getMediumImageUrl() );
        simpleCreateProduct.setLargeImageUrl( bffRawItemDto.getLargeImageUrl() );
        simpleCreateProduct.setQuantityUomId( bffRawItemDto.getQuantityUomId() );
        simpleCreateProduct.setQuantityIncluded( bffRawItemDto.getQuantityIncluded() );
        simpleCreateProduct.setPiecesIncluded( bffRawItemDto.getPiecesIncluded() );
        simpleCreateProduct.setWeightUomId( bffRawItemDto.getWeightUomId() );
        simpleCreateProduct.setShippingWeight( bffRawItemDto.getShippingWeight() );
        simpleCreateProduct.setProductWeight( bffRawItemDto.getProductWeight() );
        simpleCreateProduct.setHeightUomId( bffRawItemDto.getHeightUomId() );
        simpleCreateProduct.setProductHeight( bffRawItemDto.getProductHeight() );
        simpleCreateProduct.setShippingHeight( bffRawItemDto.getShippingHeight() );
        simpleCreateProduct.setWidthUomId( bffRawItemDto.getWidthUomId() );
        simpleCreateProduct.setProductWidth( bffRawItemDto.getProductWidth() );
        simpleCreateProduct.setShippingWidth( bffRawItemDto.getShippingWidth() );
        simpleCreateProduct.setDepthUomId( bffRawItemDto.getDepthUomId() );
        simpleCreateProduct.setProductDepth( bffRawItemDto.getProductDepth() );
        simpleCreateProduct.setShippingDepth( bffRawItemDto.getShippingDepth() );
        simpleCreateProduct.setDiameterUomId( bffRawItemDto.getDiameterUomId() );
        simpleCreateProduct.setProductDiameter( bffRawItemDto.getProductDiameter() );
        simpleCreateProduct.setDefaultShipmentBoxTypeId( bffRawItemDto.getDefaultShipmentBoxTypeId() );
        simpleCreateProduct.setActive( bffRawItemDto.getActive() );

        return simpleCreateProduct;
    }

    @Override
    public AbstractProductCommand.SimpleMergePatchProduct toMergePatchProduct(BffRawItemDto bffRawItemDto) {
        if ( bffRawItemDto == null ) {
            return null;
        }

        AbstractProductCommand.SimpleMergePatchProduct simpleMergePatchProduct = new AbstractProductCommand.SimpleMergePatchProduct();

        simpleMergePatchProduct.setProductId( bffRawItemDto.getProductId() );
        simpleMergePatchProduct.setProductName( bffRawItemDto.getProductName() );
        simpleMergePatchProduct.setDescription( bffRawItemDto.getDescription() );
        simpleMergePatchProduct.setSmallImageUrl( bffRawItemDto.getSmallImageUrl() );
        simpleMergePatchProduct.setMediumImageUrl( bffRawItemDto.getMediumImageUrl() );
        simpleMergePatchProduct.setLargeImageUrl( bffRawItemDto.getLargeImageUrl() );
        simpleMergePatchProduct.setQuantityUomId( bffRawItemDto.getQuantityUomId() );
        simpleMergePatchProduct.setQuantityIncluded( bffRawItemDto.getQuantityIncluded() );
        simpleMergePatchProduct.setPiecesIncluded( bffRawItemDto.getPiecesIncluded() );
        simpleMergePatchProduct.setWeightUomId( bffRawItemDto.getWeightUomId() );
        simpleMergePatchProduct.setShippingWeight( bffRawItemDto.getShippingWeight() );
        simpleMergePatchProduct.setProductWeight( bffRawItemDto.getProductWeight() );
        simpleMergePatchProduct.setHeightUomId( bffRawItemDto.getHeightUomId() );
        simpleMergePatchProduct.setProductHeight( bffRawItemDto.getProductHeight() );
        simpleMergePatchProduct.setShippingHeight( bffRawItemDto.getShippingHeight() );
        simpleMergePatchProduct.setWidthUomId( bffRawItemDto.getWidthUomId() );
        simpleMergePatchProduct.setProductWidth( bffRawItemDto.getProductWidth() );
        simpleMergePatchProduct.setShippingWidth( bffRawItemDto.getShippingWidth() );
        simpleMergePatchProduct.setDepthUomId( bffRawItemDto.getDepthUomId() );
        simpleMergePatchProduct.setProductDepth( bffRawItemDto.getProductDepth() );
        simpleMergePatchProduct.setShippingDepth( bffRawItemDto.getShippingDepth() );
        simpleMergePatchProduct.setDiameterUomId( bffRawItemDto.getDiameterUomId() );
        simpleMergePatchProduct.setProductDiameter( bffRawItemDto.getProductDiameter() );
        simpleMergePatchProduct.setDefaultShipmentBoxTypeId( bffRawItemDto.getDefaultShipmentBoxTypeId() );
        simpleMergePatchProduct.setActive( bffRawItemDto.getActive() );

        return simpleMergePatchProduct;
    }
}
