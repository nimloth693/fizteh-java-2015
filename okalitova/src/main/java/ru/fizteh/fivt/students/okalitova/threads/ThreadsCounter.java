package ru.fizteh.fivt.students.okalitova.threads;

import com.sun.corba.se.impl.orbutil.concurrent.Mutex;

import java.util.ArrayList;

/**
 * Created by nimloth on 06.12.15.
 */

public class ThreadsCounter {
    private static int counter = -1;
    private static Mutex mutex = new Mutex();

    private static Runnable countThread = () -> {
        String threadName = Thread.currentThread().getName();
        boolean ready = false;
        while (!ready) {
            synchronized (mutex) {
                if (counter + 1 == Integer.parseInt(threadName)) {
                    counter++;
                    System.out.println("Thread-" + counter);
                    ready = true;
                }
            }
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
