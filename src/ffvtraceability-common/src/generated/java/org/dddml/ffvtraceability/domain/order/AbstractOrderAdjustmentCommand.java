// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.order;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.AbstractCommand;

public abstract class AbstractOrderAdjustmentCommand extends AbstractCommand implements OrderAdjustmentCommand {

    private String orderAdjustmentId;

    public String getOrderAdjustmentId()
    {
        return this.orderAdjustmentId;
    }

    public void setOrderAdjustmentId(String orderAdjustmentId)
    {
        this.orderAdjustmentId = orderAdjustmentId;
    }

    private String orderId;

    public String getOrderId()
    {
        return this.orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }


    public static abstract class AbstractCreateOrMergePatchOrderAdjustment extends AbstractOrderAdjustmentCommand implements CreateOrMergePatchOrderAdjustment
    {

        private String orderAdjustmentTypeId;

        public String getOrderAdjustmentTypeId()
        {
            return this.orderAdjustmentTypeId;
        }

        public void setOrderAdjustmentTypeId(String orderAdjustmentTypeId)
        {
            this.orderAdjustmentTypeId = orderAdjustmentTypeId;
        }

        private String orderItemSeqId;

        public String getOrderItemSeqId()
        {
            return this.orderItemSeqId;
        }

        public void setOrderItemSeqId(String orderItemSeqId)
        {
            this.orderItemSeqId = orderItemSeqId;
        }

        private String shipGroupSeqId;

        public String getShipGroupSeqId()
        {
            return this.shipGroupSeqId;
        }

        public void setShipGroupSeqId(String shipGroupSeqId)
        {
            this.shipGroupSeqId = shipGroupSeqId;
        }

        private String comments;

        public String getComments()
        {
            return this.comments;
        }

