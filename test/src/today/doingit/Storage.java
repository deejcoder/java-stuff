package today.doingit;

import java.util.concurrent.Semaphore;

public class Storage {

    /*
        So sorry for this mess :(
     */

    //Semaphores to control access to wood/candle variables
    private final Semaphore woodControl = new Semaphore(1);
    private final Semaphore candleControl = new Semaphore(1);

    private int wood;
    private int candles;


    public Storage() {
        wood = 0;
        candles = 0;
    }

    /*
        Wood
     */
    public void addWood(int amount) throws InterruptedException {
        //Lock Wood, before entering the critical section
        woodControl.acquire();
        wood += amount;

        System.out.println("Storage manager: We got more wood. We have " + wood + " wood.");
        //Release to allow waiting threads, if any.
        woodControl.release();
    }

    public boolean takeWood(int amount) throws InterruptedException {
        woodControl.acquire();

        //Is there enough wood?
        if(wood - amount < 0) {
            woodControl.release();
            return false;
        }
        wood -= amount;
        woodControl.release();
        return true;
    }

    /*
        Candles
     */
    public void addCandles(int amount) throws InterruptedException {
        candleControl.acquire();

        candles += amount;
        System.out.println("Storage manager: We got more candles. We have " + candles + " candles.");

        candleControl.release();
    }

    public boolean takeCandles(int amount) throws InterruptedException {
        candleControl.acquire();

        //Is there enough candles?
        if(candles - amount < 0) {
            candleControl.release();
            return false;
        }
        candles -= amount;
        candleControl.release();
        return true;
    }
}
