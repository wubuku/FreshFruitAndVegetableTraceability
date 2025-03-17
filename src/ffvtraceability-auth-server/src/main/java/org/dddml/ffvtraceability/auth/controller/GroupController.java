package org.dddml.ffvtraceability.auth.controller;

import org.dddml.ffvtraceability.auth.dto.GroupDto;
import org.dddml.ffvtraceability.auth.dto.GroupVo;
import org.dddml.ffvtraceability.auth.dto.UserDto;
import org.dddml.ffvtraceability.auth.exception.BusinessException;
import org.dddml.ffvtraceability.auth.mapper.GroupDtoMapper;
import org.dddml.ffvtraceability.auth.mapper.UserDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth-srv/groups")
public class GroupController {
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    private final JdbcTemplate jdbcTemplate;

    public GroupController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping
    @Transactional
    public GroupDto createGroup(@RequestBody GroupVo groupVo) {
        String groupName = groupVo.getGroupName();
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
        return groupDto;
    }

    @GetMapping("/{groupId}")
    @Transactional(readOnly = true)
    public GroupDto getGroup(@PathVariable("groupId") Long groupId) {
        String sql = "SELECT id, group_name, enabled, description FROM groups WHERE id = ?";
        GroupDto groupDto = jdbcTemplate.query(sql, new GroupDtoMapper(), groupId).stream().findFirst().orElse(null);
        if (groupDto == null) {
            throw new BusinessException("Group not found with id: " + groupId);
        }
        String sqlGetPermissions = """
                SELECT ga.authority 
                FROM group_authorities ga
                JOIN permissions p ON ga.authority = p.permission_id
                WHERE ga.group_id = ? 
                AND (p.enabled IS NULL OR p.enabled = true)
                """;
        groupDto.setPermissions(jdbcTemplate.queryForList(sqlGetPermissions, String.class, groupId));
        return groupDto;
    }

    @PutMapping("/{groupId}")
    @Transactional
    public GroupDto updateGroup(@PathVariable("groupId") Long groupId, @RequestBody GroupVo groupVo) {
        String sql = "SELECT id, group_name, enabled, description FROM groups WHERE id = ?";
        GroupDto groupDto = jdbcTemplate.query(sql, new GroupDtoMapper(), groupId).stream().findFirst().orElse(null);
        if (groupDto == null) {
            throw new IllegalArgumentException("Group not found with id: " + groupId);
        }
        String groupName = groupVo.getGroupName();
        if (groupName == null || groupName.isBlank()) {
            throw new IllegalArgumentException("Group name can't be null");
        }
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM groups WHERE group_name = ? AND id <> ?",
                Integer.class,
                groupName,
                groupId
        );
        if (count > 0) {
            throw new BusinessException("Group name already exists: " + groupName);
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
        return groupDto;
    }

    @GetMapping("/{groupId}/users")
    @Transactional(readOnly = true)
    public List<UserDto> getGroupMembers(@PathVariable("groupId") Long groupId) {
        String sql = """
                SELECT u.* FROM users u 
                where 
                u.username in (select username from group_members gm where gm.group_id=?) 
                order by u.created_at desc
                """;
        //            users.forEach(user -> {
//                String selectGroups = "select * from groups where id in (select group_id from group_members gm where gm.username=?)";
//                user.setGroups(jdbcTemplate.query(selectGroups, new GroupDtoMapper(), user.getUsername()));
//
//            });
        return jdbcTemplate.query(sql, new UserDtoMapper(), groupId);
    }

    @PutMapping("/{groupId}/users")
    @Transactional
    public void syncGroupMembers(@PathVariable("groupId") Long
                                         groupId, @RequestBody List<String> usernames) {
        String sql = "SELECT COUNT(1) FROM groups WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, groupId);
        if (count < 1) {
            throw new BusinessException("Group not found with id: " + groupId);
        }
        if (usernames == null) {
            usernames = new ArrayList<>();
        } else {
            usernames = usernames.stream()
                    .filter(username -> Objects.nonNull(username) && !username.isBlank())
                    .distinct().collect(Collectors.toList());
            if (!usernames.isEmpty()) {
                List<String> invalidUsers = checkUserExistence(usernames);
                if (!invalidUsers.isEmpty()) {
                    throw new BusinessException("Invalid user accounts:  " + String.join(", ", invalidUsers));
                }
            }
        }
        Set<String> existingUsernames = new HashSet<>(jdbcTemplate.queryForList(
                "SELECT username FROM group_members WHERE group_id = ?",
                String.class,
                groupId
        ));
        // 4. 计算变更集
        Set<String> toAdd = new HashSet<>(usernames);
        toAdd.removeAll(existingUsernames);

        Set<String> toRemove = new HashSet<>(existingUsernames);
        toRemove.removeAll(usernames);

        // 5. 执行批量删除
        if (!toRemove.isEmpty()) {
            jdbcTemplate.batchUpdate(
                    "DELETE FROM group_members WHERE group_id = ? AND username = ?",
                    toRemove.stream()
                            .map(username -> new Object[]{groupId, username})
                            .collect(Collectors.toList())
            );
        }
        if (!toAdd.isEmpty()) {
            jdbcTemplate.batchUpdate(
                    "INSERT INTO group_members (group_id, username) VALUES (?, ?)",
                    toAdd.stream()
                            .map(username -> new Object[]{groupId, username})
                            .collect(Collectors.toList())
            );
        }
    }

    private List<String> checkUserExistence(List<String> usernames) {
        String inClause = String.join(",",
                Collections.nCopies(usernames.size(), "?")
        );

        String sql = "SELECT username FROM users WHERE username IN (" + inClause + ")";

        List<String> existingUsers = jdbcTemplate.queryForList(
                sql, String.class, usernames.toArray()
        );

        return usernames.stream()
                .filter(u -> !existingUsers.contains(u))
                .collect(Collectors.toList());
    }

}