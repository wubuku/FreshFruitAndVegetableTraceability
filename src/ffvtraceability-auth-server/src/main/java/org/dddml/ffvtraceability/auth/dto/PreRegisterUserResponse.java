package org.dddml.ffvtraceability.auth.dto;

public class PreRegisterUserResponse {
    private String username;
    private String oneTimePassword;

    public PreRegisterUserResponse(String username, String oneTimePassword) {
        this.username = username;
        this.oneTimePassword = oneTimePassword;
    }

    public String getUsername() {
        return username;
    }

    public String getOneTimePassword() {
        return oneTimePassword;
    }
}