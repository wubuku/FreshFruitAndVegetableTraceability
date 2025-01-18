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

        bffFacilityLocationDto.setActive( bffFacilityLocationProjection.getActive() );
        bffFacilityLocationDto.setAisleId( bffFacilityLocationProjection.getAisleId() );
        bffFacilityLocationDto.setAreaId( bffFacilityLocationProjection.getAreaId() );
        bffFacilityLocationDto.setDescription( bffFacilityLocationProjection.getDescription() );
        bffFacilityLocationDto.setFacilityId( bffFacilityLocationProjection.getFacilityId() );
        bffFacilityLocationDto.setGeoPointId( bffFacilityLocationProjection.getGeoPointId() );
        bffFacilityLocationDto.setGln( bffFacilityLocationProjection.getGln() );
        bffFacilityLocationDto.setLevelId( bffFacilityLocationProjection.getLevelId() );
        bffFacilityLocationDto.setLocationCode( bffFacilityLocationProjection.getLocationCode() );
        bffFacilityLocationDto.setLocationName( bffFacilityLocationProjection.getLocationName() );
        bffFacilityLocationDto.setLocationSeqId( bffFacilityLocationProjection.getLocationSeqId() );
        bffFacilityLocationDto.setLocationTypeEnumId( bffFacilityLocationProjection.getLocationTypeEnumId() );
        bffFacilityLocationDto.setPositionId( bffFacilityLocationProjection.getPositionId() );
        bffFacilityLocationDto.setSectionId( bffFacilityLocationProjection.getSectionId() );

        return bffFacilityLocationDto;
    }

    @Override
    public BffFacilityLocationDto toBffFacilityLocationDto(FacilityLocationState facilityLocationState) {
        if ( facilityLocationState == null ) {
            return null;
        }

        BffFacilityLocationDto bffFacilityLocationDto = new BffFacilityLocationDto();

        bffFacilityLocationDto.setActive( facilityLocationState.getActive() );
        bffFacilityLocationDto.setAisleId( facilityLocationState.getAisleId() );
        bffFacilityLocationDto.setAreaId( facilityLocationState.getAreaId() );
        bffFacilityLocationDto.setDescription( facilityLocationState.getDescription() );
        bffFacilityLocationDto.setGeoPointId( facilityLocationState.getGeoPointId() );
        bffFacilityLocationDto.setGln( facilityLocationState.getGln() );
        bffFacilityLocationDto.setLevelId( facilityLocationState.getLevelId() );
        bffFacilityLocationDto.setLocationCode( facilityLocationState.getLocationCode() );
        bffFacilityLocationDto.setLocationName( facilityLocationState.getLocationName() );
        bffFacilityLocationDto.setLocationTypeEnumId( facilityLocationState.getLocationTypeEnumId() );
        bffFacilityLocationDto.setPositionId( facilityLocationState.getPositionId() );
        bffFacilityLocationDto.setSectionId( facilityLocationState.getSectionId() );

        bffFacilityLocationDto.setLocationSeqId( facilityLocationState.getFacilityLocationId().getLocationSeqId() );

        return bffFacilityLocationDto;
    }
}
