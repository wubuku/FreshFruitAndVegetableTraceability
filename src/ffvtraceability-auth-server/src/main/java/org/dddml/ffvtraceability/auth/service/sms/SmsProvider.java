package org.dddml.ffvtraceability.auth.service.sms;

/**
 * Interface for SMS service providers
 */
public interface SmsProvider {
    
    /**
     * Send a verification code to a phone number
     * @param phoneNumber The phone number to send to
     * @param code The verification code to send
     * @return true if sending was successful, false otherwise
     */
    boolean sendVerificationCode(String phoneNumber, String code);
    
    /**
     * Get the provider name
     * @return The provider name (e.g., "aliyun", "huoshan", "simulator")
     */
    String getProviderName();
} 