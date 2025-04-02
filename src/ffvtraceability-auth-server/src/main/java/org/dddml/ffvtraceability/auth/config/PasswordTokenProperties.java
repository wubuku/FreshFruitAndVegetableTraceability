package org.dddml.ffvtraceability.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "password.token")
public class PasswordTokenProperties {

    private Integer expireInHours = 24;
    private String createPasswordUrl;
    private String resetPasswordUrl;

    public Integer getExpireInHours() {
        return expireInHours;
    }

    public void setExpireInHours(Integer expireInHours) {
        this.expireInHours = expireInHours;
    }

    public String getCreatePasswordUrl() {
        return createPasswordUrl;
    }

    public void setCreatePasswordUrl(String createPasswordUrl) {
        this.createPasswordUrl = createPasswordUrl;
    }

    public String getResetPasswordUrl() {
        return resetPasswordUrl;
    }

    public void setResetPasswordUrl(String resetPasswordUrl) {
        this.resetPasswordUrl = resetPasswordUrl;
    }
}