package com.interviewhub.model;

/**
 * Skill Score Model
 * Represents granular evaluation on individual competencies (Technical, Problem Solving, etc.)
 */
public class SkillScore {

    private int skillScoreId;
    private int feedbackId;
    private String skillName;
    private double score; // 0.00 to 100.00

    public SkillScore() {
    }

    public SkillScore(int skillScoreId, int feedbackId, String skillName, double score) {
        this.skillScoreId = skillScoreId;
        this.feedbackId = feedbackId;
        this.skillName = skillName;
        this.score = score;
    }

    public SkillScore(String skillName, double score) {
        this.skillName = skillName;
        this.score = score;
    }

    public int getSkillScoreId() {
        return skillScoreId;
    }

    public void setSkillScoreId(int skillScoreId) {
        this.skillScoreId = skillScoreId;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return String.format("%s: %.2f/100", skillName, score);
    }
}
