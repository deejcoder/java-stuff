package today.doingit;

public class Supplier extends Thread {

    private boolean done;
    private final static int WEEK = 1000; //1 week = 1s

    //How frequently does this supplier supply goods? In weeks
    private int frequency;

    /*
        What type of supplier is this?
     */
    enum Type {
        SUPPLY_WOOD("Wood"),
        SUPPLY_CANDLE("Candles");

        private String name;
        Type(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
    private Type type;
    //====================

    //Hey team! This is the shed we're storing the goods.
    private final Storage storeShed;

    /**
     *
     * @param type - the Type of supply this supplier supplies
     * @param frequency - how often does this supplier supply 100 supplies, in weeks?
     * @param storeShed - the storage shed to store the supplies
     */
    public Supplier(Type type, int frequency, Storage storeShed) {
        done = false;

        this.frequency = frequency;
        this.type = type;
        this.storeShed = storeShed;
    }

    /*
        RUN: THREAD (implements Monitors)
     */
    @Override
    public synchronized void run() {
        //Need to put this in a loop to avoid unexpected awakenings
        while(true) {
            try {
                if(!done) {

                    //Store some supplies in the shed.
                    switch(type) {
                        case SUPPLY_WOOD: storeShed.addWood(100); break;
                        case SUPPLY_CANDLE: storeShed.addCandles(100); break;
                    }

                    //System.out.println("Another week...I have supplied 100 " + type.toString());

                    //Wait another x week(s).
                    wait(WEEK*frequency);
                }
                else {
                    break;
                }
            }
            catch(InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    public synchronized void toggleSupply(boolean status) {
        done = !status;
        if(done) {
            //Notify the supply thread to stop
            notify();

            System.out.println("The supplier supplying " + type.toString() + " has been notified to stop.");
        }
    }
}
