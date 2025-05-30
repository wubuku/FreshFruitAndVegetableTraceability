// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.party;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.AbstractEvent;

public abstract class AbstractPersonEvent extends AbstractPartyEvent implements PartyEvent {
    protected AbstractPersonEvent() {
    }

    protected AbstractPersonEvent(PartyEventId eventId) {
        super(eventId);
    }

    protected PartyIdentificationEventDao getPartyIdentificationEventDao() {
        return (PartyIdentificationEventDao)ApplicationContext.current.get("partyIdentificationEventDao");
    }

    protected PartyIdentificationEventId newPartyIdentificationEventId(String partyIdentificationTypeId)
    {
        PartyIdentificationEventId eventId = new PartyIdentificationEventId(this.getPartyEventId().getPartyId(), 
            partyIdentificationTypeId, 
            this.getPartyEventId().getVersion());
        return eventId;
    }

    protected void throwOnInconsistentEventIds(PartyIdentificationEvent.SqlPartyIdentificationEvent e)
    {
        throwOnInconsistentEventIds(this, e);
    }

    public static void throwOnInconsistentEventIds(PartyEvent.SqlPartyEvent oe, PartyIdentificationEvent.SqlPartyIdentificationEvent e)
    {
        if (!oe.getPartyEventId().getPartyId().equals(e.getPartyIdentificationEventId().getPartyId()))
        { 
            throw DomainError.named("inconsistentEventIds", "Outer Id PartyId %1$s but inner id PartyId %2$s", 
                oe.getPartyEventId().getPartyId(), e.getPartyIdentificationEventId().getPartyId());
        }
    }

    public PartyIdentificationEvent.PartyIdentificationStateCreated newPartyIdentificationStateCreated(String partyIdentificationTypeId) {
        return new AbstractPartyIdentificationEvent.SimplePartyIdentificationStateCreated(newPartyIdentificationEventId(partyIdentificationTypeId));
    }

    public PartyIdentificationEvent.PartyIdentificationStateMergePatched newPartyIdentificationStateMergePatched(String partyIdentificationTypeId) {
        return new AbstractPartyIdentificationEvent.SimplePartyIdentificationStateMergePatched(newPartyIdentificationEventId(partyIdentificationTypeId));
    }

    public PartyIdentificationEvent.PartyIdentificationStateRemoved newPartyIdentificationStateRemoved(String partyIdentificationTypeId) {
        return new AbstractPartyIdentificationEvent.SimplePartyIdentificationStateRemoved(newPartyIdentificationEventId(partyIdentificationTypeId));
    }

