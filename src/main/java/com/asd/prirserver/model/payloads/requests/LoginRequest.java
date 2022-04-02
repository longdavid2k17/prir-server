package com.asd.prirserver.model.payloads.requests;

import javax.validation.constraints.NotBlank;

/**
 * Klasa opakowująca dane potrzebne do wykonania procedury logowania użytkownika. Posiada pola username i password
 * @author Dawid Kańtoch
 * @version 1.0
 * @since 1.0
 */
public class LoginRequest
{
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public LoginRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
