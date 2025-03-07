package org.dddml.ffvtraceability.auth.config;

import org.dddml.ffvtraceability.auth.security.CustomUserDetails;
import org.dddml.ffvtraceability.auth.security.handler.CustomAuthenticationSuccessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(AuthStateProperties.class)
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthServerProperties authServerProperties;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    private static OffsetDateTime toOffsetDateTime(Object dbValue) {
        OffsetDateTime passwordLastChanged = null;

        if (dbValue != null) {
            // 处理所有可能的时间类型
            if (dbValue instanceof OffsetDateTime) {
                passwordLastChanged = (OffsetDateTime) dbValue;
            } else if (dbValue instanceof Timestamp ts) {
                passwordLastChanged = ts.toInstant()
                        .atOffset(ZoneOffset.systemDefault().getRules().getOffset(Instant.now()));
            } else if (dbValue instanceof LocalDateTime ldt) {
                passwordLastChanged = ldt.atOffset(ZoneOffset.systemDefault().getRules().getOffset(Instant.now()));
            } else if (dbValue instanceof Date date) {
                passwordLastChanged = date.toInstant()
                        .atOffset(ZoneOffset.systemDefault().getRules().getOffset(Instant.now()));
            } else {
//                logger.warn("Unexpected datetime type: {} for value: {}",
//                        dbValue.getClass().getName(), dbValue);
                throw new IllegalArgumentException("Unsupported datetime type: " + dbValue.getClass().getName());
            }
        }
        logger.debug("Password last changed type: {}, value: {}",
                dbValue != null ? dbValue.getClass().getName() : "null",
                passwordLastChanged);
        return passwordLastChanged;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.ignoringRequestMatchers("/web-clients/oauth2/**"))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/oauth2/**",
                                "/web-clients/oauth2/**",
                                "/login",
                                "/error",
                                "/oauth2-test",
                                "/oauth2-test-callback",
                                "/password/change"
                        ).permitAll()
                        //.requestMatchers("").hasAuthority("")
                        .requestMatchers(
                                "/pre-register/**",
                                "/permission-management/**",
                                "/user-management",
                                "/group-management"
                        )
                        .hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler((request, response, authentication) -> {
                            RequestCache requestCache = new HttpSessionRequestCache();
                            SavedRequest savedRequest = requestCache.getRequest(request, response);

                            if (savedRequest != null) {
                                String targetUrl = savedRequest.getRedirectUrl();
                                response.sendRedirect(targetUrl);
                            } else {
                                response.sendRedirect("/");
                            }
                        })
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        return username -> {
            String sql = """
                    SELECT u.username, 
                           u.password, 
                           u.enabled,
                           u.password_change_required,
                           u.password_last_changed,
                           u.first_login,
                           a.authority,
                           g.group_name
                    FROM users u
                    LEFT JOIN authorities a ON u.username = a.username
                    LEFT JOIN group_members gm ON u.username = gm.username
                    LEFT JOIN groups g ON gm.group_id = g.id
                    WHERE u.username = ?
                    """;

            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, username);

            if (results.isEmpty()) {
                throw new UsernameNotFoundException("User not found: " + username);
            }

            Set<GrantedAuthority> authorities = new HashSet<>();
            Set<String> groups = new HashSet<>();

            for (Map<String, Object> row : results) {
                String authority = (String) row.get("authority");
                String groupName = (String) row.get("group_name");

                if (authority != null) {
                    authorities.add(new SimpleGrantedAuthority(authority));
                }
                if (groupName != null) {
                    groups.add(groupName);
                }
            }

            Map<String, Object> firstRow = results.get(0);

            OffsetDateTime passwordLastChanged = toOffsetDateTime(firstRow.get("password_last_changed"));

            return new CustomUserDetails(
                    username,
                    (String) firstRow.get("password"),
                    authorities,
                    groups,
                    (Boolean) firstRow.get("password_change_required"),
                    passwordLastChanged,
                    (Boolean) firstRow.get("first_login")
            );
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        DelegatingPasswordEncoder delegatingPasswordEncoder = (DelegatingPasswordEncoder)
                PasswordEncoderFactories.createDelegatingPasswordEncoder();

        // 添加日志记录
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                String encoded = delegatingPasswordEncoder.encode(rawPassword);
                logger.debug("Password encoding - Raw length: {}, Encoded: {}",
                        rawPassword.length(), encoded);
                return encoded;
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                boolean matches = delegatingPasswordEncoder.matches(rawPassword, encodedPassword);
                logger.debug("Password matching - Raw length: {}, Encoded: {}, Matches: {}",
                        rawPassword.length(), encodedPassword, matches);
                return matches;
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider() {
            @Override
            protected Authentication createSuccessAuthentication(Object principal,
                                                                 Authentication authentication, UserDetails user) {
                Authentication result = super.createSuccessAuthentication(
                        principal, authentication, user);

                // 如果是我们的 CustomUserDetails，保存组信息到 Authentication details
                if (user instanceof CustomUserDetails customUser) {
                    Map<String, Object> details = new HashMap<>();
                    details.put("groups", customUser.getGroups());
                    ((UsernamePasswordAuthenticationToken) result).setDetails(details);
                }

                return result;
            }
        };
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

}