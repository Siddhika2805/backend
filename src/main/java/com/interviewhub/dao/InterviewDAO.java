package com.interviewhub.dao;

import com.interviewhub.database.DBConnection;
import com.interviewhub.model.Interview;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Interview DAO
 * Handles database operations for confirmed/scheduled, completed, and cancelled interviews.
 */
public class InterviewDAO {

    public int createInterview(Interview interview, Connection externalConn) throws SQLException {
        String sql = "INSERT INTO interviews (candidate_id, interviewer_id, request_id, interview_type, interview_date, interview_time, interview_status, overall_score) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        boolean manageConn = (externalConn == null);
        Connection conn = manageConn ? DBConnection.getConnection() : externalConn;

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, interview.getCandidateId());
            stmt.setInt(2, interview.getInterviewerId());
            if (interview.getRequestId() != null) {
                stmt.setInt(3, interview.getRequestId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setString(4, interview.getInterviewType());
            stmt.setDate(5, Date.valueOf(interview.getInterviewDate()));
            stmt.setTime(6, Time.valueOf(interview.getInterviewTime()));
            stmt.setString(7, interview.getInterviewStatus() != null ? interview.getInterviewStatus() : "SCHEDULED");
            if (interview.getOverallScore() != null) {
                stmt.setDouble(8, interview.getOverallScore());
            } else {
                stmt.setNull(8, Types.DECIMAL);
            }

            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    interview.setInterviewId(id);
                    return id;
                }
            }
        } finally {
            if (manageConn && conn != null) {
                conn.close();
            }
        }
        throw new SQLException("Failed to create interview, no ID generated.");
    }

    public Interview getInterviewById(int interviewId) throws SQLException {
        String sql = "SELECT i.*, cu.name as candidate_name, iu.name as interviewer_name " +
                "FROM interviews i " +
                "JOIN candidates c ON i.candidate_id = c.candidate_id " +
                "JOIN users cu ON c.user_id = cu.user_id " +
                "JOIN interviewers iv ON i.interviewer_id = iv.interviewer_id " +
                "JOIN users iu ON iv.user_id = iu.user_id " +
                "WHERE i.interview_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, interviewId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractInterview(rs);
                }
            }
        }
        return null;
    }

    public List<Interview> getInterviewsByCandidate(int candidateId) throws SQLException {
        List<Interview> list = new ArrayList<>();
        String sql = "SELECT i.*, cu.name as candidate_name, iu.name as interviewer_name " +
                "FROM interviews i " +
                "JOIN candidates c ON i.candidate_id = c.candidate_id " +
                "JOIN users cu ON c.user_id = cu.user_id " +
                "JOIN interviewers iv ON i.interviewer_id = iv.interviewer_id " +
                "JOIN users iu ON iv.user_id = iu.user_id " +
                "WHERE i.candidate_id = ? ORDER BY i.interview_date ASC, i.interview_time ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, candidateId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractInterview(rs));
                }
            }
        }
        return list;
    }

    public List<Interview> getInterviewsByInterviewer(int interviewerId) throws SQLException {
        List<Interview> list = new ArrayList<>();
        String sql = "SELECT i.*, cu.name as candidate_name, iu.name as interviewer_name " +
                "FROM interviews i " +
                "JOIN candidates c ON i.candidate_id = c.candidate_id " +
                "JOIN users cu ON c.user_id = cu.user_id " +
                "JOIN interviewers iv ON i.interviewer_id = iv.interviewer_id " +
                "JOIN users iu ON iv.user_id = iu.user_id " +
                "WHERE i.interviewer_id = ? ORDER BY i.interview_date ASC, i.interview_time ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, interviewerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractInterview(rs));
                }
            }
        }
        return list;
    }

    public List<Interview> getAllInterviews() throws SQLException {
        List<Interview> list = new ArrayList<>();
        String sql = "SELECT i.*, cu.name as candidate_name, iu.name as interviewer_name " +
                "FROM interviews i " +
                "JOIN candidates c ON i.candidate_id = c.candidate_id " +
                "JOIN users cu ON c.user_id = cu.user_id " +
                "JOIN interviewers iv ON i.interviewer_id = iv.interviewer_id " +
                "JOIN users iu ON iv.user_id = iu.user_id " +
                "ORDER BY i.interview_date DESC, i.interview_time DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(extractInterview(rs));
            }
        }
        return list;
    }

    public boolean updateInterviewStatusAndScore(int interviewId, String status, Double score, Connection externalConn) throws SQLException {
        String sql = "UPDATE interviews SET interview_status = ?, overall_score = ? WHERE interview_id = ?";
        boolean manageConn = (externalConn == null);
        Connection conn = manageConn ? DBConnection.getConnection() : externalConn;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            if (score != null) {
                stmt.setDouble(2, score);
            } else {
                stmt.setNull(2, Types.DECIMAL);
            }
            stmt.setInt(3, interviewId);
            return stmt.executeUpdate() > 0;
        } finally {
            if (manageConn && conn != null) {
                conn.close();
            }
        }
    }

    public boolean cancelInterview(int interviewId) throws SQLException {
        String sql = "UPDATE interviews SET interview_status = 'CANCELLED' WHERE interview_id = ? AND interview_status = 'SCHEDULED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, interviewId);
            return stmt.executeUpdate() > 0;
        }
    }

    private Interview extractInterview(ResultSet rs) throws SQLException {
        int reqIdVal = rs.getInt("request_id");
        Integer requestId = rs.wasNull() ? null : reqIdVal;

        double scoreVal = rs.getDouble("overall_score");
        Double overallScore = rs.wasNull() ? null : scoreVal;

        Interview interview = new Interview(
                rs.getInt("interview_id"),
                rs.getInt("candidate_id"),
                rs.getInt("interviewer_id"),
                requestId,
                rs.getString("interview_type"),
                rs.getDate("interview_date").toLocalDate(),
                rs.getTime("interview_time").toLocalTime(),
                rs.getString("interview_status"),
                overallScore,
                rs.getTimestamp("created_at")
        );
        interview.setCandidateName(rs.getString("candidate_name"));
        interview.setInterviewerName(rs.getString("interviewer_name"));
        return interview;
    }
}
