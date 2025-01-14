package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffFacilityDto;
import org.dddml.ffvtraceability.domain.facility.AbstractFacilityCommand;
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
        bffFacilityDto.setFacilityLevel( bffFacilityProjection.getFacilityLevel() );
        bffFacilityDto.setSequenceNumber( bffFacilityProjection.getSequenceNumber() );

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
        bffFacilityDto.setFacilityLevel( facilityState.getFacilityLevel() );
        bffFacilityDto.setSequenceNumber( facilityState.getSequenceNumber() );

        return bffFacilityDto;
    }

    @Override
    public AbstractFacilityCommand.SimpleCreateFacility toCreateFacility(BffFacilityDto bffFacilityDto) {
        if ( bffFacilityDto == null ) {
            return null;
        }

        AbstractFacilityCommand.SimpleCreateFacility simpleCreateFacility = new AbstractFacilityCommand.SimpleCreateFacility();

        simpleCreateFacility.setFacilityId( bffFacilityDto.getFacilityId() );
        simpleCreateFacility.setFacilityTypeId( bffFacilityDto.getFacilityTypeId() );
        simpleCreateFacility.setParentFacilityId( bffFacilityDto.getParentFacilityId() );
        simpleCreateFacility.setOwnerPartyId( bffFacilityDto.getOwnerPartyId() );
        simpleCreateFacility.setFacilityName( bffFacilityDto.getFacilityName() );
        simpleCreateFacility.setFacilitySize( bffFacilityDto.getFacilitySize() );
        simpleCreateFacility.setFacilitySizeUomId( bffFacilityDto.getFacilitySizeUomId() );
        simpleCreateFacility.setDescription( bffFacilityDto.getDescription() );
        simpleCreateFacility.setGeoPointId( bffFacilityDto.getGeoPointId() );
        simpleCreateFacility.setGeoId( bffFacilityDto.getGeoId() );
        simpleCreateFacility.setFacilityLevel( bffFacilityDto.getFacilityLevel() );
        simpleCreateFacility.setActive( bffFacilityDto.getActive() );
        simpleCreateFacility.setSequenceNumber( bffFacilityDto.getSequenceNumber() );

        return simpleCreateFacility;
    }

    @Override
    public AbstractFacilityCommand.SimpleMergePatchFacility toMergePatchFacility(BffFacilityDto bffFacilityDto) {
        if ( bffFacilityDto == null ) {
            return null;
        }

        AbstractFacilityCommand.SimpleMergePatchFacility simpleMergePatchFacility = new AbstractFacilityCommand.SimpleMergePatchFacility();

        simpleMergePatchFacility.setFacilityId( bffFacilityDto.getFacilityId() );
        simpleMergePatchFacility.setFacilityTypeId( bffFacilityDto.getFacilityTypeId() );
        simpleMergePatchFacility.setParentFacilityId( bffFacilityDto.getParentFacilityId() );
        simpleMergePatchFacility.setOwnerPartyId( bffFacilityDto.getOwnerPartyId() );
        simpleMergePatchFacility.setFacilityName( bffFacilityDto.getFacilityName() );
        simpleMergePatchFacility.setFacilitySize( bffFacilityDto.getFacilitySize() );
        simpleMergePatchFacility.setFacilitySizeUomId( bffFacilityDto.getFacilitySizeUomId() );
        simpleMergePatchFacility.setDescription( bffFacilityDto.getDescription() );
        simpleMergePatchFacility.setGeoPointId( bffFacilityDto.getGeoPointId() );
        simpleMergePatchFacility.setGeoId( bffFacilityDto.getGeoId() );
        simpleMergePatchFacility.setFacilityLevel( bffFacilityDto.getFacilityLevel() );
        simpleMergePatchFacility.setActive( bffFacilityDto.getActive() );
        simpleMergePatchFacility.setSequenceNumber( bffFacilityDto.getSequenceNumber() );

        return simpleMergePatchFacility;
    }
}
