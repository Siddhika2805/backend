package com.interviewhub.interfaces;

import com.interviewhub.model.Student;

/**
 * Academic Rubric Interface: Eligible
 * Defines eligibility evaluation for student placement drives.
 */
public interface Eligible {
    /**
     * Determines whether a given student meets the eligibility criteria.
     *
     * @param student the student candidate to evaluate
     * @return true if eligible, false otherwise
     */
    boolean isEligible(Student student);
}
