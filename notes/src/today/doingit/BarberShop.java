package today.doingit;

/*
    Implementation of semaphores.
 */

public class BarberShop {

    public static void main(String[] args) {
	    Barber barber = new Barber();
	    WaitingRoom waitingRoom = new WaitingRoom();
	    barber.setWaitingRoom(waitingRoom);

	    Person person1 = new Person(barber);
        Person person2 = new Person(barber);
        Person person3 = new Person(barber);
        Person person4 = new Person(barber);
        Person person5 = new Person(barber);
        Person person6 = new Person(barber);
        Person person7 = new Person(barber);
        Person person8 = new Person(barber);
        Person person9 = new Person(barber);
        Person person10 = new Person(barber);

        person1.start();
        person2.start();
        person3.start();
        person4.start();
        person5.start();
        person6.start();
        person7.start();
        person8.start();
        person9.start();
        person10.start();

    }
}
