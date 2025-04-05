package org.dddml.ffvtraceability.fileservice.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "storage.type", havingValue = "cos")
public class CosConfig {

    private final CosConfigProperties cosConfigProperties;

    public CosConfig(CosConfigProperties cosConfigProperties) {
        this.cosConfigProperties = cosConfigProperties;
    }

    @Bean
    public COSCredentials cosCredentials() {
        return new BasicCOSCredentials(cosConfigProperties.getSecretId(), cosConfigProperties.getSecretKey());
    }

    @Bean
    public ClientConfig cosClientConfig() {
        return new ClientConfig(new Region(cosConfigProperties.getRegion()));
    }

    @Bean
    public COSClient cosClient(COSCredentials cosCredentials, ClientConfig clientConfig) {
        return new COSClient(cosCredentials, clientConfig);
    }
} 