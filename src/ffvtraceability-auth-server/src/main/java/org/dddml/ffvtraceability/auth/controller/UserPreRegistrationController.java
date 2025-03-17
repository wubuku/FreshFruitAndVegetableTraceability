package org.dddml.ffvtraceability.auth.controller;

import jakarta.validation.Valid;
import org.dddml.ffvtraceability.auth.dto.PreRegisterUserDto;
import org.dddml.ffvtraceability.auth.dto.PreRegisterUserResponse;
import org.dddml.ffvtraceability.auth.service.UserPreRegistrationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth-srv/users")
public class UserPreRegistrationController {

    private final UserPreRegistrationService userPreRegistrationService;

    public UserPreRegistrationController(UserPreRegistrationService userPreRegistrationService) {
        this.userPreRegistrationService = userPreRegistrationService;
    }

    @PostMapping("/pre-register")
    public PreRegisterUserResponse preRegisterUser(
            @Valid @RequestBody PreRegisterUserDto preRegisterUser) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return userPreRegistrationService.preRegisterUser(preRegisterUser, currentUsername);
    }


    @PutMapping("/{username}/regenerate-password")
    public PreRegisterUserResponse reGeneratePassword(@PathVariable("username") String username) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return userPreRegistrationService.reGeneratePassword(username, currentUsername);
    }
}