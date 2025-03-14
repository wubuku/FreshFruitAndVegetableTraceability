package org.dddml.ffvtraceability.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

@Configuration
public class SessionConfig {

//    @Bean
//    public HttpSessionIdResolver httpSessionIdResolver() {
//        return new CompositeSessionIdResolver(
//                new CookieHttpSessionIdResolver(),  // 支持 cookie
//                HeaderHttpSessionIdResolver.xAuthToken()  // 支持 header
//        );
//    }
} 