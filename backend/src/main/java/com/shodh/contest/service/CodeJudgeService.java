package com.shodh.contest.service;

import com.shodh.contest.model.Problem;
import com.shodh.contest.model.Submission;
import com.shodh.contest.model.TestCase;
import com.shodh.contest.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CodeJudgeService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Value("${judge.temp.dir:/tmp/shodh-judge}")
    private String tempDir;

    @Value("${judge.timeout.seconds:5}")
    private long timeoutSeconds;

    @Value("${judge.memory.limit.mb:256}")
    private long memoryLimitMb;

    @Async
    @Transactional
    public void judgeSubmission(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        try {
            submission.setStatus(Submission.SubmissionStatus.RUNNING);
            submissionRepository.save(submission);

            Problem problem = submission.getProblem();
            List<TestCase> testCases = problem.getTestCases();

            int passedTests = 0;
            long totalExecutionTime = 0;
            boolean allPassed = true;
            String resultMessage = "";

            for (TestCase testCase : testCases) {
                ExecutionResult result = executeCode(submission, testCase);
                totalExecutionTime += result.executionTimeMs;

                if (result.status == ExecutionStatus.SUCCESS) {
                    String actualOutput = result.output.trim();
                    String expectedOutput = testCase.getExpectedOutput().trim();

                    if (actualOutput.equals(expectedOutput)) {
                        passedTests++;
                    } else {
                        allPassed = false;
                        resultMessage = "Wrong Answer on test case " + testCase.getTestOrder();
                        break;
                    }
                } else {
                    allPassed = false;
                    switch (result.status) {
                        case TIME_LIMIT_EXCEEDED:
                            submission.setStatus(Submission.SubmissionStatus.TIME_LIMIT_EXCEEDED);
                            resultMessage = "Time Limit Exceeded on test case " + testCase.getTestOrder();
                            break;
                        case RUNTIME_ERROR:
                            submission.setStatus(Submission.SubmissionStatus.RUNTIME_ERROR);
                            resultMessage = "Runtime Error: " + result.error;
                            break;
                        case COMPILATION_ERROR:
                            submission.setStatus(Submission.SubmissionStatus.COMPILATION_ERROR);
                            resultMessage = "Compilation Error: " + result.error;
                            break;
                        default:
                            submission.setStatus(Submission.SubmissionStatus.RUNTIME_ERROR);
                            resultMessage = "Unknown Error";
                    }
                    break;
                }
            }

            if (allPassed) {
                submission.setStatus(Submission.SubmissionStatus.ACCEPTED);
                resultMessage = "All test cases passed!";
            } else if (submission.getStatus() == Submission.SubmissionStatus.RUNNING) {
                submission.setStatus(Submission.SubmissionStatus.WRONG_ANSWER);
            }

            submission.setTestCasesPassed(passedTests);
            submission.setTotalTestCases(testCases.size());
            submission.setExecutionTimeMs(totalExecutionTime / testCases.size());
            submission.setResult(resultMessage);
            submission.setCompletedAt(LocalDateTime.now());

        } catch (Exception e) {
            submission.setStatus(Submission.SubmissionStatus.RUNTIME_ERROR);
            submission.setResult("System Error: " + e.getMessage());
            submission.setCompletedAt(LocalDateTime.now());
        }

        submissionRepository.save(submission);
    }

    private ExecutionResult executeCode(Submission submission, TestCase testCase) {
        ExecutionResult result = new ExecutionResult();
        Path submissionDir = null;

        try {
            submissionDir = Files.createTempDirectory(Paths.get(tempDir), "submission-");
            Path codeFile = submissionDir.resolve("Solution.java");
            Files.writeString(codeFile, submission.getCode());

            long startTime = System.currentTimeMillis();
            
            ProcessBuilder compileBuilder = new ProcessBuilder(
                    "javac", "Solution.java"
            );
            compileBuilder.directory(submissionDir.toFile());
            compileBuilder.redirectErrorStream(true);

            Process compileProcess = compileBuilder.start();
            boolean compileFinished = compileProcess.waitFor(timeoutSeconds, TimeUnit.SECONDS);

            if (!compileFinished) {
                compileProcess.destroyForcibly();
                result.status = ExecutionStatus.TIME_LIMIT_EXCEEDED;
                return result;
            }

            if (compileProcess.exitValue() != 0) {
                result.status = ExecutionStatus.COMPILATION_ERROR;
                result.error = readStream(compileProcess.getInputStream());
                return result;
            }

            ProcessBuilder runBuilder = new ProcessBuilder(
                    "java", "-Xmx" + memoryLimitMb + "m", "Solution"
            );
            runBuilder.directory(submissionDir.toFile());
            
            Process runProcess = runBuilder.start();
            
            try (OutputStream os = runProcess.getOutputStream()) {
                os.write(testCase.getInput().getBytes());
                os.flush();
            }

            boolean runFinished = runProcess.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            long endTime = System.currentTimeMillis();
            result.executionTimeMs = endTime - startTime;

            if (!runFinished) {
                runProcess.destroyForcibly();
                result.status = ExecutionStatus.TIME_LIMIT_EXCEEDED;
                return result;
            }

            if (runProcess.exitValue() != 0) {
                result.status = ExecutionStatus.RUNTIME_ERROR;
                result.error = readStream(runProcess.getErrorStream());
                return result;
            }

            result.output = readStream(runProcess.getInputStream());
            result.status = ExecutionStatus.SUCCESS;

        } catch (Exception e) {
            result.status = ExecutionStatus.RUNTIME_ERROR;
            result.error = e.getMessage();
        } finally {
            if (submissionDir != null) {
                try {
                    deleteDirectory(submissionDir.toFile());
                } catch (IOException ignored) {
                }
            }
        }

        return result;
    }

    private String readStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString();
    }

    private void deleteDirectory(File directory) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

    private static class ExecutionResult {
        ExecutionStatus status;
        String output = "";
        String error = "";
        long executionTimeMs = 0;
    }

    private enum ExecutionStatus {
        SUCCESS,
        COMPILATION_ERROR,
        RUNTIME_ERROR,
        TIME_LIMIT_EXCEEDED
    }
}
