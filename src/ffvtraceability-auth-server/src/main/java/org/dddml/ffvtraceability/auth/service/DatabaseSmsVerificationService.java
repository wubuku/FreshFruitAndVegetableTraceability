package org.dddml.ffvtraceability.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Database implementation of SMS verification service
 */
@Service
public class DatabaseSmsVerificationService implements SmsVerificationService {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseSmsVerificationService.class);
    
    private final JdbcTemplate jdbcTemplate;
    
    @Value("${sms.code-expiration-minutes:5}")
    private int defaultExpirationMinutes;
    
    @Autowired
    public DatabaseSmsVerificationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    @Transactional
    public void saveVerificationCode(String phoneNumber, String code, int expirationMinutes) {
        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(expirationMinutes);
        
        jdbcTemplate.update(
            "INSERT INTO sms_verification_codes (phone_number, code, expire_time) VALUES (?, ?, ?)",
            phoneNumber, code, Timestamp.valueOf(expireTime)
        );
        
        logger.debug("Saved verification code for phone number: {}, expires at: {}", phoneNumber, expireTime);
    }
    
    @Override
    @Transactional
    public boolean verifyCode(String phoneNumber, String code) {
        try {
            return jdbcTemplate.execute((Connection conn) -> {
                try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT id FROM sms_verification_codes " +
                    "WHERE phone_number = ? AND code = ? AND expire_time > ? AND used = FALSE " +
                    "ORDER BY created_at DESC LIMIT 1 FOR UPDATE"
                )) {
                    ps.setString(1, phoneNumber);
                    ps.setString(2, code);
                    ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                    
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            long id = rs.getLong("id");
                            
                            // Mark the code as used
                            try (PreparedStatement updatePs = conn.prepareStatement(
                                "UPDATE sms_verification_codes SET used = TRUE WHERE id = ?"
                            )) {
                                updatePs.setLong(1, id);
                                updatePs.executeUpdate();
                                
                                logger.debug("Successfully verified SMS code for phone number: {}", phoneNumber);
                                return true;
                            }
                        }
                        
                        logger.debug("Failed to verify SMS code for phone number: {}", phoneNumber);
                        return false;
                    }
                }
            });
        } catch (Exception e) {
            logger.error("Error verifying SMS code", e);
            return false;
        }
    }
    
    @Override
    public boolean checkRateLimit(String phoneNumber) {
        // Check last minute limit (1 SMS per minute)
        int lastMinuteCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM sms_send_records WHERE phone_number = ? AND send_time > ?",
            Integer.class,
            phoneNumber,
            Timestamp.valueOf(LocalDateTime.now().minusMinutes(1))
        );
        
        if (lastMinuteCount >= 1) {
            logger.warn("Rate limit exceeded for phone number: {} (1 per minute)", phoneNumber);
            return false;
        }
        
        // Check hourly limit (5 SMS per hour)
        int hourlyCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM sms_send_records WHERE phone_number = ? AND send_time > ?",
            Integer.class,
            phoneNumber,
            Timestamp.valueOf(LocalDateTime.now().minusHours(1))
        );
        
        if (hourlyCount >= 5) {
            logger.warn("Rate limit exceeded for phone number: {} (5 per hour)", phoneNumber);
            return false;
        }
        
        // Check daily limit (10 SMS per day)
        int dailyCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM sms_send_records WHERE phone_number = ? AND send_time > ?",
            Integer.class,
            phoneNumber,
            Timestamp.valueOf(LocalDateTime.now().minusDays(1))
        );
        
        if (dailyCount >= 10) {
            logger.warn("Rate limit exceeded for phone number: {} (10 per day)", phoneNumber);
            return false;
        }
        
        return true;
    }
    
    @Override
    @Transactional
    public void recordSendAttempt(String phoneNumber, String provider, boolean success, String message) {
        jdbcTemplate.update(
            "INSERT INTO sms_send_records (phone_number, provider, success, message) VALUES (?, ?, ?, ?)",
            phoneNumber, provider, success, message
        );
        
        logger.debug("Recorded SMS send attempt for phone number: {}, provider: {}, success: {}", 
                   phoneNumber, provider, success);
    }
} 