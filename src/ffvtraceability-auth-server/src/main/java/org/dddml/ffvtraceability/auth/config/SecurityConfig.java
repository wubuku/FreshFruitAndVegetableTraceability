package org.dddml.ffvtraceability.auth.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.cors.CorsConfigurationSource;

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
//                                .requestMatchers(
//                                        "/oauth2/**",
//                                        "/web-clients/oauth2/**",
//                                        "/login",
//                                        "/error",
//                                        "/oauth2-test",
//                                        "/oauth2-test-callback",
//                                        "/password/change"
//                                ).permitAll()
//                        .requestMatchers("/user-management")
//                        .hasAuthority("Users_Read")
//                        .requestMatchers("/group-management")
//                        .hasAuthority("Roles_Read")
//                        .requestMatchers(
//                                "/pre-register/**",
//                                "/permission-management/**"
//                        )
//                        .hasAuthority("ROLE_ADMIN")
                                .requestMatchers("/**").permitAll()
                        //.anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureHandler((request, response, exception) -> {
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            String errorMessage;
                            if (exception instanceof DisabledException) {
                                errorMessage = "User is disabled";
                            } else if (exception instanceof BadCredentialsException) {
                                errorMessage = "Username or password error";
                            } else {
                                errorMessage = exception.getMessage();
                            }
                            String jsonResponse = String.format("{\"error\": \"%s\"}", errorMessage);
                            response.getWriter().write(jsonResponse);
                        })
                        .successHandler((request, response, authentication) -> {
                            RequestCache requestCache = new HttpSessionRequestCache();
                            SavedRequest savedRequest = requestCache.getRequest(request, response);

                            if (savedRequest != null) {
                                String targetUrl = savedRequest.getRedirectUrl();
                                response.sendRedirect(targetUrl);
                            } else {
                                //重定向到 "/"
                                //response.sendRedirect("/");
                                CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                                response.setHeader(token.getHeaderName(), token.getToken());
                                //response.setContentType("text/plain;charset=UTF-8");
                                //response.addCookie(new Cookie("XSRF-TOKEN", token.getToken()));
                                response.setStatus(HttpServletResponse.SC_OK);
                                //response.getWriter().flush();
                            }
                        })
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        return username -> {
            String queryUser = """
                    SELECT u.username, u.password, u.enabled, u.password_change_required, u.password_last_changed, u.first_login
                    FROM users u
                    WHERE u.username = ?
                    """;
            Map<String, Object> userInfo = null;
            try {
                userInfo = jdbcTemplate.queryForMap(queryUser, username);
            } catch (Exception exception) {
                throw new UsernameNotFoundException("User not found: " + username);
            }
            String sqlPermissions = """
                        SELECT DISTINCT p.permission_id
                        FROM permissions p
                        JOIN group_authorities ga ON p.permission_id = ga.authority
                        JOIN group_members gm ON ga.group_id = gm.group_id
                        WHERE gm.username = ?
                    """;
            List<String> permissions = null;
            try {
                permissions = jdbcTemplate.queryForList(sqlPermissions, String.class, username);
            } catch (Exception exception) {
                permissions = new ArrayList<>();
            }
            List<String> groupNames = null;
            try {
                groupNames = jdbcTemplate.queryForList("""
                                select 
                                group_name 
                                from groups 
                                where id in (select group_id from group_members gm where gm.username=?) 
                                and enabled is true
                                """,
                        String.class, username);
            } catch (Exception e) {
                groupNames = new ArrayList<>();
            }
//            jdbcTemplate.queryForList("SELECT 1 FROM users WHERE username = ?", username);
//            String sql = """
//                    SELECT u.username,
//                           u.password,
//                           u.enabled,
//                           u.password_change_required,
//                           u.password_last_changed,
//                           u.first_login,
//                           a.authority,
//                           g.group_name
//                    FROM users u
//                    LEFT JOIN authorities a ON u.username = a.username
//                    LEFT JOIN group_members gm ON u.username = gm.username
//                    LEFT JOIN groups g ON gm.group_id = g.id
//                    WHERE u.username = ?
//                    """;
//
//            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, username);
//
//            if (results.isEmpty()) {
//                throw new UsernameNotFoundException("User not found: " + username);
//            }

            Set<GrantedAuthority> authorities = new HashSet<>();
            Set<String> groups = new HashSet<>(groupNames);
            for (String permission : permissions) {
                authorities.add(new SimpleGrantedAuthority(permission));
            }

//            for (Map<String, Object> row : results) {
//                String authority = (String) row.get("authority");
//                String groupName = (String) row.get("group_name");
//
//                if (authority != null) {
//                    authorities.add(new SimpleGrantedAuthority(authority));
//                }
//                if (groupName != null) {
//                    groups.add(groupName);
//                }
//            }

//            Map<String, Object> firstRow = null;// results.get(0);

            OffsetDateTime passwordLastChanged = toOffsetDateTime(userInfo.get("password_last_changed"));

            return new CustomUserDetails(
                    username,
                    (String) userInfo.get("password"),
                    userInfo.get("enabled") != null && (Boolean) userInfo.get("enabled"),
                    authorities,
                    groups,
                    (Boolean) userInfo.get("password_change_required"),
                    passwordLastChanged,
                    (Boolean) userInfo.get("first_login")
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