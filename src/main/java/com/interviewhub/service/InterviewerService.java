package com.interviewhub.service;

import com.interviewhub.dao.*;
import com.interviewhub.database.DBConnection;
import com.interviewhub.exceptions.InterviewBookingException;
import com.interviewhub.exceptions.InterviewNotFoundException;
import com.interviewhub.exceptions.InvalidScoreException;
import com.interviewhub.exceptions.UnauthorizedAccessException;
import com.interviewhub.model.*;
import com.interviewhub.utils.InputValidator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Interviewer Service Layer
 * Coordinates interview request management, conducting interviews, evaluation submission,
 * and interviewer statistics.
 */
public class InterviewerService {

    private final InterviewerDAO interviewerDAO = new InterviewerDAO();
    private final InterviewRequestDAO requestDAO = new InterviewRequestDAO();
    private final InterviewDAO interviewDAO = new InterviewDAO();
    private final FeedbackDAO feedbackDAO = new FeedbackDAO();
    private final QuestionDAO questionDAO = new QuestionDAO();

    public Interviewer getProfile(int interviewerId) throws SQLException {
        return interviewerDAO.getInterviewerById(interviewerId);
    }

    public boolean updateProfile(Interviewer interviewer) throws SQLException {
        InputValidator.validateNonBlank(interviewer.getName(), "Name");
        InputValidator.validateNonBlank(interviewer.getExpertise(), "Expertise");
        InputValidator.validateNonBlank(interviewer.getSpecialization(), "Specialization");
        InputValidator.validateNonBlank(interviewer.getOrganization(), "Organization");
        return interviewerDAO.updateInterviewer(interviewer);
    }

    public List<InterviewRequest> getPendingRequests(int interviewerId) throws SQLException {
        List<InterviewRequest> all = requestDAO.getRequestsByInterviewer(interviewerId);
        List<InterviewRequest> pending = new ArrayList<>();
        for (InterviewRequest r : all) {
            if ("PENDING".equalsIgnoreCase(r.getRequestStatus())) {
                pending.add(r);
            }
        }
        return pending;
    }

    public List<InterviewRequest> getAllRequests(int interviewerId) throws SQLException {
        return requestDAO.getRequestsByInterviewer(interviewerId);
    }

    /**
     * Accepts an interview request and creates a SCHEDULED interview record atomically.
     */
    public Interview acceptRequest(int requestId, int interviewerId)
            throws UnauthorizedAccessException, InterviewBookingException, SQLException {

        InterviewRequest req = requestDAO.getRequestById(requestId);
        if (req == null) {
            throw new InterviewBookingException("Request #" + requestId + " not found.");
        }

        if (req.getInterviewerId() != interviewerId) {
            throw new UnauthorizedAccessException("Unauthorized: This interview request is addressed to another interviewer.");
        }

        if (!"PENDING".equalsIgnoreCase(req.getRequestStatus())) {
            throw new InterviewBookingException("Cannot accept request: Request is already " + req.getRequestStatus());
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            // 1. Update request status to ACCEPTED
            requestDAO.updateRequestStatus(requestId, "ACCEPTED", conn);

            // 2. Create corresponding SCHEDULED Interview record
            Interview interview = new Interview();
            interview.setCandidateId(req.getCandidateId());
            interview.setInterviewerId(req.getInterviewerId());
            interview.setRequestId(req.getRequestId());
            interview.setInterviewType(req.getInterviewType());
            interview.setInterviewDate(req.getPreferredDate());
            interview.setInterviewTime(req.getPreferredTime());
            interview.setInterviewStatus("SCHEDULED");
            interview.setOverallScore(null);

            int interviewId = interviewDAO.createInterview(interview, conn);
            interview.setInterviewId(interviewId);

            conn.commit(); // Commit transaction
            return interview;

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

    /**
     * Rejects an interview request.
     */
    public boolean rejectRequest(int requestId, int interviewerId)
            throws UnauthorizedAccessException, InterviewBookingException, SQLException {

        InterviewRequest req = requestDAO.getRequestById(requestId);
        if (req == null) {
            throw new InterviewBookingException("Request #" + requestId + " not found.");
        }

        if (req.getInterviewerId() != interviewerId) {
            throw new UnauthorizedAccessException("Unauthorized: This request belongs to another interviewer.");
        }

        if (!"PENDING".equalsIgnoreCase(req.getRequestStatus())) {
            throw new InterviewBookingException("Cannot reject request: Request is already " + req.getRequestStatus());
        }

        return requestDAO.updateRequestStatus(requestId, "REJECTED", null);
    }

    public List<Interview> getScheduledInterviews(int interviewerId) throws SQLException {
        List<Interview> all = interviewDAO.getInterviewsByInterviewer(interviewerId);
        List<Interview> scheduled = new ArrayList<>();
        for (Interview inv : all) {
            if ("SCHEDULED".equalsIgnoreCase(inv.getInterviewStatus())) {
                scheduled.add(inv);
            }
        }
        return scheduled;
    }

    public List<Interview> getInterviewHistory(int interviewerId) throws SQLException {
        return interviewDAO.getInterviewsByInterviewer(interviewerId);
    }

    /**
     * Submits evaluation feedback and granular skill scores for an interview.
     * Enforces:
     * - Interview must exist and be assigned to THIS interviewer
     * - Interview must be in SCHEDULED status
     * - Overall score and skill scores must be within 0.00 and 100.00
     * - Executes inside an atomic JDBC transaction.
     */
    public boolean submitEvaluation(int interviewId, int interviewerId, String feedbackText,
                                    double overallScore, List<SkillScore> skillScores)
            throws UnauthorizedAccessException, InterviewNotFoundException, InvalidScoreException, SQLException {

        Interview inv = interviewDAO.getInterviewById(interviewId);
        if (inv == null) {
            throw new InterviewNotFoundException("Interview #" + interviewId + " does not exist.");
        }

        if (inv.getInterviewerId() != interviewerId) {
            throw new UnauthorizedAccessException("Forbidden: You cannot evaluate an interview conducted by another interviewer.");
        }

        if (!"SCHEDULED".equalsIgnoreCase(inv.getInterviewStatus())) {
            throw new IllegalStateException("Interview has already been completed or cancelled.");
        }

        InputValidator.validateNonBlank(feedbackText, "Feedback Text");
        InputValidator.validateScore(overallScore);

        if (skillScores != null) {
            for (SkillScore ss : skillScores) {
                InputValidator.validateNonBlank(ss.getSkillName(), "Skill Name");
                InputValidator.validateScore(ss.getScore());
            }
        }

        InterviewFeedback feedback = new InterviewFeedback();
        feedback.setInterviewId(interviewId);
        feedback.setInterviewerId(interviewerId);
        feedback.setFeedbackText(feedbackText);
        feedback.setOverallScore(overallScore);

        return feedbackDAO.submitEvaluationTransaction(feedback, skillScores);
    }

    public InterviewerStatistics getStatistics(int interviewerId) throws SQLException {
        return feedbackDAO.getInterviewerStatistics(interviewerId);
    }

    public List<Question> getQuestionsByCategory(String category) throws SQLException {
        return questionDAO.getQuestionsByCategory(category);
    }

    public List<Question> getAllQuestions() throws SQLException {
        return questionDAO.getAllQuestions();
    }
}
