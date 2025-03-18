package org.dddml.ffvtraceability.auth.controller;

import org.dddml.ffvtraceability.auth.config.PasswordTokenProperties;
import org.dddml.ffvtraceability.auth.dto.ForgotPasswordVo;
import org.dddml.ffvtraceability.auth.dto.PasswordTokenDto;
import org.dddml.ffvtraceability.auth.dto.PasswordVo;
import org.dddml.ffvtraceability.auth.dto.UserDto;
import org.dddml.ffvtraceability.auth.exception.BusinessException;
import org.dddml.ffvtraceability.auth.mapper.UserDtoMapper;
import org.dddml.ffvtraceability.auth.service.EmailService;
import org.dddml.ffvtraceability.auth.service.PasswordTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/auth-srv/password-tokens")
public class PasswordTokenController {
    private static final Logger logger = LoggerFactory.getLogger(PasswordTokenController.class);

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private PasswordTokenService passwordTokenService;
    @Autowired
    private PasswordTokenProperties passwordTokenProperties;
    @Autowired
    private EmailService emailService;

    public PasswordTokenController(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{token}")
    public PasswordTokenDto getPasswordToken(@PathVariable("token") String token) {
        return passwordTokenService.getPasswordToken(token);
    }

    /**
     * 根据username获取最后创建的token的时间
     *
     * @return
     */
    @GetMapping("/last-token-created-at/{username}")
    public PasswordTokenDto getLastCreatedAt(@PathVariable("username") String username) {
        OffsetDateTime tokenCreatedAt =
                jdbcTemplate.query("""
                                        select token_created_at from password_tokens 
                                        where username  = ? 
                                        order by token_created_at desc 
                                        limit 1
                                        """,
                                (rs, rowNum) -> rs.getObject("token_created_at", OffsetDateTime.class),
                                username)
                        .stream().findFirst().orElse(null);
        PasswordTokenDto passwordTokenDto = new PasswordTokenDto();
        passwordTokenDto.setUsername(username);
        if (tokenCreatedAt != null) {
            passwordTokenDto.setTokenCreatedAt(tokenCreatedAt);
        }
        return passwordTokenDto;
    }


    private void sendResetPasswordEmail(String mailTo, String token) {
        StringBuilder sb = new StringBuilder();
        sb.append("Password Reset\r\n");
        sb.append("You have requested to reset your password.Use the link below to create a new password\r\n");
        sb.append(passwordTokenProperties.getCreatePasswordUrl()).append("?").append("token=").append(token);
        sb.append("&type=reset\r\n");
        sb.append("\r\n");
        sb.append("Thanks\r\n");
        sb.append("Powered by Fresh Fruit & Vegetable Traceability System\r\n");
        emailService.sendTextMail(mailTo, "Password Reset", sb.toString());
    }

    @PostMapping("/forgot-password")
    public void forgotPassword(@RequestBody ForgotPasswordVo forgotPasswordVo) {
        OffsetDateTime now = OffsetDateTime.now();
        String sql = "SELECT * FROM users WHERE username = ?";
        UserDto user = jdbcTemplate.query(sql, new UserDtoMapper(), forgotPasswordVo.getUsername()).stream().findFirst().orElse(null);
        if (user == null) {
            throw new BusinessException("User not found");
        }
        if (!user.getEnabled()) {
            throw new BusinessException("User is disabled");
        }
        String token = UUID.randomUUID().toString();
        passwordTokenService.savePermissionToken(user.getUsername(), token, "reset", now);
        sendResetPasswordEmail(forgotPasswordVo.getUsername(), token);

    }

    @PutMapping("/create-password")
    public void createPassword(@RequestBody PasswordVo passwordVo) {
        if (passwordVo.getToken() == null || passwordVo.getToken().isBlank()) {
            throw new BusinessException("Token is required");
        }
        passwordVo.setToken(passwordVo.getToken().trim());
        PasswordTokenDto passwordToken = passwordTokenService.getPasswordToken(passwordVo.getToken());
        if (passwordToken == null) {
            throw new BusinessException("Token is invalid");
        }
        if (passwordToken.getPasswordCreatedAt() != null) {
            throw new BusinessException("Token is already used");
        }
        OffsetDateTime now = OffsetDateTime.now();
        if (now.isAfter(passwordToken.getTokenCreatedAt().plusMinutes(passwordTokenProperties.getExpireInMinutes()))) {
            throw new BusinessException("Token is expired");
        }
        if (passwordToken.getToken() == null) {
            throw new BusinessException("Unrecognized type");
        }
        if (passwordToken.getType().equals("register") || passwordVo.getType().equals("reset")) {
            String username = passwordToken.getUsername();
            String encodedPassword = passwordEncoder.encode(passwordVo.getPassword());
            int updated = jdbcTemplate.update("""
                    UPDATE users 
                    SET password = ?, 
                        password_change_required = false,
                        password_last_changed = ?,
                        first_login = false
                    WHERE username = ?
                    """, encodedPassword, now, username);
        } else {
            throw new BusinessException("Unrecognized type");
        }
        jdbcTemplate.update("""
                UPDATE password_tokens 
                SET password_created_at = ?
                WHERE token = ?
                """, now, passwordToken.getToken());
    }
}