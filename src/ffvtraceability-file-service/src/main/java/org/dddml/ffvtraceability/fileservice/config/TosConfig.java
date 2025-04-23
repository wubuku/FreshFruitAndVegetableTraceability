package org.dddml.ffvtraceability.fileservice.config;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "storage.type", havingValue = "tos")
public class TosConfig {
    
    private final TosConfigProperties tosConfigProperties;

    public TosConfig(TosConfigProperties tosConfigProperties) {
        this.tosConfigProperties = tosConfigProperties;
    }

    @Bean
    public TOSV2 tosClient() {
        return new TOSV2ClientBuilder().build(
                tosConfigProperties.getRegion(),
                tosConfigProperties.getEndpoint(),
                tosConfigProperties.getAccessKey(),
                tosConfigProperties.getSecretKey()
        );
    }
} 