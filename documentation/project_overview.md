# InterviewHub – Multi-Interviewer Interview Management & Performance Tracking System

## 1. Project Introduction
**InterviewHub** is a console-based, two-sided interview coordination, evaluation, and performance analytics platform developed in Java 17 and backed by a relational MySQL database via JDBC. The system bridges candidates (college students and job applicants) with expert interviewers across multiple evaluation categories (Technical and HR). 

The platform provides a structured, multi-interview environment where a candidate can request interviews with multiple interviewers, track approval status, sit for rounds, receive manual scores across standardized rubrics (0 to 100) with detailed written remarks, and observe quantifiable improvement over time. Concurrently, interviewers manage request pipelines, review question banks, enter granular evaluations, and track interview metrics.

Importantly, this is an **organic core Java system** without AI automation, web frameworks, or third-party UI layers.

---

## 2. Problem Statement
In traditional campus placement and recruitment training setups:
1. **Lack of Multi-Round Progress Visibility**: Candidates attend isolated mock interviews without a centralized historical record comparing performance across rounds.
2. **Subjective & Unstandardized Feedback**: Evaluations are often unstructured notes rather than rubric-driven competency scoring.
3. **Double-Booking & Concurrency Clashes**: Manual scheduling often results in conflicting interview slots.
4. **Disconnect with Campus Placement Eligibility**: Mock interviews operate in isolation from actual placement drives and academic eligibility criteria.

---

## 3. Project Objectives
- Build a clean, menu-driven console application adhering strictly to OOP principles.
- Implement a two-sided workflow connecting multiple candidates with multiple interviewers.
- Calculate performance improvement metrics (overall average, highest/lowest scores, growth percentage from first to latest round, and strongest/weakest competencies) from persistent MySQL records.
- Preserve academic placement entities (`Student`, `Company`, `PlacementDrive`, `Application`) and the `Eligible` interface.
- Demonstrate thread synchronization (`SlotManager` with atomic check-and-decrement), Producer–Consumer (`TaskQueue` with `wait()`/`notifyAll()`), and the Executor Framework (`Callable`/`Future`).
- Implement secure authentication with PBKDF2 with HMAC-SHA256 password hashing.

---

## 4. Scope & User Roles
The platform enforces role-based access control (RBAC) across three roles:

| Role | Responsibilities | Key Capabilities |
| :--- | :--- | :--- |
| **Candidate** | Student / Interviewee | Profile management, browse/search interviewers, request slots, view scheduled rounds, inspect feedback, track growth, apply to eligible placement drives. |
| **Interviewer** | Technical / HR Evaluator | Review pending requests, accept/reject slots, view scheduled interviews, browse question banks, record rubric scores (0-100) and written remarks. |
| **Admin** | System Administrator | User management (activate/deactivate), onboard interviewers, manage question banks, oversee placement drives, manage applications, view global analytics. |

---

## 5. Academic Milestones Coverage (35 Marks)

### Milestone 1: OOP, Strings, and Exception Handling (8 Marks)
- **Class Hierarchy**: `User` -> `Student` -> `Candidate`, `Interviewer`, `Admin`.
- **Academic Entities**: `Student`, `Company`, `PlacementDrive`, `Application`.
- **Interfaces**:
  - `Eligible`: Implemented by `PlacementDrive.isEligible(Student)`.
  - `Evaluatable`: Implemented by `Interview`.
- **String Handling**: Validation and normalization (`trim()`, `toLowerCase()`, regex email pattern, non-blank checks, `StringBuilder` for formatted reports).
- **Wrapper Classes**: Nullable `Double overallScore`, `Integer createdBy`.
- **Custom Exceptions**: `InvalidStudentException`, `DuplicateRecordException`, `InvalidEmailException`, `InterviewNotFoundException`, `InvalidScoreException`, `UnauthorizedAccessException`, `InterviewBookingException`.

### Milestone 2: Java Collections & Record Management (6 Marks)
- **ArrayList**: Dynamic list manipulation for candidates, interviewers, and interviews.
- **HashMap**: Fast $O(1)$ lookups (`userId -> User`, `candidateId -> Candidate`, `interviewId -> Interview`).
- **HashSet**: Duplicate registration prevention (email addresses, unique student IDs).
- **Comparator Sorting**: Candidates by CGPA (descending), Candidates by Name (alphabetical), Interviews by Date (chronological).
- **Iterators**: Traversal and safe removal with `iterator.remove()`.
- **Analytics Feature**: Top 5 Candidates ranked by average interview score.

### Milestone 3: Multithreading & Synchronization (7 Marks)
- Shared `SlotManager` monitoring finite interview capacity.
- `synchronized` critical section enforcing atomic check-and-decrement.
- Concurrency simulation: 5 concurrent threads competing for 2 slots.
- Explicit lifecycle control with `start()`, `join()`, and thread identification via `Thread.currentThread().getName()`.

### Milestone 4: Producer–Consumer & Executor Framework (6 Marks)
- Bounded `TaskQueue` handling asynchronous interview tasks.
- Classical synchronization with `wait()` in a `while` loop and `notifyAll()`.
- `ExecutorService` fixed thread pool executing `Callable` analytics tasks.
- `Future.get()` for retrieving results and clean `shutdown()` / `awaitTermination()`.

### Milestone 5: JDBC & MySQL Relational Database (8 Marks)
- Third Normal Form (3NF) relational schema with 11 tables.
- Robust `DBConnection` reading local `database.properties`.
- PreparedStatement for 100% of queries to eliminate SQL Injection.
- Atomic transactions with `setAutoCommit(false)`, `commit()`, and `rollback()` for feedback and score persistence.
