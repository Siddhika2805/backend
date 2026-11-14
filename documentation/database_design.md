# Database Architecture & Relational Design

## 1. Entity-Relationship Diagram (Mermaid)

```mermaid
erDiagram
    USERS ||--o| CANDIDATES : "one-to-one (profile)"
    USERS ||--o| INTERVIEWERS : "one-to-one (profile)"
    USERS ||--o{ QUESTIONS : "creates"
    
    COMPANIES ||--o{ PLACEMENT_DRIVES : "sponsors"
    CANDIDATES ||--o{ APPLICATIONS : "submits"
    PLACEMENT_DRIVES ||--o{ APPLICATIONS : "receives"
    
    CANDIDATES ||--o{ INTERVIEW_REQUESTS : "requests"
    INTERVIEWERS ||--o{ INTERVIEW_REQUESTS : "receives"
    
    CANDIDATES ||--o{ INTERVIEWS : "attends"
    INTERVIEWERS ||--o{ INTERVIEWS : "conducts"
    INTERVIEW_REQUESTS ||--o| INTERVIEWS : "originates"
    
    INTERVIEWS ||--|| INTERVIEW_FEEDBACK : "has evaluation"
    INTERVIEW_FEEDBACK ||--|{ SKILL_SCORES : "contains rubric scores"

    USERS {
        int user_id PK
        string name
        string email UK
        string password_hash
        string password_salt
        string role
        string status
        timestamp created_at
    }

    CANDIDATES {
        int candidate_id PK
        int user_id FK
        string student_id UK
        string department
        decimal cgpa
        int graduation_year
        string specialization
    }

    INTERVIEWERS {
        int interviewer_id PK
        int user_id FK
        string expertise
        int experience_years
        string specialization
        string organization
    }

    COMPANIES {
        int company_id PK
        string company_name UK
        string industry
        string location
    }

    PLACEMENT_DRIVES {
        int drive_id PK
        int company_id FK
        string drive_name
        decimal eligibility_cgpa
        string eligible_department
        date application_deadline
        string status
    }

    APPLICATIONS {
        int application_id PK
        int candidate_id FK
        int drive_id FK
        timestamp application_date
        string application_status
    }

    INTERVIEW_REQUESTS {
        int request_id PK
        int candidate_id FK
        int interviewer_id FK
        string interview_type
        date preferred_date
        time preferred_time
        string request_status
        timestamp created_at
    }

    INTERVIEWS {
        int interview_id PK
        int candidate_id FK
        int interviewer_id FK
        int request_id FK
        string interview_type
        date interview_date
        time interview_time
        string interview_status
        decimal overall_score
        timestamp created_at
    }

    QUESTIONS {
        int question_id PK
        text question_text
        string category
        string difficulty
        string specialization
        int created_by FK
    }

    INTERVIEW_FEEDBACK {
        int feedback_id PK
        int interview_id FK
        int interviewer_id FK
        text feedback_text
        decimal overall_score
        timestamp submitted_at
    }

    SKILL_SCORES {
        int skill_score_id PK
        int feedback_id FK
        string skill_name
        decimal score
    }
```

---

## 2. Relational Schema Details & Normalization

The database adheres strictly to **Third Normal Form (3NF)**:
1. **1NF**: Every column contains atomic values, each table has a primary key, and repeating groups are eliminated.
2. **2NF**: All non-key attributes are fully functionally dependent on the entire primary key.
3. **3NF**: There are no transitive dependencies; non-key attributes depend only on the primary key.

### Key Constraints & Integrity Rules
- **Unique Constraints**:
  - `users(email)` ensures unique user accounts.
  - `candidates(student_id)` and `candidates(user_id)` prevent duplicate student profiles.
  - `applications(candidate_id, drive_id)` prevents redundant applications to the same drive.
  - `interviews(request_id)` ensures a one-to-one link from a request to an interview.
- **Check Constraints**:
  - `cgpa >= 0.00 AND cgpa <= 10.00`
  - `overall_score >= 0.00 AND overall_score <= 100.00`
  - `score >= 0.00 AND score <= 100.00` on individual skill rubrics.
- **Foreign Keys**:
  - Cascade deletes on user profiles ensure orphaned records are not left behind.
  - Set null rules on audit fields (e.g. `questions.created_by`) ensure content remains if an admin is deleted.

---

## 3. Database Transactions (ACID Compliance)
In `FeedbackDAO.submitEvaluationTransaction()`:
1. `conn.setAutoCommit(false)` initiates an atomic transaction block.
2. Step A: Insert `interview_feedback` row.
3. Step B: Insert batch of `skill_scores` using PreparedStatement batch execution.
4. Step C: Update `interviews` row with status `COMPLETED` and overall score.
5. `conn.commit()` commits all changes atomically.
6. If any step fails, `conn.rollback()` restores the database state, preventing partial evaluation writes.
