package com.interviewhub.model;

/**
 * Interviewer Statistics Model
 * Tracks interviews conducted, completion rates, and scoring patterns for interviewers.
 */
public class InterviewerStatistics {

    private int interviewerId;
    private String interviewerName;
    private int totalConducted;
    private int totalCompleted;
    private int totalCancelled;
    private double averageCandidateScore;
    private int uniqueCandidatesInterviewed;

    public InterviewerStatistics() {
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

    public int getTotalConducted() {
        return totalConducted;
    }

    public void setTotalConducted(int totalConducted) {
        this.totalConducted = totalConducted;
    }

    public int getTotalCompleted() {
        return totalCompleted;
    }

    public void setTotalCompleted(int totalCompleted) {
        this.totalCompleted = totalCompleted;
    }

    public int getTotalCancelled() {
        return totalCancelled;
    }

    public void setTotalCancelled(int totalCancelled) {
        this.totalCancelled = totalCancelled;
    }

    public double getAverageCandidateScore() {
        return averageCandidateScore;
    }

    public void setAverageCandidateScore(double averageCandidateScore) {
        this.averageCandidateScore = averageCandidateScore;
    }

    public int getUniqueCandidatesInterviewed() {
        return uniqueCandidatesInterviewed;
    }

    public void setUniqueCandidatesInterviewed(int uniqueCandidatesInterviewed) {
        this.uniqueCandidatesInterviewed = uniqueCandidatesInterviewed;
    }

    public String toFormattedReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("==============================================================\n");
        sb.append(String.format("INTERVIEWER STATISTICS: %s (ID: %d)\n",
                interviewerName != null ? interviewerName : "Interviewer", interviewerId));
        sb.append("==============================================================\n");
        sb.append(String.format("Total Assigned / Conducted : %d\n", totalConducted));
        sb.append(String.format("Total Successfully Completed: %d\n", totalCompleted));
        sb.append(String.format("Total Cancelled Interviews : %d\n", totalCancelled));
        sb.append(String.format("Unique Candidates Evaluated: %d\n", uniqueCandidatesInterviewed));
        if (totalCompleted > 0) {
            sb.append(String.format("Average Candidate Score    : %.2f / 100.00\n", averageCandidateScore));
        } else {
            sb.append("Average Candidate Score    : N/A (No completed evaluations yet)\n");
        }
        sb.append("==============================================================\n");
        return sb.toString();
    }
}
