package com.example.demo.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * PUBLIC_INTERFACE
 * DTO used for login/register requests.
 */
public class AuthRequest {

    @Schema(description = "User email address", example = "user@example.com")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "User password (plain text)", example = "StrongP@ssw0rd")
    @NotBlank
    @Size(min = 8, max = 200)
    private String password;

    public AuthRequest() {}

    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // PUBLIC_INTERFACE
    public String getEmail() {
        return email;
    }

    // PUBLIC_INTERFACE
    public String getPassword() {
        return password;
    }

    // PUBLIC_INTERFACE
    public void setEmail(String email) {
        this.email = email;
    }

    // PUBLIC_INTERFACE
    public void setPassword(String password) {
        this.password = password;
    }
}
