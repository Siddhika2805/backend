package com.interviewhub.oop;

import com.interviewhub.exceptions.InvalidEmailException;
import com.interviewhub.exceptions.InvalidScoreException;
import com.interviewhub.model.*;
import com.interviewhub.utils.InputValidator;
import com.interviewhub.utils.PasswordUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Milestone 1 Verification Test
 * Tests OOP Inheritance, Eligible interface, Evaluatable interface,
 * PBKDF2 Password Hashing, Input Normalization, and Custom Exceptions.
 */
public class ModelValidationTest {

    @Test
    public void testCandidateInheritanceAndPolymorphism() {
        Candidate candidate = new Candidate();
        candidate.setUserId(10);
        candidate.setName("Rahul Sharma");
        candidate.setEmail("rahul@college.edu");
        candidate.setStudentId("STU2025");
        candidate.setDepartment("Computer Science");
        candidate.setCgpa(8.85);
        candidate.setGraduationYear(2025);
        candidate.setSpecialization("Software Engineering");

        // Verify Student & User inheritance
        assertTrue(candidate instanceof Student);
        assertTrue(candidate instanceof User);
        assertEquals("CANDIDATE", candidate.getRole());

        // Verify polymorphic method overriding
        String details = candidate.getRoleDetails();
        assertTrue(details.contains("Candidate ID"));
        assertTrue(details.contains("Computer Science"));
    }

    @Test
    public void testEligibleInterfaceImplementation() {
        PlacementDrive drive = new PlacementDrive();
        drive.setDriveName("Google SWE Drive");
        drive.setEligibilityCgpa(8.00);
        drive.setEligibleDepartment("Computer Science");
        drive.setApplicationDeadline(LocalDate.now().plusMonths(2));
        drive.setStatus("OPEN");

        Student eligibleStudent = new Student();
        eligibleStudent.setCgpa(8.50);
        eligibleStudent.setDepartment("Computer Science");

        Student ineligibleCgpa = new Student();
        ineligibleCgpa.setCgpa(7.50);
        ineligibleCgpa.setDepartment("Computer Science");

        Student ineligibleDept = new Student();
        ineligibleDept.setCgpa(9.00);
        ineligibleDept.setDepartment("Civil Engineering");

        assertTrue(drive.isEligible(eligibleStudent), "Student with 8.5 CGPA in CS should be eligible");
        assertFalse(drive.isEligible(ineligibleCgpa), "Student with 7.5 CGPA should NOT be eligible");
        assertFalse(drive.isEligible(ineligibleDept), "Student in Civil Engineering should NOT be eligible");
    }

    @Test
    public void testEmailValidationAndNormalization() throws InvalidEmailException {
        String normalized = InputValidator.validateAndNormalizeEmail("   Rahul.Sharma@College.EDU   ");
        assertEquals("rahul.sharma@college.edu", normalized);

        assertThrows(InvalidEmailException.class, () -> {
            InputValidator.validateAndNormalizeEmail("invalid-email-string");
        });
        assertThrows(InvalidEmailException.class, () -> {
            InputValidator.validateAndNormalizeEmail("");
        });
    }

    @Test
    public void testScoreValidationCustomException() {
        assertDoesNotThrow(() -> InputValidator.validateScore(85.50));
        assertThrows(InvalidScoreException.class, () -> InputValidator.validateScore(-5.0));
        assertThrows(InvalidScoreException.class, () -> InputValidator.validateScore(105.0));
    }

    @Test
    public void testPasswordHashingPbkdf2() {
        String salt = PasswordUtil.generateSalt();
        assertNotNull(salt);

        String hash1 = PasswordUtil.hashPassword("Secret@123", salt);
        String hash2 = PasswordUtil.hashPassword("Secret@123", salt);
        assertEquals(hash1, hash2, "Identical password and salt must produce the same hash");

        assertTrue(PasswordUtil.verifyPassword("Secret@123", hash1, salt));
        assertFalse(PasswordUtil.verifyPassword("WrongPassword", hash1, salt));
    }
}
