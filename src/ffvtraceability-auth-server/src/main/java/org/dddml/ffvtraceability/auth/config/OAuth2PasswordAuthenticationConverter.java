//package org.dddml.ffvtraceability.auth.config;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.OAuth2Error;
//import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
//import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
//import org.springframework.security.web.authentication.AuthenticationConverter;
//import org.springframework.util.StringUtils;
//
//import java.util.*;
//
//public class OAuth2PasswordAuthenticationConverter implements AuthenticationConverter {
//    private static final Logger logger = LoggerFactory.getLogger(OAuth2PasswordAuthenticationConverter.class);
//
//    @Override
//    public Authentication convert(HttpServletRequest request) {
//        // 检查授权类型是否为 password
//        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
//        if (!"password".equals(grantType)) {
//            return null;
//        }
//
//        // 获取请求参数
//        String username = request.getParameter(OAuth2ParameterNames.USERNAME);
//        String password = request.getParameter(OAuth2ParameterNames.PASSWORD);
//        String scope = request.getParameter(OAuth2ParameterNames.SCOPE);
//
//        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
//            throw new OAuth2AuthenticationException(
//                new OAuth2Error(
//                    "invalid_request",
//                    "Username and password are required",
//                    null
//                )
//            );
//        }
//
//        // 处理作用域
//        Set<String> requestedScopes = null;
//        if (StringUtils.hasText(scope)) {
//            requestedScopes = new HashSet<>(
//                Arrays.asList(StringUtils.delimitedListToStringArray(scope, " "))
//            );
//        }
//
//        // 构建额外参数
//        Map<String, Object> additionalParameters = new HashMap<>();
//        additionalParameters.put(OAuth2ParameterNames.USERNAME, username);
//        additionalParameters.put(OAuth2ParameterNames.PASSWORD, password);
//
//        // 获取客户端认证信息
//        Authentication clientPrincipal = (Authentication) request.getAttribute(
//                OAuth2ClientAuthenticationToken.class.getName());
//
//        if (clientPrincipal == null) {
//            // Instead of throwing an exception, return null to allow other converters to process the request
//            return null;
//        }
//
//        return new OAuth2PasswordAuthenticationToken(
//            AuthorizationGrantType.PASSWORD,
//            clientPrincipal,
//            requestedScopes,
//            additionalParameters
//        );
//    }
//}