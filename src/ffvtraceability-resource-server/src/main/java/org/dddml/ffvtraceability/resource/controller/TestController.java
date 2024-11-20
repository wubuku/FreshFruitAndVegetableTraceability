package org.dddml.ffvtraceability.resource.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/public/test")
    public String publicTest() {
        return "Public API - No authentication required";
    }

    @GetMapping("/test")
    public String authenticatedTest(@AuthenticationPrincipal Jwt jwt) {
        return "Authenticated API - Hello, " + jwt.getSubject();
    }

    @PreAuthorize("hasAnyAuthority('DIRECT_ADMIN_AUTH','ROLE_ADMIN')") // NOTE: 需要启用方法级安全这里才生效
    @GetMapping("/admin/test")
    public String adminTest(@AuthenticationPrincipal Jwt jwt) {
        return "Admin API - Hello, administrator " + jwt.getSubject();
    }
}