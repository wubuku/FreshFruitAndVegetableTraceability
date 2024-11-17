//package org.dddml.ffvtraceability.auth.config;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
//
//import java.util.Map;
//import java.util.Set;
//
//public class OAuth2PasswordAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {
//    private final Set<String> scopes;
//
//    public OAuth2PasswordAuthenticationToken(AuthorizationGrantType authorizationGrantType,
//                                          Authentication clientPrincipal,
//                                          Set<String> scopes,
//                                          Map<String, Object> additionalParameters) {
//        super(authorizationGrantType, clientPrincipal, additionalParameters);
//        this.scopes = scopes;
//    }
//
//    public Set<String> getScopes() {
//        return this.scopes;
//    }
//}