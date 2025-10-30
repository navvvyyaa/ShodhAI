package com.shodh.contest.controller;

import com.shodh.contest.dto.SubmissionRequest;
import com.shodh.contest.dto.SubmissionResponse;
import com.shodh.contest.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/submissions")
@CrossOrigin(origins = {"http://localhost:5000", "http://0.0.0.0:5000"})
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @PostMapping
    public ResponseEntity<SubmissionResponse> submitCode(@RequestBody SubmissionRequest request) {
        SubmissionResponse response = submissionService.createSubmission(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<SubmissionResponse> getSubmissionStatus(@PathVariable Long submissionId) {
        SubmissionResponse response = submissionService.getSubmissionStatus(submissionId);
        return ResponseEntity.ok(response);
    }
}
