// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.contactmech;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.AbstractEvent;

public abstract class AbstractTelecomNumberEvent extends AbstractContactMechEvent implements ContactMechEvent {
    protected AbstractTelecomNumberEvent() {
    }

    protected AbstractTelecomNumberEvent(ContactMechEventId eventId) {
        super(eventId);
    }

    public static class ContactMechLobEvent extends AbstractContactMechEvent {

        public Map<String, Object> getDynamicProperties() {
            return dynamicProperties;
        }

        public void setDynamicProperties(Map<String, Object> dynamicProperties) {
            if (dynamicProperties == null) {
                throw new IllegalArgumentException("dynamicProperties is null.");
            }
            this.dynamicProperties = dynamicProperties;
        }

        private Map<String, Object> dynamicProperties = new HashMap<>();

        @Override
        public String getEventType() {
            return "ContactMechLobEvent";
        }

    }


    public static abstract class AbstractTelecomNumberStateEvent extends AbstractContactMechStateEvent implements TelecomNumberEvent.TelecomNumberStateEvent {
        private String countryCode;

        public String getCountryCode()
        {
            return this.countryCode;
        }

        public void setCountryCode(String countryCode)
        {
            this.countryCode = countryCode;
        }

        private String areaCode;

        public String getAreaCode()
        {
            return this.areaCode;
        }

        public void setAreaCode(String areaCode)
        {
            this.areaCode = areaCode;
        }

        private String contactNumber;

        public String getContactNumber()
        {
            return this.contactNumber;
        }

        public void setContactNumber(String contactNumber)
        {
            this.contactNumber = contactNumber;
        }

        protected AbstractTelecomNumberStateEvent(ContactMechEventId eventId) {
            super(eventId);
        }
    }

    public static abstract class AbstractTelecomNumberStateCreated extends AbstractTelecomNumberStateEvent implements TelecomNumberEvent.TelecomNumberStateCreated
    {
        public AbstractTelecomNumberStateCreated() {
            this(new ContactMechEventId());
        }

        public AbstractTelecomNumberStateCreated(ContactMechEventId eventId) {
            super(eventId);
        }

        public String getEventType() {
            return StateEventType.CREATED;
        }

    }


    public static abstract class AbstractTelecomNumberStateMergePatched extends AbstractTelecomNumberStateEvent implements TelecomNumberEvent.TelecomNumberStateMergePatched
    {
        public AbstractTelecomNumberStateMergePatched() {
            this(new ContactMechEventId());
        }

        public AbstractTelecomNumberStateMergePatched(ContactMechEventId eventId) {
            super(eventId);
        }

        public String getEventType() {
            return StateEventType.MERGE_PATCHED;
        }

        private Boolean isPropertyCountryCodeRemoved;

        public Boolean getIsPropertyCountryCodeRemoved() {
            return this.isPropertyCountryCodeRemoved;
        }

        public void setIsPropertyCountryCodeRemoved(Boolean removed) {
            this.isPropertyCountryCodeRemoved = removed;
        }

        private Boolean isPropertyAreaCodeRemoved;

        public Boolean getIsPropertyAreaCodeRemoved() {
            return this.isPropertyAreaCodeRemoved;
        }

        public void setIsPropertyAreaCodeRemoved(Boolean removed) {
            this.isPropertyAreaCodeRemoved = removed;
        }

        private Boolean isPropertyContactNumberRemoved;

        public Boolean getIsPropertyContactNumberRemoved() {
            return this.isPropertyContactNumberRemoved;
        }

        public void setIsPropertyContactNumberRemoved(Boolean removed) {
            this.isPropertyContactNumberRemoved = removed;
        }

        private Boolean isPropertyContactMechTypeIdRemoved;

        public Boolean getIsPropertyContactMechTypeIdRemoved() {
            return this.isPropertyContactMechTypeIdRemoved;
        }

        public void setIsPropertyContactMechTypeIdRemoved(Boolean removed) {
            this.isPropertyContactMechTypeIdRemoved = removed;
        }

        private Boolean isPropertyInfoStringRemoved;

        public Boolean getIsPropertyInfoStringRemoved() {
            return this.isPropertyInfoStringRemoved;
        }

        public void setIsPropertyInfoStringRemoved(Boolean removed) {
            this.isPropertyInfoStringRemoved = removed;
        }

