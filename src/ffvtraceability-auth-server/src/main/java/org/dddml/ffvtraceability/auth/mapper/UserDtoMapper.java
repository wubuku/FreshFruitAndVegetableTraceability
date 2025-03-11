package org.dddml.ffvtraceability.auth.mapper;

import org.dddml.ffvtraceability.auth.dto.UserDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class UserDtoMapper implements RowMapper<UserDto> {
    @Override
    public UserDto mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        UserDto userDto = new UserDto();
        userDto.setUsername(rs.getString("username"));
        userDto.setEnabled(rs.getBoolean("enabled"));
        userDto.setTempPasswordLastGenerated(rs.getObject("temp_password_last_generated", OffsetDateTime.class));
        userDto.setPasswordChangeRequired(rs.getBoolean("password_change_required"));
        userDto.setDepartmentId(rs.getString("department_id"));
        userDto.setAssociatedGln(rs.getString("associated_gln"));
        userDto.setEmail(rs.getString("email"));
        userDto.setDirectManagerName(rs.getString("direct_manager_name"));
        userDto.setEmployeeNumber(rs.getString("employee_number"));
        userDto.setFirstName(rs.getString("first_name"));
        userDto.setLastName(rs.getString("last_name"));
        userDto.setLanguageSkills(rs.getString("language_skills"));
        userDto.setCertificationDescription(rs.getString("certification_description"));
        userDto.setMobileNumber(rs.getString("mobile_number"));
        userDto.setProfileImageUrl(rs.getString("profile_image_url"));
        userDto.setEmployeeContractNumber(rs.getString("employee_contract_number"));
        userDto.setEmployeeTypeId(rs.getString("employee_type_id"));
        userDto.setSkillSetDescription(rs.getString("skill_set_description"));
        userDto.setTelephoneNumber(rs.getString("telephone_number"));
        userDto.setUpdatedBy(rs.getString("updated_by"));
        userDto.setCreatedBy(rs.getString("created_by"));
        userDto.setFromDate(rs.getObject("from_date", OffsetDateTime.class));
        userDto.setUpdatedAt(rs.getObject("updated_at", OffsetDateTime.class));
        userDto.setCreatedAt(rs.getObject("created_at", OffsetDateTime.class));
        return userDto;
    }
}