package com.example.demo.dto;

import java.time.LocalDateTime;
import com.example.demo.entity.SubmissionStatus;

public class SubmissionResponse {

    private Long submissionId;
    private String solutionText;
    private LocalDateTime submittedAt;
    private SubmissionStatus status;

    public SubmissionResponse(Long submissionId, String solutionText, LocalDateTime submittedAt,
            SubmissionStatus status) {
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

    public SubmissionStatus getStatus() {
        return status;
    }
}
