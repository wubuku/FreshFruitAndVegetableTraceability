package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;

import org.dddml.ffvtraceability.domain.BffLotDto;
import org.dddml.ffvtraceability.domain.repository.BffLotProjection;
import org.springframework.stereotype.Component;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class BffLotMapperImpl implements BffLotMapper {

    @Override
    public BffLotDto toBffLotDto(BffLotProjection bffLotProjection) {
        if (bffLotProjection == null) {
            return null;
        }

        BffLotDto bffLotDto = new BffLotDto();

        bffLotDto.setExpirationDate(instantToOffsetDateTime(bffLotProjection.getExpirationDateInstant()));
        bffLotDto.setGs1Batch(bffLotProjection.getGs1Batch());
        bffLotDto.setLotId(bffLotProjection.getLotId());
        bffLotDto.setQuantity(bffLotProjection.getQuantity());

        return bffLotDto;
    }
}
