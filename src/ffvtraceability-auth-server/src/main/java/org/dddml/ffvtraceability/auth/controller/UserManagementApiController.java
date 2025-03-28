package org.dddml.ffvtraceability.auth.controller;

import org.dddml.ffvtraceability.auth.dto.UserDto;
import org.dddml.ffvtraceability.auth.exception.BusinessException;
import org.dddml.ffvtraceability.auth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth-srv/users")
public class UserManagementApiController {
    private static final Logger logger = LoggerFactory.getLogger(UserManagementApiController.class);

    private final JdbcTemplate jdbcTemplate;
    private final UserService userService;

    public UserManagementApiController(JdbcTemplate jdbcTemplate, UserService userService) {
        this.jdbcTemplate = jdbcTemplate;
        this.userService = userService;
    }

    @GetMapping("/list")
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getUsers() {
        String sql = """
                SELECT u.username, u.enabled, u.password_change_required,
                       STRING_AGG(DISTINCT g.group_name, ', ') as groups,
                       STRING_AGG(DISTINCT a.authority, ', ') as authorities
                FROM users u
                LEFT JOIN group_members gm ON u.username = gm.username
                LEFT JOIN groups g ON gm.group_id = g.id
                LEFT JOIN authorities a ON u.username = a.username
                WHERE u.username != '*'
                GROUP BY u.username, u.enabled, u.password_change_required
                ORDER BY u.username
                """;

        return jdbcTemplate.queryForList(sql);
    }


    @GetMapping("/{username}")
    @Transactional(readOnly = true)
    public UserDto getUserByUserName(@PathVariable("username") String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping("/{username}/toggle-enabled")
    @Transactional
    public void toggleEnabled(@PathVariable String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        if (username.equals(currentUsername)) {
            throw new BusinessException("Cannot disable your own account");
        }
        jdbcTemplate.update(
                "UPDATE users SET enabled = NOT enabled WHERE username = ?",
                username
        );
    }

    @PostMapping("/{username}/toggle-password-change")
    public void togglePasswordChange(@PathVariable String username) {
        jdbcTemplate.update(
                "UPDATE users SET password_change_required = NOT password_change_required WHERE username = ?",
                username
        );
    }
} 