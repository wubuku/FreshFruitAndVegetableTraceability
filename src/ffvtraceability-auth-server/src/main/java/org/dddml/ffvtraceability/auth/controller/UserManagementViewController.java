package org.dddml.ffvtraceability.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserManagementViewController {

    @GetMapping("/user-management")
    // @PreAuthorize("hasRole('ADMIN')") // 这是方法级安全，需要在 SecurityConfig 中启用配置
    public String userManagementPage() {
        return "user-management";
    }
} 