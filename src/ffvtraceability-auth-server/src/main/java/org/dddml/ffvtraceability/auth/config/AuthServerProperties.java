package org.dddml.ffvtraceability.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "auth-server")
public class AuthServerProperties {
    private String issuer;
    private TokenConfig token;
    private CorsConfig cors;

    // Getters and setters
    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public TokenConfig getToken() {
        return token;
    }

    public void setToken(TokenConfig token) {
        this.token = token;
    }

    public CorsConfig getCors() {
        return cors;
    }

    public void setCors(CorsConfig cors) {
        this.cors = cors;
    }

    public static class TokenConfig {
        private long accessTokenTtl;
        private long refreshTokenTtl;
        private long authorizationCodeTtl;

        // Getters and setters
        public long getAccessTokenTtl() {
            return accessTokenTtl;
        }

        public void setAccessTokenTtl(long accessTokenTtl) {
            this.accessTokenTtl = accessTokenTtl;
        }

        public long getRefreshTokenTtl() {
            return refreshTokenTtl;
        }

        public void setRefreshTokenTtl(long refreshTokenTtl) {
            this.refreshTokenTtl = refreshTokenTtl;
        }

        public long getAuthorizationCodeTtl() {
            return authorizationCodeTtl;
        }

        public void setAuthorizationCodeTtl(long authorizationCodeTtl) {
            this.authorizationCodeTtl = authorizationCodeTtl;
        }
    }

    public static class CorsConfig {
        private String allowedOrigins;
        private String allowedMethods;
        private String allowedHeaders;
        private boolean allowCredentials;

        // Getters and setters
        public String getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(String allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        public String getAllowedMethods() {
            return allowedMethods;
        }

        public void setAllowedMethods(String allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        public String getAllowedHeaders() {
            return allowedHeaders;
        }

        public void setAllowedHeaders(String allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }

        public boolean isAllowCredentials() {
            return allowCredentials;
        }

        public void setAllowCredentials(boolean allowCredentials) {
            this.allowCredentials = allowCredentials;
        }
    }
}