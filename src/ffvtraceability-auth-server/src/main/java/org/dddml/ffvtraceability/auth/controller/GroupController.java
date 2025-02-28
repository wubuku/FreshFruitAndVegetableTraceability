package org.dddml.ffvtraceability.auth.controller;

import org.dddml.ffvtraceability.auth.dto.GroupDto;
import org.dddml.ffvtraceability.auth.dto.GroupVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth-srv/groups")
public class GroupController {
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    private final JdbcTemplate jdbcTemplate;

    public GroupController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

            if (groupVo.getPermissions() != null && !groupVo.getPermissions().isEmpty()) {
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