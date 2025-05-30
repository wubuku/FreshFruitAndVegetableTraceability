// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.order;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.partyrole.*;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.specialization.DomainError;

public interface OrderCommand extends Command {

    String getOrderId();

    void setOrderId(String orderId);

    Long getVersion();

    void setVersion(Long version);

    static void throwOnInvalidStateTransition(OrderHeaderState state, Command c) {
        if (state.getVersion() == null) {
            if (isCreationCommand((OrderCommand)c)) {
                return;
            }
            throw DomainError.named("premature", "Can't do anything to unexistent aggregate");
        }
        if (isCreationCommand((OrderCommand)c))
            throw DomainError.named("rebirth", "Can't create aggregate that already exists");
    }

    static boolean isCreationCommand(OrderCommand c) {
        if ((c instanceof OrderCommand.CreateOrder) 
            && (COMMAND_TYPE_CREATE.equals(c.getCommandType()) || c.getVersion().equals(OrderHeaderState.VERSION_NULL)))
            return true;
        if ((c instanceof OrderCommand.MergePatchOrder))
            return false;
        if (c.getCommandType() != null) {
            String commandType = c.getCommandType();
            if (commandType.equals("UpdateFulfillmentStatus"))
                return false;
        }

        if (c.getVersion().equals(OrderHeaderState.VERSION_NULL))
            return true;
        return false;
    }

    interface CreateOrMergePatchOrder extends OrderCommand {
        String getOrderTypeId();

        void setOrderTypeId(String orderTypeId);

        String getOrderName();

        void setOrderName(String orderName);

        String getExternalId();

        void setExternalId(String externalId);

        String getSalesChannelEnumId();

        void setSalesChannelEnumId(String salesChannelEnumId);

        OffsetDateTime getOrderDate();

        void setOrderDate(OffsetDateTime orderDate);

        String getPriority();

        void setPriority(String priority);

        OffsetDateTime getEntryDate();

        void setEntryDate(OffsetDateTime entryDate);

        OffsetDateTime getPickSheetPrintedDate();

        void setPickSheetPrintedDate(OffsetDateTime pickSheetPrintedDate);

        String getVisitId();

        void setVisitId(String visitId);

        String getStatusId();

        void setStatusId(String statusId);

        String getFirstAttemptOrderId();

        void setFirstAttemptOrderId(String firstAttemptOrderId);

        String getCurrencyUomId();

        void setCurrencyUomId(String currencyUomId);

        String getSyncStatusId();

        void setSyncStatusId(String syncStatusId);

        String getBillingAccountId();

        void setBillingAccountId(String billingAccountId);

        String getOriginFacilityId();

        void setOriginFacilityId(String originFacilityId);

        String getProductStoreId();

        void setProductStoreId(String productStoreId);

        String getTerminalId();

        void setTerminalId(String terminalId);

        String getTransactionId();

        void setTransactionId(String transactionId);

        String getAutoOrderShoppingListId();

        void setAutoOrderShoppingListId(String autoOrderShoppingListId);

        String getNeedsInventoryIssuance();

        void setNeedsInventoryIssuance(String needsInventoryIssuance);

        String getIsRushOrder();

        void setIsRushOrder(String isRushOrder);

        String getInternalCode();

        void setInternalCode(String internalCode);

        java.math.BigDecimal getRemainingSubTotal();

        void setRemainingSubTotal(java.math.BigDecimal remainingSubTotal);

        java.math.BigDecimal getGrandTotal();

        void setGrandTotal(java.math.BigDecimal grandTotal);

        String getIsViewed();

        void setIsViewed(String isViewed);

        String getInvoicePerShipment();

        void setInvoicePerShipment(String invoicePerShipment);

        String getMemo();

        void setMemo(String memo);

        String getContactDescription();

        void setContactDescription(String contactDescription);

        String getFulfillmentStatusId();

        void setFulfillmentStatusId(String fulfillmentStatusId);

    }

    interface CreateOrder extends CreateOrMergePatchOrder {
        CreateOrderRoleCommandCollection getCreateOrderRoleCommands();

        OrderRoleCommand.CreateOrderRole newCreateOrderRole();

        CreateOrderContactMechCommandCollection getCreateOrderContactMechCommands();

        OrderContactMechCommand.CreateOrderContactMech newCreateOrderContactMech();

        CreateOrderItemCommandCollection getCreateOrderItemCommands();

