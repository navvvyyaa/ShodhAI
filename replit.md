# Shodh-a-Code Contest Platform - Replit Project

## Project Overview
Full-stack live coding contest platform with real-time code judging, built with Spring Boot backend and Next.js frontend.

## Architecture
- **Backend**: Spring Boot 3.2.0 REST API with async code execution engine (port 8080)
- **Frontend**: Next.js 14 + React + Tailwind CSS (port 5000)
- **Database**: H2 in-memory database with JPA/Hibernate
- **Code Execution**: ProcessBuilder-based Java code judge with resource limits

## Key Features
1. Contest management with multiple coding problems
2. Live code judge with test case validation
3. Real-time submission status tracking (2s polling)
4. Live leaderboard with auto-refresh (20s polling)
5. Monaco code editor with syntax highlighting
6. Pre-seeded sample contest (Contest ID: 1) with 3 problems

## Recent Changes (Oct 30, 2025)
- Initial project setup with complete backend and frontend implementation
- Created data models: User, Contest, Problem, TestCase, Submission
- Implemented REST API endpoints for contests, submissions, and leaderboard
- Built asynchronous code judge service using ProcessBuilder
- Configured workflows for backend (Java) and frontend (Next.js)
- Added sample data seeder with 3 coding problems
- Created responsive UI with problem viewer, code editor, and leaderboard

## Project Structure
```
backend/
├── src/main/java/com/shodh/contest/
│   ├── config/          # Spring configuration and data seeding
│   ├── controller/      # REST API endpoints
│   ├── dto/             # Data transfer objects
│   ├── model/           # JPA entities
│   ├── repository/      # Database repositories
│   └── service/         # Business logic and code judge
├── pom.xml              # Maven dependencies
└── target/              # Compiled JAR

frontend/
├── src/
│   ├── app/             # Next.js pages
│   ├── components/      # React components
│   ├── lib/             # API client
│   └── types/           # TypeScript types
├── package.json
└── next.config.js
```

## Workflows
1. **backend**: Runs Spring Boot application on port 8080
2. **frontend**: Runs Next.js dev server on port 5000 (webview)

## User Preferences
- Clean, production-ready code without excessive comments
- RESTful API design patterns
- Async/await for non-blocking operations
- Responsive UI with Tailwind CSS
- Real-time updates via polling (WebSocket ready for future)

## Testing
- Default Contest ID: 1
- Access at http://localhost:5000
- Sample problems include: Sum of Two Numbers, Reverse a String, Factorial
- Code submissions are processed asynchronously with resource limits

## Known Limitations
- Currently supports Java only (multi-language support planned)
- Uses ProcessBuilder instead of Docker (simpler for development)
- Polling-based real-time updates (WebSocket recommended for production)
- In-memory database (data resets on restart)

## Dependencies Installed
- Java GraalVM 22.3 (Java 19)
- Maven 3.9.9
- Node.js 20
- Next.js, React, TypeScript, Tailwind CSS, Monaco Editor, Axios
