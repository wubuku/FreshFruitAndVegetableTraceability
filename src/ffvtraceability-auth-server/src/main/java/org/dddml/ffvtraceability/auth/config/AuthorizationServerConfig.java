package org.dddml.ffvtraceability.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.*;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties({JwtKeyProperties.class, AuthServerProperties.class})
public class AuthorizationServerConfig {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServerConfig.class);

    private final JwtKeyProperties jwtKeyProperties;
    private final AuthServerProperties authServerProperties;

    public AuthorizationServerConfig(JwtKeyProperties jwtKeyProperties, AuthServerProperties authServerProperties) {
        this.jwtKeyProperties = jwtKeyProperties;
        this.authServerProperties = authServerProperties;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = http
                .getConfigurer(OAuth2AuthorizationServerConfigurer.class);


        authorizationServerConfigurer
                .clientAuthentication(clientAuth -> {
                    clientAuth.authenticationProviders(providers -> providers
                            .removeIf(provider -> provider.getClass().getSimpleName().startsWith("X509")));
                })
                .tokenGenerator(tokenGenerator())
                .oidc(Customizer.withDefaults());

        http.exceptionHandling((exceptionHandlingConfigurer) -> exceptionHandlingConfigurer
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        String[] origins = authServerProperties.getCors().getAllowedOrigins().split(",");
        configuration.setAllowedOriginPatterns(Arrays.asList(origins));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"));

        configuration.setAllowCredentials(true);

        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private OAuth2TokenGenerator<?> tokenGenerator() {
        JwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource());
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);

        // 添加自定义的 token claims
        jwtGenerator.setJwtCustomizer(context -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                JwtClaimsSet.Builder claims = context.getClaims();
                Authentication authentication = context.getPrincipal();

                // 添加标准权限
                Set<String> authorities = authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet());
                claims.claim("authorities", authorities);

                // 从 Authentication details 中获取组信息
                Object details = authentication.getDetails();
                if (details instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> detailsMap = (Map<String, Object>) details;
                    if (detailsMap.containsKey("groups")) {
                        claims.claim("groups", detailsMap.get("groups"));
                    }
                }
            }
        });

        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();

        // 令牌有效期将通过 RegisteredClient 配置来设置

        return new DelegatingOAuth2TokenGenerator(
                jwtGenerator,
                accessTokenGenerator,
                refreshTokenGenerator);
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(
            JdbcTemplate jdbcTemplate,
            RegisteredClientRepository registeredClientRepository,
            ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        JdbcOAuth2AuthorizationService service = new JdbcOAuth2AuthorizationService(
                jdbcTemplate,
                registeredClientRepository);
        JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper rowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(
                registeredClientRepository);
        rowMapper.setObjectMapper(objectMapper);
        service.setAuthorizationRowMapper(
                rowMapper);

        return service;
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        // 1. 加载 RSA 密钥对（私钥签名，公钥验证）
        KeyPair keyPair = loadKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        // 2. 构建 RSAKey（包含公钥和私钥）
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)  // 私钥仅用于签名，不对外暴露
                .keyID(UUID.randomUUID().toString())  // 唯一标识符（用于密钥轮换）
                .build();

        // 3. 包装为 JWKSet
        JWKSet jwkSet = new JWKSet(rsaKey);

        // 4. 返回 ImmutableJWKSet（不可变的安全实现）
        return new ImmutableJWKSet<>(jwkSet);
    }


    private KeyPair loadKeyPair() {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(
                    jwtKeyProperties.getKeyStore().getInputStream(),
                    jwtKeyProperties.getKeyStorePassword().toCharArray());

            Key key = keyStore.getKey(
                    jwtKeyProperties.getKeyAlias(),
                    jwtKeyProperties.getPrivateKeyPassphrase().toCharArray());

            Certificate cert = keyStore.getCertificate(jwtKeyProperties.getKeyAlias());
            PublicKey publicKey = cert.getPublicKey();

            return new KeyPair(publicKey, (PrivateKey) key);
        } catch (Exception e) {
            logger.error("Error loading JWT key pair", e);
            throw new RuntimeException("Could not load JWT key pair", e);
        }
    }

//可以通过以下配置来设置 issuer
// spring:
//  security:
//    oauth2:
//      authorization-server:
//        issuer: ${AUTH_SERVER_ISSUER:http://localhost:9000}
//    @Bean
//    public AuthorizationServerSettings authorizationServerSettings() {
//        return AuthorizationServerSettings.builder()
//                .issuer(authServerProperties.getIssuer())
//                .build();
//    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /*
     * @Bean
     * public ObjectMapper objectMapper() {
     * ObjectMapper mapper = new ObjectMapper();
     *
     * // 注册 Spring Security 核心模块
     * ClassLoader classLoader = getClass().getClassLoader();
     * List<com.fasterxml.jackson.databind.Module> securityModules =
     * SecurityJackson2Modules.getModules(classLoader);
     * mapper.registerModules(securityModules);
     *
     * // 注册 OAuth2 相关的模块
     * mapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
     *
     * // 注册我们的自定义模块
     * mapper.registerModule(new CustomJacksonModule());
     *
     * return mapper;
     * }
     */

}