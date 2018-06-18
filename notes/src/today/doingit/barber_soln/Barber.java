package today.doingit.barber_soln;

import java.util.concurrent.Semaphore;

public class Barber implements Runnable{

	Semaphore customerWaiting, seats, barberSleeping;
	
	Barber(Semaphore customerWaiting, Semaphore seats, Semaphore barberSleeping) {
		this.customerWaiting = customerWaiting;
		this.seats = seats;
		this.barberSleeping = barberSleeping;
	}
	
	public void run() {
		
		while (true){
			
			// Get a customer, sleep otherwise
			try {
			    //Is there at least one customer waiting?
				customerWaiting.acquire();
			} catch (InterruptedException e) {}

			// Cut the hair of the customer	
			System.out.println("Cutting Hair");
			//Notify threads that are waiting to acquire, that the haircut is finished.
			barberSleeping.release();			
			
		}
	}
}
