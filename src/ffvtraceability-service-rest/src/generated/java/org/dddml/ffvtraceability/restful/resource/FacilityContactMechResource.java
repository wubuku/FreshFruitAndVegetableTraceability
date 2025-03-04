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
import org.dddml.ffvtraceability.domain.facilitycontactmech.*;
import static org.dddml.ffvtraceability.domain.meta.M.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.*;
import org.dddml.support.criterion.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestMapping(path = "FacilityContactMeches", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class FacilityContactMechResource {
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
    private FacilityContactMechApplicationService facilityContactMechApplicationService;


    /**
     * Retrieve.
     * Retrieve FacilityContactMeches
     */
    @GetMapping
    @Transactional(readOnly = true)
    public FacilityContactMechStateDto[] getAll( HttpServletRequest request,
                    @RequestParam(value = "sort", required = false) String sort,
                    @RequestParam(value = "fields", required = false) String fields,
                    @RequestParam(value = "firstResult", defaultValue = "0") Integer firstResult,
                    @RequestParam(value = "maxResults", defaultValue = "2147483647") Integer maxResults,
                    @RequestParam(value = "filter", required = false) String filter) {
        
        if (firstResult < 0) { firstResult = 0; }
        if (maxResults == null || maxResults < 1) { maxResults = Integer.MAX_VALUE; }

            Iterable<FacilityContactMechState> states = null; 
            CriterionDto criterion = null;
            if (!StringHelper.isNullOrEmpty(filter)) {
                criterion = deserializeCriterionDto(filter);
            } else {
                criterion = QueryParamUtils.getQueryCriterionDto(request.getParameterMap().entrySet().stream()
                    .filter(kv -> FacilityContactMechResourceUtils.getFilterPropertyName(kv.getKey()) != null)
                    .collect(Collectors.toMap(kv -> kv.getKey(), kv -> kv.getValue())));
            }
            Criterion c = CriterionDto.toSubclass(criterion, getCriterionTypeConverter(), getPropertyTypeResolver(), 
                n -> (FacilityContactMechMetadata.aliasMap.containsKey(n) ? FacilityContactMechMetadata.aliasMap.get(n) : n));
            states = facilityContactMechApplicationService.get(
                c,
                FacilityContactMechResourceUtils.getQuerySorts(request.getParameterMap()),
                firstResult, maxResults);

            FacilityContactMechStateDto.DtoConverter dtoConverter = new FacilityContactMechStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toFacilityContactMechStateDtoArray(states);

        
    }

    /**
     * Retrieve in pages.
     * Retrieve FacilityContactMeches in pages.
     */
    @GetMapping("_page")
    @Transactional(readOnly = true)
    public Page<FacilityContactMechStateDto> getPage( HttpServletRequest request,
                    @RequestParam(value = "fields", required = false) String fields,
                    @RequestParam(value = "page", defaultValue = "0") Integer page,
                    @RequestParam(value = "size", defaultValue = "20") Integer size,
                    @RequestParam(value = "filter", required = false) String filter) {
        
            Integer firstResult = (page == null ? 0 : page) * (size == null ? 20 : size);
            Integer maxResults = (size == null ? 20 : size);
            Iterable<FacilityContactMechState> states = null; 
            CriterionDto criterion = null;
            if (!StringHelper.isNullOrEmpty(filter)) {
                criterion = deserializeCriterionDto(filter);
            } else {
                criterion = QueryParamUtils.getQueryCriterionDto(request.getParameterMap().entrySet().stream()
                    .filter(kv -> FacilityContactMechResourceUtils.getFilterPropertyName(kv.getKey()) != null)
                    .collect(Collectors.toMap(kv -> kv.getKey(), kv -> kv.getValue())));
            }
            Criterion c = CriterionDto.toSubclass(criterion, getCriterionTypeConverter(), getPropertyTypeResolver(), 
                n -> (FacilityContactMechMetadata.aliasMap.containsKey(n) ? FacilityContactMechMetadata.aliasMap.get(n) : n));
            states = facilityContactMechApplicationService.get(
                c,
                FacilityContactMechResourceUtils.getQuerySorts(request.getParameterMap()),
                firstResult, maxResults);
            long count = facilityContactMechApplicationService.getCount(c);

            FacilityContactMechStateDto.DtoConverter dtoConverter = new FacilityContactMechStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            Page.PageImpl<FacilityContactMechStateDto> statePage =  new Page.PageImpl<>(dtoConverter.toFacilityContactMechStateDtoList(states), count);
            statePage.setSize(size);
            statePage.setNumber(page);
            return statePage;

        
    }

    /**
     * Retrieve.
     * Retrieves FacilityContactMech with the specified ID.
     */
    @GetMapping("{facilityContactMechId}")
    @Transactional(readOnly = true)
    public FacilityContactMechStateDto get(@PathVariable("facilityContactMechId") String facilityContactMechId, @RequestParam(value = "fields", required = false) String fields) {
        
            FacilityContactMechId idObj = FacilityContactMechResourceUtils.parseIdString(facilityContactMechId);
            FacilityContactMechState state = facilityContactMechApplicationService.get(idObj);
            if (state == null) { return null; }

            FacilityContactMechStateDto.DtoConverter dtoConverter = new FacilityContactMechStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toFacilityContactMechStateDto(state);

        
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
                n -> (FacilityContactMechMetadata.aliasMap.containsKey(n) ? FacilityContactMechMetadata.aliasMap.get(n) : n));
            count = facilityContactMechApplicationService.getCount(c);
            return count;

        
    }


    /**
     * Create.
     * Create FacilityContactMech
     */
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public FacilityContactMechId post(@RequestBody CreateOrMergePatchFacilityContactMechDto.CreateFacilityContactMechDto value,  HttpServletResponse response) {
        
            FacilityContactMechCommand.CreateFacilityContactMech cmd = value;//.toCreateFacilityContactMech();
            if (cmd.getFacilityContactMechId() == null) {
                throw DomainError.named("nullId", "Aggregate Id in cmd is null, aggregate name: %1$s.", "FacilityContactMech");
            }
            FacilityContactMechId idObj = cmd.getFacilityContactMechId();
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            facilityContactMechApplicationService.when(cmd);

            return idObj;
        
    }


    /**
     * Create or update.
     * Create or update FacilityContactMech
     */
    @PutMapping("{facilityContactMechId}")
    public void put(@PathVariable("facilityContactMechId") String facilityContactMechId, @RequestBody CreateOrMergePatchFacilityContactMechDto value) {
        
            if (value.getVersion() != null) {
                value.setCommandType(Command.COMMAND_TYPE_MERGE_PATCH);
                FacilityContactMechCommand.MergePatchFacilityContactMech cmd = (FacilityContactMechCommand.MergePatchFacilityContactMech) value.toSubclass();
                FacilityContactMechResourceUtils.setNullIdOrThrowOnInconsistentIds(facilityContactMechId, cmd);
                cmd.setRequesterId(SecurityContextUtil.getRequesterId());
                facilityContactMechApplicationService.when(cmd);
                return;
            }

            value.setCommandType(Command.COMMAND_TYPE_CREATE);
            FacilityContactMechCommand.CreateFacilityContactMech cmd = (FacilityContactMechCommand.CreateFacilityContactMech) value.toSubclass();
            FacilityContactMechResourceUtils.setNullIdOrThrowOnInconsistentIds(facilityContactMechId, cmd);
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            facilityContactMechApplicationService.when(cmd);

        
    }


    /**
     * Patch.
     * Patch FacilityContactMech
     */
    @PatchMapping("{facilityContactMechId}")
    public void patch(@PathVariable("facilityContactMechId") String facilityContactMechId, @RequestBody CreateOrMergePatchFacilityContactMechDto.MergePatchFacilityContactMechDto value) {
        

            FacilityContactMechCommand.MergePatchFacilityContactMech cmd = value;//.toMergePatchFacilityContactMech();
            FacilityContactMechResourceUtils.setNullIdOrThrowOnInconsistentIds(facilityContactMechId, cmd);
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            facilityContactMechApplicationService.when(cmd);

        
    }

    @GetMapping("_metadata/filteringFields")
    public List<PropertyMetadataDto> getMetadataFilteringFields() {
        

            List<PropertyMetadataDto> filtering = new ArrayList<>();
            FacilityContactMechMetadata.propertyTypeMap.forEach((key, value) -> {
                filtering.add(new PropertyMetadataDto(key, value, true));
            });
            return filtering;

        
    }

    @GetMapping("{facilityContactMechId}/_events/{version}")
    @Transactional(readOnly = true)
    public FacilityContactMechEvent getEvent(@PathVariable("facilityContactMechId") String facilityContactMechId, @PathVariable("version") long version) {
        

            FacilityContactMechId idObj = FacilityContactMechResourceUtils.parseIdString(facilityContactMechId);
            //FacilityContactMechStateEventDtoConverter dtoConverter = getFacilityContactMechStateEventDtoConverter();
            return facilityContactMechApplicationService.getEvent(idObj, version);

        
    }

    @GetMapping("{facilityContactMechId}/_historyStates/{version}")
    @Transactional(readOnly = true)
    public FacilityContactMechStateDto getHistoryState(@PathVariable("facilityContactMechId") String facilityContactMechId, @PathVariable("version") long version, @RequestParam(value = "fields", required = false) String fields) {
        

            FacilityContactMechId idObj = FacilityContactMechResourceUtils.parseIdString(facilityContactMechId);
            FacilityContactMechStateDto.DtoConverter dtoConverter = new FacilityContactMechStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toFacilityContactMechStateDto(facilityContactMechApplicationService.getHistoryState(idObj, version));

        
    }

    /**
     * Retrieve.
     * Retrieves FacilityContactMechPurpose with the specified ContactMechPurposeTypeId.
     */
    @GetMapping("{facilityContactMechId}/FacilityContactMechPurposes/{contactMechPurposeTypeId}")
    @Transactional(readOnly = true)
    public FacilityContactMechPurposeStateDto getFacilityContactMechPurpose(@PathVariable("facilityContactMechId") String facilityContactMechId, @PathVariable("contactMechPurposeTypeId") String contactMechPurposeTypeId) {
        

            FacilityContactMechPurposeState state = facilityContactMechApplicationService.getFacilityContactMechPurpose(deserializeJsonArgument(facilityContactMechId, FacilityContactMechId.class), contactMechPurposeTypeId);
            if (state == null) { return null; }
            FacilityContactMechPurposeStateDto.DtoConverter dtoConverter = new FacilityContactMechPurposeStateDto.DtoConverter();
            FacilityContactMechPurposeStateDto stateDto = dtoConverter.toFacilityContactMechPurposeStateDto(state);
            dtoConverter.setAllFieldsReturned(true);
            return stateDto;

        
    }

    /**
     * Create or update.
     * Create or update FacilityContactMechPurpose
     */
    @PutMapping(path = "{facilityContactMechId}/FacilityContactMechPurposes/{contactMechPurposeTypeId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void putFacilityContactMechPurpose(@PathVariable("facilityContactMechId") String facilityContactMechId, @PathVariable("contactMechPurposeTypeId") String contactMechPurposeTypeId,
                       @RequestParam(value = "commandId", required = false) String commandId,
                       @RequestParam(value = "version", required = false) Long version,
                       @RequestParam(value = "requesterId", required = false) String requesterId,
                       @RequestBody CreateOrMergePatchFacilityContactMechPurposeDto.MergePatchFacilityContactMechPurposeDto body) {
        
            FacilityContactMechCommand.MergePatchFacilityContactMech mergePatchFacilityContactMech = new CreateOrMergePatchFacilityContactMechDto.MergePatchFacilityContactMechDto();
            mergePatchFacilityContactMech.setFacilityContactMechId(deserializeJsonArgument(facilityContactMechId, FacilityContactMechId.class));
            mergePatchFacilityContactMech.setCommandId(commandId != null && !commandId.isEmpty() ? commandId : body.getCommandId());
            if (version != null) { mergePatchFacilityContactMech.setVersion(version); }
            mergePatchFacilityContactMech.setRequesterId(requesterId != null && !requesterId.isEmpty() ? requesterId : body.getRequesterId());
            FacilityContactMechPurposeCommand.MergePatchFacilityContactMechPurpose mergePatchFacilityContactMechPurpose = body;//.toMergePatchFacilityContactMechPurpose();
            mergePatchFacilityContactMechPurpose.setContactMechPurposeTypeId(contactMechPurposeTypeId);
            mergePatchFacilityContactMech.getFacilityContactMechPurposeCommands().add(mergePatchFacilityContactMechPurpose);
            mergePatchFacilityContactMech.setRequesterId(SecurityContextUtil.getRequesterId());
            facilityContactMechApplicationService.when(mergePatchFacilityContactMech);
        
    }

    /**
     * Delete.
     * Delete FacilityContactMechPurpose
     */
    @DeleteMapping("{facilityContactMechId}/FacilityContactMechPurposes/{contactMechPurposeTypeId}")
    public void deleteFacilityContactMechPurpose(@PathVariable("facilityContactMechId") String facilityContactMechId, @PathVariable("contactMechPurposeTypeId") String contactMechPurposeTypeId,
                       @RequestParam(value = "commandId", required = false) String commandId,
                       @RequestParam(value = "version", required = false) Long version,
                       @RequestParam(value = "requesterId", required = false) String requesterId) {
        
            FacilityContactMechCommand.MergePatchFacilityContactMech mergePatchFacilityContactMech = new CreateOrMergePatchFacilityContactMechDto.MergePatchFacilityContactMechDto();
            mergePatchFacilityContactMech.setFacilityContactMechId(deserializeJsonArgument(facilityContactMechId, FacilityContactMechId.class));
            mergePatchFacilityContactMech.setCommandId(commandId);// != null && !commandId.isEmpty() ? commandId : body.getCommandId());
            if (version != null) { 
                mergePatchFacilityContactMech.setVersion(version); 
            } else {
                mergePatchFacilityContactMech.setVersion(facilityContactMechApplicationService.get(deserializeJsonArgument(facilityContactMechId, FacilityContactMechId.class)).getVersion());
            }
            mergePatchFacilityContactMech.setRequesterId(requesterId);// != null && !requesterId.isEmpty() ? requesterId : body.getRequesterId());
            FacilityContactMechPurposeCommand.RemoveFacilityContactMechPurpose removeFacilityContactMechPurpose = new RemoveFacilityContactMechPurposeDto();
            removeFacilityContactMechPurpose.setContactMechPurposeTypeId(contactMechPurposeTypeId);
            mergePatchFacilityContactMech.getFacilityContactMechPurposeCommands().add(removeFacilityContactMechPurpose);
            mergePatchFacilityContactMech.setRequesterId(SecurityContextUtil.getRequesterId());
            facilityContactMechApplicationService.when(mergePatchFacilityContactMech);
        
    }

    /**
     * FacilityContactMechPurpose List
     */
    @GetMapping("{facilityContactMechId}/FacilityContactMechPurposes")
    @Transactional(readOnly = true)
    public FacilityContactMechPurposeStateDto[] getFacilityContactMechPurposes(@PathVariable("facilityContactMechId") String facilityContactMechId,
                    @RequestParam(value = "sort", required = false) String sort,
                    @RequestParam(value = "fields", required = false) String fields,
                    @RequestParam(value = "filter", required = false) String filter,
                     HttpServletRequest request) {
        
            CriterionDto criterion = null;
            if (!StringHelper.isNullOrEmpty(filter)) {
                criterion = deserializeCriterionDto(filter);
            } else {
                criterion = QueryParamUtils.getQueryCriterionDto(request.getParameterMap().entrySet().stream()
                    .filter(kv -> FacilityContactMechResourceUtils.getFacilityContactMechPurposeFilterPropertyName(kv.getKey()) != null)
                    .collect(Collectors.toMap(kv -> kv.getKey(), kv -> kv.getValue())));
            }
            Criterion c = CriterionDto.toSubclass(criterion, getCriterionTypeConverter(), getPropertyTypeResolver(), 
                n -> (FacilityContactMechPurposeMetadata.aliasMap.containsKey(n) ? FacilityContactMechPurposeMetadata.aliasMap.get(n) : n));
            Iterable<FacilityContactMechPurposeState> states = facilityContactMechApplicationService.getFacilityContactMechPurposes(deserializeJsonArgument(facilityContactMechId, FacilityContactMechId.class), c,
                    FacilityContactMechResourceUtils.getFacilityContactMechPurposeQuerySorts(request.getParameterMap()));
            if (states == null) { return null; }
            FacilityContactMechPurposeStateDto.DtoConverter dtoConverter = new FacilityContactMechPurposeStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toFacilityContactMechPurposeStateDtoArray(states);
        
    }

    /**
     * Create.
     * Create FacilityContactMechPurpose
     */
    @PostMapping(path = "{facilityContactMechId}/FacilityContactMechPurposes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void postFacilityContactMechPurpose(@PathVariable("facilityContactMechId") String facilityContactMechId,
                       @RequestParam(value = "commandId", required = false) String commandId,
                       @RequestParam(value = "version", required = false) Long version,
                       @RequestParam(value = "requesterId", required = false) String requesterId,
                       @RequestBody CreateOrMergePatchFacilityContactMechPurposeDto.CreateFacilityContactMechPurposeDto body) {
        
            FacilityContactMechCommand.MergePatchFacilityContactMech mergePatchFacilityContactMech = new AbstractFacilityContactMechCommand.SimpleMergePatchFacilityContactMech();
            mergePatchFacilityContactMech.setFacilityContactMechId(deserializeJsonArgument(facilityContactMechId, FacilityContactMechId.class));
            mergePatchFacilityContactMech.setCommandId(commandId != null && !commandId.isEmpty() ? commandId : body.getCommandId());
            if (version != null) { mergePatchFacilityContactMech.setVersion(version); }
            mergePatchFacilityContactMech.setRequesterId(requesterId != null && !requesterId.isEmpty() ? requesterId : body.getRequesterId());
            FacilityContactMechPurposeCommand.CreateFacilityContactMechPurpose createFacilityContactMechPurpose = body.toCreateFacilityContactMechPurpose();
            mergePatchFacilityContactMech.getFacilityContactMechPurposeCommands().add(createFacilityContactMechPurpose);
            mergePatchFacilityContactMech.setRequesterId(SecurityContextUtil.getRequesterId());
            facilityContactMechApplicationService.when(mergePatchFacilityContactMech);
        
    }



    //protected  FacilityContactMechStateEventDtoConverter getFacilityContactMechStateEventDtoConverter() {
    //    return new FacilityContactMechStateEventDtoConverter();
    //}

    protected TypeConverter getCriterionTypeConverter() {
        return new DefaultTypeConverter();
    }

    protected PropertyTypeResolver getPropertyTypeResolver() {
        return new PropertyTypeResolver() {
            @Override
            public Class resolveTypeByPropertyName(String propertyName) {
                return FacilityContactMechResourceUtils.getFilterPropertyType(propertyName);
            }
        };
    }

    protected PropertyTypeResolver getFacilityContactMechPurposePropertyTypeResolver() {
        return new PropertyTypeResolver() {
            @Override
            public Class resolveTypeByPropertyName(String propertyName) {
                return FacilityContactMechResourceUtils.getFacilityContactMechPurposeFilterPropertyType(propertyName);
            }
        };
    }

 
    public static class FacilityContactMechResourceUtils {

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

        public static FacilityContactMechId parseIdString(String idString) {
            try {
                return objectMapper.readValue(idString, FacilityContactMechId.class);
            } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        public static void setNullIdOrThrowOnInconsistentIds(String facilityContactMechId, org.dddml.ffvtraceability.domain.facilitycontactmech.FacilityContactMechCommand value) {
            FacilityContactMechId idObj = parseIdString(facilityContactMechId);
            if (value.getFacilityContactMechId() == null) {
                value.setFacilityContactMechId(idObj);
            } else if (!value.getFacilityContactMechId().equals(idObj)) {
                throw DomainError.named("inconsistentId", "Argument Id %1$s NOT equals body Id %2$s", facilityContactMechId, value.getFacilityContactMechId());
            }
        }
    
        public static List<String> getQueryOrders(String str, String separator) {
            return QueryParamUtils.getQueryOrders(str, separator, FacilityContactMechMetadata.aliasMap);
        }

        public static List<String> getQuerySorts(Map<String, String[]> queryNameValuePairs) {
            String[] values = queryNameValuePairs.get("sort");
            return QueryParamUtils.getQuerySorts(values, FacilityContactMechMetadata.aliasMap);
        }

        public static String getFilterPropertyName(String fieldName) {
            if ("sort".equalsIgnoreCase(fieldName)
                    || "firstResult".equalsIgnoreCase(fieldName)
                    || "maxResults".equalsIgnoreCase(fieldName)
                    || "fields".equalsIgnoreCase(fieldName)) {
                return null;
            }
            if (FacilityContactMechMetadata.aliasMap.containsKey(fieldName)) {
                return FacilityContactMechMetadata.aliasMap.get(fieldName);
            }
            return null;
        }

        public static Class getFilterPropertyType(String propertyName) {
            if (FacilityContactMechMetadata.propertyTypeMap.containsKey(propertyName)) {
                String propertyType = FacilityContactMechMetadata.propertyTypeMap.get(propertyName);
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

        public static List<String> getFacilityContactMechPurposeQueryOrders(String str, String separator) {
            return QueryParamUtils.getQueryOrders(str, separator, FacilityContactMechPurposeMetadata.aliasMap);
        }

        public static List<String> getFacilityContactMechPurposeQuerySorts(Map<String, String[]> queryNameValuePairs) {
            String[] values = queryNameValuePairs.get("sort");
            return QueryParamUtils.getQuerySorts(values, FacilityContactMechPurposeMetadata.aliasMap);
        }

        public static String getFacilityContactMechPurposeFilterPropertyName(String fieldName) {
            if ("sort".equalsIgnoreCase(fieldName)
                    || "firstResult".equalsIgnoreCase(fieldName)
                    || "maxResults".equalsIgnoreCase(fieldName)
                    || "fields".equalsIgnoreCase(fieldName)) {
                return null;
            }
            if (FacilityContactMechPurposeMetadata.aliasMap.containsKey(fieldName)) {
                return FacilityContactMechPurposeMetadata.aliasMap.get(fieldName);
            }
            return null;
        }

        public static Class getFacilityContactMechPurposeFilterPropertyType(String propertyName) {
            if (FacilityContactMechPurposeMetadata.propertyTypeMap.containsKey(propertyName)) {
                String propertyType = FacilityContactMechPurposeMetadata.propertyTypeMap.get(propertyName);
                if (!StringHelper.isNullOrEmpty(propertyType)) {
                    if (BoundedContextMetadata.CLASS_MAP.containsKey(propertyType)) {
                        return BoundedContextMetadata.CLASS_MAP.get(propertyType);
                    }
                }
            }
            return String.class;
        }

        public static Iterable<Map.Entry<String, Object>> getFacilityContactMechPurposeQueryFilterMap(Map<String, String[]> queryNameValuePairs) {
            Map<String, Object> filter = new HashMap<>();
            queryNameValuePairs.forEach((key, values) -> {
                if (values.length > 0) {
                    String pName = getFacilityContactMechPurposeFilterPropertyName(key);
                    if (!StringHelper.isNullOrEmpty(pName)) {
                        Class pClass = getFacilityContactMechPurposeFilterPropertyType(pName);
                        filter.put(pName, ApplicationContext.current.getTypeConverter().convertFromString(pClass, values[0]));
                    }
                }
            });
            return filter.entrySet();
        }

        public static FacilityContactMechStateDto[] toFacilityContactMechStateDtoArray(Iterable<FacilityContactMechId> ids) {
            List<FacilityContactMechStateDto> states = new ArrayList<>();
            ids.forEach(i -> {
                FacilityContactMechStateDto dto = new FacilityContactMechStateDto();
                dto.setFacilityContactMechId(i);
                states.add(dto);
            });
            return states.toArray(new FacilityContactMechStateDto[0]);
        }

    }

}

