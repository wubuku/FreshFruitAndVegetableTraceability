package org.dddml.ffvtraceability.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/permissions")
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

    @GetMapping("/groups")
    public List<GroupInfo> getGroups() {
        return jdbcTemplate.query(
                "SELECT id, group_name FROM groups ORDER BY group_name",
                (rs, rowNum) -> new GroupInfo(rs.getLong("id"), rs.getString("group_name"))
        );
    }

    @GetMapping("/group/{groupId}")
    public List<String> getGroupPermissions(@PathVariable Long groupId) {
        return jdbcTemplate.queryForList(
                "SELECT authority FROM group_authorities WHERE group_id = ? ORDER BY authority",
                String.class,
                groupId
        );
    }

    @PostMapping("/group/update")
    public ResponseEntity<?> updateGroupPermission(@RequestBody Map<String, Object> request) {
        Long groupId = Long.valueOf(request.get("groupId").toString());
        String permission = (String) request.get("permission");
        boolean granted = (boolean) request.get("granted");

        try {
            if (granted) {
                jdbcTemplate.update(
                        "INSERT INTO group_authorities (group_id, authority) VALUES (?, ?) ON CONFLICT DO NOTHING",
                        groupId, permission
                );
            } else {
                jdbcTemplate.update(
                        "DELETE FROM group_authorities WHERE group_id = ? AND authority = ?",
                        groupId, permission
                );
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Failed to update group permission", e);
            return ResponseEntity.badRequest().body("Failed to update group permission");
        }
    }

    @PostMapping("/group/batch-update")
    public ResponseEntity<?> batchUpdateGroupPermissions(@RequestBody Map<String, Object> request) {
        Long groupId = Long.valueOf(request.get("groupId").toString());
        @SuppressWarnings("unchecked")
        List<String> permissions = (List<String>) request.get("permissions");
        boolean granted = (boolean) request.get("granted");

        logger.debug("Batch updating permissions for group: {}, granted: {}, permissions: {}",
                groupId, granted, permissions);

        try {
            if (granted) {
                for (String permission : permissions) {
                    jdbcTemplate.update(
                            "INSERT INTO group_authorities (group_id, authority) VALUES (?, ?) ON CONFLICT DO NOTHING",
                            groupId, permission
                    );
                }
            } else {
                jdbcTemplate.batchUpdate(
                        "DELETE FROM group_authorities WHERE group_id = ? AND authority = ?",
                        permissions.stream()
                                .map(permission -> new Object[]{groupId, permission})
                                .collect(Collectors.toList())
                );
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Failed to batch update group permissions", e);
            return ResponseEntity.badRequest().body("Failed to batch update group permissions");
        }
    }

    // 添加一个新的数据类来表示组信息
    public static class GroupInfo {
        private Long id;
        private String name;

        public GroupInfo(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}