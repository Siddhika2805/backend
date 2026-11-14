package com.interviewhub.model;

import java.sql.Timestamp;

/**
 * Academic Entity: Student (extends User)
 * Represents an enrolled college student participating in academic & placement activities.
 */
public class Student extends User {

    private String studentId;
    private String department;
    private double cgpa;
    private int graduationYear;

    public Student() {
        super();
        setRole("CANDIDATE");
    }

    public Student(int userId, String name, String email, String passwordHash, String passwordSalt,
                   String status, Timestamp createdAt, String studentId, String department, double cgpa, int graduationYear) {
        super(userId, name, email, passwordHash, passwordSalt, "CANDIDATE", status, createdAt);
        this.studentId = studentId;
        this.department = department;
        this.cgpa = cgpa;
        this.graduationYear = graduationYear;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear;
    }

    @Override
    public String getRoleDetails() {
        return String.format("Student ID: %s | Department: %s | CGPA: %.2f | Grad Year: %d",
                studentId, department, cgpa, graduationYear);
    }
}
