package com.interviewhub.model;

/**
 * System Statistics Model
 * Provides high-level administrative insights into overall platform usage and performance.
 */
public class SystemStatistics {

    private int totalUsers;
    private int totalCandidates;
    private int totalInterviewers;
    private int totalInterviews;
    private int totalCompletedInterviews;
    private int totalScheduledInterviews;
    private int totalCancelledInterviews;
    private double overallAverageScore;
    private int totalPlacementDrives;
    private int totalApplications;
    private int totalQuestions;

    public SystemStatistics() {
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    public int getTotalCandidates() {
        return totalCandidates;
    }

    public void setTotalCandidates(int totalCandidates) {
        this.totalCandidates = totalCandidates;
    }

    public int getTotalInterviewers() {
        return totalInterviewers;
    }

    public void setTotalInterviewers(int totalInterviewers) {
        this.totalInterviewers = totalInterviewers;
    }

    public int getTotalInterviews() {
        return totalInterviews;
    }

    public void setTotalInterviews(int totalInterviews) {
        this.totalInterviews = totalInterviews;
    }

    public int getTotalCompletedInterviews() {
        return totalCompletedInterviews;
    }

    public void setTotalCompletedInterviews(int totalCompletedInterviews) {
        this.totalCompletedInterviews = totalCompletedInterviews;
    }

    public int getTotalScheduledInterviews() {
        return totalScheduledInterviews;
    }

    public void setTotalScheduledInterviews(int totalScheduledInterviews) {
        this.totalScheduledInterviews = totalScheduledInterviews;
    }

    public int getTotalCancelledInterviews() {
        return totalCancelledInterviews;
    }

    public void setTotalCancelledInterviews(int totalCancelledInterviews) {
        this.totalCancelledInterviews = totalCancelledInterviews;
    }

    public double getOverallAverageScore() {
        return overallAverageScore;
    }

    public void setOverallAverageScore(double overallAverageScore) {
        this.overallAverageScore = overallAverageScore;
    }

    public int getTotalPlacementDrives() {
        return totalPlacementDrives;
    }

    public void setTotalPlacementDrives(int totalPlacementDrives) {
        this.totalPlacementDrives = totalPlacementDrives;
    }

    public int getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(int totalApplications) {
        this.totalApplications = totalApplications;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String toFormattedReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("==============================================================\n");
        sb.append("                INTERVIEWHUB SYSTEM OVERVIEW                  \n");
        sb.append("==============================================================\n");
        sb.append(String.format("Total Registered Users     : %d\n", totalUsers));
        sb.append(String.format("  - Candidates             : %d\n", totalCandidates));
        sb.append(String.format("  - Interviewers           : %d\n", totalInterviewers));
        sb.append("--------------------------------------------------------------\n");
        sb.append(String.format("Total Interviews Recorded  : %d\n", totalInterviews));
        sb.append(String.format("  - Completed Interviews   : %d\n", totalCompletedInterviews));
        sb.append(String.format("  - Scheduled (Upcoming)   : %d\n", totalScheduledInterviews));
        sb.append(String.format("  - Cancelled Interviews   : %d\n", totalCancelledInterviews));
        if (totalCompletedInterviews > 0) {
            sb.append(String.format("Platform Average Score     : %.2f / 100.00\n", overallAverageScore));
        } else {
            sb.append("Platform Average Score     : N/A\n");
        }
        sb.append("--------------------------------------------------------------\n");
        sb.append(String.format("Campus Placement Drives    : %d\n", totalPlacementDrives));
        sb.append(String.format("Total Student Applications : %d\n", totalApplications));
        sb.append(String.format("Interview Question Bank    : %d questions\n", totalQuestions));
        sb.append("==============================================================\n");
        return sb.toString();
    }
}
