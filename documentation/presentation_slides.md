# InterviewHub – Academic Project Presentation Deck

**Project Name:** InterviewHub – Multi-Interviewer Interview Management & Performance Tracking System  
**Department:** Department of Computer Science and Engineering, Chitkara University, Punjab  
**Evaluator / Submitted to:** Dr. Gagan Singla  
**Submitted by:**
- **Siddhika**
- **Vrinda**
- **Gunjot**
- **Parth**

---

## Slide 1: Title Slide
- **Header:** Project Presentation on
- **Title:** **InterviewHub**
- **Subtitle:** Multi-Interviewer Interview Management & Performance Tracking System
- **Department:** Department of Computer Science and Engineering, Chitkara University, Punjab
- **Team Members:**
  - Siddhika
  - Vrinda
  - Gunjot
  - Parth
- **Submitted to:** Dr. Gagan Singla

---

## Slide 2: Problem Statement & Proposed Solution
### Problem Statement:
1. **Scattered & Disconnected Mock Interviews**:
   - Candidates attend isolated mock rounds with different interviewers without a unified platform to track request approvals, schedule dates, or maintain multi-round histories.
2. **No Standardized Competency Evaluation**:
   - Feedback is usually verbal or unstructured notes. Without multi-skill rubric scoring (0–100), candidates cannot diagnose specific weaknesses like Problem Solving or System Design.
3. **No Longitudinal Performance Tracking**:
   - Candidates cannot quantify growth over time. Furthermore, mock practice is disconnected from academic placement drives and eligibility criteria (CGPA/department rules).

### Our Answer: InterviewHub
> A robust two-sided Core Java + MySQL platform connecting candidates and interviewers, enforcing conflict-free slot booking, standardizing 6-skill rubric evaluation, calculating mathematical performance improvement over time, and integrating campus placement drive eligibility.

---

## Slide 3: Project Objectives and Scope
### Objectives:
1. **Multi-Interviewer Coordination**: Enable candidates to search, filter by specialization, and request Technical or HR interview slots with multiple expert interviewers.
2. **Standardized Rubric Evaluation**: Allow interviewers to record candidate performance across 6 competency rubrics (0–100) with detailed qualitative remarks.
3. **Longitudinal Performance Tracking**: Calculate real database-backed growth: Average, Highest, Lowest, First vs Latest score, and Improvement Percentage.
4. **Thread Safety & Concurrency**: Enforce atomic slot reservation via synchronized `SlotManager` and asynchronous background task handling via Producer–Consumer queue.
5. **Campus Placement Integration**: Preserve academic entities (`Student`, `Company`, `PlacementDrive`, `Application`) and evaluate eligibility via the `Eligible` interface.

### Scope:
- **In Scope**:
  - Pure Core Java 17 Console CLI (`Scanner` over `System.in`)
  - Layered Architecture (`model` $\rightarrow$ `dao` $\rightarrow$ `service` $\rightarrow$ `ui`)
  - MySQL 8.0 Relational Persistence via JDBC (`PreparedStatement` & Transactions)
  - PBKDF2 Cryptographic Password Hashing with Salt
  - Multithreading & Atomic Check-and-Decrement (`SlotManager`)
  - Producer–Consumer Task Queue (`wait()`/`notifyAll()`) & `ExecutorService`
  - 13 Automated JUnit 5 Unit & Concurrency Test Suites
- **Future Work**:
  - WebRTC Live Video/Audio Integration
  - Automated Calendar Sync (Google Calendar / Outlook API)
  - Enterprise Web & Mobile Client Frontends

---

## Slide 4: Hardware and Software Interface
### Hardware Interface:
- **Processor**: Intel Core i3 / AMD Ryzen 3 or higher (Multi-core CPU for multithreaded simulations)
- **Memory (RAM)**: 4 GB minimum (8 GB recommended for simultaneous IDE & MySQL server)
- **Storage**: Minimum 500 MB free space for JDK 17, Maven local repository (`.m2`), and MySQL databases
- **Peripherals**: Standard physical keyboard & terminal monitor display for console CLI menu interaction

### Software Interface:
- **Language & Platform**: Java 17 (JDK) with pure Object-Oriented Architecture
- **Database Engine**: MySQL 8.0 Server via JDBC (`mysql-connector-j` 8.3.0)
- **Interface / CLI**: Interactive menu-driven Console CLI using `Scanner`
- **Build & Dependencies**: Apache Maven 3.9+ with portable Windows wrapper (`mvnw.cmd`)
- **Security / Crypto**: PBKDF2 with HMAC-SHA256 (Java Cryptography Architecture)
- **Automated Testing**: JUnit 5 (`junit-jupiter` 5.10.2) running 13 test suites
- **CI/CD & Version Control**: Git, GitHub & GitHub Actions automated build pipeline

