package org.dddml.ffvtraceability.auth.service;

import org.dddml.ffvtraceability.auth.dto.PasswordTokenDto;
import org.dddml.ffvtraceability.auth.mapper.PasswordTokenMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class PasswordTokenService {
    private final JdbcTemplate jdbcTemplate;

    public PasswordTokenService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    public PasswordTokenDto getPasswordToken(@PathVariable("token") String token) {
        return jdbcTemplate.query(
                        "SELECT token, username, type, token_created_at, password_created_at FROM password_tokens WHERE token = ?",
                        new PasswordTokenMapper(), token)
                .stream().findFirst().orElse(null);
    }


    public void savePermissionToken(String username, String token, String type, OffsetDateTime now) {
        if (now == null) {
            now = OffsetDateTime.now();
        }
        if (token == null) {
            token = UUID.randomUUID().toString();
        }
        jdbcTemplate.update(
                "INSERT INTO password_tokens (username, token, type, token_created_at) VALUES (?,?,?,?)",
                username, token, type, now
        );
    }
}
