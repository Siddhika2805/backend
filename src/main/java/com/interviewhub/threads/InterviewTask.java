package com.interviewhub.threads;

/**
 * Task entity processed by the Producer-Consumer pipeline.
 */
public class InterviewTask {

    public enum TaskType {
        REQUEST_PROCESSING,
        NOTIFICATION_DISPATCH,
        EVALUATION_REPORT_GENERATION,
        PERFORMANCE_ANALYTICS_CALCULATION
    }

    private final int taskId;
    private final TaskType taskType;
    private final String description;
    private final long createdAt;

    public InterviewTask(int taskId, TaskType taskType, String description) {
        this.taskId = taskId;
        this.taskType = taskType;
        this.description = description;
        this.createdAt = System.currentTimeMillis();
    }

    public int getTaskId() {
        return taskId;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public String getDescription() {
        return description;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return String.format("Task #%d [%s]: %s", taskId, taskType, description);
    }
}
