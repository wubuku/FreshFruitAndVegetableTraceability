package org.dddml.ffvtraceability.auth.service.sms;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dddml.ffvtraceability.auth.config.SmsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of SmsProvider for Aliyun SMS service
 */
public class AliyunSmsProvider implements SmsProvider {
    private static final Logger logger = LoggerFactory.getLogger(AliyunSmsProvider.class);
    
    private final IAcsClient client;
    private final SmsProperties.Aliyun config;
    private final ObjectMapper objectMapper;
    
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";
    private static final String ACTION = "SendSms";
    private static final String VERSION = "2017-05-25";
    
    public AliyunSmsProvider(IAcsClient client, SmsProperties.Aliyun config) {
        this.client = client;
        this.config = config;
        this.objectMapper = new ObjectMapper();
    }
    
    @Override
    public boolean sendVerificationCode(String phoneNumber, String code) {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(DOMAIN);
        request.setSysVersion(VERSION);
        request.setSysAction(ACTION);
        
        // Set SMS parameters
        Map<String, String> templateParam = new HashMap<>();
        templateParam.put("code", code);
        
        try {
            String templateParamJson = objectMapper.writeValueAsString(templateParam);
            
            request.putQueryParameter("RegionId", config.getRegion());
            request.putQueryParameter("PhoneNumbers", phoneNumber);
            request.putQueryParameter("SignName", config.getSignName());
            request.putQueryParameter("TemplateCode", config.getTemplateCode());
            request.putQueryParameter("TemplateParam", templateParamJson);
            
            CommonResponse response = client.getCommonResponse(request);
            String responseData = response.getData();
            
            // Parse response
            JsonNode root = objectMapper.readTree(responseData);
            String code1 = root.path("Code").asText();
            
            boolean success = "OK".equalsIgnoreCase(code1);
            
            if (success) {
                logger.info("Successfully sent SMS via Aliyun to {}, response: {}", phoneNumber, responseData);
            } else {
                logger.error("Failed to send SMS via Aliyun to {}, response: {}", phoneNumber, responseData);
            }
            
            return success;
        } catch (ClientException e) {
            logger.error("Aliyun SMS client exception", e);
            return false;
        } catch (Exception e) {
            logger.error("Error sending SMS via Aliyun", e);
            return false;
        }
    }
    
    @Override
    public String getProviderName() {
        return "aliyun";
    }
} 