package part2;

import java.util.LinkedList;
import java.util.NoSuchElementException;

class Buffer_sync {
	LinkedList<Integer> queue;

	public Buffer_sync() {
		queue = new LinkedList<Integer>();
    }
	
	synchronized void write(int i) {

	    queue.add(i);
	}

	synchronized int read() {
		try {

			return queue.removeFirst();

		} catch(NoSuchElementException e) {
			// the buffer is empty!?
			return -1;
		}
	}
}

