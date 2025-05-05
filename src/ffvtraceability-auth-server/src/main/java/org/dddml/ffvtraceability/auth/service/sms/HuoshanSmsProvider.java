package org.dddml.ffvtraceability.auth.service.sms;

import org.dddml.ffvtraceability.auth.config.SmsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of SmsProvider for Huoshan (ByteDance Volcano Engine) SMS service
 */
public class HuoshanSmsProvider implements SmsProvider {
    private static final Logger logger = LoggerFactory.getLogger(HuoshanSmsProvider.class);
    
    private final SmsProperties.Huoshan config;
    private final RestTemplate restTemplate;
    
    private static final String ACTION = "SendSms";
    private static final String VERSION = "2020-12-25";
    private static final String SERVICE = "volcSMS";
    
    public HuoshanSmsProvider(SmsProperties.Huoshan config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }
    
    @Override
    public boolean sendVerificationCode(String phoneNumber, String code) {
        try {
            // Generate timestamp in UTC format
            String timestamp = ZonedDateTime.now(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
            
            // Create request body
            Map<String, Object> bodyParams = new HashMap<>();
            bodyParams.put("SmsAccount", config.getSmsAccount());
            bodyParams.put("Sign", config.getSignName());
            bodyParams.put("TemplateID", config.getTemplateId());
            bodyParams.put("TemplateParamSet", new String[]{code});
            bodyParams.put("PhoneNumberSet", new String[]{phoneNumber});
            
            // Set up headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", generateAuthorization(bodyParams, ACTION, timestamp));
            headers.set("X-Date", timestamp);
            headers.set("X-Service", SERVICE);
            headers.set("X-Content-SHA256", "");
            
            // Send the request
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(bodyParams, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    config.getEndpoint() + "/", request, Map.class);
            
            boolean success = response.getStatusCode().is2xxSuccessful();
            
            if (success) {
                logger.info("Successfully sent SMS via Huoshan to {}, response: {}", phoneNumber, response.getBody());
            } else {
                logger.error("Failed to send SMS via Huoshan to {}, response: {}", phoneNumber, response.getBody());
            }
            
            return success;
        } catch (Exception e) {
            logger.error("Error sending SMS via Huoshan", e);
            return false;
        }
    }
    
    private String generateAuthorization(Map<String, Object> bodyParams, String action, String timestamp) {
        try {
            // Format: HMAC-SHA256 Credential=AKXXXXXXXXXXXXXXX/20221122/cn-north-4/sms/request, SignedHeaders=content-type;host;x-date;x-service, Signature=xxxxxx
            String canonicalRequest = "POST\n/" + 
                    "\n" + 
                    "action=" + action + "&" +
                    "version=" + VERSION + "\n" +
                    "content-type:application/json\n" +
                    "host:" + config.getEndpoint().replace("https://", "") + "\n" +
                    "x-date:" + timestamp + "\n" +
                    "x-service:" + SERVICE + "\n" +
                    "\n" +
                    "content-type;host;x-date;x-service";
            
            // Create signing key
            String signingKey = config.getSecretKey();
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(signingKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            
            // Calculate signature
            byte[] signatureBytes = mac.doFinal(canonicalRequest.getBytes(StandardCharsets.UTF_8));
            String signature = Base64.getEncoder().encodeToString(signatureBytes);
            
            // Generate authorization header
            return "HMAC-SHA256 Credential=" + config.getAccessKeyId() + "/" + 
                   timestamp.substring(0, 8) + "/" + 
                   config.getEndpoint().replace("https://", "").split("\\.")[0] + "/" + 
                   SERVICE + "/request, " +
                   "SignedHeaders=content-type;host;x-date;x-service, " +
                   "Signature=" + signature;
        } catch (Exception e) {
            logger.error("Error generating Huoshan authorization header", e);
            return "";
        }
    }
    
    @Override
    public String getProviderName() {
        return "huoshan";
    }
} 