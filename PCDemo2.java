import java.util.*;

// Shared buffer class - thread-safe version with synchronized methods
class SharedBuffer {

    LinkedList<Integer> buffer = new LinkedList<>(); // buffer to store produced items
    int capacity; // maximum buffer size

    public SharedBuffer(int capacity) {
        this.capacity = capacity;
    }

    // Producer adds item into buffer
    public synchronized void produce(int value) throws InterruptedException {
        // If buffer is full, producer must wait
        while (buffer.size() == capacity) {
            wait(); // release lock and wait until notified
        }

        // Add item to buffer
        buffer.add(value);
        System.out.println("Produced: " + value);

        // Notify waiting consumer that item is available
        notify();
    }

    // Consumer removes item from buffer
    public synchronized int consume() throws InterruptedException {
        // If buffer is empty, consumer must wait
        while (buffer.isEmpty()) {
            wait(); // release lock and wait until notified
        }

        // Remove item from buffer
        int val = buffer.removeFirst();
        System.out.println("Consumed: " + val);

        // Notify waiting producer that space is available
        notify();

        return val;
    }
}

// Producer thread class
class Producer implements Runnable {
    SharedBuffer sharedBuffer;

    public Producer(SharedBuffer sharedBuffer) {
        this.sharedBuffer = sharedBuffer;
    }

    public void run() {
        int value = 0;
        try {
            // Produce 10 items
            for (int i = 0; i < 10; i++) {
                sharedBuffer.produce(value);
                value++;
                Thread.sleep(500); // simulate work
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Consumer thread class
class Consumer implements Runnable {
    SharedBuffer sharedBuffer;

    public Consumer(SharedBuffer sharedBuffer) {
        this.sharedBuffer = sharedBuffer;
    }

    public void run() {
        try {
            // Consume 10 items
            for (int i = 0; i < 10; i++) {
                sharedBuffer.consume();
                Thread.sleep(800); // simulate work
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Main class to start Producer and Consumer threads
class PCDemo2 {
    public static void main(String[] args) {
        SharedBuffer sharedBuffer = new SharedBuffer(5); // buffer capacity = 5

        Producer producer = new Producer(sharedBuffer);
        Consumer consumer = new Consumer(sharedBuffer);

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        // Start both threads
        producerThread.start();
        consumerThread.start();
    }
}

/*
------------------------------------------------------------
FIXES IN THIS SYNCHRONIZED VERSION:
------------------------------------------------------------
1. Producer waits when buffer is full (using wait()).
   -> Only resumes when consumer consumes and calls notify().

2. Consumer waits when buffer is empty (using wait()).
   -> Only resumes when producer produces and calls notify().

3. synchronized keyword ensures mutual exclusion.
   -> Only one thread accesses the buffer at a time.

4. notify() ensures coordination between threads.
   -> Producer wakes consumer, consumer wakes producer.

This version demonstrates the CORRECT solution to the
Producer–Consumer problem with synchronization.
------------------------------------------------------------
*/
