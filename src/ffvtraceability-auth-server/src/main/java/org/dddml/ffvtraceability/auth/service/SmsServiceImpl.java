package org.dddml.ffvtraceability.auth.service;

import org.dddml.ffvtraceability.auth.authentication.SmsLoginAuthenticationProvider;
import org.dddml.ffvtraceability.auth.service.sms.SmsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Default implementation of the SMS service
 */
@Service
public class SmsServiceImpl implements SmsService {
    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);
    
    private final SmsVerificationService smsVerificationService;
    private final SmsLoginAuthenticationProvider smsLoginAuthenticationProvider;
    private final SmsProvider smsProvider;
    
    @Value("${sms.code-length:6}")
    private int codeLength;
    
    @Value("${sms.code-expiration-minutes:5}")
    private int expirationMinutes;
    
    @Autowired
    public SmsServiceImpl(SmsVerificationService smsVerificationService, 
                     SmsLoginAuthenticationProvider smsLoginAuthenticationProvider,
                     SmsProvider smsProvider) {
        this.smsVerificationService = smsVerificationService;
        this.smsLoginAuthenticationProvider = smsLoginAuthenticationProvider;
        this.smsProvider = smsProvider;
    }
    
    @Override
    public boolean sendVerificationCode(String phoneNumber, String code) {
        // Check rate limiting
        if (!smsVerificationService.checkRateLimit(phoneNumber)) {
            logger.warn("Rate limit exceeded for phone number: {}", phoneNumber);
            return false;
        }
        
        try {
            // Save the code to the database
            smsVerificationService.saveVerificationCode(phoneNumber, code, expirationMinutes);
            
            // Send the SMS using the configured provider
            logger.info("Sending SMS verification code to phone number: {}", phoneNumber);
            boolean success = smsProvider.sendVerificationCode(phoneNumber, code);
            
            // Record the send attempt
            smsVerificationService.recordSendAttempt(phoneNumber, smsProvider.getProviderName(), success, 
                    success ? "SMS sent successfully" : "Failed to send SMS");
            
            return success;
        } catch (Exception e) {
            logger.error("Error sending SMS verification code", e);
            smsVerificationService.recordSendAttempt(phoneNumber, smsProvider.getProviderName(), false, e.getMessage());
            return false;
        }
    }
    
    @Override
    public String generateVerificationCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(codeLength);
        for (int i = 0; i < codeLength; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    
    @Override
    public Authentication verifyCodeAndLogin(String phoneNumber, String code) {
        boolean verified = smsVerificationService.verifyCode(phoneNumber, code);
        
        if (!verified) {
            logger.warn("Failed to verify SMS code for phone number: {}", phoneNumber);
            return null;
        }
        
        // Use the authentication provider to create an authenticated token
        return smsLoginAuthenticationProvider.createAuthenticatedToken(phoneNumber);
    }
} 