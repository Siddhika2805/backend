package com.interviewhub.service;

import com.interviewhub.dao.*;
import com.interviewhub.exceptions.InterviewBookingException;
import com.interviewhub.exceptions.InterviewNotFoundException;
import com.interviewhub.exceptions.UnauthorizedAccessException;
import com.interviewhub.model.*;
import com.interviewhub.utils.InputValidator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Candidate Service Layer
 * Coordinates all candidate functionalities including booking, tracking, feedback review, and campus placements.
 */
public class CandidateService {

    private final CandidateDAO candidateDAO = new CandidateDAO();
    private final InterviewerDAO interviewerDAO = new InterviewerDAO();
    private final InterviewRequestDAO requestDAO = new InterviewRequestDAO();
    private final InterviewDAO interviewDAO = new InterviewDAO();
    private final FeedbackDAO feedbackDAO = new FeedbackDAO();
    private final PlacementDriveDAO driveDAO = new PlacementDriveDAO();
    private final ApplicationDAO applicationDAO = new ApplicationDAO();

    public Candidate getProfile(int candidateId) throws SQLException {
        return candidateDAO.getCandidateById(candidateId);
    }

    public boolean updateProfile(Candidate candidate) throws SQLException {
        InputValidator.validateNonBlank(candidate.getName(), "Name");
        InputValidator.validateNonBlank(candidate.getDepartment(), "Department");
        InputValidator.validateCgpa(candidate.getCgpa());
        InputValidator.validateNonBlank(candidate.getSpecialization(), "Specialization");
        return candidateDAO.updateCandidate(candidate);
    }

    public List<Interviewer> getAvailableInterviewers() throws SQLException {
        List<Interviewer> all = interviewerDAO.getAllInterviewers();
        List<Interviewer> active = new ArrayList<>();
        for (Interviewer i : all) {
            if (i.isActive()) {
                active.add(i);
            }
        }
        return active;
    }

    public List<Interviewer> searchInterviewers(String specialization) throws SQLException {
        if (specialization == null || specialization.trim().isEmpty()) {
            return getAvailableInterviewers();
        }
        return interviewerDAO.getInterviewersBySpecialization(specialization);
    }

    /**
     * Submits an interview booking request.
     * Enforces business rules:
     * - Interviewer must exist and be ACTIVE
     * - Preferred date must not be in the past
     * - Duplicate / conflicting active requests for the same candidate/interviewer/slot are rejected.
     */
    public InterviewRequest requestInterview(int candidateId, int interviewerId, String type,
                                             LocalDate date, LocalTime time)
            throws InterviewBookingException, SQLException {

        Interviewer interviewer = interviewerDAO.getInterviewerById(interviewerId);
        if (interviewer == null || !interviewer.isActive()) {
            throw new InterviewBookingException("Cannot request interview: Selected interviewer does not exist or is INACTIVE.");
        }

        if (date.isBefore(LocalDate.now())) {
            throw new InterviewBookingException("Invalid interview date: Preferred date cannot be in the past.");
        }

        String validatedType = InputValidator.validateInterviewType(type);

        if (requestDAO.hasConflictingSlot(candidateId, interviewerId, date, time)) {
            throw new InterviewBookingException("Conflicting booking: An active interview request or scheduled slot already exists for this date and time.");
        }

        InterviewRequest req = new InterviewRequest();
        req.setCandidateId(candidateId);
        req.setInterviewerId(interviewerId);
        req.setInterviewType(validatedType);
        req.setPreferredDate(date);
        req.setPreferredTime(time);
        req.setRequestStatus("PENDING");

        int id = requestDAO.createRequest(req);
        req.setRequestId(id);
        return req;
    }

    public List<InterviewRequest> getMyRequests(int candidateId) throws SQLException {
        return requestDAO.getRequestsByCandidate(candidateId);
    }

