package com.interviewhub.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Interview Request Model
 * Tracks candidate booking requests before they are confirmed by interviewers.
 */
public class InterviewRequest {

    private int requestId;
    private int candidateId;
    private String candidateName;
    private int interviewerId;
    private String interviewerName;
    private String interviewType; // TECHNICAL, HR
    private LocalDate preferredDate;
    private LocalTime preferredTime;
    private String requestStatus; // PENDING, ACCEPTED, REJECTED, CANCELLED
    private Timestamp createdAt;

    public InterviewRequest() {
    }

    public InterviewRequest(int requestId, int candidateId, int interviewerId, String interviewType,
                            LocalDate preferredDate, LocalTime preferredTime, String requestStatus, Timestamp createdAt) {
        this.requestId = requestId;
        this.candidateId = candidateId;
        this.interviewerId = interviewerId;
        this.interviewType = interviewType;
        this.preferredDate = preferredDate;
        this.preferredTime = preferredTime;
        this.requestStatus = requestStatus;
        this.createdAt = createdAt;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
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

    public int getInterviewerId() {
        return interviewerId;
    }

    public void setInterviewerId(int interviewerId) {
        this.interviewerId = interviewerId;
    }

    public String getInterviewerName() {
        return interviewerName;
    }

    public void setInterviewerName(String interviewerName) {
        this.interviewerName = interviewerName;
    }

    public String getInterviewType() {
        return interviewType;
    }

    public void setInterviewType(String interviewType) {
        this.interviewType = interviewType;
    }

    public LocalDate getPreferredDate() {
        return preferredDate;
    }

    public void setPreferredDate(LocalDate preferredDate) {
        this.preferredDate = preferredDate;
    }

    public LocalTime getPreferredTime() {
        return preferredTime;
    }

    public void setPreferredTime(LocalTime preferredTime) {
        this.preferredTime = preferredTime;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("Request #%d [Candidate: %s | Interviewer: %s | Type: %s | Date: %s %s | Status: %s]",
                requestId, candidateName != null ? candidateName : String.valueOf(candidateId),
                interviewerName != null ? interviewerName : String.valueOf(interviewerId),
                interviewType, preferredDate, preferredTime, requestStatus);
    }
}
