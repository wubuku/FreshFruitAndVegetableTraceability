package org.dddml.ffvtraceability.auth.service;

import org.dddml.ffvtraceability.auth.dto.PreRegisterUserDto;
import org.dddml.ffvtraceability.auth.dto.PreRegisterUserResponse;
import org.dddml.ffvtraceability.auth.dto.UserDto;
import org.dddml.ffvtraceability.auth.mapper.GroupDtoMapper;
import org.dddml.ffvtraceability.auth.mapper.UserDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class UserPreRegistrationService {
    private static final Logger logger = LoggerFactory.getLogger(UserPreRegistrationService.class);
    private static final String ALLOWED_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int OTP_LENGTH = 6;

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom random;

    public UserPreRegistrationService(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
        this.random = new SecureRandom();
    }

    @Transactional(readOnly = true)
    public UserDto getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        UserDto user = jdbcTemplate.query(sql, new UserDtoMapper(), username).stream().findFirst().orElse(null);
        //UserDto user = jdbcTemplate.queryForObject(sql, new UserDtoMapper(), username);
        //注释掉的这个写法当遇到没有结果时会报异常EmptyResultDataAccessException
        if (user != null) {
            sql = "select * from groups where id in (select group_id from group_members gm where gm.username=?)";
            user.setGroups(jdbcTemplate.query(sql, new GroupDtoMapper(), username));
//            String sqlGetPermissions = """
//                        SELECT a.authority
//                        FROM authorities a
//                        JOIN permissions p ON a.authority = p.permission_id
//                        WHERE a.username = ?
//                        AND (p.enabled IS NULL OR p.enabled = true)
//                        """; //如果用这个查询语句，那么给admin预设的几个权限就没了。
            sql = "select distinct authority from authorities where username=?";
            user.setPermissions(jdbcTemplate.queryForList(sql, String.class, username));
        }
        return user;
    }

    @Transactional
    public PreRegisterUserResponse preRegisterUser(PreRegisterUserDto preRegisterUser, String operator) {
        // Check if user already exists
        String username = preRegisterUser.getUsername();
        if (userExists(username)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }

        // Generate one-time password
        String oneTimePassword = generateOneTimePassword();
        String encodedPassword = passwordEncoder.encode(oneTimePassword);

        OffsetDateTime now = OffsetDateTime.now();
        // Insert new user
        jdbcTemplate.update(
                "INSERT INTO users (username, password, enabled, password_change_required, first_login,first_name,last_name," +
                        "email,department_id,from_data,employee_number,employee_contract_number,certification_description,skill_set_description," +
                        "language_skills,associated_gln,profile_image_url,direct_manager_name,employee_type_id,telephone_number," +
                        "mobile_number,created_at,updated_at,created_by,updated_by)" +
                        " VALUES (?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                username, encodedPassword, true, true, true, preRegisterUser.getFirstName(), preRegisterUser.getLastName(),
                preRegisterUser.getEmail(), preRegisterUser.getDepartmentId(), preRegisterUser.getFromDate(), preRegisterUser.getEmployeeNumber(),
                preRegisterUser.getEmployeeContractNumber(), preRegisterUser.getCertificationDescription(), preRegisterUser.getSkillSetDescription(),
                preRegisterUser.getLanguageSkills(), preRegisterUser.getAssociatedGln(), preRegisterUser.getProfileImageUrl(),
                preRegisterUser.getDirectManagerName(), preRegisterUser.getEmployeeTypeId(), preRegisterUser.getTelephoneNumber(), preRegisterUser.getMobileNumber(),
                now, now, operator, operator
        );

        List<Long> groupIds = preRegisterUser.getGroupIds().stream().distinct().toList();
        if (!groupIds.isEmpty()) {
            groupIds.forEach(groupId -> {
                jdbcTemplate.update("INSERT INTO group_members (username, group_id) values(?,?)", username, groupId);
            });
        } else {
            // 如果 groupIds 为空那么至少要 Add to USER_GROUP
            jdbcTemplate.update(
                    "INSERT INTO group_members (username, group_id) SELECT ?, id FROM groups WHERE group_name = 'USER_GROUP'",
                    username
            );
        }

        logger.info("Pre-registered user: {}", username);
        return new PreRegisterUserResponse(username, oneTimePassword);
    }

    private boolean userExists(String username) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE username = ?",
                Integer.class,
                username
        );
        return count != null && count > 0;
    }

    private String generateOneTimePassword() {
        StringBuilder sb = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            sb.append(ALLOWED_CHARS.charAt(random.nextInt(ALLOWED_CHARS.length())));
        }
        return sb.toString();
    }
}