// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.contactmech;

import java.util.Map;
import java.util.List;
import org.dddml.support.criterion.Criterion;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public interface TelecomNumberStateQueryRepository {
    TelecomNumberState get(String id);

    Iterable<TelecomNumberState> getAll(Integer firstResult, Integer maxResults);
    
    Iterable<TelecomNumberState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<TelecomNumberState> get(Criterion filter, List<String> orders, Integer firstResult, Integer maxResults);

    TelecomNumberState getFirst(Iterable<Map.Entry<String, Object>> filter, List<String> orders);

    TelecomNumberState getFirst(Map.Entry<String, Object> keyValue, List<String> orders);

    Iterable<TelecomNumberState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults);

    long getCount(Iterable<Map.Entry<String, Object>> filter);

    long getCount(Criterion filter);

}