    public boolean cancelInterviewRequest(int requestId, int candidateId)
            throws UnauthorizedAccessException, InterviewBookingException, SQLException {
        InterviewRequest req = requestDAO.getRequestById(requestId);
        if (req == null) {
            throw new InterviewBookingException("Interview request #" + requestId + " not found.");
        }
        if (req.getCandidateId() != candidateId) {
            throw new UnauthorizedAccessException("Unauthorized: You can only cancel your own interview requests.");
        }
        if (!"PENDING".equalsIgnoreCase(req.getRequestStatus())) {
            throw new InterviewBookingException("Only PENDING requests can be cancelled. Current status: " + req.getRequestStatus());
        }
        return requestDAO.updateRequestStatus(requestId, "CANCELLED", null);
    }

    public List<Interview> getUpcomingInterviews(int candidateId) throws SQLException {
        List<Interview> all = interviewDAO.getInterviewsByCandidate(candidateId);
        List<Interview> upcoming = new ArrayList<>();
        for (Interview inv : all) {
            if ("SCHEDULED".equalsIgnoreCase(inv.getInterviewStatus())) {
                upcoming.add(inv);
            }
        }
        return upcoming;
    }

    public List<Interview> getInterviewHistory(int candidateId) throws SQLException {
        return interviewDAO.getInterviewsByCandidate(candidateId);
    }

    public InterviewFeedback getInterviewFeedback(int interviewId, int candidateId)
            throws UnauthorizedAccessException, InterviewNotFoundException, SQLException {
        Interview inv = interviewDAO.getInterviewById(interviewId);
        if (inv == null) {
            throw new InterviewNotFoundException("Interview #" + interviewId + " does not exist.");
        }
        if (inv.getCandidateId() != candidateId) {
            throw new UnauthorizedAccessException("Access denied: You are not authorized to view feedback for another candidate's interview.");
        }
        return feedbackDAO.getFeedbackByInterviewId(interviewId);
    }

    public CandidateStatistics getPerformanceStatistics(int candidateId) throws SQLException {
        return feedbackDAO.getCandidateStatistics(candidateId);
    }

    // ==================== PLACEMENT DRIVE & ELIGIBILITY INTEGRATION ====================

    public List<PlacementDrive> getAvailablePlacementDrives() throws SQLException {
        return driveDAO.getAllDrives();
    }

    /**
     * Applies to a campus placement drive after validating eligibility via Eligible interface.
     */
    public Application applyToDrive(int candidateId, int driveId) throws SQLException, InterviewBookingException {
        Candidate candidate = candidateDAO.getCandidateById(candidateId);
        PlacementDrive drive = driveDAO.getDriveById(driveId);

        if (candidate == null || drive == null) {
            throw new InterviewBookingException("Invalid candidate or placement drive ID.");
        }

        if (!"OPEN".equalsIgnoreCase(drive.getStatus())) {
            throw new InterviewBookingException("Placement drive '" + drive.getDriveName() + "' is currently closed for applications.");
        }

        if (drive.getApplicationDeadline().isBefore(LocalDate.now())) {
            throw new InterviewBookingException("Application deadline (" + drive.getApplicationDeadline() + ") has already passed.");
        }

        if (applicationDAO.hasExistingApplication(candidateId, driveId)) {
            throw new InterviewBookingException("You have already submitted an application to this placement drive.");
        }

        // Evaluate eligibility using the Eligible interface method
        if (!drive.isEligible(candidate)) {
            throw new InterviewBookingException(String.format(
                    "Eligibility criteria not met! Required: Min CGPA %.2f & Department '%s'. Your Profile: CGPA %.2f & Department '%s'.",
                    drive.getEligibilityCgpa(), drive.getEligibleDepartment(), candidate.getCgpa(), candidate.getDepartment()));
        }

        Application app = new Application();
        app.setCandidateId(candidateId);
        app.setDriveId(driveId);
        app.setApplicationStatus("APPLIED");

        int appId = applicationDAO.createApplication(app);
        app.setApplicationId(appId);
        return app;
    }

    public List<Application> getMyApplications(int candidateId) throws SQLException {
        return applicationDAO.getApplicationsByCandidate(candidateId);
    }
}
