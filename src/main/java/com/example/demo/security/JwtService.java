package com.example.demo.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;

import com.example.demo.entity.Role;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        if (secret == null || secret.trim().length() <= 32) {
            throw new IllegalStateException(
                "JWT_SECRET is missing or too short. Must be at least 32 characters."
            );
        }

        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // ✅ TOKEN GENERATION (USED BY LOGIN)
    public String generateToken(String username, Role role) {

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.name()) // ✅ Include Role
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 60 * 60 * 1000L * 24)) // 24 hours
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ TOKEN VALIDATION
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ✅ EXTRACT USERNAME
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // ✅ EXTRACT ROLE
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // 🔒 INTERNAL
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
