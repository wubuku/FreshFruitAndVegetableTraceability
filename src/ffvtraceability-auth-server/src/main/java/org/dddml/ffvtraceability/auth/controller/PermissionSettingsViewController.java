package org.dddml.ffvtraceability.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PermissionSettingsViewController {

    @GetMapping("/permission-settings")
    //@PreAuthorize("hasRole('ADMIN')")
    public String permissionSettingsPage() {
        return "permission-settings";
    }
} 