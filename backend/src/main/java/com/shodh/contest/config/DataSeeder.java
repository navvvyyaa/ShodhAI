package com.shodh.contest.config;

import com.shodh.contest.model.Contest;
import com.shodh.contest.model.Problem;
import com.shodh.contest.model.TestCase;
import com.shodh.contest.repository.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private ContestRepository contestRepository;

    @Override
    public void run(String... args) throws Exception {
        Contest contest = new Contest();
        contest.setTitle("Shodh AI Beginner Contest");
        contest.setDescription("Welcome to your first coding contest! Solve these problems to test your programming skills.");
        contest.setStartTime(LocalDateTime.now().minusHours(1));
        contest.setEndTime(LocalDateTime.now().plusHours(3));
        contest.setProblems(new ArrayList<>());

        Problem problem1 = createProblem1();
        problem1.setContest(contest);
        contest.getProblems().add(problem1);

        Problem problem2 = createProblem2();
        problem2.setContest(contest);
        contest.getProblems().add(problem2);

        Problem problem3 = createProblem3();
        problem3.setContest(contest);
        contest.getProblems().add(problem3);

        contestRepository.save(contest);

        System.out.println("===========================================");
        System.out.println("Sample data seeded successfully!");
        System.out.println("Contest ID: " + contest.getId());
        System.out.println("Contest Title: " + contest.getTitle());
        System.out.println("Problems: " + contest.getProblems().size());
        System.out.println("===========================================");
    }

    private Problem createProblem1() {
        Problem problem = new Problem();
        problem.setTitle("Sum of Two Numbers");
        problem.setDescription("Write a program that reads two integers from input and prints their sum.\n\n" +
                "**Input Format:**\n" +
                "Two integers A and B separated by a space.\n\n" +
                "**Output Format:**\n" +
                "Print the sum of A and B.\n\n" +
                "**Constraints:**\n" +
                "- -1000 ≤ A, B ≤ 1000\n\n" +
                "**Example:**\n" +
                "Input: 5 3\n" +
                "Output: 8");
        problem.setDifficultyLevel("Easy");
        problem.setTimeLimitSeconds(1);
        problem.setMemoryLimitMb(128);
        problem.setPoints(100);
        problem.setTestCases(new ArrayList<>());

        TestCase tc1 = new TestCase();
        tc1.setProblem(problem);
        tc1.setInput("5 3");
        tc1.setExpectedOutput("8");
        tc1.setIsSample(true);
        tc1.setTestOrder(1);
        problem.getTestCases().add(tc1);

        TestCase tc2 = new TestCase();
        tc2.setProblem(problem);
        tc2.setInput("10 20");
        tc2.setExpectedOutput("30");
        tc2.setIsSample(false);
        tc2.setTestOrder(2);
        problem.getTestCases().add(tc2);

        TestCase tc3 = new TestCase();
        tc3.setProblem(problem);
        tc3.setInput("-5 5");
        tc3.setExpectedOutput("0");
        tc3.setIsSample(false);
        tc3.setTestOrder(3);
        problem.getTestCases().add(tc3);

        return problem;
    }

    private Problem createProblem2() {
        Problem problem = new Problem();
        problem.setTitle("Reverse a String");
        problem.setDescription("Write a program that reads a string and prints it in reverse order.\n\n" +
                "**Input Format:**\n" +
                "A single line containing a string.\n\n" +
                "**Output Format:**\n" +
                "Print the string in reverse order.\n\n" +
                "**Constraints:**\n" +
                "- 1 ≤ length of string ≤ 100\n\n" +
                "**Example:**\n" +
                "Input: hello\n" +
                "Output: olleh");
        problem.setDifficultyLevel("Easy");
        problem.setTimeLimitSeconds(1);
        problem.setMemoryLimitMb(128);
        problem.setPoints(150);
        problem.setTestCases(new ArrayList<>());

        TestCase tc1 = new TestCase();
        tc1.setProblem(problem);
        tc1.setInput("hello");
        tc1.setExpectedOutput("olleh");
        tc1.setIsSample(true);
        tc1.setTestOrder(1);
        problem.getTestCases().add(tc1);

        TestCase tc2 = new TestCase();
        tc2.setProblem(problem);
        tc2.setInput("world");
        tc2.setExpectedOutput("dlrow");
        tc2.setIsSample(false);
        tc2.setTestOrder(2);
        problem.getTestCases().add(tc2);

        TestCase tc3 = new TestCase();
        tc3.setProblem(problem);
        tc3.setInput("racecar");
        tc3.setExpectedOutput("racecar");
        tc3.setIsSample(false);
        tc3.setTestOrder(3);
        problem.getTestCases().add(tc3);

        return problem;
    }

    private Problem createProblem3() {
        Problem problem = new Problem();
        problem.setTitle("Factorial");
        problem.setDescription("Write a program that reads a non-negative integer N and prints its factorial.\n\n" +
                "**Input Format:**\n" +
                "A single integer N.\n\n" +
                "**Output Format:**\n" +
                "Print the factorial of N.\n\n" +
                "**Constraints:**\n" +
                "- 0 ≤ N ≤ 12\n\n" +
                "**Note:** Factorial of 0 is 1.\n\n" +
                "**Example:**\n" +
                "Input: 5\n" +
                "Output: 120");
        problem.setDifficultyLevel("Medium");
        problem.setTimeLimitSeconds(1);
        problem.setMemoryLimitMb(128);
        problem.setPoints(200);
        problem.setTestCases(new ArrayList<>());

        TestCase tc1 = new TestCase();
        tc1.setProblem(problem);
        tc1.setInput("5");
        tc1.setExpectedOutput("120");
        tc1.setIsSample(true);
        tc1.setTestOrder(1);
        problem.getTestCases().add(tc1);

        TestCase tc2 = new TestCase();
        tc2.setProblem(problem);
        tc2.setInput("0");
        tc2.setExpectedOutput("1");
        tc2.setIsSample(false);
        tc2.setTestOrder(2);
        problem.getTestCases().add(tc2);

        TestCase tc3 = new TestCase();
        tc3.setProblem(problem);
        tc3.setInput("10");
        tc3.setExpectedOutput("3628800");
        tc3.setIsSample(false);
        tc3.setTestOrder(3);
        problem.getTestCases().add(tc3);

        return problem;
    }
}
