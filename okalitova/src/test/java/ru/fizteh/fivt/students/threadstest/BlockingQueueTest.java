package ru.fizteh.fivt.students.threadstest;

import org.junit.Test;
import ru.fizteh.fivt.students.okalitova.threads.BlockingQueue;
import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * Created by nimloth on 10.12.15.
 */
public class BlockingQueueTest {

    @Test
    public void offerBLockTest() throws InterruptedException {
        BlockingQueue<Integer> blockingQueue = new BlockingQueue<Integer>(5);
        List<Integer> e1 = Arrays.asList(1, 2, 3, 4, 5, 6);
        Thread thread1 = new Thread(() -> {
            try {
                blockingQueue.offer(e1);
            } catch (InterruptedException e) {
                e.getMessage();
            }
        });
        thread1.start();
        sleep(3);
        assertThat(thread1.getState().equals(Thread.State.TIMED_WAITING), is(true));

        List<Integer> e2 = Arrays.asList(1, 2, 3, 4, 5, 6);
        Thread thread2 = new Thread(() -> {
            try {
                blockingQueue.offer(e2);
            } catch (InterruptedException e) {
                e.getMessage();
            }
        });
        thread2.start();
        sleep(3);
        assertThat(thread2.getState().equals(Thread.State.BLOCKED), is(true));
    }

    @Test
    public void offerNonBLockTest() throws InterruptedException {
        BlockingQueue<Integer> blockingQueue = new BlockingQueue<Integer>(5);
        List<Integer> e1 = Arrays.asList(1, 2);
        Thread thread1 = new Thread(() -> {
            try {
                blockingQueue.offer(e1);
            } catch (InterruptedException e) {
                e.getMessage();
            }
        });
        thread1.start();
        sleep(3);
        assertThat(thread1.getState().equals(Thread.State.TERMINATED), is(true));

        List<Integer> e2 = Arrays.asList(3, 4);
        Thread thread2 = new Thread(() -> {
            try {
                blockingQueue.offer(e2);
            } catch (InterruptedException e) {
                e.getMessage();
            }
        });
        thread2.start();
        sleep(3);
        assertThat(thread2.getState().equals(Thread.State.TERMINATED), is(true));
    }

    @Test
    public void takeBLockTest() throws InterruptedException {
        BlockingQueue<Integer> blockingQueue = new BlockingQueue<Integer>(5);
        List<Integer> e1 = Arrays.asList(1, 2);
        blockingQueue.offer(e1);
        List<Integer> e2 = Arrays.asList(3, 4);
        blockingQueue.offer(e2);
        Thread thread = new Thread(() -> {
            try {
                blockingQueue.take(5);
            } catch (InterruptedException e) {
                e.getMessage();
            }
        });
        thread.start();
        sleep(3);
        assertThat(thread.getState().equals(Thread.State.TIMED_WAITING), is(true));
    }

    @Test
    public void takeNonBLockTest() throws InterruptedException {
        BlockingQueue<Integer> blockingQueue = new BlockingQueue<Integer>(5);
        List<Integer> e1 = Arrays.asList(1, 2);
        blockingQueue.offer(e1);
        List<Integer> e2 = Arrays.asList(3, 4);
        blockingQueue.offer(e2);
        List <Integer> result = blockingQueue.take(3);
        assertThat(result.size(), is(3));
        assertThat(result, contains(1, 2, 3));
    }
}
