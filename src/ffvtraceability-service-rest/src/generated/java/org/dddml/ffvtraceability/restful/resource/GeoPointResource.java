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
import org.dddml.ffvtraceability.domain.geopoint.*;
import static org.dddml.ffvtraceability.domain.meta.M.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.*;
import org.dddml.support.criterion.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestMapping(path = "GeoPoints", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class GeoPointResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private GeoPointApplicationService geoPointApplicationService;


    /**
     * Retrieve.
     * Retrieve GeoPoints
     */
    @GetMapping
    @Transactional(readOnly = true)
    public GeoPointStateDto[] getAll( HttpServletRequest request,
                    @RequestParam(value = "sort", required = false) String sort,
                    @RequestParam(value = "fields", required = false) String fields,
                    @RequestParam(value = "firstResult", defaultValue = "0") Integer firstResult,
                    @RequestParam(value = "maxResults", defaultValue = "2147483647") Integer maxResults,
                    @RequestParam(value = "filter", required = false) String filter) {
        try {
        if (firstResult < 0) { firstResult = 0; }
        if (maxResults == null || maxResults < 1) { maxResults = Integer.MAX_VALUE; }

            Iterable<GeoPointState> states = null; 
            CriterionDto criterion = null;
            if (!StringHelper.isNullOrEmpty(filter)) {
                criterion = new ObjectMapper().readValue(filter, CriterionDto.class);
            } else {
                criterion = QueryParamUtils.getQueryCriterionDto(request.getParameterMap().entrySet().stream()
                    .filter(kv -> GeoPointResourceUtils.getFilterPropertyName(kv.getKey()) != null)
                    .collect(Collectors.toMap(kv -> kv.getKey(), kv -> kv.getValue())));
            }
            Criterion c = CriterionDto.toSubclass(criterion, getCriterionTypeConverter(), getPropertyTypeResolver(), 
                n -> (GeoPointMetadata.aliasMap.containsKey(n) ? GeoPointMetadata.aliasMap.get(n) : n));
            states = geoPointApplicationService.get(
                c,
                GeoPointResourceUtils.getQuerySorts(request.getParameterMap()),
                firstResult, maxResults);

            GeoPointStateDto.DtoConverter dtoConverter = new GeoPointStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toGeoPointStateDtoArray(states);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    /**
     * Retrieve in pages.
     * Retrieve GeoPoints in pages.
     */
    @GetMapping("_page")
    @Transactional(readOnly = true)
    public Page<GeoPointStateDto> getPage( HttpServletRequest request,
                    @RequestParam(value = "fields", required = false) String fields,
                    @RequestParam(value = "page", defaultValue = "0") Integer page,
                    @RequestParam(value = "size", defaultValue = "20") Integer size,
                    @RequestParam(value = "filter", required = false) String filter) {
        try {
            Integer firstResult = (page == null ? 0 : page) * (size == null ? 20 : size);
            Integer maxResults = (size == null ? 20 : size);
            Iterable<GeoPointState> states = null; 
            CriterionDto criterion = null;
            if (!StringHelper.isNullOrEmpty(filter)) {
                criterion = new ObjectMapper().readValue(filter, CriterionDto.class);
            } else {
                criterion = QueryParamUtils.getQueryCriterionDto(request.getParameterMap().entrySet().stream()
                    .filter(kv -> GeoPointResourceUtils.getFilterPropertyName(kv.getKey()) != null)
                    .collect(Collectors.toMap(kv -> kv.getKey(), kv -> kv.getValue())));
            }
            Criterion c = CriterionDto.toSubclass(criterion, getCriterionTypeConverter(), getPropertyTypeResolver(), 
                n -> (GeoPointMetadata.aliasMap.containsKey(n) ? GeoPointMetadata.aliasMap.get(n) : n));
            states = geoPointApplicationService.get(
                c,
                GeoPointResourceUtils.getQuerySorts(request.getParameterMap()),
                firstResult, maxResults);
            long count = geoPointApplicationService.getCount(c);

            GeoPointStateDto.DtoConverter dtoConverter = new GeoPointStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            Page.PageImpl<GeoPointStateDto> statePage =  new Page.PageImpl<>(dtoConverter.toGeoPointStateDtoList(states), count);
            statePage.setSize(size);
            statePage.setNumber(page);
            return statePage;

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    /**
     * Retrieve.
     * Retrieves GeoPoint with the specified ID.
     */
    @GetMapping("{geoPointId}")
    @Transactional(readOnly = true)
    public GeoPointStateDto get(@PathVariable("geoPointId") String geoPointId, @RequestParam(value = "fields", required = false) String fields) {
        try {
            String idObj = geoPointId;
            GeoPointState state = geoPointApplicationService.get(idObj);
            if (state == null) { return null; }

            GeoPointStateDto.DtoConverter dtoConverter = new GeoPointStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toGeoPointStateDto(state);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @GetMapping("_count")
    @Transactional(readOnly = true)
    public long getCount( HttpServletRequest request,
                         @RequestParam(value = "filter", required = false) String filter) {
        try {
            long count = 0;
            CriterionDto criterion = null;
            if (!StringHelper.isNullOrEmpty(filter)) {
                criterion = new ObjectMapper().readValue(filter, CriterionDto.class);
            } else {
                criterion = QueryParamUtils.getQueryCriterionDto(request.getParameterMap());
            }
            Criterion c = CriterionDto.toSubclass(criterion,
                getCriterionTypeConverter(), 
                getPropertyTypeResolver(), 
                n -> (GeoPointMetadata.aliasMap.containsKey(n) ? GeoPointMetadata.aliasMap.get(n) : n));
            count = geoPointApplicationService.getCount(c);
            return count;

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }


    /**
     * Create.
     * Create GeoPoint
     */
    @PostMapping @ResponseBody @ResponseStatus(HttpStatus.CREATED)
    public String post(@RequestBody CreateOrMergePatchGeoPointDto.CreateGeoPointDto value,  HttpServletResponse response) {
        try {
            GeoPointCommand.CreateGeoPoint cmd = value;//.toCreateGeoPoint();
            if (cmd.getGeoPointId() == null) {
                throw DomainError.named("nullId", "Aggregate Id in cmd is null, aggregate name: %1$s.", "GeoPoint");
            }
            String idObj = cmd.getGeoPointId();
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            geoPointApplicationService.when(cmd);

            return idObj;
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }


    /**
     * Create or update.
     * Create or update GeoPoint
     */
    @PutMapping("{geoPointId}")
    public void put(@PathVariable("geoPointId") String geoPointId, @RequestBody CreateOrMergePatchGeoPointDto value) {
        try {
            if (value.getVersion() != null) {
                value.setCommandType(Command.COMMAND_TYPE_MERGE_PATCH);
                GeoPointCommand.MergePatchGeoPoint cmd = (GeoPointCommand.MergePatchGeoPoint) value.toSubclass();
                GeoPointResourceUtils.setNullIdOrThrowOnInconsistentIds(geoPointId, cmd);
                cmd.setRequesterId(SecurityContextUtil.getRequesterId());
                geoPointApplicationService.when(cmd);
                return;
            }

            value.setCommandType(Command.COMMAND_TYPE_CREATE);
            GeoPointCommand.CreateGeoPoint cmd = (GeoPointCommand.CreateGeoPoint) value.toSubclass();
            GeoPointResourceUtils.setNullIdOrThrowOnInconsistentIds(geoPointId, cmd);
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            geoPointApplicationService.when(cmd);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }


    /**
     * Patch.
     * Patch GeoPoint
     */
    @PatchMapping("{geoPointId}")
    public void patch(@PathVariable("geoPointId") String geoPointId, @RequestBody CreateOrMergePatchGeoPointDto.MergePatchGeoPointDto value) {
        try {

            GeoPointCommand.MergePatchGeoPoint cmd = value;//.toMergePatchGeoPoint();
            GeoPointResourceUtils.setNullIdOrThrowOnInconsistentIds(geoPointId, cmd);
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            geoPointApplicationService.when(cmd);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @GetMapping("_metadata/filteringFields")
    public List<PropertyMetadataDto> getMetadataFilteringFields() {
        try {

            List<PropertyMetadataDto> filtering = new ArrayList<>();
            GeoPointMetadata.propertyTypeMap.forEach((key, value) -> {
                filtering.add(new PropertyMetadataDto(key, value, true));
            });
            return filtering;

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }


    protected TypeConverter getCriterionTypeConverter() {
        return new DefaultTypeConverter();
    }

    protected PropertyTypeResolver getPropertyTypeResolver() {
        return new PropertyTypeResolver() {
            @Override
            public Class resolveTypeByPropertyName(String propertyName) {
                return GeoPointResourceUtils.getFilterPropertyType(propertyName);
            }
        };
    }

 
    public static class GeoPointResourceUtils {

        public static void setNullIdOrThrowOnInconsistentIds(String geoPointId, org.dddml.ffvtraceability.domain.geopoint.GeoPointCommand value) {
            String idObj = geoPointId;
            if (value.getGeoPointId() == null) {
                value.setGeoPointId(idObj);
            } else if (!value.getGeoPointId().equals(idObj)) {
                throw DomainError.named("inconsistentId", "Argument Id %1$s NOT equals body Id %2$s", geoPointId, value.getGeoPointId());
            }
        }
    
        public static List<String> getQueryOrders(String str, String separator) {
            return QueryParamUtils.getQueryOrders(str, separator, GeoPointMetadata.aliasMap);
        }

        public static List<String> getQuerySorts(Map<String, String[]> queryNameValuePairs) {
            String[] values = queryNameValuePairs.get("sort");
            return QueryParamUtils.getQuerySorts(values, GeoPointMetadata.aliasMap);
        }

        public static String getFilterPropertyName(String fieldName) {
            if ("sort".equalsIgnoreCase(fieldName)
                    || "firstResult".equalsIgnoreCase(fieldName)
                    || "maxResults".equalsIgnoreCase(fieldName)
                    || "fields".equalsIgnoreCase(fieldName)) {
                return null;
            }
            if (GeoPointMetadata.aliasMap.containsKey(fieldName)) {
                return GeoPointMetadata.aliasMap.get(fieldName);
            }
            return null;
        }

        public static Class getFilterPropertyType(String propertyName) {
            if (GeoPointMetadata.propertyTypeMap.containsKey(propertyName)) {
                String propertyType = GeoPointMetadata.propertyTypeMap.get(propertyName);
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

        public static GeoPointStateDto[] toGeoPointStateDtoArray(Iterable<String> ids) {
            List<GeoPointStateDto> states = new ArrayList<>();
            ids.forEach(i -> {
                GeoPointStateDto dto = new GeoPointStateDto();
                dto.setGeoPointId(i);
                states.add(dto);
            });
            return states.toArray(new GeoPointStateDto[0]);
        }

    }

}

