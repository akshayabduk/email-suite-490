package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * PUBLIC_INTERFACE
 * Service for generating and validating JWT tokens using HS256.
 */
@Service
public class JwtService {

    private final Key signingKey;
    private final long expirationSeconds;

    public JwtService(
            @Value("${security.jwt.secret:change-this-secret-in-env}") String secret,
            @Value("${security.jwt.expiration-seconds:86400}") long expirationSeconds
    ) {
        // Ensure secret yields a strong key
        if (secret == null || secret.length() < 32) {
            // 32+ bytes recommended for HS256
            secret = String.format("%-32s", secret == null ? "" : secret).replace(' ', 'x');
        }
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationSeconds = expirationSeconds;
    }

    // PUBLIC_INTERFACE
    /**
     * Generates a JWT with subject and custom claims.
     */
    public String generateToken(String subject, Map<String, Object> claims) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(expirationSeconds)))
                .addClaims(claims)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // PUBLIC_INTERFACE
    /**
     * Parses a JWT, returning the claims if valid.
     */
    public Jws<Claims> parseToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token);
    }

    // PUBLIC_INTERFACE
    /**
     * Validates a token and returns the subject (email) if valid.
     */
    public String validateAndGetSubject(String token) {
        return parseToken(token).getBody().getSubject();
    }
}
