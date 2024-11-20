package org.dddml.ffvtraceability.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserManagementViewController {

    @GetMapping("/user-management")
    @PreAuthorize("hasRole('ADMIN')")
    public String userManagementPage() {
        return "user-management";
    }
} 