package org.dddml.ffvtraceability.auth.config;

import org.dddml.ffvtraceability.auth.security.CustomUserDetails;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
                                "/oauth2-test-callback" // 回调页面
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/oauth2-test", true) // 登录成功后强制跳转到测试页面
                        .permitAll()
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
                           ua.authority,
                           g.group_name
                    FROM users u
                    LEFT JOIN authorities ua ON u.username = ua.username
                    LEFT JOIN group_members gm ON u.username = gm.username
                    LEFT JOIN groups g ON gm.group_id = g.id
                    WHERE u.username = ?
                    """;

            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, username);

            if (results.isEmpty()) {
                throw new UsernameNotFoundException("User not found: " + username);
            }

            // 收集直接权限
            Set<GrantedAuthority> authorities = results.stream()
                    .map(row -> (String) row.get("authority"))
                    .filter(Objects::nonNull)
                    .map(authority -> new SimpleGrantedAuthority(authority))
                    .collect(Collectors.toSet());

            // 收集组名
            Set<String> groups = results.stream()
                    .map(row -> (String) row.get("group_name"))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            return new CustomUserDetails(
                    username,
                    (String) results.get(0).get("password"),
                    authorities,
                    groups
            );
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder delegate = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return delegate.encode(rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                boolean matches = delegate.matches(rawPassword, encodedPassword);
                System.out.println("Password matching: raw=" + rawPassword +
                        ", encoded=" + encodedPassword +
                        ", matches=" + matches);
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