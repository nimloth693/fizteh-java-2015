package ru.fizteh.fivt.students.okalitova.threads;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by nimloth on 08.12.15.
 */
public class BlockingQueue<T> {
    private Queue<T> queue =  new LinkedList<T>();
    private static int maxQueueSize = 0;
    private ReentrantLock writingLock = new ReentrantLock();
    private ReentrantLock readingLock = new ReentrantLock();
    private Condition empty = writingLock.newCondition();
    private Condition nonEmpty = readingLock.newCondition();

    BlockingQueue(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
    }

    synchronized void offer(List<T> e) throws InterruptedException {
        writingLock.lock();
        while (e.size() + queue.size() > maxQueueSize) {
            try {
                empty.await();
            } catch (InterruptedException ex) {
                throw ex;
            }
        }
        for (T elem : e) {
            e.add(elem);
            nonEmpty.signal();
        }
        writingLock.unlock();
    }

    synchronized List<T> take(int n) throws InterruptedException {
        readingLock.lock();
        List<T> result = new LinkedList<T>();

        while (queue.size() < n) {
            try {
                nonEmpty.await();
            } catch (InterruptedException e) {
                throw e;
            }
        }

        for (int i = 0; i < n; ++i) {
            result.add(queue.element());
            queue.remove();
        }
        if (queue.size() == 0) {
            empty.signal();
        }
        readingLock.unlock();
        return result;
    }
}