        OrderItemCommand.CreateOrderItem newCreateOrderItem();

        CreateOrderAdjustmentCommandCollection getCreateOrderAdjustmentCommands();

        OrderAdjustmentCommand.CreateOrderAdjustment newCreateOrderAdjustment();

        CreateOrderShipGroupCommandCollection getCreateOrderShipGroupCommands();

        OrderShipGroupCommand.CreateOrderShipGroup newCreateOrderShipGroup();

    }

    interface MergePatchOrder extends CreateOrMergePatchOrder {
        Boolean getIsPropertyOrderTypeIdRemoved();

        void setIsPropertyOrderTypeIdRemoved(Boolean removed);

        Boolean getIsPropertyOrderNameRemoved();

        void setIsPropertyOrderNameRemoved(Boolean removed);

        Boolean getIsPropertyExternalIdRemoved();

        void setIsPropertyExternalIdRemoved(Boolean removed);

        Boolean getIsPropertySalesChannelEnumIdRemoved();

        void setIsPropertySalesChannelEnumIdRemoved(Boolean removed);

        Boolean getIsPropertyOrderDateRemoved();

        void setIsPropertyOrderDateRemoved(Boolean removed);

        Boolean getIsPropertyPriorityRemoved();

        void setIsPropertyPriorityRemoved(Boolean removed);

        Boolean getIsPropertyEntryDateRemoved();

        void setIsPropertyEntryDateRemoved(Boolean removed);

        Boolean getIsPropertyPickSheetPrintedDateRemoved();

        void setIsPropertyPickSheetPrintedDateRemoved(Boolean removed);

        Boolean getIsPropertyVisitIdRemoved();

        void setIsPropertyVisitIdRemoved(Boolean removed);

        Boolean getIsPropertyStatusIdRemoved();

        void setIsPropertyStatusIdRemoved(Boolean removed);

        Boolean getIsPropertyFirstAttemptOrderIdRemoved();

        void setIsPropertyFirstAttemptOrderIdRemoved(Boolean removed);

        Boolean getIsPropertyCurrencyUomIdRemoved();

        void setIsPropertyCurrencyUomIdRemoved(Boolean removed);

        Boolean getIsPropertySyncStatusIdRemoved();

        void setIsPropertySyncStatusIdRemoved(Boolean removed);

        Boolean getIsPropertyBillingAccountIdRemoved();

        void setIsPropertyBillingAccountIdRemoved(Boolean removed);

        Boolean getIsPropertyOriginFacilityIdRemoved();

        void setIsPropertyOriginFacilityIdRemoved(Boolean removed);

        Boolean getIsPropertyProductStoreIdRemoved();

        void setIsPropertyProductStoreIdRemoved(Boolean removed);

        Boolean getIsPropertyTerminalIdRemoved();

        void setIsPropertyTerminalIdRemoved(Boolean removed);

        Boolean getIsPropertyTransactionIdRemoved();

        void setIsPropertyTransactionIdRemoved(Boolean removed);

        Boolean getIsPropertyAutoOrderShoppingListIdRemoved();

        void setIsPropertyAutoOrderShoppingListIdRemoved(Boolean removed);

        Boolean getIsPropertyNeedsInventoryIssuanceRemoved();

        void setIsPropertyNeedsInventoryIssuanceRemoved(Boolean removed);

        Boolean getIsPropertyIsRushOrderRemoved();

        void setIsPropertyIsRushOrderRemoved(Boolean removed);

        Boolean getIsPropertyInternalCodeRemoved();

        void setIsPropertyInternalCodeRemoved(Boolean removed);

        Boolean getIsPropertyRemainingSubTotalRemoved();

        void setIsPropertyRemainingSubTotalRemoved(Boolean removed);

        Boolean getIsPropertyGrandTotalRemoved();

        void setIsPropertyGrandTotalRemoved(Boolean removed);

        Boolean getIsPropertyIsViewedRemoved();

        void setIsPropertyIsViewedRemoved(Boolean removed);

        Boolean getIsPropertyInvoicePerShipmentRemoved();

        void setIsPropertyInvoicePerShipmentRemoved(Boolean removed);

        Boolean getIsPropertyMemoRemoved();

        void setIsPropertyMemoRemoved(Boolean removed);

        Boolean getIsPropertyContactDescriptionRemoved();

        void setIsPropertyContactDescriptionRemoved(Boolean removed);

