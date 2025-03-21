// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.tenant;

import java.util.List;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;
import org.dddml.ffvtraceability.domain.Command;

public interface TenantAggregate {
    TenantState getState();

    List<Event> getChanges();

    void create(TenantCommand.CreateTenant c);

    void mergePatch(TenantCommand.MergePatchTenant c);

    void throwOnInvalidStateTransition(Command c);
}

