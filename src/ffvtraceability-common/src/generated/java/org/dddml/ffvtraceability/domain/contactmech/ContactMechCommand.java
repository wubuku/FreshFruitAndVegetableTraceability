// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.contactmech;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.specialization.DomainError;

public interface ContactMechCommand extends Command {

    String getContactMechId();

    void setContactMechId(String contactMechId);

    Long getVersion();

    void setVersion(Long version);

    static void throwOnInvalidStateTransition(ContactMechState state, Command c) {
        if (state.getVersion() == null) {
            if (isCreationCommand((ContactMechCommand)c)) {
                return;
            }
            throw DomainError.named("premature", "Can't do anything to unexistent aggregate");
        }
        if (isCreationCommand((ContactMechCommand)c))
            throw DomainError.named("rebirth", "Can't create aggregate that already exists");
    }

    static boolean isCreationCommand(ContactMechCommand c) {
        if ((c instanceof ContactMechCommand.CreateContactMech) 
            && (COMMAND_TYPE_CREATE.equals(c.getCommandType()) || c.getVersion().equals(ContactMechState.VERSION_NULL)))
            return true;
        if ((c instanceof ContactMechCommand.MergePatchContactMech))
            return false;
        if (c.getCommandType() != null) {
            String commandType = c.getCommandType();
        }

        if (c.getVersion().equals(ContactMechState.VERSION_NULL))
            return true;
        return false;
    }

    interface CreateOrMergePatchContactMech extends ContactMechCommand {
        String getContactMechTypeId();

        void setContactMechTypeId(String contactMechTypeId);

        String getInfoString();

        void setInfoString(String infoString);

        String getToName();

        void setToName(String toName);

        String getAttnName();

        void setAttnName(String attnName);

        String getAddress1();

        void setAddress1(String address1);

        String getAddress2();

        void setAddress2(String address2);

        String getDirections();

        void setDirections(String directions);

        String getCity();

        void setCity(String city);

        String getPostalCode();

        void setPostalCode(String postalCode);

        String getPostalCodeExt();

        void setPostalCodeExt(String postalCodeExt);

        String getCountryGeoId();

        void setCountryGeoId(String countryGeoId);

        String getStateProvinceGeoId();

        void setStateProvinceGeoId(String stateProvinceGeoId);

        String getPrefectureGeoId();

        void setPrefectureGeoId(String prefectureGeoId);

        String getCountyGeoId();

        void setCountyGeoId(String countyGeoId);

        String getTownGeoId();

        void setTownGeoId(String townGeoId);

        String getAssocTelecomContactMechId();

        void setAssocTelecomContactMechId(String assocTelecomContactMechId);

        String getPostalCodeGeoId();

        void setPostalCodeGeoId(String postalCodeGeoId);

        String getGeoPointId();

        void setGeoPointId(String geoPointId);

        String getCountryCode();

        void setCountryCode(String countryCode);

        String getAreaCode();

        void setAreaCode(String areaCode);

        String getContactNumber();

        void setContactNumber(String contactNumber);

        String getAskForName();

        void setAskForName(String askForName);

    }

    interface CreateContactMech extends CreateOrMergePatchContactMech {
    }

    interface MergePatchContactMech extends CreateOrMergePatchContactMech {
        Boolean getIsPropertyContactMechTypeIdRemoved();

        void setIsPropertyContactMechTypeIdRemoved(Boolean removed);

        Boolean getIsPropertyInfoStringRemoved();

        void setIsPropertyInfoStringRemoved(Boolean removed);

        Boolean getIsPropertyToNameRemoved();

        void setIsPropertyToNameRemoved(Boolean removed);

        Boolean getIsPropertyAttnNameRemoved();

        void setIsPropertyAttnNameRemoved(Boolean removed);

        Boolean getIsPropertyAddress1Removed();

        void setIsPropertyAddress1Removed(Boolean removed);

        Boolean getIsPropertyAddress2Removed();

        void setIsPropertyAddress2Removed(Boolean removed);

        Boolean getIsPropertyDirectionsRemoved();

        void setIsPropertyDirectionsRemoved(Boolean removed);

        Boolean getIsPropertyCityRemoved();

        void setIsPropertyCityRemoved(Boolean removed);

        Boolean getIsPropertyPostalCodeRemoved();

        void setIsPropertyPostalCodeRemoved(Boolean removed);

        Boolean getIsPropertyPostalCodeExtRemoved();

        void setIsPropertyPostalCodeExtRemoved(Boolean removed);

        Boolean getIsPropertyCountryGeoIdRemoved();

        void setIsPropertyCountryGeoIdRemoved(Boolean removed);

        Boolean getIsPropertyStateProvinceGeoIdRemoved();

        void setIsPropertyStateProvinceGeoIdRemoved(Boolean removed);

        Boolean getIsPropertyPrefectureGeoIdRemoved();

        void setIsPropertyPrefectureGeoIdRemoved(Boolean removed);

        Boolean getIsPropertyCountyGeoIdRemoved();

        void setIsPropertyCountyGeoIdRemoved(Boolean removed);

        Boolean getIsPropertyTownGeoIdRemoved();

        void setIsPropertyTownGeoIdRemoved(Boolean removed);

        Boolean getIsPropertyAssocTelecomContactMechIdRemoved();

        void setIsPropertyAssocTelecomContactMechIdRemoved(Boolean removed);

        Boolean getIsPropertyPostalCodeGeoIdRemoved();

        void setIsPropertyPostalCodeGeoIdRemoved(Boolean removed);

        Boolean getIsPropertyGeoPointIdRemoved();

        void setIsPropertyGeoPointIdRemoved(Boolean removed);

        Boolean getIsPropertyCountryCodeRemoved();

        void setIsPropertyCountryCodeRemoved(Boolean removed);

        Boolean getIsPropertyAreaCodeRemoved();

        void setIsPropertyAreaCodeRemoved(Boolean removed);

        Boolean getIsPropertyContactNumberRemoved();

        void setIsPropertyContactNumberRemoved(Boolean removed);

        Boolean getIsPropertyAskForNameRemoved();

        void setIsPropertyAskForNameRemoved(Boolean removed);


    }

    interface DeleteContactMech extends ContactMechCommand {

        String getContactMechTypeId();

        void setContactMechTypeId(String contactMechTypeId);
    }

}
