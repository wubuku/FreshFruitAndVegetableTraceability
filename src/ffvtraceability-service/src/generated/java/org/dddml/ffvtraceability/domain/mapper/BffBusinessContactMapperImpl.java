package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffBusinessContactDto;
import org.dddml.ffvtraceability.domain.repository.BffFacilityContactMechRepository;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class BffBusinessContactMapperImpl implements BffBusinessContactMapper {

    @Override
    public BffBusinessContactDto toBffBusinessContactDto(BffFacilityContactMechRepository.BffBusinessContactProjectionExt projection) {
        if ( projection == null ) {
            return null;
        }

        BffBusinessContactDto bffBusinessContactDto = new BffBusinessContactDto();

        bffBusinessContactDto.setBusinessName( projection.getBusinessName() );
        bffBusinessContactDto.setPhoneNumber( projection.getPhoneNumber() );
        bffBusinessContactDto.setPhysicalLocationAddress( projection.getPhysicalLocationAddress() );
        bffBusinessContactDto.setCity( projection.getCity() );
        bffBusinessContactDto.setState( projection.getState() );
        bffBusinessContactDto.setZipCode( projection.getZipCode() );
        bffBusinessContactDto.setCountry( projection.getCountry() );
        bffBusinessContactDto.setStateProvinceGeoId( projection.getStateProvinceGeoId() );
        bffBusinessContactDto.setCountryGeoId( projection.getCountryGeoId() );
        bffBusinessContactDto.setEmail( projection.getEmail() );
        bffBusinessContactDto.setContactRole( projection.getContactRole() );

        return bffBusinessContactDto;
    }
}
