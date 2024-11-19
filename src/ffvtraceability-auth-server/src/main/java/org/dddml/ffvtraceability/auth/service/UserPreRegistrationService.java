package org.dddml.ffvtraceability.auth.service;

import org.dddml.ffvtraceability.auth.dto.PreRegisterUserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
public class UserPreRegistrationService {
    private static final Logger logger = LoggerFactory.getLogger(UserPreRegistrationService.class);
    private static final String ALLOWED_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int OTP_LENGTH = 6;

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom random;

    public UserPreRegistrationService(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
        this.random = new SecureRandom();
    }

    @Transactional
    public PreRegisterUserResponse preRegisterUser(String username) {
        // Check if user already exists
        if (userExists(username)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }

        // Generate one-time password
        String oneTimePassword = generateOneTimePassword();
        String encodedPassword = passwordEncoder.encode(oneTimePassword);

        // Insert new user
        jdbcTemplate.update(
            "INSERT INTO users (username, password, enabled, password_change_required, first_login) VALUES (?, ?, ?, ?, ?)",
            username, encodedPassword, true, true, true
        );

        // Add to USER_GROUP
        jdbcTemplate.update(
            "INSERT INTO group_members (username, group_id) SELECT ?, id FROM groups WHERE group_name = 'USER_GROUP'",
            username
        );

        logger.info("Pre-registered user: {}", username);
        return new PreRegisterUserResponse(username, oneTimePassword);
    }

    private boolean userExists(String username) {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM users WHERE username = ?",
            Integer.class,
            username
        );
        return count != null && count > 0;
    }

    private String generateOneTimePassword() {
        StringBuilder sb = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            sb.append(ALLOWED_CHARS.charAt(random.nextInt(ALLOWED_CHARS.length())));
        }
        return sb.toString();
    }
}