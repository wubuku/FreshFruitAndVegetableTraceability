// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.repository;

import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public interface BffLotProjection {
    String getLotId();

    String getGs1Batch();

    java.math.BigDecimal getQuantity();

    java.time.Instant getExpirationDateInstant();
}

