// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import org.dddml.ffvtraceability.domain.*;

public class KdeQuantityAndUom implements Serializable {
    private BigDecimal quantity;

    public BigDecimal getQuantity()
    {
        return this.quantity;
    }

    public void setQuantity(BigDecimal quantity)
    {
        this.quantity = quantity;
    }

    private String uom;

    public String getUom()
    {
        return this.uom;
    }

    public void setUom(String uom)
    {
        this.uom = uom;
    }

    public KdeQuantityAndUom()
    {
    }

    public KdeQuantityAndUom(BigDecimal quantity, String uom)
    {
        this.quantity = quantity;
        this.uom = uom;
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

        KdeQuantityAndUom other = (KdeQuantityAndUom)obj;
        return true 
            && (quantity == other.quantity || (quantity != null && quantity.equals(other.quantity)))
            && (uom == other.uom || (uom != null && uom.equals(other.uom)))
            ;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        if (this.quantity != null) {
            hash += 13 * this.quantity.hashCode();
        }
        if (this.uom != null) {
            hash += 13 * this.uom.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "KdeQuantityAndUom{" +
                "quantity=" + quantity +
                ", uom=" + '\'' + uom + '\'' +
                '}';
    }


}
