package today.doingit.barber_soln;

import java.util.concurrent.Semaphore;

public class Runner {

	public static void main(String[] args) {
		
		Semaphore barberSleeping = new Semaphore(1);
		Semaphore customerWaiting = new Semaphore(1);
		try {
		    //It is assumed that there are no customers
			customerWaiting.acquire();
			//Therefore, the Barber is also sleeping
			barberSleeping.acquire();

		} catch (InterruptedException e) {
			// This is dangerous because if there is an InterruptedException then the
			// semaphores will not be acquired, but the thread will continue as if
			// they were!  How could you fix this?
		}

		//Three chairs in the waiting room
		Semaphore seats = new Semaphore(3);

		//Initialize the Barber
		Barber bar = new Barber(customerWaiting,seats,barberSleeping);
		Thread bThread = new Thread(bar);
		bThread.start();

		//Add 30 customers
		int nCust = 30;
		Customer customers[] = new Customer[nCust];
		Thread cThread[] = new Thread[nCust];
		
		for (int i=0;i<nCust;i++) {
			 customers[i] = new Customer(customerWaiting,seats,barberSleeping);
			 cThread[i] = new Thread(customers[i]);
			 cThread[i].start();
		}

		// This program does not terminate even after all the customers have had
		// their hair cut. Why?
	}

}
