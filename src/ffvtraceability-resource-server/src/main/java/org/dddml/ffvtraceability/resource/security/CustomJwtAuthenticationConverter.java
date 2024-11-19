package org.dddml.ffvtraceability.resource.security;

import org.dddml.ffvtraceability.resource.service.GroupAuthorityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomJwtAuthenticationConverter.class);
    
    @Autowired
    private GroupAuthorityService groupAuthorityService;
    
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        
        // 记录转换开始
        logger.debug("Converting JWT to Authentication for subject: {}", jwt.getSubject());
        
        // 1. 添加直接权限
        Set<String> directAuthorities = getClaimAsSet(jwt, "authorities");
        logger.debug("Direct authorities from JWT: {}", directAuthorities);
        directAuthorities.stream()
            .map(SimpleGrantedAuthority::new)
            .forEach(authorities::add);
            
        // 2. 从组恢复权限
        Set<String> groups = getClaimAsSet(jwt, "groups");
        logger.debug("Groups from JWT: {}", groups);
        
        groups.stream()
            .map(group -> {
                Set<String> groupAuths = groupAuthorityService.getGroupAuthorities(group);
                logger.debug("Authorities for group {}: {}", group, groupAuths);
                return groupAuths;
            })
            .flatMap(Set::stream)
            .map(SimpleGrantedAuthority::new)
            .forEach(authorities::add);
        
        logger.debug("Final combined authorities: {}", authorities);
        return new JwtAuthenticationToken(jwt, authorities);
    }
    
    @SuppressWarnings("unchecked")
    private Set<String> getClaimAsSet(Jwt jwt, String claimName) {
        Object claim = jwt.getClaims().get(claimName);
        if (claim instanceof Collection) {
            return new HashSet<>((Collection<String>) claim);
        }
        logger.debug("No {} found in JWT claims", claimName);
        return Collections.emptySet();
    }
}