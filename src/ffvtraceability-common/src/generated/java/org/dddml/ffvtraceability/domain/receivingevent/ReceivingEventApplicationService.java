// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.receivingevent;

import java.util.Map;
import java.util.List;
import org.dddml.support.criterion.Criterion;
import org.dddml.ffvtraceability.domain.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.specialization.Event;
import org.dddml.ffvtraceability.domain.Command;

public interface ReceivingEventApplicationService {
    Long createWithoutId(ReceivingEventCommand.CreateReceivingEvent c);

    void when(ReceivingEventCommand.MergePatchReceivingEvent c);

    void when(ReceivingEventCommand.DeleteReceivingEvent c);

    ReceivingEventState get(Long id);

    Iterable<ReceivingEventState> getAll(Integer firstResult, Integer maxResults);

    Iterable<ReceivingEventState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<ReceivingEventState> get(Criterion filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<ReceivingEventState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults);

    long getCount(Iterable<Map.Entry<String, Object>> filter);

    long getCount(Criterion filter);

}
