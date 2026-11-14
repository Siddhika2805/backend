# Class Architecture & Object-Oriented Design

## 1. Class Hierarchy Diagram (Mermaid)

```mermaid
classDiagram
    direction TB

    class Eligible {
        <<interface>>
        +isEligible(Student student) boolean
    }

    class Evaluatable {
        <<interface>>
        +getOverallScore() Double
        +isEvaluated() boolean
        +generateEvaluationSummary() String
    }

    class User {
        <<abstract>>
        -int userId
        -String name
        -String email
        -String passwordHash
        -String passwordSalt
        -String role
        -String status
        -Timestamp createdAt
        +getRoleDetails()* String
        +isActive() boolean
    }

    class Student {
        -String studentId
        -String department
        -double cgpa
        -int graduationYear
        +getRoleDetails() String
    }

    class Candidate {
        -int candidateId
        -String specialization
        +getRoleDetails() String
    }

    class Interviewer {
        -int interviewerId
        -String expertise
        -int experienceYears
        -String specialization
        -String organization
        +getRoleDetails() String
    }

    class Admin {
        +getRoleDetails() String
    }

    class Company {
        -int companyId
        -String companyName
        -String industry
        -String location
    }

    class PlacementDrive {
        -int driveId
        -int companyId
        -String companyName
        -String driveName
        -double eligibilityCgpa
        -String eligibleDepartment
        -LocalDate applicationDeadline
        -String status
        +isEligible(Student student) boolean
    }

    class Application {
        -int applicationId
        -int candidateId
        -int driveId
        -Timestamp applicationDate
        -String applicationStatus
    }

    class Interview {
        -int interviewId
        -int candidateId
        -int interviewerId
        -Integer requestId
        -String interviewType
        -LocalDate interviewDate
        -LocalTime interviewTime
        -String interviewStatus
        -Double overallScore
        -Timestamp createdAt
        +getOverallScore() Double
        +isEvaluated() boolean
        +generateEvaluationSummary() String
    }

    class InterviewRequest {
        -int requestId
        -int candidateId
        -int interviewerId
        -String interviewType
        -LocalDate preferredDate
        -LocalTime preferredTime
        -String requestStatus
        -Timestamp createdAt
    }

    class InterviewFeedback {
        -int feedbackId
        -int interviewId
        -int interviewerId
        -String feedbackText
        -double overallScore
        -Timestamp submittedAt
        -List~SkillScore~ skillScores
        +toFormattedReport() String
    }

    class SkillScore {
        -int skillScoreId
        -int feedbackId
        -String skillName
        -double score
    }

    class Question {
        -int questionId
        -String questionText
        -String category
        -String difficulty
        -String specialization
        -Integer createdBy
    }

    User <|-- Student : Inheritance
    Student <|-- Candidate : Inheritance
    User <|-- Interviewer : Inheritance
    User <|-- Admin : Inheritance

    Eligible <|.. PlacementDrive : Implements
    Evaluatable <|.. Interview : Implements

    PlacementDrive --> Company : Associated With
    Application --> Candidate : Submitted By
    Application --> PlacementDrive : Targets
    Interview --> Candidate : Interviewee
    Interview --> Interviewer : Interviewer
    InterviewFeedback --> Interview : Evaluates
    InterviewFeedback *-- SkillScore : Contains
```

---

## 2. Object-Oriented Design Rationale

### A. Encapsulation
Every model class hides its internal fields using the `private` access modifier. Access to and modification of state is strictly governed by public getters and setters equipped with validation where appropriate.

### B. Meaningful Inheritance
The hierarchy follows real-world domain relationships:
- `User`: Encapsulates system identity, credentials, role, and active status.
- `Student`: Extends `User` to add academic attributes (`studentId`, `department`, `cgpa`, `graduationYear`).
- `Candidate`: Extends `Student` to represent an active participant in mock interviews, carrying interview specialization while inheriting academic eligibility.
- `Interviewer` and `Admin`: Extend `User` directly, preserving modular role attributes.

### C. Polymorphism & Abstraction
The abstract method `getRoleDetails()` in `User` is polymorphically overridden in `Student`, `Candidate`, `Interviewer`, and `Admin` to provide tailored profile descriptions depending on the concrete instance at runtime.

### D. Interfaces
- **`Eligible`**: Encapsulates the contract for evaluating candidate criteria against placement drive rules. `PlacementDrive` implements `isEligible(Student student)` to verify whether a student's CGPA and department satisfy minimum requirements.
- **`Evaluatable`**: Decouples evaluation reporting from concrete classes. `Interview` implements `Evaluatable` to provide uniform access to scores and report generation.
