package com.shodh.contest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemResponse {
    private Long id;
    private String title;
    private String description;
    private String difficultyLevel;
    private Integer timeLimitSeconds;
    private Integer memoryLimitMb;
    private Integer points;
    private List<TestCaseResponse> sampleTestCases;
}
