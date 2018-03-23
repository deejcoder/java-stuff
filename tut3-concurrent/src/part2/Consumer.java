package part2;

class Consumer implements Runnable {
	Buffer buffer;
	int id;

	Handler handler;

	public Consumer(int id, Buffer b, Handler test) {
		this.id = id;
		buffer = b;

		this.handler = test;
	}

	public void run() {

	    int hasSet = 0;

        for (int i = 1; i < 10; i++) {

            //throws NoSuchElementException -> handles by returning -1
            int value = buffer.read();

            //queue is empty, busy wait
            if(value == -1) {
                i--;
                continue;
            }

            System.out.println("Consume ID: " + id + " " + value);

            /*
                Only allow this block to run once, rather than per iteration
                Tells the Producers that it's read some data so they can resume.
                This would be deadlock, if this thread was never to never give access to Producers
             */
            if(hasSet == 0) {
                handler.test = 1; //Atomic
                hasSet = 1;
            }
        }
	}
}

