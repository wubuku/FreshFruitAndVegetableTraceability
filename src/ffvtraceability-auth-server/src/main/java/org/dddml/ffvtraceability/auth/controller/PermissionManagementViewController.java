package org.dddml.ffvtraceability.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PermissionManagementViewController {

    @GetMapping("/permission-management")
    //@PreAuthorize("hasRole('ADMIN')")
    public String permissionManagementPage() {
        return "permission-management";
    }
} 