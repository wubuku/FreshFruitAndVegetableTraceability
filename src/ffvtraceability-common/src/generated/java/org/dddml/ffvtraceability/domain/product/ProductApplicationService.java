// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.product;

import java.util.Map;
import java.util.List;
import org.dddml.support.criterion.Criterion;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;
import org.dddml.ffvtraceability.domain.Command;

public interface ProductApplicationService {
    void when(ProductCommand.CreateProduct c);

    void when(ProductCommand.MergePatchProduct c);

    ProductState get(String id);

    Iterable<ProductState> getAll(Integer firstResult, Integer maxResults);

    Iterable<ProductState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<ProductState> get(Criterion filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<ProductState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults);

    long getCount(Iterable<Map.Entry<String, Object>> filter);

    long getCount(Criterion filter);

    Iterable<ProductState> getAll(Class<? extends ProductState> stateType, Integer firstResult, Integer maxResults);

    Iterable<ProductState> get(Class<? extends ProductState> stateType, Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<ProductState> get(Class<? extends ProductState> stateType, Criterion filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<ProductState> getByProperty(Class<? extends ProductState> stateType, String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults);

    long getCount(Class<? extends ProductState> stateType, Iterable<Map.Entry<String, Object>> filter);

    long getCount(Class<? extends ProductState> stateType, Criterion filter);

    ProductEvent getEvent(String productId, long version);

    ProductState getHistoryState(String productId, long version);

    GoodIdentificationState getGoodIdentification(String productId, String goodIdentificationTypeId);

    Iterable<GoodIdentificationState> getGoodIdentifications(String productId, Criterion filter, List<String> orders);

}
