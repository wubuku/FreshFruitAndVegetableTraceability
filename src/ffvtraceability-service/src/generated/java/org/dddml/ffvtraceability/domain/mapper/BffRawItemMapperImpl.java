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
        bffRawItemDto.setBrandName( bffRawItem.getBrandName() );
        bffRawItemDto.setCaseUomId( bffRawItem.getCaseUomId() );
        bffRawItemDto.setCountryOfOrigin( bffRawItem.getCountryOfOrigin() );
        bffRawItemDto.setDefaultShipmentBoxTypeId( bffRawItem.getDefaultShipmentBoxTypeId() );
        bffRawItemDto.setDepthUomId( bffRawItem.getDepthUomId() );
        bffRawItemDto.setDescription( bffRawItem.getDescription() );
        bffRawItemDto.setDiameterUomId( bffRawItem.getDiameterUomId() );
        bffRawItemDto.setGtin( bffRawItem.getGtin() );
        bffRawItemDto.setHandlingInstructions( bffRawItem.getHandlingInstructions() );
        bffRawItemDto.setHeightUomId( bffRawItem.getHeightUomId() );
        bffRawItemDto.setHsCode( bffRawItem.getHsCode() );
        bffRawItemDto.setInternalId( bffRawItem.getInternalId() );
        bffRawItemDto.setInternalName( bffRawItem.getInternalName() );
        bffRawItemDto.setLargeImageUrl( bffRawItem.getLargeImageUrl() );
        bffRawItemDto.setMaterialCompositionDescription( bffRawItem.getMaterialCompositionDescription() );
        bffRawItemDto.setMediumImageUrl( bffRawItem.getMediumImageUrl() );
        bffRawItemDto.setOrganicCertifications( bffRawItem.getOrganicCertifications() );
        bffRawItemDto.setPiecesIncluded( bffRawItem.getPiecesIncluded() );
        bffRawItemDto.setProduceVariety( bffRawItem.getProduceVariety() );
        bffRawItemDto.setProductDepth( bffRawItem.getProductDepth() );
        bffRawItemDto.setProductDiameter( bffRawItem.getProductDiameter() );
        bffRawItemDto.setProductHeight( bffRawItem.getProductHeight() );
        bffRawItemDto.setProductId( bffRawItem.getProductId() );
        bffRawItemDto.setProductName( bffRawItem.getProductName() );
        bffRawItemDto.setProductWeight( bffRawItem.getProductWeight() );
        bffRawItemDto.setProductWidth( bffRawItem.getProductWidth() );
        bffRawItemDto.setQuantityIncluded( bffRawItem.getQuantityIncluded() );
        bffRawItemDto.setQuantityUomId( bffRawItem.getQuantityUomId() );
        bffRawItemDto.setShelfLifeDescription( bffRawItem.getShelfLifeDescription() );
        bffRawItemDto.setShippingDepth( bffRawItem.getShippingDepth() );
        bffRawItemDto.setShippingHeight( bffRawItem.getShippingHeight() );
        bffRawItemDto.setShippingWeight( bffRawItem.getShippingWeight() );
        bffRawItemDto.setShippingWidth( bffRawItem.getShippingWidth() );
        bffRawItemDto.setSmallImageUrl( bffRawItem.getSmallImageUrl() );
        bffRawItemDto.setStatusId( bffRawItem.getStatusId() );
        bffRawItemDto.setStorageConditions( bffRawItem.getStorageConditions() );
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
        bffRawItemDto.setBrandName( productState.getBrandName() );
        bffRawItemDto.setCaseUomId( productState.getCaseUomId() );
        bffRawItemDto.setCountryOfOrigin( productState.getCountryOfOrigin() );
        bffRawItemDto.setDefaultShipmentBoxTypeId( productState.getDefaultShipmentBoxTypeId() );
        bffRawItemDto.setDepthUomId( productState.getDepthUomId() );
        bffRawItemDto.setDescription( productState.getDescription() );
        bffRawItemDto.setDiameterUomId( productState.getDiameterUomId() );
        bffRawItemDto.setHandlingInstructions( productState.getHandlingInstructions() );
        bffRawItemDto.setHeightUomId( productState.getHeightUomId() );
        bffRawItemDto.setInternalName( productState.getInternalName() );
        bffRawItemDto.setLargeImageUrl( productState.getLargeImageUrl() );
        bffRawItemDto.setMaterialCompositionDescription( productState.getMaterialCompositionDescription() );
        bffRawItemDto.setMediumImageUrl( productState.getMediumImageUrl() );
        bffRawItemDto.setOrganicCertifications( productState.getOrganicCertifications() );
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
        bffRawItemDto.setShelfLifeDescription( productState.getShelfLifeDescription() );
        bffRawItemDto.setShippingDepth( productState.getShippingDepth() );
        bffRawItemDto.setShippingHeight( productState.getShippingHeight() );
        bffRawItemDto.setShippingWeight( productState.getShippingWeight() );
        bffRawItemDto.setShippingWidth( productState.getShippingWidth() );
        bffRawItemDto.setSmallImageUrl( productState.getSmallImageUrl() );
        bffRawItemDto.setStorageConditions( productState.getStorageConditions() );
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
        simpleCreateProduct.setBrandName( bffRawItemDto.getBrandName() );
        simpleCreateProduct.setCaseUomId( bffRawItemDto.getCaseUomId() );
        simpleCreateProduct.setCountryOfOrigin( bffRawItemDto.getCountryOfOrigin() );
        simpleCreateProduct.setDefaultShipmentBoxTypeId( bffRawItemDto.getDefaultShipmentBoxTypeId() );
        simpleCreateProduct.setDepthUomId( bffRawItemDto.getDepthUomId() );
        simpleCreateProduct.setDescription( bffRawItemDto.getDescription() );
        simpleCreateProduct.setDiameterUomId( bffRawItemDto.getDiameterUomId() );
        simpleCreateProduct.setHandlingInstructions( bffRawItemDto.getHandlingInstructions() );
        simpleCreateProduct.setHeightUomId( bffRawItemDto.getHeightUomId() );
        simpleCreateProduct.setInternalName( bffRawItemDto.getInternalName() );
        simpleCreateProduct.setLargeImageUrl( bffRawItemDto.getLargeImageUrl() );
        simpleCreateProduct.setMaterialCompositionDescription( bffRawItemDto.getMaterialCompositionDescription() );
        simpleCreateProduct.setMediumImageUrl( bffRawItemDto.getMediumImageUrl() );
        simpleCreateProduct.setOrganicCertifications( bffRawItemDto.getOrganicCertifications() );
        simpleCreateProduct.setPiecesIncluded( bffRawItemDto.getPiecesIncluded() );
        simpleCreateProduct.setProductDepth( bffRawItemDto.getProductDepth() );
        simpleCreateProduct.setProductDiameter( bffRawItemDto.getProductDiameter() );
        simpleCreateProduct.setProductHeight( bffRawItemDto.getProductHeight() );
        simpleCreateProduct.setProductName( bffRawItemDto.getProductName() );
        simpleCreateProduct.setProductWeight( bffRawItemDto.getProductWeight() );
        simpleCreateProduct.setProductWidth( bffRawItemDto.getProductWidth() );
        simpleCreateProduct.setQuantityIncluded( bffRawItemDto.getQuantityIncluded() );
        simpleCreateProduct.setQuantityUomId( bffRawItemDto.getQuantityUomId() );
        simpleCreateProduct.setShelfLifeDescription( bffRawItemDto.getShelfLifeDescription() );
        simpleCreateProduct.setShippingDepth( bffRawItemDto.getShippingDepth() );
        simpleCreateProduct.setShippingHeight( bffRawItemDto.getShippingHeight() );
        simpleCreateProduct.setShippingWeight( bffRawItemDto.getShippingWeight() );
        simpleCreateProduct.setShippingWidth( bffRawItemDto.getShippingWidth() );
        simpleCreateProduct.setSmallImageUrl( bffRawItemDto.getSmallImageUrl() );
        simpleCreateProduct.setStorageConditions( bffRawItemDto.getStorageConditions() );
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
        simpleMergePatchProduct.setBrandName( bffRawItemDto.getBrandName() );
        simpleMergePatchProduct.setCaseUomId( bffRawItemDto.getCaseUomId() );
        simpleMergePatchProduct.setCountryOfOrigin( bffRawItemDto.getCountryOfOrigin() );
        simpleMergePatchProduct.setDefaultShipmentBoxTypeId( bffRawItemDto.getDefaultShipmentBoxTypeId() );
        simpleMergePatchProduct.setDepthUomId( bffRawItemDto.getDepthUomId() );
        simpleMergePatchProduct.setDescription( bffRawItemDto.getDescription() );
        simpleMergePatchProduct.setDiameterUomId( bffRawItemDto.getDiameterUomId() );
        simpleMergePatchProduct.setHandlingInstructions( bffRawItemDto.getHandlingInstructions() );
        simpleMergePatchProduct.setHeightUomId( bffRawItemDto.getHeightUomId() );
        simpleMergePatchProduct.setInternalName( bffRawItemDto.getInternalName() );
        simpleMergePatchProduct.setLargeImageUrl( bffRawItemDto.getLargeImageUrl() );
        simpleMergePatchProduct.setMaterialCompositionDescription( bffRawItemDto.getMaterialCompositionDescription() );
        simpleMergePatchProduct.setMediumImageUrl( bffRawItemDto.getMediumImageUrl() );
        simpleMergePatchProduct.setOrganicCertifications( bffRawItemDto.getOrganicCertifications() );
        simpleMergePatchProduct.setPiecesIncluded( bffRawItemDto.getPiecesIncluded() );
        simpleMergePatchProduct.setProductDepth( bffRawItemDto.getProductDepth() );
        simpleMergePatchProduct.setProductDiameter( bffRawItemDto.getProductDiameter() );
        simpleMergePatchProduct.setProductHeight( bffRawItemDto.getProductHeight() );
        simpleMergePatchProduct.setProductName( bffRawItemDto.getProductName() );
        simpleMergePatchProduct.setProductWeight( bffRawItemDto.getProductWeight() );
        simpleMergePatchProduct.setProductWidth( bffRawItemDto.getProductWidth() );
        simpleMergePatchProduct.setQuantityIncluded( bffRawItemDto.getQuantityIncluded() );
        simpleMergePatchProduct.setQuantityUomId( bffRawItemDto.getQuantityUomId() );
        simpleMergePatchProduct.setShelfLifeDescription( bffRawItemDto.getShelfLifeDescription() );
        simpleMergePatchProduct.setShippingDepth( bffRawItemDto.getShippingDepth() );
        simpleMergePatchProduct.setShippingHeight( bffRawItemDto.getShippingHeight() );
        simpleMergePatchProduct.setShippingWeight( bffRawItemDto.getShippingWeight() );
        simpleMergePatchProduct.setShippingWidth( bffRawItemDto.getShippingWidth() );
        simpleMergePatchProduct.setSmallImageUrl( bffRawItemDto.getSmallImageUrl() );
        simpleMergePatchProduct.setStorageConditions( bffRawItemDto.getStorageConditions() );
        simpleMergePatchProduct.setWeightUomId( bffRawItemDto.getWeightUomId() );
        simpleMergePatchProduct.setWidthUomId( bffRawItemDto.getWidthUomId() );

        return simpleMergePatchProduct;
    }
}
