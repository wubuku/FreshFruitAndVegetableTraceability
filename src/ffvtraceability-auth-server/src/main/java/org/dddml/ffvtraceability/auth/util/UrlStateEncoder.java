package org.dddml.ffvtraceability.auth.util;

import org.dddml.ffvtraceability.auth.config.AuthStateProperties;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Component
public class UrlStateEncoder {
    private final TextEncryptor encryptor;

    public UrlStateEncoder(AuthStateProperties properties) {
        String salt = properties.getSalt();
        if (!salt.matches("[0-9a-fA-F]{16}")) {
            throw new IllegalArgumentException(
                    "Salt must be a 16 character hex-encoded string");
        }
        this.encryptor = Encryptors.text(properties.getPassword(), salt);
    }

    public String encode(String state) {
        if (state == null) {
            return null;
        }
        String encrypted = encryptor.encrypt(state);
        return UriUtils.encode(encrypted, StandardCharsets.UTF_8);
    }

    public String decode(String encodedState) {
        if (encodedState == null) {
            return null;
        }
        String decoded = UriUtils.decode(encodedState, StandardCharsets.UTF_8);
        return encryptor.decrypt(decoded);
    }
}