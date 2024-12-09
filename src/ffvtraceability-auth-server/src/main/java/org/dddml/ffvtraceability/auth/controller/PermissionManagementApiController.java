package org.dddml.ffvtraceability.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
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
    public List<Map<String, Object>> getBasePermissions() {
        logger.debug("Fetching base permissions...");
        try {
            String sql = """
                    SELECT permission_id, description, enabled 
                    FROM permissions 
                    ORDER BY permission_id
                    """;
            List<Map<String, Object>> permissions = jdbcTemplate.queryForList(sql);
            logger.debug("Found {} base permissions: {}", permissions.size(), permissions);
            return permissions;
        } catch (Exception e) {
            logger.error("Error fetching base permissions", e);
            throw e;
        }
    }

    @GetMapping("/user/{username}")
    public List<String> getUserPermissions(@PathVariable String username) {
        String sql = """
                SELECT a.authority 
                FROM authorities a
                JOIN permissions p ON a.authority = p.permission_id
                WHERE a.username = ? 
                AND (p.enabled IS NULL OR p.enabled = true)
                ORDER BY a.authority
                """;
        return jdbcTemplate.queryForList(sql, String.class, username);
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
        String sql = """
                SELECT ga.authority 
                FROM group_authorities ga
                JOIN permissions p ON ga.authority = p.permission_id
                WHERE ga.group_id = ? 
                AND (p.enabled IS NULL OR p.enabled = true)
                ORDER BY ga.authority
                """;
        return jdbcTemplate.queryForList(sql, String.class, groupId);
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

    @PostMapping("/create")
    public ResponseEntity<?> createPermission(@RequestBody Map<String, String> request) {
        String permissionId = request.get("permissionId");
        String description = request.get("description");

        try {
            jdbcTemplate.update(
                    "INSERT INTO permissions (permission_id, description, enabled) VALUES (?, ?, NULL)",
                    permissionId, description
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Failed to create permission", e);
            if (e.getMessage().contains("duplicate key")) {
                return ResponseEntity.badRequest().body("Permission already exists");
            }
            return ResponseEntity.badRequest().body("Failed to create permission");
        }
    }

    @PostMapping("/{permissionId}/toggle-enabled")
    public ResponseEntity<?> togglePermissionEnabled(@PathVariable String permissionId) {
        try {
            // 先检查当前状态
            Boolean currentEnabled = jdbcTemplate.queryForObject(
                    "SELECT enabled FROM permissions WHERE permission_id = ?",
                    Boolean.class,
                    permissionId
            );

            // 如果当前是 null，设置为 false；如果当前是 false，设置为 null
            Boolean newEnabled = (currentEnabled == null) ? false : null;

            jdbcTemplate.update(
                    "UPDATE permissions SET enabled = ? WHERE permission_id = ?",
                    newEnabled, permissionId
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Failed to toggle permission enabled status", e);
            return ResponseEntity.badRequest().body("Failed to update permission status");
        }
    }

    @PostMapping("/{permissionId}/update")
    public ResponseEntity<?> updatePermission(
            @PathVariable String permissionId,
            @RequestBody Map<String, String> request) {
        String description = request.get("description");

        try {
            jdbcTemplate.update(
                    "UPDATE permissions SET description = ? WHERE permission_id = ?",
                    description, permissionId
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Failed to update permission", e);
            return ResponseEntity.badRequest().body("Failed to update permission");
        }
    }

    @PostMapping("/import-csv")
    public ResponseEntity<?> importPermissionsFromCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload");
        }

        if (!file.getOriginalFilename().toLowerCase().endsWith(".csv")) {
            return ResponseEntity.badRequest().body("Please upload a CSV file");
        }

        try (BufferedReader reader
                     = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = CSVFormat.DEFAULT
                     .builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setIgnoreHeaderCase(true)
                     .setTrim(true)
                     .build()
                     .parse(reader)
        ) {
            // 验证必需的列是否存在
            Set<String> headers = new HashSet<>(csvParser.getHeaderNames());
            if (!headers.contains("permission_id")) {
                return ResponseEntity.badRequest().body("CSV file must contain 'permission_id' column");
            }

            List<String> errors = new ArrayList<>();
            int lineNumber = 1;
            int successCount = 0;

            for (CSVRecord record : csvParser) {
                lineNumber++;
                try {
                    String permissionId = record.get("permission_id").trim();
                    if (permissionId.isEmpty()) {
                        errors.add(String.format("Line %d: permission_id cannot be empty", lineNumber));
                        continue;
                    }

                    // 获取描述（如果存在）
                    String description = headers.contains("description") ?
                            record.get("description").trim() : null;

                    // 解析 enabled 值（如果存在）
                    Boolean enabled = null;
                    if (headers.contains("enabled")) {
                        String enabledStr = record.get("enabled").trim();
                        if (!enabledStr.isEmpty()) {
                            enabled = parseEnabledValue(enabledStr);
                        }
                    }

                    // 更新或插入权限
                    jdbcTemplate.update("""
                                    INSERT INTO permissions (permission_id, description, enabled)
                                    VALUES (?, ?, ?)
                                    ON CONFLICT (permission_id) DO UPDATE
                                    SET description = EXCLUDED.description,
                                        enabled = EXCLUDED.enabled
                                    """,
                            permissionId, description, enabled
                    );
                    successCount++;

                } catch (Exception e) {
                    errors.add(String.format("Line %d: %s", lineNumber, e.getMessage()));
                }
            }

            // 构建响应消息
            StringBuilder message = new StringBuilder()
                    .append(String.format("Successfully processed %d permissions. ", successCount));
            if (!errors.isEmpty()) {
                message.append(String.format("Found %d errors:\n", errors.size()));
                errors.forEach(error -> message.append(error).append("\n"));
            }

            return ResponseEntity.ok(message.toString());

        } catch (Exception e) {
            logger.error("Error processing CSV file", e);
            return ResponseEntity.badRequest().body("Error processing CSV file: " + e.getMessage());
        }
    }

    private Boolean parseEnabledValue(String value) {
        String normalizedValue = value.trim().toUpperCase();
        if (normalizedValue.isEmpty()) {
            return null;
        }

        // 视为 true 的值
        Set<String> trueValues = Set.of(
                "TRUE", "YES", "Y", "1", "T", "ENABLED", "ENABLE"
        );

        // 如果是 true 值返回 null（表示启用），否则返回 false
        return trueValues.contains(normalizedValue) ? null : false;
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