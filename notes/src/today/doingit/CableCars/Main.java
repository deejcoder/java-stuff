package today.doingit.CableCars;

public class Main {
    public static void main(String[] args) {

        CableCar car = new CableCar();
        BaseStation baseStation = new BaseStation(car);
        MountainStation mountainStation = new MountainStation(car);
        car.start();
        baseStation.start();
        //mountainStation.start();
    }
}
