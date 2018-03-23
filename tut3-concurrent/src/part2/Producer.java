package part2;

class Producer implements Runnable {
	Buffer buffer;
	int id;

	Handler handler;

	public Producer(int id, Buffer b, Handler test) {
		this.id = id;
		buffer = b;

		this.handler = test;
	}

	public void run() {

	    int flag = 0;

	    //There should be two of each value between 1..10 in the queue
        for (int i = 1; i < 10; i++) {

            buffer.write(i);

            /*
                Everything below here is redundant
                This was to simulate 'fairness', allowing a writer to start first.
                However, a writer may take longer to execute than a reader at any time.
                Hence, this didn't work.

                Alternatively, I need to detect if the queue is empty, and not read.
                I'll use busy-wait for now...
             */
            if(flag == 0) {
                while (handler.test == 0) { } //Atomic
                flag = 1;
            }

        }
    }
}

