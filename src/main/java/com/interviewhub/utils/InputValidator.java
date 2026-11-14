package com.interviewhub.utils;

import com.interviewhub.exceptions.InvalidEmailException;
import com.interviewhub.exceptions.InvalidScoreException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Utility for comprehensive data validation and string normalization.
 * Demonstrates proper Java string handling, wrapper classes, and custom exception triggers.
 */
public final class InputValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private InputValidator() {
        // Prevent instantiation
    }

    /**
     * Normalizes and validates an email address.
     * Trims whitespace, converts to lowercase, and validates pattern.
     *
     * @param email Raw email string
     * @return Normalized email
     * @throws InvalidEmailException if email is null, blank, or invalid format
     */
    public static String validateAndNormalizeEmail(String email) throws InvalidEmailException {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidEmailException("Email address cannot be blank or empty.");
        }
        String normalized = email.trim().toLowerCase();
        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new InvalidEmailException("Invalid email format: '" + email + "'. Example format: user@domain.com");
        }
        return normalized;
    }

    /**
     * Validates non-blank text fields like names or organization.
     */
    public static String validateNonBlank(String input, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank or empty.");
        }
        return input.trim();
    }

    /**
     * Validates a password meets minimum length requirement.
     */
    public static void validatePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters in length.");
        }
    }

    /**
     * Validates CGPA between 0.00 and 10.00.
     */
    public static double validateCgpa(Double cgpa) {
        if (cgpa == null || cgpa < 0.00 || cgpa > 10.00) {
            throw new IllegalArgumentException("CGPA must be between 0.00 and 10.00.");
        }
        return cgpa;
    }

    /**
     * Validates an evaluation score between 0.00 and 100.00.
     */
    public static double validateScore(Double score) throws InvalidScoreException {
        if (score == null || score < 0.00 || score > 100.00) {
            throw new InvalidScoreException("Evaluation score must be between 0.00 and 100.00. Provided: " + score);
        }
        return score;
    }

    /**
     * Validates interview type (TECHNICAL or HR).
     */
    public static String validateInterviewType(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Interview type cannot be null.");
        }
        String upper = type.trim().toUpperCase();
        if (!"TECHNICAL".equals(upper) && !"HR".equals(upper)) {
            throw new IllegalArgumentException("Interview type must be either 'TECHNICAL' or 'HR'.");
        }
        return upper;
    }

    /**
     * Parses an ISO date (YYYY-MM-DD).
     */
    public static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr.trim());
        } catch (DateTimeParseException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: YYYY-MM-DD (e.g., 2025-05-15).");
        }
    }

    /**
     * Parses an ISO time (HH:MM or HH:MM:SS).
     */
    public static LocalTime parseTime(String timeStr) {
        try {
            String trimmed = timeStr.trim();
            if (trimmed.length() == 5) {
                trimmed += ":00";
            }
            return LocalTime.parse(trimmed);
        } catch (DateTimeParseException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid time format. Expected format: HH:MM or HH:MM:SS (e.g., 14:30:00).");
        }
    }

    /**
     * Safely parses an integer or returns null on failure.
     */
    public static Integer parseInteger(String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(str.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Safely parses a double or returns null on failure.
     */
    public static Double parseDouble(String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        try {
            return Double.valueOf(str.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