    public static class PartyLobEvent extends AbstractPartyEvent {

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
            return "PartyLobEvent";
        }

    }


    public static abstract class AbstractPersonStateEvent extends AbstractPartyStateEvent implements PersonEvent.PersonStateEvent {
        private String salutation;

        public String getSalutation()
        {
            return this.salutation;
        }

        public void setSalutation(String salutation)
        {
            this.salutation = salutation;
        }

        private String firstName;

        public String getFirstName()
        {
            return this.firstName;
        }

        public void setFirstName(String firstName)
        {
            this.firstName = firstName;
        }

        private String middleName;

        public String getMiddleName()
        {
            return this.middleName;
        }

        public void setMiddleName(String middleName)
        {
            this.middleName = middleName;
        }

        private String lastName;

        public String getLastName()
        {
            return this.lastName;
        }

        public void setLastName(String lastName)
        {
            this.lastName = lastName;
        }

        private String personalTitle;

        public String getPersonalTitle()
        {
            return this.personalTitle;
        }

        public void setPersonalTitle(String personalTitle)
        {
            this.personalTitle = personalTitle;
        }

        private String nickname;

        public String getNickname()
        {
            return this.nickname;
        }

        public void setNickname(String nickname)
        {
            this.nickname = nickname;
        }

        private String gender;

        public String getGender()
        {
            return this.gender;
        }

        public void setGender(String gender)
        {
            this.gender = gender;
        }

        private java.sql.Date birthDate;

        public java.sql.Date getBirthDate()
        {
            return this.birthDate;
        }

        public void setBirthDate(java.sql.Date birthDate)
        {
            this.birthDate = birthDate;
        }

        private java.sql.Date deceasedDate;

        public java.sql.Date getDeceasedDate()
        {
            return this.deceasedDate;
        }

        public void setDeceasedDate(java.sql.Date deceasedDate)
        {
            this.deceasedDate = deceasedDate;
        }

        private String socialSecurityNumber;

        public String getSocialSecurityNumber()
        {
            return this.socialSecurityNumber;
        }

        public void setSocialSecurityNumber(String socialSecurityNumber)
        {
            this.socialSecurityNumber = socialSecurityNumber;
        }

        private String passportNumber;

        public String getPassportNumber()
        {
            return this.passportNumber;
        }

        public void setPassportNumber(String passportNumber)
        {
            this.passportNumber = passportNumber;
        }

        private java.sql.Date passportExpireDate;

        public java.sql.Date getPassportExpireDate()
        {
            return this.passportExpireDate;
        }

        public void setPassportExpireDate(java.sql.Date passportExpireDate)
        {
            this.passportExpireDate = passportExpireDate;
        }

        private String existingCustomer;

        public String getExistingCustomer()
        {
            return this.existingCustomer;
        }

        public void setExistingCustomer(String existingCustomer)
        {
            this.existingCustomer = existingCustomer;
        }

        protected AbstractPersonStateEvent(PartyEventId eventId) {
            super(eventId);
        }
    }

    public static abstract class AbstractPersonStateCreated extends AbstractPersonStateEvent implements PersonEvent.PersonStateCreated, Saveable
    {
        public AbstractPersonStateCreated() {
            this(new PartyEventId());
        }

        public AbstractPersonStateCreated(PartyEventId eventId) {
            super(eventId);
        }

        public String getEventType() {
            return StateEventType.CREATED;
        }

        private Map<PartyIdentificationEventId, PartyIdentificationEvent.PartyIdentificationStateCreated> partyIdentificationEvents = new HashMap<PartyIdentificationEventId, PartyIdentificationEvent.PartyIdentificationStateCreated>();
        
        private Iterable<PartyIdentificationEvent.PartyIdentificationStateCreated> readOnlyPartyIdentificationEvents;

        public Iterable<PartyIdentificationEvent.PartyIdentificationStateCreated> getPartyIdentificationEvents()
        {
            if (!getEventReadOnly())
            {
                return this.partyIdentificationEvents.values();
            }
            else
            {
                if (readOnlyPartyIdentificationEvents != null) { return readOnlyPartyIdentificationEvents; }
                PartyIdentificationEventDao eventDao = getPartyIdentificationEventDao();
                List<PartyIdentificationEvent.PartyIdentificationStateCreated> eL = new ArrayList<PartyIdentificationEvent.PartyIdentificationStateCreated>();
                for (PartyIdentificationEvent e : eventDao.findByPartyEventId(this.getPartyEventId()))
                {
                    ((PartyIdentificationEvent.SqlPartyIdentificationEvent)e).setEventReadOnly(true);
                    eL.add((PartyIdentificationEvent.PartyIdentificationStateCreated)e);
                }
                return (readOnlyPartyIdentificationEvents = eL);
            }
        }

        public void setPartyIdentificationEvents(Iterable<PartyIdentificationEvent.PartyIdentificationStateCreated> es)
        {
            if (es != null)
            {
                for (PartyIdentificationEvent.PartyIdentificationStateCreated e : es)
                {
                    addPartyIdentificationEvent(e);
                }
            }
            else { this.partyIdentificationEvents.clear(); }
        }
        
        public void addPartyIdentificationEvent(PartyIdentificationEvent.PartyIdentificationStateCreated e)
        {
            throwOnInconsistentEventIds((PartyIdentificationEvent.SqlPartyIdentificationEvent)e);
            this.partyIdentificationEvents.put(((PartyIdentificationEvent.SqlPartyIdentificationEvent)e).getPartyIdentificationEventId(), e);
        }

        public void save()
        {
            for (PartyIdentificationEvent.PartyIdentificationStateCreated e : this.getPartyIdentificationEvents()) {
                getPartyIdentificationEventDao().save(e);
            }
        }
    }


    public static abstract class AbstractPersonStateMergePatched extends AbstractPersonStateEvent implements PersonEvent.PersonStateMergePatched, Saveable
    {
        public AbstractPersonStateMergePatched() {
            this(new PartyEventId());
        }

        public AbstractPersonStateMergePatched(PartyEventId eventId) {
            super(eventId);
        }

        public String getEventType() {
            return StateEventType.MERGE_PATCHED;
        }

        private Boolean isPropertySalutationRemoved;

        public Boolean getIsPropertySalutationRemoved() {
            return this.isPropertySalutationRemoved;
        }

        public void setIsPropertySalutationRemoved(Boolean removed) {
            this.isPropertySalutationRemoved = removed;
        }

        private Boolean isPropertyFirstNameRemoved;

        public Boolean getIsPropertyFirstNameRemoved() {
            return this.isPropertyFirstNameRemoved;
        }

        public void setIsPropertyFirstNameRemoved(Boolean removed) {
            this.isPropertyFirstNameRemoved = removed;
        }

        private Boolean isPropertyMiddleNameRemoved;

        public Boolean getIsPropertyMiddleNameRemoved() {
            return this.isPropertyMiddleNameRemoved;
        }

        public void setIsPropertyMiddleNameRemoved(Boolean removed) {
            this.isPropertyMiddleNameRemoved = removed;
        }

        private Boolean isPropertyLastNameRemoved;

        public Boolean getIsPropertyLastNameRemoved() {
            return this.isPropertyLastNameRemoved;
        }

        public void setIsPropertyLastNameRemoved(Boolean removed) {
            this.isPropertyLastNameRemoved = removed;
        }

        private Boolean isPropertyPersonalTitleRemoved;

        public Boolean getIsPropertyPersonalTitleRemoved() {
            return this.isPropertyPersonalTitleRemoved;
        }

        public void setIsPropertyPersonalTitleRemoved(Boolean removed) {
            this.isPropertyPersonalTitleRemoved = removed;
        }

        private Boolean isPropertyNicknameRemoved;

        public Boolean getIsPropertyNicknameRemoved() {
            return this.isPropertyNicknameRemoved;
        }

        public void setIsPropertyNicknameRemoved(Boolean removed) {
            this.isPropertyNicknameRemoved = removed;
        }

        private Boolean isPropertyGenderRemoved;

        public Boolean getIsPropertyGenderRemoved() {
            return this.isPropertyGenderRemoved;
        }

        public void setIsPropertyGenderRemoved(Boolean removed) {
            this.isPropertyGenderRemoved = removed;
        }

        private Boolean isPropertyBirthDateRemoved;

        public Boolean getIsPropertyBirthDateRemoved() {
            return this.isPropertyBirthDateRemoved;
        }

        public void setIsPropertyBirthDateRemoved(Boolean removed) {
            this.isPropertyBirthDateRemoved = removed;
        }

        private Boolean isPropertyDeceasedDateRemoved;

        public Boolean getIsPropertyDeceasedDateRemoved() {
            return this.isPropertyDeceasedDateRemoved;
        }

        public void setIsPropertyDeceasedDateRemoved(Boolean removed) {
            this.isPropertyDeceasedDateRemoved = removed;
        }

        private Boolean isPropertySocialSecurityNumberRemoved;

        public Boolean getIsPropertySocialSecurityNumberRemoved() {
            return this.isPropertySocialSecurityNumberRemoved;
        }

        public void setIsPropertySocialSecurityNumberRemoved(Boolean removed) {
            this.isPropertySocialSecurityNumberRemoved = removed;
        }

        private Boolean isPropertyPassportNumberRemoved;

        public Boolean getIsPropertyPassportNumberRemoved() {
            return this.isPropertyPassportNumberRemoved;
        }

        public void setIsPropertyPassportNumberRemoved(Boolean removed) {
            this.isPropertyPassportNumberRemoved = removed;
        }

        private Boolean isPropertyPassportExpireDateRemoved;

        public Boolean getIsPropertyPassportExpireDateRemoved() {
            return this.isPropertyPassportExpireDateRemoved;
        }

        public void setIsPropertyPassportExpireDateRemoved(Boolean removed) {
            this.isPropertyPassportExpireDateRemoved = removed;
        }

        private Boolean isPropertyExistingCustomerRemoved;

        public Boolean getIsPropertyExistingCustomerRemoved() {
            return this.isPropertyExistingCustomerRemoved;
        }

        public void setIsPropertyExistingCustomerRemoved(Boolean removed) {
            this.isPropertyExistingCustomerRemoved = removed;
        }

        private Boolean isPropertyPartyTypeIdRemoved;

        public Boolean getIsPropertyPartyTypeIdRemoved() {
            return this.isPropertyPartyTypeIdRemoved;
        }

        public void setIsPropertyPartyTypeIdRemoved(Boolean removed) {
            this.isPropertyPartyTypeIdRemoved = removed;
        }

        private Boolean isPropertyPrimaryRoleTypeIdRemoved;

        public Boolean getIsPropertyPrimaryRoleTypeIdRemoved() {
            return this.isPropertyPrimaryRoleTypeIdRemoved;
        }

        public void setIsPropertyPrimaryRoleTypeIdRemoved(Boolean removed) {
            this.isPropertyPrimaryRoleTypeIdRemoved = removed;
        }

        private Boolean isPropertyExternalIdRemoved;

        public Boolean getIsPropertyExternalIdRemoved() {
            return this.isPropertyExternalIdRemoved;
        }

        public void setIsPropertyExternalIdRemoved(Boolean removed) {
            this.isPropertyExternalIdRemoved = removed;
        }

        private Boolean isPropertyPreferredCurrencyUomIdRemoved;

        public Boolean getIsPropertyPreferredCurrencyUomIdRemoved() {
            return this.isPropertyPreferredCurrencyUomIdRemoved;
        }

        public void setIsPropertyPreferredCurrencyUomIdRemoved(Boolean removed) {
            this.isPropertyPreferredCurrencyUomIdRemoved = removed;
        }

        private Boolean isPropertyDescriptionRemoved;

        public Boolean getIsPropertyDescriptionRemoved() {
            return this.isPropertyDescriptionRemoved;
        }

        public void setIsPropertyDescriptionRemoved(Boolean removed) {
            this.isPropertyDescriptionRemoved = removed;
        }

        private Boolean isPropertyStatusIdRemoved;

        public Boolean getIsPropertyStatusIdRemoved() {
            return this.isPropertyStatusIdRemoved;
        }

        public void setIsPropertyStatusIdRemoved(Boolean removed) {
            this.isPropertyStatusIdRemoved = removed;
        }

        private Boolean isPropertyShortDescriptionRemoved;

        public Boolean getIsPropertyShortDescriptionRemoved() {
            return this.isPropertyShortDescriptionRemoved;
        }

        public void setIsPropertyShortDescriptionRemoved(Boolean removed) {
            this.isPropertyShortDescriptionRemoved = removed;
        }

        private Boolean isPropertyEmailRemoved;

        public Boolean getIsPropertyEmailRemoved() {
            return this.isPropertyEmailRemoved;
        }

        public void setIsPropertyEmailRemoved(Boolean removed) {
            this.isPropertyEmailRemoved = removed;
        }

        private Boolean isPropertyWebSiteRemoved;

        public Boolean getIsPropertyWebSiteRemoved() {
            return this.isPropertyWebSiteRemoved;
        }

        public void setIsPropertyWebSiteRemoved(Boolean removed) {
            this.isPropertyWebSiteRemoved = removed;
        }

        private Boolean isPropertyTelephoneRemoved;

        public Boolean getIsPropertyTelephoneRemoved() {
            return this.isPropertyTelephoneRemoved;
        }

        public void setIsPropertyTelephoneRemoved(Boolean removed) {
            this.isPropertyTelephoneRemoved = removed;
        }


        private Map<PartyIdentificationEventId, PartyIdentificationEvent> partyIdentificationEvents = new HashMap<PartyIdentificationEventId, PartyIdentificationEvent>();
        
        private Iterable<PartyIdentificationEvent> readOnlyPartyIdentificationEvents;

        public Iterable<PartyIdentificationEvent> getPartyIdentificationEvents()
        {
            if (!getEventReadOnly())
            {
                return this.partyIdentificationEvents.values();
            }
            else
            {
                if (readOnlyPartyIdentificationEvents != null) { return readOnlyPartyIdentificationEvents; }
                PartyIdentificationEventDao eventDao = getPartyIdentificationEventDao();
                List<PartyIdentificationEvent> eL = new ArrayList<PartyIdentificationEvent>();
                for (PartyIdentificationEvent e : eventDao.findByPartyEventId(this.getPartyEventId()))
                {
                    ((PartyIdentificationEvent.SqlPartyIdentificationEvent)e).setEventReadOnly(true);
                    eL.add((PartyIdentificationEvent)e);
                }
                return (readOnlyPartyIdentificationEvents = eL);
            }
        }

        public void setPartyIdentificationEvents(Iterable<PartyIdentificationEvent> es)
        {
            if (es != null)
            {
                for (PartyIdentificationEvent e : es)
                {
                    addPartyIdentificationEvent(e);
                }
            }
            else { this.partyIdentificationEvents.clear(); }
        }
        
        public void addPartyIdentificationEvent(PartyIdentificationEvent e)
        {
            throwOnInconsistentEventIds((PartyIdentificationEvent.SqlPartyIdentificationEvent)e);
            this.partyIdentificationEvents.put(((PartyIdentificationEvent.SqlPartyIdentificationEvent)e).getPartyIdentificationEventId(), e);
        }

        public void save()
        {
            for (PartyIdentificationEvent e : this.getPartyIdentificationEvents()) {
                getPartyIdentificationEventDao().save(e);
            }
        }
    }



    public static class SimplePersonStateCreated extends AbstractPersonStateCreated
    {
        public SimplePersonStateCreated() {
        }

        public SimplePersonStateCreated(PartyEventId eventId) {
            super(eventId);
        }
    }

    public static class SimplePersonStateMergePatched extends AbstractPersonStateMergePatched
    {
        public SimplePersonStateMergePatched() {
        }

        public SimplePersonStateMergePatched(PartyEventId eventId) {
            super(eventId);
        }
    }

}

