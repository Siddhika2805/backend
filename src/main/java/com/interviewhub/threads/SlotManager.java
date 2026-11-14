package com.interviewhub.threads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Milestone 3: Multithreading & Synchronization
 *
 * SlotManager represents a shared resource maintaining a finite quota of interview slots.
 *
 * Concepts Demonstrated:
 * 1. Critical Section: The block of code where availableSlots is checked and decremented.
 * 2. Race Condition: Without synchronization, multiple concurrent threads could check availableSlots > 0
 *    simultaneously, leading to over-allocation (negative slots or > quota accepted).
 * 3. Thread Safety: Guaranteed using synchronized methods to make check-and-decrement atomic.
 */
public class SlotManager {

    private int availableSlots;
    private final int totalSlots;
    private final List<String> successfulReservations = Collections.synchronizedList(new ArrayList<>());
    private final List<String> rejectedReservations = Collections.synchronizedList(new ArrayList<>());

    public SlotManager(int totalSlots) {
        if (totalSlots < 0) {
            throw new IllegalArgumentException("Total slots cannot be negative.");
        }
        this.totalSlots = totalSlots;
        this.availableSlots = totalSlots;
    }

    /**
     * Synchronized Critical Section.
     * Atomically inspects remaining capacity, decrements available count, and logs the result.
     *
     * @param candidateName Name or identifier of candidate making the request
     * @return true if reservation succeeded, false if rejected due to full capacity
     */
    public synchronized boolean reserveSlot(String candidateName) {
        String threadName = Thread.currentThread().getName();

        // 1. Check whether a slot is available
        if (availableSlots > 0) {
            // Simulate brief processing time to show concurrency handling
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
            }

            // 2. Reserve slot & 3. Decrease available slot count
            availableSlots--;
            successfulReservations.add(candidateName);
            System.out.printf("  [SUCCESS] Thread [%s] reserved slot for [%s]. Remaining slots: %d%n",
                    threadName, candidateName, availableSlots);
            return true;
        } else {
            // 4. Reject request if no slots remain
            rejectedReservations.add(candidateName);
            System.out.printf("  [REJECTED] Thread [%s] failed for [%s]. No slots remaining (0/%d).%n",
                    threadName, candidateName, totalSlots);
            return false;
        }
    }

    public synchronized int getAvailableSlots() {
        return availableSlots;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public List<String> getSuccessfulReservations() {
        return new ArrayList<>(successfulReservations);
    }

    public List<String> getRejectedReservations() {
        return new ArrayList<>(rejectedReservations);
    }

    public synchronized void reset(int newTotalSlots) {
        this.availableSlots = newTotalSlots;
        this.successfulReservations.clear();
        this.rejectedReservations.clear();
    }
}
