package com.interviewhub.model;

/**
 * Question Model
 * Represents a question in the InterviewHub question bank.
 */
public class Question {

    private int questionId;
    private String questionText;
    private String category; // TECHNICAL, HR
    private String difficulty; // EASY, MEDIUM, HARD
    private String specialization;
    private Integer createdBy;

    public Question() {
    }

    public Question(int questionId, String questionText, String category,
                    String difficulty, String specialization, Integer createdBy) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.category = category;
        this.difficulty = difficulty;
        this.specialization = specialization;
        this.createdBy = createdBy;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return String.format("[#%d | %s | %s | %s] %s",
                questionId, category, difficulty, specialization, questionText);
    }
}
