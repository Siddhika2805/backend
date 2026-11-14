package com.interviewhub.service;

import com.interviewhub.dao.*;
import com.interviewhub.exceptions.DuplicateRecordException;
import com.interviewhub.exceptions.InvalidEmailException;
import com.interviewhub.model.*;
import com.interviewhub.utils.InputValidator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Admin Service Layer
 * Provides governance, user administration, placement drive coordination,
 * question bank management, and global system analytics.
 */
public class AdminService {

    private final UserDAO userDAO = new UserDAO();
    private final CandidateDAO candidateDAO = new CandidateDAO();
    private final InterviewerDAO interviewerDAO = new InterviewerDAO();
    private final InterviewDAO interviewDAO = new InterviewDAO();
    private final InterviewRequestDAO requestDAO = new InterviewRequestDAO();
    private final QuestionDAO questionDAO = new QuestionDAO();
    private final CompanyDAO companyDAO = new CompanyDAO();
    private final PlacementDriveDAO driveDAO = new PlacementDriveDAO();
    private final ApplicationDAO applicationDAO = new ApplicationDAO();
    private final FeedbackDAO feedbackDAO = new FeedbackDAO();
    private final AuthService authService = new AuthService();

    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }

    public boolean toggleUserStatus(int userId, String newStatus) throws SQLException {
        if (!"ACTIVE".equalsIgnoreCase(newStatus) && !"INACTIVE".equalsIgnoreCase(newStatus)) {
            throw new IllegalArgumentException("Status must be either 'ACTIVE' or 'INACTIVE'.");
        }
        return userDAO.updateUserStatus(userId, newStatus.toUpperCase());
    }

    public List<Candidate> getAllCandidates() throws SQLException {
        return candidateDAO.getAllCandidates();
    }

    public List<Interviewer> getAllInterviewers() throws SQLException {
        return interviewerDAO.getAllInterviewers();
    }

    public Interviewer addInterviewer(String name, String email, String password,
                                      String expertise, int expYears,
                                      String specialization, String organization)
            throws InvalidEmailException, DuplicateRecordException, SQLException {
        return authService.registerInterviewer(name, email, password, expertise, expYears, specialization, organization);
    }

    public boolean updateInterviewer(Interviewer interviewer) throws SQLException {
        return interviewerDAO.updateInterviewer(interviewer);
    }

    // ==================== QUESTION BANK ====================

    public Question addQuestion(String text, String category, String difficulty, String specialization, int adminUserId)
            throws SQLException {
        InputValidator.validateNonBlank(text, "Question Text");
        String cat = InputValidator.validateInterviewType(category);
        InputValidator.validateNonBlank(specialization, "Specialization");

        Question q = new Question();
        q.setQuestionText(text.trim());
        q.setCategory(cat);
        q.setDifficulty(difficulty != null ? difficulty.toUpperCase() : "MEDIUM");
        q.setSpecialization(specialization.trim());
        q.setCreatedBy(adminUserId);

        int id = questionDAO.createQuestion(q);
        q.setQuestionId(id);
        return q;
    }

    public List<Question> getAllQuestions() throws SQLException {
        return questionDAO.getAllQuestions();
    }

    public boolean updateQuestion(Question question) throws SQLException {
        return questionDAO.updateQuestion(question);
    }

    public boolean deleteQuestion(int questionId) throws SQLException {
        return questionDAO.deleteQuestion(questionId);
    }

    // ==================== INTERVIEWS & REQUESTS ====================

    public List<InterviewRequest> getAllRequests() throws SQLException {
        return requestDAO.getAllRequests();
    }

    public List<Interview> getAllInterviews() throws SQLException {
        return interviewDAO.getAllInterviews();
    }

    // ==================== PLACEMENT & COMPANIES ====================

    public Company addCompany(String name, String industry, String location) throws SQLException {
        InputValidator.validateNonBlank(name, "Company Name");
        InputValidator.validateNonBlank(industry, "Industry");
        InputValidator.validateNonBlank(location, "Location");

        Company c = new Company(name.trim(), industry.trim(), location.trim());
        int id = companyDAO.createCompany(c);
        c.setCompanyId(id);
        return c;
    }

    public List<Company> getAllCompanies() throws SQLException {
        return companyDAO.getAllCompanies();
    }

    public PlacementDrive createPlacementDrive(int companyId, String driveName, double minCgpa,
                                              String department, LocalDate deadline) throws SQLException {
        InputValidator.validateNonBlank(driveName, "Drive Name");
        InputValidator.validateCgpa(minCgpa);
        InputValidator.validateNonBlank(department, "Eligible Department");

        PlacementDrive drive = new PlacementDrive();
        drive.setCompanyId(companyId);
        drive.setDriveName(driveName.trim());
        drive.setEligibilityCgpa(minCgpa);
        drive.setEligibleDepartment(department.trim());
        drive.setApplicationDeadline(deadline);
        drive.setStatus("OPEN");

        int id = driveDAO.createDrive(drive);
        drive.setDriveId(id);
        return drive;
    }

    public List<PlacementDrive> getAllPlacementDrives() throws SQLException {
        return driveDAO.getAllDrives();
    }

    public boolean updatePlacementDriveStatus(int driveId, String status) throws SQLException {
        return driveDAO.updateDriveStatus(driveId, status.toUpperCase());
    }

    public List<Application> getAllApplications() throws SQLException {
        return applicationDAO.getAllApplications();
    }

    public boolean updateApplicationStatus(int applicationId, String status) throws SQLException {
        return applicationDAO.updateApplicationStatus(applicationId, status.toUpperCase());
    }

    // ==================== ANALYTICS ====================

    public CandidateStatistics getCandidateStatistics(int candidateId) throws SQLException {
        return feedbackDAO.getCandidateStatistics(candidateId);
    }

    public InterviewerStatistics getInterviewerStatistics(int interviewerId) throws SQLException {
        return feedbackDAO.getInterviewerStatistics(interviewerId);
    }

    public SystemStatistics getSystemStatistics() throws SQLException {
        return feedbackDAO.getSystemStatistics();
    }
}