        public void setComments(String comments)
        {
            this.comments = comments;
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

        private java.math.BigDecimal amount;

        public java.math.BigDecimal getAmount()
        {
            return this.amount;
        }

        public void setAmount(java.math.BigDecimal amount)
        {
            this.amount = amount;
        }

        private String currencyUomId;

        public String getCurrencyUomId()
        {
            return this.currencyUomId;
        }

        public void setCurrencyUomId(String currencyUomId)
        {
            this.currencyUomId = currencyUomId;
        }

        private java.math.BigDecimal recurringAmount;

        public java.math.BigDecimal getRecurringAmount()
        {
            return this.recurringAmount;
        }

        public void setRecurringAmount(java.math.BigDecimal recurringAmount)
        {
            this.recurringAmount = recurringAmount;
        }

        private java.math.BigDecimal amountAlreadyIncluded;

        public java.math.BigDecimal getAmountAlreadyIncluded()
        {
            return this.amountAlreadyIncluded;
        }

        public void setAmountAlreadyIncluded(java.math.BigDecimal amountAlreadyIncluded)
        {
            this.amountAlreadyIncluded = amountAlreadyIncluded;
        }

        private String productPromoId;

        public String getProductPromoId()
        {
            return this.productPromoId;
        }

        public void setProductPromoId(String productPromoId)
        {
            this.productPromoId = productPromoId;
        }

        private String productPromoRuleId;

        public String getProductPromoRuleId()
        {
            return this.productPromoRuleId;
        }

        public void setProductPromoRuleId(String productPromoRuleId)
        {
            this.productPromoRuleId = productPromoRuleId;
        }

        private String productPromoActionSeqId;

        public String getProductPromoActionSeqId()
        {
            return this.productPromoActionSeqId;
        }

        public void setProductPromoActionSeqId(String productPromoActionSeqId)
        {
            this.productPromoActionSeqId = productPromoActionSeqId;
        }

        private String productFeatureId;

        public String getProductFeatureId()
        {
            return this.productFeatureId;
        }

        public void setProductFeatureId(String productFeatureId)
        {
            this.productFeatureId = productFeatureId;
        }

        private String correspondingProductId;

        public String getCorrespondingProductId()
        {
            return this.correspondingProductId;
        }

        public void setCorrespondingProductId(String correspondingProductId)
        {
            this.correspondingProductId = correspondingProductId;
        }

        private String taxAuthorityRateSeqId;

        public String getTaxAuthorityRateSeqId()
        {
            return this.taxAuthorityRateSeqId;
        }

        public void setTaxAuthorityRateSeqId(String taxAuthorityRateSeqId)
        {
            this.taxAuthorityRateSeqId = taxAuthorityRateSeqId;
        }

        private String sourceReferenceId;

        public String getSourceReferenceId()
        {
            return this.sourceReferenceId;
        }

        public void setSourceReferenceId(String sourceReferenceId)
        {
            this.sourceReferenceId = sourceReferenceId;
        }

        private java.math.BigDecimal sourcePercentage;

        public java.math.BigDecimal getSourcePercentage()
        {
            return this.sourcePercentage;
        }

        public void setSourcePercentage(java.math.BigDecimal sourcePercentage)
        {
            this.sourcePercentage = sourcePercentage;
        }

        private String customerReferenceId;

        public String getCustomerReferenceId()
        {
            return this.customerReferenceId;
        }

        public void setCustomerReferenceId(String customerReferenceId)
        {
            this.customerReferenceId = customerReferenceId;
        }

        private String primaryGeoId;

        public String getPrimaryGeoId()
        {
            return this.primaryGeoId;
        }

        public void setPrimaryGeoId(String primaryGeoId)
        {
            this.primaryGeoId = primaryGeoId;
        }

        private String secondaryGeoId;

        public String getSecondaryGeoId()
        {
            return this.secondaryGeoId;
        }

        public void setSecondaryGeoId(String secondaryGeoId)
        {
            this.secondaryGeoId = secondaryGeoId;
        }

        private java.math.BigDecimal exemptAmount;

        public java.math.BigDecimal getExemptAmount()
        {
            return this.exemptAmount;
        }

        public void setExemptAmount(java.math.BigDecimal exemptAmount)
        {
            this.exemptAmount = exemptAmount;
        }

        private String taxAuthGeoId;

        public String getTaxAuthGeoId()
        {
            return this.taxAuthGeoId;
        }

        public void setTaxAuthGeoId(String taxAuthGeoId)
        {
            this.taxAuthGeoId = taxAuthGeoId;
        }

        private String taxAuthPartyId;

        public String getTaxAuthPartyId()
        {
            return this.taxAuthPartyId;
        }

        public void setTaxAuthPartyId(String taxAuthPartyId)
        {
            this.taxAuthPartyId = taxAuthPartyId;
        }

        private String overrideGlAccountId;

        public String getOverrideGlAccountId()
        {
            return this.overrideGlAccountId;
        }

        public void setOverrideGlAccountId(String overrideGlAccountId)
        {
            this.overrideGlAccountId = overrideGlAccountId;
        }

        private String includeInTax;

        public String getIncludeInTax()
        {
            return this.includeInTax;
        }

        public void setIncludeInTax(String includeInTax)
        {
            this.includeInTax = includeInTax;
        }

        private String includeInShipping;

        public String getIncludeInShipping()
        {
            return this.includeInShipping;
        }

        public void setIncludeInShipping(String includeInShipping)
        {
            this.includeInShipping = includeInShipping;
        }

        private String isManual;

        public String getIsManual()
        {
            return this.isManual;
        }

        public void setIsManual(String isManual)
        {
            this.isManual = isManual;
        }

        private String originalAdjustmentId;

        public String getOriginalAdjustmentId()
        {
            return this.originalAdjustmentId;
        }

        public void setOriginalAdjustmentId(String originalAdjustmentId)
        {
            this.originalAdjustmentId = originalAdjustmentId;
        }

        private java.math.BigDecimal oldAmountPerQuantity;

        public java.math.BigDecimal getOldAmountPerQuantity()
        {
            return this.oldAmountPerQuantity;
        }

        public void setOldAmountPerQuantity(java.math.BigDecimal oldAmountPerQuantity)
        {
            this.oldAmountPerQuantity = oldAmountPerQuantity;
        }

        private Double oldPercentage;

        public Double getOldPercentage()
        {
            return this.oldPercentage;
        }

        public void setOldPercentage(Double oldPercentage)
        {
            this.oldPercentage = oldPercentage;
        }

        private Boolean active;

        public Boolean getActive()
        {
            return this.active;
        }

        public void setActive(Boolean active)
        {
            this.active = active;
        }

    }

