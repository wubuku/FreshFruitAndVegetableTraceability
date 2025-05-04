package org.dddml.ffvtraceability.auth.mapper;

import org.dddml.ffvtraceability.auth.dto.UserIdentificationDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class UserIdentificationMapper implements RowMapper<UserIdentificationDto> {

    @Override
    public UserIdentificationDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserIdentificationDto dto = new UserIdentificationDto();
        dto.setUserIdentificationTypeId(rs.getString("user_identification_type_id"));
        dto.setUsername(rs.getString("username"));
        dto.setIdentifier(rs.getString("identifier"));
        dto.setVerified(rs.getBoolean("verified"));
        
        // Handle nullable timestamp fields
        if (rs.getTimestamp("verified_at") != null) {
            dto.setVerifiedAt(rs.getTimestamp("verified_at").toInstant().atOffset(OffsetDateTime.now().getOffset()));
        }
        if (rs.getTimestamp("created_at") != null) {
            dto.setCreatedAt(rs.getTimestamp("created_at").toInstant().atOffset(OffsetDateTime.now().getOffset()));
        }
        if (rs.getTimestamp("updated_at") != null) {
            dto.setUpdatedAt(rs.getTimestamp("updated_at").toInstant().atOffset(OffsetDateTime.now().getOffset()));
        }
        
        return dto;
    }
} 