// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.workeffortinventoryproduced;

import java.util.Map;
import java.util.List;
import org.dddml.support.criterion.Criterion;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;
import org.dddml.ffvtraceability.domain.Command;

public interface WorkEffortInventoryProducedApplicationService {
    void when(WorkEffortInventoryProducedCommand.CreateWorkEffortInventoryProduced c);

    WorkEffortInventoryProducedState get(WorkEffortInventoryProducedId id);

    Iterable<WorkEffortInventoryProducedState> getAll(Integer firstResult, Integer maxResults);

    Iterable<WorkEffortInventoryProducedState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<WorkEffortInventoryProducedState> get(Criterion filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<WorkEffortInventoryProducedState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults);

    long getCount(Iterable<Map.Entry<String, Object>> filter);

    long getCount(Criterion filter);

    WorkEffortInventoryProducedEvent getEvent(WorkEffortInventoryProducedId workEffortInventoryProducedId, long version);

    WorkEffortInventoryProducedState getHistoryState(WorkEffortInventoryProducedId workEffortInventoryProducedId, long version);

}

