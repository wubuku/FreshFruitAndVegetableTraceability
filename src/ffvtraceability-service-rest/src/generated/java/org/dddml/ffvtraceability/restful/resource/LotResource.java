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
import org.dddml.ffvtraceability.domain.lot.*;
import static org.dddml.ffvtraceability.domain.meta.M.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.*;
import org.dddml.support.criterion.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestMapping(path = "Lots", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class LotResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private LotApplicationService lotApplicationService;


    /**
     * Retrieve.
     * Retrieve Lots
     */
    @GetMapping
    @Transactional(readOnly = true)
    public LotStateDto[] getAll( HttpServletRequest request,
                    @RequestParam(value = "sort", required = false) String sort,
                    @RequestParam(value = "fields", required = false) String fields,
                    @RequestParam(value = "firstResult", defaultValue = "0") Integer firstResult,
                    @RequestParam(value = "maxResults", defaultValue = "2147483647") Integer maxResults,
                    @RequestParam(value = "filter", required = false) String filter) {
        try {
        if (firstResult < 0) { firstResult = 0; }
        if (maxResults == null || maxResults < 1) { maxResults = Integer.MAX_VALUE; }

            Iterable<LotState> states = null; 
            CriterionDto criterion = null;
            if (!StringHelper.isNullOrEmpty(filter)) {
                criterion = new ObjectMapper().readValue(filter, CriterionDto.class);
            } else {
                criterion = QueryParamUtils.getQueryCriterionDto(request.getParameterMap().entrySet().stream()
                    .filter(kv -> LotResourceUtils.getFilterPropertyName(kv.getKey()) != null)
                    .collect(Collectors.toMap(kv -> kv.getKey(), kv -> kv.getValue())));
            }
            Criterion c = CriterionDto.toSubclass(criterion, getCriterionTypeConverter(), getPropertyTypeResolver(), 
                n -> (LotMetadata.aliasMap.containsKey(n) ? LotMetadata.aliasMap.get(n) : n));
            states = lotApplicationService.get(
                c,
                LotResourceUtils.getQuerySorts(request.getParameterMap()),
                firstResult, maxResults);

            LotStateDto.DtoConverter dtoConverter = new LotStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toLotStateDtoArray(states);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    /**
     * Retrieve in pages.
     * Retrieve Lots in pages.
     */
    @GetMapping("_page")
    @Transactional(readOnly = true)
    public Page<LotStateDto> getPage( HttpServletRequest request,
                    @RequestParam(value = "fields", required = false) String fields,
                    @RequestParam(value = "page", defaultValue = "0") Integer page,
                    @RequestParam(value = "size", defaultValue = "20") Integer size,
                    @RequestParam(value = "filter", required = false) String filter) {
        try {
            Integer firstResult = (page == null ? 0 : page) * (size == null ? 20 : size);
            Integer maxResults = (size == null ? 20 : size);
            Iterable<LotState> states = null; 
            CriterionDto criterion = null;
            if (!StringHelper.isNullOrEmpty(filter)) {
                criterion = new ObjectMapper().readValue(filter, CriterionDto.class);
            } else {
                criterion = QueryParamUtils.getQueryCriterionDto(request.getParameterMap().entrySet().stream()
                    .filter(kv -> LotResourceUtils.getFilterPropertyName(kv.getKey()) != null)
                    .collect(Collectors.toMap(kv -> kv.getKey(), kv -> kv.getValue())));
            }
            Criterion c = CriterionDto.toSubclass(criterion, getCriterionTypeConverter(), getPropertyTypeResolver(), 
                n -> (LotMetadata.aliasMap.containsKey(n) ? LotMetadata.aliasMap.get(n) : n));
            states = lotApplicationService.get(
                c,
                LotResourceUtils.getQuerySorts(request.getParameterMap()),
                firstResult, maxResults);
            long count = lotApplicationService.getCount(c);

            LotStateDto.DtoConverter dtoConverter = new LotStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            Page.PageImpl<LotStateDto> statePage =  new Page.PageImpl<>(dtoConverter.toLotStateDtoList(states), count);
            statePage.setSize(size);
            statePage.setNumber(page);
            return statePage;

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    /**
     * Retrieve.
     * Retrieves Lot with the specified ID.
     */
    @GetMapping("{lotId}")
    @Transactional(readOnly = true)
    public LotStateDto get(@PathVariable("lotId") String lotId, @RequestParam(value = "fields", required = false) String fields) {
        try {
            String idObj = lotId;
            LotState state = lotApplicationService.get(idObj);
            if (state == null) { return null; }

            LotStateDto.DtoConverter dtoConverter = new LotStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toLotStateDto(state);

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
                n -> (LotMetadata.aliasMap.containsKey(n) ? LotMetadata.aliasMap.get(n) : n));
            count = lotApplicationService.getCount(c);
            return count;

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }


    /**
     * Create.
     * Create Lot
     */
    @PostMapping @ResponseBody @ResponseStatus(HttpStatus.CREATED)
    public String post(@RequestBody CreateOrMergePatchLotDto.CreateLotDto value,  HttpServletResponse response) {
        try {
            LotCommand.CreateLot cmd = value;//.toCreateLot();
            if (cmd.getLotId() == null) {
                throw DomainError.named("nullId", "Aggregate Id in cmd is null, aggregate name: %1$s.", "Lot");
            }
            String idObj = cmd.getLotId();
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            lotApplicationService.when(cmd);

            return idObj;
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }


    /**
     * Create or update.
     * Create or update Lot
     */
    @PutMapping("{lotId}")
    public void put(@PathVariable("lotId") String lotId, @RequestBody CreateOrMergePatchLotDto value) {
        try {
            if (value.getVersion() != null) {
                value.setCommandType(Command.COMMAND_TYPE_MERGE_PATCH);
                LotCommand.MergePatchLot cmd = (LotCommand.MergePatchLot) value.toSubclass();
                LotResourceUtils.setNullIdOrThrowOnInconsistentIds(lotId, cmd);
                cmd.setRequesterId(SecurityContextUtil.getRequesterId());
                lotApplicationService.when(cmd);
                return;
            }

            value.setCommandType(Command.COMMAND_TYPE_CREATE);
            LotCommand.CreateLot cmd = (LotCommand.CreateLot) value.toSubclass();
            LotResourceUtils.setNullIdOrThrowOnInconsistentIds(lotId, cmd);
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            lotApplicationService.when(cmd);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }


    /**
     * Patch.
     * Patch Lot
     */
    @PatchMapping("{lotId}")
    public void patch(@PathVariable("lotId") String lotId, @RequestBody CreateOrMergePatchLotDto.MergePatchLotDto value) {
        try {

            LotCommand.MergePatchLot cmd = value;//.toMergePatchLot();
            LotResourceUtils.setNullIdOrThrowOnInconsistentIds(lotId, cmd);
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            lotApplicationService.when(cmd);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    /**
     * Delete.
     * Delete Lot
     */
    @DeleteMapping("{lotId}")
    public void delete(@PathVariable("lotId") String lotId,
                       @NotNull @RequestParam(value = "commandId", required = false) String commandId,
                       @NotNull @RequestParam(value = "version", required = false) @Min(value = -1) Long version,
                       @RequestParam(value = "requesterId", required = false) String requesterId) {
        try {

            LotCommand.DeleteLot deleteCmd = new DeleteLotDto();

            deleteCmd.setCommandId(commandId);
            deleteCmd.setRequesterId(requesterId);
            deleteCmd.setVersion(version);
            LotResourceUtils.setNullIdOrThrowOnInconsistentIds(lotId, deleteCmd);
            deleteCmd.setRequesterId(SecurityContextUtil.getRequesterId());
            lotApplicationService.when(deleteCmd);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @GetMapping("_metadata/filteringFields")
    public List<PropertyMetadataDto> getMetadataFilteringFields() {
        try {

            List<PropertyMetadataDto> filtering = new ArrayList<>();
            LotMetadata.propertyTypeMap.forEach((key, value) -> {
                filtering.add(new PropertyMetadataDto(key, value, true));
            });
            return filtering;

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @GetMapping("{lotId}/_events/{version}")
    @Transactional(readOnly = true)
    public LotEvent getEvent(@PathVariable("lotId") String lotId, @PathVariable("version") long version) {
        try {

            String idObj = lotId;
            //LotStateEventDtoConverter dtoConverter = getLotStateEventDtoConverter();
            return lotApplicationService.getEvent(idObj, version);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @GetMapping("{lotId}/_historyStates/{version}")
    @Transactional(readOnly = true)
    public LotStateDto getHistoryState(@PathVariable("lotId") String lotId, @PathVariable("version") long version, @RequestParam(value = "fields", required = false) String fields) {
        try {

            String idObj = lotId;
            LotStateDto.DtoConverter dtoConverter = new LotStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toLotStateDto(lotApplicationService.getHistoryState(idObj, version));

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    /**
     * Retrieve.
     * Retrieves LotIdentification with the specified LotIdentificationTypeId.
     */
    @GetMapping("{lotId}/LotIdentifications/{lotIdentificationTypeId}")
    @Transactional(readOnly = true)
    public LotIdentificationStateDto getLotIdentification(@PathVariable("lotId") String lotId, @PathVariable("lotIdentificationTypeId") String lotIdentificationTypeId) {
        try {

            LotIdentificationState state = lotApplicationService.getLotIdentification(lotId, lotIdentificationTypeId);
            if (state == null) { return null; }
            LotIdentificationStateDto.DtoConverter dtoConverter = new LotIdentificationStateDto.DtoConverter();
            LotIdentificationStateDto stateDto = dtoConverter.toLotIdentificationStateDto(state);
            dtoConverter.setAllFieldsReturned(true);
            return stateDto;

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    /**
     * Create or update.
     * Create or update LotIdentification
     */
    @PutMapping(path = "{lotId}/LotIdentifications/{lotIdentificationTypeId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void putLotIdentification(@PathVariable("lotId") String lotId, @PathVariable("lotIdentificationTypeId") String lotIdentificationTypeId,
                       @RequestParam(value = "commandId", required = false) String commandId,
                       @RequestParam(value = "version", required = false) Long version,
                       @RequestParam(value = "requesterId", required = false) String requesterId,
                       @RequestBody CreateOrMergePatchLotIdentificationDto.MergePatchLotIdentificationDto body) {
        try {
            LotCommand.MergePatchLot mergePatchLot = new CreateOrMergePatchLotDto.MergePatchLotDto();
            mergePatchLot.setLotId(lotId);
            mergePatchLot.setCommandId(commandId != null && !commandId.isEmpty() ? commandId : body.getCommandId());
            if (version != null) { mergePatchLot.setVersion(version); }
            mergePatchLot.setRequesterId(requesterId != null && !requesterId.isEmpty() ? requesterId : body.getRequesterId());
            LotIdentificationCommand.MergePatchLotIdentification mergePatchLotIdentification = body;//.toMergePatchLotIdentification();
            mergePatchLotIdentification.setLotIdentificationTypeId(lotIdentificationTypeId);
            mergePatchLot.getLotIdentificationCommands().add(mergePatchLotIdentification);
            mergePatchLot.setRequesterId(SecurityContextUtil.getRequesterId());
            lotApplicationService.when(mergePatchLot);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    /**
     * Delete.
     * Delete LotIdentification
     */
    @DeleteMapping("{lotId}/LotIdentifications/{lotIdentificationTypeId}")
    public void deleteLotIdentification(@PathVariable("lotId") String lotId, @PathVariable("lotIdentificationTypeId") String lotIdentificationTypeId,
                       @RequestParam(value = "commandId", required = false) String commandId,
                       @RequestParam(value = "version", required = false) Long version,
                       @RequestParam(value = "requesterId", required = false) String requesterId) {
        try {
            LotCommand.MergePatchLot mergePatchLot = new CreateOrMergePatchLotDto.MergePatchLotDto();
            mergePatchLot.setLotId(lotId);
            mergePatchLot.setCommandId(commandId);// != null && !commandId.isEmpty() ? commandId : body.getCommandId());
            if (version != null) { 
                mergePatchLot.setVersion(version); 
            } else {
                mergePatchLot.setVersion(lotApplicationService.get(lotId).getVersion());
            }
            mergePatchLot.setRequesterId(requesterId);// != null && !requesterId.isEmpty() ? requesterId : body.getRequesterId());
            LotIdentificationCommand.RemoveLotIdentification removeLotIdentification = new RemoveLotIdentificationDto();
            removeLotIdentification.setLotIdentificationTypeId(lotIdentificationTypeId);
            mergePatchLot.getLotIdentificationCommands().add(removeLotIdentification);
            mergePatchLot.setRequesterId(SecurityContextUtil.getRequesterId());
            lotApplicationService.when(mergePatchLot);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    /**
     * LotIdentification List
     */
    @GetMapping("{lotId}/LotIdentifications")
    @Transactional(readOnly = true)
    public LotIdentificationStateDto[] getLotIdentifications(@PathVariable("lotId") String lotId,
                    @RequestParam(value = "sort", required = false) String sort,
                    @RequestParam(value = "fields", required = false) String fields,
                    @RequestParam(value = "filter", required = false) String filter,
                     HttpServletRequest request) {
        try {
            CriterionDto criterion = null;
            if (!StringHelper.isNullOrEmpty(filter)) {
                criterion = new ObjectMapper().readValue(filter, CriterionDto.class);
            } else {
                criterion = QueryParamUtils.getQueryCriterionDto(request.getParameterMap().entrySet().stream()
                    .filter(kv -> LotResourceUtils.getLotIdentificationFilterPropertyName(kv.getKey()) != null)
                    .collect(Collectors.toMap(kv -> kv.getKey(), kv -> kv.getValue())));
            }
            Criterion c = CriterionDto.toSubclass(criterion, getCriterionTypeConverter(), getPropertyTypeResolver(), 
                n -> (LotIdentificationMetadata.aliasMap.containsKey(n) ? LotIdentificationMetadata.aliasMap.get(n) : n));
            Iterable<LotIdentificationState> states = lotApplicationService.getLotIdentifications(lotId, c,
                    LotResourceUtils.getLotIdentificationQuerySorts(request.getParameterMap()));
            if (states == null) { return null; }
            LotIdentificationStateDto.DtoConverter dtoConverter = new LotIdentificationStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toLotIdentificationStateDtoArray(states);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    /**
     * Create.
     * Create LotIdentification
     */
    @PostMapping(path = "{lotId}/LotIdentifications", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void postLotIdentifications(@PathVariable("lotId") String lotId,
                       @RequestParam(value = "commandId", required = false) String commandId,
                       @RequestParam(value = "version", required = false) Long version,
                       @RequestParam(value = "requesterId", required = false) String requesterId,
                       @RequestBody CreateOrMergePatchLotIdentificationDto.CreateLotIdentificationDto body) {
        try {
            LotCommand.MergePatchLot mergePatchLot = new AbstractLotCommand.SimpleMergePatchLot();
            mergePatchLot.setLotId(lotId);
            mergePatchLot.setCommandId(commandId != null && !commandId.isEmpty() ? commandId : body.getCommandId());
            if (version != null) { mergePatchLot.setVersion(version); }
            mergePatchLot.setRequesterId(requesterId != null && !requesterId.isEmpty() ? requesterId : body.getRequesterId());
            LotIdentificationCommand.CreateLotIdentification createLotIdentification = body.toCreateLotIdentification();
            mergePatchLot.getLotIdentificationCommands().add(createLotIdentification);
            mergePatchLot.setRequesterId(SecurityContextUtil.getRequesterId());
            lotApplicationService.when(mergePatchLot);
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }



    //protected  LotStateEventDtoConverter getLotStateEventDtoConverter() {
    //    return new LotStateEventDtoConverter();
    //}

    protected TypeConverter getCriterionTypeConverter() {
        return new DefaultTypeConverter();
    }

    protected PropertyTypeResolver getPropertyTypeResolver() {
        return new PropertyTypeResolver() {
            @Override
            public Class resolveTypeByPropertyName(String propertyName) {
                return LotResourceUtils.getFilterPropertyType(propertyName);
            }
        };
    }

    protected PropertyTypeResolver getLotIdentificationPropertyTypeResolver() {
        return new PropertyTypeResolver() {
            @Override
            public Class resolveTypeByPropertyName(String propertyName) {
                return LotResourceUtils.getLotIdentificationFilterPropertyType(propertyName);
            }
        };
    }

 
    public static class LotResourceUtils {

        public static void setNullIdOrThrowOnInconsistentIds(String lotId, org.dddml.ffvtraceability.domain.lot.LotCommand value) {
            String idObj = lotId;
            if (value.getLotId() == null) {
                value.setLotId(idObj);
            } else if (!value.getLotId().equals(idObj)) {
                throw DomainError.named("inconsistentId", "Argument Id %1$s NOT equals body Id %2$s", lotId, value.getLotId());
            }
        }
    
        public static List<String> getQueryOrders(String str, String separator) {
            return QueryParamUtils.getQueryOrders(str, separator, LotMetadata.aliasMap);
        }

        public static List<String> getQuerySorts(Map<String, String[]> queryNameValuePairs) {
            String[] values = queryNameValuePairs.get("sort");
            return QueryParamUtils.getQuerySorts(values, LotMetadata.aliasMap);
        }

        public static String getFilterPropertyName(String fieldName) {
            if ("sort".equalsIgnoreCase(fieldName)
                    || "firstResult".equalsIgnoreCase(fieldName)
                    || "maxResults".equalsIgnoreCase(fieldName)
                    || "fields".equalsIgnoreCase(fieldName)) {
                return null;
            }
            if (LotMetadata.aliasMap.containsKey(fieldName)) {
                return LotMetadata.aliasMap.get(fieldName);
            }
            return null;
        }

        public static Class getFilterPropertyType(String propertyName) {
            if (LotMetadata.propertyTypeMap.containsKey(propertyName)) {
                String propertyType = LotMetadata.propertyTypeMap.get(propertyName);
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

        public static List<String> getLotIdentificationQueryOrders(String str, String separator) {
            return QueryParamUtils.getQueryOrders(str, separator, LotIdentificationMetadata.aliasMap);
        }

        public static List<String> getLotIdentificationQuerySorts(Map<String, String[]> queryNameValuePairs) {
            String[] values = queryNameValuePairs.get("sort");
            return QueryParamUtils.getQuerySorts(values, LotIdentificationMetadata.aliasMap);
        }

        public static String getLotIdentificationFilterPropertyName(String fieldName) {
            if ("sort".equalsIgnoreCase(fieldName)
                    || "firstResult".equalsIgnoreCase(fieldName)
                    || "maxResults".equalsIgnoreCase(fieldName)
                    || "fields".equalsIgnoreCase(fieldName)) {
                return null;
            }
            if (LotIdentificationMetadata.aliasMap.containsKey(fieldName)) {
                return LotIdentificationMetadata.aliasMap.get(fieldName);
            }
            return null;
        }

        public static Class getLotIdentificationFilterPropertyType(String propertyName) {
            if (LotIdentificationMetadata.propertyTypeMap.containsKey(propertyName)) {
                String propertyType = LotIdentificationMetadata.propertyTypeMap.get(propertyName);
                if (!StringHelper.isNullOrEmpty(propertyType)) {
                    if (BoundedContextMetadata.CLASS_MAP.containsKey(propertyType)) {
                        return BoundedContextMetadata.CLASS_MAP.get(propertyType);
                    }
                }
            }
            return String.class;
        }

        public static Iterable<Map.Entry<String, Object>> getLotIdentificationQueryFilterMap(Map<String, String[]> queryNameValuePairs) {
            Map<String, Object> filter = new HashMap<>();
            queryNameValuePairs.forEach((key, values) -> {
                if (values.length > 0) {
                    String pName = getLotIdentificationFilterPropertyName(key);
                    if (!StringHelper.isNullOrEmpty(pName)) {
                        Class pClass = getLotIdentificationFilterPropertyType(pName);
                        filter.put(pName, ApplicationContext.current.getTypeConverter().convertFromString(pClass, values[0]));
                    }
                }
            });
            return filter.entrySet();
        }

        public static LotStateDto[] toLotStateDtoArray(Iterable<String> ids) {
            List<LotStateDto> states = new ArrayList<>();
            ids.forEach(i -> {
                LotStateDto dto = new LotStateDto();
                dto.setLotId(i);
                states.add(dto);
            });
            return states.toArray(new LotStateDto[0]);
        }

    }

}
