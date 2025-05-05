package org.dddml.ffvtraceability.auth.service.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simulator implementation of SmsProvider that logs messages without actually sending SMS
 * Useful for development and testing environments
 */
public class SimulatorSmsProvider implements SmsProvider {
    private static final Logger logger = LoggerFactory.getLogger(SimulatorSmsProvider.class);
    
    @Override
    public boolean sendVerificationCode(String phoneNumber, String code) {
        logger.info("SIMULATOR: Would send verification code {} to phone number {}", code, phoneNumber);
        return true;
    }
    
    @Override
    public String getProviderName() {
        return "simulator";
    }
} 