package org.dddml.ffvtraceability.fileservice.config;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.dddml.ffvtraceability.fileservice.exception.StorageException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "storage.gcs")
@ConditionalOnProperty(name = "storage.type", havingValue = "gcs")
public class GcsConfig {
    private String projectId;
    private String privateBucket;
    private String publicBucket;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPrivateBucket() {
        return privateBucket;
    }

    public void setPrivateBucket(String privateBucket) {
        this.privateBucket = privateBucket;
    }

    public String getPublicBucket() {
        return publicBucket;
    }

    public void setPublicBucket(String publicBucket) {
        this.publicBucket = publicBucket;
    }

    @Bean
    public Storage storage() {
        try {
            return StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .build()
                    .getService();
        } catch (Exception e) {
            throw new StorageException("Could not initialize GCS", e);
        }
    }
} 