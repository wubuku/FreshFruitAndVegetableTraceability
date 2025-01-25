package org.dddml.ffvtraceability.fileservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.dddml.ffvtraceability.fileservice.exception.StorageException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@ConfigurationProperties(prefix = "storage.gcs")
@ConditionalOnProperty(name = "storage.type", havingValue = "gcs")
public class GcsConfig {
    private String projectId;
    private String credentialsPath;
    private String bucket;

    private final ResourceLoader resourceLoader;

    public GcsConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    // Getters and Setters
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCredentialsPath() {
        return credentialsPath;
    }

    public void setCredentialsPath(String credentialsPath) {
        this.credentialsPath = credentialsPath;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    @Bean
    public Storage storage() {
        try {
            InputStream credentialsStream = resourceLoader.getResource(credentialsPath)
                    .getInputStream();
            
            GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
            
            return StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .setCredentials(credentials)
                    .build()
                    .getService();
        } catch (IOException e) {
            throw new StorageException("Could not initialize GCS", e);
        }
    }
} 