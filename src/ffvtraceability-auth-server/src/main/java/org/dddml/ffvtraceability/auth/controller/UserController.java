package org.dddml.ffvtraceability.auth.controller;

import org.dddml.ffvtraceability.auth.dto.UserDto;
import org.dddml.ffvtraceability.auth.mapper.GroupDtoMapper;
import org.dddml.ffvtraceability.auth.mapper.UserDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth-srv/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final JdbcTemplate jdbcTemplate;

    public UserController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public ResponseEntity<?> getUsers() {
        try {
            String sql = "SELECT * FROM users order by created_at desc";
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

} 