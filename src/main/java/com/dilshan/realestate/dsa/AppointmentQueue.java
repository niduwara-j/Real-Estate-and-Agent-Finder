package com.dilshan.realestate.dsa;

import com.dilshan.realestate.model.Appointment;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Custom Generic FIFO Linked Queue implementation for Appointment scheduling and moderation.
 * Time Complexity: Enqueue: O(1), Dequeue: O(1), Peek: O(1).
 */
public class AppointmentQueue {

    private static class Node {
        Appointment data;
        Node next;

        Node(Appointment data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node front;
    private Node rear;
    private int size;

    public AppointmentQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    /**
     * Enqueue a new appointment to the back of the line (O(1)).
     */
    public synchronized void enqueue(Appointment appointment) {
        if (appointment == null) return;
        Node newNode = new Node(appointment);
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    /**
     * Dequeue the earliest appointment from the front (O(1)).
     */
    public synchronized Appointment dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Appointment queue is empty.");
        }
        Appointment data = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return data;
    }

    /**
     * Peek at the next appointment without removing it (O(1)).
     */
    public synchronized Appointment peek() {
        if (isEmpty()) {
            return null;
        }
        return front.data;
    }

    public synchronized boolean isEmpty() {
        return front == null;
    }

    public synchronized int size() {
        return size;
    }

    /**
     * Convert entire queue to List for UI rendering.
     */
    public synchronized List<Appointment> toList() {
        List<Appointment> list = new ArrayList<>();
        Node current = front;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }

    public synchronized void clear() {
        front = null;
        rear = null;
        size = 0;
    }
}
