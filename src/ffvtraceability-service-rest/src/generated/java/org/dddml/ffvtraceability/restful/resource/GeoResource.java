// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.restful.resource;

import java.util.*;
import java.util.stream.*;
import jakarta.servlet.http.*;
import jakarta.validation.constraints.*;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.dddml.support.criterion.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.geo.*;
import static org.dddml.ffvtraceability.domain.meta.M.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.*;
import org.dddml.support.criterion.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestMapping(path = "Geos", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class GeoResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static CriterionDto deserializeCriterionDto(String filter) {
        return deserializeJsonArgument(filter, CriterionDto.class);
    }

    private static <T> T deserializeJsonArgument(String s, Class<T> aClass) {
        try {
            return new ObjectMapper().readValue(s, aClass);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }


    @Autowired
    private GeoApplicationService geoApplicationService;


    /**
     * Retrieve.
     * Retrieve Geos
     */
    @GetMapping
    @Transactional(readOnly = true)
    public GeoStateDto[] getAll( HttpServletRequest request,
                    @RequestParam(value = "sort", required = false) String sort,
                    @RequestParam(value = "fields", required = false) String fields,
                    @RequestParam(value = "firstResult", defaultValue = "0") Integer firstResult,
                    @RequestParam(value = "maxResults", defaultValue = "2147483647") Integer maxResults,
                    @RequestParam(value = "filter", required = false) String filter) {
        
        if (firstResult < 0) { firstResult = 0; }
        if (maxResults == null || maxResults < 1) { maxResults = Integer.MAX_VALUE; }

            Iterable<GeoState> states = null; 
            CriterionDto criterion = null;
            if (!StringHelper.isNullOrEmpty(filter)) {
                criterion = deserializeCriterionDto(filter);
            } else {
                criterion = QueryParamUtils.getQueryCriterionDto(request.getParameterMap().entrySet().stream()
                    .filter(kv -> GeoResourceUtils.getFilterPropertyName(kv.getKey()) != null)
                    .collect(Collectors.toMap(kv -> kv.getKey(), kv -> kv.getValue())));
            }
            Criterion c = CriterionDto.toSubclass(criterion, getCriterionTypeConverter(), getPropertyTypeResolver(), 
                n -> (GeoMetadata.aliasMap.containsKey(n) ? GeoMetadata.aliasMap.get(n) : n));
            states = geoApplicationService.get(
                c,
                GeoResourceUtils.getQuerySorts(request.getParameterMap()),
                firstResult, maxResults);

            GeoStateDto.DtoConverter dtoConverter = new GeoStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toGeoStateDtoArray(states);

        
    }

    /**
     * Retrieve in pages.
     * Retrieve Geos in pages.
     */
    @GetMapping("_page")
    @Transactional(readOnly = true)
    public Page<GeoStateDto> getPage( HttpServletRequest request,
                    @RequestParam(value = "fields", required = false) String fields,
                    @RequestParam(value = "page", defaultValue = "0") Integer page,
                    @RequestParam(value = "size", defaultValue = "20") Integer size,
                    @RequestParam(value = "filter", required = false) String filter) {
        
            Integer firstResult = (page == null ? 0 : page) * (size == null ? 20 : size);
            Integer maxResults = (size == null ? 20 : size);
            Iterable<GeoState> states = null; 
            CriterionDto criterion = null;
            if (!StringHelper.isNullOrEmpty(filter)) {
                criterion = deserializeCriterionDto(filter);
            } else {
                criterion = QueryParamUtils.getQueryCriterionDto(request.getParameterMap().entrySet().stream()
                    .filter(kv -> GeoResourceUtils.getFilterPropertyName(kv.getKey()) != null)
                    .collect(Collectors.toMap(kv -> kv.getKey(), kv -> kv.getValue())));
            }
            Criterion c = CriterionDto.toSubclass(criterion, getCriterionTypeConverter(), getPropertyTypeResolver(), 
                n -> (GeoMetadata.aliasMap.containsKey(n) ? GeoMetadata.aliasMap.get(n) : n));
            states = geoApplicationService.get(
                c,
                GeoResourceUtils.getQuerySorts(request.getParameterMap()),
                firstResult, maxResults);
            long count = geoApplicationService.getCount(c);

            GeoStateDto.DtoConverter dtoConverter = new GeoStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            Page.PageImpl<GeoStateDto> statePage =  new Page.PageImpl<>(dtoConverter.toGeoStateDtoList(states), count);
            statePage.setSize(size);
            statePage.setNumber(page);
            return statePage;

        
    }

    /**
     * Retrieve.
     * Retrieves Geo with the specified ID.
     */
    @GetMapping("{geoId}")
    @Transactional(readOnly = true)
    public GeoStateDto get(@PathVariable("geoId") String geoId, @RequestParam(value = "fields", required = false) String fields) {
        
            String idObj = geoId;
            GeoState state = geoApplicationService.get(idObj);
            if (state == null) { return null; }

            GeoStateDto.DtoConverter dtoConverter = new GeoStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toGeoStateDto(state);

        
    }

    @GetMapping("_count")
    @Transactional(readOnly = true)
    public long getCount( HttpServletRequest request,
                         @RequestParam(value = "filter", required = false) String filter) {
        
            long count = 0;
            CriterionDto criterion = null;
            if (!StringHelper.isNullOrEmpty(filter)) {
                criterion = deserializeCriterionDto(filter);
            } else {
                criterion = QueryParamUtils.getQueryCriterionDto(request.getParameterMap());
            }
            Criterion c = CriterionDto.toSubclass(criterion,
                getCriterionTypeConverter(), 
                getPropertyTypeResolver(), 
                n -> (GeoMetadata.aliasMap.containsKey(n) ? GeoMetadata.aliasMap.get(n) : n));
            count = geoApplicationService.getCount(c);
            return count;

        
    }

    @GetMapping("_metadata/filteringFields")
    public List<PropertyMetadataDto> getMetadataFilteringFields() {
        

            List<PropertyMetadataDto> filtering = new ArrayList<>();
            GeoMetadata.propertyTypeMap.forEach((key, value) -> {
                filtering.add(new PropertyMetadataDto(key, value, true));
            });
            return filtering;

        
    }


    protected TypeConverter getCriterionTypeConverter() {
        return new DefaultTypeConverter();
    }

    protected PropertyTypeResolver getPropertyTypeResolver() {
        return new PropertyTypeResolver() {
            @Override
            public Class resolveTypeByPropertyName(String propertyName) {
                return GeoResourceUtils.getFilterPropertyType(propertyName);
            }
        };
    }

 
    public static class GeoResourceUtils {

        public static void setNullIdOrThrowOnInconsistentIds(String geoId, org.dddml.ffvtraceability.domain.geo.GeoCommand value) {
            String idObj = geoId;
            if (value.getGeoId() == null) {
                value.setGeoId(idObj);
            } else if (!value.getGeoId().equals(idObj)) {
                throw DomainError.named("inconsistentId", "Argument Id %1$s NOT equals body Id %2$s", geoId, value.getGeoId());
            }
        }
    
        public static List<String> getQueryOrders(String str, String separator) {
            return QueryParamUtils.getQueryOrders(str, separator, GeoMetadata.aliasMap);
        }

        public static List<String> getQuerySorts(Map<String, String[]> queryNameValuePairs) {
            String[] values = queryNameValuePairs.get("sort");
            return QueryParamUtils.getQuerySorts(values, GeoMetadata.aliasMap);
        }

        public static String getFilterPropertyName(String fieldName) {
            if ("sort".equalsIgnoreCase(fieldName)
                    || "firstResult".equalsIgnoreCase(fieldName)
                    || "maxResults".equalsIgnoreCase(fieldName)
                    || "fields".equalsIgnoreCase(fieldName)) {
                return null;
            }
            if (GeoMetadata.aliasMap.containsKey(fieldName)) {
                return GeoMetadata.aliasMap.get(fieldName);
            }
            return null;
        }

        public static Class getFilterPropertyType(String propertyName) {
            if (GeoMetadata.propertyTypeMap.containsKey(propertyName)) {
                String propertyType = GeoMetadata.propertyTypeMap.get(propertyName);
                if (!StringHelper.isNullOrEmpty(propertyType)) {
                    if (BoundedContextMetadata.CLASS_MAP.containsKey(propertyType)) {
                        return BoundedContextMetadata.CLASS_MAP.get(propertyType);
                    }
                }
            }
            return String.class;
        }

        public static Iterable<Map.Entry<String, Object>> getQueryFilterMap(Map<String, String[]> queryNameValuePairs) {
            Map<String, Object> filter = new HashMap<>();
            queryNameValuePairs.forEach((key, values) -> {
                if (values.length > 0) {
                    String pName = getFilterPropertyName(key);
                    if (!StringHelper.isNullOrEmpty(pName)) {
                        Class pClass = getFilterPropertyType(pName);
                        filter.put(pName, ApplicationContext.current.getTypeConverter().convertFromString(pClass, values[0]));
                    }
                }
            });
            return filter.entrySet();
        }

        public static GeoStateDto[] toGeoStateDtoArray(Iterable<String> ids) {
            List<GeoStateDto> states = new ArrayList<>();
            ids.forEach(i -> {
                GeoStateDto dto = new GeoStateDto();
                dto.setGeoId(i);
                states.add(dto);
            });
            return states.toArray(new GeoStateDto[0]);
        }

    }

}