### Architecture Pipeline:
$$\text{Console CLI (Scanner)} \iff \text{Service Layer (OOP Logic)} \iff \text{DAO Layer (JDBC PreparedStatement)} \iff \text{MySQL 8.0 Database}$$

---

## Slide 5: System Architecture & Performance Metrics
### Three-Tiered Core Java Architecture:
1. **Java Console UI**:
   - Role-based Menus (Candidate, Interviewer, Admin)
   - Candidate Dashboard: Bookings, History, Drives, Performance Report
   - Interviewer Portal: Request Review, Question Bank & Rubric Scoring
   - Admin Control Panel: User Governance, Drives, Global Analytics
2. **Core Logic Layer**:
   - Encapsulated Domain Models (`User`, `Student`, `Candidate`, `Eligible`)
   - `SlotManager` with `synchronized` atomic check-and-decrement
   - `TaskQueue` Producer–Consumer (`wait()` / `notifyAll()`)
   - `CollectionManager`: $O(1)$ `HashMap` lookup, `HashSet` uniqueness, `Comparator` sort
   - Transaction management in `FeedbackDAO` (`commit()` / `rollback()`)
3. **MySQL Relational Storage (JDBC)**:
   - `users`, `candidates`, `interviewers` (Identity & Profiles)
   - `interview_requests` & `interviews` (Scheduling & Coordination)
   - `interview_feedback` & `skill_scores` (Rubric Evaluations)
   - `companies`, `placement_drives`, `applications` (Campus Placements)
   - `questions` (Technical & HR Question Bank)

### Performance Tracking & Evaluation Rubrics:
- **Mathematical Improvement Formula**:
  $$\text{Improvement Percentage} = \left(\frac{\text{Latest Score} - \text{First Score}}{\text{First Score}}\right) \times 100$$
  *(Safely handles division-by-zero if baseline is zero).*
- **6 Standardized Evaluation Rubrics (0–100 Scores)**:
  1. Technical Knowledge
  2. Problem Solving
  3. Communication Skills
  4. Confidence
  5. Logical Thinking
  6. Subject Knowledge

---

## Slide 6: Team and Module Allocation
### MEMBER 1: Siddhika
- **Assigned Module**: Core OOP Architecture, Security & Admin Governance
- **Deliverables**:
  - Base `User` class hierarchy, system entry point (`Main.java`), and `Admin` model
  - PBKDF2 with HMAC-SHA256 password cryptography & validation (`PasswordUtil`, `InputValidator`)
  - Admin control panel: user activation/deactivation, interviewer onboarding
  - Question bank management and global platform analytics
  - **Workload**: 10 Functional Requirements

### MEMBER 2: Vrinda
- **Assigned Module**: Candidate Portal, Interview Request Workflow & Placement Eligibility
- **Deliverables**:
  - Candidate registration, authentication & profile updates
  - Interviewer discovery & specialization search filters
  - Interview booking workflow (Technical & HR rounds) with slot conflict detection
  - Integration of academic `Student`, `Company`, `PlacementDrive`, and `Application` entities
  - `Eligible` interface implementation for campus placement drive validation
  - **Workload**: 9 Functional Requirements

### MEMBER 3: Gunjot
- **Assigned Module**: Interviewer Portal, Rubric Evaluation & JDBC Transactions
- **Deliverables**:
  - Interviewer login, expertise & profile management
  - Incoming request review pipeline (Accept / Reject state transitions)
  - Interview question bank selection & round execution
  - Standardized rubric evaluation (6 skill dimensions, scores 0–100)
  - `FeedbackDAO` atomic transaction commit & rollback (`setAutoCommit(false)`)
  - **Workload**: 9 Functional Requirements

### MEMBER 4: Parth
- **Assigned Module**: Performance Analytics, Concurrency & Collections Optimization
- **Deliverables**:
  - Longitudinal growth calculation (First/Latest/Avg/Percentage Growth)
  - `CollectionManager`: `ArrayList`, $O(1)$ `HashMap` lookups, `HashSet` duplicate prevention
  - `Comparator` sorting (CGPA, Name, Date) & Top 5 candidate ranking
  - `SlotManager` synchronized critical section (2 slots vs. 5 concurrent threads)
  - `TaskQueue` Producer–Consumer (`wait`/`notifyAll`) & `ExecutorService` thread pool
  - **Workload**: 10 Functional Requirements

---

## Slide 7: Thank You
- **Title**: Thank You!
- **Subtitle**: InterviewHub | Questions & Discussion
- **Footer**: Department of Computer Science and Engineering, Chitkara University, Punjab
- **Team**: Presented by Siddhika, Vrinda, Gunjot, Parth
