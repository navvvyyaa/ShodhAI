import axios from 'axios';
import { Contest, SubmissionRequest, SubmissionResponse, LeaderboardEntry } from '@/types';

const API_BASE_URL = '/api';

export const api = {
  async getContest(contestId: number): Promise<Contest> {
    const response = await axios.get(`${API_BASE_URL}/contests/${contestId}`);
    return response.data;
  },

  async submitCode(request: SubmissionRequest): Promise<SubmissionResponse> {
    const response = await axios.post(`${API_BASE_URL}/submissions`, request);
    return response.data;
  },

  async getSubmissionStatus(submissionId: number): Promise<SubmissionResponse> {
    const response = await axios.get(`${API_BASE_URL}/submissions/${submissionId}`);
    return response.data;
  },

  async getLeaderboard(contestId: number): Promise<LeaderboardEntry[]> {
    const response = await axios.get(`${API_BASE_URL}/contests/${contestId}/leaderboard`);
    return response.data;
  },
};
