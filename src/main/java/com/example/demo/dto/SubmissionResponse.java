package com.example.demo.dto;

import java.time.LocalDateTime;

public class SubmissionResponse {

    private Long submissionId;
    private String solutionText;
    private LocalDateTime submittedAt;
    private String status;

    public SubmissionResponse(Long submissionId, String solutionText, LocalDateTime submittedAt, String status) {
        this.submissionId = submissionId;
        this.solutionText = solutionText;
        this.submittedAt = submittedAt;
        this.status = status;
    }

    public Long getSubmissionId() {
        return submissionId;
    }

    public String getSolutionText() {
        return solutionText;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public String getStatus() {
        return status;
    }
}
