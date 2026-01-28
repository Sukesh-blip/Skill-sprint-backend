package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.SubmissionRequest;
import com.example.demo.entity.Challenge;
import com.example.demo.entity.User;
import com.example.demo.service.ChallengeService;
import com.example.demo.service.SubmissionService;
import com.example.demo.service.UserService;

import com.example.demo.dto.SubmissionResponse;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChallengeService challengeService;

    @PostMapping("/challenges/{challengeId}/submit")
    public ResponseEntity<?> submit(
            @PathVariable Long challengeId,
            @RequestBody SubmissionRequest request,
            @RequestAttribute("username") String username) {
        if (request.getSolution() == null || request.getSolution().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Solution cannot be empty");
        }

        User user = userService.getByUsername(username);
        Challenge challenge = challengeService.getChallengeById(challengeId);

        submissionService.submitSolution(user, challenge, request.getSolution());

        return ResponseEntity.ok("Submission saved");
    }

    @GetMapping("/challenges/{challengeId}/submissions")
    public ResponseEntity<List<SubmissionResponse>> getSubmissions(
            @PathVariable Long challengeId,
            @RequestAttribute("username") String username) {

        User user = userService.getByUsername(username);
        Challenge challenge = challengeService.getChallengeById(challengeId);

        return ResponseEntity.ok(submissionService.getUserSubmissions(user, challenge));
    }

    @DeleteMapping("/submissions/{submissionId}")
    public ResponseEntity<?> deleteSubmission(
            @PathVariable Long submissionId,
            @RequestAttribute("role") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body("Unauthorized: Only Admins can delete submissions");
        }
        submissionService.deleteSubmission(submissionId);
        return ResponseEntity.ok("Submission deleted successfully");
    }
}
