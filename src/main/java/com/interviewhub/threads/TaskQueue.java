package com.interviewhub.threads;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Milestone 4: Thread-safe Shared Task Queue for Producer-Consumer pattern.
 * Demonstrates:
 * 1. Synchronized intrinsic locking.
 * 2. wait() guarded by while-loop to prevent spurious wakeups.
 * 3. notifyAll() to wake up waiting producer or consumer threads.
 */
public class TaskQueue {

    private final Queue<InterviewTask> queue = new LinkedList<>();
    private final int capacity;
    private boolean shutdown = false;

    public TaskQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Queue capacity must be greater than zero.");
        }
        this.capacity = capacity;
    }

    /**
     * Producer method: Inserts an interview task into the queue.
     * Blocks if the queue has reached full capacity.
     */
    public synchronized void produce(InterviewTask task) throws InterruptedException {
        // While loop protects against spurious wakeups (Java Concurrency Best Practice)
        while (queue.size() == capacity && !shutdown) {
            System.out.printf("  [PRODUCER WAIT] Queue is full (%d/%d). Thread [%s] is waiting...%n",
                    queue.size(), capacity, Thread.currentThread().getName());
            wait();
        }

        if (shutdown) {
            return;
        }

        queue.add(task);
        System.out.printf("  [PRODUCED] Thread [%s] added %s. (Queue size: %d/%d)%n",
                Thread.currentThread().getName(), task, queue.size(), capacity);

        // Notify consumer threads that a task is now available
        notifyAll();
    }

    /**
     * Consumer method: Retrieves and removes an interview task from the queue.
     * Blocks if the queue is currently empty.
     */
    public synchronized InterviewTask consume() throws InterruptedException {
        while (queue.isEmpty() && !shutdown) {
            wait();
        }

        if (queue.isEmpty() && shutdown) {
            return null; // Signals consumer to gracefully exit
        }

        InterviewTask task = queue.poll();
        System.out.printf("  [CONSUMED] Thread [%s] took %s. (Queue size: %d/%d)%n",
                Thread.currentThread().getName(), task, queue.size(), capacity);

        // Notify producer threads that space has become available
        notifyAll();
        return task;
    }

    public synchronized void signalShutdown() {
        this.shutdown = true;
        notifyAll();
    }

    public synchronized int size() {
        return queue.size();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}
