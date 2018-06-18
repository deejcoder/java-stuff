package today.doingit.CableCars;

public class MountainStation extends Thread {

    private final CableCar car;

    public MountainStation(CableCar car) {
        this.car = car;
    }

    @Override
    public void run() {
        for(int i = 0; i <= 500; i++) {

            //Tourists decide to leave the mountain at random times
            try {
                Thread.sleep((long)Math.random()*100);
            }
            catch(InterruptedException ie) {
                ie.printStackTrace();
            }

            car.leaveMountain();
        }
    }
}
