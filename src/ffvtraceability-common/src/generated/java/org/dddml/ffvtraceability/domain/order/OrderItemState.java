// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.order;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface OrderItemState
{
    Long VERSION_ZERO = 0L;

    Long VERSION_NULL = VERSION_ZERO - 1;

    String getOrderItemSeqId();

    String getExternalId();

    String getOrderItemTypeId();

    String getOrderItemGroupSeqId();

    String getIsItemGroupPrimary();

    String getFromInventoryItemId();

    String getBudgetId();

    String getBudgetItemSeqId();

    String getProductId();

    String getSupplierProductId();

    String getProductFeatureId();

    String getProdCatalogId();

    String getProductCategoryId();

    String getIsPromo();

    String getQuoteId();

    String getQuoteItemSeqId();

    String getShoppingListId();

    String getShoppingListItemSeqId();

    String getSubscriptionId();

    String getDeploymentId();

    java.math.BigDecimal getQuantity();

    java.math.BigDecimal getCancelQuantity();

    java.math.BigDecimal getSelectedAmount();

    java.math.BigDecimal getUnitPrice();

    java.math.BigDecimal getUnitListPrice();

    java.math.BigDecimal getUnitAverageCost();

    java.math.BigDecimal getUnitRecurringPrice();

    String getIsModifiedPrice();

    String getRecurringFreqUomId();

    String getItemDescription();

    String getComments();

    String getCorrespondingPoId();

    String getStatusId();

    String getSyncStatusId();

    String getFulfillmentStatusId();

    OffsetDateTime getEstimatedShipDate();

    OffsetDateTime getEstimatedDeliveryDate();

    OffsetDateTime getAutoCancelDate();

    OffsetDateTime getShipBeforeDate();

    OffsetDateTime getShipAfterDate();

    OffsetDateTime getCancelBackOrderDate();

    String getOverrideGlAccountId();

    String getSalesOpportunityId();

    Long getVersion();

    String getCreatedBy();

    OffsetDateTime getCreatedAt();

    String getUpdatedBy();

    OffsetDateTime getUpdatedAt();

    Boolean get__Deleted__();

    String getOrderId();

    interface MutableOrderItemState extends OrderItemState {
        void setOrderItemSeqId(String orderItemSeqId);

        void setExternalId(String externalId);

        void setOrderItemTypeId(String orderItemTypeId);

        void setOrderItemGroupSeqId(String orderItemGroupSeqId);

        void setIsItemGroupPrimary(String isItemGroupPrimary);

        void setFromInventoryItemId(String fromInventoryItemId);

        void setBudgetId(String budgetId);

        void setBudgetItemSeqId(String budgetItemSeqId);

        void setProductId(String productId);

        void setSupplierProductId(String supplierProductId);

        void setProductFeatureId(String productFeatureId);

        void setProdCatalogId(String prodCatalogId);

        void setProductCategoryId(String productCategoryId);

        void setIsPromo(String isPromo);

        void setQuoteId(String quoteId);

        void setQuoteItemSeqId(String quoteItemSeqId);

        void setShoppingListId(String shoppingListId);

        void setShoppingListItemSeqId(String shoppingListItemSeqId);

        void setSubscriptionId(String subscriptionId);

        void setDeploymentId(String deploymentId);

        void setQuantity(java.math.BigDecimal quantity);

        void setCancelQuantity(java.math.BigDecimal cancelQuantity);

        void setSelectedAmount(java.math.BigDecimal selectedAmount);

        void setUnitPrice(java.math.BigDecimal unitPrice);

        void setUnitListPrice(java.math.BigDecimal unitListPrice);

        void setUnitAverageCost(java.math.BigDecimal unitAverageCost);

        void setUnitRecurringPrice(java.math.BigDecimal unitRecurringPrice);

        void setIsModifiedPrice(String isModifiedPrice);

        void setRecurringFreqUomId(String recurringFreqUomId);

        void setItemDescription(String itemDescription);

        void setComments(String comments);

        void setCorrespondingPoId(String correspondingPoId);

        void setStatusId(String statusId);

        void setSyncStatusId(String syncStatusId);

        void setFulfillmentStatusId(String fulfillmentStatusId);

        void setEstimatedShipDate(OffsetDateTime estimatedShipDate);

        void setEstimatedDeliveryDate(OffsetDateTime estimatedDeliveryDate);

        void setAutoCancelDate(OffsetDateTime autoCancelDate);

        void setShipBeforeDate(OffsetDateTime shipBeforeDate);

        void setShipAfterDate(OffsetDateTime shipAfterDate);

        void setCancelBackOrderDate(OffsetDateTime cancelBackOrderDate);

        void setOverrideGlAccountId(String overrideGlAccountId);

        void setSalesOpportunityId(String salesOpportunityId);

        void setVersion(Long version);

        void setCreatedBy(String createdBy);

        void setCreatedAt(OffsetDateTime createdAt);

        void setUpdatedBy(String updatedBy);

        void setUpdatedAt(OffsetDateTime updatedAt);

        void set__Deleted__(Boolean __Deleted__);

        void setOrderId(String orderId);


        void mutate(Event e);

        //void when(OrderItemEvent.OrderItemStateCreated e);

        //void when(OrderItemEvent.OrderItemStateMergePatched e);

        //void when(OrderItemEvent.OrderItemStateRemoved e);
    }

    interface SqlOrderItemState extends MutableOrderItemState {
        OrderItemId getOrderItemId();

        void setOrderItemId(OrderItemId orderItemId);


        boolean isStateUnsaved();

        boolean getForReapplying();
    }
}

