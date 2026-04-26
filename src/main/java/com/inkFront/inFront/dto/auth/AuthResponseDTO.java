package com.inkFront.inFront.dto.auth;

public class AuthResponseDTO {

    private boolean success;
    private String message;
    private AuthUserDTO user;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(boolean success, String message, AuthUserDTO user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    public static AuthResponseDTO success(String message, AuthUserDTO user) {
        return new AuthResponseDTO(true, message, user);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AuthUserDTO getUser() {
        return user;
    }

    public void setUser(AuthUserDTO user) {
        this.user = user;
    }
}