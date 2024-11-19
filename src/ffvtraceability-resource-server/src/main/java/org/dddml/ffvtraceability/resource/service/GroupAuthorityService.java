package org.dddml.ffvtraceability.resource.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class GroupAuthorityService {
    private static final Logger logger = LoggerFactory.getLogger(GroupAuthorityService.class);
    
    @Autowired
    @Qualifier("securityJdbcTemplate")
    private JdbcTemplate securityJdbcTemplate;
    
    @Cacheable(value = "groupAuthorities", key = "#groupName")
    public Set<String> getGroupAuthorities(String groupName) {
        logger.info("Cache MISS - Loading authorities from database for group: {}", groupName);
        String sql = """
            SELECT authority 
            FROM group_authorities ga 
            JOIN groups g ON ga.group_id = g.id 
            WHERE g.group_name = ?
            """;
            
        Set<String> authorities = new HashSet<>(securityJdbcTemplate.queryForList(sql, String.class,
            groupName.replace("GROUP_", "")));
        logger.debug("Loaded {} authorities from database for group: {}", authorities.size(), groupName);
        return authorities;
    }
} 