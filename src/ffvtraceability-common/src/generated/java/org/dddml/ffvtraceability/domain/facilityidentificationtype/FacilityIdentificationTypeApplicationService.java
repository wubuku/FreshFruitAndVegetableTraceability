// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.facilityidentificationtype;

import java.util.Map;
import java.util.List;
import org.dddml.support.criterion.Criterion;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;
import org.dddml.ffvtraceability.domain.Command;

public interface FacilityIdentificationTypeApplicationService {
    void when(FacilityIdentificationTypeCommand.CreateFacilityIdentificationType c);

    void when(FacilityIdentificationTypeCommand.MergePatchFacilityIdentificationType c);

    void when(FacilityIdentificationTypeCommand.DeleteFacilityIdentificationType c);

    FacilityIdentificationTypeState get(String id);

    Iterable<FacilityIdentificationTypeState> getAll(Integer firstResult, Integer maxResults);

    Iterable<FacilityIdentificationTypeState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<FacilityIdentificationTypeState> get(Criterion filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<FacilityIdentificationTypeState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults);

    long getCount(Iterable<Map.Entry<String, Object>> filter);

    long getCount(Criterion filter);

    FacilityIdentificationTypeEvent getEvent(String facilityIdentificationTypeId, long version);

    FacilityIdentificationTypeState getHistoryState(String facilityIdentificationTypeId, long version);

}
