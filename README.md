# Shodh-a-Code Contest Platform

A full-stack live coding contest platform with real-time code judging, leaderboards, and asynchronous submission processing.

## Features

### Backend (Spring Boot)
- **RESTful API** with comprehensive endpoints for contests, problems, submissions, and leaderboards
- **Live Code Judge Engine** that executes user code with resource limits (time, memory)
- **Asynchronous Processing** using Spring's async capabilities for non-blocking submission evaluation
- **Data Models**: Contest, Problem, TestCase, Submission, User with JPA/Hibernate
- **Sample Data**: Pre-populated contest with 3 coding problems for immediate testing
- **H2 In-Memory Database** for fast development and testing

### Frontend (Next.js + React + Tailwind CSS)
- **Join Page**: Simple form to enter Contest ID and Username
- **Contest Dashboard**: 
  - Problem viewer with detailed descriptions and sample test cases
  - Monaco code editor with syntax highlighting
  - Real-time submission status tracking with 2-second polling
  - Live leaderboard with 20-second auto-refresh
- **Responsive UI** built with Tailwind CSS
- **Real-time Updates** using polling mechanism

### Code Execution
- **Docker-based Sandboxing**: Executes code in isolated Docker containers with openjdk:17-slim
- **Automatic Fallback**: Uses ProcessBuilder if Docker is unavailable
- **Security Isolation**: Containers run with --network=none, --read-only, --pids-limit
- **Resource Limits**: Configurable time (5s), memory (256MB), and CPU (0.5 cores) constraints
- **Test Case Validation**: Automated comparison of actual vs expected output
- **Multiple Status Types**: Pending, Running, Accepted, Wrong Answer, TLE, Runtime Error, Compilation Error

## Tech Stack

- **Backend**: Spring Boot 3.2.0, Java 19 (GraalVM), Maven
- **Frontend**: Next.js 14, React 18, TypeScript, Tailwind CSS
- **Database**: H2 (in-memory)
- **Code Editor**: Monaco Editor
- **HTTP Client**: Axios

## Quick Start

### Prerequisites
- Java 19+ (GraalVM recommended)
- Node.js 20+
- Maven

### Running the Application

1. **Start Backend** (runs on port 8080):
   ```bash
   cd backend
   mvn clean package -DskipTests
   java -jar target/contest-platform-1.0.0.jar
   ```

2. **Start Frontend** (runs on port 5000):
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

3. **Access the Application**:
   - Open http://localhost:5000
   - Default Contest ID: **1**
   - Enter any username to join

## API Endpoints

### Contests
- `GET /api/contests/{contestId}` - Get contest details with problems

### Submissions
- `POST /api/submissions` - Submit code for evaluation
  ```json
  {
    "contestId": 1,
    "problemId": 1,
    "username": "john_doe",
    "code": "...",
    "language": "java"
  }
  ```
- `GET /api/submissions/{submissionId}` - Get submission status and results

### Leaderboard
- `GET /api/contests/{contestId}/leaderboard` - Get live rankings

## Sample Problems

The platform comes pre-loaded with 3 problems:

1. **Sum of Two Numbers** (Easy, 100 points)
   - Read two integers, output their sum
   
2. **Reverse a String** (Easy, 150 points)
   - Read a string, output it reversed
   
3. **Factorial** (Medium, 200 points)
   - Calculate factorial of a number

## Architecture

### Code Judge Flow

1. User submits code via frontend
2. Backend creates submission with PENDING status
3. Async executor picks up submission
4. Check if Docker is available:
   - **With Docker**: Execute in isolated container with security restrictions
   - **Without Docker**: Fall back to ProcessBuilder with basic isolation
5. For each test case:
   - Compile code in container (javac Solution.java)
   - Execute with stdin from input.txt file
   - Capture stdout and compare with expected output
   - Track execution time with timeout enforcement
6. Container cleanup and resource release
7. Update submission status (ACCEPTED/WRONG_ANSWER/TLE/etc.)
8. Frontend polls for updates every 2 seconds
9. Leaderboard recalculates on accepted submissions

### Database Schema

```
users (id, username, created_at)
contests (id, title, description, start_time, end_time)
problems (id, contest_id, title, description, difficulty, points, time_limit, memory_limit)
test_cases (id, problem_id, input, expected_output, is_sample, test_order)
submissions (id, user_id, problem_id, contest_id, code, language, status, result, execution_time, test_cases_passed, total_test_cases)
```

## Configuration

### Backend (`application.properties`)
```properties
server.port=8080
judge.temp.dir=/tmp/shodh-judge
judge.timeout.seconds=5
judge.memory.limit.mb=256
```

### Frontend (`next.config.js`)
- API proxy configured to route `/api/*` to `http://localhost:8080`
- Server binds to `0.0.0.0:5000` for accessibility

## Development Notes

### Code Execution Security
- **Docker Mode** (when Docker is available):
  - Runs code in isolated containers (openjdk:17-slim)
  - Network disabled (--network=none)
  - Read-only filesystem (--read-only)
  - Memory limits enforced (--memory)
  - CPU limits enforced (--cpus=0.5)
  - Process limits (--pids-limit=50)
- **Fallback Mode** (when Docker unavailable):
  - Uses ProcessBuilder with Java security manager
  - Basic timeout and memory limit enforcement
  - Suitable for development/testing only
- Temporary files cleaned up after each execution
- Automatic container cleanup on timeout or error

### Real-time Updates
- Submission status: Polls every 2 seconds until completion
- Leaderboard: Polls every 20 seconds for fresh rankings
- WebSocket support recommended for production

## Production Deployment

### Security Configuration
**IMPORTANT**: For production deployment, set the following in `application.properties`:

```properties
judge.allow.fallback=false
```

This ensures that code execution will ONLY happen in Docker containers with full security isolation. Without Docker available, submissions will fail with a clear error message rather than falling back to the unsafe ProcessBuilder mode.

### Docker Requirements
- Docker must be installed and accessible on the production server
- The `openjdk:17-slim` image should be pre-pulled to avoid delays
- Verify Docker availability before starting the judge service

### Monitoring Recommendations
- Monitor failed container cleanup operations
- Alert on Docker service failures
- Track submission execution times and resource usage
- Log all security-related events

## Future Enhancements

- Multi-language support (Python, C++, JavaScript)
- WebSocket for real-time updates
- User authentication and account management
- Contest creation and management UI
- Detailed error messages and test case visibility
- Code similarity detection
- Advanced analytics and statistics
- Rate limiting and abuse prevention

## License

MIT License - Built as an assessment project for Shodh AI
