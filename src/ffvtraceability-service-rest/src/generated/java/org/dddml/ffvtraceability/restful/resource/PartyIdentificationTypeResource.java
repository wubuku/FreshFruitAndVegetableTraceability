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
import org.dddml.ffvtraceability.domain.partyidentificationtype.*;
import static org.dddml.ffvtraceability.domain.meta.M.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.*;
import org.dddml.support.criterion.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestMapping(path = "PartyIdentificationTypes", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class PartyIdentificationTypeResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private PartyIdentificationTypeApplicationService partyIdentificationTypeApplicationService;


    /**
     * Retrieve.
     * Retrieve PartyIdentificationTypes
     */
    @GetMapping
    @Transactional(readOnly = true)
    public PartyIdentificationTypeStateDto[] getAll( HttpServletRequest request,
                    @RequestParam(value = "sort", required = false) String sort,
                    @RequestParam(value = "fields", required = false) String fields,
                    @RequestParam(value = "firstResult", defaultValue = "0") Integer firstResult,
                    @RequestParam(value = "maxResults", defaultValue = "2147483647") Integer maxResults,
                    @RequestParam(value = "filter", required = false) String filter) {
        try {
        if (firstResult < 0) { firstResult = 0; }
        if (maxResults == null || maxResults < 1) { maxResults = Integer.MAX_VALUE; }

            Iterable<PartyIdentificationTypeState> states = null; 
            CriterionDto criterion = null;
            if (!StringHelper.isNullOrEmpty(filter)) {
                criterion = new ObjectMapper().readValue(filter, CriterionDto.class);
            } else {
                criterion = QueryParamUtils.getQueryCriterionDto(request.getParameterMap().entrySet().stream()
                    .filter(kv -> PartyIdentificationTypeResourceUtils.getFilterPropertyName(kv.getKey()) != null)
                    .collect(Collectors.toMap(kv -> kv.getKey(), kv -> kv.getValue())));
            }
            Criterion c = CriterionDto.toSubclass(criterion, getCriterionTypeConverter(), getPropertyTypeResolver(), 
                n -> (PartyIdentificationTypeMetadata.aliasMap.containsKey(n) ? PartyIdentificationTypeMetadata.aliasMap.get(n) : n));
            states = partyIdentificationTypeApplicationService.get(
                c,
                PartyIdentificationTypeResourceUtils.getQuerySorts(request.getParameterMap()),
                firstResult, maxResults);

            PartyIdentificationTypeStateDto.DtoConverter dtoConverter = new PartyIdentificationTypeStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toPartyIdentificationTypeStateDtoArray(states);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    /**
     * Retrieve in pages.
     * Retrieve PartyIdentificationTypes in pages.
     */
    @GetMapping("_page")
    @Transactional(readOnly = true)
    public Page<PartyIdentificationTypeStateDto> getPage( HttpServletRequest request,
                    @RequestParam(value = "fields", required = false) String fields,
                    @RequestParam(value = "page", defaultValue = "0") Integer page,
                    @RequestParam(value = "size", defaultValue = "20") Integer size,
                    @RequestParam(value = "filter", required = false) String filter) {
        try {
            Integer firstResult = (page == null ? 0 : page) * (size == null ? 20 : size);
            Integer maxResults = (size == null ? 20 : size);
            Iterable<PartyIdentificationTypeState> states = null; 
            CriterionDto criterion = null;
            if (!StringHelper.isNullOrEmpty(filter)) {
                criterion = new ObjectMapper().readValue(filter, CriterionDto.class);
            } else {
                criterion = QueryParamUtils.getQueryCriterionDto(request.getParameterMap().entrySet().stream()
                    .filter(kv -> PartyIdentificationTypeResourceUtils.getFilterPropertyName(kv.getKey()) != null)
                    .collect(Collectors.toMap(kv -> kv.getKey(), kv -> kv.getValue())));
            }
            Criterion c = CriterionDto.toSubclass(criterion, getCriterionTypeConverter(), getPropertyTypeResolver(), 
                n -> (PartyIdentificationTypeMetadata.aliasMap.containsKey(n) ? PartyIdentificationTypeMetadata.aliasMap.get(n) : n));
            states = partyIdentificationTypeApplicationService.get(
                c,
                PartyIdentificationTypeResourceUtils.getQuerySorts(request.getParameterMap()),
                firstResult, maxResults);
            long count = partyIdentificationTypeApplicationService.getCount(c);

            PartyIdentificationTypeStateDto.DtoConverter dtoConverter = new PartyIdentificationTypeStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            Page.PageImpl<PartyIdentificationTypeStateDto> statePage =  new Page.PageImpl<>(dtoConverter.toPartyIdentificationTypeStateDtoList(states), count);
            statePage.setSize(size);
            statePage.setNumber(page);
            return statePage;

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    /**
     * Retrieve.
     * Retrieves PartyIdentificationType with the specified ID.
     */
    @GetMapping("{partyIdentificationTypeId}")
    @Transactional(readOnly = true)
    public PartyIdentificationTypeStateDto get(@PathVariable("partyIdentificationTypeId") String partyIdentificationTypeId, @RequestParam(value = "fields", required = false) String fields) {
        try {
            String idObj = partyIdentificationTypeId;
            PartyIdentificationTypeState state = partyIdentificationTypeApplicationService.get(idObj);
            if (state == null) { return null; }

            PartyIdentificationTypeStateDto.DtoConverter dtoConverter = new PartyIdentificationTypeStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toPartyIdentificationTypeStateDto(state);

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
                n -> (PartyIdentificationTypeMetadata.aliasMap.containsKey(n) ? PartyIdentificationTypeMetadata.aliasMap.get(n) : n));
            count = partyIdentificationTypeApplicationService.getCount(c);
            return count;

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }


    /**
     * Create.
     * Create PartyIdentificationType
     */
    @PostMapping @ResponseBody @ResponseStatus(HttpStatus.CREATED)
    public String post(@RequestBody CreateOrMergePatchPartyIdentificationTypeDto.CreatePartyIdentificationTypeDto value,  HttpServletResponse response) {
        try {
            PartyIdentificationTypeCommand.CreatePartyIdentificationType cmd = value;//.toCreatePartyIdentificationType();
            if (cmd.getPartyIdentificationTypeId() == null) {
                throw DomainError.named("nullId", "Aggregate Id in cmd is null, aggregate name: %1$s.", "PartyIdentificationType");
            }
            String idObj = cmd.getPartyIdentificationTypeId();
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            partyIdentificationTypeApplicationService.when(cmd);

            return idObj;
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }


    /**
     * Create or update.
     * Create or update PartyIdentificationType
     */
    @PutMapping("{partyIdentificationTypeId}")
    public void put(@PathVariable("partyIdentificationTypeId") String partyIdentificationTypeId, @RequestBody CreateOrMergePatchPartyIdentificationTypeDto value) {
        try {
            if (value.getVersion() != null) {
                value.setCommandType(Command.COMMAND_TYPE_MERGE_PATCH);
                PartyIdentificationTypeCommand.MergePatchPartyIdentificationType cmd = (PartyIdentificationTypeCommand.MergePatchPartyIdentificationType) value.toSubclass();
                PartyIdentificationTypeResourceUtils.setNullIdOrThrowOnInconsistentIds(partyIdentificationTypeId, cmd);
                cmd.setRequesterId(SecurityContextUtil.getRequesterId());
                partyIdentificationTypeApplicationService.when(cmd);
                return;
            }

            value.setCommandType(Command.COMMAND_TYPE_CREATE);
            PartyIdentificationTypeCommand.CreatePartyIdentificationType cmd = (PartyIdentificationTypeCommand.CreatePartyIdentificationType) value.toSubclass();
            PartyIdentificationTypeResourceUtils.setNullIdOrThrowOnInconsistentIds(partyIdentificationTypeId, cmd);
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            partyIdentificationTypeApplicationService.when(cmd);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }


    /**
     * Patch.
     * Patch PartyIdentificationType
     */
    @PatchMapping("{partyIdentificationTypeId}")
    public void patch(@PathVariable("partyIdentificationTypeId") String partyIdentificationTypeId, @RequestBody CreateOrMergePatchPartyIdentificationTypeDto.MergePatchPartyIdentificationTypeDto value) {
        try {

            PartyIdentificationTypeCommand.MergePatchPartyIdentificationType cmd = value;//.toMergePatchPartyIdentificationType();
            PartyIdentificationTypeResourceUtils.setNullIdOrThrowOnInconsistentIds(partyIdentificationTypeId, cmd);
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            partyIdentificationTypeApplicationService.when(cmd);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    /**
     * Delete.
     * Delete PartyIdentificationType
     */
    @DeleteMapping("{partyIdentificationTypeId}")
    public void delete(@PathVariable("partyIdentificationTypeId") String partyIdentificationTypeId,
                       @NotNull @RequestParam(value = "commandId", required = false) String commandId,
                       @NotNull @RequestParam(value = "version", required = false) @Min(value = -1) Long version,
                       @RequestParam(value = "requesterId", required = false) String requesterId) {
        try {

            PartyIdentificationTypeCommand.DeletePartyIdentificationType deleteCmd = new DeletePartyIdentificationTypeDto();//.toDeletePartyIdentificationType();;

            deleteCmd.setCommandId(commandId);
            deleteCmd.setRequesterId(requesterId);
            deleteCmd.setVersion(version);
            PartyIdentificationTypeResourceUtils.setNullIdOrThrowOnInconsistentIds(partyIdentificationTypeId, deleteCmd);
            deleteCmd.setRequesterId(SecurityContextUtil.getRequesterId());
            partyIdentificationTypeApplicationService.when(deleteCmd);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @GetMapping("_metadata/filteringFields")
    public List<PropertyMetadataDto> getMetadataFilteringFields() {
        try {

            List<PropertyMetadataDto> filtering = new ArrayList<>();
            PartyIdentificationTypeMetadata.propertyTypeMap.forEach((key, value) -> {
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
                return PartyIdentificationTypeResourceUtils.getFilterPropertyType(propertyName);
            }
        };
    }

 
    public static class PartyIdentificationTypeResourceUtils {

        public static void setNullIdOrThrowOnInconsistentIds(String partyIdentificationTypeId, org.dddml.ffvtraceability.domain.partyidentificationtype.PartyIdentificationTypeCommand value) {
            String idObj = partyIdentificationTypeId;
            if (value.getPartyIdentificationTypeId() == null) {
                value.setPartyIdentificationTypeId(idObj);
            } else if (!value.getPartyIdentificationTypeId().equals(idObj)) {
                throw DomainError.named("inconsistentId", "Argument Id %1$s NOT equals body Id %2$s", partyIdentificationTypeId, value.getPartyIdentificationTypeId());
            }
        }
    
        public static List<String> getQueryOrders(String str, String separator) {
            return QueryParamUtils.getQueryOrders(str, separator, PartyIdentificationTypeMetadata.aliasMap);
        }

        public static List<String> getQuerySorts(Map<String, String[]> queryNameValuePairs) {
            String[] values = queryNameValuePairs.get("sort");
            return QueryParamUtils.getQuerySorts(values, PartyIdentificationTypeMetadata.aliasMap);
        }

        public static String getFilterPropertyName(String fieldName) {
            if ("sort".equalsIgnoreCase(fieldName)
                    || "firstResult".equalsIgnoreCase(fieldName)
                    || "maxResults".equalsIgnoreCase(fieldName)
                    || "fields".equalsIgnoreCase(fieldName)) {
                return null;
            }
            if (PartyIdentificationTypeMetadata.aliasMap.containsKey(fieldName)) {
                return PartyIdentificationTypeMetadata.aliasMap.get(fieldName);
            }
            return null;
        }

        public static Class getFilterPropertyType(String propertyName) {
            if (PartyIdentificationTypeMetadata.propertyTypeMap.containsKey(propertyName)) {
                String propertyType = PartyIdentificationTypeMetadata.propertyTypeMap.get(propertyName);
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

        public static PartyIdentificationTypeStateDto[] toPartyIdentificationTypeStateDtoArray(Iterable<String> ids) {
            List<PartyIdentificationTypeStateDto> states = new ArrayList<>();
            ids.forEach(i -> {
                PartyIdentificationTypeStateDto dto = new PartyIdentificationTypeStateDto();
                dto.setPartyIdentificationTypeId(i);
                states.add(dto);
            });
            return states.toArray(new PartyIdentificationTypeStateDto[0]);
        }

    }

}
