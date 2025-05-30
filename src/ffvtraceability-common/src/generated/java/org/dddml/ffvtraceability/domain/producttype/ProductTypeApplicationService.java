// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.producttype;

import java.util.Map;
import java.util.List;
import org.dddml.support.criterion.Criterion;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;
import org.dddml.ffvtraceability.domain.Command;

public interface ProductTypeApplicationService {
    void when(ProductTypeCommand.CreateProductType c);

    void when(ProductTypeCommand.MergePatchProductType c);

    ProductTypeState get(String id);

    Iterable<ProductTypeState> getAll(Integer firstResult, Integer maxResults);

    Iterable<ProductTypeState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<ProductTypeState> get(Criterion filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<ProductTypeState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults);

    long getCount(Iterable<Map.Entry<String, Object>> filter);

    long getCount(Criterion filter);

}

