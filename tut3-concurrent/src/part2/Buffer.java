package part2;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReentrantLock;

class Buffer {
    LinkedList<Integer> queue;

    /*
        Use same lock for both since reading while writing = problem
        and since .removeFirst writes anyway...
     */
    private final ReentrantLock writeLock = new ReentrantLock();

    public Buffer() {
        queue = new LinkedList<Integer>();
    }

    public void write(int i)  {

        //Make scope of locks as minimal as possible
        writeLock.lock();
        try {
            queue.add(i);
        }
        finally {
            writeLock.unlock();
        }
    }

    public int read() {
        writeLock.lock();
        try {
            try {
                return queue.removeFirst();

            } catch (NoSuchElementException e) {
                // the buffer is empty!?
                return -1;
            }
        }
        finally {
            writeLock.unlock();
        }
    }
}

