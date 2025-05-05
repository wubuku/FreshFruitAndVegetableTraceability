package org.dddml.ffvtraceability.auth.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dddml.ffvtraceability.auth.service.sms.AliyunSmsProvider;
import org.dddml.ffvtraceability.auth.service.sms.HuoshanSmsProvider;
import org.dddml.ffvtraceability.auth.service.sms.SimulatorSmsProvider;
import org.dddml.ffvtraceability.auth.service.sms.SmsProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration for SMS providers
 */
@Configuration
public class SmsProviderConfig {

    @Bean
    public IAcsClient acsClient(SmsProperties smsProperties) {
        SmsProperties.Aliyun aliyunConfig = smsProperties.getAliyun();
        return new DefaultAcsClient(
                DefaultProfile.getProfile(
                        aliyunConfig.getRegion(),
                        aliyunConfig.getAccessKeyId(),
                        aliyunConfig.getAccessKeySecret()
                )
        );
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    
    @Bean
    public AliyunSmsProvider aliyunSmsProvider(IAcsClient acsClient, SmsProperties smsProperties) {
        return new AliyunSmsProvider(acsClient, smsProperties.getAliyun());
    }
    
    @Bean
    public HuoshanSmsProvider huoshanSmsProvider(SmsProperties smsProperties, RestTemplate restTemplate) {
        return new HuoshanSmsProvider(smsProperties.getHuoshan(), restTemplate);
    }
    
    @Bean
    public SimulatorSmsProvider simulatorSmsProvider() {
        return new SimulatorSmsProvider();
    }
    
    @Bean
    @Primary
    public SmsProvider smsProvider(
            SmsProperties smsProperties,
            AliyunSmsProvider aliyunSmsProvider,
            HuoshanSmsProvider huoshanSmsProvider,
            SimulatorSmsProvider simulatorSmsProvider) {
        
        String provider = smsProperties.getProvider();
        
        switch (provider.toLowerCase()) {
            case "aliyun":
                return aliyunSmsProvider;
            case "huoshan":
                return huoshanSmsProvider;
            case "simulator":
            default:
                return simulatorSmsProvider;
        }
    }
} 