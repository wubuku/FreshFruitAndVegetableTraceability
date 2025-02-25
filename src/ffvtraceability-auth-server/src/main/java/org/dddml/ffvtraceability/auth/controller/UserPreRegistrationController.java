package org.dddml.ffvtraceability.auth.controller;

import jakarta.validation.Valid;
import org.dddml.ffvtraceability.auth.dto.PreRegisterUserDto;
import org.dddml.ffvtraceability.auth.dto.PreRegisterUserResponse;
import org.dddml.ffvtraceability.auth.dto.UserDto;
import org.dddml.ffvtraceability.auth.service.UserPreRegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserPreRegistrationController {

    private final UserPreRegistrationService userPreRegistrationService;

    public UserPreRegistrationController(UserPreRegistrationService userPreRegistrationService) {
        this.userPreRegistrationService = userPreRegistrationService;
    }

    @PostMapping("/pre-register")
    public ResponseEntity<PreRegisterUserResponse> preRegisterUser(
            @Valid @RequestBody PreRegisterUserDto preRegisterUser) {
        PreRegisterUserResponse response = userPreRegistrationService.preRegisterUser(preRegisterUser);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUserByUserName(@PathVariable("username") String username) {
        return null;
    }
}