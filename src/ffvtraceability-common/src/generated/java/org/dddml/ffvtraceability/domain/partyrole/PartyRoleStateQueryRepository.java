// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.partyrole;

import java.util.Map;
import java.util.List;
import org.dddml.support.criterion.Criterion;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public interface PartyRoleStateQueryRepository {
    PartyRoleState get(PartyRoleId id);

    Iterable<PartyRoleState> getAll(Integer firstResult, Integer maxResults);
    
    Iterable<PartyRoleState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<PartyRoleState> get(Criterion filter, List<String> orders, Integer firstResult, Integer maxResults);

    PartyRoleState getFirst(Iterable<Map.Entry<String, Object>> filter, List<String> orders);

    PartyRoleState getFirst(Map.Entry<String, Object> keyValue, List<String> orders);

    Iterable<PartyRoleState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults);

    long getCount(Iterable<Map.Entry<String, Object>> filter);

    long getCount(Criterion filter);

}

