import java.util.*;

// Shared buffer class - acts as the common resource for producer and consumer
class SharedBuffer {

    LinkedList<Integer> buffer = new LinkedList<>(); // buffer to store produced items
    int capacity; // maximum buffer size

    public SharedBuffer(int capacity) {
        this.capacity = capacity;
    }

    // Producer adds items into buffer
    void produce(int value) {
        // Problem: when buffer is full, producer just prints a message and continues
        // It does NOT wait, which causes incorrect behavior
        if (buffer.size() == capacity) {
            System.out.println("Buffer is FULL! Producer can't add item");
        } else {
            buffer.add(value);
            System.out.println("Produced: " + value);
        }
    }

    // Consumer removes items from buffer
    int consume() {
        // Problem: when buffer is empty, consumer just prints a message and continues
        // It does NOT wait, which causes incorrect behavior
        if (buffer.isEmpty()) {
            System.out.println("Buffer is EMPTY! Consumer can't remove item");
            return -1;
        } else {
            int val = buffer.removeFirst();
            System.out.println("Consumed: " + val);
            return val;
        }
    }
}

// Producer thread class
class Producer implements Runnable {
    SharedBuffer sharedBuffer;

    public Producer(SharedBuffer sharedBuffer) {
        this.sharedBuffer = sharedBuffer;
    }

    // Produces 10 items and tries to add them into buffer
    public void run() {
        int value = 0;
        int i = 0;
        while (i < 10) {
            sharedBuffer.produce(value);
            value++;
            i++;
        }
    }
}

// Consumer thread class
class Consumer implements Runnable {
    SharedBuffer sharedBuffer;

    public Consumer(SharedBuffer sharedBuffer) {
        this.sharedBuffer = sharedBuffer;
    }

    // Consumes 10 items from buffer
    public void run() {
        int i = 0;
        while (i < 10) {
            sharedBuffer.consume();
            i++;
        }
    }
}

// Main class to start Producer and Consumer threads
class PCDemo1 {
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
PROBLEMS IN THIS UNSYNCHRONIZED VERSION:
------------------------------------------------------------
1. Producer keeps producing even when buffer is full.
   -> Just prints a message instead of waiting.

2. Consumer keeps consuming even when buffer is empty.
   -> Just prints a message instead of waiting.

3. Race Condition:
   - Producer and Consumer run in parallel without synchronization.
   - This may lead to out-of-order outputs (e.g., Consumer consumes
     an item before Producer's "Produced" message is printed).

This version demonstrates WHY synchronization (synchronized,
wait, notify) is needed in the Producer–Consumer problem.
------------------------------------------------------------
*/
