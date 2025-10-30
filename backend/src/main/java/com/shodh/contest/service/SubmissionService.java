package com.shodh.contest.service;

import com.shodh.contest.dto.SubmissionRequest;
import com.shodh.contest.dto.SubmissionResponse;
import com.shodh.contest.model.Contest;
import com.shodh.contest.model.Problem;
import com.shodh.contest.model.Submission;
import com.shodh.contest.model.User;
import com.shodh.contest.repository.ContestRepository;
import com.shodh.contest.repository.ProblemRepository;
import com.shodh.contest.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private CodeJudgeService codeJudgeService;

    @Transactional
    public SubmissionResponse createSubmission(SubmissionRequest request) {
        User user = userService.getOrCreateUser(request.getUsername());
        
        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        Contest contest = contestRepository.findById(request.getContestId())
                .orElseThrow(() -> new RuntimeException("Contest not found"));

        Submission submission = new Submission();
        submission.setUser(user);
        submission.setProblem(problem);
        submission.setContest(contest);
        submission.setCode(request.getCode());
        submission.setLanguage(request.getLanguage());
        submission.setStatus(Submission.SubmissionStatus.PENDING);

        submission = submissionRepository.save(submission);

        codeJudgeService.judgeSubmission(submission.getId());

        SubmissionResponse response = new SubmissionResponse();
        response.setSubmissionId(submission.getId());
        response.setStatus(submission.getStatus().toString());
        return response;
    }

    @Transactional(readOnly = true)
    public SubmissionResponse getSubmissionStatus(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        SubmissionResponse response = new SubmissionResponse();
        response.setSubmissionId(submission.getId());
        response.setStatus(submission.getStatus().toString());
        response.setResult(submission.getResult());
        response.setExecutionTimeMs(submission.getExecutionTimeMs());
        response.setMemoryUsedMb(submission.getMemoryUsedMb());
        response.setTestCasesPassed(submission.getTestCasesPassed());
        response.setTotalTestCases(submission.getTotalTestCases());

        return response;
    }
}
