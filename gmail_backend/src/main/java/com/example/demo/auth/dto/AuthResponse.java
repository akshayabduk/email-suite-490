package com.example.demo.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * PUBLIC_INTERFACE
 * DTO used for authentication responses carrying a JWT token and basic user info.
 */
public class AuthResponse {

    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Authenticated user's id", example = "1")
    private Long userId;

    @Schema(description = "Authenticated user's email", example = "user@example.com")
    private String email;

    @Schema(description = "Role of the user", example = "USER")
    private String role;

    public AuthResponse() {}

    public AuthResponse(String token, Long userId, String email, String role) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    // PUBLIC_INTERFACE
    public String getToken() {
        return token;
    }

    // PUBLIC_INTERFACE
    public Long getUserId() {
        return userId;
    }

    // PUBLIC_INTERFACE
    public String getEmail() {
        return email;
    }

    // PUBLIC_INTERFACE
    public String getRole() {
        return role;
    }

    // PUBLIC_INTERFACE
    public void setToken(String token) {
        this.token = token;
    }

    // PUBLIC_INTERFACE
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // PUBLIC_INTERFACE
    public void setEmail(String email) {
        this.email = email;
    }

    // PUBLIC_INTERFACE
    public void setRole(String role) {
        this.role = role;
    }
}
