package org.dddml.ffvtraceability.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for SMS service providers
 */
@Configuration
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {
    
    private String provider = "simulator"; // Default provider
    private int codeLength = 6;
    private int codeExpirationMinutes = 5;
    private Aliyun aliyun = new Aliyun();
    private Huoshan huoshan = new Huoshan();
    
    public String getProvider() {
        return provider;
    }
    
    public void setProvider(String provider) {
        this.provider = provider;
    }
    
    public int getCodeLength() {
        return codeLength;
    }
    
    public void setCodeLength(int codeLength) {
        this.codeLength = codeLength;
    }
    
    public int getCodeExpirationMinutes() {
        return codeExpirationMinutes;
    }
    
    public void setCodeExpirationMinutes(int codeExpirationMinutes) {
        this.codeExpirationMinutes = codeExpirationMinutes;
    }
    
    public Aliyun getAliyun() {
        return aliyun;
    }
    
    public void setAliyun(Aliyun aliyun) {
        this.aliyun = aliyun;
    }
    
    public Huoshan getHuoshan() {
        return huoshan;
    }
    
    public void setHuoshan(Huoshan huoshan) {
        this.huoshan = huoshan;
    }
    
    /**
     * Aliyun SMS configuration properties
     */
    public static class Aliyun {
        private String accessKeyId;
        private String accessKeySecret;
        private String region = "cn-hangzhou";
        private String signName;
        private String templateCode;
        
        public String getAccessKeyId() {
            return accessKeyId;
        }
        
        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }
        
        public String getAccessKeySecret() {
            return accessKeySecret;
        }
        
        public void setAccessKeySecret(String accessKeySecret) {
            this.accessKeySecret = accessKeySecret;
        }
        
        public String getRegion() {
            return region;
        }
        
        public void setRegion(String region) {
            this.region = region;
        }
        
        public String getSignName() {
            return signName;
        }
        
        public void setSignName(String signName) {
            this.signName = signName;
        }
        
        public String getTemplateCode() {
            return templateCode;
        }
        
        public void setTemplateCode(String templateCode) {
            this.templateCode = templateCode;
        }
    }
    
    /**
     * Huoshan (ByteDance Volcano Engine) SMS configuration properties
     */
    public static class Huoshan {
        private String accessKeyId;
        private String secretKey;
        private String endpoint = "https://sms.volcengineapi.com";
        private String signName;
        private String templateId;
        private String smsAccount;

        public String getAccessKeyId() {
            return accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getSignName() {
            return signName;
        }

        public void setSignName(String signName) {
            this.signName = signName;
        }

        public String getTemplateId() {
            return templateId;
        }

        public void setTemplateId(String templateId) {
            this.templateId = templateId;
        }

        public String getSmsAccount() {
            return smsAccount;
        }

        public void setSmsAccount(String smsAccount) {
            this.smsAccount = smsAccount;
        }
    }
} 