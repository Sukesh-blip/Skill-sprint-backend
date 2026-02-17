package com.example.demo.dto;

import java.time.LocalDateTime;
import com.example.demo.entity.Difficulty;

public class ChallengeDTO {
  private Long challengeId;
  private String title;
  private String description;
  private Difficulty difficulty;
  private LocalDateTime createdAt;
  private boolean solved; // ✅ Added to indicate if user solved it

  public ChallengeDTO() {
  }

  public ChallengeDTO(Long challengeId, String title, String description, Difficulty difficulty,
      LocalDateTime createdAt) {
    this.challengeId = challengeId;
    this.title = title;
    this.description = description;
    this.difficulty = difficulty;
    this.createdAt = createdAt;
  }

  public ChallengeDTO(Long challengeId, String title, String description, Difficulty difficulty,
      LocalDateTime createdAt,
      boolean solved) {
    this(challengeId, title, description, difficulty, createdAt);
    this.solved = solved;
  }

  // Getters and Setters
  public Long getChallengeId() {
    return challengeId;
  }

  public void setChallengeId(Long challengeId) {
    this.challengeId = challengeId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Difficulty getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(Difficulty difficulty) {
    this.difficulty = difficulty;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public boolean isSolved() {
    return solved;
  }

  public void setSolved(boolean solved) {
    this.solved = solved;
  }
}
