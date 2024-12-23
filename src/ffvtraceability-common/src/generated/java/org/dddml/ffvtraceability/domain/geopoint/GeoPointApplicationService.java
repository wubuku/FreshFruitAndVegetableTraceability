// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.geopoint;

import java.util.Map;
import java.util.List;
import org.dddml.support.criterion.Criterion;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;
import org.dddml.ffvtraceability.domain.Command;

public interface GeoPointApplicationService {
    void when(GeoPointCommand.CreateGeoPoint c);

    void when(GeoPointCommand.MergePatchGeoPoint c);

    GeoPointState get(String id);

    Iterable<GeoPointState> getAll(Integer firstResult, Integer maxResults);

    Iterable<GeoPointState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<GeoPointState> get(Criterion filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<GeoPointState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults);

    long getCount(Iterable<Map.Entry<String, Object>> filter);

    long getCount(Criterion filter);

}

