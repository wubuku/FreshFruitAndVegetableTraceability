package org.dddml.ffvtraceability.auth.controller;

import jakarta.validation.Valid;
import org.dddml.ffvtraceability.auth.dto.PreRegisterUserDto;
import org.dddml.ffvtraceability.auth.dto.PreRegisterUserResponse;
import org.dddml.ffvtraceability.auth.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth-srv/users")
public class UserPreRegistrationController {

    private final UserService userService;

    public UserPreRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/pre-register")
    @Transactional
    public PreRegisterUserResponse preRegisterUser(
            @Valid @RequestBody PreRegisterUserDto preRegisterUser) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.preRegisterUser(preRegisterUser, currentUsername);
    }


    @PutMapping("/{username}/regenerate-password")
    @Transactional
    public PreRegisterUserResponse reGeneratePassword(@PathVariable("username") String username) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.reGeneratePassword(username, currentUsername);
    }
}