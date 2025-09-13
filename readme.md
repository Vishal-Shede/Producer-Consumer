# Producer–Consumer Problem in Java

## 🔹 Step 1: Problem Statement
The **Producer–Consumer problem** is a classic **multithreading problem** where two types of threads share a common buffer:

- **Producer** → generates data/items and puts them into the buffer.  
- **Consumer** → takes data/items out of the buffer for processing.  

The challenge is that both threads run independently, so we must **synchronize access to the shared buffer**.

---

## 🔹 Step 2: What Goes Wrong Without Synchronization
- Producer may try to **add when buffer is full** → causes overflow.  
- Consumer may try to **remove when buffer is empty** → causes underflow.  
- If both access at the same time → **race conditions**.  

👉 This version is shown in `PCDemo1.java`.  
It demonstrates the **problems** clearly in the program output.

---

## 🔹 Step 3: Solution Approach
We use **synchronization (`synchronized`, `wait()`, `notify()`)** so that:  
- Producer waits when buffer is full, resumes when consumer consumes.  
- Consumer waits when buffer is empty, resumes when producer produces.  

This ensures **smooth cooperation** between threads.

👉 This fixed version is implemented in `PCDemo2.java`.  

---

## 🔹 Step 4: What This Demonstrates
This implementation shows:  
- **Multithreading** → Producer & Consumer are independent threads.  
- **Synchronization** → Safe access to shared resources using `wait()` and `notify()`.  
- **Thread Cooperation** → Threads communicate instead of blocking each other permanently.  

---

## 🚩 Problems in Unsynchronized Version
1. **Producer keeps producing even when buffer is full.**  
   - Just prints a message instead of waiting.  
2. **Consumer keeps consuming even when buffer is empty.**  
   - Just prints a message instead of waiting.  
3. **Race Condition**  
   - Producer and Consumer run in parallel without locks.  
   - Output can go out of order (e.g., consumer removes before producer prints "Produced").  

---

## ✅ Fixes in Synchronized Version
1. **Producer waits when buffer is full** (`wait()`).  
   → Only resumes when consumer consumes and calls `notify()`.  
2. **Consumer waits when buffer is empty** (`wait()`).  
   → Only resumes when producer produces and calls `notify()`.  
3. **`synchronized` keyword** ensures mutual exclusion.  
   → Only one thread accesses buffer at a time.  
4. **`notify()`** ensures coordination.  
   → Producer wakes consumer, consumer wakes producer.  

---

## 📂 Files in This Repo
- `PCDemo1.java` → Version without synchronization (shows problems).  
- `PCDemo2.java` → Correct synchronized version using `wait()`/`notify()`.  

---

