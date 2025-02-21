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

        bffFacilityDto.setActive( bffFacilityProjection.getActive() );
        bffFacilityDto.setDescription( bffFacilityProjection.getDescription() );
        bffFacilityDto.setFacilityId( bffFacilityProjection.getFacilityId() );
        bffFacilityDto.setFacilityLevel( bffFacilityProjection.getFacilityLevel() );
        bffFacilityDto.setFacilityName( bffFacilityProjection.getFacilityName() );
        bffFacilityDto.setFacilitySize( bffFacilityProjection.getFacilitySize() );
        bffFacilityDto.setFacilitySizeUomId( bffFacilityProjection.getFacilitySizeUomId() );
        bffFacilityDto.setFacilityTypeId( bffFacilityProjection.getFacilityTypeId() );
        bffFacilityDto.setFfrn( bffFacilityProjection.getFfrn() );
        bffFacilityDto.setGeoId( bffFacilityProjection.getGeoId() );
        bffFacilityDto.setGeoPointId( bffFacilityProjection.getGeoPointId() );
        bffFacilityDto.setGln( bffFacilityProjection.getGln() );
        bffFacilityDto.setInternalId( bffFacilityProjection.getInternalId() );
        bffFacilityDto.setOwnerPartyId( bffFacilityProjection.getOwnerPartyId() );
        bffFacilityDto.setParentFacilityId( bffFacilityProjection.getParentFacilityId() );
        bffFacilityDto.setSequenceNumber( bffFacilityProjection.getSequenceNumber() );

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
        bffFacilityDto.setFacilityLevel( facilityState.getFacilityLevel() );
        bffFacilityDto.setFacilityName( facilityState.getFacilityName() );
        bffFacilityDto.setFacilitySize( facilityState.getFacilitySize() );
        bffFacilityDto.setFacilitySizeUomId( facilityState.getFacilitySizeUomId() );
        bffFacilityDto.setFacilityTypeId( facilityState.getFacilityTypeId() );
        bffFacilityDto.setGeoId( facilityState.getGeoId() );
        bffFacilityDto.setGeoPointId( facilityState.getGeoPointId() );
        bffFacilityDto.setOwnerPartyId( facilityState.getOwnerPartyId() );
        bffFacilityDto.setParentFacilityId( facilityState.getParentFacilityId() );
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
        simpleCreateFacility.setActive( bffFacilityDto.getActive() );
        simpleCreateFacility.setDescription( bffFacilityDto.getDescription() );
        simpleCreateFacility.setFacilityLevel( bffFacilityDto.getFacilityLevel() );
        simpleCreateFacility.setFacilityName( bffFacilityDto.getFacilityName() );
        simpleCreateFacility.setFacilitySize( bffFacilityDto.getFacilitySize() );
        simpleCreateFacility.setFacilitySizeUomId( bffFacilityDto.getFacilitySizeUomId() );
        simpleCreateFacility.setFacilityTypeId( bffFacilityDto.getFacilityTypeId() );
        simpleCreateFacility.setGeoId( bffFacilityDto.getGeoId() );
        simpleCreateFacility.setGeoPointId( bffFacilityDto.getGeoPointId() );
        simpleCreateFacility.setOwnerPartyId( bffFacilityDto.getOwnerPartyId() );
        simpleCreateFacility.setParentFacilityId( bffFacilityDto.getParentFacilityId() );
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
        simpleMergePatchFacility.setActive( bffFacilityDto.getActive() );
        simpleMergePatchFacility.setDescription( bffFacilityDto.getDescription() );
        simpleMergePatchFacility.setFacilityLevel( bffFacilityDto.getFacilityLevel() );
        simpleMergePatchFacility.setFacilityName( bffFacilityDto.getFacilityName() );
        simpleMergePatchFacility.setFacilitySize( bffFacilityDto.getFacilitySize() );
        simpleMergePatchFacility.setFacilitySizeUomId( bffFacilityDto.getFacilitySizeUomId() );
        simpleMergePatchFacility.setFacilityTypeId( bffFacilityDto.getFacilityTypeId() );
        simpleMergePatchFacility.setGeoId( bffFacilityDto.getGeoId() );
        simpleMergePatchFacility.setGeoPointId( bffFacilityDto.getGeoPointId() );
        simpleMergePatchFacility.setOwnerPartyId( bffFacilityDto.getOwnerPartyId() );
        simpleMergePatchFacility.setParentFacilityId( bffFacilityDto.getParentFacilityId() );
        simpleMergePatchFacility.setSequenceNumber( bffFacilityDto.getSequenceNumber() );

        return simpleMergePatchFacility;
    }
}
