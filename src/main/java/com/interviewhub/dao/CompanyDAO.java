package com.interviewhub.dao;

import com.interviewhub.database.DBConnection;
import com.interviewhub.model.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Company DAO
 * Manages database persistence for recruiting Companies.
 */
public class CompanyDAO {

    public int createCompany(Company company) throws SQLException {
        String sql = "INSERT INTO companies (company_name, industry, location) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, company.getCompanyName());
            stmt.setString(2, company.getIndustry());
            stmt.setString(3, company.getLocation());

            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    company.setCompanyId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Failed to create company, no ID obtained.");
    }

    public Company getCompanyById(int companyId) throws SQLException {
        String sql = "SELECT * FROM companies WHERE company_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, companyId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Company(
                            rs.getInt("company_id"),
                            rs.getString("company_name"),
                            rs.getString("industry"),
                            rs.getString("location")
                    );
                }
            }
        }
        return null;
    }

    public List<Company> getAllCompanies() throws SQLException {
        List<Company> list = new ArrayList<>();
        String sql = "SELECT * FROM companies ORDER BY company_id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Company(
                        rs.getInt("company_id"),
                        rs.getString("company_name"),
                        rs.getString("industry"),
                        rs.getString("location")
                ));
            }
        }
        return list;
    }
}
