// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.shipment;

import java.util.List;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;
import org.dddml.ffvtraceability.domain.Command;

public interface ShipmentAggregate {
    ShipmentState getState();

    List<Event> getChanges();

    void create(ShipmentCommand.CreateShipment c);

    void mergePatch(ShipmentCommand.MergePatchShipment c);

    void delete(ShipmentCommand.DeleteShipment c);

    void throwOnInvalidStateTransition(Command c);
}
