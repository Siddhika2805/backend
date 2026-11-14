# InterviewHub Viva & Assessment Preparation Guide
*Tailored for 2nd Year Computer Science Engineering Students*

---

## 1. Object-Oriented Programming (OOP) Questions

### Q1: What is the inheritance hierarchy in InterviewHub and why is it designed this way?
**Answer**:
The hierarchy begins with the abstract base class `User`, which holds common authentication and identity state (`userId`, `name`, `email`, `passwordHash`, `status`). 
- `Student` extends `User` to add college academic attributes (`studentId`, `department`, `cgpa`, `graduationYear`).
- `Candidate` extends `Student` to represent an active interviewee on the platform with a chosen `specialization`. This reflects the real world: every candidate is a student, and every student is a user.
- `Interviewer` and `Admin` extend `User` directly with their respective professional or administrative attributes.

### Q2: What is the role of the `Eligible` interface?
**Answer**:
`Eligible` specifies a contract with the single method `boolean isEligible(Student student)`. It is implemented by `PlacementDrive`. When a candidate applies to a placement drive, the drive evaluates whether the candidate meets the minimum CGPA and belongs to the eligible academic department. This demonstrates decoupling: eligibility logic resides inside the drive without hardcoding student checks into the controller.

### Q3: What is the difference between Method Overloading and Method Overriding in this project?
**Answer**:
- **Method Overriding (Runtime Polymorphism)**: The abstract method `getRoleDetails()` in `User` is overridden by `Student`, `Candidate`, `Interviewer`, and `Admin` to return custom details specific to that subclass.
- **Method Overloading (Compile-time Polymorphism)**: Model classes feature multiple constructors (e.g. no-arg default constructor, full constructor with database ID, and creation constructor without generated ID).

---

## 2. Strings, Wrapper Classes & Exceptions

### Q4: Why did you use `StringBuilder` instead of regular String concatenation (`+`) for reports?
**Answer**:
In Java, `String` objects are **immutable**. Concatenating strings in a loop creates a new `String` object in memory on every iteration, leading to excessive garbage collection overhead. `StringBuilder` is **mutable**, allowing strings to be appended to an internal resizable character array without creating intermediate garbage objects.

### Q5: Why are wrapper classes like `Double` and `Integer` used instead of primitives `double` and `int` in models?
**Answer**:
Primitives cannot be `null`. However, database columns such as `overall_score` in the `interviews` table or `created_by` in `questions` can be `NULL` (e.g. an interview that has been scheduled but not yet evaluated). Using `Double` and `Integer` allows our Java objects to accurately mirror database `NULL` values.

### Q6: What custom exceptions did you create and how do they benefit the project?
**Answer**:
We created domain-specific checked exceptions:
- `InvalidEmailException`: Thrown when email formatting fails or is blank.
- `DuplicateRecordException`: Thrown when a duplicate email or student ID is registered.
- `InvalidScoreException`: Thrown when evaluation scores are outside 0.00 to 100.00.
- `InterviewBookingException`: Thrown on slot scheduling conflicts or date violations.
- `UnauthorizedAccessException`: Thrown on invalid login or attempts to view unauthorized data.
Custom exceptions clarify why an operation failed and allow the service layer to return user-friendly messages without crashing the application.

---

## 3. Java Collections

### Q7: Why did you use both `ArrayList` and `HashMap` in `CollectionManager`?
**Answer**:
- `ArrayList`: Maintains an ordered sequence of records, supports dynamic resizing, and allows sorting using `Comparator`.
- `HashMap`: Provides average $O(1)$ constant-time lookup by ID (e.g. looking up a Candidate by their `candidateId` without iterating through the entire list).

### Q8: How does `HashSet` prevent duplicates?
**Answer**:
`HashSet` is backed by a `HashMap`. When adding an element, it hashes the element and checks for equality using `hashCode()` and `equals()`. We use a `HashSet<String>` for normalized email addresses to instantly detect duplicate registrations before making expensive database calls.

