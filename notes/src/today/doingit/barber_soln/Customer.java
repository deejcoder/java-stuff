package today.doingit.barber_soln;
import java.util.concurrent.Semaphore;

public class Customer implements Runnable{

	Semaphore customerWaiting, seats, barberSleeping;

	boolean cut = false;
	Customer(Semaphore customerWaiting, Semaphore seats, Semaphore barberSleeping) {
	    //When customerWaiting is released, there is at least one customer waiting.
		this.customerWaiting = customerWaiting;
		//The amount of seats = 3
		this.seats = seats;
		//Is the Barber sleeping (from no customers)?
		this.barberSleeping = barberSleeping;

	}

	/*
	    1.  A customer walks in and tries to sit down. Otherwise, they wait.
	    2.  The waiting customer then tries to request the Barber to do their haircut.
	    3.  If the Barber is sleeping, wake him up (releasing customerWaiting)
	    4.  Request haircut
	 */
	public void run() {
		while (!cut) {
			
			// A random delay
			// Don't want all the threads trying at once!
			try {
				Thread.sleep((long)(Math.random()*100));
			} catch (InterruptedException e1) {}


			// Try to get a seat in the waiting room
			try {
				seats.acquire();
			} catch (InterruptedException e) {}
			System.out.println(seats.availablePermits());

			System.out.println(Thread.currentThread().getName()+" is sitting down");
			// Try and wake barber
			customerWaiting.release();


			// Get hair cut
			try {
				barberSleeping.acquire();
			} catch (InterruptedException e) {}
			cut = true;
			seats.release();
			
		}
		System.out.println(Thread.currentThread().getName()+" has had hair cut");		
	}
}
