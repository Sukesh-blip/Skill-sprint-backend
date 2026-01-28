package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.SubmissionResponse;
import com.example.demo.entity.Challenge;
import com.example.demo.entity.Submission;
import com.example.demo.entity.User;
import com.example.demo.repository.SubmissionRepository;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    // SUBMIT SOLUTION
    public Submission submitSolution(User user, Challenge challenge, String solution) {
        Submission submission = new Submission();
        submission.setUser(user);
        submission.setChallenge(challenge);
        submission.setSolution(solution);
        submission.setStatus("SUBMITTED");
        submission.setSubmittedAt(LocalDateTime.now());

        return submissionRepository.save(submission);
    }

    // GET USER SUBMISSIONS FOR A CHALLENGE
    public List<SubmissionResponse> getUserSubmissions(User user, Challenge challenge) {
        return submissionRepository
                .findByUserAndChallengeOrderBySubmittedAtDesc(user, challenge)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // DELETE SUBMISSION
    public void deleteSubmission(Long submissionId) {
        submissionRepository.deleteById(submissionId);
    }

    // DTO MAPPER (IMPORTANT)
    private SubmissionResponse toResponse(Submission submission) {
        return new SubmissionResponse(
                submission.getSubmissionId(),
                submission.getSolution(),
                submission.getSubmittedAt(),
                submission.getStatus());
    }
}
