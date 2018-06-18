package today.doingit;

/*
    This is the waiting room,
    where at most three customers may wait for a haircut.
 */
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class WaitingRoom {
    //Limit the amount of permits for the semaphore to three: MAX_CHAIRS
    private final static int MAX_CHAIRS = 5;
    private final Semaphore available = new Semaphore(MAX_CHAIRS, true);

    //Seats stores the customers sitting in x seat.
    private ArrayList<Person> seats = new ArrayList<Person>();


    public WaitingRoom() {
    }

    /**
     * A person is requesting a seat in the waiting room
     * @param person
     * @throws InterruptedException
     */
    public void startWaiting(Person person) throws InterruptedException {
        //Requests a seat, the person waits until one is free
        available.acquire();
        if(seats.size() >= MAX_CHAIRS) {
            System.out.println("There is an overflow of customers, and too little chairs.");
        }

        //Give the customer the seat, now it is available
        System.out.println(person.getName() + " is waiting for a haircut.");
        seats.add(person);

    }

    public void finishWaiting(Person person) {
        seats.remove(person);
        System.out.println(person.getName() + " has finished waiting and is getting their haircut.");
        available.release();
    }

}
