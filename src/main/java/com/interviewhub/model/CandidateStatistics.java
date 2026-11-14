package com.interviewhub.model;

import java.util.Map;

/**
 * Candidate Performance Statistics Model
 * Calculates and formats comprehensive candidate performance metrics across interviews.
 * Handles division by zero, first score comparison, and skill-wise breakdown.
 */
public class CandidateStatistics {

    private int candidateId;
    private String candidateName;
    private int totalAttended;
    private int totalCompleted;
    private double averageScore;
    private double highestScore;
    private double lowestScore;
    private Double firstScore;
    private Double latestScore;
    private Double improvementPercentage;
    private String strongestSkill;
    private double strongestSkillScore;
    private String weakestSkill;
    private double weakestSkillScore;
    private Map<String, Double> skillAverages;

    public CandidateStatistics() {
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

    public int getTotalAttended() {
        return totalAttended;
    }

    public void setTotalAttended(int totalAttended) {
        this.totalAttended = totalAttended;
    }

    public int getTotalCompleted() {
        return totalCompleted;
    }

    public void setTotalCompleted(int totalCompleted) {
        this.totalCompleted = totalCompleted;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public double getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(double highestScore) {
        this.highestScore = highestScore;
    }

    public double getLowestScore() {
        return lowestScore;
    }

    public void setLowestScore(double lowestScore) {
        this.lowestScore = lowestScore;
    }

    public Double getFirstScore() {
        return firstScore;
    }

    public void setFirstScore(Double firstScore) {
        this.firstScore = firstScore;
    }

    public Double getLatestScore() {
        return latestScore;
    }

    public void setLatestScore(Double latestScore) {
        this.latestScore = latestScore;
    }

    public Double getImprovementPercentage() {
        return improvementPercentage;
    }

    public void setImprovementPercentage(Double improvementPercentage) {
        this.improvementPercentage = improvementPercentage;
    }

    public String getStrongestSkill() {
        return strongestSkill;
    }

    public void setStrongestSkill(String strongestSkill) {
        this.strongestSkill = strongestSkill;
    }

    public double getStrongestSkillScore() {
        return strongestSkillScore;
    }

    public void setStrongestSkillScore(double strongestSkillScore) {
        this.strongestSkillScore = strongestSkillScore;
    }

    public String getWeakestSkill() {
        return weakestSkill;
    }

    public void setWeakestSkill(String weakestSkill) {
        this.weakestSkill = weakestSkill;
    }

    public double getWeakestSkillScore() {
        return weakestSkillScore;
    }

    public void setWeakestSkillScore(double weakestSkillScore) {
        this.weakestSkillScore = weakestSkillScore;
    }

    public Map<String, Double> getSkillAverages() {
        return skillAverages;
    }

    public void setSkillAverages(Map<String, Double> skillAverages) {
        this.skillAverages = skillAverages;
    }

    /**
     * Formats performance statistics using StringBuilder.
     */
    public String toFormattedReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("==============================================================\n");
        sb.append(String.format("PERFORMANCE TRACKING REPORT: %s (ID: %d)\n",
                candidateName != null ? candidateName : "Candidate", candidateId));
        sb.append("==============================================================\n");
        sb.append(String.format("Total Interviews Attended  : %d\n", totalAttended));
        sb.append(String.format("Total Completed Interviews : %d\n", totalCompleted));

        if (totalCompleted == 0) {
            sb.append("--------------------------------------------------------------\n");
            sb.append("No completed interview evaluations recorded yet.\n");
            sb.append("==============================================================\n");
            return sb.toString();
        }

        sb.append(String.format("Average Overall Score      : %.2f / 100.00\n", averageScore));
        sb.append(String.format("Highest Score Achieved     : %.2f / 100.00\n", highestScore));
        sb.append(String.format("Lowest Score Achieved      : %.2f / 100.00\n", lowestScore));

        if (firstScore != null && latestScore != null) {
            sb.append(String.format("First Interview Score      : %.2f / 100.00\n", firstScore));
            sb.append(String.format("Latest Interview Score     : %.2f / 100.00\n", latestScore));
            if (improvementPercentage != null) {
                String prefix = improvementPercentage >= 0 ? "+" : "";
                sb.append(String.format("Improvement Percentage     : %s%.2f%%\n", prefix, improvementPercentage));
            } else if (firstScore == 0.0) {
                sb.append("Improvement Percentage     : Initial score was 0.00 (Baseline initialized)\n");
            }
        }

        sb.append("--------------------------------------------------------------\n");
        sb.append("SKILL ANALYSIS:\n");
        if (strongestSkill != null) {
            sb.append(String.format("  * Strongest Competency   : %s (Avg: %.2f / 100.00)\n",
                    strongestSkill, strongestSkillScore));
        }
        if (weakestSkill != null) {
            sb.append(String.format("  * Areas for Growth       : %s (Avg: %.2f / 100.00)\n",
                    weakestSkill, weakestSkillScore));
        }

        if (skillAverages != null && !skillAverages.isEmpty()) {
            sb.append("--------------------------------------------------------------\n");
            sb.append("Skill-wise Average Breakdown:\n");
            for (Map.Entry<String, Double> entry : skillAverages.entrySet()) {
                sb.append(String.format("  - %-25s : %6.2f / 100.00\n", entry.getKey(), entry.getValue()));
            }
        }
        sb.append("==============================================================\n");
        return sb.toString();
    }
}
