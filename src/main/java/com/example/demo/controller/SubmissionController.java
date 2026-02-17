package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.SubmissionRequest;
import com.example.demo.dto.SubmissionResponse;
import com.example.demo.entity.Challenge;
import com.example.demo.entity.User;
import com.example.demo.service.ChallengeService;
import com.example.demo.service.SubmissionService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChallengeService challengeService;

    // ✅ SUBMIT SOLUTION (USER)
    @PostMapping("/challenges/{challengeId}/submit")
    public ResponseEntity<?> submit(
            @PathVariable Long challengeId,
            @RequestBody SubmissionRequest request) {

        if (request.getSolutionText() == null || request.getSolutionText().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Solution cannot be empty");
        }

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userService.getByUsername(username);
        Challenge challenge = challengeService.getChallengeById(challengeId);

        submissionService.submitSolution(user, challenge, request.getSolutionText());

        return ResponseEntity.ok("Submission saved");
    }

    // ✅ GET USER SUBMISSIONS
    @GetMapping("/challenges/{challengeId}/submissions")
    public ResponseEntity<List<SubmissionResponse>> getSubmissions(
            @PathVariable Long challengeId) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userService.getByUsername(username);
        Challenge challenge = challengeService.getChallengeById(challengeId);

        return ResponseEntity.ok(
                submissionService.getUserSubmissions(user, challenge));
    }

    // ✅ DELETE SUBMISSION (ADMIN ONLY)
    @DeleteMapping("/submissions/{submissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteSubmission(
            @PathVariable Long submissionId) {

        submissionService.deleteSubmission(submissionId);
        return ResponseEntity.ok("Submission deleted successfully");
    }
}
