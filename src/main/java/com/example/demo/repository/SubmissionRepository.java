package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Submission;
import com.example.demo.entity.User;
import com.example.demo.entity.Challenge;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByUser(User user);

    List<Submission> findByChallenge(Challenge challenge);

    List<Submission> findByUserAndChallengeOrderBySubmittedAtDesc(
            User user,
            Challenge challenge);

    @Query("SELECT DISTINCT s.challenge.challengeId FROM Submission s WHERE s.user.userId = :userId")
    List<Long> findSolvedChallengeIdsByUserId(@Param("userId") Long userId);
}
