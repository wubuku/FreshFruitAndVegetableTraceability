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
import org.dddml.ffvtraceability.domain.supplierproduct.*;
import static org.dddml.ffvtraceability.domain.meta.M.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.dddml.support.criterion.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestMapping(path = "SupplierProducts", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class SupplierProductResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private SupplierProductApplicationService supplierProductApplicationService;


    /**
     * Retrieve.
     * Retrieve SupplierProducts
     */
    @GetMapping
    @Transactional(readOnly = true)
    public SupplierProductStateDto[] getAll( HttpServletRequest request,
                    @RequestParam(value = "sort", required = false) String sort,
                    @RequestParam(value = "fields", required = false) String fields,
                    @RequestParam(value = "firstResult", defaultValue = "0") Integer firstResult,
                    @RequestParam(value = "maxResults", defaultValue = "2147483647") Integer maxResults,
                    @RequestParam(value = "filter", required = false) String filter) {
        try {
        if (firstResult < 0) { firstResult = 0; }
        if (maxResults == null || maxResults < 1) { maxResults = Integer.MAX_VALUE; }

            Iterable<SupplierProductState> states = null; 
            CriterionDto criterion = null;
            if (!StringHelper.isNullOrEmpty(filter)) {
                criterion = new ObjectMapper().readValue(filter, CriterionDto.class);
            } else {
                criterion = QueryParamUtils.getQueryCriterionDto(request.getParameterMap().entrySet().stream()
                    .filter(kv -> SupplierProductResourceUtils.getFilterPropertyName(kv.getKey()) != null)
                    .collect(Collectors.toMap(kv -> kv.getKey(), kv -> kv.getValue())));
            }
            Criterion c = CriterionDto.toSubclass(criterion, getCriterionTypeConverter(), getPropertyTypeResolver(), 
                n -> (SupplierProductMetadata.aliasMap.containsKey(n) ? SupplierProductMetadata.aliasMap.get(n) : n));
            states = supplierProductApplicationService.get(
                c,
                SupplierProductResourceUtils.getQuerySorts(request.getParameterMap()),
                firstResult, maxResults);

            SupplierProductStateDto.DtoConverter dtoConverter = new SupplierProductStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toSupplierProductStateDtoArray(states);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    /**
     * Retrieve in pages.
     * Retrieve SupplierProducts in pages.
     */
    @GetMapping("_page")
    @Transactional(readOnly = true)
    public Page<SupplierProductStateDto> getPage( HttpServletRequest request,
                    @RequestParam(value = "fields", required = false) String fields,
                    @RequestParam(value = "page", defaultValue = "0") Integer page,
                    @RequestParam(value = "size", defaultValue = "20") Integer size,
                    @RequestParam(value = "filter", required = false) String filter) {
        try {
            Integer firstResult = (page == null ? 0 : page) * (size == null ? 20 : size);
            Integer maxResults = (size == null ? 20 : size);
            Iterable<SupplierProductState> states = null; 
            CriterionDto criterion = null;
            if (!StringHelper.isNullOrEmpty(filter)) {
                criterion = new ObjectMapper().readValue(filter, CriterionDto.class);
            } else {
                criterion = QueryParamUtils.getQueryCriterionDto(request.getParameterMap().entrySet().stream()
                    .filter(kv -> SupplierProductResourceUtils.getFilterPropertyName(kv.getKey()) != null)
                    .collect(Collectors.toMap(kv -> kv.getKey(), kv -> kv.getValue())));
            }
            Criterion c = CriterionDto.toSubclass(criterion, getCriterionTypeConverter(), getPropertyTypeResolver(), 
                n -> (SupplierProductMetadata.aliasMap.containsKey(n) ? SupplierProductMetadata.aliasMap.get(n) : n));
            states = supplierProductApplicationService.get(
                c,
                SupplierProductResourceUtils.getQuerySorts(request.getParameterMap()),
                firstResult, maxResults);
            long count = supplierProductApplicationService.getCount(c);

            SupplierProductStateDto.DtoConverter dtoConverter = new SupplierProductStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            Page.PageImpl<SupplierProductStateDto> statePage =  new Page.PageImpl<>(dtoConverter.toSupplierProductStateDtoList(states), count);
            statePage.setSize(size);
            statePage.setNumber(page);
            return statePage;

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    /**
     * Retrieve.
     * Retrieves SupplierProduct with the specified ID.
     */
    @GetMapping("{supplierProductTenantizedId}")
    @Transactional(readOnly = true)
    public SupplierProductStateDto get(@PathVariable("supplierProductTenantizedId") String supplierProductTenantizedId, @RequestParam(value = "fields", required = false) String fields) {
        try {
            SupplierProductAssocId idObj = SupplierProductResourceUtils.parseIdString(supplierProductTenantizedId);
            SupplierProductState state = supplierProductApplicationService.get(idObj);
            if (state == null) { return null; }

            SupplierProductStateDto.DtoConverter dtoConverter = new SupplierProductStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toSupplierProductStateDto(state);

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
                n -> (SupplierProductMetadata.aliasMap.containsKey(n) ? SupplierProductMetadata.aliasMap.get(n) : n));
            count = supplierProductApplicationService.getCount(c);
            return count;

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }


    /**
     * Create.
     * Create SupplierProduct
     */
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public SupplierProductAssocId post(@RequestBody CreateOrMergePatchSupplierProductDto.CreateSupplierProductDto value,  HttpServletResponse response) {
        try {
            SupplierProductCommand.CreateSupplierProduct cmd = value;//.toCreateSupplierProduct();
            if (cmd.getSupplierProductAssocId() == null) {
                throw DomainError.named("nullId", "Aggregate Id in cmd is null, aggregate name: %1$s.", "SupplierProduct");
            }
            SupplierProductAssocId idObj = cmd.getSupplierProductAssocId();
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            supplierProductApplicationService.when(cmd);

            return idObj;
        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }


    /**
     * Create or update.
     * Create or update SupplierProduct
     */
    @PutMapping("{supplierProductTenantizedId}")
    public void put(@PathVariable("supplierProductTenantizedId") String supplierProductTenantizedId, @RequestBody CreateOrMergePatchSupplierProductDto value) {
        try {
            if (value.getVersion() != null) {
                value.setCommandType(Command.COMMAND_TYPE_MERGE_PATCH);
                SupplierProductCommand.MergePatchSupplierProduct cmd = (SupplierProductCommand.MergePatchSupplierProduct) value.toSubclass();
                SupplierProductResourceUtils.setNullIdOrThrowOnInconsistentIds(supplierProductTenantizedId, cmd);
                cmd.setRequesterId(SecurityContextUtil.getRequesterId());
                supplierProductApplicationService.when(cmd);
                return;
            }

            value.setCommandType(Command.COMMAND_TYPE_CREATE);
            SupplierProductCommand.CreateSupplierProduct cmd = (SupplierProductCommand.CreateSupplierProduct) value.toSubclass();
            SupplierProductResourceUtils.setNullIdOrThrowOnInconsistentIds(supplierProductTenantizedId, cmd);
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            supplierProductApplicationService.when(cmd);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }


    /**
     * Patch.
     * Patch SupplierProduct
     */
    @PatchMapping("{supplierProductTenantizedId}")
    public void patch(@PathVariable("supplierProductTenantizedId") String supplierProductTenantizedId, @RequestBody CreateOrMergePatchSupplierProductDto.MergePatchSupplierProductDto value) {
        try {

            SupplierProductCommand.MergePatchSupplierProduct cmd = value;//.toMergePatchSupplierProduct();
            SupplierProductResourceUtils.setNullIdOrThrowOnInconsistentIds(supplierProductTenantizedId, cmd);
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            supplierProductApplicationService.when(cmd);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }


    @PutMapping("{supplierProductAssocId}/_commands/UpdateAvailableThruDate")
    public void updateAvailableThruDate(@PathVariable("supplierProductAssocId") String supplierProductAssocId, @RequestBody SupplierProductCommands.UpdateAvailableThruDate content) {
        try {

            SupplierProductCommands.UpdateAvailableThruDate cmd = content;//.toUpdateAvailableThruDate();
            SupplierProductAssocId idObj = SupplierProductResourceUtils.parseIdString(supplierProductAssocId);
            if (cmd.getSupplierProductTenantizedId() == null) {
                cmd.setSupplierProductTenantizedId(idObj);
            } else if (!cmd.getSupplierProductTenantizedId().equals(idObj)) {
                throw DomainError.named("inconsistentId", "Argument Id %1$s NOT equals body Id %2$s", supplierProductAssocId, cmd.getSupplierProductTenantizedId());
            }
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            supplierProductApplicationService.when(cmd);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }


    @PutMapping("{supplierProductAssocId}/_commands/Disable")
    public void disable(@PathVariable("supplierProductAssocId") String supplierProductAssocId, @RequestBody SupplierProductCommands.Disable content) {
        try {

            SupplierProductCommands.Disable cmd = content;//.toDisable();
            SupplierProductAssocId idObj = SupplierProductResourceUtils.parseIdString(supplierProductAssocId);
            if (cmd.getSupplierProductTenantizedId() == null) {
                cmd.setSupplierProductTenantizedId(idObj);
            } else if (!cmd.getSupplierProductTenantizedId().equals(idObj)) {
                throw DomainError.named("inconsistentId", "Argument Id %1$s NOT equals body Id %2$s", supplierProductAssocId, cmd.getSupplierProductTenantizedId());
            }
            cmd.setRequesterId(SecurityContextUtil.getRequesterId());
            supplierProductApplicationService.when(cmd);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @GetMapping("_metadata/filteringFields")
    public List<PropertyMetadataDto> getMetadataFilteringFields() {
        try {

            List<PropertyMetadataDto> filtering = new ArrayList<>();
            SupplierProductMetadata.propertyTypeMap.forEach((key, value) -> {
                filtering.add(new PropertyMetadataDto(key, value, true));
            });
            return filtering;

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @GetMapping("{supplierProductAssocId}/_events/{version}")
    @Transactional(readOnly = true)
    public SupplierProductEvent getEvent(@PathVariable("supplierProductAssocId") String supplierProductAssocId, @PathVariable("version") long version) {
        try {

            SupplierProductAssocId idObj = SupplierProductResourceUtils.parseIdString(supplierProductAssocId);
            //SupplierProductStateEventDtoConverter dtoConverter = getSupplierProductStateEventDtoConverter();
            return supplierProductApplicationService.getEvent(idObj, version);

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }

    @GetMapping("{supplierProductAssocId}/_historyStates/{version}")
    @Transactional(readOnly = true)
    public SupplierProductStateDto getHistoryState(@PathVariable("supplierProductAssocId") String supplierProductAssocId, @PathVariable("version") long version, @RequestParam(value = "fields", required = false) String fields) {
        try {

            SupplierProductAssocId idObj = SupplierProductResourceUtils.parseIdString(supplierProductAssocId);
            SupplierProductStateDto.DtoConverter dtoConverter = new SupplierProductStateDto.DtoConverter();
            if (StringHelper.isNullOrEmpty(fields)) {
                dtoConverter.setAllFieldsReturned(true);
            } else {
                dtoConverter.setReturnedFieldsString(fields);
            }
            return dtoConverter.toSupplierProductStateDto(supplierProductApplicationService.getHistoryState(idObj, version));

        } catch (Exception ex) { logger.info(ex.getMessage(), ex); throw DomainErrorUtils.convertException(ex); }
    }



    //protected  SupplierProductStateEventDtoConverter getSupplierProductStateEventDtoConverter() {
    //    return new SupplierProductStateEventDtoConverter();
    //}

    protected TypeConverter getCriterionTypeConverter() {
        return new DefaultTypeConverter();
    }

    protected PropertyTypeResolver getPropertyTypeResolver() {
        return new PropertyTypeResolver() {
            @Override
            public Class resolveTypeByPropertyName(String propertyName) {
                return SupplierProductResourceUtils.getFilterPropertyType(propertyName);
            }
        };
    }

    // ////////////////////////////////
 
    public static class SupplierProductResourceUtils {

        public static SupplierProductAssocId parseIdString(String idString) {
            TextFormatter<SupplierProductAssocId> formatter = SupplierProductMetadata.URL_ID_TEXT_FORMATTER;
            return formatter.parse(idString);
        }

        public static void setNullIdOrThrowOnInconsistentIds(String supplierProductAssocId, org.dddml.ffvtraceability.domain.supplierproduct.SupplierProductCommand value) {
            SupplierProductAssocId idObj = parseIdString(supplierProductAssocId);
            if (value.getSupplierProductAssocId() == null) {
                value.setSupplierProductAssocId(idObj);
            } else if (!value.getSupplierProductAssocId().equals(idObj)) {
                throw DomainError.named("inconsistentId", "Argument Id %1$s NOT equals body Id %2$s", supplierProductAssocId, value.getSupplierProductAssocId());
            }
        }
    
        public static List<String> getQueryOrders(String str, String separator) {
            return QueryParamUtils.getQueryOrders(str, separator, SupplierProductMetadata.aliasMap);
        }

        public static List<String> getQuerySorts(Map<String, String[]> queryNameValuePairs) {
            String[] values = queryNameValuePairs.get("sort");
            return QueryParamUtils.getQuerySorts(values, SupplierProductMetadata.aliasMap);
        }

        public static String getFilterPropertyName(String fieldName) {
            if ("sort".equalsIgnoreCase(fieldName)
                    || "firstResult".equalsIgnoreCase(fieldName)
                    || "maxResults".equalsIgnoreCase(fieldName)
                    || "fields".equalsIgnoreCase(fieldName)) {
                return null;
            }
            if (SupplierProductMetadata.aliasMap.containsKey(fieldName)) {
                return SupplierProductMetadata.aliasMap.get(fieldName);
            }
            return null;
        }

        public static Class getFilterPropertyType(String propertyName) {
            if (SupplierProductMetadata.propertyTypeMap.containsKey(propertyName)) {
                String propertyType = SupplierProductMetadata.propertyTypeMap.get(propertyName);
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

        /*
        public static SupplierProductStateDto[] toSupplierProductStateDtoArray(Iterable<SupplierProductTenantizedId> ids) {
            List<SupplierProductStateDto> states = new ArrayList<>();
            ids.forEach(i -> {
                SupplierProductStateDto dto = new SupplierProductStateDto();
                dto.setSupplierProductTenantizedId(i);
                states.add(dto);
            });
            return states.toArray(new SupplierProductStateDto[0]);
        }

        */
    }

}
