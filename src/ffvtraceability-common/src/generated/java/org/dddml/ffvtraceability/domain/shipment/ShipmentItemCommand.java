// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.shipment;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.specialization.DomainError;

public interface ShipmentItemCommand extends Command {

    String getShipmentItemSeqId();

    void setShipmentItemSeqId(String shipmentItemSeqId);

    interface CreateOrMergePatchShipmentItem extends ShipmentItemCommand
    {

        String getProductId();

        void setProductId(String productId);

        java.math.BigDecimal getQuantity();

        void setQuantity(java.math.BigDecimal quantity);

        String getShipmentContentDescription();

        void setShipmentContentDescription(String shipmentContentDescription);

        Boolean getActive();

        void setActive(Boolean active);

    }

    interface CreateShipmentItem extends CreateOrMergePatchShipmentItem
    {
    }

    interface MergePatchShipmentItem extends CreateOrMergePatchShipmentItem
    {
        Boolean getIsPropertyProductIdRemoved();

        void setIsPropertyProductIdRemoved(Boolean removed);

        Boolean getIsPropertyQuantityRemoved();

        void setIsPropertyQuantityRemoved(Boolean removed);

        Boolean getIsPropertyShipmentContentDescriptionRemoved();

        void setIsPropertyShipmentContentDescriptionRemoved(Boolean removed);

        Boolean getIsPropertyActiveRemoved();

        void setIsPropertyActiveRemoved(Boolean removed);


    }

    interface RemoveShipmentItem extends ShipmentItemCommand
    {
    }

}
