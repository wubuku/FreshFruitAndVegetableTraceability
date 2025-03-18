package org.dddml.ffvtraceability.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "password.token")
public class PasswordTokenProperties {

    private Long expireInMinutes = 5L;
    private String createPasswordUrl;

    public Long getExpireInMinutes() {
        return expireInMinutes;
    }

    public void setExpireInMinutes(Long expireInMinutes) {
        this.expireInMinutes = expireInMinutes;
    }

    public String getCreatePasswordUrl() {
        return createPasswordUrl;
    }

    public void setCreatePasswordUrl(String createPasswordUrl) {
        this.createPasswordUrl = createPasswordUrl;
    }
}