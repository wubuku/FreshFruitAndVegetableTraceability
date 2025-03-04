// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.lot;

import java.util.Map;
import java.util.List;
import org.dddml.support.criterion.Criterion;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;
import org.dddml.ffvtraceability.domain.Command;

public interface LotApplicationService {
    void when(LotCommand.CreateLot c);

    void when(LotCommand.MergePatchLot c);

    LotState get(String id);

    Iterable<LotState> getAll(Integer firstResult, Integer maxResults);

    Iterable<LotState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<LotState> get(Criterion filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<LotState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults);

    long getCount(Iterable<Map.Entry<String, Object>> filter);

    long getCount(Criterion filter);

    LotEvent getEvent(String lotId, long version);

    LotState getHistoryState(String lotId, long version);

    LotIdentificationState getLotIdentification(String lotId, String lotIdentificationTypeId);

    Iterable<LotIdentificationState> getLotIdentifications(String lotId, Criterion filter, List<String> orders);

}

