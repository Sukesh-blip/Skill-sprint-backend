package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.entity.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${app.admin.key}")
    private String secretAdminKey;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // ✅ Protected Admin Registration
        if ("ADMIN".equalsIgnoreCase(request.getRole())) {
            if (secretAdminKey.equals(request.getAdminKey())) {
                user.setRole(Role.ADMIN);
            } else {
                return ResponseEntity.status(403).body("Invalid Admin Key. Registration rejected.");
            }
        } else {
            user.setRole(Role.USER);
        }

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // ✅ Verify Role during Login
        if (request.getRole() != null && !user.getRole().name().equalsIgnoreCase(request.getRole())) {
            return ResponseEntity.status(401).body("Invalid role for this user");
        }

        // ✅ EXTRA SECURITY: Verify Admin Key during Login (if user is ADMIN)
        if (Role.ADMIN.equals(user.getRole())) {
            if (request.getAdminKey() == null || !secretAdminKey.equals(request.getAdminKey())) {
                return ResponseEntity.status(401).body("Admin login requires a valid Secret Admin Key");
            }
        }

        String token = jwtService.generateToken(user.getUsername(), user.getRole());

        return ResponseEntity.ok(new LoginResponse(token, user.getRole(), user.getUsername()));
    }

    // ✅ LOGOUT (Frontend must clear local storage)
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("Logged out successfully. Please clear your local storage.");
    }
}