### Q9: How is Comparator used for sorting?
**Answer**:
We implement `Comparator<Candidate>` to sort candidates by CGPA descending (`Double.compare(c2.getCgpa(), c1.getCgpa())`) and by name alphabetically (`String.CASE_INSENSITIVE_ORDER`). Comparators separate sorting logic from the entity itself, allowing multiple independent sorting strategies.

---

## 4. Multithreading & Synchronization

### Q10: What is a Race Condition and how did you prevent it in `SlotManager`?
**Answer**:
A **race condition** occurs when two or more concurrent threads access shared mutable data (e.g. `availableSlots`) simultaneously, and the outcome depends on the timing of execution. If two threads check `if (availableSlots > 0)` at the same time, both might decrement the counter, causing negative slots or overbooking.
We prevented this by declaring `reserveSlot()` as `synchronized`. This acquires the intrinsic lock (monitor) of the `SlotManager` instance, ensuring only one thread can execute the check-and-decrement critical section at any given moment.

### Q11: What is the difference between `thread.start()` and `thread.run()`?
**Answer**:
- `thread.start()`: Instructs the JVM and the operating system to create a new thread of execution with its own call stack, which then calls `run()` asynchronously.
- `thread.run()`: Merely executes the method synchronously on the **current caller thread**, without any concurrency.

### Q12: Why do we call `thread.join()`?
**Answer**:
`join()` pauses the current thread (usually the main thread) until the target thread has completed execution. This ensures that the simulation summary is printed only after all concurrent worker threads have finished.

---

## 5. Producer–Consumer & Executor Framework

### Q13: Why is `wait()` placed inside a `while` loop rather than an `if` condition?
**Answer**:
Java threads are vulnerable to **spurious wakeups**, where a waiting thread can wake up without any `notify()` call. Furthermore, in a multi-consumer setup, another consumer might wake up first and consume the item. Putting `wait()` inside a `while (queue.isEmpty())` loop ensures that upon waking, the thread re-evaluates the condition before proceeding.

### Q14: What is the difference between `Runnable` and `Callable`?
**Answer**:
- `Runnable`: Has `public void run()`. It cannot return a result and cannot throw checked exceptions.
- `Callable<V>`: Has `public V call() throws Exception`. It returns a calculated result of type `V` and can throw checked exceptions.

### Q15: What is a `Future` and what does `future.get()` do?
**Answer**:
A `Future<V>` represents the pending result of an asynchronous task submitted to an `ExecutorService`. Calling `future.get()` blocks the calling thread until the worker thread finishes computing and returns the value.

---

## 6. JDBC & Database Architecture

### Q16: Why is `PreparedStatement` preferred over `Statement`?
**Answer**:
1. **Security**: `PreparedStatement` pre-compiles the SQL query structure and parameterizes values, preventing **SQL Injection**.
2. **Performance**: Pre-compiled statements can be cached and reused by the MySQL database engine.
3. **Type Handling**: Handles dates, nullables, and strings cleanly without messy manual string concatenation and escaping.

### Q17: How did you implement database transactions in JDBC?
**Answer**:
In `FeedbackDAO.submitEvaluationTransaction()`:
1. We set `conn.setAutoCommit(false)` to disable automatic per-statement commits.
2. We execute: insert feedback, insert skill scores, and update interview status to `COMPLETED`.
3. If all statements succeed, we call `conn.commit()`.
4. If an exception occurs, we call `conn.rollback()` in the `catch` block to revert all changes, ensuring the database never holds half-written evaluations.
5. In the `finally` block, we restore `conn.setAutoCommit(true)`.

### Q18: How is the improvement percentage calculated?
**Answer**:
$$\text{Improvement Percentage} = \frac{\text{Latest Score} - \text{First Score}}{\text{First Score}} \times 100$$
To prevent `ArithmeticException` or `NaN` from division by zero, the method checks `if (firstScore > 0.0)`. If the initial score was 0.0, an informative baseline message is displayed instead.
