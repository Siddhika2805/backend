package com.interviewhub.dao;

import com.interviewhub.database.DBConnection;
import com.interviewhub.model.Application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Application DAO
 * Manages database persistence for placement drive student applications.
 */
public class ApplicationDAO {

    public int createApplication(Application app) throws SQLException {
        String sql = "INSERT INTO applications (candidate_id, drive_id, application_status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, app.getCandidateId());
            stmt.setInt(2, app.getDriveId());
            stmt.setString(3, app.getApplicationStatus() != null ? app.getApplicationStatus() : "APPLIED");

            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    app.setApplicationId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Failed to create application, no ID generated.");
    }

    public boolean hasExistingApplication(int candidateId, int driveId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM applications WHERE candidate_id = ? AND drive_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, candidateId);
            stmt.setInt(2, driveId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public List<Application> getApplicationsByCandidate(int candidateId) throws SQLException {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT a.*, u.name as candidate_name, d.drive_name " +
                "FROM applications a " +
                "JOIN candidates c ON a.candidate_id = c.candidate_id " +
                "JOIN users u ON c.user_id = u.user_id " +
                "JOIN placement_drives d ON a.drive_id = d.drive_id " +
                "WHERE a.candidate_id = ? ORDER BY a.application_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, candidateId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Application app = extractApplication(rs);
                    list.add(app);
                }
            }
        }
        return list;
    }

    public List<Application> getAllApplications() throws SQLException {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT a.*, u.name as candidate_name, d.drive_name " +
                "FROM applications a " +
                "JOIN candidates c ON a.candidate_id = c.candidate_id " +
                "JOIN users u ON c.user_id = u.user_id " +
                "JOIN placement_drives d ON a.drive_id = d.drive_id " +
                "ORDER BY a.application_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(extractApplication(rs));
            }
        }
        return list;
    }

    public boolean updateApplicationStatus(int applicationId, String status) throws SQLException {
        String sql = "UPDATE applications SET application_status = ? WHERE application_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, applicationId);
            return stmt.executeUpdate() > 0;
        }
    }

    private Application extractApplication(ResultSet rs) throws SQLException {
        Application app = new Application(
                rs.getInt("application_id"),
                rs.getInt("candidate_id"),
                rs.getInt("drive_id"),
                rs.getTimestamp("application_date"),
                rs.getString("application_status")
        );
        app.setCandidateName(rs.getString("candidate_name"));
        app.setDriveName(rs.getString("drive_name"));
        return app;
    }
}
