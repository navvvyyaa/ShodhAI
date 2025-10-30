'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';

export default function JoinPage() {
  const router = useRouter();
  const [contestId, setContestId] = useState('1');
  const [username, setUsername] = useState('');
  const [error, setError] = useState('');

  const handleJoin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (!contestId || !username.trim()) {
      setError('Please fill in all fields');
      return;
    }

    try {
      const response = await fetch(`/api/contests/${contestId}`);
      if (!response.ok) {
        throw new Error('Contest not found');
      }
      
      router.push(`/contest/${contestId}?username=${encodeURIComponent(username)}`);
    } catch (err) {
      setError('Invalid Contest ID. Please try again.');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-500 to-purple-600">
      <div className="bg-white p-8 rounded-lg shadow-2xl w-full max-w-md">
        <div className="text-center mb-8">
          <h1 className="text-4xl font-bold text-gray-800 mb-2">Shodh-a-Code</h1>
          <p className="text-gray-600">Live Coding Contest Platform</p>
        </div>

        <form onSubmit={handleJoin} className="space-y-6">
          <div>
            <label htmlFor="contestId" className="block text-sm font-medium text-gray-700 mb-2">
              Contest ID
            </label>
            <input
              type="text"
              id="contestId"
              value={contestId}
              onChange={(e) => setContestId(e.target.value)}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none text-gray-800"
              placeholder="Enter contest ID"
              required
            />
          </div>

          <div>
            <label htmlFor="username" className="block text-sm font-medium text-gray-700 mb-2">
              Username
            </label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none text-gray-800"
              placeholder="Enter your username"
              required
            />
          </div>

          {error && (
            <div className="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-lg text-sm">
              {error}
            </div>
          )}

          <button
            type="submit"
            className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 rounded-lg transition duration-200 transform hover:scale-105"
          >
            Join Contest
          </button>
        </form>

        <div className="mt-6 text-center text-sm text-gray-500">
          <p>Default Contest ID: 1</p>
        </div>
      </div>
    </div>
  );
}
