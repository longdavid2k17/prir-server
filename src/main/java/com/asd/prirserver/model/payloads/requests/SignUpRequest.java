package com.asd.prirserver.model.payloads.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * <p>Klasa opakowująca dane potrzebne do rejestracji użytkownika w systemie.</p>
 * @author Dawid Kańtoch
 * @version 1.0
 * @since 1.0
 */
public class SignUpRequest
{
    @NotBlank
    @Size(min = 3,max = 50)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6,max = 50)
    private String password;

    private Set<String> role;

    private Long avatar;

    public SignUpRequest() {
    }

    public Long getAvatar() {
        return avatar;
    }

    public void setAvatar(Long avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }
}
