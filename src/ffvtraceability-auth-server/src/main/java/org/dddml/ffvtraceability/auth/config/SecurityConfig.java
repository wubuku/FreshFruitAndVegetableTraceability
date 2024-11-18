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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
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
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

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
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/oauth2/**",          // OAuth2 endpoints
                                "/login",              // 登录页面
                                "/error",              // 错误页面
                                "/oauth2-test",        // 测试首页
                                "/oauth2-test-callback", // 回调页面
                                "/password/change"     // 密码修改页面
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler)
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