package org.dddml.ffvtraceability.auth.mapper;

import org.dddml.ffvtraceability.auth.dto.GroupDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupDtoMapper implements RowMapper<GroupDto> {
    @Override
    public GroupDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        GroupDto groupDto = new GroupDto();
        groupDto.setId(rs.getLong("id"));
        groupDto.setGroupName(rs.getString("group_name"));
        groupDto.setEnabled(rs.getBoolean("enabled"));
        groupDto.setDescription(rs.getString("description"));
        return groupDto;
    }
}