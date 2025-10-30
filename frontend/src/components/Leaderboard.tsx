'use client';

import { LeaderboardEntry } from '@/types';

interface LeaderboardProps {
  entries: LeaderboardEntry[];
  currentUsername?: string;
}

export default function Leaderboard({ entries, currentUsername }: LeaderboardProps) {
  return (
    <div className="bg-white rounded-lg shadow-md p-4">
      <h3 className="text-xl font-bold text-gray-800 mb-4 flex items-center">
        <span className="mr-2">🏆</span>
        Leaderboard
      </h3>
      
      {entries.length === 0 ? (
        <p className="text-gray-500 text-center py-4">No submissions yet</p>
      ) : (
        <div className="overflow-y-auto max-h-96">
          <table className="w-full">
            <thead className="bg-gray-50 sticky top-0">
              <tr className="text-left text-xs font-semibold text-gray-600 uppercase">
                <th className="p-2">Rank</th>
                <th className="p-2">User</th>
                <th className="p-2">Score</th>
                <th className="p-2">Solved</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {entries.map((entry) => (
                <tr
                  key={entry.username}
                  className={`hover:bg-gray-50 ${
                    entry.username === currentUsername ? 'bg-blue-50 font-semibold' : ''
                  }`}
                >
                  <td className="p-2">
                    <span className={`inline-flex items-center justify-center w-6 h-6 rounded-full text-sm ${
                      entry.rank === 1 ? 'bg-yellow-100 text-yellow-800' :
                      entry.rank === 2 ? 'bg-gray-200 text-gray-700' :
                      entry.rank === 3 ? 'bg-orange-100 text-orange-700' :
                      'bg-gray-100 text-gray-600'
                    }`}>
                      {entry.rank}
                    </span>
                  </td>
                  <td className="p-2 text-gray-800">{entry.username}</td>
                  <td className="p-2 text-gray-700">{entry.totalScore}</td>
                  <td className="p-2 text-gray-700">{entry.problemsSolved}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
