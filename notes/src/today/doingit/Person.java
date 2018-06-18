package today.doingit;

/*
    A person is a customer that needs a haircut
 */
public class Person extends Thread {

    private final Barber barber;
    public Person(Barber barber) {
        this.barber = barber;
    }

    @Override
    public void run() {
        try {
            if(Math.random() < 0.5) {
                this.sleep(200);
            }
            barber.requestHaircut(this);
        }
        catch(InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
