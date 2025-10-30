export interface Contest {
  id: number;
  title: string;
  description: string;
  startTime: string;
  endTime: string;
  problems: Problem[];
}

export interface Problem {
  id: number;
  title: string;
  description: string;
  difficultyLevel: string;
  timeLimitSeconds: number;
  memoryLimitMb: number;
  points: number;
  sampleTestCases: TestCase[];
}

export interface TestCase {
  input: string;
  expectedOutput: string;
}

export interface SubmissionRequest {
  contestId: number;
  problemId: number;
  username: string;
  code: string;
  language: string;
}

export interface SubmissionResponse {
  submissionId: number;
  status: string;
  result?: string;
  executionTimeMs?: number;
  memoryUsedMb?: number;
  testCasesPassed?: number;
  totalTestCases?: number;
}

export interface LeaderboardEntry {
  rank: number;
  username: string;
  totalScore: number;
  problemsSolved: number;
  totalTimeMs: number;
}
