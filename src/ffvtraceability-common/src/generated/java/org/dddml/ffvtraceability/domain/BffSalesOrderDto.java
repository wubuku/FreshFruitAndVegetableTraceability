// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain;

import java.io.Serializable;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public class BffSalesOrderDto implements Serializable {
    private String orderId;

    public String getOrderId()
    {
        return this.orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    private String orderName;

    public String getOrderName()
    {
        return this.orderName;
    }

    public void setOrderName(String orderName)
    {
        this.orderName = orderName;
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

    private OffsetDateTime orderDate;

    public OffsetDateTime getOrderDate()
    {
        return this.orderDate;
    }

    public void setOrderDate(OffsetDateTime orderDate)
    {
        this.orderDate = orderDate;
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

    private String currencyUomId;

    public String getCurrencyUomId()
    {
        return this.currencyUomId;
    }

    public void setCurrencyUomId(String currencyUomId)
    {
        this.currencyUomId = currencyUomId;
    }

    private String syncStatusId;

    public String getSyncStatusId()
    {
        return this.syncStatusId;
    }

    public void setSyncStatusId(String syncStatusId)
    {
        this.syncStatusId = syncStatusId;
    }

    private String originFacilityId;

    public String getOriginFacilityId()
    {
        return this.originFacilityId;
    }

    public void setOriginFacilityId(String originFacilityId)
    {
        this.originFacilityId = originFacilityId;
    }

    private String memo;

    public String getMemo()
    {
        return this.memo;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    private String contactDescription;

    public String getContactDescription()
    {
        return this.contactDescription;
    }

    public void setContactDescription(String contactDescription)
    {
        this.contactDescription = contactDescription;
    }

    private String fulfillmentStatusId;

    public String getFulfillmentStatusId()
    {
        return this.fulfillmentStatusId;
    }

    public void setFulfillmentStatusId(String fulfillmentStatusId)
    {
        this.fulfillmentStatusId = fulfillmentStatusId;
    }

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

    private OffsetDateTime createdAt;

    public OffsetDateTime getCreatedAt()
    {
        return this.createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    private String createdBy;

    public String getCreatedBy()
    {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    private OffsetDateTime updatedAt;

    public OffsetDateTime getUpdatedAt()
    {
        return this.updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    private String updatedBy;

    public String getUpdatedBy()
    {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy)
    {
        this.updatedBy = updatedBy;
    }

    private java.util.List<BffSalesOrderItemDto> orderItems;

    public java.util.List<BffSalesOrderItemDto> getOrderItems() {
        return this.orderItems;
    }

    public void setOrderItems(java.util.List<BffSalesOrderItemDto> orderItems) {
        this.orderItems = orderItems;
    }

    public BffSalesOrderDto()
    {
    }

    public BffSalesOrderDto(String orderId, String orderName, String externalId, OffsetDateTime orderDate, String statusId, String currencyUomId, String syncStatusId, String originFacilityId, String memo, String contactDescription, String fulfillmentStatusId, String customerId, String customerName, OffsetDateTime createdAt, String createdBy, OffsetDateTime updatedAt, String updatedBy, java.util.List<BffSalesOrderItemDto> orderItems)
    {
        this.orderId = orderId;
        this.orderName = orderName;
        this.externalId = externalId;
        this.orderDate = orderDate;
        this.statusId = statusId;
        this.currencyUomId = currencyUomId;
        this.syncStatusId = syncStatusId;
        this.originFacilityId = originFacilityId;
        this.memo = memo;
        this.contactDescription = contactDescription;
        this.fulfillmentStatusId = fulfillmentStatusId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.orderItems = orderItems;
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

        BffSalesOrderDto other = (BffSalesOrderDto)obj;
        return true 
            && (orderId == other.orderId || (orderId != null && orderId.equals(other.orderId)))
            && (orderName == other.orderName || (orderName != null && orderName.equals(other.orderName)))
            && (externalId == other.externalId || (externalId != null && externalId.equals(other.externalId)))
            && (orderDate == other.orderDate || (orderDate != null && orderDate.equals(other.orderDate)))
            && (statusId == other.statusId || (statusId != null && statusId.equals(other.statusId)))
            && (currencyUomId == other.currencyUomId || (currencyUomId != null && currencyUomId.equals(other.currencyUomId)))
            && (syncStatusId == other.syncStatusId || (syncStatusId != null && syncStatusId.equals(other.syncStatusId)))
            && (originFacilityId == other.originFacilityId || (originFacilityId != null && originFacilityId.equals(other.originFacilityId)))
            && (memo == other.memo || (memo != null && memo.equals(other.memo)))
            && (contactDescription == other.contactDescription || (contactDescription != null && contactDescription.equals(other.contactDescription)))
            && (fulfillmentStatusId == other.fulfillmentStatusId || (fulfillmentStatusId != null && fulfillmentStatusId.equals(other.fulfillmentStatusId)))
            && (customerId == other.customerId || (customerId != null && customerId.equals(other.customerId)))
            && (customerName == other.customerName || (customerName != null && customerName.equals(other.customerName)))
            && (createdAt == other.createdAt || (createdAt != null && createdAt.equals(other.createdAt)))
            && (createdBy == other.createdBy || (createdBy != null && createdBy.equals(other.createdBy)))
            && (updatedAt == other.updatedAt || (updatedAt != null && updatedAt.equals(other.updatedAt)))
            && (updatedBy == other.updatedBy || (updatedBy != null && updatedBy.equals(other.updatedBy)))
            && (orderItems == other.orderItems || (orderItems != null && orderItems.equals(other.orderItems)))
            ;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        if (this.orderId != null) {
            hash += 13 * this.orderId.hashCode();
        }
        if (this.orderName != null) {
            hash += 13 * this.orderName.hashCode();
        }
        if (this.externalId != null) {
            hash += 13 * this.externalId.hashCode();
        }
        if (this.orderDate != null) {
            hash += 13 * this.orderDate.hashCode();
        }
        if (this.statusId != null) {
            hash += 13 * this.statusId.hashCode();
        }
        if (this.currencyUomId != null) {
            hash += 13 * this.currencyUomId.hashCode();
        }
        if (this.syncStatusId != null) {
            hash += 13 * this.syncStatusId.hashCode();
        }
        if (this.originFacilityId != null) {
            hash += 13 * this.originFacilityId.hashCode();
        }
        if (this.memo != null) {
            hash += 13 * this.memo.hashCode();
        }
        if (this.contactDescription != null) {
            hash += 13 * this.contactDescription.hashCode();
        }
        if (this.fulfillmentStatusId != null) {
            hash += 13 * this.fulfillmentStatusId.hashCode();
        }
        if (this.customerId != null) {
            hash += 13 * this.customerId.hashCode();
        }
        if (this.customerName != null) {
            hash += 13 * this.customerName.hashCode();
        }
        if (this.createdAt != null) {
            hash += 13 * this.createdAt.hashCode();
        }
        if (this.createdBy != null) {
            hash += 13 * this.createdBy.hashCode();
        }
        if (this.updatedAt != null) {
            hash += 13 * this.updatedAt.hashCode();
        }
        if (this.updatedBy != null) {
            hash += 13 * this.updatedBy.hashCode();
        }
        if (this.orderItems != null) {
            hash += 13 * this.orderItems.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "BffSalesOrderDto{" +
                "orderId=" + '\'' + orderId + '\'' +
                ", orderName=" + '\'' + orderName + '\'' +
                ", externalId=" + '\'' + externalId + '\'' +
                ", orderDate=" + orderDate +
                ", statusId=" + '\'' + statusId + '\'' +
                ", currencyUomId=" + '\'' + currencyUomId + '\'' +
                ", syncStatusId=" + '\'' + syncStatusId + '\'' +
                ", originFacilityId=" + '\'' + originFacilityId + '\'' +
                ", memo=" + '\'' + memo + '\'' +
                ", contactDescription=" + '\'' + contactDescription + '\'' +
                ", fulfillmentStatusId=" + '\'' + fulfillmentStatusId + '\'' +
                ", customerId=" + '\'' + customerId + '\'' +
                ", customerName=" + '\'' + customerName + '\'' +
                ", createdAt=" + createdAt +
                ", createdBy=" + '\'' + createdBy + '\'' +
                ", updatedAt=" + updatedAt +
                ", updatedBy=" + '\'' + updatedBy + '\'' +
                ", orderItems=" + orderItems +
                '}';
    }


}