        Boolean getIsPropertyFulfillmentStatusIdRemoved();

        void setIsPropertyFulfillmentStatusIdRemoved(Boolean removed);


        OrderRoleCommandCollection getOrderRoleCommands();

        OrderRoleCommand.CreateOrderRole newCreateOrderRole();

        OrderRoleCommand.MergePatchOrderRole newMergePatchOrderRole();

        OrderRoleCommand.RemoveOrderRole newRemoveOrderRole();

        OrderContactMechCommandCollection getOrderContactMechCommands();

        OrderContactMechCommand.CreateOrderContactMech newCreateOrderContactMech();

        OrderContactMechCommand.MergePatchOrderContactMech newMergePatchOrderContactMech();

        OrderContactMechCommand.RemoveOrderContactMech newRemoveOrderContactMech();

        OrderItemCommandCollection getOrderItemCommands();

        OrderItemCommand.CreateOrderItem newCreateOrderItem();

        OrderItemCommand.MergePatchOrderItem newMergePatchOrderItem();

        OrderItemCommand.RemoveOrderItem newRemoveOrderItem();

        OrderAdjustmentCommandCollection getOrderAdjustmentCommands();

        OrderAdjustmentCommand.CreateOrderAdjustment newCreateOrderAdjustment();

        OrderAdjustmentCommand.MergePatchOrderAdjustment newMergePatchOrderAdjustment();

        OrderAdjustmentCommand.RemoveOrderAdjustment newRemoveOrderAdjustment();

        OrderShipGroupCommandCollection getOrderShipGroupCommands();

        OrderShipGroupCommand.CreateOrderShipGroup newCreateOrderShipGroup();

        OrderShipGroupCommand.MergePatchOrderShipGroup newMergePatchOrderShipGroup();

        OrderShipGroupCommand.RemoveOrderShipGroup newRemoveOrderShipGroup();

    }

    interface DeleteOrder extends OrderCommand {
    }

    interface CreateOrderRoleCommandCollection extends Iterable<OrderRoleCommand.CreateOrderRole> {
        void add(OrderRoleCommand.CreateOrderRole c);

        void remove(OrderRoleCommand.CreateOrderRole c);

        void clear();
    }

    interface OrderRoleCommandCollection extends Iterable<OrderRoleCommand> {
        void add(OrderRoleCommand c);

        void remove(OrderRoleCommand c);

        void clear();
    }

    interface CreateOrderContactMechCommandCollection extends Iterable<OrderContactMechCommand.CreateOrderContactMech> {
        void add(OrderContactMechCommand.CreateOrderContactMech c);

        void remove(OrderContactMechCommand.CreateOrderContactMech c);

        void clear();
    }

    interface OrderContactMechCommandCollection extends Iterable<OrderContactMechCommand> {
        void add(OrderContactMechCommand c);

        void remove(OrderContactMechCommand c);

        void clear();
    }

    interface CreateOrderItemCommandCollection extends Iterable<OrderItemCommand.CreateOrderItem> {
        void add(OrderItemCommand.CreateOrderItem c);

        void remove(OrderItemCommand.CreateOrderItem c);

        void clear();
    }

    interface OrderItemCommandCollection extends Iterable<OrderItemCommand> {
        void add(OrderItemCommand c);

        void remove(OrderItemCommand c);

        void clear();
    }

    interface CreateOrderAdjustmentCommandCollection extends Iterable<OrderAdjustmentCommand.CreateOrderAdjustment> {
        void add(OrderAdjustmentCommand.CreateOrderAdjustment c);

        void remove(OrderAdjustmentCommand.CreateOrderAdjustment c);

        void clear();
    }

    interface OrderAdjustmentCommandCollection extends Iterable<OrderAdjustmentCommand> {
        void add(OrderAdjustmentCommand c);

        void remove(OrderAdjustmentCommand c);

        void clear();
    }

    interface CreateOrderShipGroupCommandCollection extends Iterable<OrderShipGroupCommand.CreateOrderShipGroup> {
        void add(OrderShipGroupCommand.CreateOrderShipGroup c);

        void remove(OrderShipGroupCommand.CreateOrderShipGroup c);

        void clear();
    }

    interface OrderShipGroupCommandCollection extends Iterable<OrderShipGroupCommand> {
        void add(OrderShipGroupCommand c);

        void remove(OrderShipGroupCommand c);

        void clear();
    }

}

