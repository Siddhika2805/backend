package com.interviewhub.dao;

import com.interviewhub.database.DBConnection;
import com.interviewhub.model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Question DAO
 * Manages full CRUD operations for Technical and HR interview questions.
 */
public class QuestionDAO {

    public int createQuestion(Question question) throws SQLException {
        String sql = "INSERT INTO questions (question_text, category, difficulty, specialization, created_by) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, question.getQuestionText());
            stmt.setString(2, question.getCategory());
            stmt.setString(3, question.getDifficulty() != null ? question.getDifficulty() : "MEDIUM");
            stmt.setString(4, question.getSpecialization());
            if (question.getCreatedBy() != null) {
                stmt.setInt(5, question.getCreatedBy());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    question.setQuestionId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Failed to create question, no ID generated.");
    }

    public Question getQuestionById(int questionId) throws SQLException {
        String sql = "SELECT * FROM questions WHERE question_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, questionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractQuestion(rs);
                }
            }
        }
        return null;
    }

    public List<Question> getAllQuestions() throws SQLException {
        List<Question> list = new ArrayList<>();
        String sql = "SELECT * FROM questions ORDER BY question_id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(extractQuestion(rs));
            }
        }
        return list;
    }

    public List<Question> getQuestionsByCategory(String category) throws SQLException {
        List<Question> list = new ArrayList<>();
        String sql = "SELECT * FROM questions WHERE category = ? ORDER BY question_id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.toUpperCase());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractQuestion(rs));
                }
            }
        }
        return list;
    }

    public boolean updateQuestion(Question question) throws SQLException {
        String sql = "UPDATE questions SET question_text = ?, category = ?, difficulty = ?, specialization = ? WHERE question_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, question.getQuestionText());
            stmt.setString(2, question.getCategory());
            stmt.setString(3, question.getDifficulty());
            stmt.setString(4, question.getSpecialization());
            stmt.setInt(5, question.getQuestionId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteQuestion(int questionId) throws SQLException {
        String sql = "DELETE FROM questions WHERE question_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, questionId);
            return stmt.executeUpdate() > 0;
        }
    }

    private Question extractQuestion(ResultSet rs) throws SQLException {
        int createdByVal = rs.getInt("created_by");
        Integer createdBy = rs.wasNull() ? null : createdByVal;

        return new Question(
                rs.getInt("question_id"),
                rs.getString("question_text"),
                rs.getString("category"),
                rs.getString("difficulty"),
                rs.getString("specialization"),
                createdBy
        );
    }
}
