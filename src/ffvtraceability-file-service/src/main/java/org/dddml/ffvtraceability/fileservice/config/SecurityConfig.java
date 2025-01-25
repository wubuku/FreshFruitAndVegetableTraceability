package org.dddml.ffvtraceability.fileservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final StorageSecurityProperties securityProperties;

    public SecurityConfig(StorageSecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(mvcMatcherBuilder.pattern("/api/files/**"))
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")))
                .headers(headers -> headers.frameOptions().disable())  // 允许 H2 控制台在 iframe 中显示
                .authorizeHttpRequests(auth -> {
                    // 配置公开访问的端点
                    auth.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                            .requestMatchers(mvcMatcherBuilder.pattern("/api/files/*/media")).permitAll()
                            .requestMatchers(mvcMatcherBuilder.pattern("/api/files/*/download")).permitAll();  // 允许下载端点的匿名访问

                    // 根据配置决定是否允许匿名上传
                    if (securityProperties.isAllowAnonymousUpload()) {
                        auth.requestMatchers(mvcMatcherBuilder.pattern("/api/files/upload")).permitAll();
                    }

                    // 其他端点需要认证
                    auth.requestMatchers(mvcMatcherBuilder.pattern("/api/files/**")).authenticated()
                            .anyRequest().authenticated();
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        return jwtConverter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}