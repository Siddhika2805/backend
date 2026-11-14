package com.interviewhub.model;

import java.sql.Timestamp;

/**
 * Academic Entity: Application
 * Represents a student candidate's application to a placement drive.
 */
public class Application {

    private int applicationId;
    private int candidateId;
    private String candidateName;
    private int driveId;
    private String driveName;
    private Timestamp applicationDate;
    private String applicationStatus; // APPLIED, SHORTLISTED, REJECTED, SELECTED

    public Application() {
    }

    public Application(int applicationId, int candidateId, int driveId,
                       Timestamp applicationDate, String applicationStatus) {
        this.applicationId = applicationId;
        this.candidateId = candidateId;
        this.driveId = driveId;
        this.applicationDate = applicationDate;
        this.applicationStatus = applicationStatus;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public int getDriveId() {
        return driveId;
    }

    public void setDriveId(int driveId) {
        this.driveId = driveId;
    }

    public String getDriveName() {
        return driveName;
    }

    public void setDriveName(String driveName) {
        this.driveName = driveName;
    }

    public Timestamp getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Timestamp applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    @Override
    public String toString() {
        return String.format("Application [ID: %d | Candidate: %s | Drive: %s | Status: %s | Date: %s]",
                applicationId, candidateName != null ? candidateName : String.valueOf(candidateId),
                driveName != null ? driveName : String.valueOf(driveId), applicationStatus, applicationDate);
    }
}