        private Boolean isPropertyAskForNameRemoved;

        public Boolean getIsPropertyAskForNameRemoved() {
            return this.isPropertyAskForNameRemoved;
        }

        public void setIsPropertyAskForNameRemoved(Boolean removed) {
            this.isPropertyAskForNameRemoved = removed;
        }

        private Boolean isPropertyAddress1Removed;

        public Boolean getIsPropertyAddress1Removed() {
            return this.isPropertyAddress1Removed;
        }

        public void setIsPropertyAddress1Removed(Boolean removed) {
            this.isPropertyAddress1Removed = removed;
        }

        private Boolean isPropertyAddress2Removed;

        public Boolean getIsPropertyAddress2Removed() {
            return this.isPropertyAddress2Removed;
        }

        public void setIsPropertyAddress2Removed(Boolean removed) {
            this.isPropertyAddress2Removed = removed;
        }

        private Boolean isPropertyDirectionsRemoved;

        public Boolean getIsPropertyDirectionsRemoved() {
            return this.isPropertyDirectionsRemoved;
        }

        public void setIsPropertyDirectionsRemoved(Boolean removed) {
            this.isPropertyDirectionsRemoved = removed;
        }

        private Boolean isPropertyCityRemoved;

        public Boolean getIsPropertyCityRemoved() {
            return this.isPropertyCityRemoved;
        }

        public void setIsPropertyCityRemoved(Boolean removed) {
            this.isPropertyCityRemoved = removed;
        }

        private Boolean isPropertyPostalCodeRemoved;

        public Boolean getIsPropertyPostalCodeRemoved() {
            return this.isPropertyPostalCodeRemoved;
        }

        public void setIsPropertyPostalCodeRemoved(Boolean removed) {
            this.isPropertyPostalCodeRemoved = removed;
        }

        private Boolean isPropertyPostalCodeExtRemoved;

        public Boolean getIsPropertyPostalCodeExtRemoved() {
            return this.isPropertyPostalCodeExtRemoved;
        }

        public void setIsPropertyPostalCodeExtRemoved(Boolean removed) {
            this.isPropertyPostalCodeExtRemoved = removed;
        }

        private Boolean isPropertyCountryGeoIdRemoved;

        public Boolean getIsPropertyCountryGeoIdRemoved() {
            return this.isPropertyCountryGeoIdRemoved;
        }

        public void setIsPropertyCountryGeoIdRemoved(Boolean removed) {
            this.isPropertyCountryGeoIdRemoved = removed;
        }

        private Boolean isPropertyStateProvinceGeoIdRemoved;

        public Boolean getIsPropertyStateProvinceGeoIdRemoved() {
            return this.isPropertyStateProvinceGeoIdRemoved;
        }

        public void setIsPropertyStateProvinceGeoIdRemoved(Boolean removed) {
            this.isPropertyStateProvinceGeoIdRemoved = removed;
        }

        private Boolean isPropertyPostalCodeGeoIdRemoved;

        public Boolean getIsPropertyPostalCodeGeoIdRemoved() {
            return this.isPropertyPostalCodeGeoIdRemoved;
        }

        public void setIsPropertyPostalCodeGeoIdRemoved(Boolean removed) {
            this.isPropertyPostalCodeGeoIdRemoved = removed;
        }

        private Boolean isPropertyGeoPointIdRemoved;

        public Boolean getIsPropertyGeoPointIdRemoved() {
            return this.isPropertyGeoPointIdRemoved;
        }

        public void setIsPropertyGeoPointIdRemoved(Boolean removed) {
            this.isPropertyGeoPointIdRemoved = removed;
        }


    }



    public static class SimpleTelecomNumberStateCreated extends AbstractTelecomNumberStateCreated
    {
        public SimpleTelecomNumberStateCreated() {
        }

        public SimpleTelecomNumberStateCreated(ContactMechEventId eventId) {
            super(eventId);
        }
    }

    public static class SimpleTelecomNumberStateMergePatched extends AbstractTelecomNumberStateMergePatched
    {
        public SimpleTelecomNumberStateMergePatched() {
        }

        public SimpleTelecomNumberStateMergePatched(ContactMechEventId eventId) {
            super(eventId);
        }
    }

}

