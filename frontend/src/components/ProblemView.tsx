'use client';

import { Problem } from '@/types';

interface ProblemViewProps {
  problem: Problem;
}

export default function ProblemView({ problem }: ProblemViewProps) {
  return (
    <div className="h-full overflow-y-auto bg-white p-6 rounded-lg shadow-md">
      <div className="mb-4">
        <h2 className="text-2xl font-bold text-gray-800 mb-2">{problem.title}</h2>
        <div className="flex gap-2 items-center text-sm">
          <span className={`px-2 py-1 rounded ${
            problem.difficultyLevel === 'Easy' ? 'bg-green-100 text-green-800' :
            problem.difficultyLevel === 'Medium' ? 'bg-yellow-100 text-yellow-800' :
            'bg-red-100 text-red-800'
          }`}>
            {problem.difficultyLevel}
          </span>
          <span className="text-gray-600">Points: {problem.points}</span>
          <span className="text-gray-600">Time Limit: {problem.timeLimitSeconds}s</span>
          <span className="text-gray-600">Memory: {problem.memoryLimitMb}MB</span>
        </div>
      </div>

      <div className="prose max-w-none">
        <div className="text-gray-700 whitespace-pre-wrap mb-6">
          {problem.description}
        </div>

        {problem.sampleTestCases && problem.sampleTestCases.length > 0 && (
          <div className="mt-6">
            <h3 className="text-lg font-semibold text-gray-800 mb-3">Sample Test Cases</h3>
            {problem.sampleTestCases.map((testCase, index) => (
              <div key={index} className="mb-4 bg-gray-50 p-4 rounded-lg">
                <div className="mb-2">
                  <span className="font-semibold text-gray-700">Input:</span>
                  <pre className="mt-1 bg-white p-2 rounded border border-gray-200 text-sm">
                    {testCase.input}
                  </pre>
                </div>
                <div>
                  <span className="font-semibold text-gray-700">Expected Output:</span>
                  <pre className="mt-1 bg-white p-2 rounded border border-gray-200 text-sm">
                    {testCase.expectedOutput}
                  </pre>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
