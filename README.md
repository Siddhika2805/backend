## InterviewHub – Multi-Interviewer Interview Management & Performance Tracking System

> **A Pure Core Java 17 + JDBC + MySQL Console-Based Academic Application**  
> Designed and architected for 2nd Year Computer Science Engineering Evaluation (35 Marks across 5 Milestones).

---

## 📌 Project Overview
**InterviewHub** is a two-sided interview coordination, evaluation, and performance analytics platform that connects candidates with multiple interviewers. Candidates request interviews across Technical and HR categories, attend scheduled rounds, receive rubric-based scoring (0–100) and written qualitative feedback, and track performance progression over time. Concurrently, interviewers manage pending requests, conduct interviews using standardized question banks, and evaluate candidate competencies. System governance and campus placement drive eligibility are coordinated by the Administrator.

---

## 🛠️ Technology Stack
- **Language**: Java 17 (Pure Core Java, Object-Oriented Architecture)
- **Database**: MySQL 8.0+
- **Connectivity**: JDBC (MySQL Connector/J 8.3.0)
- **Build & Dependency Management**: Apache Maven 3.9+
- **Testing**: JUnit 5 (JUnit Jupiter 5.10.2)
- **Interface**: Menu-driven Interactive Console Application (Scanner over System.in)
- **Security**: PBKDF2 with HMAC-SHA256 Password Hashing (Java Cryptography Architecture)

---

## 📂 Project Architecture

```
InterviewHub/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/interviewhub/
│   │   │       ├── Main.java                          # Application Entry Point
│   │   │       ├── collections/
│   │   │       │   └── CollectionManager.java         # Milestone 2 Collections
│   │   │       ├── dao/
│   │   │       │   ├── ApplicationDAO.java
│   │   │       │   ├── CandidateDAO.java
│   │   │       │   ├── CompanyDAO.java
│   │   │       │   ├── FeedbackDAO.java               # JDBC Transactions & Analytics
│   │   │       │   ├── InterviewDAO.java
│   │   │       │   ├── InterviewRequestDAO.java
│   │   │       │   ├── InterviewerDAO.java
│   │   │       │   ├── PlacementDriveDAO.java
│   │   │       │   ├── QuestionDAO.java
│   │   │       │   └── UserDAO.java
│   │   │       ├── database/
│   │   │       │   └── DBConnection.java              # JDBC Connection Manager
│   │   │       ├── exceptions/                        # Domain Custom Exceptions
│   │   │       │   ├── DuplicateRecordException.java
│   │   │       │   ├── InterviewBookingException.java
│   │   │       │   ├── InterviewNotFoundException.java
│   │   │       │   ├── InvalidEmailException.java
│   │   │       │   ├── InvalidScoreException.java
│   │   │       │   ├── InvalidStudentException.java
│   │   │       │   └── UnauthorizedAccessException.java
│   │   │       ├── interfaces/
│   │   │       │   ├── Eligible.java                  # Academic Rubric Interface
│   │   │       │   └── Evaluatable.java               # Evaluation Reporting Interface
│   │   │       ├── model/                             # Encapsulated Domain Models
│   │   │       │   ├── Admin.java
│   │   │       │   ├── Application.java
│   │   │       │   ├── Candidate.java
│   │   │       │   ├── CandidateStatistics.java
│   │   │       │   ├── Company.java
│   │   │       │   ├── Interview.java
│   │   │       │   ├── InterviewFeedback.java
│   │   │       │   ├── InterviewRequest.java
│   │   │       │   ├── Interviewer.java
│   │   │       │   ├── InterviewerStatistics.java
│   │   │       │   ├── PlacementDrive.java
│   │   │       │   ├── Question.java
│   │   │       │   ├── SkillScore.java
│   │   │       │   ├── Student.java
│   │   │       │   ├── SystemStatistics.java
│   │   │       │   └── User.java
│   │   │       ├── service/                           # Business Logic Layer
│   │   │       │   ├── AdminService.java
│   │   │       │   ├── AuthService.java
│   │   │       │   ├── CandidateService.java
│   │   │       │   └── InterviewerService.java
│   │   │       ├── threads/                           # Milestones 3 & 4
│   │   │       │   ├── AsyncPipelineManager.java      # Producer-Consumer & Executor
│   │   │       │   ├── InterviewReservationTask.java
│   │   │       │   ├── InterviewTask.java
│   │   │       │   ├── SlotManager.java               # Synchronized Critical Section
│   │   │       │   ├── TaskConsumer.java
│   │   │       │   ├── TaskProducer.java
│   │   │       │   └── TaskQueue.java
│   │   │       ├── ui/
│   │   │       │   └── ConsoleUI.java                 # Menu-driven Console Interface
│   │   │       └── utils/
│   │   │           ├── InputValidator.java
│   │   │           └── PasswordUtil.java              # PBKDF2 Hashing
│   │   └── resources/
│   │       ├── database.properties.example            # Credentials Template
│   │       └── database.properties                    # Local Config (Git-ignored)
│   └── test/
│       └── java/com/interviewhub/
│           ├── collections/CollectionsTest.java
│           ├── oop/ModelValidationTest.java
│           └── threads/
│               ├── AsyncPipelineTest.java
│               └── ConcurrencyTest.java
├── database/
│   ├── schema.sql                                     # Complete 3NF DDL Script
│   └── sample_data.sql                                # Realistic Seed Records
├── documentation/
│   ├── project_overview.md                            # Comprehensive Architecture
│   ├── class_diagram.md                               # Mermaid Diagrams & OOP
│   ├── database_design.md                             # ER Diagrams & Schemas
│   ├── testing_evidence.md                            # Test Runs & Analysis
│   └── viva_preparation.md                           # Beginner-Friendly Viva Q&A
├── pom.xml                                            # Maven Configuration
├── mvnw.cmd                                           # Maven Wrapper for Windows
├── .gitignore
└── README.md
```

