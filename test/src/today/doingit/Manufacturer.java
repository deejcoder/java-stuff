package today.doingit;


public class Manufacturer extends Thread {

    private final Storage storeShed;
    private final Supplier wood;
    private final Supplier candle;

    public Manufacturer(Storage storeShed, Supplier wood, Supplier candle) {
        this.storeShed = storeShed;

        this.wood = wood;
        this.candle = candle;

    }

    @Override
    public void run() {
        //Produce 1000 Candle Holders
        for(int made = 0; made < 1000; made++) {
            //Produce a Candle Holder
            produceHolder();
            if(made % 200 == 0) {
                System.out.println("Sir! We have made " + made + " Candle Holders!");
            }

        }

        System.out.println("Sir! We have made 1,000 Candle Handlers!");
        //Stop the supply threads (notify them)
        wood.toggleSupply(false);
        candle.toggleSupply(false);
    }

    private void produceHolder() {

        //Take the required resources per candle holder
        try {
            storeShed.takeWood(1);
            storeShed.takeCandles(3);
        }
        catch(InterruptedException ie) {
            ie.printStackTrace();
        }

        //It takes some unknown random time to produce candle holders,
        try {
            sleep((long) (Math.random() * 60));
        }
        catch(InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
