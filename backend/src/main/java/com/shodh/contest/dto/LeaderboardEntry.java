package com.shodh.contest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardEntry {
    private Integer rank;
    private String username;
    private Integer totalScore;
    private Integer problemsSolved;
    private Long totalTimeMs;
}
