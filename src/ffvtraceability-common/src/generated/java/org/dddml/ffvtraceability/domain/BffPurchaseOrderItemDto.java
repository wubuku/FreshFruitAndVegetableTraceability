// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain;

import java.io.Serializable;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public class BffPurchaseOrderItemDto implements Serializable {
    private String orderItemSeqId;

    public String getOrderItemSeqId()
    {
        return this.orderItemSeqId;
    }

    public void setOrderItemSeqId(String orderItemSeqId)
    {
        this.orderItemSeqId = orderItemSeqId;
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

    private String productId;

    public String getProductId()
    {
        return this.productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    private String supplierProductId;

    public String getSupplierProductId()
    {
        return this.supplierProductId;
    }

    public void setSupplierProductId(String supplierProductId)
    {
        this.supplierProductId = supplierProductId;
    }

    private java.math.BigDecimal quantity;

    public java.math.BigDecimal getQuantity()
    {
        return this.quantity;
    }

    public void setQuantity(java.math.BigDecimal quantity)
    {
        this.quantity = quantity;
    }

    private java.math.BigDecimal cancelQuantity;

    public java.math.BigDecimal getCancelQuantity()
    {
        return this.cancelQuantity;
    }

    public void setCancelQuantity(java.math.BigDecimal cancelQuantity)
    {
        this.cancelQuantity = cancelQuantity;
    }

    private java.math.BigDecimal selectedAmount;

    public java.math.BigDecimal getSelectedAmount()
    {
        return this.selectedAmount;
    }

    public void setSelectedAmount(java.math.BigDecimal selectedAmount)
    {
        this.selectedAmount = selectedAmount;
    }

    private java.math.BigDecimal unitPrice;

    public java.math.BigDecimal getUnitPrice()
    {
        return this.unitPrice;
    }

    public void setUnitPrice(java.math.BigDecimal unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    private String itemDescription;

    public String getItemDescription()
    {
        return this.itemDescription;
    }

    public void setItemDescription(String itemDescription)
    {
        this.itemDescription = itemDescription;
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

    private String statusId;

    public String getStatusId()
    {
        return this.statusId;
    }

    public void setStatusId(String statusId)
    {
        this.statusId = statusId;
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

    private OffsetDateTime estimatedShipDate;

    public OffsetDateTime getEstimatedShipDate()
    {
        return this.estimatedShipDate;
    }

    public void setEstimatedShipDate(OffsetDateTime estimatedShipDate)
    {
        this.estimatedShipDate = estimatedShipDate;
    }

    private OffsetDateTime estimatedDeliveryDate;

    public OffsetDateTime getEstimatedDeliveryDate()
    {
        return this.estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(OffsetDateTime estimatedDeliveryDate)
    {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public BffPurchaseOrderItemDto()
    {
    }

    public BffPurchaseOrderItemDto(String orderItemSeqId, String externalId, String productId, String supplierProductId, java.math.BigDecimal quantity, java.math.BigDecimal cancelQuantity, java.math.BigDecimal selectedAmount, java.math.BigDecimal unitPrice, String itemDescription, String comments, String statusId, String syncStatusId, OffsetDateTime estimatedShipDate, OffsetDateTime estimatedDeliveryDate)
    {
        this.orderItemSeqId = orderItemSeqId;
        this.externalId = externalId;
        this.productId = productId;
        this.supplierProductId = supplierProductId;
        this.quantity = quantity;
        this.cancelQuantity = cancelQuantity;
        this.selectedAmount = selectedAmount;
        this.unitPrice = unitPrice;
        this.itemDescription = itemDescription;
        this.comments = comments;
        this.statusId = statusId;
        this.syncStatusId = syncStatusId;
        this.estimatedShipDate = estimatedShipDate;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
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

        BffPurchaseOrderItemDto other = (BffPurchaseOrderItemDto)obj;
        return true 
            && (orderItemSeqId == other.orderItemSeqId || (orderItemSeqId != null && orderItemSeqId.equals(other.orderItemSeqId)))
            && (externalId == other.externalId || (externalId != null && externalId.equals(other.externalId)))
            && (productId == other.productId || (productId != null && productId.equals(other.productId)))
            && (supplierProductId == other.supplierProductId || (supplierProductId != null && supplierProductId.equals(other.supplierProductId)))
            && (quantity == other.quantity || (quantity != null && quantity.equals(other.quantity)))
            && (cancelQuantity == other.cancelQuantity || (cancelQuantity != null && cancelQuantity.equals(other.cancelQuantity)))
            && (selectedAmount == other.selectedAmount || (selectedAmount != null && selectedAmount.equals(other.selectedAmount)))
            && (unitPrice == other.unitPrice || (unitPrice != null && unitPrice.equals(other.unitPrice)))
            && (itemDescription == other.itemDescription || (itemDescription != null && itemDescription.equals(other.itemDescription)))
            && (comments == other.comments || (comments != null && comments.equals(other.comments)))
            && (statusId == other.statusId || (statusId != null && statusId.equals(other.statusId)))
            && (syncStatusId == other.syncStatusId || (syncStatusId != null && syncStatusId.equals(other.syncStatusId)))
            && (estimatedShipDate == other.estimatedShipDate || (estimatedShipDate != null && estimatedShipDate.equals(other.estimatedShipDate)))
            && (estimatedDeliveryDate == other.estimatedDeliveryDate || (estimatedDeliveryDate != null && estimatedDeliveryDate.equals(other.estimatedDeliveryDate)))
            ;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        if (this.orderItemSeqId != null) {
            hash += 13 * this.orderItemSeqId.hashCode();
        }
        if (this.externalId != null) {
            hash += 13 * this.externalId.hashCode();
        }
        if (this.productId != null) {
            hash += 13 * this.productId.hashCode();
        }
        if (this.supplierProductId != null) {
            hash += 13 * this.supplierProductId.hashCode();
        }
        if (this.quantity != null) {
            hash += 13 * this.quantity.hashCode();
        }
        if (this.cancelQuantity != null) {
            hash += 13 * this.cancelQuantity.hashCode();
        }
        if (this.selectedAmount != null) {
            hash += 13 * this.selectedAmount.hashCode();
        }
        if (this.unitPrice != null) {
            hash += 13 * this.unitPrice.hashCode();
        }
        if (this.itemDescription != null) {
            hash += 13 * this.itemDescription.hashCode();
        }
        if (this.comments != null) {
            hash += 13 * this.comments.hashCode();
        }
        if (this.statusId != null) {
            hash += 13 * this.statusId.hashCode();
        }
        if (this.syncStatusId != null) {
            hash += 13 * this.syncStatusId.hashCode();
        }
        if (this.estimatedShipDate != null) {
            hash += 13 * this.estimatedShipDate.hashCode();
        }
        if (this.estimatedDeliveryDate != null) {
            hash += 13 * this.estimatedDeliveryDate.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "BffPurchaseOrderItemDto{" +
                "orderItemSeqId=" + '\'' + orderItemSeqId + '\'' +
                ", externalId=" + '\'' + externalId + '\'' +
                ", productId=" + '\'' + productId + '\'' +
                ", supplierProductId=" + '\'' + supplierProductId + '\'' +
                ", quantity=" + quantity +
                ", cancelQuantity=" + cancelQuantity +
                ", selectedAmount=" + selectedAmount +
                ", unitPrice=" + unitPrice +
                ", itemDescription=" + '\'' + itemDescription + '\'' +
                ", comments=" + '\'' + comments + '\'' +
                ", statusId=" + '\'' + statusId + '\'' +
                ", syncStatusId=" + '\'' + syncStatusId + '\'' +
                ", estimatedShipDate=" + estimatedShipDate +
                ", estimatedDeliveryDate=" + estimatedDeliveryDate +
                '}';
    }


}
