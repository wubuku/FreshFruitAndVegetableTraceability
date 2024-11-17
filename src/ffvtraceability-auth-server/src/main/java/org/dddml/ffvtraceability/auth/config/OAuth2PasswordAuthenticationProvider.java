//package org.dddml.ffvtraceability.auth.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.oauth2.core.*;
//import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
//import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
//import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
//import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
//import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
//import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
//import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
//import org.springframework.security.oauth2.server.authorization.token.*;
//import org.springframework.stereotype.Component;
//
//import java.security.Principal;
//import java.util.Map;
//
//@Component
//public class OAuth2PasswordAuthenticationProvider implements AuthenticationProvider {
//    private static final Logger logger = LoggerFactory.getLogger(OAuth2PasswordAuthenticationProvider.class);
//
//    private final AuthenticationManager authenticationManager;
//    private final OAuth2AuthorizationService authorizationService;
//    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
//
//    public OAuth2PasswordAuthenticationProvider(
//            AuthenticationManager authenticationManager,
//            OAuth2AuthorizationService authorizationService,
//            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
//        this.authenticationManager = authenticationManager;
//        this.authorizationService = authorizationService;
//        this.tokenGenerator = tokenGenerator;
//    }
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        OAuth2PasswordAuthenticationToken passwordAuthentication =
//                (OAuth2PasswordAuthenticationToken) authentication;
//
//        // 获取客户端认证信息
//        OAuth2ClientAuthenticationToken clientPrincipal =
//                (OAuth2ClientAuthenticationToken) passwordAuthentication.getPrincipal();
//        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
//
//        logger.debug("Authenticating client: {}", clientPrincipal.getName());
//
//        // 验证用户名密码
//        Map<String, Object> additionalParameters = passwordAuthentication.getAdditionalParameters();
//        String username = (String) additionalParameters.get(OAuth2ParameterNames.USERNAME);
//        String password = (String) additionalParameters.get(OAuth2ParameterNames.PASSWORD);
//
//        logger.debug("Authenticating user: {}", username);
//
//        try {
//            UsernamePasswordAuthenticationToken usernamePasswordAuthentication =
//                    new UsernamePasswordAuthenticationToken(username, password);
//            Authentication usernamePasswordAuthenticationResult =
//                    authenticationManager.authenticate(usernamePasswordAuthentication);
//
//            // 生成令牌
//            DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
//                    .registeredClient(registeredClient)
//                    .principal(usernamePasswordAuthenticationResult)
//                    .authorizationServerContext(AuthorizationServerContextHolder.getContext())
//                    .authorizationGrantType(new AuthorizationGrantType("password"))
//                    .authorizedScopes(passwordAuthentication.getScopes())
//                    .authorizationGrant(passwordAuthentication);
//
//            OAuth2TokenContext tokenContext = tokenContextBuilder.build();
//            OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
//
//            if (generatedAccessToken == null) {
//                throw new OAuth2AuthenticationException(new OAuth2Error("server_error", "The token generator failed to generate the access token.", null));
//            }
//
//            OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
//                    generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
//                    generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
//
//            // 保存授权信息
//            OAuth2Authorization authorization = OAuth2Authorization.withRegisteredClient(registeredClient)
//                    .principalName(usernamePasswordAuthenticationResult.getName())
//                    .authorizationGrantType(new AuthorizationGrantType("password"))
//                    .accessToken(accessToken)
//                    .attribute(Principal.class.getName(), usernamePasswordAuthenticationResult)
//                    .build();
//            this.authorizationService.save(authorization);
//
//            logger.debug("Successfully authenticated and generated token for user: {}", username);
//
//            return new OAuth2AccessTokenAuthenticationToken(
//                    registeredClient, clientPrincipal, accessToken, null);
//
//        } catch (AuthenticationException ex) {
//            logger.error("Authentication failed for user: {}", username, ex);
//            throw new OAuth2AuthenticationException(
//                new OAuth2Error("invalid_grant", "Invalid username or password", null),
//                ex
//            );
//        }
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return OAuth2PasswordAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//}