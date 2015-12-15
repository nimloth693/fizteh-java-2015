package ru.fizteh.fivt.students.okalitova.threads;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by nimloth on 06.12.15.
 */

public class ThreadsCounter {
    private static int counter = -1;
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition cond = lock.newCondition();

    private static Runnable countThread = () -> {
        try {
            String threadName = Thread.currentThread().getName();
            lock.lock();

            while (counter + 1 != Integer.parseInt(threadName)) {
                try {
                    cond.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            counter++;
            System.out.println("Thread-" + counter);
            cond.signalAll();
        } finally {
            lock.unlock();
        }
    };

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        System.out.println(n);
        ArrayList<Thread> t = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            t.add(i, new Thread(countThread, String.valueOf(i)));
            t.get(i).start();
        }
    }
}
