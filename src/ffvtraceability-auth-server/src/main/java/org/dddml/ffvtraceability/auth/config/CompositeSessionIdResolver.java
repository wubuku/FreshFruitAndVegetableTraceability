package org.dddml.ffvtraceability.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.session.web.http.HttpSessionIdResolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompositeSessionIdResolver implements HttpSessionIdResolver {
    private final List<HttpSessionIdResolver> resolvers;

    public CompositeSessionIdResolver(HttpSessionIdResolver... resolvers) {
        this.resolvers = new ArrayList<>();
        Collections.addAll(this.resolvers, resolvers);
    }

    @Override
    public List<String> resolveSessionIds(HttpServletRequest request) {
        List<String> sessionIds = new ArrayList<>();
        for (HttpSessionIdResolver resolver : resolvers) {
            List<String> ids = resolver.resolveSessionIds(request);
            if (ids != null && !ids.isEmpty()) {
                sessionIds.addAll(ids);
            }
        }
        return sessionIds;
    }

    @Override
    public void setSessionId(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        for (HttpSessionIdResolver resolver : resolvers) {
            resolver.setSessionId(request, response, sessionId);
        }
    }

    @Override
    public void expireSession(HttpServletRequest request, HttpServletResponse response) {
        for (HttpSessionIdResolver resolver : resolvers) {
            resolver.expireSession(request, response);
        }
    }
} 