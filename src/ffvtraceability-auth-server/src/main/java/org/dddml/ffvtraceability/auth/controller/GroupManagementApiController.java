package org.dddml.ffvtraceability.auth.controller;

import org.dddml.ffvtraceability.auth.dto.GroupDto;
import org.dddml.ffvtraceability.auth.exception.BusinessException;
import org.dddml.ffvtraceability.auth.mapper.GroupDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth-srv/groups")
public class GroupManagementApiController {
    private static final Logger logger = LoggerFactory.getLogger(GroupManagementApiController.class);

    private final JdbcTemplate jdbcTemplate;

    public GroupManagementApiController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<GroupDto> findGroups(@RequestParam(value = "enabled", required = false) Boolean enabled) {
        List<GroupDto> groups = null;
        StringBuilder sql = new StringBuilder("SELECT * FROM groups");
        if (enabled != null) {
            sql.append(" WHERE enabled = ? order by id");
            groups = jdbcTemplate.query(sql.toString(), new GroupDtoMapper(), enabled);
        } else {
            sql.append(" order by id");
            groups = jdbcTemplate.query(sql.toString(), new GroupDtoMapper());
        }
        groups.forEach(group -> {
            String sqlGetPermissions = """
                    SELECT ga.authority 
                    FROM group_authorities ga
                    JOIN permissions p ON ga.authority = p.permission_id
                    WHERE ga.group_id = ? 
                    AND (p.enabled IS NULL OR p.enabled = true)
                    """;
            group.setPermissions(jdbcTemplate.queryForList(sqlGetPermissions, String.class, group.getId()));
        });
        return groups;
    }

    @GetMapping("/list")
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getGroups() {
        String sql = """
                SELECT g.id, g.group_name, g.enabled,
                        STRING_AGG(DISTINCT u.username, ', ') as members,
                COUNT(DISTINCT gm.username) as member_count,
                STRING_AGG(DISTINCT ga.authority, ', ') as authorities
                FROM groups g
                LEFT JOIN group_members gm ON g.id = gm.group_id
                LEFT JOIN users u ON gm.username = u.username
                LEFT JOIN group_authorities ga ON g.id = ga.group_id
                GROUP BY g.id, g.group_name, g.enabled
                ORDER BY g.group_name
                """;
        return jdbcTemplate.queryForList(sql);
    }

    @GetMapping("/{groupId}/members")
    @Transactional(readOnly = true)
    public List<String> getGroupMembers(@PathVariable Long groupId) {
        return jdbcTemplate.queryForList(
                "SELECT username FROM group_members WHERE group_id = ? ORDER BY username",
                String.class,
                groupId
        );
    }

    @GetMapping("/available-users")
    @Transactional(readOnly = true)
    public List<String> getAvailableUsers() {
        return jdbcTemplate.queryForList(
                "SELECT username FROM users WHERE username != '*' ORDER BY username",
                String.class
        );
    }

    @PostMapping("/create")
    @Transactional
    public Map<String, Object> createGroup(@RequestBody Map<String, String> request) {
        String groupName = request.get("groupName");
        if (groupName == null || groupName.isBlank()) {
            throw new IllegalArgumentException("Group name can't be null");
        }
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM groups WHERE group_name = ?",
                Integer.class,
                groupName
        );
        if (count > 0) {
            throw new BusinessException("Group name already exists: " + groupName);
        }
        String description = request.get("description");

//        try {
        logger.debug("Attempting to create group with name: {},description:{}", groupName, description);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO groups (group_name,description) VALUES (?,?)",
                    new String[]{"id"}
            );
            ps.setString(1, groupName);
            ps.setString(2, description);
            return ps;
        }, keyHolder);

        if (rows == 0) {
            logger.error("No rows were inserted for group: {}", groupName);
            throw new BusinessException("Failed to create group - no rows inserted");
        }

        Number key = keyHolder.getKey();
        if (key == null) {
            logger.error("Failed to get generated key for group: {}", groupName);
            throw new BusinessException("Failed to create group - no generated key");
        }

        Map<String, Object> response = Map.of(
                "id", key.longValue(),
                "groupName", groupName,
                "description", description
        );

        logger.debug("Successfully created group: {}, with ID: {}", groupName, key.longValue());
        return response;

//        } catch (Exception e) {
//            logger.error("Failed to create group: {} - {}", groupName, e.getMessage());
//            if (e.getMessage() != null && e.getMessage().contains("duplicate key value")) {
//                return ResponseEntity.badRequest().body("Group name already exists");
//            }
//            return ResponseEntity.badRequest().body("Failed to create group: " + e.getMessage());
//        }
    }

    @PostMapping("/{groupId}/members")
    @Transactional
    public void addGroupMember(@PathVariable Long groupId, @RequestBody Map<String, String> request) {
        String username = request.get("username");
        jdbcTemplate.update(
                "INSERT INTO group_members (group_id, username) VALUES (?, ?) ON CONFLICT DO NOTHING",
                groupId, username
        );
    }

    @DeleteMapping("/{groupId}/members/{username}")
    @Transactional
    public void removeGroupMember(@PathVariable Long groupId, @PathVariable String username) {
        jdbcTemplate.update(
                "DELETE FROM group_members WHERE group_id = ? AND username = ?",
                groupId, username
        );
    }

    @PostMapping("/{groupId}/toggle-enabled")
    @Transactional
    public void toggleGroupEnabled(@PathVariable Long groupId) {
        // 首先检查是否是 ADMIN_GROUP，不允许禁用
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM groups WHERE id = ? AND group_name = 'ADMIN_GROUP'",
                Integer.class,
                groupId
        );

        if (count != null && count > 0) {
            throw new BusinessException("Cannot disable ADMIN_GROUP");
        }

        // 切换状态
        int rows = jdbcTemplate.update(
                "UPDATE groups SET enabled = NOT enabled WHERE id = ?",
                groupId
        );

        if (rows == 0) {
            throw new BusinessException("Group not found");
        }
        // 如果组被禁用，同时删除所有组成员关系
        jdbcTemplate.update(
                "DELETE FROM group_members WHERE group_id = ? AND EXISTS (SELECT 1 FROM groups WHERE id = ? AND enabled = false)",
                groupId, groupId
        );

    }
}