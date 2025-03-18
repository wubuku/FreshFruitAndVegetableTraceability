package org.dddml.ffvtraceability.auth.controller;

import org.dddml.ffvtraceability.auth.dto.PasswordTokenDto;
import org.dddml.ffvtraceability.auth.mapper.PasswordTokenMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth-srv/password-token")
public class PasswordTokenController {
    private static final Logger logger = LoggerFactory.getLogger(PasswordTokenController.class);

    private final JdbcTemplate jdbcTemplate;

    public PasswordTokenController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/{token}")
    public PasswordTokenDto getPasswordToken(@PathVariable("token") String token) {
        return jdbcTemplate.query(
                        "SELECT token, username, type, token_created_at, password_created_at FROM password_tokens WHERE token = ?",
                        new PasswordTokenMapper(), token)
                .stream().findFirst().orElse(null);
    }
}