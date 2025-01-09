package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffFacilityDto;
import org.dddml.ffvtraceability.domain.facility.FacilityState;
import org.dddml.ffvtraceability.domain.repository.BffFacilityProjection;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class BffFacilityMapperImpl implements BffFacilityMapper {

    @Override
    public BffFacilityDto toBffFacilityDto(BffFacilityProjection bffFacilityProjection) {
        if ( bffFacilityProjection == null ) {
            return null;
        }

        BffFacilityDto bffFacilityDto = new BffFacilityDto();

        bffFacilityDto.setActive( bffFacilityProjection.getActive() );
        bffFacilityDto.setDescription( bffFacilityProjection.getDescription() );
        bffFacilityDto.setFacilityId( bffFacilityProjection.getFacilityId() );
        bffFacilityDto.setFacilityName( bffFacilityProjection.getFacilityName() );
        bffFacilityDto.setFacilitySize( bffFacilityProjection.getFacilitySize() );
        bffFacilityDto.setFacilitySizeUomId( bffFacilityProjection.getFacilitySizeUomId() );
        bffFacilityDto.setFacilityTypeId( bffFacilityProjection.getFacilityTypeId() );
        bffFacilityDto.setFfrn( bffFacilityProjection.getFfrn() );
        bffFacilityDto.setGeoId( bffFacilityProjection.getGeoId() );
        bffFacilityDto.setGeoPointId( bffFacilityProjection.getGeoPointId() );
        bffFacilityDto.setGln( bffFacilityProjection.getGln() );
        bffFacilityDto.setOwnerPartyId( bffFacilityProjection.getOwnerPartyId() );
        bffFacilityDto.setParentFacilityId( bffFacilityProjection.getParentFacilityId() );

        return bffFacilityDto;
    }

    @Override
    public BffFacilityDto toBffFacilityDto(FacilityState facilityState) {
        if ( facilityState == null ) {
            return null;
        }

        BffFacilityDto bffFacilityDto = new BffFacilityDto();

        bffFacilityDto.setActive( facilityState.getActive() );
        bffFacilityDto.setDescription( facilityState.getDescription() );
        bffFacilityDto.setFacilityId( facilityState.getFacilityId() );
        bffFacilityDto.setFacilityName( facilityState.getFacilityName() );
        bffFacilityDto.setFacilitySize( facilityState.getFacilitySize() );
        bffFacilityDto.setFacilitySizeUomId( facilityState.getFacilitySizeUomId() );
        bffFacilityDto.setFacilityTypeId( facilityState.getFacilityTypeId() );
        bffFacilityDto.setGeoId( facilityState.getGeoId() );
        bffFacilityDto.setGeoPointId( facilityState.getGeoPointId() );
        bffFacilityDto.setOwnerPartyId( facilityState.getOwnerPartyId() );
        bffFacilityDto.setParentFacilityId( facilityState.getParentFacilityId() );

        return bffFacilityDto;
    }
}
