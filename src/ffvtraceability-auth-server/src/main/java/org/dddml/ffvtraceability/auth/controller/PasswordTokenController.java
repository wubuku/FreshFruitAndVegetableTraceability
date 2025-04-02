package org.dddml.ffvtraceability.auth.controller;

import jakarta.validation.constraints.NotNull;
import org.dddml.ffvtraceability.auth.config.PasswordTokenProperties;
import org.dddml.ffvtraceability.auth.dto.ForgotPasswordVo;
import org.dddml.ffvtraceability.auth.dto.PasswordTokenDto;
import org.dddml.ffvtraceability.auth.dto.PasswordVo;
import org.dddml.ffvtraceability.auth.dto.UserDto;
import org.dddml.ffvtraceability.auth.exception.BusinessException;
import org.dddml.ffvtraceability.auth.mapper.PasswordTokenMapper;
import org.dddml.ffvtraceability.auth.mapper.UserDtoMapper;
import org.dddml.ffvtraceability.auth.service.EmailService;
import org.dddml.ffvtraceability.auth.service.PasswordTokenService;
import org.dddml.ffvtraceability.auth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
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
    @Autowired
    private UserService userService;

    public PasswordTokenController(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{token}")
    public PasswordTokenDto getPasswordToken(@PathVariable("token") String token) {
        return passwordTokenService.getPasswordToken(token);
    }

    // 根据提供的username，我会返回register类型（通过管理员发送邮件）的最后一条Token的信息，
    // 如果找不到这样的信息，我会返回200+响应体中只返回username（管理员可以发送邮件），
    // 找到一条这样的信息，你要参考里面的passwordCreatedAt属性，如果passwordCreatedAt有值，说明它已经通过这种方式注册过了（管理员不能再发送邮件），
    // 如果 passwordCreatedAt 为null，则检查其tokenCreatedAt字段，检查它是否超时，如果超时则同样不能发送邮件
    @GetMapping("/last-register-type-token")
    @Transactional(readOnly = true)
    public PasswordTokenDto getLastRegisterTypeToken(@NotNull @RequestParam("username") String username) {
        //因为这个查询语句是找最后一条register类型的token，也许由于某些原因存在register类型但是 token_created_at不为null，甚至用户通过forgot已经设置了密码，但是不重要了。
        String sql = "SELECT * FROM users WHERE username = ?";
        UserDto user = jdbcTemplate.query(sql, new UserDtoMapper(), username).stream().findFirst().orElse(null);
        if (user == null)
            throw new BusinessException("User not found");
        if (!user.getEnabled())
            throw new BusinessException("User is disabled");
        PasswordTokenDto passwordTokenDto = jdbcTemplate.query("""
                        select * from password_tokens 
                        where username  = ?
                        and type = 'register' 
                        order by token_created_at desc 
                        limit 1
                        """,
                new PasswordTokenMapper(),
                username).stream().findFirst().orElse(null);
        if (passwordTokenDto == null) {
            passwordTokenDto = new PasswordTokenDto();
            passwordTokenDto.setUsername(username);
        }
        return passwordTokenDto;
    }

    @PutMapping("/resend-register-email")
    @Transactional
    public void resendRegisterEmail(@RequestParam("username") String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        UserDto user = jdbcTemplate.query(sql, new UserDtoMapper(), username).stream().findFirst().orElse(null);
        if (user == null)
            throw new BusinessException("User not found");
        if (!user.getEnabled())
            throw new BusinessException("User is disabled");
        OffsetDateTime now = OffsetDateTime.now();
        String token = UUID.randomUUID().toString();
        passwordTokenService.savePermissionToken(username, token, "register", now);
        userService.sendCreatePasswordEmail(username, token);
    }

    /**
     * 根据username获取最后创建的token的时间
     *
     * @return
     */
//    @GetMapping("/last-token-created-at/{username}")
//    @Deprecated
//    public PasswordTokenDto getLastCreatedAt(@PathVariable("username") String username) {
//        OffsetDateTime tokenCreatedAt =
//                jdbcTemplate.query("""
//                                        select token_created_at from password_tokens
//                                        where username  = ?
//                                        order by token_created_at desc
//                                        limit 1
//                                        """,
//                                (rs, rowNum) -> rs.getObject("token_created_at", OffsetDateTime.class),
//                                username)
//                        .stream().findFirst().orElse(null);
//        PasswordTokenDto passwordTokenDto = new PasswordTokenDto();
//        passwordTokenDto.setUsername(username);
//        if (tokenCreatedAt != null) {
//            passwordTokenDto.setTokenCreatedAt(tokenCreatedAt);
//        }
//        return passwordTokenDto;
//    }
    private void sendResetPasswordEmail(String mailTo, String token) {
        StringBuilder sbLink = new StringBuilder();
        sbLink.append(passwordTokenProperties.getCreatePasswordUrl()).append("?").append("token=").append(token).append("&type=reset");
        StringBuilder sbHtml = new StringBuilder("""
                <div style="max-width: 600px; padding: 46px; background: white; outline: 1px #D4D4D8 solid; margin: 0 auto; font-family: Inter;">
                <img style="width: 165px; height: 50px;" src="cid:logo" alt="Logo">
                <div style="margin: 24px 0;">
                <h1 style="font-size: 24px; font-weight: 600; margin: 0 0 8px 0;">Password Reset</h1>
                <p style="font-size: 16px; line-height: 24px; margin: 0;">You have requested to reset your password.Use the link below to create a new password.
                </p>
                </div>
                """);
        sbHtml.append("<a href='");
        sbHtml.append(sbLink);
        sbHtml.append("""
                ' target='_blank'
                    style="display: inline-block;
                    padding: 8px 16px;
                    background: #15803D;
                    color: #FFFFFF;
                    text-decoration: none;
                    border-radius: 4px;
                    font-size: 16px;
                    line-height: 24px;
                    margin: 16px 0;">
                    Reset password
                    </a>
                  <hr style="border: 0;
                            height: 0;
                            border-top: 1px solid #D4D4D8;
                            margin: 24px 0;">
                  <div style="text-align: center; margin-top: 24px;">
                      <span style="font-size: 14px;">Powered by</span>
                      <img style="width: 96px; height: 28px; vertical-align: middle;" src="cid:blueforce" alt="Blueforce">
                  </div>
                </div>
                """);
        Map<String, ClassPathResource> inlineResources = new HashMap<>();
        inlineResources.put("logo", new ClassPathResource("images/logo.png"));
        inlineResources.put("blueforce", new ClassPathResource("images/blueforce.png"));
        emailService.sendHtmlMail(mailTo, "Password Reset", sbHtml.toString(), inlineResources);
    }

    /**
     * 这个接口应该是任何人都可以调用的，
     *
     * @param forgotPasswordVo
     */
    @PostMapping("/forgot-password")
    @Transactional
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
        //查询用户之前是否完成过管理员发送的完成注册邮件中生成密码
        sql = "SELECT count(*) FROM password_tokens WHERE username = ? AND type = 'register' AND password_created_at IS NOT NULL";
        //password_created_at IS NOT NULL 表示他至少完成了一次通过管理员发送的完成注册邮件设置密码
        int count = jdbcTemplate.queryForObject(sql, Integer.class, forgotPasswordVo.getUsername());
        if (count < 1) {
            throw new BusinessException("""
                    Your account setup is incomplete. 
                    Please use the set up link sent to your email. 
                    If the link has expired, contact your admin to resend it.
                    """);
        }
        String token = UUID.randomUUID().toString();
        passwordTokenService.savePermissionToken(user.getUsername(), token, "reset", now);
        sendResetPasswordEmail(forgotPasswordVo.getUsername(), token);

    }

    @PutMapping("/create-password")
    @Transactional
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
        if (now.isAfter(passwordToken.getTokenCreatedAt().plusMinutes(passwordTokenProperties.getExpireInHours()))) {
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