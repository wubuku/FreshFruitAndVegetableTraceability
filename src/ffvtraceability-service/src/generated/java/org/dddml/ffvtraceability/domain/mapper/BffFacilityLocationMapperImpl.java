package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffFacilityLocationDto;
import org.dddml.ffvtraceability.domain.facilitylocation.FacilityLocationState;
import org.dddml.ffvtraceability.domain.repository.BffFacilityLocationProjection;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class BffFacilityLocationMapperImpl implements BffFacilityLocationMapper {

    @Override
    public BffFacilityLocationDto toBffFacilityLocationDto(BffFacilityLocationProjection bffFacilityLocationProjection) {
        if ( bffFacilityLocationProjection == null ) {
            return null;
        }

        BffFacilityLocationDto bffFacilityLocationDto = new BffFacilityLocationDto();

        bffFacilityLocationDto.setLocationSeqId( bffFacilityLocationProjection.getLocationSeqId() );
        bffFacilityLocationDto.setLocationTypeEnumId( bffFacilityLocationProjection.getLocationTypeEnumId() );
        bffFacilityLocationDto.setAreaId( bffFacilityLocationProjection.getAreaId() );
        bffFacilityLocationDto.setAisleId( bffFacilityLocationProjection.getAisleId() );
        bffFacilityLocationDto.setSectionId( bffFacilityLocationProjection.getSectionId() );
        bffFacilityLocationDto.setLevelId( bffFacilityLocationProjection.getLevelId() );
        bffFacilityLocationDto.setPositionId( bffFacilityLocationProjection.getPositionId() );
        bffFacilityLocationDto.setGeoPointId( bffFacilityLocationProjection.getGeoPointId() );
        bffFacilityLocationDto.setActive( bffFacilityLocationProjection.getActive() );

        return bffFacilityLocationDto;
    }

    @Override
    public BffFacilityLocationDto toBffFacilityLocationDto(FacilityLocationState facilityLocationState) {
        if ( facilityLocationState == null ) {
            return null;
        }

        BffFacilityLocationDto bffFacilityLocationDto = new BffFacilityLocationDto();

        bffFacilityLocationDto.setLocationTypeEnumId( facilityLocationState.getLocationTypeEnumId() );
        bffFacilityLocationDto.setAreaId( facilityLocationState.getAreaId() );
        bffFacilityLocationDto.setAisleId( facilityLocationState.getAisleId() );
        bffFacilityLocationDto.setSectionId( facilityLocationState.getSectionId() );
        bffFacilityLocationDto.setLevelId( facilityLocationState.getLevelId() );
        bffFacilityLocationDto.setPositionId( facilityLocationState.getPositionId() );
        bffFacilityLocationDto.setGeoPointId( facilityLocationState.getGeoPointId() );
        bffFacilityLocationDto.setActive( facilityLocationState.getActive() );

        bffFacilityLocationDto.setLocationSeqId( facilityLocationState.getFacilityLocationId().getLocationSeqId() );

        return bffFacilityLocationDto;
    }
}
