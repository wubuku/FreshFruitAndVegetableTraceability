package org.dddml.ffvtraceability.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.state.encryption")
public class AuthStateProperties {
    private String password = "defaultpassword";
    private String salt = "5c0744940b5c369b";

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}