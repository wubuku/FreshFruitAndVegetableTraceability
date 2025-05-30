// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.partyrole;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.AbstractCommand;

public abstract class AbstractPartyRoleCommand extends AbstractCommand implements PartyRoleCommand {

    private PartyRoleId partyRoleId;

    public PartyRoleId getPartyRoleId()
    {
        return this.partyRoleId;
    }

    public void setPartyRoleId(PartyRoleId partyRoleId)
    {
        this.partyRoleId = partyRoleId;
    }

    private Long version;

    public Long getVersion()
    {
        return this.version;
    }

    public void setVersion(Long version)
    {
        this.version = version;
    }


    public static abstract class AbstractCreateOrMergePatchPartyRole extends AbstractPartyRoleCommand implements CreateOrMergePatchPartyRole
    {
        private String tpaNumber;

        public String getTpaNumber()
        {
            return this.tpaNumber;
        }

        public void setTpaNumber(String tpaNumber)
        {
            this.tpaNumber = tpaNumber;
        }

        private String certificationCodes;

        public String getCertificationCodes()
        {
            return this.certificationCodes;
        }

        public void setCertificationCodes(String certificationCodes)
        {
            this.certificationCodes = certificationCodes;
        }

        private String bankAccountInformation;

        public String getBankAccountInformation()
        {
            return this.bankAccountInformation;
        }

        public void setBankAccountInformation(String bankAccountInformation)
        {
            this.bankAccountInformation = bankAccountInformation;
        }

        private String supplierTypeEnumId;

        public String getSupplierTypeEnumId()
        {
            return this.supplierTypeEnumId;
        }

        public void setSupplierTypeEnumId(String supplierTypeEnumId)
        {
            this.supplierTypeEnumId = supplierTypeEnumId;
        }

        private String supplierProductTypeDescription;

        public String getSupplierProductTypeDescription()
        {
            return this.supplierProductTypeDescription;
        }

        public void setSupplierProductTypeDescription(String supplierProductTypeDescription)
        {
            this.supplierProductTypeDescription = supplierProductTypeDescription;
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

    }

    public static abstract class AbstractCreatePartyRole extends AbstractCreateOrMergePatchPartyRole implements CreatePartyRole
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }

    }

    public static abstract class AbstractMergePatchPartyRole extends AbstractCreateOrMergePatchPartyRole implements MergePatchPartyRole
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_MERGE_PATCH;
        }

        private Boolean isPropertyTpaNumberRemoved;

        public Boolean getIsPropertyTpaNumberRemoved()
        {
            return this.isPropertyTpaNumberRemoved;
        }

        public void setIsPropertyTpaNumberRemoved(Boolean removed)
        {
            this.isPropertyTpaNumberRemoved = removed;
        }

        private Boolean isPropertyCertificationCodesRemoved;

        public Boolean getIsPropertyCertificationCodesRemoved()
        {
            return this.isPropertyCertificationCodesRemoved;
        }

        public void setIsPropertyCertificationCodesRemoved(Boolean removed)
        {
            this.isPropertyCertificationCodesRemoved = removed;
        }

        private Boolean isPropertyBankAccountInformationRemoved;

        public Boolean getIsPropertyBankAccountInformationRemoved()
        {
            return this.isPropertyBankAccountInformationRemoved;
        }

        public void setIsPropertyBankAccountInformationRemoved(Boolean removed)
        {
            this.isPropertyBankAccountInformationRemoved = removed;
        }

        private Boolean isPropertySupplierTypeEnumIdRemoved;

        public Boolean getIsPropertySupplierTypeEnumIdRemoved()
        {
            return this.isPropertySupplierTypeEnumIdRemoved;
        }

        public void setIsPropertySupplierTypeEnumIdRemoved(Boolean removed)
        {
            this.isPropertySupplierTypeEnumIdRemoved = removed;
        }

        private Boolean isPropertySupplierProductTypeDescriptionRemoved;

        public Boolean getIsPropertySupplierProductTypeDescriptionRemoved()
        {
            return this.isPropertySupplierProductTypeDescriptionRemoved;
        }

        public void setIsPropertySupplierProductTypeDescriptionRemoved(Boolean removed)
        {
            this.isPropertySupplierProductTypeDescriptionRemoved = removed;
        }

        private Boolean isPropertyShippingAddressRemoved;

        public Boolean getIsPropertyShippingAddressRemoved()
        {
            return this.isPropertyShippingAddressRemoved;
        }

        public void setIsPropertyShippingAddressRemoved(Boolean removed)
        {
            this.isPropertyShippingAddressRemoved = removed;
        }

        private Boolean isPropertyPaymentMethodEnumIdRemoved;

        public Boolean getIsPropertyPaymentMethodEnumIdRemoved()
        {
            return this.isPropertyPaymentMethodEnumIdRemoved;
        }

        public void setIsPropertyPaymentMethodEnumIdRemoved(Boolean removed)
        {
            this.isPropertyPaymentMethodEnumIdRemoved = removed;
        }

        private Boolean isPropertyCreditRatingRemoved;

        public Boolean getIsPropertyCreditRatingRemoved()
        {
            return this.isPropertyCreditRatingRemoved;
        }

        public void setIsPropertyCreditRatingRemoved(Boolean removed)
        {
            this.isPropertyCreditRatingRemoved = removed;
        }

        private Boolean isPropertyCustomerTypeEnumIdRemoved;

        public Boolean getIsPropertyCustomerTypeEnumIdRemoved()
        {
            return this.isPropertyCustomerTypeEnumIdRemoved;
        }

        public void setIsPropertyCustomerTypeEnumIdRemoved(Boolean removed)
        {
            this.isPropertyCustomerTypeEnumIdRemoved = removed;
        }

        private Boolean isPropertyCustomerProductTypeDescriptionRemoved;

        public Boolean getIsPropertyCustomerProductTypeDescriptionRemoved()
        {
            return this.isPropertyCustomerProductTypeDescriptionRemoved;
        }

        public void setIsPropertyCustomerProductTypeDescriptionRemoved(Boolean removed)
        {
            this.isPropertyCustomerProductTypeDescriptionRemoved = removed;
        }


    }

    public static class SimpleCreatePartyRole extends AbstractCreatePartyRole
    {
    }

    
    public static class SimpleMergePatchPartyRole extends AbstractMergePatchPartyRole
    {
    }

    
    public static class SimpleDeletePartyRole extends AbstractPartyRoleCommand implements DeletePartyRole
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_DELETE;
        }
    }

    

}