---

## 🎓 Academic Assessment Milestones (35 Marks)

| Milestone | Topics Covered | Marks | Implementation |
| :--- | :--- | :---: | :--- |
| **Milestone 1** | OOP, Inheritance, Interfaces, Strings, Custom Exceptions | 8 | `User` -> `Student` -> `Candidate`, `Eligible` in `PlacementDrive`, `Evaluatable` in `Interview`, custom exceptions, regex & `StringBuilder`. |
| **Milestone 2** | Java Collections & Record Management | 6 | `ArrayList`, `HashMap` fast lookup, `HashSet` duplicate prevention, `Comparator` sorting, Iterator removal, Top 5 ranking. |
| **Milestone 3** | Multithreading & Synchronization | 7 | `SlotManager` synchronized critical section, atomic check-and-decrement, 5 threads competing for 2 slots test. |
| **Milestone 4** | Producer-Consumer & Executor Framework | 6 | `TaskQueue` with `wait()` in `while` loop & `notifyAll()`, `ExecutorService` fixed thread pool with `Callable`/`Future`. |
| **Milestone 5** | JDBC & MySQL Relational Database | 8 | 11 relational tables, DAO pattern, 100% `PreparedStatement`, atomic JDBC transaction in `FeedbackDAO`. |

---

## 🚀 Setup & Execution Guide

### 1. Database Configuration
1. Open MySQL Command Line or MySQL Workbench:
   ```sql
   SOURCE database/schema.sql;
   SOURCE database/sample_data.sql;
   ```
2. Create `src/main/resources/database.properties` (or copy from `.example`):
   ```properties
   db.url=jdbc:mysql://localhost:3306/interviewhub_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
   db.user=root
   db.password=YOUR_MYSQL_PASSWORD_HERE
   ```

### 2. How to Run the Application in Terminal

You can run the application in any terminal (PowerShell, Command Prompt, or VS Code Terminal) using any of the following methods:

#### ⭐ Method A: One-Command Runner (Easiest)
In **PowerShell** or **VS Code Terminal**:
```powershell
.\run.bat
# or
.\run.ps1
```
In **Command Prompt (CMD)**:
```cmd
run.bat
```

#### ⭐ Method B: Pure Java (No Maven Required)
```powershell
java -jar InterviewHub.jar
```

#### ⭐ Method C: Using Maven
```powershell
# In PowerShell:
.\mvnw.ps1 exec:java
# or
.\mvnw.cmd exec:java

# In Command Prompt:
mvnw.cmd exec:java
# or
mvn exec:java
```

### 3. Run Automated JUnit Test Suite (13/13 tests)
```powershell
.\mvnw.ps1 test
# or
.\mvnw.cmd test
```

---

## 🔑 Default Credentials (from `sample_data.sql`)

| Role | Email | Password | Details |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin@interviewhub.com` | `Admin@123` | System Administrator |
| **Interviewer** | `sarah.swe@techcorp.com` | `Password@123` | Dr. Sarah Jenkins (TechCorp, SWE) |
| **Interviewer** | `david.hr@globalhire.com` | `Password@123` | David Miller (GlobalHire, HR) |
| **Candidate** | `rahul.sharma@college.edu` | `Password@123` | Rahul Sharma (STU202401, CS) |
| **Candidate** | `ananya.patel@college.edu` | `Password@123` | Ananya Patel (STU202402, IT) |
