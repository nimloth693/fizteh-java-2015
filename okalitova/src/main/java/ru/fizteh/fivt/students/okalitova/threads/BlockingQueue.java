package ru.fizteh.fivt.students.okalitova.threads;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by nimloth on 08.12.15.
 */
public class BlockingQueue<T> {
    private Queue<T> queue =  new LinkedList<T>();
    private static int maxQueueSize = 0;
    private ReentrantLock lock = new ReentrantLock();
    private Condition empty = lock.newCondition();
    private Condition nonEmpty = lock.newCondition();

    public BlockingQueue(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
    }

    public synchronized void offer(List<T> e) throws InterruptedException {
        try {
            lock.lock();
            while (e.size() + queue.size() > maxQueueSize) {
                try {
                    boolean flag = empty.await(5, TimeUnit.SECONDS); //таймаут 5 секунд
                    if (!flag) {
                        return;
                    }
                } catch (InterruptedException ex) {
                    throw ex;
                }
            }
            for (T elem : e) {
                queue.add(elem);
            }
            if (queue.size() > 0) {
                nonEmpty.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public synchronized List<T> take(int n) throws InterruptedException {
        try {
            lock.lock();
            List<T> result = new LinkedList<T>();

            while (queue.size() < n) {
                try {
                    boolean flag = nonEmpty.await(5, TimeUnit.SECONDS); //таймаут 5 секунд
                    if (!flag) {
                        return result;
                    }
                } catch (InterruptedException e) {
                    throw e;
                }
            }

            for (int i = 0; i < n; ++i) {
                result.add(queue.remove());
            }
            empty.signal();
            return result;
        } finally {
            lock.unlock();
        }
    }
}
