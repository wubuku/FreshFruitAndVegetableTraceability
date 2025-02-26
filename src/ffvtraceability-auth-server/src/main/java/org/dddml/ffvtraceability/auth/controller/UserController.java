package org.dddml.ffvtraceability.auth.controller;

import org.dddml.ffvtraceability.auth.dto.UserDto;
import org.dddml.ffvtraceability.auth.mapper.GroupDtoMapper;
import org.dddml.ffvtraceability.auth.mapper.UserDtoMapper;
import org.dddml.ffvtraceability.auth.service.UserPreRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth-srv/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final JdbcTemplate jdbcTemplate;
    private final UserPreRegistrationService userPreRegistrationService;

    public UserController(JdbcTemplate jdbcTemplate, UserPreRegistrationService userPreRegistrationService) {
        this.jdbcTemplate = jdbcTemplate;
        this.userPreRegistrationService = userPreRegistrationService;
    }

    @GetMapping
    public ResponseEntity<?> getUsers() {
        try {
            String sql = "SELECT * FROM users";
            List<UserDto> users = jdbcTemplate.query(sql, new UserDtoMapper());
            users.forEach(user -> {
                String selectGroups = "select * from groups where id in (select group_id from group_members gm where gm.username=?)";
                user.setGroups(jdbcTemplate.query(selectGroups, new GroupDtoMapper(), user.getUsername()));

            });
            return ResponseEntity.ok(users);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUserByUserName(@PathVariable("username") String username) {
        UserDto userDto = userPreRegistrationService.getUserByUsername(username);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/{username}/toggle-enabled")
    public ResponseEntity<?> toggleEnabled(@PathVariable String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        if (username.equals(currentUsername)) {
            return ResponseEntity.badRequest().body("Cannot disable your own account");
        }

        try {
            jdbcTemplate.update(
                    "UPDATE users SET enabled = NOT enabled WHERE username = ?",
                    username
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Failed to toggle user enabled status", e);
            return ResponseEntity.badRequest().body("Failed to update user status");
        }
    }

    @PostMapping("/{username}/toggle-password-change")
    public ResponseEntity<?> togglePasswordChange(@PathVariable String username) {
        try {
            jdbcTemplate.update(
                    "UPDATE users SET password_change_required = NOT password_change_required WHERE username = ?",
                    username
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Failed to toggle password change required", e);
            return ResponseEntity.badRequest().body("Failed to update password change status");
        }
    }
} 