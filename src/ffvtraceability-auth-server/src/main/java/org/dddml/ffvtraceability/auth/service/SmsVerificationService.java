package org.dddml.ffvtraceability.auth.service;

/**
 * Interface for SMS verification service
 * This interface defines methods for managing SMS verification codes
 */
public interface SmsVerificationService {
    
    /**
     * Save a verification code for a phone number
     * @param phoneNumber The phone number
     * @param code The verification code
     * @param expirationMinutes The expiration time in minutes
     */
    void saveVerificationCode(String phoneNumber, String code, int expirationMinutes);
    
    /**
     * Verify a code for a phone number
     * @param phoneNumber The phone number
     * @param code The verification code
     * @return true if the code is valid, false otherwise
     */
    boolean verifyCode(String phoneNumber, String code);
    
    /**
     * Check rate limits for sending SMS to a phone number
     * @param phoneNumber The phone number
     * @return true if within rate limits, false if rate limit exceeded
     */
    boolean checkRateLimit(String phoneNumber);
    
    /**
     * Record a SMS sending attempt
     * @param phoneNumber The phone number
     * @param provider The SMS provider used
     * @param success Whether the sending was successful
     * @param message Optional message (error message or response code)
     */
    void recordSendAttempt(String phoneNumber, String provider, boolean success, String message);
} 