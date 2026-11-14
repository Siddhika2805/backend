package com.interviewhub.model;

import java.sql.Timestamp;

/**
 * Interviewer Model (extends User)
 * Represents an interviewer conducting technical or HR rounds.
 */
public class Interviewer extends User {

    private int interviewerId;
    private String expertise;
    private int experienceYears;
    private String specialization;
    private String organization;

    public Interviewer() {
        super();
        setRole("INTERVIEWER");
    }

    public Interviewer(int userId, String name, String email, String passwordHash, String passwordSalt,
                       String status, Timestamp createdAt, int interviewerId, String expertise,
                       int experienceYears, String specialization, String organization) {
        super(userId, name, email, passwordHash, passwordSalt, "INTERVIEWER", status, createdAt);
        this.interviewerId = interviewerId;
        this.expertise = expertise;
        this.experienceYears = experienceYears;
        this.specialization = specialization;
        this.organization = organization;
    }

    public int getInterviewerId() {
        return interviewerId;
    }

    public void setInterviewerId(int interviewerId) {
        this.interviewerId = interviewerId;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    @Override
    public String getRoleDetails() {
        return String.format("Interviewer ID: %d | Organization: %s | Specialization: %s | Experience: %d yrs | Expertise: %s",
                interviewerId, organization, specialization, experienceYears, expertise);
    }

    @Override
    public String toString() {
        return String.format("Interviewer [ID: %d | Name: %s | Org: %s | Spec: %s | Exp: %d yrs]",
                interviewerId, getName(), organization, specialization, experienceYears);
    }
}
