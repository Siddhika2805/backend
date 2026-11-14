package com.interviewhub.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Interview Feedback Model
 * Encapsulates interviewer remarks, overall score, and granular skill-wise evaluations.
 */
public class InterviewFeedback {

    private int feedbackId;
    private int interviewId;
    private int interviewerId;
    private String interviewerName;
    private String feedbackText;
    private double overallScore; // 0.00 to 100.00
    private Timestamp submittedAt;
    private List<SkillScore> skillScores = new ArrayList<>();

    public InterviewFeedback() {
    }

    public InterviewFeedback(int feedbackId, int interviewId, int interviewerId,
                             String feedbackText, double overallScore, Timestamp submittedAt) {
        this.feedbackId = feedbackId;
        this.interviewId = interviewId;
        this.interviewerId = interviewerId;
        this.feedbackText = feedbackText;
        this.overallScore = overallScore;
        this.submittedAt = submittedAt;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getInterviewId() {
        return interviewId;
    }

    public void setInterviewId(int interviewId) {
        this.interviewId = interviewId;
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

    public String getFeedbackText() {
        return feedbackText;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(double overallScore) {
        this.overallScore = overallScore;
    }

    public Timestamp getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Timestamp submittedAt) {
        this.submittedAt = submittedAt;
    }

    public List<SkillScore> getSkillScores() {
        return skillScores;
    }

    public void setSkillScores(List<SkillScore> skillScores) {
        this.skillScores = skillScores != null ? skillScores : new ArrayList<>();
    }

    public void addSkillScore(SkillScore score) {
        this.skillScores.add(score);
    }

    /**
     * Demonstrates StringBuilder for building structured evaluation feedback reports.
     */
    public String toFormattedReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("====================================================\n");
        sb.append(String.format("INTERVIEW EVALUATION REPORT (Feedback #%d)\n", feedbackId));
        sb.append("====================================================\n");
        sb.append(String.format("Evaluator       : %s (ID: %d)\n",
                interviewerName != null ? interviewerName : "N/A", interviewerId));
        sb.append(String.format("Submitted At    : %s\n", submittedAt));
        sb.append(String.format("Overall Score   : %.2f / 100.00\n", overallScore));
        sb.append("----------------------------------------------------\n");
        sb.append("Skill-wise Scores:\n");
        if (skillScores == null || skillScores.isEmpty()) {
            sb.append("  (No individual skill scores recorded)\n");
        } else {
            for (SkillScore ss : skillScores) {
                sb.append(String.format("  - %-25s : %6.2f / 100.00\n", ss.getSkillName(), ss.getScore()));
            }
        }
        sb.append("----------------------------------------------------\n");
        sb.append("Interviewer Remarks:\n");
        sb.append("  ").append(feedbackText).append("\n");
        sb.append("====================================================\n");
        return sb.toString();
    }
}
