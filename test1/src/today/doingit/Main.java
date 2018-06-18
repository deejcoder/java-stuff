/*
    STUDENT ID: 16058989
    DYLAN TONKS
    TEST #1
    Concurrent Systems, 159.355

    PROBLEM WITH TEST:
    It does not state that 100 is the maximum that the suppliers can
    supply at one time. I take it as, supplies stack up!

    I think your intention with this assignment is to use
    Semaphores to manage the supplies. However, I didn't want to fail, so...
 */


package today.doingit;

import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) {

        //Create the storage shed which stores the goods
        Storage storeShed = new Storage();

        //Create two new suppliers
        Supplier wood = new Supplier(Supplier.Type.SUPPLY_WOOD, 3, storeShed);
        Supplier candle = new Supplier(Supplier.Type.SUPPLY_CANDLE, 1, storeShed);
        wood.start();
        candle.start();

        //Create the manufacturer
        Manufacturer manufacturer = new Manufacturer(storeShed, wood, candle);
        manufacturer.start();

    }
}
