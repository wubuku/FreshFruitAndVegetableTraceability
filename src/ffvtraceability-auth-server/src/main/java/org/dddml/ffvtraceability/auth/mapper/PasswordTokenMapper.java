package org.dddml.ffvtraceability.auth.mapper;

import org.dddml.ffvtraceability.auth.dto.PasswordTokenDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class PasswordTokenMapper implements RowMapper<PasswordTokenDto> {
    @Override
    public PasswordTokenDto mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        PasswordTokenDto passwordTokenDto = new PasswordTokenDto();
        passwordTokenDto.setUsername(rs.getString("username"));
        passwordTokenDto.setToken(rs.getString("token"));
        passwordTokenDto.setPasswordCreatedAt(rs.getObject("password_created_at", OffsetDateTime.class));
        passwordTokenDto.setTokenCreatedAt(rs.getObject("token_created_at", OffsetDateTime.class));
        passwordTokenDto.setUsername(rs.getString("username"));
        passwordTokenDto.setType(rs.getString("type"));
        return passwordTokenDto;
    }
}