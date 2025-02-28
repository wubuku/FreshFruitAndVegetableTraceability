package org.dddml.ffvtraceability.auth.controller;

import org.dddml.ffvtraceability.auth.dto.GroupDto;
import org.dddml.ffvtraceability.auth.dto.GroupVo;
import org.dddml.ffvtraceability.auth.mapper.GroupDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth-srv/groups")
public class GroupController {
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    private final JdbcTemplate jdbcTemplate;

    public GroupController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PutMapping("/{groupId}")
    @Transactional
    public ResponseEntity<?> updateGroup(@PathVariable("groupId") Long groupId, @RequestBody GroupVo groupVo) {
        try {
            String sql = "SELECT id, group_name, enabled, description FROM groups WHERE id = ?";
            GroupDto groupDto = jdbcTemplate.query(sql, new GroupDtoMapper(), groupId).stream().findFirst().orElse(null);
            if (groupDto == null) {
                return ResponseEntity.badRequest().body("Group not found with id: " + groupId);
            }
            String groupName = groupVo.getGroupName();
            if (groupName == null || groupName.isBlank()) {
                return ResponseEntity.badRequest().body("Group name can't be null");
            }
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM groups WHERE group_name = ? AND id <> ?",
                    Integer.class,
                    groupName,
                    groupId
            );
            if (count > 0) {
                return ResponseEntity.badRequest().body("Group name already exists: " + groupName);
            }
            String description = groupVo.getDescription();
            jdbcTemplate.update(
                    "UPDATE groups SET group_name = ?, description = ? WHERE id = ?",
                    groupName,
                    description,
                    groupId
            );
            if (groupVo.getPermissions() == null) {
                groupVo.setPermissions(new ArrayList<>());
            } else {
                groupVo.setPermissions(groupVo.getPermissions().stream()
                        .filter(permission -> Objects.nonNull(permission) && !permission.isBlank())
                        .distinct().collect(Collectors.toList()));

                // 1. 查询现有权限（使用Set提升比对效率）
                Set<String> existingPermissions = new HashSet<>(
                        jdbcTemplate.queryForList(
                                "SELECT authority FROM group_authorities WHERE group_id = ?",
                                String.class,
                                groupId
                        )
                );
                // 2. 计算变更集（使用集合运算）
                Set<String> targetSet = new HashSet<>(groupVo.getPermissions());

                // 需要删除的权限：存在现有但不在目标中
                Set<String> toDelete = new HashSet<>(existingPermissions);
                toDelete.removeAll(targetSet);

                // 需要新增的权限：存在目标但不在现有中
                Set<String> toAdd = new HashSet<>(targetSet);
                toAdd.removeAll(existingPermissions);

                // 3. 执行批量删除（存在待删除项时）
                if (!toDelete.isEmpty()) {
                    jdbcTemplate.batchUpdate(
                            "DELETE FROM group_authorities WHERE group_id = ? AND authority = ?",
                            toDelete.stream()
                                    .map(auth -> new Object[]{groupId, auth})
                                    .collect(Collectors.toList())
                    );
                }

                // 4. 执行批量新增（存在待新增项时）
                if (!toAdd.isEmpty()) {
                    jdbcTemplate.batchUpdate(
                            "INSERT INTO group_authorities (group_id, authority) VALUES (?, ?)",
                            toAdd.stream()
                                    .map(auth -> new Object[]{groupId, auth})
                                    .collect(Collectors.toList())
                    );
                }
            }
            logger.debug("Successfully updated group: {}", groupName);

            groupDto.setGroupName(groupName);
            groupDto.setDescription(description);
            groupDto.setId(groupId);
            groupDto.setPermissions(groupVo.getPermissions());

            return ResponseEntity.ok(groupDto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update group: " + e.getMessage());
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> createGroup(@RequestBody GroupVo groupVo) {
        String groupName = groupVo.getGroupName();
        if (groupName == null || groupName.isBlank()) {
            return ResponseEntity.badRequest().body("Group name can't be null");
        }
        try {
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM groups WHERE group_name = ?",
                    Integer.class,
                    groupName
            );
            if (count > 0) {
                return ResponseEntity.badRequest().body("Group name already exists: " + groupName);
            }
            String description = groupVo.getDescription();
            logger.debug("Attempting to create group with name: {},description:{}", groupName, description);

            // 或者使用 SimpleJdbcInsert（推荐）
            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("groups")
                    .usingGeneratedKeyColumns("id");

            Map<String, Object> params = new HashMap<>();
            params.put("group_name", groupName);
            params.put("description", description);
            params.put("enabled", true);
            Number id = insert.executeAndReturnKey(params);

            if (groupVo.getPermissions() == null) {
                groupVo.setPermissions(new ArrayList<>());
            } else {
                groupVo.setPermissions(groupVo.getPermissions().stream()
                        .filter(permission -> Objects.nonNull(permission) && !permission.isBlank())
                        .distinct().collect(Collectors.toList()));
                // 批量插入权限
                jdbcTemplate.batchUpdate(
                        "INSERT INTO group_authorities (group_id, authority) VALUES (?, ?)",
                        groupVo.getPermissions().stream()
                                .map(permission -> new Object[]{id.longValue(), permission})
                                .collect(Collectors.toList())
                );
            }
            GroupDto groupDto = new GroupDto();
            groupDto.setGroupName(groupName);
            groupDto.setDescription(description);
            groupDto.setId(id.longValue());
            groupDto.setEnabled(true);
            groupDto.setPermissions(groupVo.getPermissions());

            logger.debug("Successfully created group: {}, with ID: {}", groupName, id.longValue());
            return ResponseEntity.ok(groupDto);

        } catch (Exception e) {
            logger.error("Failed to create group: {} - {}", groupName, e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("duplicate key value")) {
                return ResponseEntity.badRequest().body("Group name already exists");
            }
            return ResponseEntity.badRequest().body("Failed to create group: " + e.getMessage());
        }
    }


}