package org.dddml.ffvtraceability.auth.controller;

import jakarta.validation.Valid;
import org.dddml.ffvtraceability.auth.dto.PreRegisterUserDto;
import org.dddml.ffvtraceability.auth.dto.PreRegisterUserResponse;
import org.dddml.ffvtraceability.auth.service.UserPreRegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserPreRegistrationController {

    private final UserPreRegistrationService userPreRegistrationService;

    public UserPreRegistrationController(UserPreRegistrationService userPreRegistrationService) {
        this.userPreRegistrationService = userPreRegistrationService;
    }

    @PostMapping("/pre-register")
    public ResponseEntity<?> preRegisterUser(
            @Valid @RequestBody PreRegisterUserDto preRegisterUser) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            PreRegisterUserResponse response = userPreRegistrationService.preRegisterUser(preRegisterUser, currentUsername);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Register user failed:" + ex.getMessage());
        }
    }


}