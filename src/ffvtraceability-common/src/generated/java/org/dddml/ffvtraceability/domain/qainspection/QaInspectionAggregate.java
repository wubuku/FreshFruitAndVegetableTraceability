// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.qainspection;

import java.util.List;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;
import org.dddml.ffvtraceability.domain.Command;

public interface QaInspectionAggregate {
    QaInspectionState getState();

    List<Event> getChanges();

    void create(QaInspectionCommand.CreateQaInspection c);

    void mergePatch(QaInspectionCommand.MergePatchQaInspection c);

    void qaInspectionAction(String value, Long version, String commandId, String requesterId, QaInspectionCommands.QaInspectionAction c);

    void throwOnInvalidStateTransition(Command c);
}

