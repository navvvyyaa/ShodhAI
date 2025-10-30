'use client';

import { SubmissionResponse } from '@/types';

interface SubmissionStatusProps {
  submission: SubmissionResponse | null;
}

export default function SubmissionStatus({ submission }: SubmissionStatusProps) {
  if (!submission) return null;

  const getStatusClass = (status: string) => {
    switch (status.toUpperCase()) {
      case 'PENDING':
        return 'status-pending';
      case 'RUNNING':
        return 'status-running';
      case 'ACCEPTED':
        return 'status-accepted';
      case 'WRONG_ANSWER':
      case 'TIME_LIMIT_EXCEEDED':
      case 'RUNTIME_ERROR':
      case 'COMPILATION_ERROR':
      case 'MEMORY_LIMIT_EXCEEDED':
        return 'status-error';
      default:
        return 'status-pending';
    }
  };

  return (
    <div className="bg-white rounded-lg shadow-md p-4 mb-4">
      <h3 className="text-lg font-semibold text-gray-800 mb-3">Submission Status</h3>
      
      <div className="space-y-2">
        <div className="flex items-center justify-between">
          <span className="text-gray-600">Status:</span>
          <span className={`status-badge ${getStatusClass(submission.status)}`}>
            {submission.status.replace(/_/g, ' ')}
          </span>
        </div>

        {submission.result && (
          <div className="flex items-start justify-between">
            <span className="text-gray-600">Result:</span>
            <span className="text-gray-800 text-right flex-1 ml-4">{submission.result}</span>
          </div>
        )}

        {submission.testCasesPassed !== undefined && submission.totalTestCases !== undefined && (
          <div className="flex items-center justify-between">
            <span className="text-gray-600">Test Cases:</span>
            <span className="text-gray-800">
              {submission.testCasesPassed} / {submission.totalTestCases} passed
            </span>
          </div>
        )}

        {submission.executionTimeMs !== undefined && (
          <div className="flex items-center justify-between">
            <span className="text-gray-600">Execution Time:</span>
            <span className="text-gray-800">{submission.executionTimeMs} ms</span>
          </div>
        )}
      </div>
    </div>
  );
}
