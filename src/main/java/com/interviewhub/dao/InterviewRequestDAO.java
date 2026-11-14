package com.interviewhub.dao;

import com.interviewhub.database.DBConnection;
import com.interviewhub.model.InterviewRequest;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * InterviewRequest DAO
 * Handles candidate request submissions, conflict detection, and status transitions.
 */
public class InterviewRequestDAO {

    public int createRequest(InterviewRequest request) throws SQLException {
        String sql = "INSERT INTO interview_requests (candidate_id, interviewer_id, interview_type, preferred_date, preferred_time, request_status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, request.getCandidateId());
            stmt.setInt(2, request.getInterviewerId());
            stmt.setString(3, request.getInterviewType());
            stmt.setDate(4, Date.valueOf(request.getPreferredDate()));
            stmt.setTime(5, Time.valueOf(request.getPreferredTime()));
            stmt.setString(6, request.getRequestStatus() != null ? request.getRequestStatus() : "PENDING");

            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    request.setRequestId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Failed to create interview request, no ID generated.");
    }

    public boolean hasConflictingSlot(int candidateId, int interviewerId, LocalDate date, LocalTime time) throws SQLException {
        // Checks if interviewer or candidate already has an active request or scheduled interview at the exact date and time
        String sql = "SELECT COUNT(*) FROM interview_requests " +
                "WHERE (interviewer_id = ? OR candidate_id = ?) " +
                "AND preferred_date = ? AND preferred_time = ? " +
                "AND request_status IN ('PENDING', 'ACCEPTED')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, interviewerId);
            stmt.setInt(2, candidateId);
            stmt.setDate(3, Date.valueOf(date));
            stmt.setTime(4, Time.valueOf(time));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public InterviewRequest getRequestById(int requestId) throws SQLException {
        String sql = "SELECT r.*, cu.name as candidate_name, iu.name as interviewer_name " +
                "FROM interview_requests r " +
                "JOIN candidates c ON r.candidate_id = c.candidate_id " +
                "JOIN users cu ON c.user_id = cu.user_id " +
                "JOIN interviewers i ON r.interviewer_id = i.interviewer_id " +
                "JOIN users iu ON i.user_id = iu.user_id " +
                "WHERE r.request_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractRequest(rs);
                }
            }
        }
        return null;
    }

    public List<InterviewRequest> getRequestsByCandidate(int candidateId) throws SQLException {
        List<InterviewRequest> list = new ArrayList<>();
        String sql = "SELECT r.*, cu.name as candidate_name, iu.name as interviewer_name " +
                "FROM interview_requests r " +
                "JOIN candidates c ON r.candidate_id = c.candidate_id " +
                "JOIN users cu ON c.user_id = cu.user_id " +
                "JOIN interviewers i ON r.interviewer_id = i.interviewer_id " +
                "JOIN users iu ON i.user_id = iu.user_id " +
                "WHERE r.candidate_id = ? ORDER BY r.request_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, candidateId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractRequest(rs));
                }
            }
        }
        return list;
    }

    public List<InterviewRequest> getRequestsByInterviewer(int interviewerId) throws SQLException {
        List<InterviewRequest> list = new ArrayList<>();
        String sql = "SELECT r.*, cu.name as candidate_name, iu.name as interviewer_name " +
                "FROM interview_requests r " +
                "JOIN candidates c ON r.candidate_id = c.candidate_id " +
                "JOIN users cu ON c.user_id = cu.user_id " +
                "JOIN interviewers i ON r.interviewer_id = i.interviewer_id " +
                "JOIN users iu ON i.user_id = iu.user_id " +
                "WHERE r.interviewer_id = ? ORDER BY r.request_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, interviewerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractRequest(rs));
                }
            }
        }
        return list;
    }

    public List<InterviewRequest> getAllRequests() throws SQLException {
        List<InterviewRequest> list = new ArrayList<>();
        String sql = "SELECT r.*, cu.name as candidate_name, iu.name as interviewer_name " +
                "FROM interview_requests r " +
                "JOIN candidates c ON r.candidate_id = c.candidate_id " +
                "JOIN users cu ON c.user_id = cu.user_id " +
                "JOIN interviewers i ON r.interviewer_id = i.interviewer_id " +
                "JOIN users iu ON i.user_id = iu.user_id " +
                "ORDER BY r.request_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(extractRequest(rs));
            }
        }
        return list;
    }

    public boolean updateRequestStatus(int requestId, String status, Connection externalConn) throws SQLException {
        String sql = "UPDATE interview_requests SET request_status = ? WHERE request_id = ?";
        boolean manageConn = (externalConn == null);
        Connection conn = manageConn ? DBConnection.getConnection() : externalConn;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, requestId);
            return stmt.executeUpdate() > 0;
        } finally {
            if (manageConn && conn != null) {
                conn.close();
            }
        }
    }

    private InterviewRequest extractRequest(ResultSet rs) throws SQLException {
        InterviewRequest req = new InterviewRequest(
                rs.getInt("request_id"),
                rs.getInt("candidate_id"),
                rs.getInt("interviewer_id"),
                rs.getString("interview_type"),
                rs.getDate("preferred_date").toLocalDate(),
                rs.getTime("preferred_time").toLocalTime(),
                rs.getString("request_status"),
                rs.getTimestamp("created_at")
        );
        req.setCandidateName(rs.getString("candidate_name"));
        req.setInterviewerName(rs.getString("interviewer_name"));
        return req;
    }
}
