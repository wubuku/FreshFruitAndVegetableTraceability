package org.dddml.ffvtraceability.auth.service;

/**
 * Interface for SMS services
 * Different implementations can be provided for different SMS providers
 */
public interface SmsService {
    
    /**
     * Send a verification code to the phone number
     * @param phoneNumber The phone number to send the code to
     * @param code The verification code to send
     * @return true if the SMS was sent successfully
     */
    boolean sendVerificationCode(String phoneNumber, String code);
    
    /**
     * Generate a verification code
     * @return The generated verification code
     */
    String generateVerificationCode();
    
    /**
     * Verify a code for a phone number
     * @param phoneNumber The phone number
     * @param code The verification code
     * @return The authentication if successful, null otherwise
     */
    org.springframework.security.core.Authentication verifyCodeAndLogin(String phoneNumber, String code);
} 