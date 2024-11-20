package org.dddml.ffvtraceability.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
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
                   STRING_AGG(g.group_name, ', ') as groups
            FROM users u
            LEFT JOIN group_members gm ON u.username = gm.username
            LEFT JOIN groups g ON gm.group_id = g.id
            WHERE u.username != '*'
            GROUP BY u.username, u.enabled, u.password_change_required
            ORDER BY u.username
            """;
            
        return jdbcTemplate.queryForList(sql);
    }
    
    @PostMapping("/{username}/toggle-enabled")
    public ResponseEntity<?> toggleEnabled(@PathVariable String username) {
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
    
    @PostMapping("/{username}/require-password-change")
    public ResponseEntity<?> requirePasswordChange(@PathVariable String username) {
        try {
            jdbcTemplate.update(
                "UPDATE users SET password_change_required = true WHERE username = ?",
                username
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Failed to set password change required", e);
            return ResponseEntity.badRequest().body("Failed to update user");
        }
    }
} 