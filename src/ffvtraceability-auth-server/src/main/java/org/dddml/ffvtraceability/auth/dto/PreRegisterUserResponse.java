package org.dddml.ffvtraceability.auth.dto;

import java.time.OffsetDateTime;

public class PreRegisterUserResponse {
    private String username;
    private String oneTimePassword;
    private OffsetDateTime tempPasswordLastGenerated;

    public PreRegisterUserResponse(String username, String oneTimePassword, OffsetDateTime tempPasswordLastGenerated) {
        this.username = username;
        this.oneTimePassword = oneTimePassword;
        this.tempPasswordLastGenerated = tempPasswordLastGenerated;
    }

    public OffsetDateTime getTempPasswordLastGenerated() {
        return tempPasswordLastGenerated;
    }

    public String getUsername() {
        return username;
    }

    public String getOneTimePassword() {
        return oneTimePassword;
    }
}