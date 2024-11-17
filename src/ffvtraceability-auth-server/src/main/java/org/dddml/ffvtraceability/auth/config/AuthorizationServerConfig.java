package org.dddml.ffvtraceability.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Configuration
public class AuthorizationServerConfig {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServerConfig.class);
    // 保持 RSA 密钥对在服务器运行期间不变
    private static final KeyPair rsaKeyPair = generateRsaKey();

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                http.getConfigurer(OAuth2AuthorizationServerConfigurer.class);

        authorizationServerConfigurer
                .tokenGenerator(tokenGenerator())
                .clientAuthentication(clientAuth -> {
                    clientAuth.authenticationProviders(providers ->
                            providers.removeIf(provider ->
                                    provider.getClass().getSimpleName().startsWith("X509")
                            )
                    );
                    // logger.debug("Configuring client authentication");
                })
                .oidc(Customizer.withDefaults());

        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .cors(Customizer.withDefaults());

        return http.build();
    }

    private OAuth2TokenGenerator<?> tokenGenerator() {
        logger.debug("Creating token generator");
        JwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource());
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(
                jwtGenerator,
                accessTokenGenerator,
                refreshTokenGenerator
        );
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
        RegisteredClient client = RegisteredClient.withId("ffv-client-static-id")
                .clientId("ffv-client")
                .clientSecret(passwordEncoder.encode("secret"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://127.0.0.1:3000/callback")
                .redirectUri("com.ffv.app://oauth2/callback")
                .scope(OidcScopes.OPENID)
                .scope("read")
                .scope("write")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(1))      // 增加访问令牌有效期
                        .authorizationCodeTimeToLive(Duration.ofMinutes(10))  // 增加授权码有效期
                        .refreshTokenTimeToLive(Duration.ofDays(7))     // 增加刷新令牌有效期
                        .reuseRefreshTokens(true)
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .requireProofKey(true)
                        .build())
                .build();

        logger.info("Registering OAuth2 client with ID: {}", client.getId());
        return new InMemoryRegisteredClientRepository(client);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAPublicKey publicKey = (RSAPublicKey) rsaKeyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) rsaKeyPair.getPrivate();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://localhost:9000")
                .build();
    }

    @Bean
    public OAuth2AuthorizationService authorizationService() {
        return new OAuth2AuthorizationService() {
            private final InMemoryOAuth2AuthorizationService delegate = new InMemoryOAuth2AuthorizationService();

            @Override
            public void save(OAuth2Authorization authorization) {
                // logger.info("Saving authorization [{}] at {}", 
                //     authorization.getId(), 
                //     new java.util.Date());

                OAuth2Authorization.Token<?> authorizationCode =
                        authorization.getToken(OAuth2ParameterNames.CODE);
                if (authorizationCode != null) {
                    // logger.info("Authorization code details:");
                    // logger.info("  Code: {}", authorizationCode.getToken().getTokenValue());
                    // logger.info("  Issued at: {}", 
                    //     new java.util.Date(authorizationCode.getToken().getIssuedAt().toEpochMilli()));
                    // logger.info("  Expires at: {}", 
                    //     new java.util.Date(authorizationCode.getToken().getExpiresAt().toEpochMilli()));

                    // Log PKCE parameters
                    Map<String, Object> metadata = authorizationCode.getMetadata();
                    // logger.info("PKCE parameters:");
                    // logger.info("  code_challenge: {}", metadata.get("code_challenge"));
                    // logger.info("  code_challenge_method: {}", metadata.get("code_challenge_method"));
                }
                delegate.save(authorization);
            }

            @Override
            public void remove(OAuth2Authorization authorization) {
                delegate.remove(authorization);
            }

            @Override
            public OAuth2Authorization findById(String id) {
                return delegate.findById(id);
            }

            @Override
            public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
                OAuth2Authorization auth = delegate.findByToken(token, tokenType);
                if (auth != null && OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
                    OAuth2Authorization.Token<?> authorizationCode =
                            auth.getToken(OAuth2ParameterNames.CODE);
                    if (authorizationCode != null) {
                        // logger.info("Token request verification at {}", new java.util.Date());
                        // logger.info("Authorization code details:");
                        // logger.info("  ID: {}", auth.getId());
                        // logger.info("  Code: {}", authorizationCode.getToken().getTokenValue());

                        // Log PKCE parameters
                        Map<String, Object> metadata = authorizationCode.getMetadata();
                        // logger.info("PKCE parameters:");
                        // logger.info("  code_challenge: {}", metadata.get("code_challenge"));
                        // logger.info("  code_challenge_method: {}", metadata.get("code_challenge_method"));
                    }
                }
                return auth;
            }
        };
    }
}