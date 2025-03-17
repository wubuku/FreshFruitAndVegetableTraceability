package org.dddml.ffvtraceability.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserManagementViewController {

    @GetMapping("/user-management")
    //@PreAuthorize("hasRole('ADMIN')")
    public String userManagementPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUsername", auth.getName());
        return "user-management";
    }
} 