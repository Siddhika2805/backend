package com.interviewhub.model;

import java.sql.Timestamp;

/**
 * Candidate Model (extends Student extends User)
 * Represents an active interviewee in the InterviewHub system.
 */
public class Candidate extends Student {

    private int candidateId;
    private String specialization;

    public Candidate() {
        super();
    }

    public Candidate(int userId, String name, String email, String passwordHash, String passwordSalt,
                     String status, Timestamp createdAt, int candidateId, String studentId,
                     String department, double cgpa, int graduationYear, String specialization) {
        super(userId, name, email, passwordHash, passwordSalt, status, createdAt, studentId, department, cgpa, graduationYear);
        this.candidateId = candidateId;
        this.specialization = specialization;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public String getRoleDetails() {
        return String.format("Candidate ID: %d | Student ID: %s | Department: %s | Specialization: %s | CGPA: %.2f | Year: %d",
                candidateId, getStudentId(), getDepartment(), specialization, getCgpa(), getGraduationYear());
    }

    @Override
    public String toString() {
        return String.format("Candidate [ID: %d | Name: %s | Dept: %s | CGPA: %.2f | Specialization: %s]",
                candidateId, getName(), getDepartment(), getCgpa(), specialization);
    }
}
