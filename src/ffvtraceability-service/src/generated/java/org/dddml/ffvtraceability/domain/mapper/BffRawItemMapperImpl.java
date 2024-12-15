package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffRawItemDto;
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

        return bffRawItemDto;
    }
}
