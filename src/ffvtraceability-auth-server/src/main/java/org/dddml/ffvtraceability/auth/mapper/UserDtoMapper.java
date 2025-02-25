package org.dddml.ffvtraceability.auth.mapper;

import org.dddml.ffvtraceability.auth.dto.UserDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDtoMapper implements RowMapper<UserDto> {
    @Override
    public UserDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserDto userDto = new UserDto();
        userDto.setUsername(rs.getString("username"));
        userDto.setEnabled(rs.getBoolean("enabled"));
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
        return userDto;
    }
}