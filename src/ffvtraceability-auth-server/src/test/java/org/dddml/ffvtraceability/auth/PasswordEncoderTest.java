package org.dddml.ffvtraceability.auth;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTest {
    
    @Test
    public void testPasswordEncoding() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String rawPassword = "admin";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Raw password: " + rawPassword);
        System.out.println("Encoded password: " + encodedPassword);
        
        // 验证现有密码
        String existingEncodedPassword = "{bcrypt}$2a$10$eKBDBSf4DBNzRwbF7fx5IetdKKjqzkYoST0F7Dkro84eRiDTBJYky";
        boolean matches = encoder.matches(rawPassword, existingEncodedPassword);
        System.out.println("Existing password matches: " + matches);
    }
}