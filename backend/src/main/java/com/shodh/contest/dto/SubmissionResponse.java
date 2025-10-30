package com.shodh.contest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionResponse {
    private Long submissionId;
    private String status;
    private String result;
    private Long executionTimeMs;
    private Double memoryUsedMb;
    private Integer testCasesPassed;
    private Integer totalTestCases;
}
