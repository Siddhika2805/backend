package com.interviewhub.threads;

import java.util.List;

/**
 * Milestone 4: Producer thread that creates and enqueues interview processing tasks.
 */
public class TaskProducer implements Runnable {

    private final TaskQueue taskQueue;
    private final List<InterviewTask> tasksToProduce;

    public TaskProducer(TaskQueue taskQueue, List<InterviewTask> tasksToProduce) {
        this.taskQueue = taskQueue;
        this.tasksToProduce = tasksToProduce;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        try {
            for (InterviewTask task : tasksToProduce) {
                taskQueue.produce(task);
                Thread.sleep(100); // Simulate arrival intervals
            }
            System.out.printf("[PRODUCER FINISHED] Thread [%s] finished enqueuing all %d tasks.%n",
                    threadName, tasksToProduce.size());
        } catch (InterruptedException e) {
            System.out.printf("[PRODUCER INTERRUPTED] Thread [%s] was interrupted.%n", threadName);
            Thread.currentThread().interrupt();
        }
    }
}
