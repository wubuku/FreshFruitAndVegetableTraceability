// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.contactmech;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.contactmech.PostalAddressEvent.*;

public abstract class AbstractPostalAddressState extends AbstractContactMechState implements PostalAddressState.SqlPostalAddressState {

    private String toName;

    public String getToName() {
        return this.toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    private String attnName;

    public String getAttnName() {
        return this.attnName;
    }

    public void setAttnName(String attnName) {
        this.attnName = attnName;
    }

    private String address1;

    public String getAddress1() {
        return this.address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    private String address2;

    public String getAddress2() {
        return this.address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    private String directions;

    public String getDirections() {
        return this.directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    private String city;

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private String postalCode;

    public String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    private String postalCodeExt;

    public String getPostalCodeExt() {
        return this.postalCodeExt;
    }

    public void setPostalCodeExt(String postalCodeExt) {
        this.postalCodeExt = postalCodeExt;
    }

    private String countryGeoId;

    public String getCountryGeoId() {
        return this.countryGeoId;
    }

    public void setCountryGeoId(String countryGeoId) {
        this.countryGeoId = countryGeoId;
    }

    private String stateProvinceGeoId;

    public String getStateProvinceGeoId() {
        return this.stateProvinceGeoId;
    }

    public void setStateProvinceGeoId(String stateProvinceGeoId) {
        this.stateProvinceGeoId = stateProvinceGeoId;
    }

    private String prefectureGeoId;

    public String getPrefectureGeoId() {
        return this.prefectureGeoId;
    }

    public void setPrefectureGeoId(String prefectureGeoId) {
        this.prefectureGeoId = prefectureGeoId;
    }

    private String countyGeoId;

    public String getCountyGeoId() {
        return this.countyGeoId;
    }

    public void setCountyGeoId(String countyGeoId) {
        this.countyGeoId = countyGeoId;
    }

    private String townGeoId;

    public String getTownGeoId() {
        return this.townGeoId;
    }

    public void setTownGeoId(String townGeoId) {
        this.townGeoId = townGeoId;
    }

    private String assocTelecomContactMechId;

    public String getAssocTelecomContactMechId() {
        return this.assocTelecomContactMechId;
    }

    public void setAssocTelecomContactMechId(String assocTelecomContactMechId) {
        this.assocTelecomContactMechId = assocTelecomContactMechId;
    }

    private String postalCodeGeoId;

    public String getPostalCodeGeoId() {
        return this.postalCodeGeoId;
    }

    public void setPostalCodeGeoId(String postalCodeGeoId) {
        this.postalCodeGeoId = postalCodeGeoId;
    }

    private String geoPointId;

    public String getGeoPointId() {
        return this.geoPointId;
    }

    public void setGeoPointId(String geoPointId) {
        this.geoPointId = geoPointId;
    }

    public AbstractPostalAddressState(List<Event> events) {
        initializeForReapplying();
        if (events != null && events.size() > 0) {
            this.setContactMechId(((ContactMechEvent.SqlContactMechEvent) events.get(0)).getContactMechEventId().getContactMechId());
            for (Event e : events) {
                mutate(e);
                this.setVersion((this.getVersion() == null ? ContactMechState.VERSION_NULL : this.getVersion()) + 1);
            }
        }
    }


    public AbstractPostalAddressState() {
        initializeProperties();
    }

    protected void initializeForReapplying() {
        super.setForReapplying(true);

        initializeProperties();
    }
    
    protected void initializeProperties() {
        super.initializeProperties();
    }

    public void mutate(Event e) {
        setStateReadOnly(false);
        if (e instanceof PostalAddressStateCreated) {
            when((PostalAddressStateCreated) e);
        } else if (e instanceof PostalAddressStateMergePatched) {
            when((PostalAddressStateMergePatched) e);
        } else {
            throw new UnsupportedOperationException(String.format("Unsupported event type: %1$s", e.getClass().getName()));
        }
    }

    public void when(PostalAddressStateCreated e) {
        throwOnWrongEvent(e);

        this.setContactMechTypeId(e.getContactMechTypeId());
        this.setInfoString(e.getInfoString());
        this.setToName(e.getToName());
        this.setAttnName(e.getAttnName());
        this.setAddress1(e.getAddress1());
        this.setAddress2(e.getAddress2());
        this.setDirections(e.getDirections());
        this.setCity(e.getCity());
        this.setPostalCode(e.getPostalCode());
        this.setPostalCodeExt(e.getPostalCodeExt());
        this.setCountryGeoId(e.getCountryGeoId());
        this.setStateProvinceGeoId(e.getStateProvinceGeoId());
        this.setPrefectureGeoId(e.getPrefectureGeoId());
        this.setCountyGeoId(e.getCountyGeoId());
        this.setTownGeoId(e.getTownGeoId());
        this.setAssocTelecomContactMechId(e.getAssocTelecomContactMechId());
        this.setPostalCodeGeoId(e.getPostalCodeGeoId());
        this.setGeoPointId(e.getGeoPointId());

        this.setCreatedBy(e.getCreatedBy());
        this.setCreatedAt(e.getCreatedAt());

    }

    @Override
    public void merge(ContactMechState s) {
        merge((PostalAddressState)s);
    }

    public void merge(PostalAddressState s) {
        if (s == this) {
            return;
        }
        this.setContactMechTypeId(s.getContactMechTypeId());
        this.setInfoString(s.getInfoString());
        this.setToName(s.getToName());
        this.setAttnName(s.getAttnName());
        this.setAddress1(s.getAddress1());
        this.setAddress2(s.getAddress2());
        this.setDirections(s.getDirections());
        this.setCity(s.getCity());
        this.setPostalCode(s.getPostalCode());
        this.setPostalCodeExt(s.getPostalCodeExt());
        this.setCountryGeoId(s.getCountryGeoId());
        this.setStateProvinceGeoId(s.getStateProvinceGeoId());
        this.setPrefectureGeoId(s.getPrefectureGeoId());
        this.setCountyGeoId(s.getCountyGeoId());
        this.setTownGeoId(s.getTownGeoId());
        this.setAssocTelecomContactMechId(s.getAssocTelecomContactMechId());
        this.setPostalCodeGeoId(s.getPostalCodeGeoId());
        this.setGeoPointId(s.getGeoPointId());
    }

    public void when(PostalAddressStateMergePatched e) {
        throwOnWrongEvent(e);

        if (e.getContactMechTypeId() == null) {
            if (e.getIsPropertyContactMechTypeIdRemoved() != null && e.getIsPropertyContactMechTypeIdRemoved()) {
                this.setContactMechTypeId(null);
            }
        } else {
            this.setContactMechTypeId(e.getContactMechTypeId());
        }
        if (e.getInfoString() == null) {
            if (e.getIsPropertyInfoStringRemoved() != null && e.getIsPropertyInfoStringRemoved()) {
                this.setInfoString(null);
            }
        } else {
            this.setInfoString(e.getInfoString());
        }
        if (e.getToName() == null) {
            if (e.getIsPropertyToNameRemoved() != null && e.getIsPropertyToNameRemoved()) {
                this.setToName(null);
            }
        } else {
            this.setToName(e.getToName());
        }
        if (e.getAttnName() == null) {
            if (e.getIsPropertyAttnNameRemoved() != null && e.getIsPropertyAttnNameRemoved()) {
                this.setAttnName(null);
            }
        } else {
            this.setAttnName(e.getAttnName());
        }
        if (e.getAddress1() == null) {
            if (e.getIsPropertyAddress1Removed() != null && e.getIsPropertyAddress1Removed()) {
                this.setAddress1(null);
            }
        } else {
            this.setAddress1(e.getAddress1());
        }
        if (e.getAddress2() == null) {
            if (e.getIsPropertyAddress2Removed() != null && e.getIsPropertyAddress2Removed()) {
                this.setAddress2(null);
            }
        } else {
            this.setAddress2(e.getAddress2());
        }
        if (e.getDirections() == null) {
            if (e.getIsPropertyDirectionsRemoved() != null && e.getIsPropertyDirectionsRemoved()) {
                this.setDirections(null);
            }
        } else {
            this.setDirections(e.getDirections());
        }
        if (e.getCity() == null) {
            if (e.getIsPropertyCityRemoved() != null && e.getIsPropertyCityRemoved()) {
                this.setCity(null);
            }
        } else {
            this.setCity(e.getCity());
        }
        if (e.getPostalCode() == null) {
            if (e.getIsPropertyPostalCodeRemoved() != null && e.getIsPropertyPostalCodeRemoved()) {
                this.setPostalCode(null);
            }
        } else {
            this.setPostalCode(e.getPostalCode());
        }
        if (e.getPostalCodeExt() == null) {
            if (e.getIsPropertyPostalCodeExtRemoved() != null && e.getIsPropertyPostalCodeExtRemoved()) {
                this.setPostalCodeExt(null);
            }
        } else {
            this.setPostalCodeExt(e.getPostalCodeExt());
        }
        if (e.getCountryGeoId() == null) {
            if (e.getIsPropertyCountryGeoIdRemoved() != null && e.getIsPropertyCountryGeoIdRemoved()) {
                this.setCountryGeoId(null);
            }
        } else {
            this.setCountryGeoId(e.getCountryGeoId());
        }
        if (e.getStateProvinceGeoId() == null) {
            if (e.getIsPropertyStateProvinceGeoIdRemoved() != null && e.getIsPropertyStateProvinceGeoIdRemoved()) {
                this.setStateProvinceGeoId(null);
            }
        } else {
            this.setStateProvinceGeoId(e.getStateProvinceGeoId());
        }
        if (e.getPrefectureGeoId() == null) {
            if (e.getIsPropertyPrefectureGeoIdRemoved() != null && e.getIsPropertyPrefectureGeoIdRemoved()) {
                this.setPrefectureGeoId(null);
            }
        } else {
            this.setPrefectureGeoId(e.getPrefectureGeoId());
        }
        if (e.getCountyGeoId() == null) {
            if (e.getIsPropertyCountyGeoIdRemoved() != null && e.getIsPropertyCountyGeoIdRemoved()) {
                this.setCountyGeoId(null);
            }
        } else {
            this.setCountyGeoId(e.getCountyGeoId());
        }
        if (e.getTownGeoId() == null) {
            if (e.getIsPropertyTownGeoIdRemoved() != null && e.getIsPropertyTownGeoIdRemoved()) {
                this.setTownGeoId(null);
            }
        } else {
            this.setTownGeoId(e.getTownGeoId());
        }
        if (e.getAssocTelecomContactMechId() == null) {
            if (e.getIsPropertyAssocTelecomContactMechIdRemoved() != null && e.getIsPropertyAssocTelecomContactMechIdRemoved()) {
                this.setAssocTelecomContactMechId(null);
            }
        } else {
            this.setAssocTelecomContactMechId(e.getAssocTelecomContactMechId());
        }
        if (e.getPostalCodeGeoId() == null) {
            if (e.getIsPropertyPostalCodeGeoIdRemoved() != null && e.getIsPropertyPostalCodeGeoIdRemoved()) {
                this.setPostalCodeGeoId(null);
            }
        } else {
            this.setPostalCodeGeoId(e.getPostalCodeGeoId());
        }
        if (e.getGeoPointId() == null) {
            if (e.getIsPropertyGeoPointIdRemoved() != null && e.getIsPropertyGeoPointIdRemoved()) {
                this.setGeoPointId(null);
            }
        } else {
            this.setGeoPointId(e.getGeoPointId());
        }

        this.setUpdatedBy(e.getCreatedBy());
        this.setUpdatedAt(e.getCreatedAt());

    }

    public void save() {
       super.save();
    }


    public static class SimplePostalAddressState extends AbstractPostalAddressState {

        public SimplePostalAddressState() {
        }

        public SimplePostalAddressState(List<Event> events) {
            super(events);
        }

        public static SimplePostalAddressState newForReapplying() {
            SimplePostalAddressState s = new SimplePostalAddressState();
            s.initializeForReapplying();
            return s;
        }

    }



}
