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

public interface OrderShipGroupState
{
    Long VERSION_ZERO = 0L;

    Long VERSION_NULL = VERSION_ZERO - 1;

    String getShipGroupSeqId();

    String getShipmentMethodTypeId();

    String getSupplierPartyId();

    String getVendorPartyId();

    String getCarrierPartyId();

    String getCarrierRoleTypeId();

    String getFacilityId();

    String getContactMechId();

    String getTelecomContactMechId();

    String getTrackingNumber();

    String getShippingInstructions();

    String getMaySplit();

    String getGiftMessage();

    String getIsGift();

    OffsetDateTime getShipAfterDate();

    OffsetDateTime getShipByDate();

    OffsetDateTime getEstimatedShipDate();

    OffsetDateTime getEstimatedDeliveryDate();

    String getSupplierCurrencyUomId();

    java.math.BigDecimal getSupplierPayableAmount();

    String getSupplierSyncStatusId();

    String getSupplierSyncCode();

    String getSupplierSyncMessage();

    Long getVersion();

    String getCreatedBy();

    OffsetDateTime getCreatedAt();

    String getUpdatedBy();

    OffsetDateTime getUpdatedAt();

    Boolean getDeleted();

    String getOrderId();

    EntityStateCollection<String, OrderItemShipGroupAssociationState> getOrderItemShipGroupAssociations();

    interface MutableOrderShipGroupState extends OrderShipGroupState {
        void setShipGroupSeqId(String shipGroupSeqId);

        void setShipmentMethodTypeId(String shipmentMethodTypeId);

        void setSupplierPartyId(String supplierPartyId);

        void setVendorPartyId(String vendorPartyId);

        void setCarrierPartyId(String carrierPartyId);

        void setCarrierRoleTypeId(String carrierRoleTypeId);

        void setFacilityId(String facilityId);

        void setContactMechId(String contactMechId);

        void setTelecomContactMechId(String telecomContactMechId);

        void setTrackingNumber(String trackingNumber);

        void setShippingInstructions(String shippingInstructions);

        void setMaySplit(String maySplit);

        void setGiftMessage(String giftMessage);

        void setIsGift(String isGift);

        void setShipAfterDate(OffsetDateTime shipAfterDate);

        void setShipByDate(OffsetDateTime shipByDate);

        void setEstimatedShipDate(OffsetDateTime estimatedShipDate);

        void setEstimatedDeliveryDate(OffsetDateTime estimatedDeliveryDate);

        void setSupplierCurrencyUomId(String supplierCurrencyUomId);

        void setSupplierPayableAmount(java.math.BigDecimal supplierPayableAmount);

        void setSupplierSyncStatusId(String supplierSyncStatusId);

        void setSupplierSyncCode(String supplierSyncCode);

        void setSupplierSyncMessage(String supplierSyncMessage);

        void setVersion(Long version);

        void setCreatedBy(String createdBy);

        void setCreatedAt(OffsetDateTime createdAt);

        void setUpdatedBy(String updatedBy);

        void setUpdatedAt(OffsetDateTime updatedAt);

        void setDeleted(Boolean deleted);

        void setOrderId(String orderId);


        void mutate(Event e);

        //void when(OrderShipGroupEvent.OrderShipGroupStateCreated e);

        //void when(OrderShipGroupEvent.OrderShipGroupStateMergePatched e);

        //void when(OrderShipGroupEvent.OrderShipGroupStateRemoved e);
    }

    interface SqlOrderShipGroupState extends MutableOrderShipGroupState {
        OrderShipGroupId getOrderShipGroupId();

        void setOrderShipGroupId(OrderShipGroupId orderShipGroupId);


        boolean isStateUnsaved();

        boolean getForReapplying();
    }
}
