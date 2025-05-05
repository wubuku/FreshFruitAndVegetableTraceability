package org.dddml.ffvtraceability.auth.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Authentication provider for SMS login
 */
@Component
public class SmsLoginAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }

        SmsLoginAuthenticationToken token = (SmsLoginAuthenticationToken) authentication;
        String phoneNumber = token.getPhoneNumber();
        
        // At this point, verification should have already taken place
        // This method is primarily used by Spring Security's authentication mechanism
        if (token.isAuthenticated()) {
            return token;
        }
        
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsLoginAuthenticationToken.class.isAssignableFrom(authentication);
    }
    
    /**
     * Create an authenticated token for the phone number
     * This is used after verification of SMS code
     * 
     * @param phoneNumber The verified phone number
     * @return An authenticated token
     */
    public Authentication createAuthenticatedToken(String phoneNumber) {
        return new SmsLoginAuthenticationToken(
            phoneNumber,
            null, // No credentials needed since already verified
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
            true  // Set as authenticated
        );
    }
    
    /**
     * Custom authentication token for SMS login
     */
    public static class SmsLoginAuthenticationToken extends AbstractAuthenticationToken {
        private final String phoneNumber;
        private final Object credentials;

        /**
         * Constructor for unauthenticated token
         */
        public SmsLoginAuthenticationToken(String phoneNumber, Object credentials) {
            super(null);
            this.phoneNumber = phoneNumber;
            this.credentials = credentials;
            setAuthenticated(false);
        }

        /**
         * Constructor for authenticated token
         */
        public SmsLoginAuthenticationToken(
                String phoneNumber,
                Object credentials,
                java.util.Collection<SimpleGrantedAuthority> authorities,
                boolean authenticated) {
            super(authorities);
            this.phoneNumber = phoneNumber;
            this.credentials = credentials;
            setAuthenticated(authenticated);
        }

        @Override
        public Object getCredentials() {
            return credentials;
        }

        @Override
        public Object getPrincipal() {
            return phoneNumber;
        }
        
        /**
         * Get the phone number
         */
        public String getPhoneNumber() {
            return phoneNumber;
        }
    }
} 