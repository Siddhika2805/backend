package com.interviewhub.threads;

/**
 * Milestone 4: Consumer thread that dequeues and processes interview tasks.
 */
public class TaskConsumer implements Runnable {

    private final TaskQueue taskQueue;
    private int processedCount = 0;

    public TaskConsumer(TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        try {
            while (true) {
                InterviewTask task = taskQueue.consume();
                if (task == null) {
                    // Queue has been shut down and is empty
                    break;
                }

                // Simulate processing of interview background tasks
                processTask(task, threadName);
                processedCount++;
                Thread.sleep(150);
            }
            System.out.printf("[CONSUMER FINISHED] Thread [%s] gracefully stopped. Processed %d tasks.%n",
                    threadName, processedCount);
        } catch (InterruptedException e) {
            System.out.printf("[CONSUMER INTERRUPTED] Thread [%s] was interrupted.%n", threadName);
            Thread.currentThread().interrupt();
        }
    }

    private void processTask(InterviewTask task, String worker) {
        switch (task.getTaskType()) {
            case REQUEST_PROCESSING:
                System.out.printf("    ==> [%s] Validating slot and interviewer schedule for %s%n",
                        worker, task.getDescription());
                break;
            case NOTIFICATION_DISPATCH:
                System.out.printf("    ==> [%s] Dispatching automated notification for %s%n",
                        worker, task.getDescription());
                break;
            case EVALUATION_REPORT_GENERATION:
                System.out.printf("    ==> [%s] Compiling rubric scores and written feedback for %s%n",
                        worker, task.getDescription());
                break;
            case PERFORMANCE_ANALYTICS_CALCULATION:
                System.out.printf("    ==> [%s] Computing historical averages and improvement trend for %s%n",
                        worker, task.getDescription());
                break;
        }
    }

    public int getProcessedCount() {
        return processedCount;
    }
}
