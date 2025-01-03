package org.dddml.ffvtraceability.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserManagementApiController {
    private static final Logger logger = LoggerFactory.getLogger(UserManagementApiController.class);

    private final JdbcTemplate jdbcTemplate;

    public UserManagementApiController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/list")
    public List<Map<String, Object>> getUsers() {
        String sql = """
                SELECT u.username, u.enabled, u.password_change_required,
                       STRING_AGG(DISTINCT g.group_name, ', ') as groups,
                       STRING_AGG(DISTINCT a.authority, ', ') as authorities
                FROM users u
                LEFT JOIN group_members gm ON u.username = gm.username
                LEFT JOIN groups g ON gm.group_id = g.id
                LEFT JOIN authorities a ON u.username = a.username
                WHERE u.username != '*'
                GROUP BY u.username, u.enabled, u.password_change_required
                ORDER BY u.username
                """;

        return jdbcTemplate.queryForList(sql);
    }

    @PostMapping("/{username}/toggle-enabled")
    public ResponseEntity<?> toggleEnabled(@PathVariable String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        if (username.equals(currentUsername)) {
            return ResponseEntity.badRequest().body("Cannot disable your own account");
        }

        try {
            jdbcTemplate.update(
                    "UPDATE users SET enabled = NOT enabled WHERE username = ?",
                    username
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Failed to toggle user enabled status", e);
            return ResponseEntity.badRequest().body("Failed to update user status");
        }
    }

    @PostMapping("/{username}/toggle-password-change")
    public ResponseEntity<?> togglePasswordChange(@PathVariable String username) {
        try {
            jdbcTemplate.update(
                    "UPDATE users SET password_change_required = NOT password_change_required WHERE username = ?",
                    username
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Failed to toggle password change required", e);
            return ResponseEntity.badRequest().body("Failed to update password change status");
        }
    }
} 