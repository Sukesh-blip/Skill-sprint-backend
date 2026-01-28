package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Challenge;
import com.example.demo.dto.ChallengeDTO;
import com.example.demo.service.ChallengeService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/challenges")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    @GetMapping
    public ResponseEntity<List<ChallengeDTO>> getAllChallenges(
            @RequestAttribute(value = "username", required = false) String username) {

        if (username != null) {
            return ResponseEntity.ok(challengeService.getAllChallengesDTOForUser(username));
        }
        return ResponseEntity.ok(challengeService.getAllChallengesDTO());
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestBody Challenge challenge,
            @RequestAttribute("role") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body("Unauthorized: Only Admins can create challenges");
        }
        return ResponseEntity.ok(challengeService.createChallengeDTO(challenge));
    }

    @PutMapping("/update/{challengeId}")
    public ResponseEntity<?> update(
            @PathVariable Long challengeId,
            @RequestBody Challenge updated,
            @RequestAttribute("role") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body("Unauthorized: Only Admins can update challenges");
        }
        return ResponseEntity.ok(challengeService.updateChallengeDTO(challengeId, updated));
    }

    @DeleteMapping("/delete/{challengeId}")
    public ResponseEntity<?> delete(
            @PathVariable Long challengeId,
            @RequestAttribute("role") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body("Unauthorized: Only Admins can delete challenges");
        }
        challengeService.deleteChallenge(challengeId);
        return ResponseEntity.ok("Challenge deleted successfully");
    }
}
