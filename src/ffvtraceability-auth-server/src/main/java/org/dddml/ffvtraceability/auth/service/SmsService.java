package org.dddml.ffvtraceability.auth.service;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dddml.ffvtraceability.auth.config.SmsConfig;
import org.dddml.ffvtraceability.auth.dto.UserDto;
import org.dddml.ffvtraceability.auth.exception.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;

@Service
public class SmsService {
    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);
    private static final String SMS_VERIFICATION_CACHE = "smsVerificationCache";
    
    private final IAcsClient acsClient;
    private final SmsConfig smsConfig;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final UserIdentificationService userIdentificationService;
    private final CacheManager cacheManager;
    private final ObjectMapper objectMapper;

    public SmsService(IAcsClient acsClient,
                      SmsConfig smsConfig,
                      UserDetailsService userDetailsService,
                      UserService userService,
                      UserIdentificationService userIdentificationService,
                      CacheManager cacheManager,
                      ObjectMapper objectMapper) {
        this.acsClient = acsClient;
        this.smsConfig = smsConfig;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.userIdentificationService = userIdentificationService;
        this.cacheManager = cacheManager;
        this.objectMapper = objectMapper;
    }

    /**
     * Send a verification code to the phone number
     * @param phoneNumber The phone number to send the code to
     * @return true if the SMS was sent successfully
     */
    public boolean sendVerificationCode(String phoneNumber) {
        // Generate a random verification code
        String verificationCode = generateVerificationCode();
        
        try {
            // Store the verification code in cache
            Cache cache = cacheManager.getCache(SMS_VERIFICATION_CACHE);
            if (cache != null) {
                cache.put(phoneNumber, verificationCode);
            }
            
            // Send the SMS
            CommonRequest request = new CommonRequest();
            request.setSysMethod(MethodType.POST);
            request.setSysDomain("dysmsapi.aliyuncs.com");
            request.setSysVersion("2017-05-25");
            request.setSysAction("SendSms");
            request.putQueryParameter("RegionId", smsConfig.getRegionId());
            request.putQueryParameter("PhoneNumbers", phoneNumber);
            request.putQueryParameter("SignName", smsConfig.getSignName());
            request.putQueryParameter("TemplateCode", smsConfig.getTemplateCode());
            
            // Set template parameters
            Map<String, String> templateParam = new HashMap<>();
            templateParam.put("code", verificationCode);
            request.putQueryParameter("TemplateParam", objectMapper.writeValueAsString(templateParam));
            
            CommonResponse response = acsClient.getCommonResponse(request);
            logger.info("SMS service response: {}", response.getData());
            
            return true;
        } catch (ClientException | JsonProcessingException e) {
            logger.error("Failed to send SMS", e);
            return false;
        }
    }

    /**
     * Verify the SMS code and authenticate the user
     * @param phoneNumber The phone number
     * @param code The verification code
     * @return The authentication object if successful
     */
    @Transactional
    public Authentication verifyCodeAndLogin(String phoneNumber, String code) {
        // Get the stored verification code from cache
        Cache cache = cacheManager.getCache(SMS_VERIFICATION_CACHE);
        if (cache == null) {
            throw new AuthenticationException("Verification service not available");
        }
        
        String storedCode = cache.get(phoneNumber, String.class);
        if (storedCode == null || !storedCode.equals(code)) {
            throw new AuthenticationException("Invalid verification code");
        }
        
        // Clear the code from cache after successful verification
        cache.evict(phoneNumber);
        
        // Check if there is a user with this phone number
        Optional<String> existingUser = userIdentificationService.findUsernameByIdentifier("PHONE", phoneNumber);
        
        if (existingUser.isPresent()) {
            // User exists, authenticate
            return authenticateUser(existingUser.get());
        } else {
            // User doesn't exist, create a new one
            return createNewPhoneUser(phoneNumber);
        }
    }

    /**
     * Create a new user using phone number authentication
     * @param phoneNumber The verified phone number
     * @return The authentication token
     */
    private Authentication createNewPhoneUser(String phoneNumber) {
        // Generate a random username and password
        String username = "phone_" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        String password = UUID.randomUUID().toString();
        
        // Create the user
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setEnabled(true);
        userDto.setMobileNumber(phoneNumber);
        
        // Create the user in the database
        userService.createUser(userDto, password);
        
        // Link the phone number to the user
        userIdentificationService.addUserIdentification(
                username, "PHONE", phoneNumber, true);
        
        // Authenticate the user
        return authenticateUser(username);
    }

    /**
     * Authenticate user with the UserDetailsService
     * @param username The username to authenticate
     * @return Authentication token
     */
    private Authentication authenticateUser(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    /**
     * Generate a random 6-digit verification code
     * @return The generated code
     */
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6-digit code
        return String.valueOf(code);
    }
} 