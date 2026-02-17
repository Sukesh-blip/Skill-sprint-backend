package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.ChallengeDTO;
import com.example.demo.entity.Challenge;
import com.example.demo.service.ChallengeService;

@RestController
@RequestMapping("/api/challenges")
@CrossOrigin(origins = "http://localhost:5173")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    // ✅ GET ALL CHALLENGES (USER OR ADMIN)
    @GetMapping
    public ResponseEntity<List<ChallengeDTO>> getAllChallenges() {

        String username = null;

        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            username = SecurityContextHolder.getContext().getAuthentication().getName();
        }

        if (username != null && !"anonymousUser".equals(username)) {
            return ResponseEntity.ok(
                    challengeService.getAllChallengesDTOForUser(username));
        }

        return ResponseEntity.ok(challengeService.getAllChallengesDTO());
    }

    // ✅ CREATE CHALLENGE (ADMIN ONLY)
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ChallengeDTO> create(@RequestBody Challenge challenge) {
        return ResponseEntity.ok(
                challengeService.createChallengeDTO(challenge));
    }

    // ✅ UPDATE CHALLENGE (ADMIN ONLY)
    @PutMapping("/update/{challengeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ChallengeDTO> update(
            @PathVariable Long challengeId,
            @RequestBody Challenge updated) {

        return ResponseEntity.ok(
                challengeService.updateChallengeDTO(challengeId, updated));
    }

    // ✅ DELETE CHALLENGE (ADMIN ONLY)
    @DeleteMapping("/delete/{challengeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long challengeId) {

        challengeService.deleteChallenge(challengeId);
        return ResponseEntity.ok("Challenge deleted successfully");
    }
}
