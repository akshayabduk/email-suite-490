package com.example.demo.auth;

import com.example.demo.auth.dto.AuthRequest;
import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.auth.dto.MeResponse;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * PUBLIC_INTERFACE
 * REST controller exposing authentication endpoints.
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "User registration, login, and current user info")
public class AuthController {

    private final AuthService authService;
    private final UserRepository users;

    public AuthController(AuthService authService, UserRepository users) {
        this.authService = authService;
        this.users = users;
    }

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user with USER role, storing a BCrypt-hashed password and returns a JWT token."
    )
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest request) {
        AuthResponse res = authService.register(request);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = "Authenticates a user and returns a JWT token on success."
    )
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse res = authService.login(request);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/me")
    @Operation(
            summary = "Current user",
            description = "Returns information about the currently authenticated user. Requires Authorization: Bearer <token>."
    )
    public ResponseEntity<MeResponse> me(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }
        String email = authentication.getName();
        User u = users.findByEmail(email).orElse(null);
        if (u == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(new MeResponse(u.getId(), u.getEmail(), u.getRole()));
    }
}
