package org.dddml.ffvtraceability.domain.mapper;

import javax.annotation.processing.Generated;
import org.dddml.ffvtraceability.domain.BffBusinessContactDto;
import org.dddml.ffvtraceability.domain.repository.BffBusinessContactProjection;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class BffBusinessContactMapperImpl implements BffBusinessContactMapper {

    @Override
    public BffBusinessContactDto toBffBusinessContactDto(BffBusinessContactProjection projection) {
        if ( projection == null ) {
            return null;
        }

        BffBusinessContactDto bffBusinessContactDto = new BffBusinessContactDto();

        bffBusinessContactDto.setBusinessName( projection.getBusinessName() );
        bffBusinessContactDto.setCity( projection.getCity() );
        bffBusinessContactDto.setContactRole( projection.getContactRole() );
        bffBusinessContactDto.setCountry( projection.getCountry() );
        bffBusinessContactDto.setCountryGeoId( projection.getCountryGeoId() );
        bffBusinessContactDto.setEmail( projection.getEmail() );
        bffBusinessContactDto.setPhoneNumber( projection.getPhoneNumber() );
        bffBusinessContactDto.setPhysicalLocationAddress( projection.getPhysicalLocationAddress() );
        bffBusinessContactDto.setState( projection.getState() );
        bffBusinessContactDto.setStateProvinceGeoId( projection.getStateProvinceGeoId() );
        bffBusinessContactDto.setZipCode( projection.getZipCode() );

        return bffBusinessContactDto;
    }
}
