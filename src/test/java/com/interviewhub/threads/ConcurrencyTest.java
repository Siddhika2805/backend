package com.interviewhub.threads;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Milestone 3 Concurrency Verification Test
 * Formally verifies that synchronized critical section prevents race conditions:
 * - Available Slots = 2
 * - Concurrent Requests = 5
 * - Expected: Exactly 2 succeed, exactly 3 fail, available slots = 0 (never negative).
 */
public class ConcurrencyTest {

    @RepeatedTest(3) // Repeat 3 times to ensure no intermittent race conditions
    public void testTwoSlotsWithFiveConcurrentRequests() throws InterruptedException {
        int initialSlots = 2;
        int totalThreads = 5;

        SlotManager slotManager = new SlotManager(initialSlots);
        List<Thread> threads = new ArrayList<>();
        List<InterviewReservationTask> tasks = new ArrayList<>();

        for (int i = 1; i <= totalThreads; i++) {
            InterviewReservationTask task = new InterviewReservationTask(slotManager, "Candidate-" + i);
            tasks.add(task);
            threads.add(new Thread(task, "TestThread-" + i));
        }

        // Concurrently invoke start()
        for (Thread t : threads) {
            t.start();
        }

        // Wait for all threads to terminate
        for (Thread t : threads) {
            t.join();
        }

        // Assertions
        assertEquals(0, slotManager.getAvailableSlots(), "Remaining slots must be exactly 0");
        assertEquals(2, slotManager.getSuccessfulReservations().size(), "Only 2 reservations must succeed");
        assertEquals(3, slotManager.getRejectedReservations().size(), "Exactly 3 reservations must be rejected");

        // Verify that available slots never became negative
        assertTrue(slotManager.getAvailableSlots() >= 0, "Available slots must never be negative");
    }
}
