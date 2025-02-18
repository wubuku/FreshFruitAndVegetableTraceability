package org.dddml.ffvtraceability.domain.attributesetinstance;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dddml.ffvtraceability.domain.attributesetinstance.AttributeSetInstanceCommand.CreateAttributeSetInstance;
import org.dddml.ffvtraceability.specialization.IdGenerator;
import org.erdtman.jcs.JsonCanonicalizer;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class AttributeSetInstanceIdGenerator implements
        IdGenerator<String, AttributeSetInstanceCommand.CreateAttributeSetInstance, AttributeSetInstanceState> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean isArbitraryIdEnabled() {
        return true;
    }

    @Override
    public String generateId(CreateAttributeSetInstance command) {
        String json;
        try {
            json = objectMapper.writeValueAsString(command.getAttributes());
            JsonCanonicalizer jc = new JsonCanonicalizer(json);
            String encoded = jc.getEncodedString();
            // MessageDigest md = MessageDigest.getInstance("SHA-1");
            // byte[] hashBytes = md.digest(encoded.getBytes(StandardCharsets.UTF_8));
            // return bytesToHex(hashBytes);
            return DigestUtils.md5DigestAsHex(encoded.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            return getNextId();
        }
    }

    @Override
    public String getNextId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public boolean equals(CreateAttributeSetInstance command, AttributeSetInstanceState state) {
        return command.getAttributes().equals(state.getAttributes());
    }

    // private String bytesToHex(byte[] bytes) {
    //     StringBuilder sb = new StringBuilder();
    //     for (byte b : bytes) {
    //         sb.append(String.format("%02x", b));
    //     }
    //     return sb.toString();
    // }
}
