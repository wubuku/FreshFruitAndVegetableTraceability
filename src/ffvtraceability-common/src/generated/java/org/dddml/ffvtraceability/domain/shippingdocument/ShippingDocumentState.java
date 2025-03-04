// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.shippingdocument;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface ShippingDocumentState
{
    Long VERSION_ZERO = 0L;

    Long VERSION_NULL = VERSION_ZERO - 1;

    String getDocumentId();

    String getShipmentId();

    String getShipmentItemSeqId();

    String getShipmentPackageSeqId();

    String getDescription();

    Long getVersion();

    String getCreatedBy();

    OffsetDateTime getCreatedAt();

    String getUpdatedBy();

    OffsetDateTime getUpdatedAt();

    Boolean getDeleted();

    String getTenantId();

    interface MutableShippingDocumentState extends ShippingDocumentState {
        void setDocumentId(String documentId);

        void setShipmentId(String shipmentId);

        void setShipmentItemSeqId(String shipmentItemSeqId);

        void setShipmentPackageSeqId(String shipmentPackageSeqId);

        void setDescription(String description);

        void setVersion(Long version);

        void setCreatedBy(String createdBy);

        void setCreatedAt(OffsetDateTime createdAt);

        void setUpdatedBy(String updatedBy);

        void setUpdatedAt(OffsetDateTime updatedAt);

        void setDeleted(Boolean deleted);

        void setTenantId(String tenantId);


        void mutate(Event e);

        //void when(ShippingDocumentEvent.ShippingDocumentStateCreated e);

        //void when(ShippingDocumentEvent.ShippingDocumentStateMergePatched e);

        //void when(ShippingDocumentEvent.ShippingDocumentStateDeleted e);
    }

    interface SqlShippingDocumentState extends MutableShippingDocumentState {

        boolean isStateUnsaved();

        boolean getForReapplying();
    }
}

