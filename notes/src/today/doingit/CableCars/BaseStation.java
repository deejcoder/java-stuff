package today.doingit.CableCars;

import java.util.concurrent.Semaphore;

public class BaseStation extends Thread{

    private final CableCar car;

    public BaseStation(CableCar car) {
        this.car = car;
    }

    @Override
    public void run() {
        for (int i = 0; i < 500; i++) {

            //Tourists come at random times
            try {
                Thread.sleep((long)Math.random()*100);
            }
            catch(InterruptedException ie) {
                ie.printStackTrace();
            }

            car.buyTicket();
        }
    }
}
