package org.dddml.ffvtraceability.auth.service;

import org.dddml.ffvtraceability.auth.config.PasswordTokenProperties;
import org.dddml.ffvtraceability.auth.dto.PreRegisterUserDto;
import org.dddml.ffvtraceability.auth.dto.PreRegisterUserResponse;
import org.dddml.ffvtraceability.auth.dto.UserDto;
import org.dddml.ffvtraceability.auth.exception.BusinessException;
import org.dddml.ffvtraceability.auth.mapper.GroupDtoMapper;
import org.dddml.ffvtraceability.auth.mapper.UserDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final String ALLOWED_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int OTP_LENGTH = 6;

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom random;
    //@Autowired
    private final PasswordTokenProperties passwordTokenProperties;
    @Autowired
    private PasswordTokenService passwordTokenService;
    @Autowired
    private EmailService emailService;

    public UserService(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder, PasswordTokenProperties passwordTokenProperties) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
        this.random = new SecureRandom();
        this.passwordTokenProperties = passwordTokenProperties;
    }

    public void sendCreatePasswordEmail(String mailTo, String token) {
        StringBuilder sbLink = new StringBuilder();
        sbLink.append(passwordTokenProperties.getCreatePasswordUrl()).append("?").append("token=").append(token).append("&type=register");
        StringBuilder sbHtml = new StringBuilder("""
                <div style="width: 100%; height: 100%; padding: 46px; background: white; overflow: hidden; outline: 1px #D4D4D8 solid; outline-offset: -1px; flex-direction: column; justify-content: flex-start; align-items: flex-start; gap: 24px; display: inline-flex">
                    <img style="width: 165.31px; height: 49.65px" src="cid:logo" />
                    <div style="align-self: stretch; flex-direction: column; justify-content: flex-start; align-items: flex-start; gap: 8px; display: flex">
                        <div style="align-self: stretch; color: black; font-size: 24px; font-family: Inter; font-weight: 600; line-height: 32px; word-wrap: break-word">Finish Setting up Your Account </div>
                        <div style="align-self: stretch; color: black; font-size: 16px; font-family: Inter; font-weight: 400; line-height: 24px; word-wrap: break-word">Use the link below to complete your account setup. It is valid for """);
        sbHtml.append(" ").append(passwordTokenProperties.getExpireInHours()).append(" ");
        sbHtml.append(""" 
                hours. If it expires, contact the admin to request a new one.</div>
                </div>
                <button onclick="window.open('""");
        sbHtml.append(sbLink);
        sbHtml.append("""
                ', '_blank')"
                    style="
                    height: 40px;
                    padding: 8px 16px;
                    background: #15803D;
                    border-radius: 4px;
                    color: #FFFFFF;
                    border: none;
                    cursor: pointer;
                    font-family: Inter;
                    font-size: 16px;
                    gap: 8px;
                    display: inline-flex;
                    align-items: center;
                    justify-content: center;
                ">
                Finish set-up
                    </button>
                    <div style="width: 522.89px; height: 0px; outline: 1px #D4D4D8 solid; outline-offset: -0.50px"></div>
                    <div data-orientation="Horizontal" style="justify-content: center; align-items: center; gap: 4px; display: inline-flex">
                        <div style="text-align: center; color: black; font-size: 14px; font-family: Inter; font-weight: 500; line-height: 20px; word-wrap: break-word">Powered by</div>
                        <img style="width: 96.30px; height: 27.91px" src="cid:blueforce" />
                    </div>
                </div>
                """);
//        sbHtml.append("<br><br><a href='");
//        sbHtml.append(sbLink.toString()).append("'>").append(sbLink.toString()).append("</a>");
        Map<String, ClassPathResource> inlineResources = new HashMap<>();
        inlineResources.put("logo", new ClassPathResource("images/logo.png"));
        inlineResources.put("blueforce", new ClassPathResource("images/blueforce.png"));
//        StringBuilder sb = new StringBuilder();
//        sb.append("Finish Setting up Your Account\r\n");
//        sb.append("Use the link below to complete your account setup.\r\n");
//        sb.append("It's valid for ").append(passwordTokenProperties.getExpireInHours()).append(" hours.\r\n");
//        sb.append("If expired, you can request a new one.\r\n");
//        sb.append(passwordTokenProperties.getCreatePasswordUrl()).append("?").append("token=").append(token);
//        sb.append("&type=register\r\n");
//        sb.append("\r\n");
//        sb.append("Thanks\r\n");
//        sb.append("Powered by Fresh Fruit & Vegetable Traceability System\r\n");
        emailService.sendHtmlMail(mailTo, "Finish Setting up Your Account", sbHtml.toString(), inlineResources);
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
            // 获取用户关联的所有组的权限集合
            String sqlPermissions = """
                        SELECT DISTINCT p.permission_id
                        FROM permissions p
                        JOIN group_authorities ga ON p.permission_id = ga.authority
                        JOIN group_members gm ON ga.group_id = gm.group_id
                        WHERE gm.username = ?
                    """;
            List<String> permissions = jdbcTemplate.queryForList(sqlPermissions, String.class, username);
            user.setPermissions(permissions);
        }
        return user;
    }

    /**
     * 给指定用户重新生成密码
     *
     * @param username
     * @param operator
     * @return
     */
    @Transactional
    public PreRegisterUserResponse reGeneratePassword(String username, String operator) {
        String sql = "SELECT * FROM users WHERE username = ?";
        UserDto user = jdbcTemplate.query(sql, new UserDtoMapper(), username).stream().findFirst().orElse(null);
        if (user == null) {
            throw new BusinessException("User not found: " + username);
        }
        String oneTimePassword = generateOneTimePassword();
        String encodedPassword = passwordEncoder.encode(oneTimePassword);
        OffsetDateTime now = OffsetDateTime.now();
        jdbcTemplate.update(
                """
                        UPDATE users SET 
                        password = ?,
                        password_change_required = true, 
                        temp_password_last_generated = ?,
                        updated_by = ?,
                        updated_at = ?
                        WHERE username = ?
                        """,
                encodedPassword, now, operator, now, username
        );
        return new PreRegisterUserResponse(username, oneTimePassword, now);
    }

    @Transactional
    public PreRegisterUserResponse preRegisterUser(PreRegisterUserDto preRegisterUser, String operator) {
        // Check if user already exists
        String username = preRegisterUser.getUsername();
        if (userExists(username)) {
            throw new BusinessException("Username already exists: " + username);
        }

        // Generate one-time password
        String oneTimePassword = generateOneTimePassword();
        String encodedPassword = passwordEncoder.encode(oneTimePassword);

        OffsetDateTime now = OffsetDateTime.now();
        // Insert new user
        jdbcTemplate.update(
                "INSERT INTO users (username, password, enabled, password_change_required,temp_password_last_generated, first_login,first_name,last_name," +
                        "email,department_id,from_date,employee_number,employee_contract_number,certification_description,skill_set_description," +
                        "language_skills,associated_gln,profile_image_url,direct_manager_name,employee_type_id,telephone_number," +
                        "mobile_number,created_at,updated_at,created_by,updated_by)" +
                        " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                username, encodedPassword, true, true, now, true, preRegisterUser.getFirstName(), preRegisterUser.getLastName(),
                preRegisterUser.getEmail(), preRegisterUser.getDepartmentId(), preRegisterUser.getFromDate(), preRegisterUser.getEmployeeNumber(),
                preRegisterUser.getEmployeeContractNumber(), preRegisterUser.getCertificationDescription(), preRegisterUser.getSkillSetDescription(),
                preRegisterUser.getLanguageSkills(), preRegisterUser.getAssociatedGln(), preRegisterUser.getProfileImageUrl(),
                preRegisterUser.getDirectManagerName(), preRegisterUser.getEmployeeTypeId(), preRegisterUser.getTelephoneNumber(), preRegisterUser.getMobileNumber(),
                now, now, operator, operator
        );
        if (preRegisterUser.getGroupIds() == null) {
            preRegisterUser.setGroupIds(new ArrayList<>());
        }
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
        String token = UUID.randomUUID().toString();
        passwordTokenService.savePermissionToken(username, token, "register", now);
        sendCreatePasswordEmail(username, token);
        logger.info("Pre-registered user: {}", username);
        return new PreRegisterUserResponse(username, oneTimePassword, now);
    }


    private boolean userExists(String username) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE username = ?",
                Integer.class,
                username
        );
        return count > 0;
    }

    private String generateOneTimePassword() {
        StringBuilder sb = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            sb.append(ALLOWED_CHARS.charAt(random.nextInt(ALLOWED_CHARS.length())));
        }
        return sb.toString();
    }
}