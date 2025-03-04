// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.workeffortinventoryassign;

import java.util.Map;
import java.util.List;
import org.dddml.support.criterion.Criterion;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;
import org.dddml.ffvtraceability.domain.Command;

public interface WorkEffortInventoryAssignApplicationService {
    void when(WorkEffortInventoryAssignCommand.CreateWorkEffortInventoryAssign c);

    void when(WorkEffortInventoryAssignCommand.MergePatchWorkEffortInventoryAssign c);

    WorkEffortInventoryAssignState get(WorkEffortInventoryAssignId id);

    Iterable<WorkEffortInventoryAssignState> getAll(Integer firstResult, Integer maxResults);

    Iterable<WorkEffortInventoryAssignState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<WorkEffortInventoryAssignState> get(Criterion filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<WorkEffortInventoryAssignState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults);

    long getCount(Iterable<Map.Entry<String, Object>> filter);

    long getCount(Criterion filter);

    WorkEffortInventoryAssignEvent getEvent(WorkEffortInventoryAssignId workEffortInventoryAssignId, long version);

    WorkEffortInventoryAssignState getHistoryState(WorkEffortInventoryAssignId workEffortInventoryAssignId, long version);

}

