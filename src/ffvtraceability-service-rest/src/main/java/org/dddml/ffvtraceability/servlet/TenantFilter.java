package org.dddml.ffvtraceability.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dddml.ffvtraceability.domain.TenantContext;
import org.dddml.ffvtraceability.domain.TenantSupport;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TenantFilter implements Filter {

    private static final String TENANT_ID_HEADER = "X-TenantID";

    private static final boolean AUTO_SET_SUPER_TENANT_ID_WHEN_NO_TENANT_ID_SUPPLIED = false;

    private static final boolean RETURN_ERROR_WHEN_NO_TENANT_ID_SUPPLIED = false;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String tenantHeader = request.getHeader(TENANT_ID_HEADER);
        if (tenantHeader != null && !tenantHeader.isEmpty()) {
            TenantContext.setTenantId(tenantHeader);
        } else if (AUTO_SET_SUPER_TENANT_ID_WHEN_NO_TENANT_ID_SUPPLIED) {
            TenantContext.setTenantId(TenantSupport.SUPER_TENANT_ID);
        } else if (RETURN_ERROR_WHEN_NO_TENANT_ID_SUPPLIED) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"error\": \"No tenant supplied\"}");
            response.getWriter().flush();
            return;
        }
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            TenantContext.setTenantId(null);
        }
    }

    @Override
    public void destroy() {
    }
}
