// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain;

import java.io.Serializable;
import org.dddml.ffvtraceability.domain.*;
import jakarta.validation.constraints.Pattern;

public class BffCustomerDto implements Serializable {
    private String customerId;

    public String getCustomerId()
    {
        return this.customerId;
    }

    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    private String customerName;

    public String getCustomerName()
    {
        return this.customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    /**
     * GGN (GLOBALG.A.P. Number)
     */
    @Pattern(
        regexp = "^\\d{13}$",
        message = "Must match GGN"
    )
    private String ggn;

    public String getGgn()
    {
        return this.ggn;
    }

    public void setGgn(String ggn)
    {
        this.ggn = ggn;
    }

    /**
     * GLN (Global Location Number)
     */
    @Pattern(
        regexp = "^\\d{12}\\d$",
        message = "Must match GLN"
    )
    private String gln;

    public String getGln()
    {
        return this.gln;
    }

    public void setGln(String gln)
    {
        this.gln = gln;
    }

    private String externalId;

    public String getExternalId()
    {
        return this.externalId;
    }

    public void setExternalId(String externalId)
    {
        this.externalId = externalId;
    }

    private String preferredCurrencyUomId;

    public String getPreferredCurrencyUomId()
    {
        return this.preferredCurrencyUomId;
    }

    public void setPreferredCurrencyUomId(String preferredCurrencyUomId)
    {
        this.preferredCurrencyUomId = preferredCurrencyUomId;
    }

    private String description;

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    private String statusId;

    public String getStatusId()
    {
        return this.statusId;
    }

    public void setStatusId(String statusId)
    {
        this.statusId = statusId;
    }

    private java.util.List<BffBusinessContactDto> businessContacts;

    public java.util.List<BffBusinessContactDto> getBusinessContacts() {
        return this.businessContacts;
    }

    public void setBusinessContacts(java.util.List<BffBusinessContactDto> businessContacts) {
        this.businessContacts = businessContacts;
    }

    private String customerShortName;

    public String getCustomerShortName()
    {
        return this.customerShortName;
    }

    public void setCustomerShortName(String customerShortName)
    {
        this.customerShortName = customerShortName;
    }

    private String taxId;

    public String getTaxId()
    {
        return this.taxId;
    }

    public void setTaxId(String taxId)
    {
        this.taxId = taxId;
    }

    /**
     * GS1 Company Prefix
     */
    private String gs1CompanyPrefix;

    public String getGs1CompanyPrefix()
    {
        return this.gs1CompanyPrefix;
    }

    public void setGs1CompanyPrefix(String gs1CompanyPrefix)
    {
        this.gs1CompanyPrefix = gs1CompanyPrefix;
    }

    private String internalId;

    public String getInternalId()
    {
        return this.internalId;
    }

    public void setInternalId(String internalId)
    {
        this.internalId = internalId;
    }

    /**
     * Trade Partner Agreement Number
     */
    private String tpaNumber;

    public String getTpaNumber()
    {
        return this.tpaNumber;
    }

    public void setTpaNumber(String tpaNumber)
    {
        this.tpaNumber = tpaNumber;
    }

    private String shippingAddress;

    public String getShippingAddress()
    {
        return this.shippingAddress;
    }

    public void setShippingAddress(String shippingAddress)
    {
        this.shippingAddress = shippingAddress;
    }

    private String paymentMethodEnumId;

    public String getPaymentMethodEnumId()
    {
        return this.paymentMethodEnumId;
    }

    public void setPaymentMethodEnumId(String paymentMethodEnumId)
    {
        this.paymentMethodEnumId = paymentMethodEnumId;
    }

    private String creditRating;

    public String getCreditRating()
    {
        return this.creditRating;
    }

    public void setCreditRating(String creditRating)
    {
        this.creditRating = creditRating;
    }

    private String customerTypeEnumId;

    public String getCustomerTypeEnumId()
    {
        return this.customerTypeEnumId;
    }

    public void setCustomerTypeEnumId(String customerTypeEnumId)
    {
        this.customerTypeEnumId = customerTypeEnumId;
    }

    private String customerProductTypeDescription;

    public String getCustomerProductTypeDescription()
    {
        return this.customerProductTypeDescription;
    }

    public void setCustomerProductTypeDescription(String customerProductTypeDescription)
    {
        this.customerProductTypeDescription = customerProductTypeDescription;
    }

    /**
     * Certification Codes
     */
    private String certificationCodes;

    public String getCertificationCodes()
    {
        return this.certificationCodes;
    }

    public void setCertificationCodes(String certificationCodes)
    {
        this.certificationCodes = certificationCodes;
    }

    /**
     * Bank Account Information
     */
    private String bankAccountInformation;

    public String getBankAccountInformation()
    {
        return this.bankAccountInformation;
    }

    public void setBankAccountInformation(String bankAccountInformation)
    {
        this.bankAccountInformation = bankAccountInformation;
    }

    /**
     * Telecom Country Code
     */
    private String telecomCountryCode;

    public String getTelecomCountryCode()
    {
        return this.telecomCountryCode;
    }

    public void setTelecomCountryCode(String telecomCountryCode)
    {
        this.telecomCountryCode = telecomCountryCode;
    }

    private String telephone;

    public String getTelephone()
    {
        return this.telephone;
    }

    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }

    private String email;

    public String getEmail()
    {
        return this.email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    private String webSite;

    public String getWebSite()
    {
        return this.webSite;
    }

    public void setWebSite(String webSite)
    {
        this.webSite = webSite;
    }

    private String active;

    public String getActive()
    {
        return this.active;
    }

    public void setActive(String active)
    {
        this.active = active;
    }

    private java.util.List<BffFacilityDto> facilities;

    public java.util.List<BffFacilityDto> getFacilities() {
        return this.facilities;
    }

    public void setFacilities(java.util.List<BffFacilityDto> facilities) {
        this.facilities = facilities;
    }

    public BffCustomerDto()
    {
    }

    public BffCustomerDto(String customerId, String customerName, String ggn, String gln, String externalId, String preferredCurrencyUomId, String description, String statusId, java.util.List<BffBusinessContactDto> businessContacts, String customerShortName, String taxId, String gs1CompanyPrefix, String internalId, String tpaNumber, String shippingAddress, String paymentMethodEnumId, String creditRating, String customerTypeEnumId, String customerProductTypeDescription, String certificationCodes, String bankAccountInformation, String telecomCountryCode, String telephone, String email, String webSite, String active, java.util.List<BffFacilityDto> facilities)
    {
        this.customerId = customerId;
        this.customerName = customerName;
        this.ggn = ggn;
        this.gln = gln;
        this.externalId = externalId;
        this.preferredCurrencyUomId = preferredCurrencyUomId;
        this.description = description;
        this.statusId = statusId;
        this.businessContacts = businessContacts;
        this.customerShortName = customerShortName;
        this.taxId = taxId;
        this.gs1CompanyPrefix = gs1CompanyPrefix;
        this.internalId = internalId;
        this.tpaNumber = tpaNumber;
        this.shippingAddress = shippingAddress;
        this.paymentMethodEnumId = paymentMethodEnumId;
        this.creditRating = creditRating;
        this.customerTypeEnumId = customerTypeEnumId;
        this.customerProductTypeDescription = customerProductTypeDescription;
        this.certificationCodes = certificationCodes;
        this.bankAccountInformation = bankAccountInformation;
        this.telecomCountryCode = telecomCountryCode;
        this.telephone = telephone;
        this.email = email;
        this.webSite = webSite;
        this.active = active;
        this.facilities = facilities;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        BffCustomerDto other = (BffCustomerDto)obj;
        return true 
            && (customerId == other.customerId || (customerId != null && customerId.equals(other.customerId)))
            && (customerName == other.customerName || (customerName != null && customerName.equals(other.customerName)))
            && (ggn == other.ggn || (ggn != null && ggn.equals(other.ggn)))
            && (gln == other.gln || (gln != null && gln.equals(other.gln)))
            && (externalId == other.externalId || (externalId != null && externalId.equals(other.externalId)))
            && (preferredCurrencyUomId == other.preferredCurrencyUomId || (preferredCurrencyUomId != null && preferredCurrencyUomId.equals(other.preferredCurrencyUomId)))
            && (description == other.description || (description != null && description.equals(other.description)))
            && (statusId == other.statusId || (statusId != null && statusId.equals(other.statusId)))
            && (businessContacts == other.businessContacts || (businessContacts != null && businessContacts.equals(other.businessContacts)))
            && (customerShortName == other.customerShortName || (customerShortName != null && customerShortName.equals(other.customerShortName)))
            && (taxId == other.taxId || (taxId != null && taxId.equals(other.taxId)))
            && (gs1CompanyPrefix == other.gs1CompanyPrefix || (gs1CompanyPrefix != null && gs1CompanyPrefix.equals(other.gs1CompanyPrefix)))
            && (internalId == other.internalId || (internalId != null && internalId.equals(other.internalId)))
            && (tpaNumber == other.tpaNumber || (tpaNumber != null && tpaNumber.equals(other.tpaNumber)))
            && (shippingAddress == other.shippingAddress || (shippingAddress != null && shippingAddress.equals(other.shippingAddress)))
            && (paymentMethodEnumId == other.paymentMethodEnumId || (paymentMethodEnumId != null && paymentMethodEnumId.equals(other.paymentMethodEnumId)))
            && (creditRating == other.creditRating || (creditRating != null && creditRating.equals(other.creditRating)))
            && (customerTypeEnumId == other.customerTypeEnumId || (customerTypeEnumId != null && customerTypeEnumId.equals(other.customerTypeEnumId)))
            && (customerProductTypeDescription == other.customerProductTypeDescription || (customerProductTypeDescription != null && customerProductTypeDescription.equals(other.customerProductTypeDescription)))
            && (certificationCodes == other.certificationCodes || (certificationCodes != null && certificationCodes.equals(other.certificationCodes)))
            && (bankAccountInformation == other.bankAccountInformation || (bankAccountInformation != null && bankAccountInformation.equals(other.bankAccountInformation)))
            && (telecomCountryCode == other.telecomCountryCode || (telecomCountryCode != null && telecomCountryCode.equals(other.telecomCountryCode)))
            && (telephone == other.telephone || (telephone != null && telephone.equals(other.telephone)))
            && (email == other.email || (email != null && email.equals(other.email)))
            && (webSite == other.webSite || (webSite != null && webSite.equals(other.webSite)))
            && (active == other.active || (active != null && active.equals(other.active)))
            && (facilities == other.facilities || (facilities != null && facilities.equals(other.facilities)))
            ;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        if (this.customerId != null) {
            hash += 13 * this.customerId.hashCode();
        }
        if (this.customerName != null) {
            hash += 13 * this.customerName.hashCode();
        }
        if (this.ggn != null) {
            hash += 13 * this.ggn.hashCode();
        }
        if (this.gln != null) {
            hash += 13 * this.gln.hashCode();
        }
        if (this.externalId != null) {
            hash += 13 * this.externalId.hashCode();
        }
        if (this.preferredCurrencyUomId != null) {
            hash += 13 * this.preferredCurrencyUomId.hashCode();
        }
        if (this.description != null) {
            hash += 13 * this.description.hashCode();
        }
        if (this.statusId != null) {
            hash += 13 * this.statusId.hashCode();
        }
        if (this.businessContacts != null) {
            hash += 13 * this.businessContacts.hashCode();
        }
        if (this.customerShortName != null) {
            hash += 13 * this.customerShortName.hashCode();
        }
        if (this.taxId != null) {
            hash += 13 * this.taxId.hashCode();
        }
        if (this.gs1CompanyPrefix != null) {
            hash += 13 * this.gs1CompanyPrefix.hashCode();
        }
        if (this.internalId != null) {
            hash += 13 * this.internalId.hashCode();
        }
        if (this.tpaNumber != null) {
            hash += 13 * this.tpaNumber.hashCode();
        }
        if (this.shippingAddress != null) {
            hash += 13 * this.shippingAddress.hashCode();
        }
        if (this.paymentMethodEnumId != null) {
            hash += 13 * this.paymentMethodEnumId.hashCode();
        }
        if (this.creditRating != null) {
            hash += 13 * this.creditRating.hashCode();
        }
        if (this.customerTypeEnumId != null) {
            hash += 13 * this.customerTypeEnumId.hashCode();
        }
        if (this.customerProductTypeDescription != null) {
            hash += 13 * this.customerProductTypeDescription.hashCode();
        }
        if (this.certificationCodes != null) {
            hash += 13 * this.certificationCodes.hashCode();
        }
        if (this.bankAccountInformation != null) {
            hash += 13 * this.bankAccountInformation.hashCode();
        }
        if (this.telecomCountryCode != null) {
            hash += 13 * this.telecomCountryCode.hashCode();
        }
        if (this.telephone != null) {
            hash += 13 * this.telephone.hashCode();
        }
        if (this.email != null) {
            hash += 13 * this.email.hashCode();
        }
        if (this.webSite != null) {
            hash += 13 * this.webSite.hashCode();
        }
        if (this.active != null) {
            hash += 13 * this.active.hashCode();
        }
        if (this.facilities != null) {
            hash += 13 * this.facilities.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "BffCustomerDto{" +
                "customerId=" + '\'' + customerId + '\'' +
                ", customerName=" + '\'' + customerName + '\'' +
                ", ggn=" + '\'' + ggn + '\'' +
                ", gln=" + '\'' + gln + '\'' +
                ", externalId=" + '\'' + externalId + '\'' +
                ", preferredCurrencyUomId=" + '\'' + preferredCurrencyUomId + '\'' +
                ", description=" + '\'' + description + '\'' +
                ", statusId=" + '\'' + statusId + '\'' +
                ", businessContacts=" + businessContacts +
                ", customerShortName=" + '\'' + customerShortName + '\'' +
                ", taxId=" + '\'' + taxId + '\'' +
                ", gs1CompanyPrefix=" + '\'' + gs1CompanyPrefix + '\'' +
                ", internalId=" + '\'' + internalId + '\'' +
                ", tpaNumber=" + '\'' + tpaNumber + '\'' +
                ", shippingAddress=" + '\'' + shippingAddress + '\'' +
                ", paymentMethodEnumId=" + '\'' + paymentMethodEnumId + '\'' +
                ", creditRating=" + '\'' + creditRating + '\'' +
                ", customerTypeEnumId=" + '\'' + customerTypeEnumId + '\'' +
                ", customerProductTypeDescription=" + '\'' + customerProductTypeDescription + '\'' +
                ", certificationCodes=" + '\'' + certificationCodes + '\'' +
                ", bankAccountInformation=" + '\'' + bankAccountInformation + '\'' +
                ", telecomCountryCode=" + '\'' + telecomCountryCode + '\'' +
                ", telephone=" + '\'' + telephone + '\'' +
                ", email=" + '\'' + email + '\'' +
                ", webSite=" + '\'' + webSite + '\'' +
                ", active=" + '\'' + active + '\'' +
                ", facilities=" + facilities +
                '}';
    }


}

