package org.dddml.ffvtraceability.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
@PreAuthorize("hasRole('ADMIN')")
public class GroupManagementApiController {
    private static final Logger logger = LoggerFactory.getLogger(GroupManagementApiController.class);
    
    private final JdbcTemplate jdbcTemplate;
    
    public GroupManagementApiController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @GetMapping("/list")
    public List<Map<String, Object>> getGroups() {
        String sql = """
            SELECT g.id, g.group_name, g.enabled,
                   STRING_AGG(u.username, ', ') as members,
                   COUNT(gm.username) as member_count
            FROM groups g
            LEFT JOIN group_members gm ON g.id = gm.group_id
            LEFT JOIN users u ON gm.username = u.username
            GROUP BY g.id, g.group_name, g.enabled
            ORDER BY g.group_name
            """;
            
        return jdbcTemplate.queryForList(sql);
    }
    
    @GetMapping("/{groupId}/members")
    public List<String> getGroupMembers(@PathVariable Long groupId) {
        return jdbcTemplate.queryForList(
            "SELECT username FROM group_members WHERE group_id = ? ORDER BY username",
            String.class,
            groupId
        );
    }
    
    @GetMapping("/available-users")
    public List<String> getAvailableUsers() {
        return jdbcTemplate.queryForList(
            "SELECT username FROM users WHERE username != '*' ORDER BY username",
            String.class
        );
    }
    
    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody Map<String, String> request) {
        String groupName = request.get("groupName");
        
        try {
            logger.debug("Attempting to create group with name: {}", groupName);
            
            KeyHolder keyHolder = new GeneratedKeyHolder();
            
            int rows = jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO groups (group_name) VALUES (?)",
                    new String[]{"id"}
                );
                ps.setString(1, groupName);
                return ps;
            }, keyHolder);
            
            if (rows == 0) {
                logger.error("No rows were inserted for group: {}", groupName);
                return ResponseEntity.badRequest().body("Failed to create group - no rows inserted");
            }
            
            Number key = keyHolder.getKey();
            if (key == null) {
                logger.error("Failed to get generated key for group: {}", groupName);
                return ResponseEntity.badRequest().body("Failed to get group ID");
            }
            
            Map<String, Object> response = Map.of(
                "id", key.longValue(),
                "groupName", groupName
            );
            
            logger.debug("Successfully created group: {}, with ID: {}", groupName, key.longValue());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Failed to create group: {} - {}", groupName, e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("duplicate key value")) {
                return ResponseEntity.badRequest().body("Group name already exists");
            }
            return ResponseEntity.badRequest().body("Failed to create group: " + e.getMessage());
        }
    }
    
    @PostMapping("/{groupId}/members")
    public ResponseEntity<?> addGroupMember(@PathVariable Long groupId, @RequestBody Map<String, String> request) {
        String username = request.get("username");
        
        try {
            jdbcTemplate.update(
                "INSERT INTO group_members (group_id, username) VALUES (?, ?) ON CONFLICT DO NOTHING",
                groupId, username
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Failed to add group member", e);
            return ResponseEntity.badRequest().body("Failed to add member to group");
        }
    }
    
    @DeleteMapping("/{groupId}/members/{username}")
    public ResponseEntity<?> removeGroupMember(@PathVariable Long groupId, @PathVariable String username) {
        try {
            jdbcTemplate.update(
                "DELETE FROM group_members WHERE group_id = ? AND username = ?",
                groupId, username
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Failed to remove group member", e);
            return ResponseEntity.badRequest().body("Failed to remove member from group");
        }
    }
    
    @PostMapping("/{groupId}/toggle-enabled")
    public ResponseEntity<?> toggleGroupEnabled(@PathVariable Long groupId) {
        try {
            // 首先检查是否是 ADMIN_GROUP，不允许禁用
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM groups WHERE id = ? AND group_name = 'ADMIN_GROUP'",
                Integer.class,
                groupId
            );
            
            if (count != null && count > 0) {
                return ResponseEntity.badRequest().body("Cannot disable ADMIN_GROUP");
            }

            // 切换状态
            int rows = jdbcTemplate.update(
                "UPDATE groups SET enabled = NOT enabled WHERE id = ?",
                groupId
            );
            
            if (rows == 0) {
                return ResponseEntity.badRequest().body("Group not found");
            }
            
            // 如果组被禁用，同时删除所有组成员关系
            jdbcTemplate.update(
                "DELETE FROM group_members WHERE group_id = ? AND EXISTS (SELECT 1 FROM groups WHERE id = ? AND enabled = false)",
                groupId, groupId
            );
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Failed to toggle group status", e);
            return ResponseEntity.badRequest().body("Failed to update group status");
        }
    }
} 