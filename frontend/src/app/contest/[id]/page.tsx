'use client';

import { useState, useEffect, useCallback } from 'react';
import { useParams, useSearchParams } from 'next/navigation';
import CodeEditor from '@/components/CodeEditor';
import ProblemView from '@/components/ProblemView';
import Leaderboard from '@/components/Leaderboard';
import SubmissionStatus from '@/components/SubmissionStatus';
import { api } from '@/lib/api';
import { Contest, Problem, SubmissionResponse, LeaderboardEntry } from '@/types';

export default function ContestPage() {
  const params = useParams();
  const searchParams = useSearchParams();
  const contestId = parseInt(params.id as string);
  const username = searchParams.get('username') || 'Anonymous';

  const [contest, setContest] = useState<Contest | null>(null);
  const [selectedProblem, setSelectedProblem] = useState<Problem | null>(null);
  const [code, setCode] = useState<string>('');
  const [language] = useState<string>('java');
  const [currentSubmission, setCurrentSubmission] = useState<SubmissionResponse | null>(null);
  const [leaderboard, setLeaderboard] = useState<LeaderboardEntry[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    loadContest();
    const leaderboardInterval = setInterval(() => {
      loadLeaderboard();
    }, 20000);

    return () => clearInterval(leaderboardInterval);
  }, [contestId]);

  useEffect(() => {
    if (contest && contest.problems.length > 0 && !selectedProblem) {
      setSelectedProblem(contest.problems[0]);
      setCode(getDefaultCode(contest.problems[0]));
    }
  }, [contest]);

  const loadContest = async () => {
    try {
      const data = await api.getContest(contestId);
      setContest(data);
      loadLeaderboard();
    } catch (err) {
      setError('Failed to load contest');
    }
  };

  const loadLeaderboard = async () => {
    try {
      const data = await api.getLeaderboard(contestId);
      setLeaderboard(data);
    } catch (err) {
      console.error('Failed to load leaderboard:', err);
    }
  };

  const getDefaultCode = (problem: Problem): string => {
    if (problem.title === 'Sum of Two Numbers') {
      return `import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int b = sc.nextInt();
        System.out.println(a + b);
        sc.close();
    }
}`;
    } else if (problem.title === 'Reverse a String') {
      return `import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        String reversed = new StringBuilder(str).reverse().toString();
        System.out.println(reversed);
        sc.close();
    }
}`;
    } else if (problem.title === 'Factorial') {
      return `import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long factorial = 1;
        for (int i = 1; i <= n; i++) {
            factorial *= i;
        }
        System.out.println(factorial);
        sc.close();
    }
}`;
    }
    return `import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Write your code here
        sc.close();
    }
}`;
  };

  const handleProblemSelect = (problem: Problem) => {
    setSelectedProblem(problem);
    setCode(getDefaultCode(problem));
    setCurrentSubmission(null);
  };

  const handleSubmit = async () => {
    if (!selectedProblem) return;

    setLoading(true);
    setError('');

    try {
      const response = await api.submitCode({
        contestId,
        problemId: selectedProblem.id,
        username,
        code,
        language,
      });

      setCurrentSubmission(response);
      pollSubmissionStatus(response.submissionId);
    } catch (err) {
      setError('Failed to submit code');
      setLoading(false);
    }
  };

  const pollSubmissionStatus = async (submissionId: number) => {
    const pollInterval = setInterval(async () => {
      try {
        const status = await api.getSubmissionStatus(submissionId);
        setCurrentSubmission(status);

        if (status.status !== 'PENDING' && status.status !== 'RUNNING') {
          clearInterval(pollInterval);
          setLoading(false);
          loadLeaderboard();
        }
      } catch (err) {
        console.error('Failed to poll submission status:', err);
        clearInterval(pollInterval);
        setLoading(false);
      }
    }, 2000);
  };

  if (!contest) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-xl text-gray-600">Loading contest...</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-100">
      <header className="bg-white shadow-md">
        <div className="container mx-auto px-4 py-4">
          <div className="flex justify-between items-center">
            <div>
              <h1 className="text-2xl font-bold text-gray-800">{contest.title}</h1>
              <p className="text-gray-600 text-sm">{contest.description}</p>
            </div>
            <div className="text-right">
              <p className="text-sm text-gray-600">Logged in as:</p>
              <p className="font-semibold text-gray-800">{username}</p>
            </div>
          </div>
        </div>
      </header>

      <div className="container mx-auto px-4 py-6">
        <div className="mb-4">
          <div className="flex gap-2 overflow-x-auto pb-2">
            {contest.problems.map((problem) => (
              <button
                key={problem.id}
                onClick={() => handleProblemSelect(problem)}
                className={`px-4 py-2 rounded-lg font-medium whitespace-nowrap transition ${
                  selectedProblem?.id === problem.id
                    ? 'bg-blue-600 text-white'
                    : 'bg-white text-gray-700 hover:bg-gray-100'
                }`}
              >
                {problem.title}
              </button>
            ))}
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2">
            <div className="grid grid-rows-2 gap-4 h-[calc(100vh-250px)]">
              <div className="row-span-1">
                {selectedProblem && <ProblemView problem={selectedProblem} />}
              </div>
              
              <div className="row-span-1">
                <div className="bg-white rounded-lg shadow-md p-4 h-full flex flex-col">
                  <div className="flex justify-between items-center mb-3">
                    <h3 className="text-lg font-semibold text-gray-800">Code Editor</h3>
                    <button
                      onClick={handleSubmit}
                      disabled={loading}
                      className={`px-6 py-2 rounded-lg font-medium transition ${
                        loading
                          ? 'bg-gray-400 cursor-not-allowed'
                          : 'bg-green-600 hover:bg-green-700 text-white'
                      }`}
                    >
                      {loading ? 'Submitting...' : 'Submit Code'}
                    </button>
                  </div>
                  
                  {error && (
                    <div className="bg-red-50 border border-red-200 text-red-600 px-4 py-2 rounded-lg text-sm mb-3">
                      {error}
                    </div>
                  )}
                  
                  <div className="flex-1">
                    <CodeEditor value={code} onChange={setCode} language={language} />
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div className="lg:col-span-1">
            <div className="space-y-4">
              <SubmissionStatus submission={currentSubmission} />
              <Leaderboard entries={leaderboard} currentUsername={username} />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
