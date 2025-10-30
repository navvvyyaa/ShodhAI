package com.shodh.contest.controller;

import com.shodh.contest.dto.ContestResponse;
import com.shodh.contest.dto.LeaderboardEntry;
import com.shodh.contest.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contests")
@CrossOrigin(origins = {"http://localhost:5000", "http://0.0.0.0:5000"})
public class ContestController {

    @Autowired
    private ContestService contestService;

    @GetMapping("/{contestId}")
    public ResponseEntity<ContestResponse> getContest(@PathVariable Long contestId) {
        ContestResponse contest = contestService.getContestById(contestId);
        return ResponseEntity.ok(contest);
    }

    @GetMapping("/{contestId}/leaderboard")
    public ResponseEntity<List<LeaderboardEntry>> getLeaderboard(@PathVariable Long contestId) {
        List<LeaderboardEntry> leaderboard = contestService.getLeaderboard(contestId);
        return ResponseEntity.ok(leaderboard);
    }
}
