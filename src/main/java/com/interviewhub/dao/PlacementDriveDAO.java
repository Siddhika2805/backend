package com.interviewhub.dao;

import com.interviewhub.database.DBConnection;
import com.interviewhub.model.PlacementDrive;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PlacementDrive DAO
 * Manages database persistence for campus placement drives.
 */
public class PlacementDriveDAO {

    public int createDrive(PlacementDrive drive) throws SQLException {
        String sql = "INSERT INTO placement_drives (company_id, drive_name, eligibility_cgpa, eligible_department, application_deadline, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, drive.getCompanyId());
            stmt.setString(2, drive.getDriveName());
            stmt.setDouble(3, drive.getEligibilityCgpa());
            stmt.setString(4, drive.getEligibleDepartment());
            stmt.setDate(5, Date.valueOf(drive.getApplicationDeadline()));
            stmt.setString(6, drive.getStatus() != null ? drive.getStatus() : "OPEN");

            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    drive.setDriveId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Failed to create placement drive, no ID generated.");
    }

    public PlacementDrive getDriveById(int driveId) throws SQLException {
        String sql = "SELECT d.*, c.company_name FROM placement_drives d " +
                "JOIN companies c ON d.company_id = c.company_id WHERE d.drive_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, driveId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractDrive(rs);
                }
            }
        }
        return null;
    }

    public List<PlacementDrive> getAllDrives() throws SQLException {
        List<PlacementDrive> list = new ArrayList<>();
        String sql = "SELECT d.*, c.company_name FROM placement_drives d " +
                "JOIN companies c ON d.company_id = c.company_id ORDER BY d.drive_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(extractDrive(rs));
            }
        }
        return list;
    }

    public boolean updateDriveStatus(int driveId, String status) throws SQLException {
        String sql = "UPDATE placement_drives SET status = ? WHERE drive_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, driveId);
            return stmt.executeUpdate() > 0;
        }
    }

    private PlacementDrive extractDrive(ResultSet rs) throws SQLException {
        return new PlacementDrive(
                rs.getInt("drive_id"),
                rs.getInt("company_id"),
                rs.getString("company_name"),
                rs.getString("drive_name"),
                rs.getDouble("eligibility_cgpa"),
                rs.getString("eligible_department"),
                rs.getDate("application_deadline").toLocalDate(),
                rs.getString("status")
        );
    }
}
