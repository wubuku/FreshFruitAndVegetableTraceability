// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain;

import java.io.Serializable;
import org.dddml.ffvtraceability.domain.*;

public class BffPurchaseOrderDto implements Serializable {
    private String orderId;

    public String getOrderId()
    {
        return this.orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    private java.util.List<BffPurchaseOrderItemDto> orderItems;

    public java.util.List<BffPurchaseOrderItemDto> getOrderItems() {
        return this.orderItems;
    }

    public void setOrderItems(java.util.List<BffPurchaseOrderItemDto> orderItems) {
        this.orderItems = orderItems;
    }

    public BffPurchaseOrderDto()
    {
    }

    public BffPurchaseOrderDto(String orderId, java.util.List<BffPurchaseOrderItemDto> orderItems)
    {
        this.orderId = orderId;
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

        BffPurchaseOrderDto other = (BffPurchaseOrderDto)obj;
        return true 
            && (orderId == other.orderId || (orderId != null && orderId.equals(other.orderId)))
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
        if (this.orderItems != null) {
            hash += 13 * this.orderItems.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "BffPurchaseOrderDto{" +
                "orderId=" + '\'' + orderId + '\'' +
                ", orderItems=" + orderItems +
                '}';
    }


}

