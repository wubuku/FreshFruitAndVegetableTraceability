package org.dddml.ffvtraceability.auth.controller;

import jakarta.validation.Valid;
import org.dddml.ffvtraceability.auth.dto.PreRegisterUserRequest;
import org.dddml.ffvtraceability.auth.dto.PreRegisterUserResponse;
import org.dddml.ffvtraceability.auth.service.UserPreRegistrationService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<PreRegisterUserResponse> preRegisterUser(
            @Valid @RequestBody PreRegisterUserRequest request) {
        PreRegisterUserResponse response = userPreRegistrationService.preRegisterUser(request.getUsername());
        return ResponseEntity.ok(response);
    }
}