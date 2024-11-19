package org.dddml.ffvtraceability.resource.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    
    @Autowired
    @Qualifier("securityJdbcTemplate")
    private JdbcTemplate securityJdbcTemplate;
    
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        
        // 1. 添加直接权限
        getClaimAsSet(jwt, "authorities")
            .stream()
            .map(SimpleGrantedAuthority::new)
            .forEach(authorities::add);
            
        // 2. 从组恢复权限
        getClaimAsSet(jwt, "groups")
            .stream()
            .map(this::getGroupAuthorities)
            .flatMap(Set::stream)
            .map(SimpleGrantedAuthority::new)
            .forEach(authorities::add);
        
        return new JwtAuthenticationToken(jwt, authorities);
    }
    
    @SuppressWarnings("unchecked")
    private Set<String> getClaimAsSet(Jwt jwt, String claimName) {
        Object claim = jwt.getClaims().get(claimName);
        if (claim instanceof Collection) {
            return new HashSet<>((Collection<String>) claim);
        }
        return Collections.emptySet();
    }
    
    private Set<String> getGroupAuthorities(String groupName) {
        String sql = """
            SELECT authority 
            FROM group_authorities ga 
            JOIN groups g ON ga.group_id = g.id 
            WHERE g.group_name = ?
            """;
            
        return new HashSet<>(jdbcTemplate.queryForList(sql, String.class, 
            groupName.replace("GROUP_", "")));  // 移除GROUP_前缀
    }
} 