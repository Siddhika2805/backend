package com.interviewhub.model;

import java.sql.Timestamp;

/**
 * Base User Model
 * Demonstrates OOP Encapsulation, Abstraction, and Polymorphic method overriding.
 */
public abstract class User {

    private int userId;
    private String name;
    private String email;
    private String passwordHash;
    private String passwordSalt;
    private String role; // ADMIN, CANDIDATE, INTERVIEWER
    private String status; // ACTIVE, INACTIVE
    private Timestamp createdAt;

    public User() {
    }

    public User(int userId, String name, String email, String passwordHash, String passwordSalt, String role, String status, Timestamp createdAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
    }

    public User(String name, String email, String passwordHash, String passwordSalt, String role) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.role = role;
        this.status = "ACTIVE";
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(this.status);
    }

    /**
     * Polymorphic method to display role-specific profile details.
     */
    public abstract String getRoleDetails();

    @Override
    public String toString() {
        return String.format("User [ID: %d | Name: %s | Email: %s | Role: %s | Status: %s]",
                userId, name, email, role, status);
    }
}
