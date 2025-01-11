package org.dddml.ffvtraceability.restful.resource;

import org.dddml.ffvtraceability.domain.TenantContext;
import org.dddml.ffvtraceability.domain.tenant.TenantApplicationService;
import org.dddml.ffvtraceability.domain.tenant.TenantState;
import org.dddml.ffvtraceability.domain.tenant.TenantStateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "BffTenants", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class BffTenantResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TenantApplicationService tenantApplicationService;

    @GetMapping("current")
    public TenantStateDto getCurrentTenant() {
        TenantStateDto.DtoConverter dtoConverter = new TenantStateDto.DtoConverter();
        if (TenantContext.getTenantId() == null) {
            logger.error("TenantId is not set.");
            throw new IllegalStateException("TenantId is not set.");
        }
        TenantState tenantState = tenantApplicationService.get(TenantContext.getTenantId());
        if (tenantState == null) {
            String message = "Tenant not found: " + TenantContext.getTenantId();
            logger.error(message);
            throw new IllegalStateException(message);
        }
        return dtoConverter.toTenantStateDto(tenantState);
    }

}
