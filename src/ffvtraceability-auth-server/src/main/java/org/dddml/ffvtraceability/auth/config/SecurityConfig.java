package org.dddml.ffvtraceability.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/oauth2/**", "/login", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);

        users.setEnableGroups(true);
        users.setGroupAuthoritiesByUsernameQuery(
                "SELECT g.id, g.group_name, ga.authority " +
                        "FROM groups g " +
                        "INNER JOIN group_members gm ON gm.group_id = g.id " +
                        "INNER JOIN group_authorities ga ON ga.group_id = g.id " +
                        "WHERE gm.username = ?"
        );

        users.setUsersByUsernameQuery(
                "SELECT username, password, enabled FROM users WHERE username = ?"
        );
        users.setAuthoritiesByUsernameQuery(
                "SELECT username, authority FROM authorities WHERE username = ?"
        );

        return users;
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
}