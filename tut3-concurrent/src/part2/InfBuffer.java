package part2;

public class InfBuffer {

	public static void main(String args[]) {

	    /*
	        Race conditions still exist (Consumer beats Producer at executing)
	        - either fix properly
	        - or, sleep all consumers for a longer period.
	     */
        Handler test = new Handler();
	    Buffer b = new Buffer();

	    Thread p1 = new Thread(new Producer(1, b, test));
        Thread p2 = new Thread(new Producer(-1, b, test));

        Thread c1 = new Thread(new Consumer(1, b, test));
        Thread c2 = new Thread(new Consumer(-1, b, test));

        p1.start();
        p2.start();

        c1.start();
        c2.start();

	}
}

class Handler {

    volatile int test = 0;
    public Handler() {}

}

