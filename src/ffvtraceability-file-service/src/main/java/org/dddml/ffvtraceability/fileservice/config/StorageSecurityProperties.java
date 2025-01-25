package org.dddml.ffvtraceability.fileservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "storage.security")
public class StorageSecurityProperties {
    private boolean allowAnonymousUpload = false;

    public boolean isAllowAnonymousUpload() {
        return allowAnonymousUpload;
    }

    public void setAllowAnonymousUpload(boolean allowAnonymousUpload) {
        this.allowAnonymousUpload = allowAnonymousUpload;
    }
}