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
import org.dddml.ffvtraceability.domain.facilitylocation.*;
import static org.dddml.ffvtraceability.domain.meta.M.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.*;
import org.dddml.support.criterion.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestMapping(path = "FacilityLocations", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class FacilityLocationResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private FacilityLocationApplicationService facilityLocationApplicationService;


    /**
     * Retrieve.
     * Retrieve FacilityLocations
     */
    @GetMapping
    @Transactional(readOnly = true)
    public FacilityLocationStateDto[] getAll( HttpServletRequest request,
                    @RequestParam(value = "sort", required = false) String sort,
                    @RequestParam(value = "fields", required = false) String fields,
                    @RequestParam(value = "firstResult", defaultValue = "0") Integer firstResult,
                    @RequestParam(value = "maxResults", defaultValue = "2147483647") Integer maxResults,
                    @RequestParam(value = "filter", required = false) String filter) {
        try {
        if (firstResult < 0) { firstResult = 0; }
        if (maxResults == null || maxResults < 1) { maxResults = Integer.MAX_VALUE; }

            Iterable<FacilityLocationState> states = null; 
            CriterionDto criterion = null;
            if (!StringHelper.isNullOrEmpty(filter)) {
                criterion = new ObjectMapper().readValue(filter, CriterionDto.class);
            } else {
                criterion = QueryParamUtils.getQueryCriterionDto(request.getParameterMap().entrySet().stream()
                    .filter(kv -> FacilityLocationResourceUtils.getFilterPropertyName(kv.getKey()) != null)
                    .collect(Collectors.toMap(kv -> kv.getKey(), kv -> kv.getValue())));
            }
            Criterion c = CriterionDto.toSubclass(criterion, getCriterionTypeConverter(), getPropertyTypeResolver(), 
                n -> (FacilityLocationMetadata.aliasMap.containsKey(n) ? FacilityLocationMetadata.aliasMap.get(n) : n));
            states = facilityLocationApplicationService.get(
                c,
                FacilityLocationResourceUtils.getQuerySorts(request.getParameterMap()),
                firstResult, maxResults);

            FacilityLocationStateDto.DtoConverter dtoConverter = new FacilityLocationStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toFacilityLocationStateDtoArray(states);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    /**
     * Retrieve in pages.
     * Retrieve FacilityLocations in pages.
     */
    @GetMapping("_page")
    @Transactional(readOnly = true)
    public Page<FacilityLocationStateDto> getPage( HttpServletRequest request,
                    @RequestParam(value = "fields", required = false) String fields,
                    @RequestParam(value = "page", defaultValue = "0") Integer page,
                    @RequestParam(value = "size", defaultValue = "20") Integer size,
                    @RequestParam(value = "filter", required = false) String filter) {
        try {
            Integer firstResult = (page == null ? 0 : page) * (size == null ? 20 : size);
            Integer maxResults = (size == null ? 20 : size);
            Iterable<FacilityLocationState> states = null; 
            CriterionDto criterion = null;
            if (!StringHelper.isNullOrEmpty(filter)) {
                criterion = new ObjectMapper().readValue(filter, CriterionDto.class);
            } else {
                criterion = QueryParamUtils.getQueryCriterionDto(request.getParameterMap().entrySet().stream()
                    .filter(kv -> FacilityLocationResourceUtils.getFilterPropertyName(kv.getKey()) != null)
                    .collect(Collectors.toMap(kv -> kv.getKey(), kv -> kv.getValue())));
            }
            Criterion c = CriterionDto.toSubclass(criterion, getCriterionTypeConverter(), getPropertyTypeResolver(), 
                n -> (FacilityLocationMetadata.aliasMap.containsKey(n) ? FacilityLocationMetadata.aliasMap.get(n) : n));
            states = facilityLocationApplicationService.get(
                c,
                FacilityLocationResourceUtils.getQuerySorts(request.getParameterMap()),
                firstResult, maxResults);
            long count = facilityLocationApplicationService.getCount(c);

            FacilityLocationStateDto.DtoConverter dtoConverter = new FacilityLocationStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            Page.PageImpl<FacilityLocationStateDto> statePage =  new Page.PageImpl<>(dtoConverter.toFacilityLocationStateDtoList(states), count);
            statePage.setSize(size);
            statePage.setNumber(page);
            return statePage;

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    /**
     * Retrieve.
     * Retrieves FacilityLocation with the specified ID.
     */
    @GetMapping("{facilityLocationId}")
    @Transactional(readOnly = true)
    public FacilityLocationStateDto get(@PathVariable("facilityLocationId") String facilityLocationId, @RequestParam(value = "fields", required = false) String fields) {
        try {
            FacilityLocationId idObj = FacilityLocationResourceUtils.parseIdString(facilityLocationId);
            FacilityLocationState state = facilityLocationApplicationService.get(idObj);
            if (state == null) { return null; }

            FacilityLocationStateDto.DtoConverter dtoConverter = new FacilityLocationStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toFacilityLocationStateDto(state);

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
                n -> (FacilityLocationMetadata.aliasMap.containsKey(n) ? FacilityLocationMetadata.aliasMap.get(n) : n));
            count = facilityLocationApplicationService.getCount(c);
            return count;

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }


    /**
     * Create.
     * Create FacilityLocation
     */
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public FacilityLocationId post(@RequestBody CreateOrMergePatchFacilityLocationDto.CreateFacilityLocationDto value,  HttpServletResponse response) {
        try {
            FacilityLocationCommand.CreateFacilityLocation cmd = value;//.toCreateFacilityLocation();
            if (cmd.getFacilityLocationId() == null) {
                throw DomainError.named("nullId", "Aggregate Id in cmd is null, aggregate name: %1$s.", "FacilityLocation");
            }
            FacilityLocationId idObj = cmd.getFacilityLocationId();
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            facilityLocationApplicationService.when(cmd);

            return idObj;
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }


    /**
     * Create or update.
     * Create or update FacilityLocation
     */
    @PutMapping("{facilityLocationId}")
    public void put(@PathVariable("facilityLocationId") String facilityLocationId, @RequestBody CreateOrMergePatchFacilityLocationDto value) {
        try {
            if (value.getVersion() != null) {
                value.setCommandType(Command.COMMAND_TYPE_MERGE_PATCH);
                FacilityLocationCommand.MergePatchFacilityLocation cmd = (FacilityLocationCommand.MergePatchFacilityLocation) value.toSubclass();
                FacilityLocationResourceUtils.setNullIdOrThrowOnInconsistentIds(facilityLocationId, cmd);
                cmd.setRequesterId(SecurityContextUtil.getRequesterId());
                facilityLocationApplicationService.when(cmd);
                return;
            }

            value.setCommandType(Command.COMMAND_TYPE_CREATE);
            FacilityLocationCommand.CreateFacilityLocation cmd = (FacilityLocationCommand.CreateFacilityLocation) value.toSubclass();
            FacilityLocationResourceUtils.setNullIdOrThrowOnInconsistentIds(facilityLocationId, cmd);
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            facilityLocationApplicationService.when(cmd);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }


    /**
     * Patch.
     * Patch FacilityLocation
     */
    @PatchMapping("{facilityLocationId}")
    public void patch(@PathVariable("facilityLocationId") String facilityLocationId, @RequestBody CreateOrMergePatchFacilityLocationDto.MergePatchFacilityLocationDto value) {
        try {

            FacilityLocationCommand.MergePatchFacilityLocation cmd = value;//.toMergePatchFacilityLocation();
            FacilityLocationResourceUtils.setNullIdOrThrowOnInconsistentIds(facilityLocationId, cmd);
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            facilityLocationApplicationService.when(cmd);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @GetMapping("_metadata/filteringFields")
    public List<PropertyMetadataDto> getMetadataFilteringFields() {
        try {

            List<PropertyMetadataDto> filtering = new ArrayList<>();
            FacilityLocationMetadata.propertyTypeMap.forEach((key, value) -> {
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
                return FacilityLocationResourceUtils.getFilterPropertyType(propertyName);
            }
        };
    }

 
    public static class FacilityLocationResourceUtils {

        private static final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

        static {
            objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            objectMapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
            objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
                    .setDateFormat(new com.fasterxml.jackson.databind.util.StdDateFormat().withColonInTimeZone(true))
                    .configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                    .configure(com.fasterxml.jackson.databind.DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
        }

        public static FacilityLocationId parseIdString(String idString) {
            try {
                return objectMapper.readValue(idString, FacilityLocationId.class);
            } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        public static void setNullIdOrThrowOnInconsistentIds(String facilityLocationId, org.dddml.ffvtraceability.domain.facilitylocation.FacilityLocationCommand value) {
            FacilityLocationId idObj = parseIdString(facilityLocationId);
            if (value.getFacilityLocationId() == null) {
                value.setFacilityLocationId(idObj);
            } else if (!value.getFacilityLocationId().equals(idObj)) {
                throw DomainError.named("inconsistentId", "Argument Id %1$s NOT equals body Id %2$s", facilityLocationId, value.getFacilityLocationId());
            }
        }
    
        public static List<String> getQueryOrders(String str, String separator) {
            return QueryParamUtils.getQueryOrders(str, separator, FacilityLocationMetadata.aliasMap);
        }

        public static List<String> getQuerySorts(Map<String, String[]> queryNameValuePairs) {
            String[] values = queryNameValuePairs.get("sort");
            return QueryParamUtils.getQuerySorts(values, FacilityLocationMetadata.aliasMap);
        }

        public static String getFilterPropertyName(String fieldName) {
            if ("sort".equalsIgnoreCase(fieldName)
                    || "firstResult".equalsIgnoreCase(fieldName)
                    || "maxResults".equalsIgnoreCase(fieldName)
                    || "fields".equalsIgnoreCase(fieldName)) {
                return null;
            }
            if (FacilityLocationMetadata.aliasMap.containsKey(fieldName)) {
                return FacilityLocationMetadata.aliasMap.get(fieldName);
            }
            return null;
        }

        public static Class getFilterPropertyType(String propertyName) {
            if (FacilityLocationMetadata.propertyTypeMap.containsKey(propertyName)) {
                String propertyType = FacilityLocationMetadata.propertyTypeMap.get(propertyName);
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

        public static FacilityLocationStateDto[] toFacilityLocationStateDtoArray(Iterable<FacilityLocationId> ids) {
            List<FacilityLocationStateDto> states = new ArrayList<>();
            ids.forEach(i -> {
                FacilityLocationStateDto dto = new FacilityLocationStateDto();
                dto.setFacilityLocationId(i);
                states.add(dto);
            });
            return states.toArray(new FacilityLocationStateDto[0]);
        }

    }

}
