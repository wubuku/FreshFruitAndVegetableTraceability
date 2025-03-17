package org.dddml.ffvtraceability.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth-srv")
public class GroupManagementViewController {

    @GetMapping("/group-management")
    //@PreAuthorize("hasRole('ADMIN')")
    public String groupManagementPage() {
        return "group-management";
    }
} 