package org.dddml.ffvtraceability.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/permissions")
@PreAuthorize("hasRole('ADMIN')")
public class PermissionManagementApiController {
    private static final Logger logger = LoggerFactory.getLogger(PermissionManagementApiController.class);
    
    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    @Qualifier("restApiObjectMapper")
    private ObjectMapper objectMapper;
    
    public PermissionManagementApiController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @GetMapping("/users")
    public List<String> getUsers() {
        return jdbcTemplate.queryForList(
            "SELECT username FROM users WHERE username != '*' ORDER BY username",
            String.class
        );
    }
    
    @GetMapping("/base")
    public List<String> getBasePermissions() {
        logger.debug("Fetching base permissions...");
        List<String> permissions = jdbcTemplate.queryForList(
            "SELECT authority FROM authorities WHERE username = '*' ORDER BY authority",
            String.class
        );
        logger.debug("Found {} base permissions: {}", permissions.size(), permissions);
        return permissions;
    }
    
    @GetMapping("/user/{username}")
    public List<String> getUserPermissions(@PathVariable String username) {
        return jdbcTemplate.queryForList(
            "SELECT authority FROM authorities WHERE username = ? ORDER BY authority",
            String.class,
            username
        );
    }
    
    @PostMapping("/update")
    public ResponseEntity<?> updatePermission(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        String permission = (String) request.get("permission");
        boolean granted = (boolean) request.get("granted");
        
        try {
            if (granted) {
                jdbcTemplate.update(
                    "INSERT INTO authorities (username, authority) VALUES (?, ?) ON CONFLICT DO NOTHING",
                    username, permission
                );
            } else {
                jdbcTemplate.update(
                    "DELETE FROM authorities WHERE username = ? AND authority = ?",
                    username, permission
                );
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Failed to update permission", e);
            return ResponseEntity.badRequest().body("Failed to update permission");
        }
    }
    
    @PostMapping("/batch-update")
    public ResponseEntity<?> batchUpdatePermissions(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        @SuppressWarnings("unchecked")
        List<String> permissions = (List<String>) request.get("permissions");
        boolean granted = (boolean) request.get("granted");
        
        logger.debug("Batch updating permissions for user: {}, granted: {}, permissions: {}", 
                     username, granted, permissions);
        
        try {
            if (granted) {
                // 批量插入权限
                for (String permission : permissions) {
                    jdbcTemplate.update(
                        "INSERT INTO authorities (username, authority) VALUES (?, ?) ON CONFLICT DO NOTHING",
                        username, permission
                    );
                }
            } else {
                // 批量删除权限
                jdbcTemplate.batchUpdate(
                    "DELETE FROM authorities WHERE username = ? AND authority = ?",
                    permissions.stream()
                        .map(permission -> new Object[]{username, permission})
                        .collect(Collectors.toList())
                );
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Failed to batch update permissions", e);
            return ResponseEntity.badRequest().body("Failed to batch update permissions");
        }
    }
}