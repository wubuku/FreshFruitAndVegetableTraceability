package org.dddml.ffvtraceability.auth.controller;

import org.dddml.ffvtraceability.auth.security.CustomUserDetails;
import org.dddml.ffvtraceability.auth.util.UrlStateEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth-srv/password")
public class PasswordController {
    private static final Logger logger = LoggerFactory.getLogger(PasswordController.class);

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    private final UrlStateEncoder urlStateEncoder;

    public PasswordController(JdbcTemplate jdbcTemplate,
                              PasswordEncoder passwordEncoder,
                              UrlStateEncoder urlStateEncoder) {
        logger.debug("Creating PasswordController...");

        if (jdbcTemplate == null) {
            logger.error("JdbcTemplate is null!");
            throw new IllegalArgumentException("JdbcTemplate cannot be null");
        }
        if (passwordEncoder == null) {
            logger.error("PasswordEncoder is null!");
            throw new IllegalArgumentException("PasswordEncoder cannot be null");
        }
        if (urlStateEncoder == null) {
            logger.error("UrlStateEncoder is null!");
            throw new IllegalArgumentException("UrlStateEncoder cannot be null");
        }

        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
        this.urlStateEncoder = urlStateEncoder;

        logger.debug("PasswordController dependencies:");
        logger.debug("JdbcTemplate: {}", jdbcTemplate.getClass().getName());
        logger.debug("PasswordEncoder: {}", passwordEncoder.getClass().getName());
        logger.debug("UrlStateEncoder: {}", urlStateEncoder.getClass().getName());
    }

    @GetMapping("/change")
    public String showChangePasswordForm(@RequestParam(required = false) String state,
                                         Model model) {
        model.addAttribute("state", state);
        return "change-password";
    }

    @PostMapping("/change")
    @Transactional
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam(required = false) String state,
                                 Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        logger.debug("Attempting to change password for user: {}", userDetails.getUsername());

        // 直接从数据库查询当前密码
        String currentStoredPassword = jdbcTemplate.queryForObject(
                "SELECT password FROM users WHERE username = ?",
                String.class,
                userDetails.getUsername()
        );

        logger.debug("Current stored password: {}", currentStoredPassword);
        logger.debug("Attempting to match password: {}", currentPassword);

        // 验证当前密码
        if (!passwordEncoder.matches(currentPassword, currentStoredPassword)) {
            logger.warn("Current password verification failed for user: {}", userDetails.getUsername());
            return "redirect:/password/change?error";
        }

        // 加密新密码
        String encodedPassword = passwordEncoder.encode(newPassword);
        logger.debug("New encoded password: {}", encodedPassword);

        // 确保新密码可以被验证
        if (!passwordEncoder.matches(newPassword, encodedPassword)) {
            logger.error("New password verification failed immediately after encoding!");
            return "redirect:/password/change?error";
        }

        // 更新密码
        int updated = jdbcTemplate.update("""
                UPDATE users 
                SET password = ?, 
                    password_change_required = false,
                    password_last_changed = CURRENT_TIMESTAMP,
                    first_login = false
                WHERE username = ?
                """, encodedPassword, userDetails.getUsername());

        logger.debug("Password update affected {} rows", updated);

        // 安全处理 state 参数
        try {
            if (state != null && !state.isEmpty()) {
                String originalUrl = urlStateEncoder.decode(state);
                if (originalUrl != null && !originalUrl.isEmpty()) {
                    logger.debug("Redirecting to original URL: {}", originalUrl);
                    return "redirect:" + originalUrl;
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to decode state parameter: {}", e.getMessage());
            // 如果解码失败，继续使用默认重定向
        }

        // 默认重定向
        return "redirect:/login?logout";
    }
}