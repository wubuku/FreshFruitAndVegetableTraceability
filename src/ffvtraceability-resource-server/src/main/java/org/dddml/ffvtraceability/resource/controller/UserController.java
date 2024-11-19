package org.dddml.ffvtraceability.resource.controller;

import org.dddml.ffvtraceability.resource.model.UserInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/me")
    public UserInfo getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(jwt.getSubject());
        
        // 从 JWT 中提取权限
        List<String> authorities = jwt.getClaimAsStringList("authorities");
        if (authorities != null) {
            userInfo.setAuthorities(new HashSet<>(authorities));
        } else {
            userInfo.setAuthorities(new HashSet<>());
        }
        
        // 从 JWT 中提取组信息
        List<String> groups = jwt.getClaimAsStringList("groups");
        if (groups != null) {
            userInfo.setGroups(new HashSet<>(groups));
        } else {
            userInfo.setGroups(new HashSet<>());
        }
        
        return userInfo;
    }
} 