package com.interviewhub.dao;

import com.interviewhub.database.DBConnection;
import com.interviewhub.model.*;

import java.sql.*;
import java.util.*;

/**
 * Feedback and Statistics DAO
 * Demonstrates:
 * 1. JDBC Transactions with explicit setAutoCommit(false), commit(), and rollback().
 * 2. PreparedStatement and ResultSet.
 * 3. Analytical SQL queries for performance calculation and skill-wise evaluation.
 */
public class FeedbackDAO {

    /**
     * Submits interview feedback, updates interview status to COMPLETED, and inserts skill scores
     * within a single atomic database transaction.
     * If any part fails, the entire transaction is rolled back.
     */
    public boolean submitEvaluationTransaction(InterviewFeedback feedback, List<SkillScore> skillScores) throws SQLException {
        String insertFeedbackSql = "INSERT INTO interview_feedback (interview_id, interviewer_id, feedback_text, overall_score) " +
                "VALUES (?, ?, ?, ?)";
        String insertSkillSql = "INSERT INTO skill_scores (feedback_id, skill_name, score) VALUES (?, ?, ?)";
        String updateInterviewSql = "UPDATE interviews SET interview_status = 'COMPLETED', overall_score = ? WHERE interview_id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Begin Transaction

            // 1. Insert feedback record
            int feedbackId;
            try (PreparedStatement fStmt = conn.prepareStatement(insertFeedbackSql, Statement.RETURN_GENERATED_KEYS)) {
                fStmt.setInt(1, feedback.getInterviewId());
                fStmt.setInt(2, feedback.getInterviewerId());
                fStmt.setString(3, feedback.getFeedbackText());
                fStmt.setDouble(4, feedback.getOverallScore());

                int affected = fStmt.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("Inserting feedback failed, no rows affected.");
                }

                try (ResultSet keys = fStmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        feedbackId = keys.getInt(1);
                        feedback.setFeedbackId(feedbackId);
                    } else {
                        throw new SQLException("Inserting feedback failed, no generated key.");
                    }
                }
            }

            // 2. Insert skill scores
            if (skillScores != null && !skillScores.isEmpty()) {
                try (PreparedStatement sStmt = conn.prepareStatement(insertSkillSql)) {
                    for (SkillScore score : skillScores) {
                        sStmt.setInt(1, feedbackId);
                        sStmt.setString(2, score.getSkillName());
                        sStmt.setDouble(3, score.getScore());
                        sStmt.addBatch();
                    }
                    sStmt.executeBatch();
                }
            }

            // 3. Update interview status to COMPLETED and set overall_score
            try (PreparedStatement iStmt = conn.prepareStatement(updateInterviewSql)) {
                iStmt.setDouble(1, feedback.getOverallScore());
                iStmt.setInt(2, feedback.getInterviewId());
                int updated = iStmt.executeUpdate();
                if (updated == 0) {
                    throw new SQLException("Failed to mark interview as COMPLETED.");
                }
            }

            conn.commit(); // Commit Transaction
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback Transaction
                    System.err.println("[TRANSACTION ROLLBACK] Rolled back feedback submission due to error: " + e.getMessage());
                } catch (SQLException ex) {
                    System.err.println("[ERROR] Failed to rollback transaction: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {}
            }
        }
    }

    public InterviewFeedback getFeedbackByInterviewId(int interviewId) throws SQLException {
        String sql = "SELECT f.*, u.name as interviewer_name " +
                "FROM interview_feedback f " +
                "JOIN interviewers i ON f.interviewer_id = i.interviewer_id " +
                "JOIN users u ON i.user_id = u.user_id " +
                "WHERE f.interview_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, interviewId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    InterviewFeedback feedback = new InterviewFeedback(
                            rs.getInt("feedback_id"),
                            rs.getInt("interview_id"),
                            rs.getInt("interviewer_id"),
                            rs.getString("feedback_text"),
                            rs.getDouble("overall_score"),
                            rs.getTimestamp("submitted_at")
                    );
                    feedback.setInterviewerName(rs.getString("interviewer_name"));
                    feedback.setSkillScores(getSkillScoresByFeedbackId(feedback.getFeedbackId()));
                    return feedback;
                }
            }
        }
        return null;
    }

    public List<SkillScore> getSkillScoresByFeedbackId(int feedbackId) throws SQLException {
        List<SkillScore> list = new ArrayList<>();
        String sql = "SELECT * FROM skill_scores WHERE feedback_id = ? ORDER BY skill_name ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, feedbackId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new SkillScore(
                            rs.getInt("skill_score_id"),
                            rs.getInt("feedback_id"),
                            rs.getString("skill_name"),
                            rs.getDouble("score")
                    ));
                }
            }
        }
        return list;
    }

    /**
     * Calculates comprehensive Candidate Statistics from actual database records.
     */
    public CandidateStatistics getCandidateStatistics(int candidateId) throws SQLException {
        CandidateStatistics stats = new CandidateStatistics();
        stats.setCandidateId(candidateId);

        // Candidate Name
        String nameSql = "SELECT u.name FROM candidates c JOIN users u ON c.user_id = u.user_id WHERE c.candidate_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(nameSql)) {
            stmt.setInt(1, candidateId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    stats.setCandidateName(rs.getString("name"));
                }
            }
        }

        // Total interviews attended and completed
        String countSql = "SELECT " +
                "COUNT(*) as total_attended, " +
                "SUM(CASE WHEN interview_status = 'COMPLETED' THEN 1 ELSE 0 END) as total_completed, " +
                "AVG(CASE WHEN interview_status = 'COMPLETED' THEN overall_score ELSE NULL END) as avg_score, " +
                "MAX(CASE WHEN interview_status = 'COMPLETED' THEN overall_score ELSE NULL END) as max_score, " +
                "MIN(CASE WHEN interview_status = 'COMPLETED' THEN overall_score ELSE NULL END) as min_score " +
                "FROM interviews WHERE candidate_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(countSql)) {
            stmt.setInt(1, candidateId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    stats.setTotalAttended(rs.getInt("total_attended"));
                    int completed = rs.getInt("total_completed");
                    stats.setTotalCompleted(completed);

                    if (completed > 0) {
                        stats.setAverageScore(rs.getDouble("avg_score"));
                        stats.setHighestScore(rs.getDouble("max_score"));
                        stats.setLowestScore(rs.getDouble("min_score"));
                    }
                }
            }
        }

        if (stats.getTotalCompleted() > 0) {
            // Retrieve chronological completed interview scores to compute improvement
            String chronoSql = "SELECT overall_score FROM interviews " +
                    "WHERE candidate_id = ? AND interview_status = 'COMPLETED' AND overall_score IS NOT NULL " +
                    "ORDER BY interview_date ASC, interview_time ASC, interview_id ASC";

            List<Double> scores = new ArrayList<>();
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(chronoSql)) {
                stmt.setInt(1, candidateId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        scores.add(rs.getDouble("overall_score"));
                    }
                }
            }

            if (!scores.isEmpty()) {
                double first = scores.get(0);
                double latest = scores.get(scores.size() - 1);
                stats.setFirstScore(first);
                stats.setLatestScore(latest);

                // Calculation: Improvement Percentage = ((Latest Score - First Score) / First Score) * 100
                // Robust division by zero check:
                if (first > 0.0) {
                    double improvement = ((latest - first) / first) * 100.0;
                    stats.setImprovementPercentage(improvement);
                } else {
                    stats.setImprovementPercentage(null);
                }
            }

            // Skill-wise analysis
            String skillSql = "SELECT ss.skill_name, AVG(ss.score) as avg_skill " +
                    "FROM skill_scores ss " +
                    "JOIN interview_feedback f ON ss.feedback_id = f.feedback_id " +
                    "JOIN interviews i ON f.interview_id = i.interview_id " +
                    "WHERE i.candidate_id = ? " +
                    "GROUP BY ss.skill_name " +
                    "ORDER BY avg_skill DESC";

            Map<String, Double> skillAverages = new LinkedHashMap<>();
            String strongestSkill = null;
            double strongestScore = -1.0;
            String weakestSkill = null;
            double weakestScore = 101.0;

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(skillSql)) {
                stmt.setInt(1, candidateId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String skill = rs.getString("skill_name");
                        double avg = rs.getDouble("avg_skill");
                        skillAverages.put(skill, avg);

                        if (strongestSkill == null || avg > strongestScore) {
                            strongestSkill = skill;
                            strongestScore = avg;
                        }
                        if (weakestSkill == null || avg < weakestScore) {
                            weakestSkill = skill;
                            weakestScore = avg;
                        }
                    }
                }
            }

            stats.setSkillAverages(skillAverages);
            stats.setStrongestSkill(strongestSkill);
            stats.setStrongestSkillScore(strongestScore >= 0 ? strongestScore : 0.0);
            stats.setWeakestSkill(weakestSkill);
            stats.setWeakestSkillScore(weakestScore <= 100 ? weakestScore : 0.0);
        }

        return stats;
    }

    /**
     * Calculates Interviewer Statistics from actual database records.
     */
    public InterviewerStatistics getInterviewerStatistics(int interviewerId) throws SQLException {
        InterviewerStatistics stats = new InterviewerStatistics();
        stats.setInterviewerId(interviewerId);

        String nameSql = "SELECT u.name FROM interviewers i JOIN users u ON i.user_id = u.user_id WHERE i.interviewer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(nameSql)) {
            stmt.setInt(1, interviewerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    stats.setInterviewerName(rs.getString("name"));
                }
            }
        }

        String sql = "SELECT " +
                "COUNT(*) as total_conducted, " +
                "SUM(CASE WHEN interview_status = 'COMPLETED' THEN 1 ELSE 0 END) as total_completed, " +
                "SUM(CASE WHEN interview_status = 'CANCELLED' THEN 1 ELSE 0 END) as total_cancelled, " +
                "AVG(CASE WHEN interview_status = 'COMPLETED' THEN overall_score ELSE NULL END) as avg_score, " +
                "COUNT(DISTINCT candidate_id) as unique_candidates " +
                "FROM interviews WHERE interviewer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, interviewerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    stats.setTotalConducted(rs.getInt("total_conducted"));
                    stats.setTotalCompleted(rs.getInt("total_completed"));
                    stats.setTotalCancelled(rs.getInt("total_cancelled"));
                    stats.setAverageCandidateScore(rs.getDouble("avg_score"));
                    stats.setUniqueCandidatesInterviewed(rs.getInt("unique_candidates"));
                }
            }
        }

        return stats;
    }

    /**
     * Calculates System-wide Statistics for Administrator.
     */
    public SystemStatistics getSystemStatistics() throws SQLException {
        SystemStatistics stats = new SystemStatistics();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Users & roles
            try (ResultSet rs = stmt.executeQuery("SELECT " +
                    "COUNT(*) as total_users, " +
                    "SUM(CASE WHEN role = 'CANDIDATE' THEN 1 ELSE 0 END) as total_candidates, " +
                    "SUM(CASE WHEN role = 'INTERVIEWER' THEN 1 ELSE 0 END) as total_interviewers " +
                    "FROM users")) {
                if (rs.next()) {
                    stats.setTotalUsers(rs.getInt("total_users"));
                    stats.setTotalCandidates(rs.getInt("total_candidates"));
                    stats.setTotalInterviewers(rs.getInt("total_interviewers"));
                }
            }

            // Interviews
            try (ResultSet rs = stmt.executeQuery("SELECT " +
                    "COUNT(*) as total_interviews, " +
                    "SUM(CASE WHEN interview_status = 'COMPLETED' THEN 1 ELSE 0 END) as completed_count, " +
                    "SUM(CASE WHEN interview_status = 'SCHEDULED' THEN 1 ELSE 0 END) as scheduled_count, " +
                    "SUM(CASE WHEN interview_status = 'CANCELLED' THEN 1 ELSE 0 END) as cancelled_count, " +
                    "AVG(CASE WHEN interview_status = 'COMPLETED' THEN overall_score ELSE NULL END) as avg_score " +
                    "FROM interviews")) {
                if (rs.next()) {
                    stats.setTotalInterviews(rs.getInt("total_interviews"));
                    stats.setTotalCompletedInterviews(rs.getInt("completed_count"));
                    stats.setTotalScheduledInterviews(rs.getInt("scheduled_count"));
                    stats.setTotalCancelledInterviews(rs.getInt("cancelled_count"));
                    stats.setOverallAverageScore(rs.getDouble("avg_score"));
                }
            }

            // Placement drives
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM placement_drives")) {
                if (rs.next()) {
                    stats.setTotalPlacementDrives(rs.getInt(1));
                }
            }

            // Applications
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM applications")) {
                if (rs.next()) {
                    stats.setTotalApplications(rs.getInt(1));
                }
            }

            // Questions
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM questions")) {
                if (rs.next()) {
                    stats.setTotalQuestions(rs.getInt(1));
                }
            }
        }

        return stats;
    }
}
