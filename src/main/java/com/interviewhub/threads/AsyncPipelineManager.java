package com.interviewhub.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Milestone 4: Producer-Consumer & Executor Framework Manager
 *
 * Demonstrates:
 * 1. Classical wait() / notifyAll() Producer-Consumer using TaskQueue.
 * 2. Java Concurrency ExecutorService with newFixedThreadPool().
 * 3. Callable tasks returning meaningful calculated evaluation results.
 * 4. Future.get() to asynchronously collect results.
 * 5. Orderly shutdown() and awaitTermination() lifecycle management.
 */
public class AsyncPipelineManager {

    /**
     * Demonstrates classic Producer-Consumer using wait() and notifyAll().
     */
    public static void runProducerConsumerSimulation() {
        System.out.println("==============================================================");
        System.out.println("        PRODUCER-CONSUMER PIPELINE SIMULATION (MILESTONE 4)    ");
        System.out.println("==============================================================");
        System.out.println("Demonstrating bounded TaskQueue (Capacity: 3), Producer, and Consumer.");
        System.out.println("--------------------------------------------------------------");

        TaskQueue taskQueue = new TaskQueue(3);

        List<InterviewTask> tasks = List.of(
                new InterviewTask(101, InterviewTask.TaskType.REQUEST_PROCESSING, "Candidate Rahul requesting Technical Round"),
                new InterviewTask(102, InterviewTask.TaskType.NOTIFICATION_DISPATCH, "Notify Interviewer Sarah of Pending Booking"),
                new InterviewTask(103, InterviewTask.TaskType.EVALUATION_REPORT_GENERATION, "Generate PDF Evaluation for Interview #1"),
                new InterviewTask(104, InterviewTask.TaskType.PERFORMANCE_ANALYTICS_CALCULATION, "Recalculate Growth Index for Candidate Ananya"),
                new InterviewTask(105, InterviewTask.TaskType.NOTIFICATION_DISPATCH, "Send Confirmation SMS/Email for Interview #4")
        );

        TaskProducer producerTask = new TaskProducer(taskQueue, tasks);
        TaskConsumer consumerTask1 = new TaskConsumer(taskQueue);
        TaskConsumer consumerTask2 = new TaskConsumer(taskQueue);

        Thread producerThread = new Thread(producerTask, "Producer-Thread");
        Thread consumerThread1 = new Thread(consumerTask1, "Consumer-Worker-1");
        Thread consumerThread2 = new Thread(consumerTask2, "Consumer-Worker-2");

        consumerThread1.start();
        consumerThread2.start();
        producerThread.start();

        try {
            // Wait for producer to finish creating tasks
            producerThread.join();

            // Allow brief window for consumers to clear remaining queue items
            Thread.sleep(600);

            // Signal shutdown to consumers so they can exit gracefully
            taskQueue.signalShutdown();

            consumerThread1.join();
            consumerThread2.join();

        } catch (InterruptedException e) {
            System.err.println("Simulation interrupted: " + e.getMessage());
        }

        System.out.println("--------------------------------------------------------------");
        System.out.println("[RESULT] Producer-Consumer pipeline completed successfully.");
        System.out.println("==============================================================");
    }

    /**
     * Demonstrates ExecutorService with Callable, Future, and proper shutdown.
     */
    public static void runExecutorServiceSimulation() {
        System.out.println("==============================================================");
        System.out.println("         EXECUTOR FRAMEWORK CONCURRENCY DEMO (MILESTONE 4)    ");
        System.out.println("==============================================================");
        System.out.println("Fixed Thread Pool of 3 worker threads executing Callable tasks.");
        System.out.println("--------------------------------------------------------------");

        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Define Callable tasks returning calculated results
        Callable<String> candidateTask1 = () -> {
            String thread = Thread.currentThread().getName();
            Thread.sleep(300); // Simulate analytical aggregation
            return String.format("[%s] Candidate Rahul Sharma: 4 interviews, Avg: 71.50, Growth: +63.64%%", thread);
        };

        Callable<String> candidateTask2 = () -> {
            String thread = Thread.currentThread().getName();
            Thread.sleep(200);
            return String.format("[%s] Candidate Ananya Patel: 2 interviews, Avg: 89.00, Growth: +5.88%%", thread);
        };

        Callable<String> interviewerTask = () -> {
            String thread = Thread.currentThread().getName();
            Thread.sleep(250);
            return String.format("[%s] Interviewer Dr. Sarah Jenkins: 4 conducted, Avg Score Awarded: 76.50", thread);
        };

        Callable<String> driveTask = () -> {
            String thread = Thread.currentThread().getName();
            Thread.sleep(150);
            return String.format("[%s] Placement Drive 'Google Summer SWE': 3 shortlisted applicants", thread);
        };

        List<Future<String>> futures = new ArrayList<>();
        futures.add(executor.submit(candidateTask1));
        futures.add(executor.submit(candidateTask2));
        futures.add(executor.submit(interviewerTask));
        futures.add(executor.submit(driveTask));

        System.out.println("[INFO] Submitted 4 Callable tasks to thread pool. Awaiting Future.get() results:\n");

        for (int i = 0; i < futures.size(); i++) {
            try {
                // Blocks until the specific worker thread returns the result
                String result = futures.get(i).get();
                System.out.println("  Result #" + (i + 1) + " -> " + result);
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Error obtaining future result: " + e.getMessage());
            }
        }

        // Mandatory clean shutdown
        System.out.println("\n[INFO] Initiating orderly executor shutdown...");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
                System.out.println("[WARN] Forcing shutdownNow()...");
                executor.shutdownNow();
            } else {
                System.out.println("[SUCCESS] All worker threads terminated cleanly.");
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        System.out.println("==============================================================");
    }
}
