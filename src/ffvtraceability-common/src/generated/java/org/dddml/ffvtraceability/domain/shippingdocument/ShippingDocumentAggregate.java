// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.shippingdocument;

import java.util.List;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;
import org.dddml.ffvtraceability.domain.Command;

public interface ShippingDocumentAggregate {
    ShippingDocumentState getState();

    List<Event> getChanges();

    void create(ShippingDocumentCommand.CreateShippingDocument c);

    void mergePatch(ShippingDocumentCommand.MergePatchShippingDocument c);

    void delete(ShippingDocumentCommand.DeleteShippingDocument c);

    void throwOnInvalidStateTransition(Command c);
}
