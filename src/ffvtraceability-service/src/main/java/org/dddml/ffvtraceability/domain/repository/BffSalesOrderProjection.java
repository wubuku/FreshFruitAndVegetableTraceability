// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.repository;

import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public interface BffSalesOrderProjection {
    String getOrderId();

    String getOrderName();

    String getExternalId();

    java.time.Instant getOrderDate();
    String getStatusId();

    String getCurrencyUomId();

    String getSyncStatusId();

    String getOriginFacilityId();

    String getMemo();

    String getContactDescription();

    String getFulfillmentStatusId();

    String getCustomerId();

    String getCustomerName();

    java.time.Instant getCreatedAt();
    String getCreatedBy();

    java.time.Instant getUpdatedAt();
    String getUpdatedBy();

}

