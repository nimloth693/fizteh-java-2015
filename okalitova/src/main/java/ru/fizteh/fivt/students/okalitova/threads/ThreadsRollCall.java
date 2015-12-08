package ru.fizteh.fivt.students.okalitova.threads;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by nimloth on 06.12.15.
 */
public class ThreadsRollCall {
    private static boolean ready = false;
    private static boolean flag = false;
    private static CyclicBarrier barrier;
    private static volatile Random random = new Random(43);

    private static Runnable rollCall = () -> {
        while (!ready) {
            int randInt = random.nextInt(9);
            if (randInt == 0) {
                System.out.println("No");
                flag = true;
            } else {
                System.out.println("Yes");
            }
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    };

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);

        barrier = new CyclicBarrier(n, () -> {
            if (flag) {
                System.out.println("Are you ready?");
                ready = false;
                flag = false;
            } else {
                System.exit(0);
            }
        });

        System.out.println("Are you ready?");
        ArrayList<Thread> t = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            t.add(i, new Thread(rollCall));
            t.get(i).start();
        }
    }
}
