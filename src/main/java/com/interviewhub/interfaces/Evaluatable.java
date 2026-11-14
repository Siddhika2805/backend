package com.interviewhub.interfaces;

/**
 * Interface: Evaluatable
 * Implemented by entities that undergo or provide formal interview evaluation.
 */
public interface Evaluatable {
    /**
     * Gets the overall numerical score (0 to 100).
     *
     * @return overall score or null if not yet evaluated
     */
    Double getOverallScore();

    /**
     * Checks if the entity has been evaluated.
     *
     * @return true if completed and scored, false otherwise
     */
    boolean isEvaluated();

    /**
     * Generates a formatted evaluation summary string.
     *
     * @return detailed evaluation summary
     */
    String generateEvaluationSummary();
}
