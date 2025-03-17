package org.dddml.ffvtraceability.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth-srv")
public class PreRegisterViewController {

    @GetMapping("/pre-register")
    //@PreAuthorize("hasRole('ADMIN')")
    public String preRegisterPage() {
        return "pre-register";
    }
} 