    public static abstract class AbstractCreateOrderAdjustment extends AbstractCreateOrMergePatchOrderAdjustment implements CreateOrderAdjustment
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }

    }

    public static abstract class AbstractMergePatchOrderAdjustment extends AbstractCreateOrMergePatchOrderAdjustment implements MergePatchOrderAdjustment
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_MERGE_PATCH;
        }

        private Boolean isPropertyOrderAdjustmentTypeIdRemoved;

        public Boolean getIsPropertyOrderAdjustmentTypeIdRemoved()
        {
            return this.isPropertyOrderAdjustmentTypeIdRemoved;
        }

        public void setIsPropertyOrderAdjustmentTypeIdRemoved(Boolean removed)
        {
            this.isPropertyOrderAdjustmentTypeIdRemoved = removed;
        }

        private Boolean isPropertyOrderItemSeqIdRemoved;

        public Boolean getIsPropertyOrderItemSeqIdRemoved()
        {
            return this.isPropertyOrderItemSeqIdRemoved;
        }

        public void setIsPropertyOrderItemSeqIdRemoved(Boolean removed)
        {
            this.isPropertyOrderItemSeqIdRemoved = removed;
        }

        private Boolean isPropertyShipGroupSeqIdRemoved;

        public Boolean getIsPropertyShipGroupSeqIdRemoved()
        {
            return this.isPropertyShipGroupSeqIdRemoved;
        }

        public void setIsPropertyShipGroupSeqIdRemoved(Boolean removed)
        {
            this.isPropertyShipGroupSeqIdRemoved = removed;
        }

        private Boolean isPropertyCommentsRemoved;

        public Boolean getIsPropertyCommentsRemoved()
        {
            return this.isPropertyCommentsRemoved;
        }

        public void setIsPropertyCommentsRemoved(Boolean removed)
        {
            this.isPropertyCommentsRemoved = removed;
        }

        private Boolean isPropertyDescriptionRemoved;

        public Boolean getIsPropertyDescriptionRemoved()
        {
            return this.isPropertyDescriptionRemoved;
        }

        public void setIsPropertyDescriptionRemoved(Boolean removed)
        {
            this.isPropertyDescriptionRemoved = removed;
        }

        private Boolean isPropertyAmountRemoved;

        public Boolean getIsPropertyAmountRemoved()
        {
            return this.isPropertyAmountRemoved;
        }

        public void setIsPropertyAmountRemoved(Boolean removed)
        {
            this.isPropertyAmountRemoved = removed;
        }

        private Boolean isPropertyCurrencyUomIdRemoved;

        public Boolean getIsPropertyCurrencyUomIdRemoved()
        {
            return this.isPropertyCurrencyUomIdRemoved;
        }

        public void setIsPropertyCurrencyUomIdRemoved(Boolean removed)
        {
            this.isPropertyCurrencyUomIdRemoved = removed;
        }

        private Boolean isPropertyRecurringAmountRemoved;

        public Boolean getIsPropertyRecurringAmountRemoved()
        {
            return this.isPropertyRecurringAmountRemoved;
        }

        public void setIsPropertyRecurringAmountRemoved(Boolean removed)
        {
            this.isPropertyRecurringAmountRemoved = removed;
        }

        private Boolean isPropertyAmountAlreadyIncludedRemoved;

        public Boolean getIsPropertyAmountAlreadyIncludedRemoved()
        {
            return this.isPropertyAmountAlreadyIncludedRemoved;
        }

        public void setIsPropertyAmountAlreadyIncludedRemoved(Boolean removed)
        {
            this.isPropertyAmountAlreadyIncludedRemoved = removed;
        }

        private Boolean isPropertyProductPromoIdRemoved;

        public Boolean getIsPropertyProductPromoIdRemoved()
        {
            return this.isPropertyProductPromoIdRemoved;
        }

        public void setIsPropertyProductPromoIdRemoved(Boolean removed)
        {
            this.isPropertyProductPromoIdRemoved = removed;
        }

        private Boolean isPropertyProductPromoRuleIdRemoved;

        public Boolean getIsPropertyProductPromoRuleIdRemoved()
        {
            return this.isPropertyProductPromoRuleIdRemoved;
        }

        public void setIsPropertyProductPromoRuleIdRemoved(Boolean removed)
        {
            this.isPropertyProductPromoRuleIdRemoved = removed;
        }

        private Boolean isPropertyProductPromoActionSeqIdRemoved;

        public Boolean getIsPropertyProductPromoActionSeqIdRemoved()
        {
            return this.isPropertyProductPromoActionSeqIdRemoved;
        }

        public void setIsPropertyProductPromoActionSeqIdRemoved(Boolean removed)
        {
            this.isPropertyProductPromoActionSeqIdRemoved = removed;
        }

        private Boolean isPropertyProductFeatureIdRemoved;

        public Boolean getIsPropertyProductFeatureIdRemoved()
        {
            return this.isPropertyProductFeatureIdRemoved;
        }

        public void setIsPropertyProductFeatureIdRemoved(Boolean removed)
        {
            this.isPropertyProductFeatureIdRemoved = removed;
        }

        private Boolean isPropertyCorrespondingProductIdRemoved;

        public Boolean getIsPropertyCorrespondingProductIdRemoved()
        {
            return this.isPropertyCorrespondingProductIdRemoved;
        }

        public void setIsPropertyCorrespondingProductIdRemoved(Boolean removed)
        {
            this.isPropertyCorrespondingProductIdRemoved = removed;
        }

        private Boolean isPropertyTaxAuthorityRateSeqIdRemoved;

        public Boolean getIsPropertyTaxAuthorityRateSeqIdRemoved()
        {
            return this.isPropertyTaxAuthorityRateSeqIdRemoved;
        }

        public void setIsPropertyTaxAuthorityRateSeqIdRemoved(Boolean removed)
        {
            this.isPropertyTaxAuthorityRateSeqIdRemoved = removed;
        }

        private Boolean isPropertySourceReferenceIdRemoved;

        public Boolean getIsPropertySourceReferenceIdRemoved()
        {
            return this.isPropertySourceReferenceIdRemoved;
        }

        public void setIsPropertySourceReferenceIdRemoved(Boolean removed)
        {
            this.isPropertySourceReferenceIdRemoved = removed;
        }

        private Boolean isPropertySourcePercentageRemoved;

        public Boolean getIsPropertySourcePercentageRemoved()
        {
            return this.isPropertySourcePercentageRemoved;
        }

        public void setIsPropertySourcePercentageRemoved(Boolean removed)
        {
            this.isPropertySourcePercentageRemoved = removed;
        }

        private Boolean isPropertyCustomerReferenceIdRemoved;

        public Boolean getIsPropertyCustomerReferenceIdRemoved()
        {
            return this.isPropertyCustomerReferenceIdRemoved;
        }

        public void setIsPropertyCustomerReferenceIdRemoved(Boolean removed)
        {
            this.isPropertyCustomerReferenceIdRemoved = removed;
        }

        private Boolean isPropertyPrimaryGeoIdRemoved;

        public Boolean getIsPropertyPrimaryGeoIdRemoved()
        {
            return this.isPropertyPrimaryGeoIdRemoved;
        }

        public void setIsPropertyPrimaryGeoIdRemoved(Boolean removed)
        {
            this.isPropertyPrimaryGeoIdRemoved = removed;
        }

        private Boolean isPropertySecondaryGeoIdRemoved;

        public Boolean getIsPropertySecondaryGeoIdRemoved()
        {
            return this.isPropertySecondaryGeoIdRemoved;
        }

        public void setIsPropertySecondaryGeoIdRemoved(Boolean removed)
        {
            this.isPropertySecondaryGeoIdRemoved = removed;
        }

        private Boolean isPropertyExemptAmountRemoved;

        public Boolean getIsPropertyExemptAmountRemoved()
        {
            return this.isPropertyExemptAmountRemoved;
        }

        public void setIsPropertyExemptAmountRemoved(Boolean removed)
        {
            this.isPropertyExemptAmountRemoved = removed;
        }

        private Boolean isPropertyTaxAuthGeoIdRemoved;

        public Boolean getIsPropertyTaxAuthGeoIdRemoved()
        {
            return this.isPropertyTaxAuthGeoIdRemoved;
        }

        public void setIsPropertyTaxAuthGeoIdRemoved(Boolean removed)
        {
            this.isPropertyTaxAuthGeoIdRemoved = removed;
        }

        private Boolean isPropertyTaxAuthPartyIdRemoved;

        public Boolean getIsPropertyTaxAuthPartyIdRemoved()
        {
            return this.isPropertyTaxAuthPartyIdRemoved;
        }

        public void setIsPropertyTaxAuthPartyIdRemoved(Boolean removed)
        {
            this.isPropertyTaxAuthPartyIdRemoved = removed;
        }

        private Boolean isPropertyOverrideGlAccountIdRemoved;

        public Boolean getIsPropertyOverrideGlAccountIdRemoved()
        {
            return this.isPropertyOverrideGlAccountIdRemoved;
        }

        public void setIsPropertyOverrideGlAccountIdRemoved(Boolean removed)
        {
            this.isPropertyOverrideGlAccountIdRemoved = removed;
        }

        private Boolean isPropertyIncludeInTaxRemoved;

        public Boolean getIsPropertyIncludeInTaxRemoved()
        {
            return this.isPropertyIncludeInTaxRemoved;
        }

        public void setIsPropertyIncludeInTaxRemoved(Boolean removed)
        {
            this.isPropertyIncludeInTaxRemoved = removed;
        }

        private Boolean isPropertyIncludeInShippingRemoved;

        public Boolean getIsPropertyIncludeInShippingRemoved()
        {
            return this.isPropertyIncludeInShippingRemoved;
        }

        public void setIsPropertyIncludeInShippingRemoved(Boolean removed)
        {
            this.isPropertyIncludeInShippingRemoved = removed;
        }

        private Boolean isPropertyIsManualRemoved;

        public Boolean getIsPropertyIsManualRemoved()
        {
            return this.isPropertyIsManualRemoved;
        }

        public void setIsPropertyIsManualRemoved(Boolean removed)
        {
            this.isPropertyIsManualRemoved = removed;
        }

        private Boolean isPropertyOriginalAdjustmentIdRemoved;

        public Boolean getIsPropertyOriginalAdjustmentIdRemoved()
        {
            return this.isPropertyOriginalAdjustmentIdRemoved;
        }

        public void setIsPropertyOriginalAdjustmentIdRemoved(Boolean removed)
        {
            this.isPropertyOriginalAdjustmentIdRemoved = removed;
        }

        private Boolean isPropertyOldAmountPerQuantityRemoved;

        public Boolean getIsPropertyOldAmountPerQuantityRemoved()
        {
            return this.isPropertyOldAmountPerQuantityRemoved;
        }

        public void setIsPropertyOldAmountPerQuantityRemoved(Boolean removed)
        {
            this.isPropertyOldAmountPerQuantityRemoved = removed;
        }

        private Boolean isPropertyOldPercentageRemoved;

        public Boolean getIsPropertyOldPercentageRemoved()
        {
            return this.isPropertyOldPercentageRemoved;
        }

        public void setIsPropertyOldPercentageRemoved(Boolean removed)
        {
            this.isPropertyOldPercentageRemoved = removed;
        }

        private Boolean isPropertyActiveRemoved;

        public Boolean getIsPropertyActiveRemoved()
        {
            return this.isPropertyActiveRemoved;
        }

        public void setIsPropertyActiveRemoved(Boolean removed)
        {
            this.isPropertyActiveRemoved = removed;
        }


    }

    public static class SimpleCreateOrderAdjustment extends AbstractCreateOrderAdjustment
    {
    }

    
    public static class SimpleMergePatchOrderAdjustment extends AbstractMergePatchOrderAdjustment
    {
    }

    
    public static class SimpleRemoveOrderAdjustment extends AbstractOrderAdjustmentCommand implements RemoveOrderAdjustment
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_REMOVE;
        }
    }

    

}
