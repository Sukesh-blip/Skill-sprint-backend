package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Challenge;
import com.example.demo.entity.User;
import com.example.demo.repository.ChallengeRepository;
import com.example.demo.repository.SubmissionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.dto.ChallengeDTO;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    // GET ALL
    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }

    public List<ChallengeDTO> getAllChallengesDTO() {
        return challengeRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ChallengeDTO> getAllChallengesDTOForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Long> solvedIds = submissionRepository.findSolvedChallengeIdsByUserId(user.getUserId())
                .stream().collect(Collectors.toSet());

        return challengeRepository.findAll().stream()
                .map(c -> {
                    ChallengeDTO dto = toDTO(c);
                    dto.setSolved(solvedIds.contains(c.getChallengeId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private ChallengeDTO toDTO(Challenge challenge) {
        return new ChallengeDTO(
                challenge.getChallengeId(),
                challenge.getTitle(),
                challenge.getDescription(),
                challenge.getDifficulty(),
                challenge.getCreatedAt());
    }

    // CREATE
    public Challenge createChallenge(Challenge challenge) {
        return challengeRepository.save(challenge);
    }

    public ChallengeDTO createChallengeDTO(Challenge challenge) {
        return toDTO(createChallenge(challenge));
    }

    // GET BY ID
    public Challenge getChallengeById(Long challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("Challenge not found"));
    }

    // UPDATE
    public Challenge updateChallenge(Long challengeId, Challenge updated) {
        Challenge existing = getChallengeById(challengeId);

        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setDifficulty(updated.getDifficulty());

        return challengeRepository.save(existing);
    }

    public ChallengeDTO updateChallengeDTO(Long challengeId, Challenge updated) {
        return toDTO(updateChallenge(challengeId, updated));
    }

    // DELETE
    public void deleteChallenge(Long challengeId) {
        Challenge challenge = getChallengeById(challengeId);
        challengeRepository.delete(challenge);
    }
}
