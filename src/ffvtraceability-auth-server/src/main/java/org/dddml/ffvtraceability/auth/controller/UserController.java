package org.dddml.ffvtraceability.auth.controller;

import org.dddml.ffvtraceability.auth.dto.PreRegisterUserDto;
import org.dddml.ffvtraceability.auth.dto.UserDto;
import org.dddml.ffvtraceability.auth.exception.BusinessException;
import org.dddml.ffvtraceability.auth.mapper.GroupDtoMapper;
import org.dddml.ffvtraceability.auth.mapper.UserDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth-srv/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final JdbcTemplate jdbcTemplate;

    public UserController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<UserDto> getUsers() {
        String sql = "SELECT * FROM users order by created_at desc";
        List<UserDto> users = jdbcTemplate.query(sql, new UserDtoMapper());
        users.forEach(user -> {
            String selectGroups = "select * from groups where id in (select group_id from group_members gm where gm.username=?)";
            user.setGroups(jdbcTemplate.query(selectGroups, new GroupDtoMapper(), user.getUsername()));

        });
        return users;
    }

    @PutMapping("/{username}")
    @Transactional
    public PreRegisterUserDto updateUser(@PathVariable("username") String username, @RequestBody PreRegisterUserDto userDto) {
        OffsetDateTime now = OffsetDateTime.now();
        String operator = SecurityContextHolder.getContext().getAuthentication().getName();
        String sql = """
                UPDATE users 
                SET first_name=?, 
                last_name=?, 
                email=?, 
                department_id=?,
                direct_manager_name=?,
                from_date=?,
                employee_number=?,
                telephone_number=?, 
                mobile_number=?, 
                employee_type_id=?, 
                employee_contract_number=?, 
                certification_description=?, 
                skill_set_description=?, 
                language_skills=?, 
                associated_gln=?, 
                profile_image_url=?, 
                updated_at=?, 
                updated_by=? 
                WHERE username=?
                """;
        int rows = jdbcTemplate.update(sql,
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getEmail(),
                userDto.getDepartmentId(),
                userDto.getDirectManagerName(),
                userDto.getFromDate(),
                userDto.getEmployeeNumber(),
                userDto.getTelephoneNumber(),
                userDto.getMobileNumber(),
                userDto.getEmployeeTypeId(),
                userDto.getEmployeeContractNumber(),
                userDto.getCertificationDescription(),
                userDto.getSkillSetDescription(),
                userDto.getLanguageSkills(),
                userDto.getAssociatedGln(),
                userDto.getProfileImageUrl(),
                now,
                operator,
                username);
        if (rows < 1) {
            throw new BusinessException("User not found: " + username);
        }
        if (userDto.getGroupIds() == null) {
            userDto.setGroupIds(new ArrayList<>());
        } else {
            userDto.setGroupIds(userDto.getGroupIds().stream().filter(Objects::nonNull).collect(Collectors.toList()));
        }
        // 查询当前用户所有的group_id
        String selectGroupsSql = "SELECT group_id FROM group_members WHERE username = ?";
        List<Long> currentGroupIds = jdbcTemplate.queryForList(selectGroupsSql, Long.class, username);

        // 将List转换为Set方便进行集合操作
        Set<Long> currentGroupsSet = new HashSet<>(currentGroupIds);
        Set<Long> newGroupsSet = new HashSet<>(userDto.getGroupIds());

        // 找出需要删除的group_id
        Set<Long> groupsToRemove = new HashSet<>(currentGroupsSet);
        groupsToRemove.removeAll(newGroupsSet);

        // 找出需要添加的group_id
        Set<Long> groupsToAdd = new HashSet<>(newGroupsSet);
        groupsToAdd.removeAll(currentGroupsSet);

        // 删除多余的group_id关联
        if (!groupsToRemove.isEmpty()) {
            String deleteSql = "DELETE FROM group_members WHERE username = ? AND group_id IN (?)";
            List<Object[]> batchArgs = groupsToRemove.stream()
                    .map(groupId -> new Object[]{username, groupId})
                    .collect(Collectors.toList());
            jdbcTemplate.batchUpdate(deleteSql, batchArgs);
        }

        // 添加缺失的group_id关联
        if (!groupsToAdd.isEmpty()) {
            String insertSql = "INSERT INTO group_members (username, group_id) VALUES (?, ?)";
            List<Object[]> batchArgs = groupsToAdd.stream()
                    .map(groupId -> new Object[]{username, groupId})
                    .collect(Collectors.toList());
            jdbcTemplate.batchUpdate(insertSql, batchArgs);
        }
        userDto.setUsername(username);
        return userDto;
    }

} 