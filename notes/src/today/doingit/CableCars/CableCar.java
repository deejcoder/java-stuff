package today.doingit.CableCars;


import java.util.concurrent.Semaphore;

public class CableCar extends Thread {

    private final Semaphore seatsUp = new Semaphore(10);
    private final Semaphore seatsDown = new Semaphore(10);
    private final Semaphore waiting = new Semaphore(50);

    private int visitorsOnMountain;
    private int totalVisitors;

    public void CableCar() {
        visitorsOnMountain = 0;
        totalVisitors = 0;
    }

    @Override
    public void run() {

        while(totalVisitors <= 500) {

            System.out.println("Cable car is at the Base Station.");
            //Wait at the Base Station
            try {
                Thread.sleep(1000);
            }
            catch(InterruptedException ie) {
                ie.printStackTrace();
            }

            System.out.println("There are " + (10-seatsUp.availablePermits()) + " tourists on the cable car.");
            //Go up the mountain
            //..\../..\../
            //ARRIVED! All passengers, disembark!
            //Release all visitors, add to # visitors on the mountain
            synchronized(seatsUp) {
                while ((10 - seatsUp.availablePermits()) > 0) {
                    //Maximum of 50 on the mountain at a time
                    /*if(visitorsOnMountain >= 50) {
                        break;
                    }
                    seatsUp.release();*/
                    try {
                        waiting.acquire();
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    System.out.println(visitorsOnMountain);
                    visitorsOnMountain++;
                }
            }
            System.out.println(visitorsOnMountain);

            //Wait at the top of the mountain
            try {
                Thread.sleep(1000);
            }
            catch(InterruptedException ie) {
                ie.printStackTrace();
            }

            //Going down
            //..\../..\../
            //TRIP OVER!
            //Release all passengers
            while(seatsDown.availablePermits() > 0) {
                seatsDown.release();
                visitorsOnMountain--;

                //Maintain a count of total visitors
                totalVisitors++;
            }

        }
    }


    public void buyTicket() {
        try {
            seatsUp.acquire();
        }
        catch(InterruptedException ie) {
            ie.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + "Purchases a ticket and hops on the cable car.");
    }

    public void leaveMountain() {
        while(waiting.availablePermits() > 0) {
            try {
                seatsDown.acquire();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + " hops on the cable car to leave the mountain.");
            waiting.release();
            return;
        }
    }
}
