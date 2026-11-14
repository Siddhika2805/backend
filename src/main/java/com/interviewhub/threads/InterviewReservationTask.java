package com.interviewhub.threads;

import java.util.ArrayList;
import java.util.List;

/**
 * Milestone 3: Runnable task simulating concurrent candidate interview slot reservations.
 * Demonstrates Thread lifecycle, start(), join(), synchronized access, and thread safety verification.
 */
public class InterviewReservationTask implements Runnable {

    private final SlotManager slotManager;
    private final String candidateName;
    private boolean reservationResult;

    public InterviewReservationTask(SlotManager slotManager, String candidateName) {
        this.slotManager = slotManager;
        this.candidateName = candidateName;
    }

    @Override
    public void run() {
        this.reservationResult = slotManager.reserveSlot(candidateName);
    }

    public boolean isReservationResult() {
        return reservationResult;
    }

    public String getCandidateName() {
        return candidateName;
    }

    /**
     * Executes the mandatory Milestone 3 concurrency test:
     * - Available Slots: 2
     * - Concurrent Candidate Requests: 5
     *
     * Expected: Exactly 2 succeed, 3 rejected, remaining slots = 0, no negative count.
     */
    public static void runConcurrencySimulation() {
        System.out.println("==============================================================");
        System.out.println("      MULTITHREADING & SYNCHRONIZATION SIMULATION (MILESTONE 3) ");
        System.out.println("==============================================================");
        System.out.println("Test Parameters:");
        System.out.println("  - Available Interview Slots : 2");
        System.out.println("  - Concurrent Candidate Tasks: 5");
        System.out.println("--------------------------------------------------------------");

        SlotManager slotManager = new SlotManager(2);
        String[] candidates = {
                "Candidate-Rahul (STU01)",
                "Candidate-Ananya (STU02)",
                "Candidate-Vikram (STU03)",
                "Candidate-Neha (STU04)",
                "Candidate-Arjun (STU05)"
        };

        List<Thread> threadList = new ArrayList<>();
        List<InterviewReservationTask> tasks = new ArrayList<>();

        // Create 5 concurrent threads
        for (int i = 0; i < candidates.length; i++) {
            InterviewReservationTask task = new InterviewReservationTask(slotManager, candidates[i]);
            tasks.add(task);
            Thread thread = new Thread(task, "WorkerThread-" + (i + 1));
            threadList.add(thread);
        }

        System.out.println("[INFO] Spawning threads concurrently with start()...");
        long startTime = System.currentTimeMillis();

        // 1. Invoke start() to create and schedule separate OS execution threads
        // NOTE: If we invoked run() directly, it would execute sequentially on the main thread!
        for (Thread t : threadList) {
            t.start();
        }

        // 2. Invoke join() to wait for all threads to terminate before checking results
        for (Thread t : threadList) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.err.println("Thread join interrupted: " + e.getMessage());
            }
        }

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("--------------------------------------------------------------");
        System.out.printf("Simulation completed in %d ms.%n", duration);
        System.out.printf("Remaining Available Slots : %d (Must be 0)%n", slotManager.getAvailableSlots());
        System.out.printf("Successful Reservations   : %d (Must be 2)%n", slotManager.getSuccessfulReservations().size());
        System.out.printf("Rejected Requests         : %d (Must be 3)%n", slotManager.getRejectedReservations().size());

        System.out.println("\nSummary of Successful Bookings:");
        for (String name : slotManager.getSuccessfulReservations()) {
            System.out.println("  + " + name);
        }

        System.out.println("Summary of Rejected Requests:");
        for (String name : slotManager.getRejectedReservations()) {
            System.out.println("  - " + name);
        }

        System.out.println("==============================================================");
        System.out.println("VIVA / EVALUATION CONCEPTS EXPLAINED:");
        System.out.println("1. Race Condition: Occurs when multiple threads attempt to read and write");
        System.out.println("   shared mutable state (availableSlots) concurrently without coordination.");
        System.out.println("2. Critical Section: The code region where availableSlots is checked");
        System.out.println("   and decremented (reserveSlot method).");
        System.out.println("3. Synchronization: The 'synchronized' keyword acquires the intrinsic lock");
        System.out.println("   (monitor) on the SlotManager instance, enforcing mutual exclusion (mutex).");
        System.out.println("4. start() vs run(): start() requests the JVM to spin up a new operating");
        System.out.println("   system thread and invokes run() asynchronously. Calling run() directly");
        System.out.println("   would run synchronously on the caller thread without any concurrency.");
        System.out.println("==============================================================");
    }
}
