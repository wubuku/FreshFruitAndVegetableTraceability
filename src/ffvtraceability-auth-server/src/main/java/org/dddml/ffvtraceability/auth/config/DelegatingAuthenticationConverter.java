//package org.dddml.ffvtraceability.auth.config;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.AuthenticationConverter;
//
//import java.util.List;
//
//public class DelegatingAuthenticationConverter implements AuthenticationConverter {
//    private final List<AuthenticationConverter> converters;
//
//    public DelegatingAuthenticationConverter(List<AuthenticationConverter> converters) {
//        this.converters = converters;
//    }
//
//    @Override
//    public Authentication convert(HttpServletRequest request) {
//        for (AuthenticationConverter converter : converters) {
//            Authentication authentication = converter.convert(request);
//            if (authentication != null) {
//                return authentication;
//            }
//        }
//        return null;
//    }
//}