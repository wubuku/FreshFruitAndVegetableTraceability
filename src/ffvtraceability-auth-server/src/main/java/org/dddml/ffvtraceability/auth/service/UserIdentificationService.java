package org.dddml.ffvtraceability.auth.service;

import org.dddml.ffvtraceability.auth.dto.UserIdentificationDto;
import org.dddml.ffvtraceability.auth.mapper.UserIdentificationMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserIdentificationService {
    private final JdbcTemplate jdbcTemplate;

    public UserIdentificationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Find a user by identification type and identifier
     * @param identificationType The type of identification (e.g., "WECHAT_OPENID", "PHONE")
     * @param identifier The identifier value (e.g., the WeChat OpenID or phone number)
     * @return Optional containing the username if found
     */
    @Transactional(readOnly = true)
    public Optional<String> findUsernameByIdentifier(String identificationType, String identifier) {
        String sql = """
                SELECT username FROM user_identifications 
                WHERE user_identification_type_id = ? AND identifier = ?
                """;
        List<String> results = jdbcTemplate.queryForList(sql, String.class, identificationType, identifier);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    /**
     * Get all identifications for a user
     * @param username The username
     * @return List of user identifications
     */
    @Transactional(readOnly = true)
    public List<UserIdentificationDto> getUserIdentifications(String username) {
        String sql = """
                SELECT * FROM user_identifications 
                WHERE username = ?
                """;
        return jdbcTemplate.query(sql, new UserIdentificationMapper(), username);
    }

    /**
     * Add a new identification for a user
     * @param username The username
     * @param identificationType The type of identification
     * @param identifier The identifier value
     * @param verified Whether the identification is verified
     */
    @Transactional
    public void addUserIdentification(String username, String identificationType, 
                                     String identifier, boolean verified) {
        OffsetDateTime now = OffsetDateTime.now();
        String sql = """
                INSERT INTO user_identifications 
                (user_identification_type_id, username, identifier, verified, 
                verified_at, created_at, updated_at) 
                VALUES (?, ?, ?, ?, ?, ?, ?)
                ON CONFLICT (user_identification_type_id, username) DO UPDATE 
                SET identifier = ?, verified = ?, verified_at = ?, updated_at = ?
                """;
        
        jdbcTemplate.update(sql, 
                identificationType, username, identifier, verified, 
                verified ? now : null, now, now,
                identifier, verified, verified ? now : null, now);
    }

    /**
     * Mark an identification as verified
     * @param username The username
     * @param identificationType The type of identification
     */
    @Transactional
    public void verifyIdentification(String username, String identificationType) {
        OffsetDateTime now = OffsetDateTime.now();
        String sql = """
                UPDATE user_identifications 
                SET verified = true, verified_at = ?, updated_at = ? 
                WHERE username = ? AND user_identification_type_id = ?
                """;
        jdbcTemplate.update(sql, now, now, username, identificationType);
    }

    /**
     * Remove an identification for a user
     * @param username The username
     * @param identificationType The type of identification
     */
    @Transactional
    public void removeUserIdentification(String username, String identificationType) {
        String sql = """
                DELETE FROM user_identifications 
                WHERE username = ? AND user_identification_type_id = ?
                """;
        jdbcTemplate.update(sql, username, identificationType);
    }
} 