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

        bffFacilityDto.setFacilityId( bffFacilityProjection.getFacilityId() );
        bffFacilityDto.setFacilityTypeId( bffFacilityProjection.getFacilityTypeId() );
        bffFacilityDto.setParentFacilityId( bffFacilityProjection.getParentFacilityId() );
        bffFacilityDto.setOwnerPartyId( bffFacilityProjection.getOwnerPartyId() );
        bffFacilityDto.setFacilityName( bffFacilityProjection.getFacilityName() );
        bffFacilityDto.setFacilitySize( bffFacilityProjection.getFacilitySize() );
        bffFacilityDto.setFacilitySizeUomId( bffFacilityProjection.getFacilitySizeUomId() );
        bffFacilityDto.setDescription( bffFacilityProjection.getDescription() );
        bffFacilityDto.setGeoPointId( bffFacilityProjection.getGeoPointId() );
        bffFacilityDto.setGeoId( bffFacilityProjection.getGeoId() );
        bffFacilityDto.setActive( bffFacilityProjection.getActive() );
        bffFacilityDto.setGln( bffFacilityProjection.getGln() );
        bffFacilityDto.setFfrn( bffFacilityProjection.getFfrn() );

        return bffFacilityDto;
    }

    @Override
    public BffFacilityDto toBffFacilityDto(FacilityState facilityState) {
        if ( facilityState == null ) {
            return null;
        }

        BffFacilityDto bffFacilityDto = new BffFacilityDto();

        bffFacilityDto.setFacilityId( facilityState.getFacilityId() );
        bffFacilityDto.setFacilityTypeId( facilityState.getFacilityTypeId() );
        bffFacilityDto.setParentFacilityId( facilityState.getParentFacilityId() );
        bffFacilityDto.setOwnerPartyId( facilityState.getOwnerPartyId() );
        bffFacilityDto.setFacilityName( facilityState.getFacilityName() );
        bffFacilityDto.setFacilitySize( facilityState.getFacilitySize() );
        bffFacilityDto.setFacilitySizeUomId( facilityState.getFacilitySizeUomId() );
        bffFacilityDto.setDescription( facilityState.getDescription() );
        bffFacilityDto.setGeoPointId( facilityState.getGeoPointId() );
        bffFacilityDto.setGeoId( facilityState.getGeoId() );
        bffFacilityDto.setActive( facilityState.getActive() );

        return bffFacilityDto;
    }
}
