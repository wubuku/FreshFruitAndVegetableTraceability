// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.attributesetinstance;

import java.util.Map;
import java.util.List;
import org.dddml.support.criterion.Criterion;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public interface AttributeSetInstanceStateQueryRepository {
    AttributeSetInstanceState get(String id);

    Iterable<AttributeSetInstanceState> getAll(Integer firstResult, Integer maxResults);
    
    Iterable<AttributeSetInstanceState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<AttributeSetInstanceState> get(Criterion filter, List<String> orders, Integer firstResult, Integer maxResults);

    AttributeSetInstanceState getFirst(Iterable<Map.Entry<String, Object>> filter, List<String> orders);

    AttributeSetInstanceState getFirst(Map.Entry<String, Object> keyValue, List<String> orders);

    Iterable<AttributeSetInstanceState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults);

    long getCount(Iterable<Map.Entry<String, Object>> filter);

    long getCount(Criterion filter);

}

