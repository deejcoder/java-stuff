package today.doingit;

import java.util.concurrent.Semaphore;

public class Barber {

    /*
        The barber can only cut one person's hair at a time.
        Thus, only one permit (binary semaphore).
     */
    private final static int MAX_BARBER_CHAIR = 1;
    private final Semaphore chair = new Semaphore(1, true);
    //The waiting room belonging to the Barber
    private WaitingRoom waitingRoom;

    public Barber() {
    }

    /**
     * Sets the waiting room belonging to the Barber before starting
     * @param waitingRoom
     */
    public void setWaitingRoom(WaitingRoom waitingRoom) {
        this.waitingRoom = waitingRoom;
    }

    /**
     * Allows a Person to request a haircut.
     * @param person
     * @throws InterruptedException
     */
    public void requestHaircut(Person person) throws InterruptedException {
        waitingRoom.startWaiting(person);
        this.cutHair(person);
    }

    /**
     * The person requests their hair to be cut when they're next in line for a haircut.
     * @param person
     * @throws InterruptedException
     */
    protected void cutHair(Person person) throws InterruptedException {
        //Request the barber's chair for a haircut. Wait till it's free.
        chair.acquire();
        //The person can move into the chair, and has finished waiting
        waitingRoom.finishWaiting(person);

        System.out.println("Cutting " + person.getName() + "'s hair...");
        if(Math.random() < 0.5) {
            person.sleep(200);
        }
        finishHair(person);
    }

    /**
     * Called when the haircut has finished.
     * @param person
     */
    protected void finishHair(Person person) {
        System.out.println("Hair cut finished.");
        chair.release();
    }

}
