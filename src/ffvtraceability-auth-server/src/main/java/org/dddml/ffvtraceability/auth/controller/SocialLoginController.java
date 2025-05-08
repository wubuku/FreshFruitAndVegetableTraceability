package org.dddml.ffvtraceability.auth.controller;

import org.dddml.ffvtraceability.auth.exception.AuthenticationException;
import org.dddml.ffvtraceability.auth.service.SmsService;
import org.dddml.ffvtraceability.auth.service.WeChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class SocialLoginController {
    private static final Logger logger = LoggerFactory.getLogger(SocialLoginController.class);
    
    private final WeChatService weChatService;
    private final SmsService smsService;
    private final SimpleUrlAuthenticationSuccessHandler successHandler;
    
    public SocialLoginController(
            WeChatService weChatService,
            SmsService smsService) {
        this.weChatService = weChatService;
        this.smsService = smsService;
        this.successHandler = new SimpleUrlAuthenticationSuccessHandler("/");
    }
    
    /**
     * Start WeChat login process
     */
    @GetMapping("/login/wechat")
    public String startWeChatLogin(HttpServletRequest request) {
        // Generate a state parameter for CSRF protection
        String state = UUID.randomUUID().toString();
        HttpSession session = request.getSession();
        session.setAttribute("wechat_state", state);
        // Redirect to WeChat authorization page
        String authUrl = weChatService.getAuthorizationUrl(state);
        return "redirect:" + authUrl;
    }
    
    /**
     * WeChat login callback endpoint
     */
    @GetMapping("/wechat/callback")
    public void weChatCallback(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String storedState = (String) session.getAttribute("wechat_state");
        
        // Validate state for CSRF protection
        if (storedState == null || !storedState.equals(state)) {
            logger.error("Invalid state parameter in WeChat callback");
            response.sendRedirect("/login?error=invalid_state");
            return;
        }
        
        try {
            // Process the WeChat login
            Authentication authentication = weChatService.processWeChatLogin(code, state);
            
            // Handle successful authentication
            successHandler.onAuthenticationSuccess(request, response, authentication);
        } catch (AuthenticationException e) {
            logger.error("WeChat authentication failed", e);
            response.sendRedirect("/login?error=wechat_auth_failed");
        }
    }
    
    /**
     * Send SMS verification code
     */
    @PostMapping("/api/sms/send-code")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendSmsCode(@RequestBody Map<String, String> request) {
        String phoneNumber = request.get("phoneNumber");
        Map<String, Object> response = new HashMap<>();
        
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            response.put("success", false);
            response.put("message", "Phone number is required");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Generate a verification code
        String code = smsService.generateVerificationCode();
        
        // Send the verification code
        boolean sent = smsService.sendVerificationCode(phoneNumber, code);
        
        if (sent) {
            response.put("success", true);
            response.put("message", "Verification code sent");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Failed to send verification code");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Verify SMS code and login
     */
    @PostMapping("/api/sms/verify")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verifySmsCode(
            @RequestBody Map<String, String> request,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws ServletException, IOException {
        
        String phoneNumber = request.get("phoneNumber");
        String code = request.get("code");
        Map<String, Object> response = new HashMap<>();
        
        if (phoneNumber == null || phoneNumber.isEmpty() || code == null || code.isEmpty()) {
            response.put("success", false);
            response.put("message", "Phone number and verification code are required");
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            // Verify code and authenticate
            Authentication authentication = smsService.verifyCodeAndLogin(phoneNumber, code);
            
            if (authentication == null) {
                throw new AuthenticationException("Invalid verification code");
            }
            
            // Handle successful authentication
            successHandler.onAuthenticationSuccess(servletRequest, servletResponse, authentication);
            
            response.put("success", true);
            response.put("message", "Successfully authenticated");
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            logger.error("SMS verification failed", e);
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
} 