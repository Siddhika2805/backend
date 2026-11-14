package com.interviewhub.model;

import com.interviewhub.interfaces.Evaluatable;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Interview Model (implements Evaluatable)
 * Central record connecting Candidates and Interviewers.
 * Demonstrates nullable wrapper Double, interface implementation, and StringBuilder reporting.
 */
public class Interview implements Evaluatable {

    private int interviewId;
    private int candidateId;
    private String candidateName;
    private int interviewerId;
    private String interviewerName;
    private Integer requestId;
    private String interviewType; // TECHNICAL, HR
    private LocalDate interviewDate;
    private LocalTime interviewTime;
    private String interviewStatus; // SCHEDULED, COMPLETED, CANCELLED
    private Double overallScore; // Nullable wrapper class
    private Timestamp createdAt;
    private InterviewFeedback feedback;

    public Interview() {
    }

    public Interview(int interviewId, int candidateId, int interviewerId, Integer requestId,
                     String interviewType, LocalDate interviewDate, LocalTime interviewTime,
                     String interviewStatus, Double overallScore, Timestamp createdAt) {
        this.interviewId = interviewId;
        this.candidateId = candidateId;
        this.interviewerId = interviewerId;
        this.requestId = requestId;
        this.interviewType = interviewType;
        this.interviewDate = interviewDate;
        this.interviewTime = interviewTime;
        this.interviewStatus = interviewStatus;
        this.overallScore = overallScore;
        this.createdAt = createdAt;
    }

    public int getInterviewId() {
        return interviewId;
    }

    public void setInterviewId(int interviewId) {
        this.interviewId = interviewId;
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

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getInterviewType() {
        return interviewType;
    }

    public void setInterviewType(String interviewType) {
        this.interviewType = interviewType;
    }

    public LocalDate getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(LocalDate interviewDate) {
        this.interviewDate = interviewDate;
    }

    public LocalTime getInterviewTime() {
        return interviewTime;
    }

    public void setInterviewTime(LocalTime interviewTime) {
        this.interviewTime = interviewTime;
    }

    public String getInterviewStatus() {
        return interviewStatus;
    }

    public void setInterviewStatus(String interviewStatus) {
        this.interviewStatus = interviewStatus;
    }

    @Override
    public Double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Double overallScore) {
        this.overallScore = overallScore;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public InterviewFeedback getFeedback() {
        return feedback;
    }

    public void setFeedback(InterviewFeedback feedback) {
        this.feedback = feedback;
    }

    @Override
    public boolean isEvaluated() {
        return "COMPLETED".equalsIgnoreCase(this.interviewStatus) && this.overallScore != null;
    }

    @Override
    public String generateEvaluationSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Interview #%d [%s Round]\n", interviewId, interviewType));
        sb.append(String.format("Candidate   : %s (ID: %d)\n",
                candidateName != null ? candidateName : "N/A", candidateId));
        sb.append(String.format("Interviewer : %s (ID: %d)\n",
                interviewerName != null ? interviewerName : "N/A", interviewerId));
        sb.append(String.format("Date & Time : %s %s\n", interviewDate, interviewTime));
        sb.append(String.format("Status      : %s\n", interviewStatus));
        if (overallScore != null) {
            sb.append(String.format("Score       : %.2f / 100.00\n", overallScore));
        } else {
            sb.append("Score       : Not Evaluated Yet\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("Interview #%d [Candidate: %s | Interviewer: %s | Type: %s | Date: %s %s | Status: %s | Score: %s]",
                interviewId, candidateName != null ? candidateName : String.valueOf(candidateId),
                interviewerName != null ? interviewerName : String.valueOf(interviewerId),
                interviewType, interviewDate, interviewTime, interviewStatus,
                overallScore != null ? String.format("%.2f", overallScore) : "N/A");
    }
}
