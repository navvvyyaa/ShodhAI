package com.shodh.contest.service;

import com.shodh.contest.dto.ContestResponse;
import com.shodh.contest.dto.LeaderboardEntry;
import com.shodh.contest.dto.ProblemResponse;
import com.shodh.contest.dto.TestCaseResponse;
import com.shodh.contest.model.Contest;
import com.shodh.contest.model.Problem;
import com.shodh.contest.model.Submission;
import com.shodh.contest.model.TestCase;
import com.shodh.contest.repository.ContestRepository;
import com.shodh.contest.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContestService {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Transactional(readOnly = true)
    public ContestResponse getContestById(Long contestId) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Contest not found"));

        ContestResponse response = new ContestResponse();
        response.setId(contest.getId());
        response.setTitle(contest.getTitle());
        response.setDescription(contest.getDescription());
        response.setStartTime(contest.getStartTime());
        response.setEndTime(contest.getEndTime());

        List<ProblemResponse> problemResponses = contest.getProblems().stream()
                .map(this::mapProblemToResponse)
                .collect(Collectors.toList());
        response.setProblems(problemResponses);

        return response;
    }

    @Transactional(readOnly = true)
    public List<LeaderboardEntry> getLeaderboard(Long contestId) {
        List<Submission> acceptedSubmissions = submissionRepository
                .findAcceptedSubmissionsByContestOrderByTime(contestId);

        Map<String, LeaderboardStats> userStats = new HashMap<>();

        for (Submission submission : acceptedSubmissions) {
            String username = submission.getUser().getUsername();
            userStats.putIfAbsent(username, new LeaderboardStats());
            
            LeaderboardStats stats = userStats.get(username);
            if (!stats.solvedProblems.contains(submission.getProblem().getId())) {
                stats.solvedProblems.add(submission.getProblem().getId());
                stats.totalScore += submission.getProblem().getPoints();
                stats.totalTime += submission.getExecutionTimeMs() != null ? submission.getExecutionTimeMs() : 0;
            }
        }

        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        for (Map.Entry<String, LeaderboardStats> entry : userStats.entrySet()) {
            LeaderboardStats stats = entry.getValue();
            leaderboard.add(new LeaderboardEntry(
                    0,
                    entry.getKey(),
                    stats.totalScore,
                    stats.solvedProblems.size(),
                    stats.totalTime
            ));
        }

        leaderboard.sort((a, b) -> {
            int scoreCompare = b.getTotalScore().compareTo(a.getTotalScore());
            if (scoreCompare != 0) return scoreCompare;
            return a.getTotalTimeMs().compareTo(b.getTotalTimeMs());
        });

        for (int i = 0; i < leaderboard.size(); i++) {
            leaderboard.get(i).setRank(i + 1);
        }

        return leaderboard;
    }

    private ProblemResponse mapProblemToResponse(Problem problem) {
        ProblemResponse response = new ProblemResponse();
        response.setId(problem.getId());
        response.setTitle(problem.getTitle());
        response.setDescription(problem.getDescription());
        response.setDifficultyLevel(problem.getDifficultyLevel());
        response.setTimeLimitSeconds(problem.getTimeLimitSeconds());
        response.setMemoryLimitMb(problem.getMemoryLimitMb());
        response.setPoints(problem.getPoints());

        List<TestCaseResponse> sampleTestCases = problem.getTestCases().stream()
                .filter(TestCase::getIsSample)
                .map(tc -> new TestCaseResponse(tc.getInput(), tc.getExpectedOutput()))
                .collect(Collectors.toList());
        response.setSampleTestCases(sampleTestCases);

        return response;
    }

    private static class LeaderboardStats {
        Set<Long> solvedProblems = new HashSet<>();
        int totalScore = 0;
        long totalTime = 0;
    }
}
