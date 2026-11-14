package com.interviewhub.service;

import com.interviewhub.dao.CandidateDAO;
import com.interviewhub.dao.InterviewerDAO;
import com.interviewhub.dao.UserDAO;
import com.interviewhub.database.DBConnection;
import com.interviewhub.exceptions.DuplicateRecordException;
import com.interviewhub.exceptions.InvalidEmailException;
import com.interviewhub.exceptions.UnauthorizedAccessException;
import com.interviewhub.model.Admin;
import com.interviewhub.model.Candidate;
import com.interviewhub.model.Interviewer;
import com.interviewhub.model.User;
import com.interviewhub.utils.InputValidator;
import com.interviewhub.utils.PasswordUtil;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Authentication and User Registration Service
 * Enforces PBKDF2 password security, email normalization, and transactional user onboarding.
 */
public class AuthService {

    private final UserDAO userDAO = new UserDAO();
    private final CandidateDAO candidateDAO = new CandidateDAO();
    private final InterviewerDAO interviewerDAO = new InterviewerDAO();

    /**
     * Authenticates a user with email and plain-text password.
     *
     * @param rawEmail user email
     * @param password user password
     * @return concrete User object (Candidate, Interviewer, or Admin)
     * @throws UnauthorizedAccessException if authentication fails or account is inactive
     */
    public User login(String rawEmail, String password) throws UnauthorizedAccessException, InvalidEmailException, SQLException {
        String email = InputValidator.validateAndNormalizeEmail(rawEmail);
        User baseUser = userDAO.getUserByEmail(email);

        if (baseUser == null) {
            throw new UnauthorizedAccessException("Authentication failed: No user found with email " + email);
        }

        if (!baseUser.isActive()) {
            throw new UnauthorizedAccessException("Account is currently DEACTIVATED. Please contact the administrator.");
        }

        boolean valid = PasswordUtil.verifyPassword(password, baseUser.getPasswordHash(), baseUser.getPasswordSalt());
        if (!valid) {
            throw new UnauthorizedAccessException("Authentication failed: Incorrect password.");
        }

        // Return role-specific model
        switch (baseUser.getRole().toUpperCase()) {
            case "ADMIN":
                return new Admin(baseUser.getUserId(), baseUser.getName(), baseUser.getEmail(),
                        baseUser.getPasswordHash(), baseUser.getPasswordSalt(), baseUser.getStatus(), baseUser.getCreatedAt());
            case "CANDIDATE":
                Candidate c = candidateDAO.getCandidateByUserId(baseUser.getUserId());
                if (c != null) return c;
                break;
            case "INTERVIEWER":
                Interviewer i = interviewerDAO.getInterviewerByUserId(baseUser.getUserId());
                if (i != null) return i;
                break;
        }

        return baseUser;
    }

    /**
     * Registers a new Candidate account in a single atomic database transaction.
     */
    public Candidate registerCandidate(String name, String rawEmail, String password,
                                       String studentId, String department, double cgpa,
                                       int graduationYear, String specialization)
            throws InvalidEmailException, DuplicateRecordException, SQLException {

        String validName = InputValidator.validateNonBlank(name, "Name");
        String email = InputValidator.validateAndNormalizeEmail(rawEmail);
        InputValidator.validatePassword(password);
        String validStudentId = InputValidator.validateNonBlank(studentId, "Student ID");
        String validDept = InputValidator.validateNonBlank(department, "Department");
        double validCgpa = InputValidator.validateCgpa(cgpa);
        String validSpec = InputValidator.validateNonBlank(specialization, "Specialization");

        if (userDAO.emailExists(email)) {
            throw new DuplicateRecordException("Email '" + email + "' is already registered in the system.");
        }

        String salt = PasswordUtil.generateSalt();
        String hash = PasswordUtil.hashPassword(password, salt);

        Candidate candidate = new Candidate();
        candidate.setName(validName);
        candidate.setEmail(email);
        candidate.setPasswordHash(hash);
        candidate.setPasswordSalt(salt);
        candidate.setRole("CANDIDATE");
        candidate.setStatus("ACTIVE");
        candidate.setStudentId(validStudentId);
        candidate.setDepartment(validDept);
        candidate.setCgpa(validCgpa);
        candidate.setGraduationYear(graduationYear);
        candidate.setSpecialization(validSpec);

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            int userId = userDAO.createUser(candidate, conn);
            candidate.setUserId(userId);

            int candidateId = candidateDAO.createCandidate(candidate, conn);
            candidate.setCandidateId(candidateId);

            conn.commit(); // Commit transaction
            return candidate;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {}
            }
            if (e.getMessage() != null && e.getMessage().contains("Duplicate")) {
                throw new DuplicateRecordException("A record with this email or Student ID already exists.");
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

    /**
     * Registers an Interviewer account (used by Admin).
     */
    public Interviewer registerInterviewer(String name, String rawEmail, String password,
                                           String expertise, int experienceYears,
                                           String specialization, String organization)
            throws InvalidEmailException, DuplicateRecordException, SQLException {

        String validName = InputValidator.validateNonBlank(name, "Name");
        String email = InputValidator.validateAndNormalizeEmail(rawEmail);
        InputValidator.validatePassword(password);
        String validExpertise = InputValidator.validateNonBlank(expertise, "Expertise");
        String validSpec = InputValidator.validateNonBlank(specialization, "Specialization");
        String validOrg = InputValidator.validateNonBlank(organization, "Organization");

        if (userDAO.emailExists(email)) {
            throw new DuplicateRecordException("Email '" + email + "' is already registered in the system.");
        }

        String salt = PasswordUtil.generateSalt();
        String hash = PasswordUtil.hashPassword(password, salt);

        Interviewer interviewer = new Interviewer();
        interviewer.setName(validName);
        interviewer.setEmail(email);
        interviewer.setPasswordHash(hash);
        interviewer.setPasswordSalt(salt);
        interviewer.setRole("INTERVIEWER");
        interviewer.setStatus("ACTIVE");
        interviewer.setExpertise(validExpertise);
        interviewer.setExperienceYears(Math.max(0, experienceYears));
        interviewer.setSpecialization(validSpec);
        interviewer.setOrganization(validOrg);

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            int userId = userDAO.createUser(interviewer, conn);
            interviewer.setUserId(userId);

            int interviewerId = interviewerDAO.createInterviewer(interviewer, conn);
            interviewer.setInterviewerId(interviewerId);

            conn.commit();
            return interviewer;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {}
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
}
