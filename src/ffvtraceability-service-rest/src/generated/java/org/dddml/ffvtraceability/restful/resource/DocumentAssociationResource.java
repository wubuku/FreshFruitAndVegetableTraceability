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
import org.dddml.ffvtraceability.domain.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.domain.documentassociation.*;
import static org.dddml.ffvtraceability.domain.meta.M.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.*;
import org.dddml.support.criterion.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestMapping(path = "DocumentAssociations", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class DocumentAssociationResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private DocumentAssociationApplicationService documentAssociationApplicationService;


    /**
     * Retrieve.
     * Retrieve DocumentAssociations
     */
    @GetMapping
    @Transactional(readOnly = true)
    public DocumentAssociationStateDto[] getAll( HttpServletRequest request,
                    @RequestParam(value = "sort", required = false) String sort,
                    @RequestParam(value = "fields", required = false) String fields,
                    @RequestParam(value = "firstResult", defaultValue = "0") Integer firstResult,
                    @RequestParam(value = "maxResults", defaultValue = "2147483647") Integer maxResults,
                    @RequestParam(value = "filter", required = false) String filter) {
        try {
        if (firstResult < 0) { firstResult = 0; }
        if (maxResults == null || maxResults < 1) { maxResults = Integer.MAX_VALUE; }

            Iterable<DocumentAssociationState> states = null; 
            CriterionDto criterion = null;
            if (!StringHelper.isNullOrEmpty(filter)) {
                criterion = new ObjectMapper().readValue(filter, CriterionDto.class);
            } else {
                criterion = QueryParamUtils.getQueryCriterionDto(request.getParameterMap().entrySet().stream()
                    .filter(kv -> DocumentAssociationResourceUtils.getFilterPropertyName(kv.getKey()) != null)
                    .collect(Collectors.toMap(kv -> kv.getKey(), kv -> kv.getValue())));
            }
            Criterion c = CriterionDto.toSubclass(criterion, getCriterionTypeConverter(), getPropertyTypeResolver(), 
                n -> (DocumentAssociationMetadata.aliasMap.containsKey(n) ? DocumentAssociationMetadata.aliasMap.get(n) : n));
            states = documentAssociationApplicationService.get(
                c,
                DocumentAssociationResourceUtils.getQuerySorts(request.getParameterMap()),
                firstResult, maxResults);

            DocumentAssociationStateDto.DtoConverter dtoConverter = new DocumentAssociationStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toDocumentAssociationStateDtoArray(states);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    /**
     * Retrieve in pages.
     * Retrieve DocumentAssociations in pages.
     */
    @GetMapping("_page")
    @Transactional(readOnly = true)
    public Page<DocumentAssociationStateDto> getPage( HttpServletRequest request,
                    @RequestParam(value = "fields", required = false) String fields,
                    @RequestParam(value = "page", defaultValue = "0") Integer page,
                    @RequestParam(value = "size", defaultValue = "20") Integer size,
                    @RequestParam(value = "filter", required = false) String filter) {
        try {
            Integer firstResult = (page == null ? 0 : page) * (size == null ? 20 : size);
            Integer maxResults = (size == null ? 20 : size);
            Iterable<DocumentAssociationState> states = null; 
            CriterionDto criterion = null;
            if (!StringHelper.isNullOrEmpty(filter)) {
                criterion = new ObjectMapper().readValue(filter, CriterionDto.class);
            } else {
                criterion = QueryParamUtils.getQueryCriterionDto(request.getParameterMap().entrySet().stream()
                    .filter(kv -> DocumentAssociationResourceUtils.getFilterPropertyName(kv.getKey()) != null)
                    .collect(Collectors.toMap(kv -> kv.getKey(), kv -> kv.getValue())));
            }
            Criterion c = CriterionDto.toSubclass(criterion, getCriterionTypeConverter(), getPropertyTypeResolver(), 
                n -> (DocumentAssociationMetadata.aliasMap.containsKey(n) ? DocumentAssociationMetadata.aliasMap.get(n) : n));
            states = documentAssociationApplicationService.get(
                c,
                DocumentAssociationResourceUtils.getQuerySorts(request.getParameterMap()),
                firstResult, maxResults);
            long count = documentAssociationApplicationService.getCount(c);

            DocumentAssociationStateDto.DtoConverter dtoConverter = new DocumentAssociationStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            Page.PageImpl<DocumentAssociationStateDto> statePage =  new Page.PageImpl<>(dtoConverter.toDocumentAssociationStateDtoList(states), count);
            statePage.setSize(size);
            statePage.setNumber(page);
            return statePage;

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    /**
     * Retrieve.
     * Retrieves DocumentAssociation with the specified ID.
     */
    @GetMapping("{documentAssociationId}")
    @Transactional(readOnly = true)
    public DocumentAssociationStateDto get(@PathVariable("documentAssociationId") String documentAssociationId, @RequestParam(value = "fields", required = false) String fields) {
        try {
            DocumentAssociationId idObj = DocumentAssociationResourceUtils.parseIdString(documentAssociationId);
            DocumentAssociationState state = documentAssociationApplicationService.get(idObj);
            if (state == null) { return null; }

            DocumentAssociationStateDto.DtoConverter dtoConverter = new DocumentAssociationStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toDocumentAssociationStateDto(state);

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
                n -> (DocumentAssociationMetadata.aliasMap.containsKey(n) ? DocumentAssociationMetadata.aliasMap.get(n) : n));
            count = documentAssociationApplicationService.getCount(c);
            return count;

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }


    /**
     * Create.
     * Create DocumentAssociation
     */
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public DocumentAssociationId post(@RequestBody CreateOrMergePatchDocumentAssociationDto.CreateDocumentAssociationDto value,  HttpServletResponse response) {
        try {
            DocumentAssociationCommand.CreateDocumentAssociation cmd = value;//.toCreateDocumentAssociation();
            if (cmd.getDocumentAssociationId() == null) {
                throw DomainError.named("nullId", "Aggregate Id in cmd is null, aggregate name: %1$s.", "DocumentAssociation");
            }
            DocumentAssociationId idObj = cmd.getDocumentAssociationId();
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            documentAssociationApplicationService.when(cmd);

            return idObj;
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }


    /**
     * Create or update.
     * Create or update DocumentAssociation
     */
    @PutMapping("{documentAssociationId}")
    public void put(@PathVariable("documentAssociationId") String documentAssociationId, @RequestBody CreateOrMergePatchDocumentAssociationDto value) {
        try {
            if (value.getVersion() != null) {
                value.setCommandType(Command.COMMAND_TYPE_MERGE_PATCH);
                DocumentAssociationCommand.MergePatchDocumentAssociation cmd = (DocumentAssociationCommand.MergePatchDocumentAssociation) value.toSubclass();
                DocumentAssociationResourceUtils.setNullIdOrThrowOnInconsistentIds(documentAssociationId, cmd);
                cmd.setRequesterId(SecurityContextUtil.getRequesterId());
                documentAssociationApplicationService.when(cmd);
                return;
            }

            value.setCommandType(Command.COMMAND_TYPE_CREATE);
            DocumentAssociationCommand.CreateDocumentAssociation cmd = (DocumentAssociationCommand.CreateDocumentAssociation) value.toSubclass();
            DocumentAssociationResourceUtils.setNullIdOrThrowOnInconsistentIds(documentAssociationId, cmd);
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            documentAssociationApplicationService.when(cmd);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }


    /**
     * Patch.
     * Patch DocumentAssociation
     */
    @PatchMapping("{documentAssociationId}")
    public void patch(@PathVariable("documentAssociationId") String documentAssociationId, @RequestBody CreateOrMergePatchDocumentAssociationDto.MergePatchDocumentAssociationDto value) {
        try {

            DocumentAssociationCommand.MergePatchDocumentAssociation cmd = value;//.toMergePatchDocumentAssociation();
            DocumentAssociationResourceUtils.setNullIdOrThrowOnInconsistentIds(documentAssociationId, cmd);
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            documentAssociationApplicationService.when(cmd);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    /**
     * Delete.
     * Delete DocumentAssociation
     */
    @DeleteMapping("{documentAssociationId}")
    public void delete(@PathVariable("documentAssociationId") String documentAssociationId,
                       @NotNull @RequestParam(value = "commandId", required = false) String commandId,
                       @NotNull @RequestParam(value = "version", required = false) @Min(value = -1) Long version,
                       @RequestParam(value = "requesterId", required = false) String requesterId) {
        try {

            DocumentAssociationCommand.DeleteDocumentAssociation deleteCmd = new DeleteDocumentAssociationDto();

            deleteCmd.setCommandId(commandId);
            deleteCmd.setRequesterId(requesterId);
            deleteCmd.setVersion(version);
            DocumentAssociationResourceUtils.setNullIdOrThrowOnInconsistentIds(documentAssociationId, deleteCmd);
            deleteCmd.setRequesterId(SecurityContextUtil.getRequesterId());
            documentAssociationApplicationService.when(deleteCmd);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @GetMapping("_metadata/filteringFields")
    public List<PropertyMetadataDto> getMetadataFilteringFields() {
        try {

            List<PropertyMetadataDto> filtering = new ArrayList<>();
            DocumentAssociationMetadata.propertyTypeMap.forEach((key, value) -> {
                filtering.add(new PropertyMetadataDto(key, value, true));
            });
            return filtering;

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @GetMapping("{documentAssociationId}/_events/{version}")
    @Transactional(readOnly = true)
    public DocumentAssociationEvent getEvent(@PathVariable("documentAssociationId") String documentAssociationId, @PathVariable("version") long version) {
        try {

            DocumentAssociationId idObj = DocumentAssociationResourceUtils.parseIdString(documentAssociationId);
            //DocumentAssociationStateEventDtoConverter dtoConverter = getDocumentAssociationStateEventDtoConverter();
            return documentAssociationApplicationService.getEvent(idObj, version);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @GetMapping("{documentAssociationId}/_historyStates/{version}")
    @Transactional(readOnly = true)
    public DocumentAssociationStateDto getHistoryState(@PathVariable("documentAssociationId") String documentAssociationId, @PathVariable("version") long version, @RequestParam(value = "fields", required = false) String fields) {
        try {

            DocumentAssociationId idObj = DocumentAssociationResourceUtils.parseIdString(documentAssociationId);
            DocumentAssociationStateDto.DtoConverter dtoConverter = new DocumentAssociationStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toDocumentAssociationStateDto(documentAssociationApplicationService.getHistoryState(idObj, version));

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }



    //protected  DocumentAssociationStateEventDtoConverter getDocumentAssociationStateEventDtoConverter() {
    //    return new DocumentAssociationStateEventDtoConverter();
    //}

    protected TypeConverter getCriterionTypeConverter() {
        return new DefaultTypeConverter();
    }

    protected PropertyTypeResolver getPropertyTypeResolver() {
        return new PropertyTypeResolver() {
            @Override
            public Class resolveTypeByPropertyName(String propertyName) {
                return DocumentAssociationResourceUtils.getFilterPropertyType(propertyName);
            }
        };
    }

 
    public static class DocumentAssociationResourceUtils {

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

        public static DocumentAssociationId parseIdString(String idString) {
            try {
                return objectMapper.readValue(idString, DocumentAssociationId.class);
            } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        public static void setNullIdOrThrowOnInconsistentIds(String documentAssociationId, org.dddml.ffvtraceability.domain.documentassociation.DocumentAssociationCommand value) {
            DocumentAssociationId idObj = parseIdString(documentAssociationId);
            if (value.getDocumentAssociationId() == null) {
                value.setDocumentAssociationId(idObj);
            } else if (!value.getDocumentAssociationId().equals(idObj)) {
                throw DomainError.named("inconsistentId", "Argument Id %1$s NOT equals body Id %2$s", documentAssociationId, value.getDocumentAssociationId());
            }
        }
    
        public static List<String> getQueryOrders(String str, String separator) {
            return QueryParamUtils.getQueryOrders(str, separator, DocumentAssociationMetadata.aliasMap);
        }

        public static List<String> getQuerySorts(Map<String, String[]> queryNameValuePairs) {
            String[] values = queryNameValuePairs.get("sort");
            return QueryParamUtils.getQuerySorts(values, DocumentAssociationMetadata.aliasMap);
        }

        public static String getFilterPropertyName(String fieldName) {
            if ("sort".equalsIgnoreCase(fieldName)
                    || "firstResult".equalsIgnoreCase(fieldName)
                    || "maxResults".equalsIgnoreCase(fieldName)
                    || "fields".equalsIgnoreCase(fieldName)) {
                return null;
            }
            if (DocumentAssociationMetadata.aliasMap.containsKey(fieldName)) {
                return DocumentAssociationMetadata.aliasMap.get(fieldName);
            }
            return null;
        }

        public static Class getFilterPropertyType(String propertyName) {
            if (DocumentAssociationMetadata.propertyTypeMap.containsKey(propertyName)) {
                String propertyType = DocumentAssociationMetadata.propertyTypeMap.get(propertyName);
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

        public static DocumentAssociationStateDto[] toDocumentAssociationStateDtoArray(Iterable<DocumentAssociationId> ids) {
            List<DocumentAssociationStateDto> states = new ArrayList<>();
            ids.forEach(i -> {
                DocumentAssociationStateDto dto = new DocumentAssociationStateDto();
                dto.setDocumentAssociationId(i);
                states.add(dto);
            });
            return states.toArray(new DocumentAssociationStateDto[0]);
        }

    }

}
