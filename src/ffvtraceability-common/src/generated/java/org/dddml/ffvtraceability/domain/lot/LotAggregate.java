// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.lot;

import java.util.List;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;
import org.dddml.ffvtraceability.domain.Command;

public interface LotAggregate {
    LotState getState();

    List<Event> getChanges();

    void create(LotCommand.CreateLot c);

    void mergePatch(LotCommand.MergePatchLot c);

    void delete(LotCommand.DeleteLot c);

    void throwOnInvalidStateTransition(Command c);
}
