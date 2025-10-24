package com.example.demo.auth;

import com.example.demo.auth.dto.AuthRequest;
import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.security.JwtService;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * PUBLIC_INTERFACE
 * Service implementing register and login flows.
 */
@Service
public class AuthService {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public AuthService(UserRepository users,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authManager,
                       JwtService jwtService,
                       UserDetailsService userDetailsService) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    // PUBLIC_INTERFACE
    /**
     * Registers a new user with role USER and returns a JWT token in response.
     */
    public AuthResponse register(AuthRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (users.existsByEmail(email)) {
            throw new BadCredentialsException("Email already in use");
        }
        String hash = passwordEncoder.encode(request.getPassword());
        User u = new User(email, hash, "USER");
        u = users.save(u);

        String token = jwtService.generateToken(
                u.getEmail(),
                Map.of("uid", u.getId(), "role", u.getRole())
        );
        return new AuthResponse(token, u.getId(), u.getEmail(), u.getRole());
    }

    // PUBLIC_INTERFACE
    /**
     * Authenticates user and returns JWT token.
     */
    public AuthResponse login(AuthRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Load persisted user to get id and role
        User u = users.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        String token = jwtService.generateToken(
                u.getEmail(),
                Map.of("uid", u.getId(), "role", u.getRole())
        );

        return new AuthResponse(token, u.getId(), u.getEmail(), u.getRole());
    }
}
