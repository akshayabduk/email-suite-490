package com.example.demo.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * PUBLIC_INTERFACE
 * DTO for returning current user information.
 */
public class MeResponse {

    @Schema(description = "Current user's id", example = "1")
    private Long id;

    @Schema(description = "Current user's email", example = "user@example.com")
    private String email;

    @Schema(description = "Role of the user", example = "USER")
    private String role;

    public MeResponse() {}

    public MeResponse(Long id, String email, String role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    // PUBLIC_INTERFACE
    public Long getId() {
        return id;
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
    public void setId(Long id) {
        this.id = id;
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
